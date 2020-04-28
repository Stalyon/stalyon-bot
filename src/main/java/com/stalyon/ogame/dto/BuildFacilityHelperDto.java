package com.stalyon.ogame.dto;

public class BuildFacilityHelperDto {

    private Integer nextFacility;
    private Integer lvlFacility;

    public BuildFacilityHelperDto(Integer nextFacility, Integer lvlFacility) {
        this.nextFacility = nextFacility;
        this.lvlFacility = lvlFacility;
    }

    public Integer getNextFacility() {
        return nextFacility;
    }

    public void setNextFacility(Integer nextFacility) {
        this.nextFacility = nextFacility;
    }

    public Integer getLvlFacility() {
        return lvlFacility;
    }

    public void setLvlFacility(Integer lvlFacility) {
        this.lvlFacility = lvlFacility;
    }
}
