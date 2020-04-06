package com.stalyon.ogame.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class GalaxyInfosDto {

    @JsonProperty("Galaxy")
    private Integer galaxy;

    @JsonProperty("System")
    private Integer system;

    @JsonProperty("Planets")
    private List<GalaxyPlanetsDto> planets;

    @JsonProperty("ExpeditionDebris")
    private ExpeditionDebrisDto expeditionDebris;

    public GalaxyInfosDto() {
        // Do nothing
    }

    public Integer getGalaxy() {
        return galaxy;
    }

    public void setGalaxy(Integer galaxy) {
        this.galaxy = galaxy;
    }

    public Integer getSystem() {
        return system;
    }

    public void setSystem(Integer system) {
        this.system = system;
    }

    public List<GalaxyPlanetsDto> getPlanets() {
        return planets;
    }

    public void setPlanets(List<GalaxyPlanetsDto> planets) {
        this.planets = planets;
    }

    public ExpeditionDebrisDto getExpeditionDebris() {
        return expeditionDebris;
    }

    public void setExpeditionDebris(ExpeditionDebrisDto expeditionDebris) {
        this.expeditionDebris = expeditionDebris;
    }
}
