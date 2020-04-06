package com.stalyon.ogame.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserInfosDto {

    @JsonProperty("PlayerID")
    private Integer id;

    @JsonProperty("PlayerName")
    private String name;

    @JsonProperty("Points")
    private Integer points;

    @JsonProperty("Rank")
    private Integer rank;

    public UserInfosDto() {
        // Do nothing
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }
}
