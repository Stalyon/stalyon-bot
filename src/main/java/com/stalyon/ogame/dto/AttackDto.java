package com.stalyon.ogame.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class AttackDto {

    @JsonProperty("ID")
    private Integer id;

    @JsonProperty("MissionType")
    private Integer missionType;

    @JsonProperty("Origin")
    private CoordinateDto origin;

    @JsonProperty("Destination")
    private CoordinateDto destination;

    @JsonProperty("ArrivalTime")
    private Date arrivalTime;

    @JsonProperty("AttackerName")
    private String attackerName;

    @JsonProperty("Ships")
    private ShipsDto ships;

    public AttackDto() {
        // Do nothing
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMissionType() {
        return missionType;
    }

    public void setMissionType(Integer missionType) {
        this.missionType = missionType;
    }

    public CoordinateDto getOrigin() {
        return origin;
    }

    public void setOrigin(CoordinateDto origin) {
        this.origin = origin;
    }

    public CoordinateDto getDestination() {
        return destination;
    }

    public void setDestination(CoordinateDto destination) {
        this.destination = destination;
    }

    public Date getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(Date arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public String getAttackerName() {
        return attackerName;
    }

    public void setAttackerName(String attackerName) {
        this.attackerName = attackerName;
    }

    public ShipsDto getShips() {
        return ships;
    }

    public void setShips(ShipsDto ships) {
        this.ships = ships;
    }
}
