package com.stalyon.ogame.service;

import com.stalyon.ogame.OgameApiService;
import com.stalyon.ogame.config.OgameProperties;
import com.stalyon.ogame.constants.OgameCst;
import com.stalyon.ogame.dto.*;
import com.stalyon.ogame.utils.MessageService;
import com.stalyon.ogame.utils.SlotsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Profile("!noRecherches")
public class RecherchesService {

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

    @Scheduled(cron = "0 0/3 * * * *") // every 3-minutes
    public void launchRecherche() {
        if (this.ogameProperties.RECHERCHES_BUILD_AUTO && !this.ghostService.isAfkPeriod(Boolean.FALSE)) {
            PlanetDto planet = this.ogameApiService.getPlanet(this.ogameProperties.RECHERCHES_BUILD_PLANET_ID);
            PlanetsConstructionsDto constructions = this.ogameApiService.getPlanetsConstructions(planet);

            // Vérification qu'il n'y ait pas déjà une techno en cours
            if (constructions != null && constructions.getResearchID().equals(0)) {
                ResearchesDto researches = this.ogameApiService.getResearches();

                for (int i = 0; i < this.ogameProperties.RECHERCHES_BUILD_QUEUE_TECHNOS_ID.size(); i++) {
                    if (this.notLaunched(researches, this.ogameProperties.RECHERCHES_BUILD_QUEUE_TECHNOS_ID.get(i), this.ogameProperties.RECHERCHES_BUILD_QUEUE_TECHNOS_LVL.get(i))) {
                        if (this.enoughResources(planet, this.ogameProperties.RECHERCHES_BUILD_QUEUE_TECHNOS_ID.get(i), this.ogameProperties.RECHERCHES_BUILD_QUEUE_TECHNOS_LVL.get(i))) {
                            // Lancement de la recherche
                            this.messageService.logInfo("Lancement " + this.getResearchName(this.ogameProperties.RECHERCHES_BUILD_QUEUE_TECHNOS_ID.get(i)) + " " + this.ogameProperties.RECHERCHES_BUILD_QUEUE_TECHNOS_LVL.get(i) + " sur " + planet.getName(), Boolean.FALSE, Boolean.FALSE);

                            MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
                            this.ogameApiService.buildTechnology(planet.getId(), this.ogameProperties.RECHERCHES_BUILD_QUEUE_TECHNOS_ID.get(i), formData);
                            return;
                        } else if (this.ogameProperties.RECHERCHES_TRANSPORT_AUTO) {
                            // Lancer un transport
                            this.transportResources(planet, this.ogameProperties.RECHERCHES_BUILD_QUEUE_TECHNOS_ID.get(i), this.ogameProperties.RECHERCHES_BUILD_QUEUE_TECHNOS_LVL.get(i));
                            return;
                        }
                    }
                }
            }
        }
    }

