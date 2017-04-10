package com.oracle.ofsc.etadirect.rest;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Represents the "items" top level structure for assigned resources
 */
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class ResourceAssignmentItem {

    private String resourceId;
    // Must be one of: [ "required", "preferred", "forbidden" ]
    private String preferenceType;


    public ResourceAssignmentItem () {}

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getPreferenceType() {
        return preferenceType;
    }

    public void setPreferenceType(String preferenceType) {
        this.preferenceType = preferenceType;
    }
}
