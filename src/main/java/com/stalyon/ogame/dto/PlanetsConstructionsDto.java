package com.stalyon.ogame.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PlanetsConstructionsDto {

    @JsonProperty("BuildingID")
    private Integer buildingID;

    @JsonProperty("BuildingCountdown")
    private Integer buildingCountdown;

    @JsonProperty("ResearchID")
    private Integer researchID;

    @JsonProperty("ResearchCountdown")
    private Integer researchCountdown;

    public PlanetsConstructionsDto() {
        // Do nothing
    }

    public Integer getBuildingID() {
        return buildingID;
    }

    public void setBuildingID(Integer buildingID) {
        this.buildingID = buildingID;
    }

    public Integer getBuildingCountdown() {
        return buildingCountdown;
    }

    public void setBuildingCountdown(Integer buildingCountdown) {
        this.buildingCountdown = buildingCountdown;
    }

    public Integer getResearchID() {
        return researchID;
    }

    public void setResearchID(Integer researchID) {
        this.researchID = researchID;
    }

    public Integer getResearchCountdown() {
        return researchCountdown;
    }

    public void setResearchCountdown(Integer researchCountdown) {
        this.researchCountdown = researchCountdown;
    }
}
