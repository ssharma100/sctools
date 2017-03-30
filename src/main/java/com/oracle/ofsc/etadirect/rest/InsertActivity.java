package com.oracle.ofsc.etadirect.rest;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.math.BigDecimal;

/**
 * Created by xxx_sharma on 10/16/16.
 */
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class InsertActivity {

    private String resourceId;
    private String date;
    private String activityType;
    private String apptNumber;
    private String customerName;
    private String customerNumber;
    private String language = "en";
    private String timeZone;
    private String timeSlot;
    private int duration;
    private String recordType;
    // Address Oriented Information
    private BigDecimal latitude;
    private BigDecimal longitude;
    private int   travelTime = 20;
    private String workZone;

    private String deliveryWindowStart;
    private String deliveryWindowEnd;
    private String serviceWindowStart;
    private String serviceWindowEnd;

    private String slaWindowStart;     // Follow "YYYY-MM-DD HH:MM:SS"
    private String slaWindowEnd;       // Follow "YYYY-MM-DD HH:MM:SS"

    private String streetAddress;
    private String city;
    private String stateProvince;
    private String postalCode;

    // Special Items
    private String lift_gate;
    // Acosta items
    private String impact_allowable_days;

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public String getApptNumber() {
        return apptNumber;
    }

    public void setApptNumber(String apptNumber) {
        this.apptNumber = apptNumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(String customerNumber) {
        this.customerNumber = customerNumber;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public String getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getRecordType() {
        return recordType;
    }

    public void setRecordType(String recordType) {
        this.recordType = recordType;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public int getTravelTime() {
        return travelTime;
    }

    public void setTravelTime(int travelTime) {
        this.travelTime = travelTime;
    }

    public String getWorkZone() {
        return workZone;
    }

    public void setWorkZone(String workZone) {
        this.workZone = workZone;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStateProvince() {
        return stateProvince;
    }

    public void setStateProvince(String stateProvince) {
        this.stateProvince = stateProvince;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
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

    public String getLift_gate() {
        return lift_gate;
    }

    public void setLift_gate(String lift_gate) {
        this.lift_gate = lift_gate;
    }

    public String getServiceWindowStart() {
        return serviceWindowStart;
    }

    public void setServiceWindowStart(String serviceWindowStart) {
        this.serviceWindowStart = serviceWindowStart;
    }

    public String getServiceWindowEnd() {
        return serviceWindowEnd;
    }

    public void setServiceWindowEnd(String serviceWindowEnd) {
        this.serviceWindowEnd = serviceWindowEnd;
    }

    public String getSlaWindowStart() {
        return slaWindowStart;
    }

    public void setSlaWindowStart(String slaWindowStart) {
        this.slaWindowStart = slaWindowStart;
    }

    public String getSlaWindowEnd() {
        return slaWindowEnd;
    }

    public void setSlaWindowEnd(String slaWindowEnd) {
        this.slaWindowEnd = slaWindowEnd;
    }

    public String getImpact_allowable_days() {
        return impact_allowable_days;
    }

    public void setImpact_allowable_days(String impact_allowable_days) {
        this.impact_allowable_days = impact_allowable_days;
    }
}
