package com.stalyon.ogame.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class SendFleetDto {

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

    @JsonProperty("BackTime")
    private Date backTime;

    public SendFleetDto() {
        // Do nothing
    }

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

    public Date getBackTime() {
        return backTime;
    }

    public void setBackTime(Date backTime) {
        this.backTime = backTime;
    }
}
