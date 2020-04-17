package com.stalyon.ogame.utils;

import com.stalyon.ogame.dto.PlanetsResourcesBuildingsDto;
import com.stalyon.ogame.dto.PlanetsResourcesDto;

public class ResourcesBuildingsUtils {

    public static Boolean canBuildMetalMine(Integer metalMine, PlanetsResourcesDto resources) {
        PlanetsResourcesDto cost = ResourcesBuildingsUtils.getMetalMineCost(metalMine);

        return resources.getEnergy() > cost.getEnergy() && resources.getMetal() > cost.getMetal()
                && resources.getCrystal() > cost.getCrystal();
    }

    public static Integer getEcoTime(PlanetsResourcesDto cost, PlanetsResourcesBuildingsDto resourcesBuildings, PlanetsResourcesDto resources,
                                     Integer serverSpeed, Boolean hasGeologue) {
        Integer totalMetal = cost.getMetal() - resources.getMetal();
        Integer totalCrystal = cost.getCrystal() - resources.getCrystal();
        Integer totalDeut = cost.getDeuterium() - resources.getDeuterium();

        Integer metalEcoTime = 0;
        Integer crystalEcoTime = 0;
        Integer deutEcoTime = 0;

        if (totalMetal > 0) {
            metalEcoTime = (int) Math.round((double) totalMetal / (double) ResourcesBuildingsUtils.calculateMetalMineProduction(resourcesBuildings.getMetalMine(),
                    serverSpeed, hasGeologue) * 60.);
        }
        if (totalCrystal > 0) {
            crystalEcoTime = (int) Math.round((double) totalCrystal / (double) ResourcesBuildingsUtils.calculateCrystalMineProduction(resourcesBuildings.getCrystalMine(),
                    serverSpeed, hasGeologue) * 60.);
        }
        if (totalDeut > 0) {
            deutEcoTime = (int) Math.round((double) totalDeut / (double) ResourcesBuildingsUtils.calculateSyntheDeutProduction(resourcesBuildings.getDeuteriumSynthesizer(),
                    serverSpeed, hasGeologue) * 60.);
        }

        return Math.max(metalEcoTime, Math.max(crystalEcoTime, deutEcoTime));
    }

    public static Boolean getMetalMineEnoughEnergy(Integer metalMine, PlanetsResourcesDto resources) {
        PlanetsResourcesDto cost = ResourcesBuildingsUtils.getMetalMineCost(metalMine);

        return resources.getEnergy() > cost.getEnergy();
    }

    public static Boolean canBuildCrystalMine(Integer crystalMine, PlanetsResourcesDto resources) {
        PlanetsResourcesDto cost = ResourcesBuildingsUtils.getCrystalMineCost(crystalMine);

        return resources.getEnergy() > cost.getEnergy() && resources.getMetal() > cost.getMetal()
                && resources.getCrystal() > cost.getCrystal();
    }

    public static Boolean getCrystalMineEnoughEnergy(Integer crystalMine, PlanetsResourcesDto resources) {
        PlanetsResourcesDto cost = ResourcesBuildingsUtils.getCrystalMineCost(crystalMine);

        return resources.getEnergy() > cost.getEnergy();
    }

    public static Boolean canBuildDeutSynth(Integer deutSynth, PlanetsResourcesDto resources) {
        PlanetsResourcesDto cost = ResourcesBuildingsUtils.getDeutSynthCost(deutSynth);

        return resources.getEnergy() > cost.getEnergy() && resources.getMetal() > cost.getMetal()
                && resources.getCrystal() > cost.getCrystal();
    }

    public static Boolean getDeutSynthEnoughEnergy(Integer deutSynth, PlanetsResourcesDto resources) {
        PlanetsResourcesDto cost = ResourcesBuildingsUtils.getDeutSynthCost(deutSynth);

        return resources.getEnergy() > cost.getEnergy();
    }

    public static Boolean canBuildCentraleSolaire(Integer centraleSolaire, PlanetsResourcesDto resources) {
        PlanetsResourcesDto cost = ResourcesBuildingsUtils.getCentraleSolaireCost(centraleSolaire);

        return resources.getMetal() > cost.getMetal() && resources.getCrystal() > cost.getCrystal();
    }

    public static Boolean canBuildFusionReactor(Integer fusionReactor, PlanetsResourcesDto resources, Integer energyTech) {
        PlanetsResourcesDto cost = ResourcesBuildingsUtils.getFusionReactorCost(fusionReactor, energyTech);

        return resources.getMetal() > cost.getMetal() && resources.getCrystal() > cost.getCrystal()
                && resources.getDeuterium() > cost.getDeuterium();
    }

