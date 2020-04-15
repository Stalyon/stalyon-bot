package com.stalyon.ogame.service;

import com.stalyon.ogame.OgameApiService;
import com.stalyon.ogame.constants.OgameCst;
import com.stalyon.ogame.dto.*;
import com.stalyon.ogame.utils.SlotsService;
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

import java.util.*;
import java.util.stream.Collectors;

@Service
@Profile("autoAttack")
public class AttaquesService {

    private static Logger LOGGER = LoggerFactory.getLogger(AttaquesService.class);

    @Value("${attaques.auto.coord.galaxy}")
    private List<Integer> COORD_GALAXY;

    @Value("${attaques.auto.coord.system.min}")
    private List<Integer> COORD_SYSTEM_MIN;

    @Value("${attaques.auto.coord.system.max}")
    private List<Integer> COORD_SYSTEM_MAX;

    @Value("${attaques.auto.planet}")
    private List<Integer> AUTO_PLANET_ID;

    @Value("${attaques.auto.planet.system}")
    private List<Integer> PLANET_SYSTEM;

    @Value("${attaques.auto.cargo.stockage}")
    private Integer CARGO_STOCKAGE;

    @Value("${attaques.auto.cargo.id}")
    private Integer CARGO_ID;

    @Value("${attaques.auto.minimal.resources}")
    private Integer MINIMAL_RESOURCES;

    @Value("${attaques.auto.spy.sondes.nb}")
    private Integer NB_SONDES;

    @Autowired
    private OgameApiService ogameApiService;

    @Autowired
    private SlotsService slotsService;

    private int index = -1;
    private List<InactivePlanetsDto> inactivesToSpy = new ArrayList<>();
    private List<InactivePlanetsDto> inactivesToSort = new ArrayList<>();
    private List<InactivePlanetsDto> inactivesToAttack = new ArrayList<>();
    private List<CoordinateDto> attackedInactives = new ArrayList<>();
    private Boolean isInUse = Boolean.TRUE;

    @EventListener(ApplicationReadyEvent.class)
    public void scanSystems() {
        this.index++;
        this.inactivesToSpy = new ArrayList<>();
        this.inactivesToSort = new ArrayList<>();
        this.inactivesToAttack = new ArrayList<>();

        if (this.AUTO_PLANET_ID.size() <= this.index) {
            this.index = 0;
        }
        
        LOGGER.info("Scan des systèmes de " + this.COORD_GALAXY.get(this.index) + ":"
                + this.COORD_SYSTEM_MIN.get(this.index) + " à " + this.COORD_GALAXY.get(this.index) + ":"
                + this.COORD_SYSTEM_MAX.get(this.index));

        List<InactivePlanetsDto> inactivesPlanets = new ArrayList<>();
        this.inactivesToSpy = new ArrayList<>();

        for (int i = this.COORD_SYSTEM_MIN.get(this.index); i<this.COORD_SYSTEM_MAX.get(this.index); i++) {
            GalaxyInfosDto galaxyInfos = this.ogameApiService.getGalaxyInfos(this.COORD_GALAXY.get(this.index), i);
            inactivesPlanets.addAll(
                    galaxyInfos.getPlanets()
                            .stream()
                            .filter(Objects::nonNull)
                            .filter(this::isInactive)
                            .map(gp -> new InactivePlanetsDto(gp.getCoordinate()))
                            .collect(Collectors.toList())
            );
        }

        // Tri des inactifs
        inactivesPlanets
                .sort(Comparator.comparingInt(i -> Math.abs(i.getCoordinate().getSystem() - this.PLANET_SYSTEM.get(this.index))));

        this.inactivesToSpy = inactivesPlanets;
        this.isInUse = Boolean.FALSE;
        LOGGER.info("Fin du scan. Planètes inactives trouvées : " + this.inactivesToSpy.size());

        if (this.inactivesToSpy.isEmpty()) {
            // Si aucun inactif n'a été trouvé, scan suivant
            this.scanSystems();
        }
    }

    @Scheduled(cron = "0/30 * * * * *") // every 30-seconds
    public void spyOrAttackInactives() {
        if (!this.isInUse) {
            this.isInUse = Boolean.TRUE;

            if (this.inactivesToSpy.isEmpty() && (!this.inactivesToAttack.isEmpty() || !this.inactivesToSort.isEmpty())) {
                // Tri des inactifs
                this.sortInactives();

                // Lancement des attaques
                this.attackInactives();

                if (this.inactivesToAttack.isEmpty()) {
                    this.scanSystems();
                }
            } else if (!this.inactivesToSpy.isEmpty()) {
                this.spyInactives();
            }

            this.isInUse = Boolean.FALSE;
        }
    }

