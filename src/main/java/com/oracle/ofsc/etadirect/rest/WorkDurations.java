package com.oracle.ofsc.etadirect.rest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Json Pojo model for work durations
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class WorkDurations {
    private String resourceId;
    private String akey;
    private Integer override;

    public WorkDurations() {}

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getAkey() {
        return akey;
    }

    public void setAkey(String akey) {
        this.akey = akey;
    }

    public Integer getOverride() {
        return override;
    }

    public void setOverride(Integer override) {
        this.override = override;
    }
}
