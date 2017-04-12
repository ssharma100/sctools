package com.oracle.ofsc.transforms;

import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;

import java.math.BigDecimal;

/**
 * Created by xxx_sharma on 10/20/16.
 */
@CsvRecord(separator = ",", skipFirstLine = true, generateHeaderColumns=true)
public class RouteReportData {

    @DataField(pos=1)
    private int routePosition;

    @DataField(pos=2)
    private String activityId;

    @DataField(pos=3)
    private String resourceId;

    @DataField(pos=4)
    private String status;

    @DataField(pos=5)
    private String date;

    @DataField(pos=6)
    private String apptNumber;

    @DataField(pos=7)
    private int duration;

    @DataField(pos=8)
    private int travelTime;

    @DataField(pos=9)
    private String timezone;

    @DataField(pos=10)
    private String startTime;

    @DataField(pos=11)
    private String endTime;

    @DataField(pos=12, precision = 4)
    private BigDecimal latitude;

    @DataField(pos=13, precision = 4)
    private BigDecimal longitude;

    @DataField(pos=14)
    private String originAddr;

    @DataField(pos=15)
    private String destAddr;

    @DataField(pos=16)
    private String driveTime;

    @DataField(pos=17)
    private String driveDistance;


    public RouteReportData() {}

    public void setRoutePosition(int routePosition) {
        this.routePosition = routePosition;
    }

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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getApptNumber() {
        return apptNumber;
    }

    public void setApptNumber(String apptNumber) {
        this.apptNumber = apptNumber;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getTravelTime() {
        return travelTime;
    }

    public void setTravelTime(int travelTime) {
        this.travelTime = travelTime;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getRoutePosition() {

        return routePosition;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public String getOriginAddr() {
        return originAddr;
    }

    public void setOriginAddr(String originAddr) {
        this.originAddr = originAddr;
    }

    public String getDestAddr() {
        return destAddr;
    }

    public void setDestAddr(String destAddr) {
        this.destAddr = destAddr;
    }

    public String getDriveTime() {
        return driveTime;
    }

    public void setDriveTime(String driveTime) {
        this.driveTime = driveTime;
    }

    public String getDriveDistance() {
        return driveDistance;
    }

    public void setDriveDistance(String driveDistance) {
        this.driveDistance = driveDistance;
    }
}
