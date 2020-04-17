package com.stalyon.ogame.service;

import com.stalyon.ogame.OgameApiService;
import com.stalyon.ogame.config.OgameProperties;
import com.stalyon.ogame.constants.OgameCst;
import com.stalyon.ogame.dto.FleetDto;
import com.stalyon.ogame.dto.GalaxyInfosDto;
import com.stalyon.ogame.dto.ShipsDto;
import com.stalyon.ogame.dto.SlotsDto;
import com.stalyon.ogame.utils.MessageService;
import com.stalyon.ogame.utils.ShipsUtils;
import com.stalyon.ogame.utils.SlotsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;

@Service
@Profile("!noExpedition")
public class ExpeditionService {

    @Autowired
    private OgameApiService ogameApiService;

    @Autowired
    private OgameProperties ogameProperties;

    @Autowired
    private MessageService messageService;

    @Autowired
    private ShipsUtils shipsUtils;

    @Autowired
    private SlotsService slotsService;

    @Scheduled(cron = "40 * * * * *") // every minute
    public void launchExpedition() {
        SlotsDto slots = this.ogameApiService.getSlots();

        while (!slots.getExpInUse().equals(slots.getExpTotal())) {
            this.sendExpedition();

            slots = this.ogameApiService.getSlots();
        }
    }

    @Scheduled(cron = "10 1/3 * * * *") // every 3-minutes
    public void checkExpeditionDebris() {
        if (this.ogameProperties.EXPEDITION_DEBRIS_CHECK) {
            GalaxyInfosDto galaxyInfos = this.ogameApiService.getGalaxyInfos(this.ogameProperties.EXPEDITION_GALAXY, this.ogameProperties.EXPEDITION_SYSTEM);
            List<FleetDto> fleets = this.ogameApiService.getFleets();

            Integer pathfinderInWork = fleets.stream()
                    .filter(fleet -> fleet.getMission().equals(OgameCst.RECYCLE_DEBRIS_FIELD)
                            && fleet.getDestination().getGalaxy().equals(this.ogameProperties.EXPEDITION_GALAXY)
                            && fleet.getDestination().getSystem().equals(this.ogameProperties.EXPEDITION_SYSTEM)
                            && fleet.getDestination().getPosition().equals(16)
                            && fleet.getReturnFlight() == Boolean.FALSE)
                    .map(FleetDto::getShips)
                    .map(ShipsDto::getPathfinder)
                    .reduce(0, Integer::sum);

            if (galaxyInfos.getExpeditionDebris() != null
                    && galaxyInfos.getExpeditionDebris().getPathfindersNeeded() - pathfinderInWork > 10
                    && this.slotsService.hasEnoughFreeSlots(1)) {
                this.sendRecycler(galaxyInfos);
            }
        }
    }

    private void sendExpedition() {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        this.shipsUtils.prepareExpeditionShip().forEach(ship -> formData.add("ships", ship));
        formData.add("mission", OgameCst.EXPEDITION.toString());
        formData.add("speed", OgameCst.HUNDRED_PERCENT.toString());
        formData.add("galaxy", this.ogameProperties.EXPEDITION_GALAXY.toString());
        formData.add("system", this.ogameProperties.EXPEDITION_SYSTEM.toString());
        formData.add("position", "16");
        formData.add("metal", "0");
        formData.add("crystal", "0");
        formData.add("deuterium", "0");

        this.ogameApiService.sendFleet(this.ogameProperties.EXPEDITION_PLANET, formData);

        this.messageService.logInfo("Envoi d'une flotte d'expédition vers "
                + this.ogameProperties.EXPEDITION_GALAXY + ":" + this.ogameProperties.EXPEDITION_SYSTEM + ":16", Boolean.FALSE, Boolean.FALSE);
    }

    private void sendRecycler(GalaxyInfosDto galaxyInfos) {
        // Envoyer des éclaireurs pour récupéreur les débris
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("ships", OgameCst.PATHFINDER_ID + "," + galaxyInfos.getExpeditionDebris().getPathfindersNeeded());
        formData.add("mission", OgameCst.RECYCLE_DEBRIS_FIELD.toString());
        formData.add("speed", OgameCst.HUNDRED_PERCENT.toString());
        formData.add("galaxy", this.ogameProperties.EXPEDITION_GALAXY.toString());
        formData.add("system", this.ogameProperties.EXPEDITION_SYSTEM.toString());
        formData.add("position", "16");
        formData.add("metal", "0");
        formData.add("crystal", "0");
        formData.add("deuterium", "0");
        this.ogameApiService.sendFleet(this.ogameProperties.EXPEDITION_PLANET, formData);

        this.messageService.logInfo("Recyclage du CDR en " + this.ogameProperties.EXPEDITION_GALAXY + ":" + this.ogameProperties.EXPEDITION_SYSTEM
                + ":16 (Métal = " + galaxyInfos.getExpeditionDebris().getMetal() + ", Cristal = "
                + galaxyInfos.getExpeditionDebris().getCrystal() + ")", Boolean.FALSE, Boolean.FALSE);
    }
}
