package com.stalyon.ogame.dto.reponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.stalyon.ogame.dto.SendFleetDto;

public class SendFleetResponseDto extends ResponseDto {

    @JsonProperty("Result")
    private SendFleetDto result;

    public SendFleetDto getResult() {
        return result;
    }

    public void setResult(SendFleetDto result) {
        this.result = result;
    }
}
