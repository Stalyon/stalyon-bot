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
import org.springframework.beans.factory.annotation.Value;
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

    private static Logger LOGGER = LoggerFactory.getLogger(AttaquesService.class);

    @Autowired
    private OgameApiService ogameApiService;

    @Value("${spy.coord.galaxy}")
    private Integer COORD_GALAXY;

    @Value("${spy.coord.system.min}")
    private Integer COORD_SYSTEM_MIN;

    @Value("${spy.coord.system.max}")
    private Integer COORD_SYSTEM_MAX;

    @Value("${spy.planet}")
    private Integer PLANET_ID;

    @Value("${spy.sondes.nb}")
    private Integer NB_SONDES;

    @Value("${spy.filter.player.rank.min}")
    private Integer PLAYER_RANK_MIN;

    @Value("${spy.filter.planet}")
    private Boolean FILTER_PLANET;

    @Value("${spy.filter.moon}")
    private Boolean FILTER_MOON;

    @Value("${spy.filter.activity.last.min}")
    private Integer LAST_ACTIVITY_MIN;

    private List<CoordinateDto> coordsToSpy = new ArrayList<>();
    private int index = 0;

    @EventListener(ApplicationReadyEvent.class)
    public void scan() {
        for (int i = this.COORD_SYSTEM_MIN; i < this.COORD_SYSTEM_MAX; i++) {
            GalaxyInfosDto galaxyInfos = this.ogameApiService.getGalaxyInfos(this.COORD_GALAXY, i);

            if (this.FILTER_PLANET) {
                this.coordsToSpy.addAll(
                        galaxyInfos.getPlanets()
                                .stream()
                                .filter(Objects::nonNull)
                                .filter(this::planetToSpy)
                                .map(GalaxyPlanetsDto::getCoordinate)
                                .map(c -> new CoordinateDto(c.getGalaxy(), c.getSystem(), c.getPosition(), OgameCst.PLANET_TYPE))
                                .collect(Collectors.toList())
                );
            }

            if (this.FILTER_MOON) {
                this.coordsToSpy.addAll(
                        galaxyInfos.getPlanets()
                                .stream()
                                .filter(Objects::nonNull)
                                .filter(gp -> gp.getMoon() != null)
                                .filter(this::moonToSpy)
                                .map(GalaxyPlanetsDto::getCoordinate)
                                .map(c -> new CoordinateDto(c.getGalaxy(), c.getSystem(), c.getPosition(), OgameCst.MOON_TYPE))
                                .collect(Collectors.toList())
                );
            }
        }
    }

    @Scheduled(cron = "0/10 * * * * *") // every minute
    public void spy() {
        if (!this.coordsToSpy.isEmpty()) {
            SlotsDto slots = this.ogameApiService.getSlots();

            while (!this.coordsToSpy.isEmpty() && slots.getExpTotal().equals(slots.getExpInUse())
                    && slots.getInUse() + 1 < slots.getTotal() && this.index < this.coordsToSpy.size()) {
                CoordinateDto coords = this.coordsToSpy.get(this.index);

                // Espionnage
                MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
                formData.add("ships", OgameCst.ESPIONAGE_PROBE_ID + "," + this.NB_SONDES);
                formData.add("mission", OgameCst.SPY.toString());
                formData.add("speed", OgameCst.HUNDRED_PERCENT.toString());
                formData.add("galaxy", coords.getGalaxy().toString());
                formData.add("system", coords.getSystem().toString());
                formData.add("position", coords.getPosition().toString());
                formData.add("type", coords.getType().toString());
                formData.add("metal", "0");
                formData.add("crystal", "0");
                formData.add("deuterium", "0");
                this.ogameApiService.sendFleet(this.PLANET_ID, formData);
                LOGGER.info("Espionnage de " + coords.getGalaxy() + ":" + coords.getSystem() + ":" + coords.getPosition());

                slots = this.ogameApiService.getSlots();
                this.index++;
            }
        }
    }

    private Boolean planetToSpy(GalaxyPlanetsDto galaxyPlanet) {
        return !galaxyPlanet.getInactive() && !galaxyPlanet.getAdministrator() && !galaxyPlanet.getBanned()
                && !galaxyPlanet.getNewbie() && !galaxyPlanet.getVacation()
                && galaxyPlanet.getPlayer().getRank() > this.PLAYER_RANK_MIN
                && (galaxyPlanet.getActivity().equals(0) || galaxyPlanet.getActivity() > this.LAST_ACTIVITY_MIN);
    }

    private Boolean moonToSpy(GalaxyPlanetsDto galaxyPlanet) {
        return !galaxyPlanet.getInactive() && !galaxyPlanet.getAdministrator() && !galaxyPlanet.getBanned()
                && !galaxyPlanet.getNewbie() && !galaxyPlanet.getVacation()
                && galaxyPlanet.getPlayer().getRank() > this.PLAYER_RANK_MIN
                && (galaxyPlanet.getMoon().getActivity().equals(0) || galaxyPlanet.getMoon().getActivity() > this.LAST_ACTIVITY_MIN);
    }
}
