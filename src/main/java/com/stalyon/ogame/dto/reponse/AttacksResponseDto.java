package com.stalyon.ogame.dto.reponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.stalyon.ogame.dto.AttackDto;

import java.util.List;

public class AttacksResponseDto extends ResponseDto {

    @JsonProperty("Result")
    private List<AttackDto> result;

    public AttacksResponseDto() {
        // Do nothing
    }

    public List<AttackDto> getResult() {
        return result;
    }

    public void setResult(List<AttackDto> result) {
        this.result = result;
    }
}
