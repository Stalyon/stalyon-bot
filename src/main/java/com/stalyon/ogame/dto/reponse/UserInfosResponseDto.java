package com.stalyon.ogame.dto.reponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.stalyon.ogame.dto.UserInfosDto;

public class UserInfosResponseDto extends ResponseDto {

    @JsonProperty("Result")
    private UserInfosDto result;

    public UserInfosResponseDto() {
        // Do nothing
    }

    public UserInfosDto getResult() {
        return result;
    }

    public void setResult(UserInfosDto result) {
        this.result = result;
    }
}
