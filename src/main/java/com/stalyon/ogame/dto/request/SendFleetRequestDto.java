package com.stalyon.ogame.dto.request;

import com.stalyon.ogame.dto.QuantifiableDto;

import java.util.List;

public class SendFleetRequestDto {

    private List<QuantifiableDto> ships;
    private Integer speed;
    private Integer galaxy;
    private Integer system;
    private Integer position;
    private Integer mission;
    private Integer metal;
    private Integer crystal;
    private Integer deuterium;

    public SendFleetRequestDto(List<QuantifiableDto> ships, Integer speed, Integer galaxy, Integer system, Integer position,
                               Integer mission, Integer metal, Integer crystal, Integer deuterium) {
        this.ships = ships;
        this.speed = speed;
        this.galaxy = galaxy;
        this.system = system;
        this.position = position;
        this.mission = mission;
        this.metal = metal;
        this.crystal = crystal;
        this.deuterium = deuterium;
    }

    public List<QuantifiableDto> getShips() {
        return ships;
    }

    public void setShips(List<QuantifiableDto> ships) {
        this.ships = ships;
    }

    public Integer getSpeed() {
        return speed;
    }

    public void setSpeed(Integer speed) {
        this.speed = speed;
    }

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

    public Integer getMission() {
        return mission;
    }

    public void setMission(Integer mission) {
        this.mission = mission;
    }

    public Integer getMetal() {
        return metal;
    }

    public void setMetal(Integer metal) {
        this.metal = metal;
    }

    public Integer getCrystal() {
        return crystal;
    }

    public void setCrystal(Integer crystal) {
        this.crystal = crystal;
    }

    public Integer getDeuterium() {
        return deuterium;
    }

    public void setDeuterium(Integer deuterium) {
        this.deuterium = deuterium;
    }
}
