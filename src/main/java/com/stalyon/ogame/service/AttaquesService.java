package com.stalyon.ogame.service;

import com.stalyon.ogame.OgameApiService;
import com.stalyon.ogame.constants.OgameCst;
import com.stalyon.ogame.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpServerErrorException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Profile("autoAttack")
public class AttaquesService {

    private static Logger LOGGER = LoggerFactory.getLogger(AttaquesService.class);

    @Value("${attaques.auto.coord.galaxy}")
    private Integer coordGalaxy;

    @Value("${attaques.auto.coord.system.min}")
    private Integer coordSystemMin;

    @Value("${attaques.auto.coord.system.max}")
    private Integer coordSystemMax;

    @Value("${attaques.auto.planet}")
    private Integer autoPlanetId;

    @Value("${attaques.auto.cargo.stockage}")
    private Integer cargoStockage;

    @Value("${attaques.auto.cargo.id}")
    private Integer cargoId;

    @Value("${attaques.auto.minimal.resources}")
    private Integer minimalResources;

    @Autowired
    private OgameApiService ogameApiService;

    private List<InactivePlanetsDto> inactivesToSpy = new ArrayList<>();
    private List<InactivePlanetsDto> inactivesToSort = new ArrayList<>();
    private List<InactivePlanetsDto> inactivesToAttack = new ArrayList<>();
    private Boolean scanSystemsInit = Boolean.FALSE;

    @EventListener(ApplicationReadyEvent.class)
    public void scanSystems() {
        LOGGER.info("Scan des systèmes de " + this.coordGalaxy + ":"
                + this.coordSystemMin + " à " + this.coordGalaxy + ":"
                + this.coordSystemMax);

        List<InactivePlanetsDto> inactivesPlanets = new ArrayList<>();
        this.inactivesToSpy = new ArrayList<>();

        for (int i=this.coordSystemMin; i<this.coordSystemMax; i++) {
            GalaxyInfosDto galaxyInfos = this.ogameApiService.getGalaxyInfos(this.coordGalaxy, i);
            inactivesPlanets.addAll(
                    galaxyInfos.getPlanets()
                            .stream()
                            .filter(Objects::nonNull)
                            .filter(this::isInactive)
                            .map(gp -> new InactivePlanetsDto(gp.getCoordinate()))
                            .collect(Collectors.toList())
            );
        }

        this.inactivesToSpy = inactivesPlanets;
        this.scanSystemsInit = Boolean.TRUE;
        LOGGER.info("Fin du scan. Planètes inactives trouvées : " + this.inactivesToSpy.size());
    }

    @Scheduled(cron = "0 0/3 * * * *") // every 3-minutes
    public void spyInactives() {
        SlotsDto slots = this.ogameApiService.getSlots();

        int i = 0;
        boolean atLeastOnce = false;
        while (!this.inactivesToSpy.isEmpty()  && slots.getExpTotal().equals(slots.getExpInUse())
                && slots.getInUse() +2 < slots.getTotal() && i < this.inactivesToSpy.size()) {
            CoordinateDto coords = this.inactivesToSpy.get(i).getCoordinate();

            // Espionnage
            MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
            formData.add("ships", OgameCst.ESPIONAGE_PROBE_ID + "," + "8");
            formData.add("mission", OgameCst.SPY.toString());
            formData.add("speed", OgameCst.HUNDRED_PERCENT.toString());
            formData.add("galaxy", coords.getGalaxy().toString());
            formData.add("system", coords.getSystem().toString());
            formData.add("position", coords.getPosition().toString());
            formData.add("metal", "0");
            formData.add("crystal", "0");
            formData.add("deuterium", "0");
            this.ogameApiService.sendFleet(this.autoPlanetId, formData);

            slots = this.ogameApiService.getSlots();
            i++;
            atLeastOnce = true;
        }

        if (atLeastOnce) {
            for (int j = i-1; j > -1; j--) {
                this.inactivesToSort.add(this.inactivesToSpy.get(j));
                this.inactivesToSpy.remove(j);
            }

            LOGGER.info("Inactifs sondés : " + i);
        }
    }

