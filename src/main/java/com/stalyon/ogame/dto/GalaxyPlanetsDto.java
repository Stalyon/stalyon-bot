package com.stalyon.ogame.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GalaxyPlanetsDto {

    @JsonProperty("ID")
    private Integer id;

    @JsonProperty("Name")
    private String name;

    @JsonProperty("Activity")
    private Integer activity;

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

    @JsonProperty("Player")
    private PlayerDto player;

    @JsonProperty("Moon")
    private GalaxyMoonDto moon;

    @JsonProperty("Alliance")
    private AllianceDto alliance;

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

    public Integer getActivity() {
        return activity;
    }

    public void setActivity(Integer activity) {
        this.activity = activity;
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

    public PlayerDto getPlayer() {
        return player;
    }

    public void setPlayer(PlayerDto player) {
        this.player = player;
    }

    public GalaxyMoonDto getMoon() {
        return moon;
    }

    public void setMoon(GalaxyMoonDto moon) {
        this.moon = moon;
    }

    public AllianceDto getAlliance() {
        return alliance;
    }

    public void setAlliance(AllianceDto alliance) {
        this.alliance = alliance;
    }
}
