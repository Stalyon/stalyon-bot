package com.stalyon.ogame.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PlanetDto {

    @JsonProperty("ID")
    private Integer id;

    @JsonProperty("Name")
    private String name;

    @JsonProperty("Coordinate")
    private CoordinateDto coordinate;

    @JsonProperty("Moon")
    private MoonDto moon;

    @JsonIgnore
    private Boolean isMoon;

    public PlanetDto() {
        // Do nothing
    }

    public PlanetDto(Integer id, String name, CoordinateDto coordinate, MoonDto moon, Boolean isMoon) {
        this.id = id;
        this.name = name;
        this.coordinate = coordinate;
        this.moon = moon;
        this.isMoon = isMoon;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CoordinateDto getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(CoordinateDto coordinate) {
        this.coordinate = coordinate;
    }

    public MoonDto getMoon() {
        return moon;
    }

    public void setMoon(MoonDto moon) {
        this.moon = moon;
    }

    public Boolean getIsMoon() {
        return isMoon;
    }

    public void setIsMoon(Boolean isMoon) {
        this.isMoon = isMoon;
    }
}
