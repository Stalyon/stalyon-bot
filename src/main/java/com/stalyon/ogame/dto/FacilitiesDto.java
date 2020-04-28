package com.stalyon.ogame.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FacilitiesDto {

    @JsonProperty("RoboticsFactory")
    private Integer roboticsFactory;

    @JsonProperty("Shipyard")
    private Integer shipyard;

    @JsonProperty("ResearchLab")
    private Integer researchLab;

    @JsonProperty("AllianceDepot")
    private Integer allianceDepot;

    @JsonProperty("MissileSilo")
    private Integer missileSilo;

    @JsonProperty("NaniteFactory")
    private Integer naniteFactory;

    @JsonProperty("Terraformer")
    private Integer terraformer;

    @JsonProperty("SpaceDock")
    private Integer spaceDock;

    @JsonProperty("LunarBase")
    private Integer lunarBase;

    @JsonProperty("SensorPhalanx")
    private Integer sensorPhalanx;

    @JsonProperty("JumpGate")
    private Integer jumpGate;

    public FacilitiesDto() {
        // Do nothing
    }

    public Integer getRoboticsFactory() {
        return roboticsFactory;
    }

    public void setRoboticsFactory(Integer roboticsFactory) {
        this.roboticsFactory = roboticsFactory;
    }

    public Integer getShipyard() {
        return shipyard;
    }

    public void setShipyard(Integer shipyard) {
        this.shipyard = shipyard;
    }

    public Integer getResearchLab() {
        return researchLab;
    }

    public void setResearchLab(Integer researchLab) {
        this.researchLab = researchLab;
    }

    public Integer getAllianceDepot() {
        return allianceDepot;
    }

    public void setAllianceDepot(Integer allianceDepot) {
        this.allianceDepot = allianceDepot;
    }

    public Integer getMissileSilo() {
        return missileSilo;
    }

    public void setMissileSilo(Integer missileSilo) {
        this.missileSilo = missileSilo;
    }

    public Integer getNaniteFactory() {
        return naniteFactory;
    }

    public void setNaniteFactory(Integer naniteFactory) {
        this.naniteFactory = naniteFactory;
    }

    public Integer getTerraformer() {
        return terraformer;
    }

    public void setTerraformer(Integer terraformer) {
        this.terraformer = terraformer;
    }

    public Integer getSpaceDock() {
        return spaceDock;
    }

    public void setSpaceDock(Integer spaceDock) {
        this.spaceDock = spaceDock;
    }

    public Integer getLunarBase() {
        return lunarBase;
    }

    public void setLunarBase(Integer lunarBase) {
        this.lunarBase = lunarBase;
    }

    public Integer getSensorPhalanx() {
        return sensorPhalanx;
    }

    public void setSensorPhalanx(Integer sensorPhalanx) {
        this.sensorPhalanx = sensorPhalanx;
    }

    public Integer getJumpGate() {
        return jumpGate;
    }

    public void setJumpGate(Integer jumpGate) {
        this.jumpGate = jumpGate;
    }
}
