package com.oracle.ofsc.etadirect.rest;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Created by ssharma on 3/23/17.
 */
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class Assignment {
    private Integer start;
    private Integer end;
    private Integer homeZoneCenter;

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getEnd() {
        return end;
    }

    public void setEnd(Integer end) {
        this.end = end;
    }

    public Integer getHomeZoneCenter() {
        return homeZoneCenter;
    }

    public void setHomeZoneCenter(Integer homeZoneCenter) {
        this.homeZoneCenter = homeZoneCenter;
    }
}
