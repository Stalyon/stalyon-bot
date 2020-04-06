package com.stalyon.ogame.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ShipsDto {

    @JsonProperty("LightFighter")
    private Integer lightFighter;

    @JsonProperty("HeavyFighter")
    private Integer heavyFighter;

    @JsonProperty("Cruiser")
    private Integer cruiser;

    @JsonProperty("Battleship")
    private Integer battleship;

    @JsonProperty("Battlecruiser")
    private Integer battlecruiser;

    @JsonProperty("Bomber")
    private Integer bomber;

    @JsonProperty("Destroyer")
    private Integer destroyer;

    @JsonProperty("Deathstar")
    private Integer deathstar;

    @JsonProperty("SmallCargo")
    private Integer smallCargo;

    @JsonProperty("LargeCargo")
    private Integer largeCargo;

    @JsonProperty("ColonyShip")
    private Integer colonyShip;

    @JsonProperty("Recycler")
    private Integer recycler;

    @JsonProperty("EspionageProbe")
    private Integer espionageProbe;

    @JsonProperty("SolarSatellite")
    private Integer solarSatellite;

    @JsonProperty("Crawler")
    private Integer crawler;

    @JsonProperty("Reaper")
    private Integer reaper;

    @JsonProperty("Pathfinder")
    private Integer pathfinder;

    public Integer getLightFighter() {
        return lightFighter;
    }

    public void setLightFighter(Integer lightFighter) {
        this.lightFighter = lightFighter;
    }

    public Integer getHeavyFighter() {
        return heavyFighter;
    }

    public void setHeavyFighter(Integer heavyFighter) {
        this.heavyFighter = heavyFighter;
    }

    public Integer getCruiser() {
        return cruiser;
    }

    public void setCruiser(Integer cruiser) {
        this.cruiser = cruiser;
    }

    public Integer getBattleship() {
        return battleship;
    }

    public void setBattleship(Integer battleship) {
        this.battleship = battleship;
    }

    public Integer getBattlecruiser() {
        return battlecruiser;
    }

    public void setBattlecruiser(Integer battlecruiser) {
        this.battlecruiser = battlecruiser;
    }

    public Integer getBomber() {
        return bomber;
    }

    public void setBomber(Integer bomber) {
        this.bomber = bomber;
    }

    public Integer getDestroyer() {
        return destroyer;
    }

    public void setDestroyer(Integer destroyer) {
        this.destroyer = destroyer;
    }

    public Integer getDeathstar() {
        return deathstar;
    }

    public void setDeathstar(Integer deathstar) {
        this.deathstar = deathstar;
    }

    public Integer getSmallCargo() {
        return smallCargo;
    }

    public void setSmallCargo(Integer smallCargo) {
        this.smallCargo = smallCargo;
    }

    public Integer getLargeCargo() {
        return largeCargo;
    }

    public void setLargeCargo(Integer largeCargo) {
        this.largeCargo = largeCargo;
    }

    public Integer getColonyShip() {
        return colonyShip;
    }

    public void setColonyShip(Integer colonyShip) {
        this.colonyShip = colonyShip;
    }

    public Integer getRecycler() {
        return recycler;
    }

    public void setRecycler(Integer recycler) {
        this.recycler = recycler;
    }

    public Integer getEspionageProbe() {
        return espionageProbe;
    }

    public void setEspionageProbe(Integer espionageProbe) {
        this.espionageProbe = espionageProbe;
    }

    public Integer getSolarSatellite() {
        return solarSatellite;
    }

    public void setSolarSatellite(Integer solarSatellite) {
        this.solarSatellite = solarSatellite;
    }

    public Integer getCrawler() {
        return crawler;
    }

    public void setCrawler(Integer crawler) {
        this.crawler = crawler;
    }

    public Integer getReaper() {
        return reaper;
    }

    public void setReaper(Integer reaper) {
        this.reaper = reaper;
    }

    public Integer getPathfinder() {
        return pathfinder;
    }

    public void setPathfinder(Integer pathfinder) {
        this.pathfinder = pathfinder;
    }
}
