package com.stalyon.ogame.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CoordinateDto {

    @JsonProperty("Galaxy")
    private Integer galaxy;

    @JsonProperty("System")
    private Integer system;

    @JsonProperty("Position")
    private Integer position;

    @JsonProperty("Type")
    private Integer type;

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

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
