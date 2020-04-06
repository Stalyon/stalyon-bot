package com.stalyon.ogame.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SlotsDto {

    @JsonProperty("InUse")
    private Integer inUse;

    @JsonProperty("Total")
    private Integer total;

    @JsonProperty("ExpInUse")
    private Integer expInUse;

    @JsonProperty("ExpTotal")
    private Integer expTotal;

    public SlotsDto() {
        // Do nothing
    }

    public Integer getInUse() {
        return inUse;
    }

    public void setInUse(Integer inUse) {
        this.inUse = inUse;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getExpInUse() {
        return expInUse;
    }

    public void setExpInUse(Integer expInUse) {
        this.expInUse = expInUse;
    }

    public Integer getExpTotal() {
        return expTotal;
    }

    public void setExpTotal(Integer expTotal) {
        this.expTotal = expTotal;
    }
}
