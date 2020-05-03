package com.stalyon.ogame.service;

import com.stalyon.ogame.OgameApiService;
import com.stalyon.ogame.config.OgameProperties;
import com.stalyon.ogame.constants.OgameCst;
import com.stalyon.ogame.dto.PlanetDto;
import com.stalyon.ogame.dto.PlanetsResourcesDto;
import com.stalyon.ogame.dto.SendFleetDto;
import com.stalyon.ogame.dto.SlotsDto;
import com.stalyon.ogame.utils.MessageService;
import com.stalyon.ogame.utils.ShipsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.LocalTime;
import java.util.Date;

@Service
public class GhostService {

    @Autowired
    private OgameApiService ogameApiService;

    @Autowired
    private OgameProperties ogameProperties;

    @Autowired
    private MessageService messageService;

    private Boolean ghosted = Boolean.FALSE;
    private Integer doFakeActivity = 0;
    private Date arrivalTime = null;

    @Scheduled(cron = "30 2/5 * * * *") // every 5-minutes
    public void run() {
        if (this.ogameProperties.GHOST_AUTO && this.isAfkPeriod(Boolean.FALSE)) {
            if (!this.ghosted) {
                SlotsDto slots = this.ogameApiService.getSlots();

                if (slots.getExpInUse().equals(0)) {
                    this.arrivalTime = null;
                    this.ghost();

                    this.ghosted = Boolean.TRUE;
                }
            }
        } else {
            this.ghosted = Boolean.FALSE;
        }
    }

    @Scheduled(cron = "30 3/5 * * * *") // every 5-minutes
    public void fakeActivity() {
        if (this.doFakeActivity > 0) {
            // TODO: fake activity

            this.doFakeActivity--;
        }
    }

    public Boolean isAfkPeriod(Boolean orWaitLastArrival) {
        if (this.ogameProperties.GHOST_AUTO) {
            LocalTime now = LocalTime.now();

            for (int i = 0; i < this.ogameProperties.GHOST_TIME_HOUR_FROM.size(); i++) {
                LocalTime minTime = LocalTime.of(this.ogameProperties.GHOST_TIME_HOUR_FROM.get(i), this.ogameProperties.GHOST_TIME_MINUTE_FROM.get(i));
                LocalTime maxTime = LocalTime.of(this.ogameProperties.GHOST_TIME_HOUR_TO.get(i), this.ogameProperties.GHOST_TIME_MINUTE_TO.get(i));

                if (now.isBefore(maxTime) && now.isAfter(minTime)) {
                    return Boolean.TRUE;
                }
            }

        }

        if (orWaitLastArrival && this.arrivalTime != null) {
            return new Date().before(this.arrivalTime);
        }

        return Boolean.FALSE;
    }

    public Boolean isGhosted() {
        return this.ghosted;
    }

    private void ghost() {
        for (int i = 0; i < this.ogameProperties.GHOST_PLANET_FROM.size(); i++) {
            try {
                PlanetsResourcesDto resources = this.ogameApiService
                        .getPlanetsResources(new PlanetDto(this.ogameProperties.GHOST_PLANET_FROM.get(i), null, null, null, null));

                // Ghost
                MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
                ShipsUtils.allShips().forEach(ship -> formData.add("ships", ship));
                formData.add("speed", (String.valueOf(this.ogameProperties.GHOST_SPEED.get(i) / 10)));
                formData.add("galaxy", this.ogameProperties.GHOST_COORD_TO_GALAXY.get(i).toString());
                formData.add("system", this.ogameProperties.GHOST_COORD_TO_SYSTEM.get(i).toString());
                formData.add("position", this.ogameProperties.GHOST_COORD_TO_POSITION.get(i).toString());
                formData.add("type", this.ogameProperties.GHOST_COORD_TO_TYPE.get(i).toString());
                formData.add("mission", this.ogameProperties.GHOST_MISSION.get(i).toString());
                formData.add("metal", resources.getMetal().toString());
                formData.add("crystal", resources.getCrystal().toString());
                formData.add("deuterium", resources.getDeuterium().toString());
                SendFleetDto sendFleet = this.ogameApiService.sendFleet(this.ogameProperties.GHOST_PLANET_FROM.get(i), formData);

                Date arrivalTime = sendFleet.getBackTime();
                if (this.ogameProperties.GHOST_MISSION.get(i).equals(OgameCst.PARK)) {
                    arrivalTime = sendFleet.getArrivalTime();
                }

                if (this.arrivalTime == null || arrivalTime.after(this.arrivalTime)) {
                    this.arrivalTime = arrivalTime;
                }

                this.messageService.logInfo("Ghost vers " + this.ogameProperties.GHOST_COORD_TO_GALAXY.get(i) + ":"
                        + this.ogameProperties.GHOST_COORD_TO_SYSTEM.get(i) + ":"
                        + this.ogameProperties.GHOST_COORD_TO_POSITION.get(i), Boolean.FALSE, Boolean.FALSE);
            } catch (Exception e) {
                // Ignore
            }

            this.doFakeActivity = 2;
        }
    }
}
