package com.stalyon.ogame.dto.reponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.stalyon.ogame.dto.PlanetDto;

public class PlanetsPlanetResponseDto extends ResponseDto {

    @JsonProperty("Result")
    private PlanetDto result;

    public PlanetDto getResult() {
        return result;
    }

    public void setResult(PlanetDto result) {
        this.result = result;
    }
}
