package com.stalyon.ogame.dto.reponse;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ResponseDto {

    @JsonProperty("Status")
    private String status;

    @JsonProperty("Code")
    private Integer code;

    @JsonProperty("Message")
    private String message;

    public ResponseDto() {
        // Do nothing
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
