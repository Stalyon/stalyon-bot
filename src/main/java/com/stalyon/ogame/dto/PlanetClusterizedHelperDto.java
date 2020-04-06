package com.stalyon.ogame.dto;

public class PlanetClusterizedHelperDto {

    private Integer ecoTime;
    private PlanetDto planet;
    private Integer buildingId;
    private PlanetsResourcesDto cost;
    private Boolean used;
    private PlanetsResourcesDto resources;

    public PlanetClusterizedHelperDto(Integer ecoTime, PlanetDto planet, Integer buildingId, PlanetsResourcesDto cost,
                                      PlanetsResourcesDto resources) {
        this.ecoTime = ecoTime;
        this.planet = planet;
        this.buildingId = buildingId;
        this.cost = cost;
        this.resources = resources;
        this.used = false;
    }

    public Integer getEcoTime() {
        return ecoTime;
    }

    public void setEcoTime(Integer ecoTime) {
        this.ecoTime = ecoTime;
    }

    public PlanetDto getPlanet() {
        return planet;
    }

    public void setPlanet(PlanetDto planet) {
        this.planet = planet;
    }

    public Integer getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(Integer buildingId) {
        this.buildingId = buildingId;
    }

    public PlanetsResourcesDto getCost() {
        return cost;
    }

    public void setCost(PlanetsResourcesDto cost) {
        this.cost = cost;
    }

    public Boolean getUsed() {
        return used;
    }

    public void setUsed(Boolean used) {
        this.used = used;
    }

    public PlanetsResourcesDto getResources() {
        return resources;
    }

    public void setResources(PlanetsResourcesDto resources) {
        this.resources = resources;
    }
}
