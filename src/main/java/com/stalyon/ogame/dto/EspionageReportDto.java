package com.stalyon.ogame.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EspionageReportDto {

    @JsonProperty("Metal")
    private Integer metal;

    @JsonProperty("Crystal")
    private Integer crystal;

    @JsonProperty("Deuterium")
    private Integer deuterium;

    @JsonProperty("Energy")
    private Integer energy;

    @JsonProperty("Darkmatter")
    private Integer darkmatter;

    @JsonProperty("Username")
    private String username;

    @JsonProperty("HasFleetInformation")
    private Boolean hasFleetInformation;

    @JsonProperty("HasDefensesInformation")
    private Boolean hasDefensesInformation;

    @JsonProperty("HasBuildingsInformation")
    private Boolean hasBuildingsInformation;

    @JsonProperty("HasResearchesInformation")
    private Boolean hasResearchesInformation;

    @JsonProperty("IsInactive")
    private Boolean isInactive;

    @JsonProperty("IsLongInactive")
    private Boolean isLongInactive;

    @JsonProperty("RocketLauncher")
    private Integer rocketLauncher;

    @JsonProperty("LightLaser")
    private Integer lightLaser;

    @JsonProperty("HeavyLaser")
    private Integer heavyLaser;

    @JsonProperty("GaussCannon")
    private Integer gaussCannon;

    @JsonProperty("IonCannon")
    private Integer ionCannon;

    @JsonProperty("PlasmaTurret")
    private Integer plasmaTurret;

    @JsonProperty("SmallShieldDome")
    private Integer smallShieldDome;

    @JsonProperty("LargeShieldDome")
    private Integer largeShieldDome;

    @JsonProperty("AntiBallisticMissiles")
    private Integer antiBallisticMissiles;

    @JsonProperty("InterplanetaryMissiles")
    private Integer interplanetaryMissiles;

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

    @JsonProperty("Crawler")
    private Integer crawler;

    @JsonProperty("Reaper")
    private Integer reaper;

    @JsonProperty("Pathfinder")
    private Integer pathfinder;

    @JsonProperty("Coordinate")
    private CoordinateDto coordinate;

    public EspionageReportDto() {
        // Do nothing
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

    public Integer getEnergy() {
        return energy;
    }

    public void setEnergy(Integer energy) {
        this.energy = energy;
    }

    public Integer getDarkmatter() {
        return darkmatter;
    }

    public void setDarkmatter(Integer darkmatter) {
        this.darkmatter = darkmatter;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Boolean getHasFleetInformation() {
        return hasFleetInformation;
    }

    public void setHasFleetInformation(Boolean hasFleetInformation) {
        this.hasFleetInformation = hasFleetInformation;
    }

    public Boolean getHasDefensesInformation() {
        return hasDefensesInformation;
    }

    public void setHasDefensesInformation(Boolean hasDefensesInformation) {
        this.hasDefensesInformation = hasDefensesInformation;
    }

    public Boolean getHasBuildingsInformation() {
        return hasBuildingsInformation;
    }

    public void setHasBuildingsInformation(Boolean hasBuildingsInformation) {
        this.hasBuildingsInformation = hasBuildingsInformation;
    }

    public Boolean getHasResearchesInformation() {
        return hasResearchesInformation;
    }

    public void setHasResearchesInformation(Boolean hasResearchesInformation) {
        this.hasResearchesInformation = hasResearchesInformation;
    }

    public Boolean getInactive() {
        return isInactive;
    }

    public void setInactive(Boolean inactive) {
        isInactive = inactive;
    }

    public Boolean getLongInactive() {
        return isLongInactive;
    }

    public void setLongInactive(Boolean longInactive) {
        isLongInactive = longInactive;
    }

    public Integer getRocketLauncher() {
        return rocketLauncher;
    }

    public void setRocketLauncher(Integer rocketLauncher) {
        this.rocketLauncher = rocketLauncher;
    }

    public Integer getLightLaser() {
        return lightLaser;
    }

    public void setLightLaser(Integer lightLaser) {
        this.lightLaser = lightLaser;
    }

    public Integer getHeavyLaser() {
        return heavyLaser;
    }

    public void setHeavyLaser(Integer heavyLaser) {
        this.heavyLaser = heavyLaser;
    }

    public Integer getGaussCannon() {
        return gaussCannon;
    }

    public void setGaussCannon(Integer gaussCannon) {
        this.gaussCannon = gaussCannon;
    }

    public Integer getIonCannon() {
        return ionCannon;
    }

    public void setIonCannon(Integer ionCannon) {
        this.ionCannon = ionCannon;
    }

    public Integer getPlasmaTurret() {
        return plasmaTurret;
    }

    public void setPlasmaTurret(Integer plasmaTurret) {
        this.plasmaTurret = plasmaTurret;
    }

    public Integer getSmallShieldDome() {
        return smallShieldDome;
    }

    public void setSmallShieldDome(Integer smallShieldDome) {
        this.smallShieldDome = smallShieldDome;
    }

    public Integer getLargeShieldDome() {
        return largeShieldDome;
    }

    public void setLargeShieldDome(Integer largeShieldDome) {
        this.largeShieldDome = largeShieldDome;
    }

    public Integer getAntiBallisticMissiles() {
        return antiBallisticMissiles;
    }

    public void setAntiBallisticMissiles(Integer antiBallisticMissiles) {
        this.antiBallisticMissiles = antiBallisticMissiles;
    }

    public Integer getInterplanetaryMissiles() {
        return interplanetaryMissiles;
    }

    public void setInterplanetaryMissiles(Integer interplanetaryMissiles) {
        this.interplanetaryMissiles = interplanetaryMissiles;
    }

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

    public CoordinateDto getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(CoordinateDto coordinate) {
        this.coordinate = coordinate;
    }
}
