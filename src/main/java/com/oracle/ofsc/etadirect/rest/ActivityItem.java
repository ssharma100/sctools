package com.oracle.ofsc.etadirect.rest;

import java.util.Date;

/**
 * Created by ssharma on 4/28/17.
 */
public class ActivityItem {

    private Long activityId;
    private String resourceId;
    private String resourceInternalId;
    private Date date;
    private String status;

    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getResourceInternalId() {
        return resourceInternalId;
    }

    public void setResourceInternalId(String resourceInternalId) {
        this.resourceInternalId = resourceInternalId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
