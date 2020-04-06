package com.stalyon.ogame.dto.reponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.stalyon.ogame.dto.PlanetDto;

import java.util.List;

public class PlanetsResponseDto {

    @JsonProperty("Result")
    private List<PlanetDto> result;

    public List<PlanetDto> getResult() {
        return result;
    }

    public void setResult(List<PlanetDto> result) {
        this.result = result;
    }
}
