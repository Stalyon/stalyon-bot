package com.stalyon.ogame.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class FleetDto {

    @JsonProperty("ID")
    private Integer id;

    @JsonProperty("Mission")
    private Integer mission;

    @JsonProperty("ReturnFlight")
    private Boolean returnFlight;

    @JsonProperty("StartTime")
    private Date startTime;

    @JsonProperty("ArrivalTime")
    private Date arrivalTime;

    @JsonProperty("Origin")
    private CoordinateDto origin;

    @JsonProperty("Destination")
    private CoordinateDto destination;

    @JsonProperty("Ships")
    private ShipsDto ships;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMission() {
        return mission;
    }

    public void setMission(Integer mission) {
        this.mission = mission;
    }

    public Boolean getReturnFlight() {
        return returnFlight;
    }

    public void setReturnFlight(Boolean returnFlight) {
        this.returnFlight = returnFlight;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(Date arrivalTime) {
        this.arrivalTime = arrivalTime;
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

    public ShipsDto getShips() {
        return ships;
    }

    public void setShips(ShipsDto ships) {
        this.ships = ships;
    }
}