    @Scheduled(cron = "0 1/3 * * * *") // every 3-minutes
    public void sortInactives() {
        List<InactivePlanetsDto> inactivesToAttack = new ArrayList<>();

        Iterator<InactivePlanetsDto> inactivesToSortIterator = this.inactivesToSort.iterator();
        while (inactivesToSortIterator.hasNext()) {
            InactivePlanetsDto inactive = inactivesToSortIterator.next();

            try {
                EspionageReportDto espionageReport = this.ogameApiService.getEspionageReport(inactive.getCoordinate().getGalaxy(),
                        inactive.getCoordinate().getSystem(), inactive.getCoordinate().getPosition());

                if (this.noDefense(espionageReport) && this.noFleet(espionageReport)) {
                    inactivesToAttack.add(new InactivePlanetsDto(
                            inactive.getCoordinate(),
                            new PlanetsResourcesDto(
                                    espionageReport.getMetal(),
                                    espionageReport.getCrystal(),
                                    espionageReport.getDeuterium(),
                                    null
                            )
                    ));

                    inactivesToSortIterator.remove();
                }
            } catch (HttpServerErrorException e) {
                // Ignore
            }
        }

        // Filtrage et tri
        this.inactivesToAttack.addAll(inactivesToAttack
                .stream()
                .filter(inactive -> inactive.getResources() != null)
                .filter(inactive -> (inactive.getResources().getMetal() + inactive.getResources().getCrystal() + inactive.getResources().getDeuterium()) > this.minimalResources)
                .sorted((i1, i2) -> (i2.getResources().getMetal() + i2.getResources().getCrystal() + i2.getResources().getDeuterium()) - (i1.getResources().getMetal() + i1.getResources().getCrystal() + i1.getResources().getDeuterium()))
                .collect(Collectors.toList()));

        this.inactivesToSort = new ArrayList<>();

        LOGGER.info("Inactifs dans la liste d'attaque : " + this.inactivesToAttack.size());
    }

    @Scheduled(cron = "0 2/3 * * * *") // every 3-minutes
    public void attackInactives() {
        SlotsDto slots = this.ogameApiService.getSlots();

        int i = 0;
        boolean atLeastOnce = false;
        while (!this.inactivesToAttack.isEmpty() && slots.getExpTotal().equals(slots.getExpInUse())
                && slots.getInUse() +2 < slots.getTotal() && i < this.inactivesToAttack.size()) {
            CoordinateDto coords = this.inactivesToAttack.get(i).getCoordinate();
            PlanetsResourcesDto resources = this.inactivesToAttack.get(i).getResources();
            Integer totalResources = (3 * (resources.getMetal() + resources.getCrystal() + resources.getCrystal() + resources.getDeuterium())) / 4;
            Integer nbCargo = totalResources / this.cargoStockage;

            ShipsDto ships = this.ogameApiService.getShips(this.autoPlanetId);

            if (this.cargoId.equals(OgameCst.LARGE_CARGO_ID) && ships.getLargeCargo() >= nbCargo
                    || this.cargoId.equals(OgameCst.SMALL_CARGO_ID) && ships.getSmallCargo() >= nbCargo) {

                // Attaque
                MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
                formData.add("ships", this.cargoId + "," + nbCargo);
                formData.add("mission", OgameCst.ATTACK.toString());
                formData.add("speed", OgameCst.HUNDRED_PERCENT.toString());
                formData.add("galaxy", coords.getGalaxy().toString());
                formData.add("system", coords.getSystem().toString());
                formData.add("position", coords.getPosition().toString());
                formData.add("metal", "0");
                formData.add("crystal", "0");
                formData.add("deuterium", "0");
                this.ogameApiService.sendFleet(this.autoPlanetId, formData);

                LOGGER.info("Attaque de l'inactif " + coords.getGalaxy() + ":" + coords.getSystem() + ":" + coords.getPosition()
                        + " pour un total de " + totalResources + " ressources");

                slots = this.ogameApiService.getSlots();

                if ((resources.getMetal() + resources.getCrystal() + resources.getCrystal() + resources.getDeuterium()) / 4 > this.minimalResources) {
                    // Cas où il est possible de faire plusieurs vagues
                    resources.setMetal(resources.getMetal() / 4);
                    resources.setCrystal(resources.getCrystal() / 4);
                    resources.setDeuterium(resources.getDeuterium() / 4);
                } else {
                    // Sinon, passage à l'inactif suivant
                    i++;
                }
                atLeastOnce = true;
            }
        }

        if (atLeastOnce) {
            for (int j = i-1; j > -1; j--) {
                this.inactivesToAttack.remove(j);
            }
        }
    }

