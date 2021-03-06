package com.stalyon.ogame.service;

import com.stalyon.ogame.OgameApiService;
import com.stalyon.ogame.config.OgameProperties;
import com.stalyon.ogame.dto.PlanetDto;
import com.stalyon.ogame.utils.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Profile("!noInfosPlanetsId")
public class InfosPlanetsIdService {

    @Autowired
    private GhostService ghostService;

    @Autowired
    private OgameApiService ogameApiService;

    @Autowired
    private OgameProperties ogameProperties;

    @Autowired
    private MessageService messageService;

    @EventListener(ApplicationReadyEvent.class)
    public void run() {
        if (this.ogameProperties.INFOS_PLANETS_ID_ENABLE && !this.ghostService.isAfkPeriod(Boolean.FALSE)) {
            List<PlanetDto> planets = this.ogameApiService.getPlanets();

            planets.forEach(planet -> {
                this.messageService.logInfo(planet.getName() + " (" + planet.getCoordinate().getGalaxy() + ":" + planet.getCoordinate().getSystem() + ":" + planet.getCoordinate().getPosition() + ") - id : " + planet.getId(), Boolean.FALSE, Boolean.FALSE);

                if (planet.getMoon() != null) {
                    this.messageService.logInfo("Lune : " + planet.getMoon().getName() + " - id : " + planet.getMoon().getId(), Boolean.FALSE, Boolean.FALSE);
                }
            });
        }
    }
}
