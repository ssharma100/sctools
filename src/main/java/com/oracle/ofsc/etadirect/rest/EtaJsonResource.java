package com.oracle.ofsc.etadirect.rest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Json Model For Resource
 */
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class EtaJsonResource {

    private String resourceId;
    private String organization;
    private String email;
    private String phone;
    private String status;
    private String resourceInternalId;
    private String parentResourceId;
    private String parentResourceInternalId;
    private String resourceType;
    private String timeZone;
    private String dateFormat;
    private String timeFormat;
    private String language;
    private String name;
    private String work_hours;
    @JsonProperty("XA_IMPACTABLE")
    private String xaImpactable;
    private Integer impact_hours;
    private Integer impact_worked;

    public String getResourceInternalId() {
        return resourceInternalId;
    }

    public void setResourceInternalId(String resourceInternalId) {
        this.resourceInternalId = resourceInternalId;
    }

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

    public Integer getImpact_hours() {
        return impact_hours;
    }

    public void setImpact_hours(Integer impact_hours) {
        this.impact_hours = impact_hours;
    }

    public Integer getImpact_worked() {
        return impact_worked;
    }

    public void setImpact_worked(Integer impact_worked) {
        this.impact_worked = impact_worked;
    }

    public String getParentResourceInternalId() {
        return parentResourceInternalId;
    }

    public void setParentResourceInternalId(String parentResourceInternalId) {
        this.parentResourceInternalId = parentResourceInternalId;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getTimeFormat() {
        return timeFormat;
    }

    public void setTimeFormat(String timeFormat) {
        this.timeFormat = timeFormat;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
