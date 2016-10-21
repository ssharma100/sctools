package com.oracle.ofsc.etadirect.rest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class RouteInfo {

    private String activityId;
    private String resourceId;
    private String apptNumber;
    private String date;
    private String status;
    private String activityType;
    private Integer duration;
    private Integer travelTime;
    private String longitude;
    private String latitude;
    private Integer positionInRoute;
    private String deliveryWindowStart;
    private String deliveryWindowEnd;
    private String startTime;
    private String endTime;
    private String resourceTimeZone;

    public RouteInfo() {}

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getApptNumber() {
        return apptNumber;
    }

    public void setApptNumber(String apptNumber) {
        this.apptNumber = apptNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public Integer getDuration() {
        return duration;
    }

    public Integer getTravelTime() {
        return travelTime;
    }

    public void setTravelTime(Integer travelTime) {
        this.travelTime = travelTime;
    }

    public Integer getPositionInRoute() {
        return positionInRoute;
    }

    public void setPositionInRoute(Integer positionInRoute) {
        this.positionInRoute = positionInRoute;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getDeliveryWindowStart() {
        return deliveryWindowStart;
    }

    public void setDeliveryWindowStart(String deliveryWindowStart) {
        this.deliveryWindowStart = deliveryWindowStart;
    }

    public String getDeliveryWindowEnd() {
        return deliveryWindowEnd;
    }

    public void setDeliveryWindowEnd(String deliveryWindowEnd) {
        this.deliveryWindowEnd = deliveryWindowEnd;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getResourceTimeZone() {
        return resourceTimeZone;
    }

    public void setResourceTimeZone(String resourceTimeZone) {
        this.resourceTimeZone = resourceTimeZone;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