    private void transportResources(PlanetDto planet, Integer technoId, Integer technoLvl) {
        if (!this.ghostService.isAfkPeriod(Boolean.TRUE)) {
            List<PlanetDto> myPlanets = this.ogameApiService.getPlanets();
            PlanetsResourcesDto cost = this.ogameApiService.getPrice(technoId, technoLvl);
            PlanetsResourcesDto resources = this.ogameApiService.getPlanetsResources(planet);
            List<PlanetClusterizedHelperDto> planetsClusterized = new ArrayList<>();

            List<PlanetDto> planets = new ArrayList<>();
            myPlanets.forEach(p -> {
                planets.add(p);

                if (p.getMoon() != null) {
                    planets.add(new PlanetDto(
                            p.getMoon().getId(),
                            p.getMoon().getName(),
                            p.getMoon().getCoordinate(),
                            null,
                            true
                    ));
                }
            });

            planets
                    .stream()
                    .filter(p -> this.ogameProperties.RECHERCHES_TRANSPORT_PLANETS_CLUSTERIZED != null
                            && this.ogameProperties.RECHERCHES_TRANSPORT_PLANETS_CLUSTERIZED.stream().anyMatch(pl -> pl.equals(p.getId())))
                    .forEach(p -> {
                        PlanetsResourcesDto clusterizedPlanetResources = this.ogameApiService.getPlanetsResources(p);

                        planetsClusterized.add(new PlanetClusterizedHelperDto(null, p, null, null, clusterizedPlanetResources));
                    });

            List<PlanetClusterizedHelperDto> sorted = planetsClusterized.stream()
                    .sorted((p1, p2) -> (p2.getResources().getMetal() + p2.getResources().getCrystal() + p2.getResources().getDeuterium()) - (p1.getResources().getMetal() + p1.getResources().getCrystal() + p1.getResources().getDeuterium()))
                    .collect(Collectors.toList());

            if (cost.getMetal() <= this.totalMetalPlanets(sorted)
                    && cost.getCrystal() <= this.totalCrystalPlanets(sorted)
                    && cost.getDeuterium() <= this.totalDeutPlanets(sorted)) {
                Integer metalMissing = cost.getMetal() - resources.getMetal();
                Integer crystalMissing = cost.getCrystal() - resources.getCrystal();
                Integer deutMissing = cost.getDeuterium() - resources.getDeuterium();
                metalMissing = metalMissing < 0 ? 0 : metalMissing;
                crystalMissing = crystalMissing < 0 ? 0 : crystalMissing;
                deutMissing = deutMissing < 0 ? 0 : deutMissing;

                for (PlanetClusterizedHelperDto pc : sorted) {
                    Integer metalToSend = metalMissing > pc.getResources().getMetal() ? pc.getResources().getMetal() : metalMissing;
                    Integer crystalToSend = crystalMissing > pc.getResources().getCrystal() ? pc.getResources().getCrystal() : crystalMissing;
                    Integer deutToSend = deutMissing > pc.getResources().getDeuterium() ? pc.getResources().getDeuterium() : deutMissing;
                    Integer nbLargeCargo = ((metalToSend + crystalToSend + deutToSend) / this.ogameProperties.RECHERCHES_TRANSPORT_STOCKAGE) + 1;

                    MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
                    formData.add("ships", OgameCst.LARGE_CARGO_ID + "," + nbLargeCargo);
                    formData.add("mission", OgameCst.TRANSPORT.toString());
                    formData.add("speed", OgameCst.HUNDRED_PERCENT.toString());
                    formData.add("galaxy", planet.getCoordinate().getGalaxy().toString());
                    formData.add("system", planet.getCoordinate().getSystem().toString());
                    formData.add("position", planet.getCoordinate().getPosition().toString());
                    formData.add("metal", metalToSend.toString());
                    formData.add("crystal", crystalToSend.toString());
                    formData.add("deuterium", deutToSend.toString());

                    if (metalToSend > 0 || crystalToSend > 0 || deutToSend > 0) {
                        ShipsDto ships = this.ogameApiService.getShips(pc.getPlanet().getId());

                        if (ships.getLargeCargo() >= nbLargeCargo) {
                            this.messageService.logInfo("Transport de ressources (Métal = " + metalToSend + ", Cristal = " + crystalToSend
                                            + ", Deut = " + deutToSend + ") de " + pc.getPlanet().getName() + " pour nouvelle recherche",
                                    Boolean.FALSE, Boolean.FALSE);
                            this.ogameApiService.sendFleet(pc.getPlanet().getId(), formData);

                            metalMissing = metalMissing - metalToSend;
                            crystalMissing = crystalMissing - crystalToSend;
                            deutMissing = deutMissing - deutToSend;
                            metalMissing = metalMissing < 0 ? 0 : metalMissing;
                            crystalMissing = crystalMissing < 0 ? 0 : crystalMissing;
                            deutMissing = deutMissing < 0 ? 0 : deutMissing;

                            if (metalMissing <= 0 && crystalMissing <= 0 && deutMissing <= 0) {
                                return;
                            }
                        } else {
                            this.messageService.logInfo("Impossible d'envoyer les ressources depuis " + pc.getPlanet().getName()
                                    + ", pas de GT disponibles", Boolean.FALSE, Boolean.FALSE);
                        }
                    }
                }
            }
        }
    }

    private Boolean enoughResources(PlanetDto planet, Integer technoId, Integer technoLvl) {
        PlanetsResourcesDto cost = this.ogameApiService.getPrice(technoId, technoLvl);
        PlanetsResourcesDto resources = this.ogameApiService.getPlanetsResources(planet);

        return cost.getMetal() <= resources.getMetal()
                && cost.getCrystal() <= resources.getCrystal()
                && cost.getDeuterium() <= resources.getDeuterium();
    }

