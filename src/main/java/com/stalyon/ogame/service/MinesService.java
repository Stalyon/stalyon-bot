package com.stalyon.ogame.service;

import com.stalyon.ogame.OgameApiService;
import com.stalyon.ogame.config.OgameProperties;
import com.stalyon.ogame.constants.OgameCst;
import com.stalyon.ogame.dto.*;
import com.stalyon.ogame.utils.MessageService;
import com.stalyon.ogame.utils.ResourcesBuildingsUtils;
import com.stalyon.ogame.utils.SlotsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Profile("!noMines")
public class MinesService {

    @Autowired
    private OgameApiService ogameApiService;

    @Autowired
    private OgameProperties ogameProperties;

    @Autowired
    private MessageService messageService;

    @Autowired
    private SlotsService slotsService;

    private List<Integer> waittingClusterizedPlanets = new ArrayList<>();

    @Scheduled(cron = "0 0/2 * * * *") // every minute
    public void autoBuild() {
        if (this.ogameProperties.MINES_AUTO_BUILD) {
            // Récupération des planètes
            List<PlanetDto> planets = this.ogameApiService.getPlanets();

            planets.stream()
                    .filter(planet -> this.ogameProperties.MINES_EXCLUDED_PLANETS == null
                            || this.ogameProperties.MINES_EXCLUDED_PLANETS.stream().noneMatch(p -> p.equals(planet.getId())))
                    .forEach(planet -> {
                        // Récupération des ressources pour la planète
                        PlanetsResourcesDto resources = this.ogameApiService.getPlanetsResources(planet);

                        // Récupération des bâtiments de type resources
                        PlanetsResourcesBuildingsDto resourcesBuildings =
                                this.ogameApiService.getPlanetsResourcesBuildings(planet);

                        if (this.ogameApiService.getPlanetsConstructions(planet).getBuildingID() == 0) {
                            boolean launched = false;
                            if (ResourcesBuildingsUtils.canBuildMetalMine(resourcesBuildings.getMetalMine() + 1, resources)) {
                                this.messageService.logInfo("Construction Mine Métal " + (resourcesBuildings.getMetalMine() + 1) + " sur " + planet.getName(), Boolean.FALSE, Boolean.FALSE);

                                MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
                                this.ogameApiService.buildBuilding(planet.getId(), OgameCst.METAL_MINE_ID, formData);
                                launched = true;
                            } else if (ResourcesBuildingsUtils.canBuildCrystalMine(resourcesBuildings.getCrystalMine() + 1, resources)) {
                                this.messageService.logInfo("Construction Mine Cristal " + (resourcesBuildings.getCrystalMine() + 1) + " sur " + planet.getName(), Boolean.FALSE, Boolean.FALSE);

                                MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
                                this.ogameApiService.buildBuilding(planet.getId(), OgameCst.CRYSTAL_MINE_ID, formData);
                                launched = true;
                            } else if (this.ogameProperties.MINES_AUTO_BUILD_DEUT && resourcesBuildings.getDeuteriumSynthesizer() < this.ogameProperties.MINES_SYNTHE_DEUT_MAX
                                    && ResourcesBuildingsUtils.canBuildDeutSynth(resourcesBuildings.getDeuteriumSynthesizer() + 1, resources)) {
                                this.messageService.logInfo("Construction Synthé Deut " + (resourcesBuildings.getDeuteriumSynthesizer() + 1) + " sur " + planet.getName(), Boolean.FALSE, Boolean.FALSE);

                                MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
                                this.ogameApiService.buildBuilding(planet.getId(), OgameCst.DEUTERIUM_SYNTHESIZER_ID, formData);
                                launched = true;
                            } else if (ResourcesBuildingsUtils.notEnoughEnergy(resourcesBuildings, resources, this.ogameProperties.MINES_AUTO_BUILD_DEUT)) {
                                if (resourcesBuildings.getSolarPlant() < this.ogameProperties.MINES_SOLAR_PLANT_MAX
                                        && ResourcesBuildingsUtils.canBuildCentraleSolaire(resourcesBuildings.getSolarPlant() + 1, resources)) {
                                    this.messageService.logInfo("Construction Centrale Solaire  " + (resourcesBuildings.getSolarPlant() + 1) + " sur " + planet.getName(), Boolean.FALSE, Boolean.FALSE);

                                    MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
                                    this.ogameApiService.buildBuilding(planet.getId(), OgameCst.SOLAR_PLANT_ID, formData);
                                    launched = true;
                                } else if (resourcesBuildings.getSolarPlant() >= this.ogameProperties.MINES_SOLAR_PLANT_MAX
                                        && ResourcesBuildingsUtils.canBuildFusionReactor(resourcesBuildings.getFusionReactor() + 1, resources, this.ogameProperties.ENERGY_TECH)) {
                                    this.messageService.logInfo("Construction Réacteur Fusion " + (resourcesBuildings.getFusionReactor() + 1) + " sur " + planet.getName(), Boolean.FALSE, Boolean.FALSE);

                                    MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
                                    this.ogameApiService.buildBuilding(planet.getId(), OgameCst.FUSION_REACTOR_ID, formData);
                                    launched = true;
                                }
                            }

                            if (launched) {
                                this.waittingClusterizedPlanets.remove(planet.getId());
                            }
                        }
                    });
        }
    }

