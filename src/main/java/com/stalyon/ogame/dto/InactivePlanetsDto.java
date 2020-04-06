package com.stalyon.ogame.dto;

public class InactivePlanetsDto {

    private CoordinateDto coordinate;
    private PlanetsResourcesDto resources;

    public InactivePlanetsDto(CoordinateDto coordinate) {
        this.coordinate = coordinate;
    }

    public InactivePlanetsDto(CoordinateDto coordinate, PlanetsResourcesDto resources) {
        this.coordinate = coordinate;
        this.resources = resources;
    }

    public CoordinateDto getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(CoordinateDto coordinate) {
        this.coordinate = coordinate;
    }

    public PlanetsResourcesDto getResources() {
        return resources;
    }

    public void setResources(PlanetsResourcesDto resources) {
        this.resources = resources;
    }
}
