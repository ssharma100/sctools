package com.oracle.ofsc.transforms;

import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;

import java.math.BigDecimal;

/**
 * Models the input file for generic information about activity loading
 */
@CsvRecord(separator = ",", skipFirstLine = true)
public class GenericActivityData {

    @DataField(pos=1)
    private String activityKey;
    @DataField(pos=2)
    private String  resourceId;
    @DataField(pos=3)
    private String  activityType;
    @DataField(pos=4)
    private String activityDate;
    @DataField(pos=5, precision = 4)
    private BigDecimal latitude;
    @DataField(pos=6, precision = 4)
    private BigDecimal longitude;
    @DataField(pos=7)
    private int         duration;
    @DataField(pos=8)
    private String      activityStart;
    @DataField(pos=9)
    private String      activityEnd;
    @DataField(pos=10)
    private String      city;
    @DataField(pos=11)
    private String      state;
    @DataField(pos=12)
    private String      zipCode;
    @DataField(pos=13)
    private String      timezone;
    @DataField(pos=14)
    private String      timeSlot;

    public GenericActivityData() {}

    public String getActivityKey() {
        return activityKey;
    }

    public void setActivityKey(String activityKey) {
        this.activityKey = activityKey;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public String getActivityDate() {
        return activityDate;
    }

    public void setActivityDate(String activityDate) {
        this.activityDate = activityDate;
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

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getActivityStart() {
        return activityStart;
    }

    public void setActivityStart(String activityStart) {
        this.activityStart = activityStart;
    }

    public String getActivityEnd() {
        return activityEnd;
    }

    public void setActivityEnd(String activityEnd) {
        this.activityEnd = activityEnd;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
    }
}
