package com.stalyon.ogame.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MoonDto {

    @JsonProperty("ID")
    private Integer id;

    @JsonProperty("Name")
    private String name;

    @JsonProperty("Coordinate")
    private CoordinateDto coordinate;

    public MoonDto() {
        // Do nothing
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
}
