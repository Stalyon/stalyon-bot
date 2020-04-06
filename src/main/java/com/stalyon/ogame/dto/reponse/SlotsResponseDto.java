package com.stalyon.ogame.dto.reponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.stalyon.ogame.dto.SlotsDto;

public class SlotsResponseDto extends ResponseDto {

    @JsonProperty("Result")
    private SlotsDto result;

    public SlotsResponseDto() {
        // Do nothing
    }

    public SlotsDto getResult() {
        return result;
    }

    public void setResult(SlotsDto result) {
        this.result = result;
    }
}
