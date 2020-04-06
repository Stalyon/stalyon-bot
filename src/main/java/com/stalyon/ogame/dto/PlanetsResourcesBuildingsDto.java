package com.stalyon.ogame.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PlanetsResourcesBuildingsDto {

    @JsonProperty("MetalMine")
    private Integer metalMine;

    @JsonProperty("CrystalMine")
    private Integer crystalMine;

    @JsonProperty("DeuteriumSynthesizer")
    private Integer deuteriumSynthesizer;

    @JsonProperty("SolarPlant")
    private Integer solarPlant;

    @JsonProperty("FusionReactor")
    private Integer fusionReactor;

    @JsonProperty("SolarSatellite")
    private Integer solarSatellite;

    @JsonProperty("MetalStorage")
    private Integer metalStorage;

    @JsonProperty("CrystalStorage")
    private Integer crystalStorage;

    @JsonProperty("DeuteriumTank")
    private Integer deuteriumTank;

    public PlanetsResourcesBuildingsDto() {
        // Do nothing
    }

    public Integer getMetalMine() {
        return metalMine;
    }

    public void setMetalMine(Integer metalMine) {
        this.metalMine = metalMine;
    }

    public Integer getCrystalMine() {
        return crystalMine;
    }

    public void setCrystalMine(Integer crystalMine) {
        this.crystalMine = crystalMine;
    }

    public Integer getDeuteriumSynthesizer() {
        return deuteriumSynthesizer;
    }

    public void setDeuteriumSynthesizer(Integer deuteriumSynthesizer) {
        this.deuteriumSynthesizer = deuteriumSynthesizer;
    }

    public Integer getSolarPlant() {
        return solarPlant;
    }

    public void setSolarPlant(Integer solarPlant) {
        this.solarPlant = solarPlant;
    }

    public Integer getFusionReactor() {
        return fusionReactor;
    }

    public void setFusionReactor(Integer fusionReactor) {
        this.fusionReactor = fusionReactor;
    }

    public Integer getSolarSatellite() {
        return solarSatellite;
    }

    public void setSolarSatellite(Integer solarSatellite) {
        this.solarSatellite = solarSatellite;
    }

    public Integer getMetalStorage() {
        return metalStorage;
    }

    public void setMetalStorage(Integer metalStorage) {
        this.metalStorage = metalStorage;
    }

    public Integer getCrystalStorage() {
        return crystalStorage;
    }

    public void setCrystalStorage(Integer crystalStorage) {
        this.crystalStorage = crystalStorage;
    }

    public Integer getDeuteriumTank() {
        return deuteriumTank;
    }

    public void setDeuteriumTank(Integer deuteriumTank) {
        this.deuteriumTank = deuteriumTank;
    }
}