    public static PlanetsResourcesDto getMetalMineCost(Integer metalMine) {
        return new PlanetsResourcesDto(
                (int) Math.round(60 * Math.pow(1.5, metalMine - 1)),
                (int) Math.round(15 * Math.pow(1.5, metalMine - 1)),
                0,
                (int) Math.round(10 * metalMine * Math.pow(1.1, metalMine))
                        - (int) Math.round(10 * (metalMine - 1) * Math.pow(1.1, (metalMine - 1)))
        );
    }

    public static PlanetsResourcesDto getCrystalMineCost(Integer crystalMine) {
        return new PlanetsResourcesDto(
                (int) Math.round(48 * Math.pow(1.6, crystalMine - 1)),
                (int) Math.round(24 * Math.pow(1.6, crystalMine - 1)),
                0,
                (int) Math.round(10 * crystalMine * Math.pow(1.1, crystalMine))
                        - (int) Math.round(10 * (crystalMine - 1) * Math.pow(1.1, (crystalMine - 1)))
        );
    }

    public static PlanetsResourcesDto getDeutSynthCost(Integer deutSynth) {
        return new PlanetsResourcesDto(
                (int) Math.round(225 * Math.pow(1.5, deutSynth - 1)),
                (int) Math.round(75 * Math.pow(1.5, deutSynth - 1)),
                0,
                (int) Math.round(20 * deutSynth * Math.pow(1.1, deutSynth))
                        - (int) Math.round(20 * (deutSynth - 1) * Math.pow(1.1, (deutSynth - 1)))
        );
    }

    public static PlanetsResourcesDto getCentraleSolaireCost(Integer centraleSolaireMine) {
        return new PlanetsResourcesDto(
                (int) Math.round(75 * Math.pow(1.5, centraleSolaireMine - 1)),
                (int) Math.round(30 * Math.pow(1.5, centraleSolaireMine - 1)),
                0,
                (int) Math.round(20 * centraleSolaireMine * Math.pow(1.1, centraleSolaireMine))
        );
    }

    public static PlanetsResourcesDto getFusionReactorCost(Integer fusionReactor, Integer energyTech) {
        return new PlanetsResourcesDto(
                (int) Math.round(900 * Math.pow(1.8, fusionReactor - 1)),
                (int) Math.round(360 * Math.pow(1.8, fusionReactor - 1)),
                (int) Math.round(180 * Math.pow(1.8, fusionReactor - 1)),
                (int) Math.round(30 * fusionReactor * Math.pow((1.05 + Math.pow(energyTech, 0.01)), fusionReactor))
        );
    }

    public static Boolean notEnoughEnergy(PlanetsResourcesBuildingsDto resourcesBuildings, PlanetsResourcesDto resources, Boolean autoBuildDeut) {
        return !(ResourcesBuildingsUtils.getMetalMineEnoughEnergy(resourcesBuildings.getMetalMine() + 1, resources)
                || ResourcesBuildingsUtils.getCrystalMineEnoughEnergy(resourcesBuildings.getCrystalMine() + 1, resources)
                || autoBuildDeut && ResourcesBuildingsUtils.getDeutSynthEnoughEnergy(resourcesBuildings.getDeuteriumSynthesizer() + 1, resources));
    }

    private static Integer calculateMetalMineProduction(Integer metalMine, Integer serverSpeed, Boolean hasGeologue) {
        Double geologue = hasGeologue ? 1.1 : 1;
        Integer prodBase = 30 * serverSpeed;
        Integer prodMine = (int) Math.round(30 * metalMine * Math.pow(1.1, metalMine) * geologue) * serverSpeed;

        return prodBase + prodMine;
    }

    private static Integer calculateCrystalMineProduction(Integer crystalMine, Integer serverSpeed, Boolean hasGeologue) {
        Double geologue = hasGeologue ? 1.1 : 1;
        Integer prodBase = 15 * serverSpeed;
        Integer prodMine = (int) Math.round(20 * crystalMine * Math.pow(1.1, crystalMine) * geologue) * serverSpeed;

        return prodBase + prodMine;
    }

    private static Integer calculateSyntheDeutProduction(Integer syntheDeut, Integer serverSpeed, Boolean hasGeologue) {
        // TODO Pierre: calculer la production (possibilité de récupérer la prod via le bot ?)

        return 0;
    }
}
