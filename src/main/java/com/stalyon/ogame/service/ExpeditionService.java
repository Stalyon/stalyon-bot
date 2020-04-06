package com.stalyon.ogame.service;

import com.stalyon.ogame.OgameApiService;
import com.stalyon.ogame.constants.OgameCst;
import com.stalyon.ogame.dto.*;
import com.stalyon.ogame.utils.ShipsUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Profile("!noExpedition")
public class ExpeditionService {

    private static Logger LOGGER = LoggerFactory.getLogger(ExpeditionService.class);

    @Value("${expedition.debris.check}")
    private Boolean EXPEDITION_DEBRIS_CHECK;

    @Value("${expedition.debris.planet}")
    private Integer EXPEDITION_DEBRIS_PLANET;

    @Value("${expedition.debris.galaxy}")
    private Integer EXPEDITION_DEBRIS_GALAXY;

    @Value("${expedition.debris.system}")
    private Integer EXPEDITION_DEBRIS_SYSTEM;

    @Autowired
    private OgameApiService ogameApiService;

    private Map<Integer, FleetDto> currentFleets = new HashMap<>();

    @Scheduled(cron = "40 * * * * *") // every minute
    public void launchExpedition() {
        List<FleetDto> fleets = this.ogameApiService.getFleets();

        Map<Integer, FleetDto> newFleets = new HashMap<>();
        fleets.stream()
                .filter(fleet -> fleet.getMission().equals(OgameCst.EXPEDITION) && fleet.getReturnFlight())
                .forEach(fleet -> newFleets.put(fleet.getId(), fleet));

        this.currentFleets.forEach((id, fleet) -> {
            if (!newFleets.containsKey(id)) {
                this.sendExpedition(fleet);
            }
        });

        this.currentFleets = new HashMap<>();
        this.currentFleets.putAll(newFleets);
    }

    @Scheduled(cron = "10 1/5 * * * *") // every 5-minutes
    public void checkExpeditionDebris() {
        if (this.EXPEDITION_DEBRIS_CHECK) {
            GalaxyInfosDto galaxyInfos = this.ogameApiService.getGalaxyInfos(this.EXPEDITION_DEBRIS_GALAXY, this.EXPEDITION_DEBRIS_SYSTEM);
            List<FleetDto> fleets = this.ogameApiService.getFleets();

            Integer pathfinderInWork = fleets.stream()
                    .filter(fleet -> fleet.getMission().equals(OgameCst.RECYCLE_DEBRIS_FIELD)
                            && fleet.getDestination().getGalaxy().equals(this.EXPEDITION_DEBRIS_GALAXY)
                            && fleet.getDestination().getSystem().equals(this.EXPEDITION_DEBRIS_SYSTEM)
                            && fleet.getDestination().getPosition().equals(16)
                            && fleet.getReturnFlight() == Boolean.FALSE)
                    .map(FleetDto::getShips)
                    .map(ShipsDto::getPathfinder)
                    .reduce(0, Integer::sum);

            if (galaxyInfos.getExpeditionDebris() != null && galaxyInfos.getExpeditionDebris().getPathfindersNeeded() - pathfinderInWork > 10) {
                SlotsDto slots = this.ogameApiService.getSlots();

                if (slots.getExpTotal().equals(slots.getExpInUse()) && slots.getInUse() +2 < slots.getTotal()) {

                    // Envoyer des éclaireurs pour récupéreur les débris
                    MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
                    formData.add("ships", OgameCst.PATHFINDER_ID + "," + galaxyInfos.getExpeditionDebris().getPathfindersNeeded());
                    formData.add("mission", OgameCst.RECYCLE_DEBRIS_FIELD.toString());
                    formData.add("speed", OgameCst.HUNDRED_PERCENT.toString());
                    formData.add("galaxy", this.EXPEDITION_DEBRIS_GALAXY.toString());
                    formData.add("system", this.EXPEDITION_DEBRIS_SYSTEM.toString());
                    formData.add("position", "16");
                    formData.add("metal", "0");
                    formData.add("crystal", "0");
                    formData.add("deuterium", "0");
                    this.ogameApiService.sendFleet(this.EXPEDITION_DEBRIS_PLANET, formData);

                    LOGGER.info("Recyclage du CDR en " + this.EXPEDITION_DEBRIS_GALAXY + ":" + this.EXPEDITION_DEBRIS_SYSTEM
                            + ":16 (Métal = " + galaxyInfos.getExpeditionDebris().getMetal() + ", Cristal = "
                            + galaxyInfos.getExpeditionDebris().getCrystal() + ")");
                }
            }
        }
    }

    private void sendExpedition(FleetDto fleet) {
        // Récupérer l'id de la planète
        PlanetDto destination = this.ogameApiService.getPlanet(fleet.getOrigin().getGalaxy(),
                fleet.getOrigin().getSystem(), fleet.getOrigin().getPosition());

        Integer destinationId = destination.getId();
        if (fleet.getOrigin().getType().equals(OgameCst.MOON_TYPE)) {
            destinationId = destination.getMoon().getId();
        }

        if (destinationId != null) {
            MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
            ShipsUtils.prepareExpeditionShip(fleet.getShips()).forEach(ship -> formData.add("ships", ship));
            formData.add("mission", OgameCst.EXPEDITION.toString());
            formData.add("speed", OgameCst.HUNDRED_PERCENT.toString());
            formData.add("galaxy", fleet.getDestination().getGalaxy().toString());
            formData.add("system", fleet.getDestination().getSystem().toString());
            formData.add("position", fleet.getDestination().getPosition().toString());
            formData.add("metal", "0");
            formData.add("crystal", "0");
            formData.add("deuterium", "0");

            this.ogameApiService.sendFleet(destinationId, formData);

            LOGGER.info("Envoi d'une flotte d'expédition de " + fleet.getOrigin().getGalaxy() + ":" +
                    fleet.getOrigin().getSystem() + ":" + fleet.getOrigin().getPosition() + " vers "
                    + fleet.getDestination().getGalaxy() + ":" + fleet.getDestination().getSystem()
                    + ":" + fleet.getDestination().getPosition());
        }
    }
}
