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
    private ResourcesBuildingsUtils resourcesBuildingsUtils;

    @Autowired
    private SlotsService slotsService;

    private List<Integer> waittingClusterizedPlanets = new ArrayList<>();

    @Scheduled(cron = "0 0/2 * * * *") // every minute
    public void autoBuild() {
        if (this.ogameProperties.MINES_AUTO_BUILD_ENABLE) {
            // Récupération des planètes
            List<PlanetDto> planets = this.ogameApiService.getPlanets();

            // Filtrage sur les planètes non exclues
            planets.stream()
                    .filter(planet -> this.ogameProperties.MINES_EXCLUDED_PLANETS == null
                            || this.ogameProperties.MINES_EXCLUDED_PLANETS.stream().noneMatch(p -> p.equals(planet.getId())))
                    .filter(planet -> this.ogameApiService.getPlanetsConstructions(planet).getBuildingID() == 0)
                    .forEach(planet -> {
                        // Récupération des ressources pour la planète
                        PlanetsResourcesDto resources = this.ogameApiService.getPlanetsResources(planet);

                        // Récupération des bâtiments de type resources
                        PlanetsResourcesBuildingsDto resourcesBuildings =
                                this.ogameApiService.getPlanetsResourcesBuildings(planet);

                        boolean launched = false;
                        if (this.resourcesBuildingsUtils.canBuildMetalMine(resourcesBuildings.getMetalMine(), resources)) {
                            // Si le niveau de la mine de métal est inférieur au paramétrage et qu'il y a assez de ressources : construction
                            this.messageService.logInfo("Construction Mine Métal " + (resourcesBuildings.getMetalMine() + 1) + " sur " + planet.getName(), Boolean.FALSE, Boolean.FALSE);

                            MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
                            this.ogameApiService.buildBuilding(planet.getId(), OgameCst.METAL_MINE_ID, formData);
                            launched = true;
                        } else if (this.resourcesBuildingsUtils.canBuildCrystalMine(resourcesBuildings.getCrystalMine(), resources)) {
                            // Si le niveau de la mine de cristal est inférieur au paramétrage et qu'il y a assez de ressources : construction
                            this.messageService.logInfo("Construction Mine Cristal " + (resourcesBuildings.getCrystalMine() + 1) + " sur " + planet.getName(), Boolean.FALSE, Boolean.FALSE);

                            MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
                            this.ogameApiService.buildBuilding(planet.getId(), OgameCst.CRYSTAL_MINE_ID, formData);
                            launched = true;
                        } else if (this.resourcesBuildingsUtils.canBuildDeutSynth(resourcesBuildings.getDeuteriumSynthesizer(), resources)) {
                            // Si le niveau du synthé est inférieur au paramétrage et qu'il y a assez de ressources : construction
                            this.messageService.logInfo("Construction Synthé Deut " + (resourcesBuildings.getDeuteriumSynthesizer() + 1) + " sur " + planet.getName(), Boolean.FALSE, Boolean.FALSE);

                            MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
                            this.ogameApiService.buildBuilding(planet.getId(), OgameCst.DEUTERIUM_SYNTHESIZER_ID, formData);
                            launched = true;
                        } else if (this.resourcesBuildingsUtils.notEnoughEnergy(resourcesBuildings, resources)) {
                            if (this.resourcesBuildingsUtils.canBuildCentraleSolaire(resourcesBuildings.getSolarPlant(), resources)) {
                                // S'il n'y a pas assez d'énergie pour construire une mine, que le niveau de la CES est inférieur au paramétrage, et qu'il y a assez de ressources : construction
                                this.messageService.logInfo("Construction Centrale Solaire " + (resourcesBuildings.getSolarPlant() + 1) + " sur " + planet.getName(), Boolean.FALSE, Boolean.FALSE);

                                MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
                                this.ogameApiService.buildBuilding(planet.getId(), OgameCst.SOLAR_PLANT_ID, formData);
                                launched = true;
                            } else if (this.resourcesBuildingsUtils.canBuildFusionReactor(resourcesBuildings.getFusionReactor(), resources)) {
                                // S'il n'y a pas assez d'énergie pour construire une mine, que le niveau de la CEF est inférieur au paramétrage, et qu'il y a assez de ressources : construction
                                this.messageService.logInfo("Construction Réacteur Fusion " + (resourcesBuildings.getFusionReactor() + 1) + " sur " + planet.getName(), Boolean.FALSE, Boolean.FALSE);

                                MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
                                this.ogameApiService.buildBuilding(planet.getId(), OgameCst.FUSION_REACTOR_ID, formData);
                                launched = true;
                            }
                        }

                        if (launched) {
                            this.waittingClusterizedPlanets.remove(planet.getId());
                        }
                    });
        }
    }

    @Scheduled(cron = "50 0/15 * * * *") // every 15-minutes
    public void autoBuildTransport() {
        if (this.ogameProperties.MINES_AUTO_BUILD_TRANSPORT) {
            // Récupération des planètes / lunes
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

            // Filtrage sur les planètes (pas lunes) clusterizées et non exclues, et qui n'ont pas de construction en cours
            planets.stream()
                    .filter(planet -> this.ogameProperties.MINES_EXCLUDED_PLANETS == null
                            || this.ogameProperties.MINES_EXCLUDED_PLANETS.stream().noneMatch(p -> p.equals(planet.getId())))
                    .filter(planet -> !this.waittingClusterizedPlanets.contains(planet.getId()))
                    .filter(planet -> this.ogameProperties.MINES_CLUSTERIZED_PLANETS != null
                            && this.ogameProperties.MINES_CLUSTERIZED_PLANETS.stream().anyMatch(p -> p.equals(planet.getId())))
                    .filter(planet -> planet.getIsMoon() == null || !planet.getIsMoon())
                    .filter(planet -> this.ogameApiService.getPlanetsConstructions(planet).getBuildingID() == 0)
                    .forEach(planet -> {
                        // Récupération des ressources pour la planète
                        PlanetsResourcesDto resources = this.ogameApiService.getPlanetsResources(planet);

                        // Récupération des bâtiments de type resources
                        PlanetsResourcesBuildingsDto resourcesBuildings =
                                this.ogameApiService.getPlanetsResourcesBuildings(planet);

                        // Vérification que la planète ne puisse pas construire de bâtiment
                        if (!(this.resourcesBuildingsUtils.canBuildMetalMine(resourcesBuildings.getMetalMine(), resources)
                                || this.resourcesBuildingsUtils.canBuildCrystalMine(resourcesBuildings.getCrystalMine(), resources)
                                || this.resourcesBuildingsUtils.canBuildDeutSynth(resourcesBuildings.getDeuteriumSynthesizer(), resources)
                                || this.resourcesBuildingsUtils.notEnoughEnergy(resourcesBuildings, resources)
                                && (this.resourcesBuildingsUtils.canBuildCentraleSolaire(resourcesBuildings.getSolarPlant(), resources)
                                || resourcesBuildings.getSolarPlant() >= this.ogameProperties.MINES_SOLAR_PLANT_MAX && this.resourcesBuildingsUtils.canBuildFusionReactor(resourcesBuildings.getFusionReactor(), resources)))) {
                            // Calculer le prochain bâtiment à lancer
                            PlanetsResourcesDto metalMineCost = ResourcesBuildingsUtils.getMetalMineCost(resourcesBuildings.getMetalMine() + 1);
                            Integer metalMineEcoTime = this.resourcesBuildingsUtils
                                    .getEcoTime(metalMineCost, resourcesBuildings, resources);

                            PlanetsResourcesDto crystalMineCost = ResourcesBuildingsUtils.getCrystalMineCost(resourcesBuildings.getCrystalMine() + 1);
                            Integer crystalMineEcoTime = this.resourcesBuildingsUtils
                                    .getEcoTime(crystalMineCost, resourcesBuildings, resources);

                            PlanetsResourcesDto deutSynthCost = ResourcesBuildingsUtils.getDeutSynthCost(resourcesBuildings.getDeuteriumSynthesizer() + 1);
                            Integer deutSynthEcoTime = this.resourcesBuildingsUtils
                                    .getEcoTime(deutSynthCost, resourcesBuildings, resources);

                            PlanetsResourcesDto centraleSolaireCost = ResourcesBuildingsUtils.getCentraleSolaireCost(resourcesBuildings.getSolarPlant() + 1);
                            Integer centraleSolaireEcoTime = this.resourcesBuildingsUtils
                                    .getEcoTime(centraleSolaireCost, resourcesBuildings, resources);

                            PlanetsResourcesDto reactorFusionCost = this.resourcesBuildingsUtils.getFusionReactorCost(resourcesBuildings.getFusionReactor() + 1);
                            Integer reactorFusionEcoTime = this.resourcesBuildingsUtils
                                    .getEcoTime(reactorFusionCost, resourcesBuildings, resources);

                            if (resourcesBuildings.getMetalMine() >= this.ogameProperties.MINES_MINE_METAL_MAX
                                    || !ResourcesBuildingsUtils.getMetalMineEnoughEnergy(resourcesBuildings.getMetalMine() + 1, resources)) {
                                metalMineEcoTime = Integer.MAX_VALUE;
                            }
                            if (resourcesBuildings.getCrystalMine() >= this.ogameProperties.MINES_MINE_CRISTAL_MAX
                                    || !ResourcesBuildingsUtils.getCrystalMineEnoughEnergy(resourcesBuildings.getCrystalMine() + 1, resources)) {
                                crystalMineEcoTime = Integer.MAX_VALUE;
                            }
                            if (resourcesBuildings.getDeuteriumSynthesizer() >= this.ogameProperties.MINES_SYNTHE_DEUT_MAX
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
                            } else if (resourcesBuildings.getFusionReactor() < this.ogameProperties.MINES_REACTOR_FUSION_MAX) {
                                // Prochain bâtiment = Réacteur de fusion
                                planetsClusterized.add(new PlanetClusterizedHelperDto(reactorFusionEcoTime, planet, OgameCst.FUSION_REACTOR_ID, reactorFusionCost, resources));
                            } else {
                                planetsClusterized.add(new PlanetClusterizedHelperDto(null, planet, null, null, resources));
                            }
                        }
                    });

            // Filtrage sur les planètes clusterizées et non exclues, qui ont un bâtiment en construction ou s'il s'agit d'une lune
            planets.stream()
                    .filter(planet -> this.ogameProperties.MINES_EXCLUDED_PLANETS == null
                            || this.ogameProperties.MINES_EXCLUDED_PLANETS.stream().noneMatch(p -> p.equals(planet.getId())))
                    .filter(planet -> !this.waittingClusterizedPlanets.contains(planet.getId()))
                    .filter(planet -> this.ogameProperties.MINES_CLUSTERIZED_PLANETS != null
                            && this.ogameProperties.MINES_CLUSTERIZED_PLANETS.stream().anyMatch(p -> p.equals(planet.getId())))
                    .filter(planet -> this.ogameApiService.getPlanetsConstructions(planet).getBuildingID() != 0
                            || (planet.getIsMoon() != null && planet.getIsMoon()))
                    .forEach(planet -> {
                        // Récupération des ressources pour la planète
                        PlanetsResourcesDto resources = this.ogameApiService.getPlanetsResources(planet);

                        // Ajout dans la liste des planètes/lunes pour le transport
                        planetsClusterized.add(new PlanetClusterizedHelperDto(null, planet, null, null, resources));
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
