package com.stalyon.ogame.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ExpeditionDebrisDto {

    @JsonProperty("Metal")
    private Integer metal;

    @JsonProperty("Crystal")
    private Integer crystal;

    @JsonProperty("PathfindersNeeded")
    private Integer pathfindersNeeded;

    public ExpeditionDebrisDto() {
        // Do nothing
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

    public Integer getPathfindersNeeded() {
        return pathfindersNeeded;
    }

    public void setPathfindersNeeded(Integer pathfindersNeeded) {
        this.pathfindersNeeded = pathfindersNeeded;
    }
}
