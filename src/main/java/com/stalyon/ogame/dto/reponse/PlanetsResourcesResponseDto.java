package com.stalyon.ogame.dto.reponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.stalyon.ogame.dto.PlanetsResourcesDto;

public class PlanetsResourcesResponseDto extends ResponseDto {

    @JsonProperty("Result")
    private PlanetsResourcesDto result;

    public PlanetsResourcesResponseDto() {
        // Do nothing
    }

    public PlanetsResourcesDto getResult() {
        return result;
    }

    public void setResult(PlanetsResourcesDto result) {
        this.result = result;
    }
}
