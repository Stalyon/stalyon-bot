package com.stalyon.ogame.dto;

public class ClusterizedPlanetDto {

    private PlanetDto planet;
    private PlanetsResourcesDto resources;
    private PlanetsResourcesBuildingsDto resourcesBuildings;

    public ClusterizedPlanetDto(PlanetDto planet, PlanetsResourcesDto resources, PlanetsResourcesBuildingsDto resourcesBuildings) {
        this.planet = planet;
        this.resources = resources;
        this.resourcesBuildings = resourcesBuildings;
    }

    public PlanetDto getPlanet() {
        return planet;
    }

    public void setPlanet(PlanetDto planet) {
        this.planet = planet;
    }

    public PlanetsResourcesDto getResources() {
        return resources;
    }

    public void setResources(PlanetsResourcesDto resources) {
        this.resources = resources;
    }

    public PlanetsResourcesBuildingsDto getResourcesBuildings() {
        return resourcesBuildings;
    }

    public void setResourcesBuildings(PlanetsResourcesBuildingsDto resourcesBuildings) {
        this.resourcesBuildings = resourcesBuildings;
    }
}
