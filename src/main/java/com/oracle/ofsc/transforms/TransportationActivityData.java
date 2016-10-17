package com.oracle.ofsc.transforms;

import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;

import java.math.BigDecimal;

/**
 * CSV Parsing object - used to capture information provided by the
 * user in their call into the tool.
 * This data will be used by the system to generate the calls to ETAdirect
 * when creating an activity.
 */
@CsvRecord(separator = ",", skipFirstLine = true)
public class TransportationActivityData {

    @DataField(pos=1)
    private boolean bringBack;
    @DataField(pos=2)
    private String  appointmentKey;
    @DataField(pos=3)
    private String  resourceId;
    @DataField(pos=4)
    private String  activityType;
    @DataField(pos=5)
    private String activityDate;
    @DataField(pos=6)
    private String  workOrder;
    @DataField(pos=7)
    private int     weight;
    @DataField(pos=8, precision = 2)
    private BigDecimal cube;
    @DataField(pos=9, precision = 4)
    private BigDecimal latitude;
    @DataField(pos=10, precision = 4)
    private BigDecimal longitude;
    @DataField(pos=11)
    private int         duration;
    @DataField(pos=12)
    private String      zipCode;
    @DataField(pos=13)
    private String      deliveryStart;
    @DataField(pos=14)
    private String      deliveryEnd;
    @DataField(pos=15)
    private String      liftGate;
    @DataField(pos=16)
    private String      pickUpTime;

    public TransportationActivityData() {}

    public boolean isBringBack() {
        return bringBack;
    }

    public void setBringBack(boolean bringBack) {
        this.bringBack = bringBack;
    }

    public String getAppointmentKey() {
        return appointmentKey;
    }

    public void setAppointmentKey(String appointmentKey) {
        this.appointmentKey = appointmentKey;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getWorkOrder() {
        return workOrder;
    }

    public void setWorkOrder(String workOrder) {
        this.workOrder = workOrder;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public BigDecimal getCube() {
        return cube;
    }

    public void setCube(BigDecimal cube) {
        this.cube = cube;
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

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getDeliveryStart() {
        return deliveryStart;
    }

    public void setDeliveryStart(String deliveryStart) {
        this.deliveryStart = deliveryStart;
    }

    public String getDeliveryEnd() {
        return deliveryEnd;
    }

    public void setDeliveryEnd(String deliveryEnd) {
        this.deliveryEnd = deliveryEnd;
    }

    public String getLiftGate() {
        return liftGate;
    }

    public void setLiftGate(String liftGate) {
        this.liftGate = liftGate;
    }

    public String getPickUpTime() {
        return pickUpTime;
    }

    public void setPickUpTime(String pickUpTime) {
        this.pickUpTime = pickUpTime;
    }

    public String getActivityDate() {
        return activityDate;
    }

    public void setActivityDate(String activityDate) {
        this.activityDate = activityDate;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }
}
