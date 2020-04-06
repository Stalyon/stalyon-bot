package com.stalyon.ogame.dto.reponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.stalyon.ogame.dto.GalaxyInfosDto;

public class GalaxyInfosResponseDto extends ResponseDto {

    @JsonProperty("Result")
    private GalaxyInfosDto result;

    public GalaxyInfosResponseDto() {
        // Do nothing
    }

    public GalaxyInfosDto getResult() {
        return result;
    }

    public void setResult(GalaxyInfosDto result) {
        this.result = result;
    }
}
