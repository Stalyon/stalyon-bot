package com.stalyon.ogame.dto.reponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.stalyon.ogame.dto.ShipyardProductionDto;

import java.util.List;

public class PlanetsProductionResponseDto extends ResponseDto {

    @JsonProperty("Result")
    private List<ShipyardProductionDto> result;

    public PlanetsProductionResponseDto() {
        // Do nothing
    }

    public List<ShipyardProductionDto> getResult() {
        return result;
    }

    public void setResult(List<ShipyardProductionDto> result) {
        this.result = result;
    }
}