    @Scheduled(cron = "0 0 0/2 * * *") // every 2-hour
    public void scanSystemsScheduler() {
        if (this.scanSystemsInit) {
            this.scanSystems();
        }
    }

    private Boolean isInactive(GalaxyPlanetsDto galaxyPlanets) {
        return galaxyPlanets.getInactive() && !galaxyPlanets.getAdministrator() && !galaxyPlanets.getBanned()
                && !galaxyPlanets.getStrongPlayer() && !galaxyPlanets.getNewbie() && !galaxyPlanets.getVacation();
    }

    private Boolean noFleet(EspionageReportDto espionageReport) {
        return espionageReport.getHasFleetInformation()
                && (espionageReport.getLightFighter() == null || espionageReport.getLightFighter() == 0)
                && (espionageReport.getHeavyFighter() == null || espionageReport.getHeavyFighter() == 0)
                && (espionageReport.getCruiser() == null || espionageReport.getCruiser() == 0)
                && (espionageReport.getBattleship() == null || espionageReport.getBattleship() == 0)
                && (espionageReport.getBattlecruiser() == null || espionageReport.getBattlecruiser() == 0)
                && (espionageReport.getBomber() == null || espionageReport.getBomber() == 0)
                && (espionageReport.getDestroyer() == null || espionageReport.getDestroyer() == 0)
                && (espionageReport.getDeathstar() == null || espionageReport.getDeathstar() == 0)
                && (espionageReport.getSmallCargo() == null || espionageReport.getSmallCargo() == 0)
                && (espionageReport.getLargeCargo() == null || espionageReport.getLargeCargo() == 0)
                && (espionageReport.getColonyShip() == null || espionageReport.getColonyShip() == 0)
                && (espionageReport.getRecycler() == null || espionageReport.getRecycler() == 0)
                && (espionageReport.getEspionageProbe() == null || espionageReport.getEspionageProbe() == 0)
                && (espionageReport.getCrawler() == null || espionageReport.getCrawler() == 0)
                && (espionageReport.getReaper() == null || espionageReport.getReaper() == 0)
                && (espionageReport.getPathfinder() == null || espionageReport.getPathfinder() == 0);
    }

    private Boolean noDefense(EspionageReportDto espionageReport) {
        return espionageReport.getHasDefensesInformation()
                && (espionageReport.getRocketLauncher() == null || espionageReport.getRocketLauncher() == 0)
                && (espionageReport.getLightLaser() == null || espionageReport.getLightLaser() == 0)
                && (espionageReport.getHeavyLaser() == null || espionageReport.getHeavyLaser() == 0)
                && (espionageReport.getGaussCannon() == null || espionageReport.getGaussCannon() == 0)
                && (espionageReport.getIonCannon() == null || espionageReport.getIonCannon() == 0)
                && (espionageReport.getPlasmaTurret() == null || espionageReport.getPlasmaTurret() == 0)
                && (espionageReport.getSmallShieldDome() == null || espionageReport.getSmallShieldDome() == 0)
                && (espionageReport.getLargeShieldDome() == null || espionageReport.getLargeShieldDome() == 0);
    }
}
