package com.stalyon.ogame.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.stalyon.ogame.dto.reponse.ResponseDto;

public class EspionageReportResponseDto extends ResponseDto {

    @JsonProperty("Result")
    private EspionageReportDto result;

    public EspionageReportResponseDto() {
        // Do nothing
    }

    public EspionageReportDto getResult() {
        return result;
    }

    public void setResult(EspionageReportDto result) {
        this.result = result;
    }
}
