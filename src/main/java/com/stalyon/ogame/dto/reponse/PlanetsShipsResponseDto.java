package com.stalyon.ogame.dto.reponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.stalyon.ogame.dto.ShipsDto;

public class PlanetsShipsResponseDto extends ResponseDto {

    @JsonProperty("Result")
    private ShipsDto result;

    public PlanetsShipsResponseDto() {
        // Do nothing
    }

    public ShipsDto getResult() {
        return result;
    }

    public void setResult(ShipsDto result) {
        this.result = result;
    }
}
