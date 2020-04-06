package com.stalyon.ogame.dto.reponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.stalyon.ogame.dto.PlanetsResourcesBuildingsDto;

public class PlanetsResourcesBuildingsResponseDto extends ResponseDto {

    @JsonProperty("Result")
    private PlanetsResourcesBuildingsDto result;

    public PlanetsResourcesBuildingsResponseDto() {
        // Do nothing
    }

    public PlanetsResourcesBuildingsDto getResult() {
        return result;
    }

    public void setResult(PlanetsResourcesBuildingsDto result) {
        this.result = result;
    }
}
