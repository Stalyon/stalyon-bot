package com.stalyon.ogame.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GalaxyMoonDto {

    @JsonProperty("ID")
    private Integer id;

    @JsonProperty("Activity")
    private Integer activity;

    public GalaxyMoonDto() {
        // Do nothing
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getActivity() {
        return activity;
    }

    public void setActivity(Integer activity) {
        this.activity = activity;
    }
}
