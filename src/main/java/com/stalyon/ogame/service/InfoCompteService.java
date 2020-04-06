package com.stalyon.ogame.service;

import com.stalyon.ogame.OgameApiService;
import com.stalyon.ogame.constants.OgameCst;
import com.stalyon.ogame.dto.*;
import com.stalyon.ogame.utils.AlertService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class InfoCompteService {

    private static Logger LOGGER = LoggerFactory.getLogger(InfoCompteService.class);

    @Autowired
    private OgameApiService ogameApiService;

    @Autowired
    private AlertService alertService;

    @Scheduled(cron = "30 0 0/2 * * *") // every 2-hours
    public void infoCompte() {
        // Récupération des infos de l'utilisateur
        UserInfosDto userInfos = this.ogameApiService.getUserInfos();
        List<String> content = new ArrayList<>();

        content.add("------");
        content.add("INFO : " + userInfos.getName() + " - Points : " + userInfos.getPoints()
                + " - Rank : " + userInfos.getRank());

        List<PlanetDto> planets = this.ogameApiService.getPlanets();
        for (PlanetDto planet : planets) {
            PlanetsResourcesDto resources = this.ogameApiService.getPlanetsResources(planet);
            PlanetsResourcesBuildingsDto resourcesBuildings = this.ogameApiService.getPlanetsResourcesBuildings(planet);

            content.add("    --> " + planet.getName() + " - Metal : " + resources.getMetal()
                    + " - Cristal : " + resources.getCrystal() + " - Deut : " + resources.getDeuterium());
            content.add("      -> Mine mét " + resourcesBuildings.getMetalMine() + " - Mine cri "
                    + resourcesBuildings.getCrystalMine() + " - Synthé deut " + resourcesBuildings.getDeuteriumSynthesizer()
                    + " - CES " + resourcesBuildings.getSolarPlant() + " - CEF " + resourcesBuildings.getFusionReactor());
            if (planet.getMoon() != null) {
                PlanetsResourcesDto resourcesMoon = this.ogameApiService.getPlanetsResources(new PlanetDto(
                        planet.getMoon().getId(),
                        planet.getMoon().getName(),
                        planet.getMoon().getCoordinate(),
                        null,
                        true
                ));
                content.add("      -> " + planet.getMoon().getName() + " - Metal : " + resourcesMoon.getMetal()
                        + " - Cristal : " + resourcesMoon.getCrystal() + " - Deut : " + resourcesMoon.getDeuterium());
            }
        }

        List<FleetDto> fleets = this.ogameApiService.getFleets();
        long nbAttack = fleets.stream().filter(f -> f.getMission().equals(OgameCst.ATTACK) || f.getMission().equals(OgameCst.GROUPED_ATTACK)).count();
        long nbTransport = fleets.stream().filter(f -> f.getMission().equals(OgameCst.TRANSPORT)).count();
        long nbExpe = fleets.stream().filter(f -> f.getMission().equals(OgameCst.EXPEDITION)).count();
        content.add("  --> Nombre de flottes : " + fleets.size() + " - Attaques : " +  nbAttack
                + " - Transports : " + nbTransport + " Expéditions : " + nbExpe);
        content.add("------");

        content.forEach(LOGGER::info);
        this.alertService.sendMail(String.join("\n", content));
    }
}
