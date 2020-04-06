package com.stalyon.ogame.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GalaxyPlanetsDto {

    @JsonProperty("ID")
    private Integer id;

    @JsonProperty("Name")
    private String name;

    @JsonProperty("Coordinate")
    private CoordinateDto coordinate;

    @JsonProperty("Inactive")
    private Boolean inactive;

    @JsonProperty("Administrator")
    private Boolean administrator;

    @JsonProperty("Vacation")
    private Boolean vacation;

    @JsonProperty("StrongPlayer")
    private Boolean strongPlayer;

    @JsonProperty("Newbie")
    private Boolean newbie;

    @JsonProperty("Banned")
    private Boolean banned;

    public GalaxyPlanetsDto() {
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

    public Boolean getInactive() {
        return inactive;
    }

    public void setInactive(Boolean inactive) {
        this.inactive = inactive;
    }

    public Boolean getAdministrator() {
        return administrator;
    }

    public void setAdministrator(Boolean administrator) {
        this.administrator = administrator;
    }

    public Boolean getVacation() {
        return vacation;
    }

    public void setVacation(Boolean vacation) {
        this.vacation = vacation;
    }

    public Boolean getStrongPlayer() {
        return strongPlayer;
    }

    public void setStrongPlayer(Boolean strongPlayer) {
        this.strongPlayer = strongPlayer;
    }

    public Boolean getNewbie() {
        return newbie;
    }

    public void setNewbie(Boolean newbie) {
        this.newbie = newbie;
    }

    public Boolean getBanned() {
        return banned;
    }

    public void setBanned(Boolean banned) {
        this.banned = banned;
    }
}