    private void attackInactives() {
        LOGGER.info("Inactifs dans la liste d'attaque : " + this.inactivesToAttack.size());

        int i = 0;
        boolean atLeastOnce = false;
        while (!this.inactivesToAttack.isEmpty() && this.slotsService.hasEnoughFreeSlots(2)
                && i < this.inactivesToAttack.size()) {
            CoordinateDto coords = this.inactivesToAttack.get(i).getCoordinate();
            PlanetsResourcesDto resources = this.inactivesToAttack.get(i).getResources();
            Integer totalResources = (3 * (resources.getMetal() + resources.getCrystal() + resources.getDeuterium())) / 4;
            Integer nbCargo = totalResources / this.CARGO_STOCKAGE +1;

            ShipsDto ships = this.ogameApiService.getShips(this.AUTO_PLANET_ID.get(this.index));

            if (this.CARGO_ID.equals(OgameCst.LARGE_CARGO_ID) && ships.getLargeCargo() > 0
                    || this.CARGO_ID.equals(OgameCst.SMALL_CARGO_ID) && ships.getSmallCargo() > 0
                    || this.CARGO_ID.equals(OgameCst.ESPIONAGE_PROBE_ID) && ships.getEspionageProbe() > 0) {

                // Attaque
                MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
                formData.add("ships", this.CARGO_ID + "," + nbCargo);
                formData.add("mission", OgameCst.ATTACK.toString());
                formData.add("speed", OgameCst.HUNDRED_PERCENT.toString());
                formData.add("galaxy", coords.getGalaxy().toString());
                formData.add("system", coords.getSystem().toString());
                formData.add("position", coords.getPosition().toString());
                formData.add("metal", "0");
                formData.add("crystal", "0");
                formData.add("deuterium", "0");
                this.ogameApiService.sendFleet(this.AUTO_PLANET_ID.get(this.index), formData);

                LOGGER.info("Attaque de l'inactif " + coords.getGalaxy() + ":" + coords.getSystem() + ":" + coords.getPosition()
                        + " pour un total de " + totalResources + " ressources");

                if (this.attackedInactives.stream().noneMatch(c -> this.sameCoords(c, coords))) {
                    this.attackedInactives.add(coords);
                }

                if ((resources.getMetal() + resources.getCrystal() + resources.getDeuterium()) / 4 > this.MINIMAL_RESOURCES) {
                    // Cas où il est possible de faire plusieurs vagues
                    resources.setMetal(resources.getMetal() / 4);
                    resources.setCrystal(resources.getCrystal() / 4);
                    resources.setDeuterium(resources.getDeuterium() / 4);
                } else {
                    // Sinon, passage à l'inactif suivant
                    i++;
                }
                atLeastOnce = true;
            } else {
                i++;
            }
        }

        if (atLeastOnce) {
            for (int j = i-1; j > -1; j--) {
                this.inactivesToAttack.remove(j);
            }
        }
    }

    private void spyInactives() {
        this.updateAttackedInactives();

        int i = 0;
        int skipped = 0;
        boolean atLeastOnce = false;
        while (!this.inactivesToSpy.isEmpty() && this.slotsService.hasEnoughFreeSlots(2)
                && i < this.inactivesToSpy.size()) {
            CoordinateDto coords = this.inactivesToSpy.get(i).getCoordinate();

            if (this.attackedInactives.stream().noneMatch(c -> this.sameCoords(c, coords))) {
                // Espionnage
                MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
                formData.add("ships", OgameCst.ESPIONAGE_PROBE_ID + "," + this.NB_SONDES);
                formData.add("mission", OgameCst.SPY.toString());
                formData.add("speed", OgameCst.HUNDRED_PERCENT.toString());
                formData.add("galaxy", coords.getGalaxy().toString());
                formData.add("system", coords.getSystem().toString());
                formData.add("position", coords.getPosition().toString());
                formData.add("metal", "0");
                formData.add("crystal", "0");
                formData.add("deuterium", "0");
                this.ogameApiService.sendFleet(this.AUTO_PLANET_ID.get(this.index), formData);

                atLeastOnce = true;
            } else {
                skipped++;
            }
            i++;
        }

        if (atLeastOnce) {
            for (int j = i-1; j > -1; j--) {
                this.inactivesToSort.add(this.inactivesToSpy.get(j));
                this.inactivesToSpy.remove(j);
            }

            LOGGER.info("Inactifs sondés : " + (i - skipped));
        }
    }

    private void sortInactives() {
        List<InactivePlanetsDto> inactivesToAttack = new ArrayList<>();

        Iterator<InactivePlanetsDto> inactivesToSortIterator = this.inactivesToSort.iterator();
        while (inactivesToSortIterator.hasNext()) {
            InactivePlanetsDto inactive = inactivesToSortIterator.next();

            try {
                EspionageReportDto espionageReport = this.ogameApiService.getEspionageReport(inactive.getCoordinate().getGalaxy(),
                        inactive.getCoordinate().getSystem(), inactive.getCoordinate().getPosition());

                if (this.noDefense(espionageReport) && this.noFleet(espionageReport)
                        && this.attackedInactives.stream().noneMatch(c -> this.sameCoords(c, inactive.getCoordinate()))) {
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
                .filter(inactive -> (inactive.getResources().getMetal() + inactive.getResources().getCrystal() + inactive.getResources().getDeuterium()) > this.MINIMAL_RESOURCES)
                .sorted((i1, i2) -> (i2.getResources().getMetal() + i2.getResources().getCrystal() + i2.getResources().getDeuterium()) - (i1.getResources().getMetal() + i1.getResources().getCrystal() + i1.getResources().getDeuterium()))
                .limit(8)
                .collect(Collectors.toList()));

        this.inactivesToSort = new ArrayList<>();
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

    private Boolean sameCoords(CoordinateDto c1, CoordinateDto c2) {
        return c1.getGalaxy().equals(c2.getGalaxy())
                && c1.getSystem().equals(c2.getSystem())
                && c1.getPosition().equals(c2.getPosition());
    }

    private void updateAttackedInactives() {
        List<FleetDto> fleets = this.ogameApiService.getFleets();
        this.attackedInactives.removeIf(c -> fleets.stream().noneMatch(f -> !f.getReturnFlight() && f.getMission().equals(OgameCst.ATTACK) && this.sameCoords(c, f.getDestination())));
    }
}
