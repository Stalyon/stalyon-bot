package com.stalyon.ogame.service;

import com.stalyon.ogame.OgameApiService;
import com.stalyon.ogame.config.OgameProperties;
import com.stalyon.ogame.constants.OgameCst;
import com.stalyon.ogame.dto.FleetDto;
import com.stalyon.ogame.dto.GalaxyInfosDto;
import com.stalyon.ogame.dto.ShipsDto;
import com.stalyon.ogame.utils.MessageService;
import com.stalyon.ogame.utils.SlotsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;

@Service
@Profile("!noExpeDebrisTheft")
public class ExpeDebrisTheftService {

    @Autowired
    private OgameApiService ogameApiService;

    @Autowired
    private OgameProperties ogameProperties;

    @Autowired
    private MessageService messageService;

    @Autowired
    private SlotsService slotsService;

    @Scheduled(cron = "50 0/3 * * * *") // every 3-minutes
    public void checkExpeditionDebris() {
        if (this.ogameProperties.EXPE_THEFT_AUTO) {
            for (int i = 0; i < this.ogameProperties.EXPE_THEFT_PLANET_ID.size(); i++) {
                for (int j = this.ogameProperties.EXPE_THEFT_COORD_SYSTEM_MIN.get(i); j <= this.ogameProperties.EXPE_THEFT_COORD_SYSTEM_MAX.get(i); j++) {
                    GalaxyInfosDto galaxyInfos = this.ogameApiService.getGalaxyInfos(this.ogameProperties.EXPE_THEFT_COORD_GALAXY.get(i), j);

                    if (galaxyInfos.getExpeditionDebris() != null && galaxyInfos.getExpeditionDebris().getPathfindersNeeded() >= this.ogameProperties.EXPE_THEFT_PATHFINDER_MIN) {
                        this.sendRecycler(galaxyInfos, this.ogameProperties.EXPE_THEFT_PLANET_ID.get(i));
                    }
                }
            }
        }
    }

    private void sendRecycler(GalaxyInfosDto galaxyInfos, Integer planetId) {
        if (this.slotsService.hasEnoughFreeSlots(1)
                && this.ogameApiService.getShips(planetId).getPathfinder() > 0) {
            List<FleetDto> fleets = this.ogameApiService.getFleets();
            Integer pathfinderInWork = fleets.stream()
                    .filter(fleet -> fleet.getMission().equals(OgameCst.RECYCLE_DEBRIS_FIELD)
                            && fleet.getDestination().getGalaxy().equals(galaxyInfos.getGalaxy())
                            && fleet.getDestination().getSystem().equals(galaxyInfos.getSystem())
                            && fleet.getDestination().getPosition().equals(16)
                            && fleet.getReturnFlight() == Boolean.FALSE)
                    .map(FleetDto::getShips)
                    .map(ShipsDto::getPathfinder)
                    .reduce(0, Integer::sum);

            if (galaxyInfos.getExpeditionDebris() != null
                    && galaxyInfos.getExpeditionDebris().getPathfindersNeeded() - pathfinderInWork >= this.ogameProperties.EXPE_THEFT_PATHFINDER_MIN
                    && this.slotsService.hasEnoughFreeSlots(1)) {
                // Envoyer des éclaireurs pour récupérer les débris
                MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
                formData.add("ships", OgameCst.PATHFINDER_ID + "," + galaxyInfos.getExpeditionDebris().getPathfindersNeeded());
                formData.add("mission", OgameCst.RECYCLE_DEBRIS_FIELD.toString());
                formData.add("speed", OgameCst.HUNDRED_PERCENT.toString());
                formData.add("galaxy", galaxyInfos.getGalaxy().toString());
                formData.add("system", galaxyInfos.getSystem().toString());
                formData.add("position", "16");
                formData.add("metal", "0");
                formData.add("crystal", "0");
                formData.add("deuterium", "0");
                this.ogameApiService.sendFleet(planetId, formData);

                this.messageService.logInfo("Recyclage du CDR en " + galaxyInfos.getGalaxy() + ":" + galaxyInfos.getSystem()
                        + ":16 (Métal = " + galaxyInfos.getExpeditionDebris().getMetal() + ", Cristal = "
                        + galaxyInfos.getExpeditionDebris().getCrystal() + ")", Boolean.FALSE, Boolean.FALSE);
            }
        } else {
            this.messageService.logInfo("Pas d'éclaireurs (ou slots) dispos pour le CDR en " + galaxyInfos.getGalaxy() + ":" + galaxyInfos.getSystem()
                    + ":16 (Métal = " + galaxyInfos.getExpeditionDebris().getMetal() + ", Cristal = "
                    + galaxyInfos.getExpeditionDebris().getCrystal() + ")", Boolean.FALSE, Boolean.FALSE);
        }
    }
}
