package com.oracle.ofsc.etadirect.camel.beans;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Created by ssharma on 4/15/17.
 */
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class ResultItem {

    private String resourceId;
    private String message;

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
