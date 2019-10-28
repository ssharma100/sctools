package com.oracle.ofsc.transforms;

import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;

@CsvRecord(separator = ",", skipFirstLine = true)
public class FiberActivityData {
    @DataField(pos=1)
    private String accountId;
    @DataField(pos=2)
    private String activityKey;
    @DataField(pos=3)
    private String resourceId;
    @DataField(pos=4)
    private String activityType;
    @DataField(pos=5)
    private String startDate;
    @DataField(pos=6)
    private String endDate;
    @DataField(pos=7)
    private int duration;
    @DataField(pos=8)
    private String gCustomer;
    @DataField(pos=9)
    private String street;
    @DataField(pos=10)
    private String city;
    @DataField(pos=11)
    private String state;
    @DataField(pos=12)
    private String postalCode;
    @DataField(pos=13)
    private String timeZone;
    @DataField(pos=14)
    private String timeslot;
    @DataField(pos=15)
    private String addressId;
    @DataField(pos=16)
    private String firstName;
    @DataField(pos=17)
    private String lastName;
    @DataField(pos=18)
    private String repairType;
    @DataField(pos=19)
    private String phoneNumber;
    @DataField(pos=20)
    private int gTvCount;
    @DataField(pos=21)
    private String gServices;


    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

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

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getgCustomer() {
        return gCustomer;
    }

    public void setgCustomer(String gCustomer) {
        this.gCustomer = gCustomer;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
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

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public String getTimeslot() {
        return timeslot;
    }

    public void setTimeslot(String timeslot) {
        this.timeslot = timeslot;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getRepairType() {
        return repairType;
    }

    public void setRepairType(String repairType) {
        this.repairType = repairType;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getgTvCount() {
        return gTvCount;
    }

    public void setgTvCount(int gTvCount) {
        this.gTvCount = gTvCount;
    }

    public String getgServices() {
        return gServices;
    }

    public void setgServices(String gServices) {
        this.gServices = gServices;
    }
}
