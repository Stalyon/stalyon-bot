package com.stalyon.ogame.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AllianceDto {

    @JsonProperty("ID")
    private Integer id;

    @JsonProperty("Name")
    private String name;

    public AllianceDto() {
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
}
