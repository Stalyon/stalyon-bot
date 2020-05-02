package com.stalyon.ogame.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ShipyardProductionDto {

    @JsonProperty("ID")
    private Integer id;

    @JsonProperty("Nbr")
    private Integer nbr;

    public ShipyardProductionDto() {
        // Do nothing
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getNbr() {
        return nbr;
    }

    public void setNbr(Integer nbr) {
        this.nbr = nbr;
    }
}