    @Scheduled(cron = "50 0/15 * * * *") // every 15 minutes
    public void autoBuildTransport() {
        if (this.ogameProperties.MINES_AUTO_BUILD_TRANSPORT) {
            // Récupération des planètes
            List<PlanetDto> myPlanets = this.ogameApiService.getPlanets();
            List<PlanetClusterizedHelperDto> planetsClusterized = new ArrayList<>();

            List<PlanetDto> planets = new ArrayList<>();
            myPlanets.forEach(planet -> {
                planets.add(planet);

                if (planet.getMoon() != null) {
                    planets.add(new PlanetDto(
                            planet.getMoon().getId(),
                            planet.getMoon().getName(),
                            planet.getMoon().getCoordinate(),
                            null,
                            true
                    ));
                }
            });

            planets.stream()
                    .filter(planet -> this.ogameProperties.MINES_EXCLUDED_PLANETS == null
                            || this.ogameProperties.MINES_EXCLUDED_PLANETS.stream().noneMatch(p -> p.equals(planet.getId())))
                    .filter(planet -> !this.waittingClusterizedPlanets.contains(planet.getId()))
                    .filter(planet -> this.ogameProperties.MINES_CLUSTERIZED_PLANETS != null
                            && this.ogameProperties.MINES_CLUSTERIZED_PLANETS.stream().anyMatch(p -> p.equals(planet.getId())))
                    .forEach(planet -> {
                        // Récupération des ressources pour la planète
                        PlanetsResourcesDto resources = this.ogameApiService.getPlanetsResources(planet);

                        // Récupération des bâtiments de type resources
                        PlanetsResourcesBuildingsDto resourcesBuildings =
                                this.ogameApiService.getPlanetsResourcesBuildings(planet);

                        // Si la planète n'a pas de construction en cours...
                        if ((planet.getIsMoon() == null || !planet.getIsMoon()) && this.ogameApiService.getPlanetsConstructions(planet).getBuildingID() == 0
                                && !(ResourcesBuildingsUtils.canBuildMetalMine(resourcesBuildings.getMetalMine() + 1, resources)
                                || ResourcesBuildingsUtils.canBuildCrystalMine(resourcesBuildings.getCrystalMine() + 1, resources)
                                || this.ogameProperties.MINES_AUTO_BUILD_DEUT && ResourcesBuildingsUtils.canBuildDeutSynth(resourcesBuildings.getDeuteriumSynthesizer() + 1, resources) && resourcesBuildings.getDeuteriumSynthesizer() < this.ogameProperties.MINES_SYNTHE_DEUT_MAX
                                || ResourcesBuildingsUtils.notEnoughEnergy(resourcesBuildings, resources, this.ogameProperties.MINES_AUTO_BUILD_DEUT)
                                && (resourcesBuildings.getSolarPlant() < this.ogameProperties.MINES_SOLAR_PLANT_MAX && ResourcesBuildingsUtils.canBuildCentraleSolaire(resourcesBuildings.getSolarPlant() + 1, resources)
                                || resourcesBuildings.getSolarPlant() >= this.ogameProperties.MINES_SOLAR_PLANT_MAX && ResourcesBuildingsUtils.canBuildFusionReactor(resourcesBuildings.getFusionReactor() + 1, resources, this.ogameProperties.ENERGY_TECH)))) {
                            // Calculer le prochain bâtiment à lancer
                            PlanetsResourcesDto metalMineCost = ResourcesBuildingsUtils.getMetalMineCost(resourcesBuildings.getMetalMine() + 1);
                            Integer metalMineEcoTime = ResourcesBuildingsUtils
                                    .getEcoTime(metalMineCost, resourcesBuildings, resources, this.ogameProperties.SERVER_SPEED, this.ogameProperties.HAS_GEOLOGUE);

                            PlanetsResourcesDto crystalMineCost = ResourcesBuildingsUtils.getCrystalMineCost(resourcesBuildings.getCrystalMine() + 1);
                            Integer crystalMineEcoTime = ResourcesBuildingsUtils
                                    .getEcoTime(crystalMineCost, resourcesBuildings, resources, this.ogameProperties.SERVER_SPEED, this.ogameProperties.HAS_GEOLOGUE);

                            PlanetsResourcesDto deutSynthCost = ResourcesBuildingsUtils.getDeutSynthCost(resourcesBuildings.getDeuteriumSynthesizer() + 1);
                            Integer deutSynthEcoTime = ResourcesBuildingsUtils
                                    .getEcoTime(deutSynthCost, resourcesBuildings, resources, this.ogameProperties.SERVER_SPEED, this.ogameProperties.HAS_GEOLOGUE);

                            PlanetsResourcesDto centraleSolaireCost = ResourcesBuildingsUtils.getCentraleSolaireCost(resourcesBuildings.getSolarPlant() + 1);
                            Integer centraleSolaireEcoTime = ResourcesBuildingsUtils
                                    .getEcoTime(centraleSolaireCost, resourcesBuildings, resources, this.ogameProperties.SERVER_SPEED, this.ogameProperties.HAS_GEOLOGUE);

                            PlanetsResourcesDto reactorFusionCost = ResourcesBuildingsUtils.getFusionReactorCost(resourcesBuildings.getFusionReactor() + 1, this.ogameProperties.ENERGY_TECH);
                            Integer reactorFusionEcoTime = ResourcesBuildingsUtils
                                    .getEcoTime(reactorFusionCost, resourcesBuildings, resources, this.ogameProperties.SERVER_SPEED, this.ogameProperties.HAS_GEOLOGUE);

                            if (!ResourcesBuildingsUtils.getMetalMineEnoughEnergy(resourcesBuildings.getMetalMine() + 1, resources)) {
                                metalMineEcoTime = Integer.MAX_VALUE;
                            }
                            if (!ResourcesBuildingsUtils.getCrystalMineEnoughEnergy(resourcesBuildings.getCrystalMine() + 1, resources)) {
                                crystalMineEcoTime = Integer.MAX_VALUE;
                            }
                            if (!this.ogameProperties.MINES_AUTO_BUILD_DEUT || resourcesBuildings.getDeuteriumSynthesizer() >= this.ogameProperties.MINES_SYNTHE_DEUT_MAX
                                    || !ResourcesBuildingsUtils.getDeutSynthEnoughEnergy(resourcesBuildings.getDeuteriumSynthesizer() + 1, resources)) {
                                deutSynthEcoTime = Integer.MAX_VALUE;
                            }

                            Integer minEcoTime = Math.min(metalMineEcoTime, Math.min(crystalMineEcoTime, deutSynthEcoTime));
                            if (metalMineEcoTime.equals(minEcoTime) && minEcoTime != Integer.MAX_VALUE) {
                                // Prochain bâtiment = Mine de métal
                                planetsClusterized.add(new PlanetClusterizedHelperDto(metalMineEcoTime, planet, OgameCst.METAL_MINE_ID, metalMineCost, resources));
                            } else if (crystalMineEcoTime.equals(minEcoTime) && minEcoTime != Integer.MAX_VALUE) {
                                // Prochain bâtiment = Mine de crystal
                                planetsClusterized.add(new PlanetClusterizedHelperDto(crystalMineEcoTime, planet, OgameCst.CRYSTAL_MINE_ID, crystalMineCost, resources));
                            } else if (deutSynthEcoTime.equals(minEcoTime) && minEcoTime != Integer.MAX_VALUE) {
                                // Prochain bâtiment = Synthé de deut
                                planetsClusterized.add(new PlanetClusterizedHelperDto(deutSynthEcoTime, planet, OgameCst.DEUTERIUM_SYNTHESIZER_ID, deutSynthCost, resources));
                            } else if (resourcesBuildings.getSolarPlant() < this.ogameProperties.MINES_SOLAR_PLANT_MAX) {
                                // Prochain bâtiment = Centrale solaire
                                planetsClusterized.add(new PlanetClusterizedHelperDto(centraleSolaireEcoTime, planet, OgameCst.SOLAR_PLANT_ID, centraleSolaireCost, resources));
                            } else {
                                // Prochain bâtiment = Réacteur de fusion
                                planetsClusterized.add(new PlanetClusterizedHelperDto(reactorFusionEcoTime, planet, OgameCst.FUSION_REACTOR_ID, reactorFusionCost, resources));
                            }
                        } else if (this.ogameApiService.getPlanetsConstructions(planet).getBuildingID() != 0
                                || (planet.getIsMoon() != null && planet.getIsMoon())) {
                            planetsClusterized.add(new PlanetClusterizedHelperDto(null, planet, null, null, resources));
                        }
                    });

            List<PlanetClusterizedHelperDto> planetsClusterizedCloned = planetsClusterized.stream()
                    .filter(pc -> pc.getEcoTime() != null)
                    .filter(pc -> pc.getEcoTime() > 0)
                    .filter(pc -> !this.waittingClusterizedPlanets.contains(pc.getPlanet().getId()))
                    .filter(pc -> pc.getCost().getMetal() < this.totalMetalPlanets(planetsClusterized)
                            && pc.getCost().getCrystal() < this.totalCrystalPlanets(planetsClusterized)
                            && pc.getCost().getDeuterium() < this.totalDeutPlanets(planetsClusterized))
                    .sorted(Comparator.comparing(PlanetClusterizedHelperDto::getEcoTime))
                    .collect(Collectors.toList());


            planetsClusterizedCloned.forEach(pc -> {
                // Envoi de resources depuis les autres planètes
                List<PlanetClusterizedHelperDto> sorted = planetsClusterized.stream()
                        .filter(p -> !p.getPlanet().getId().equals(pc.getPlanet().getId()))
                        .filter(p -> !this.waittingClusterizedPlanets.contains(p.getPlanet().getId()))
                        .sorted((p1, p2) -> (p2.getResources().getMetal() + p2.getResources().getCrystal() + p2.getResources().getDeuterium()) - (p1.getResources().getMetal() + p1.getResources().getCrystal() + p1.getResources().getDeuterium()))
                        .collect(Collectors.toList());

                Integer metalMissing = pc.getCost().getMetal() - pc.getResources().getMetal();
                Integer crystalMissing = pc.getCost().getCrystal() - pc.getResources().getCrystal();
                Integer deutMissing = pc.getCost().getDeuterium() - pc.getResources().getDeuterium();
                metalMissing = metalMissing < 0 ? 0 : metalMissing;
                crystalMissing = crystalMissing < 0 ? 0 : crystalMissing;
                deutMissing = deutMissing < 0 ? 0 : deutMissing;

                int i = 0;
                while ((metalMissing > 0 || crystalMissing > 0 || deutMissing > 0)
                        && this.slotsService.hasEnoughFreeSlots(2)
                        && i < sorted.size()) {
                    PlanetClusterizedHelperDto p = sorted.get(i);
                    Integer metalToSend = metalMissing > p.getResources().getMetal() ? p.getResources().getMetal() : metalMissing;
                    Integer crystalToSend = crystalMissing > p.getResources().getCrystal() ? p.getResources().getCrystal() : crystalMissing;
                    Integer deutToSend = deutMissing > p.getResources().getDeuterium() ? p.getResources().getDeuterium() : deutMissing;
                    Integer nbLargeCargo = ((metalToSend + crystalToSend + deutToSend) / this.ogameProperties.MINES_TRANSPORT_STOCKAGE) + 1;

                    MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
                    formData.add("ships", OgameCst.LARGE_CARGO_ID + "," + nbLargeCargo);
                    formData.add("mission", OgameCst.TRANSPORT.toString());
                    formData.add("speed", OgameCst.HUNDRED_PERCENT.toString());
                    formData.add("galaxy", pc.getPlanet().getCoordinate().getGalaxy().toString());
                    formData.add("system", pc.getPlanet().getCoordinate().getSystem().toString());
                    formData.add("position", pc.getPlanet().getCoordinate().getPosition().toString());
                    formData.add("metal", metalToSend.toString());
                    formData.add("crystal", crystalToSend.toString());
                    formData.add("deuterium", deutToSend.toString());

                    if (metalToSend > 0 || crystalToSend > 0) {
                        ShipsDto ships = this.ogameApiService.getShips(p.getPlanet().getId());

                        if (ships.getLargeCargo() >= nbLargeCargo) {
                            this.messageService.logInfo("Transport de ressources (Métal = " + metalToSend + ", Cristal = " + crystalToSend
                                    + ", Deut = " + deutToSend + ") de " + p.getPlanet().getName() + " vers "
                                    + pc.getPlanet().getName(), Boolean.FALSE, Boolean.FALSE);
                            this.ogameApiService.sendFleet(p.getPlanet().getId(), formData);

                            metalMissing = metalMissing - metalToSend;
                            crystalMissing = crystalMissing - crystalToSend;
                            deutMissing = deutMissing - deutToSend;
                        } else {
                            this.messageService.logInfo("Impossible d'envoyer les ressources depuis " + p.getPlanet().getName()
                                    + ", pas de GT disponibles", Boolean.FALSE, Boolean.FALSE);
                        }
                    }

                    // Mise à jour des ressources
                    Optional<PlanetClusterizedHelperDto> producterPlanet = planetsClusterized.stream().filter(pla -> pla.getPlanet().getId().equals(p.getPlanet().getId())).findFirst();
                    if (producterPlanet.isPresent()) {
                        producterPlanet.get().getResources().setMetal(p.getResources().getMetal() - metalToSend);
                        producterPlanet.get().getResources().setCrystal(p.getResources().getCrystal() - crystalToSend);
                        producterPlanet.get().getResources().setDeuterium(p.getResources().getDeuterium() - deutToSend);
                    }

                    if (metalMissing == 0 && crystalMissing == 0 && deutMissing == 0) {
                        this.waittingClusterizedPlanets.add(pc.getPlanet().getId());
                    }

                    i++;
                }
            });
        }
    }

    private Integer totalMetalPlanets(List<PlanetClusterizedHelperDto> planetsClusterized) {
        return planetsClusterized.stream()
                .filter(pc -> !this.waittingClusterizedPlanets.contains(pc.getPlanet().getId()))
                .map(PlanetClusterizedHelperDto::getResources)
                .map(PlanetsResourcesDto::getMetal)
                .reduce(0, Integer::sum);
    }

    private Integer totalCrystalPlanets(List<PlanetClusterizedHelperDto> planetsClusterized) {
        return planetsClusterized.stream()
                .filter(pc -> !this.waittingClusterizedPlanets.contains(pc.getPlanet().getId()))
                .map(PlanetClusterizedHelperDto::getResources)
                .map(PlanetsResourcesDto::getCrystal)
                .reduce(0, Integer::sum);
    }

    private Integer totalDeutPlanets(List<PlanetClusterizedHelperDto> planetsClusterized) {
        return planetsClusterized.stream()
                .filter(pc -> !this.waittingClusterizedPlanets.contains(pc.getPlanet().getId()))
                .map(PlanetClusterizedHelperDto::getResources)
                .map(PlanetsResourcesDto::getDeuterium)
                .reduce(0, Integer::sum);
    }
}
