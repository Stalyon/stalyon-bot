package com.stalyon.ogame.dto.reponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.stalyon.ogame.dto.ResearchesDto;

public class GetResearchResponseDto extends ResponseDto {

    @JsonProperty("Result")
    private ResearchesDto result;

    public ResearchesDto getResult() {
        return result;
    }

    public void setResult(ResearchesDto result) {
        this.result = result;
    }
}
