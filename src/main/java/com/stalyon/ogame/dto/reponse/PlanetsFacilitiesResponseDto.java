package com.stalyon.ogame.dto.reponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.stalyon.ogame.dto.FacilitiesDto;

public class PlanetsFacilitiesResponseDto extends ResponseDto {

    @JsonProperty("Result")
    private FacilitiesDto result;

    public PlanetsFacilitiesResponseDto() {
        // Do nothing
    }

    public FacilitiesDto getResult() {
        return result;
    }

    public void setResult(FacilitiesDto result) {
        this.result = result;
    }
}
