package com.stalyon.ogame.dto.reponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.stalyon.ogame.dto.FleetDto;

import java.util.List;

public class FleetsResponseDto extends ResponseDto {

    @JsonProperty("Result")
    private List<FleetDto> result;

    public List<FleetDto> getResult() {
        return result;
    }

    public void setResult(List<FleetDto> result) {
        this.result = result;
    }
}
