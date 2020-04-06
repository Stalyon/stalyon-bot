package com.stalyon.ogame.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PlanetsResourcesDto {

    @JsonProperty("Metal")
    private Integer metal;

    @JsonProperty("Crystal")
    private Integer crystal;

    @JsonProperty("Deuterium")
    private Integer deuterium;

    @JsonProperty("Energy")
    private Integer energy;

    @JsonProperty("Darkmatter")
    private Integer darkmatter;

    public PlanetsResourcesDto() {
        // Do nothing
    }

    public PlanetsResourcesDto(Integer metal, Integer crystal, Integer deuterium, Integer energy) {
        this.metal = metal;
        this.crystal = crystal;
        this.deuterium = deuterium;
        this.energy = energy;
    }

    public Integer getMetal() {
        return metal;
    }

    public void setMetal(Integer metal) {
        this.metal = metal;
    }

    public Integer getCrystal() {
        return crystal;
    }

    public void setCrystal(Integer crystal) {
        this.crystal = crystal;
    }

    public Integer getDeuterium() {
        return deuterium;
    }

    public void setDeuterium(Integer deuterium) {
        this.deuterium = deuterium;
    }

    public Integer getEnergy() {
        return energy;
    }

    public void setEnergy(Integer energy) {
        this.energy = energy;
    }

    public Integer getDarkmatter() {
        return darkmatter;
    }

    public void setDarkmatter(Integer darkmatter) {
        this.darkmatter = darkmatter;
    }
}
