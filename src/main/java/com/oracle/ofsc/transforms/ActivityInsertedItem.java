package com.oracle.ofsc.transforms;

import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;

/**
 * Bindy Bean uses for the CSV generation of responses/results
 *
 */
@CsvRecord(separator = ",", generateHeaderColumns = true)
public class ActivityInsertedItem {

    @DataField(pos=1, required = true)
    private int recIndex;
    @DataField(pos=2, required = true)
    private String error = "OK";
    @DataField(pos=3)
    private long ofscId;
    @DataField(pos=4)
    private String activityType;
    @DataField(pos=5)
    private int duration;
    @DataField(pos=6)
    private String addressId;
    @DataField(pos=7)
    private String ticketId;
    @DataField(pos=8)
    private String resourceId;
    @DataField(pos=9)
    private int httpCode;
    @DataField(pos=10)
    private String httpMessage;
    @DataField(pos=11)
    private String accountId;

    public int getRecIndex() {
        return recIndex;
    }

    public void setRecIndex(int recIndex) {
        this.recIndex = recIndex;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public long getOfscId() {
        return ofscId;
    }

    public void setOfscId(long ofscId) {
        this.ofscId = ofscId;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public int getHttpCode() {
        return httpCode;
    }

    public void setHttpCode(int httpCode) {
        this.httpCode = httpCode;
    }

    public String getHttpMessage() {
        return httpMessage;
    }

    public void setHttpMessage(String httpMessage) {
        this.httpMessage = httpMessage;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }
}
