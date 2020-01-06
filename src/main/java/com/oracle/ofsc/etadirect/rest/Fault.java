package com.oracle.ofsc.etadirect.rest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * JSON Modeling Class For Errors/Bad Request Responses Of OFSC API Call
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Fault {

    private String type;
    private String title;
    private String status;
    private String detail;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
