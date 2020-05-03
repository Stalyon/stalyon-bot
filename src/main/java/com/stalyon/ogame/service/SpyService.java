package com.stalyon.ogame.service;

import com.stalyon.ogame.OgameApiService;
import com.stalyon.ogame.config.OgameProperties;
import com.stalyon.ogame.constants.OgameCst;
import com.stalyon.ogame.dto.CoordinateDto;
import com.stalyon.ogame.dto.GalaxyInfosDto;
import com.stalyon.ogame.dto.GalaxyPlanetsDto;
import com.stalyon.ogame.utils.MessageService;
import com.stalyon.ogame.utils.SlotsService;
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
@Profile("!noSpy")
public class SpyService {

    @Autowired
    private GhostService ghostService;

    @Autowired
    private OgameApiService ogameApiService;

    @Autowired
    private OgameProperties ogameProperties;

    @Autowired
    private MessageService messageService;

    @Autowired
    private SlotsService slotsService;

    private List<CoordinateDto> coordsToSpy = new ArrayList<>();
    private int index = 0;

    @EventListener(ApplicationReadyEvent.class)
    public void scan() {
        if (this.ogameProperties.SPY_ENABLE && !this.ghostService.isAfkPeriod(Boolean.FALSE)) {
            for (int i = this.ogameProperties.SPY_COORD_SYSTEM_MIN; i < this.ogameProperties.SPY_COORD_SYSTEM_MAX; i++) {
                GalaxyInfosDto galaxyInfos = this.ogameApiService.getGalaxyInfos(this.ogameProperties.SPY_COORD_GALAXY, i);

                if (this.ogameProperties.SPY_FILTER_PLANET) {
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

                if (this.ogameProperties.SPY_FILTER_MOON) {
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
    }

    @Scheduled(cron = "0/10 * * * * *") // every minute
    public void spy() {
        if (this.ogameProperties.SPY_ENABLE && !this.ghostService.isAfkPeriod(Boolean.TRUE) && !this.coordsToSpy.isEmpty()) {

            while (!this.coordsToSpy.isEmpty() && this.slotsService.hasEnoughFreeSlots(1)
                    && this.index < this.coordsToSpy.size()) {
                CoordinateDto coords = this.coordsToSpy.get(this.index);

                // Espionnage
                MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
                formData.add("ships", OgameCst.ESPIONAGE_PROBE_ID + "," + this.ogameProperties.SPY_NB_SONDES);
                formData.add("mission", OgameCst.SPY.toString());
                formData.add("speed", OgameCst.HUNDRED_PERCENT.toString());
                formData.add("galaxy", coords.getGalaxy().toString());
                formData.add("system", coords.getSystem().toString());
                formData.add("position", coords.getPosition().toString());
                formData.add("type", coords.getType().toString());
                formData.add("metal", "0");
                formData.add("crystal", "0");
                formData.add("deuterium", "0");
                this.ogameApiService.sendFleet(this.ogameProperties.SPY_PLANET_ID, formData);
                this.messageService.logInfo("Espionnage de " + coords.getGalaxy() + ":" + coords.getSystem() + ":" + coords.getPosition(), Boolean.FALSE, Boolean.FALSE);

                this.index++;
            }
        }
    }

    private Boolean planetToSpy(GalaxyPlanetsDto galaxyPlanet) {
        return !galaxyPlanet.getInactive() && !galaxyPlanet.getAdministrator() && !galaxyPlanet.getBanned()
                && !galaxyPlanet.getNewbie() && !galaxyPlanet.getVacation()
                && galaxyPlanet.getPlayer().getRank() > this.ogameProperties.SPY_PLAYER_RANK_MIN
                && (galaxyPlanet.getPlayer() != null && !this.ogameProperties.SPY_PLAYERS_EXCLUDED.contains(galaxyPlanet.getPlayer().getId()))
                && (galaxyPlanet.getAlliance() == null || !this.ogameProperties.SPY_ALLYS_EXCLUDED.contains(galaxyPlanet.getAlliance().getId()))
                && (galaxyPlanet.getActivity().equals(0) || galaxyPlanet.getActivity() > this.ogameProperties.SPY_LAST_ACTIVITY_MIN);
    }

    private Boolean moonToSpy(GalaxyPlanetsDto galaxyPlanet) {
        return !galaxyPlanet.getInactive() && !galaxyPlanet.getAdministrator() && !galaxyPlanet.getBanned()
                && !galaxyPlanet.getNewbie() && !galaxyPlanet.getVacation()
                && galaxyPlanet.getPlayer().getRank() > this.ogameProperties.SPY_PLAYER_RANK_MIN
                && (galaxyPlanet.getPlayer() != null && !this.ogameProperties.SPY_PLAYERS_EXCLUDED.contains(galaxyPlanet.getPlayer().getId()))
                && (galaxyPlanet.getAlliance() == null || !this.ogameProperties.SPY_ALLYS_EXCLUDED.contains(galaxyPlanet.getAlliance().getId()))
                && (galaxyPlanet.getMoon().getActivity().equals(0) || galaxyPlanet.getMoon().getActivity() > this.ogameProperties.SPY_LAST_ACTIVITY_MIN);
    }
}