    private Boolean notLaunched(ResearchesDto researches, Integer technoId, Integer technoLvl) {
        return technoId.equals(OgameCst.ESPIONAGE_TECHNOLOGY_ID) && researches.getEspionageTechnology() < technoLvl
                || technoId.equals(OgameCst.COMPUTER_TECHNOLOGY_ID) && researches.getComputerTechnology() < technoLvl
                || technoId.equals(OgameCst.WEAPONS_TECHNOLOGY_ID) && researches.getWeaponsTechnology() < technoLvl
                || technoId.equals(OgameCst.SHIELDING_TECHNOLOGY_ID) && researches.getShieldingTechnology() < technoLvl
                || technoId.equals(OgameCst.ARMOUR_TECHNOLOGY_ID) && researches.getArmourTechnology() < technoLvl
                || technoId.equals(OgameCst.ENERGY_TECHNOLOGY_ID) && researches.getEnergyTechnology() < technoLvl
                || technoId.equals(OgameCst.HYPERSPACE_TECHNOLOGY_ID) && researches.getHyperspaceTechnology() < technoLvl
                || technoId.equals(OgameCst.COMBUSTION_DRIVE_ID) && researches.getCombustionDrive() < technoLvl
                || technoId.equals(OgameCst.IMPULSE_DRIVE_ID) && researches.getImpulseDrive() < technoLvl
                || technoId.equals(OgameCst.HYPERSPACE_DRIVE_ID) && researches.getHyperspaceDrive() < technoLvl
                || technoId.equals(OgameCst.LASER_TECHNOLOGY_ID) && researches.getLaserTechnology() < technoLvl
                || technoId.equals(OgameCst.ION_TECHNOLOGY_ID) && researches.getIonTechnology() < technoLvl
                || technoId.equals(OgameCst.PLASMA_TECHNOLOGY_ID) && researches.getPlasmaTechnology() < technoLvl
                || technoId.equals(OgameCst.INTERGALACTIC_RESEARCH_NETWORK_ID) && researches.getIntergalacticResearchNetwork() < technoLvl
                || technoId.equals(OgameCst.ASTROPHYSICS_ID) && researches.getAstrophysics() < technoLvl
                || technoId.equals(OgameCst.GRAVITON_TECHNOLOGY_ID) && researches.getGravitonTechnology() < technoLvl;
    }

    private String getResearchName(Integer researchId) {
        if (researchId.equals(OgameCst.ESPIONAGE_TECHNOLOGY_ID)) {
            return "Espionnage";
        } else if (researchId.equals(OgameCst.COMPUTER_TECHNOLOGY_ID)) {
            return "Ordinateur";
        } else if (researchId.equals(OgameCst.WEAPONS_TECHNOLOGY_ID)) {
            return "Armes";
        } else if (researchId.equals(OgameCst.SHIELDING_TECHNOLOGY_ID)) {
            return "Bouclier";
        } else if (researchId.equals(OgameCst.ARMOUR_TECHNOLOGY_ID)) {
            return "Protection Vaisseaux Spatiaux";
        } else if (researchId.equals(OgameCst.ENERGY_TECHNOLOGY_ID)) {
            return "Energie";
        } else if (researchId.equals(OgameCst.HYPERSPACE_TECHNOLOGY_ID)) {
            return "Technologie Hyperespace";
        } else if (researchId.equals(OgameCst.COMBUSTION_DRIVE_ID)) {
            return "Réacteur Combustion";
        } else if (researchId.equals(OgameCst.IMPULSE_DRIVE_ID)) {
            return "Réacteur Impulsion";
        } else if (researchId.equals(OgameCst.HYPERSPACE_DRIVE_ID)) {
            return "Propulsion Hypersespace";
        } else if (researchId.equals(OgameCst.LASER_TECHNOLOGY_ID)) {
            return "Laser";
        } else if (researchId.equals(OgameCst.ION_TECHNOLOGY_ID)) {
            return "Ions";
        } else if (researchId.equals(OgameCst.PLASMA_TECHNOLOGY_ID)) {
            return "Plasma";
        } else if (researchId.equals(OgameCst.INTERGALACTIC_RESEARCH_NETWORK_ID)) {
            return "Réseau Recherche Intergalactique";
        } else if (researchId.equals(OgameCst.ASTROPHYSICS_ID)) {
            return "Astrophysique";
        } else if (researchId.equals(OgameCst.GRAVITON_TECHNOLOGY_ID)) {
            return "Graviton";
        }
        return "Technologie";
    }

    private Integer totalMetalPlanets(List<PlanetClusterizedHelperDto> planetsClusterized) {
        return planetsClusterized.stream()
                .map(PlanetClusterizedHelperDto::getResources)
                .map(PlanetsResourcesDto::getMetal)
                .reduce(0, Integer::sum);
    }

    private Integer totalCrystalPlanets(List<PlanetClusterizedHelperDto> planetsClusterized) {
        return planetsClusterized.stream()
                .map(PlanetClusterizedHelperDto::getResources)
                .map(PlanetsResourcesDto::getCrystal)
                .reduce(0, Integer::sum);
    }

    private Integer totalDeutPlanets(List<PlanetClusterizedHelperDto> planetsClusterized) {
        return planetsClusterized.stream()
                .map(PlanetClusterizedHelperDto::getResources)
                .map(PlanetsResourcesDto::getDeuterium)
                .reduce(0, Integer::sum);
    }
}