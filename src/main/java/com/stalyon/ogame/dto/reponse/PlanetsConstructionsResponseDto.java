package com.stalyon.ogame.dto.reponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.stalyon.ogame.dto.PlanetsConstructionsDto;

public class PlanetsConstructionsResponseDto extends ResponseDto {

    @JsonProperty("Result")
    private PlanetsConstructionsDto result;

    public PlanetsConstructionsResponseDto() {
        // Do nothing
    }

    public PlanetsConstructionsDto getResult() {
        return result;
    }

    public void setResult(PlanetsConstructionsDto result) {
        this.result = result;
    }
}
