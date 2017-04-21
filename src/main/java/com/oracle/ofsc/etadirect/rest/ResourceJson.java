package com.oracle.ofsc.etadirect.rest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Json Model For Resource
 */
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResourceJson {

    private String resourceId;
    private String organization;
    private String status;
    private String parentResourceId;
    private String resourceType;
    private String name;
    private String work_hours;
    @JsonProperty("XA_IMPACTABLE")
    private String xaImpactable;
    private String impact_hours;
    private String impact_worked;

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getParentResourceId() {
        return parentResourceId;
    }

    public void setParentResourceId(String parentResourceId) {
        this.parentResourceId = parentResourceId;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWork_hours() {
        return work_hours;
    }

    public void setWork_hours(String work_hours) {
        this.work_hours = work_hours;
    }

    public String getXaImpactable() {
        return xaImpactable;
    }

    public void setXaImpactable(String xaImpactable) {
        this.xaImpactable = xaImpactable;
    }

    public String getImpact_hours() {
        return impact_hours;
    }

    public void setImpact_hours(String impact_hours) {
        this.impact_hours = impact_hours;
    }

    public String getImpact_worked() {
        return impact_worked;
    }

    public void setImpact_worked(String impact_worked) {
        this.impact_worked = impact_worked;
    }
}
