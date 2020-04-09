package com.stalyon.ogame.service;

import com.stalyon.ogame.OgameApiService;
import com.stalyon.ogame.constants.OgameCst;
import com.stalyon.ogame.dto.CoordinateDto;
import com.stalyon.ogame.dto.GalaxyInfosDto;
import com.stalyon.ogame.dto.GalaxyPlanetsDto;
import com.stalyon.ogame.dto.SlotsDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Profile("spy")
public class SpyService {

    private static Logger LOGGER = LoggerFactory.getLogger(SpyService.class);

    @Autowired
    private OgameApiService ogameApiService;

    private List<GalaxyInfosDto> galaxyInfosToSpy = new ArrayList<>();
    private int i = 0;

    @EventListener(ApplicationReadyEvent.class)
    public void spy() {
        List<GalaxyInfosDto> galaxyInfos = new ArrayList<>();
        for (int i = 309; i < 310; i++) {
            galaxyInfos.add(this.ogameApiService.getGalaxyInfos(3, i));
        }

        this.galaxyInfosToSpy.addAll(galaxyInfos);
        LOGGER.info("Fin du scan");
    }

    @Scheduled(cron = "0/10 * * * * *") // every minute
    public void spyOrAttackInactives() {
        /*if (!this.galaxyInfosToSpy.isEmpty()) {

            this.coordsToSpy.addAll(
                    galaxyInfos.getPlanets()
                            .stream()
                            .filter(Objects::nonNull)
                            .filter(this::isToSpy)
                            .map(GalaxyPlanetsDto::getCoordinate)
                            .collect(Collectors.toList())
            );
            SlotsDto slots = this.ogameApiService.getSlots();

            while (!this.coordsToSpy.isEmpty() && slots.getExpTotal().equals(slots.getExpInUse())
                    && slots.getInUse() + 2 < slots.getTotal() && i < this.coordsToSpy.size()) {
                CoordinateDto coords = this.coordsToSpy.get(i);

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
                this.ogameApiService.sendFleet(33643869, formData);
                LOGGER.info("Espionnage de " + coords.getGalaxy() + ":" + coords.getSystem() + ":" + coords.getPosition());

                slots = this.ogameApiService.getSlots();
                i++;
            }
        }*/
    }

    private Boolean isToSpy(GalaxyPlanetsDto galaxyPlanet) {
        return !galaxyPlanet.getInactive() && !galaxyPlanet.getAdministrator() && !galaxyPlanet.getBanned()
                && !galaxyPlanet.getNewbie() && !galaxyPlanet.getVacation()
                && galaxyPlanet.getPlayer().getRank() > 150
                && (galaxyPlanet.getActivity().equals(0) || galaxyPlanet.getActivity() > 15);
    }
}
