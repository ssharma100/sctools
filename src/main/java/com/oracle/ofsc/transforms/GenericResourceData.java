package com.oracle.ofsc.transforms;

import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;

import java.math.BigDecimal;

/**
 * Modeling for Generic resource for general resource upload
 */
@CsvRecord(separator = ",", skipFirstLine = true)
public class GenericResourceData {

    @DataField(pos=1)
    private String name;

    @DataField(pos=2)
    private String resourceId;

    @DataField(pos=3)
    private String workSkillList;

    @DataField(pos=4, precision = 4)
    private BigDecimal latitude;

    @DataField(pos=5, precision = 4)
    private BigDecimal longitude;

    @DataField(pos=6)
    private String address;

    @DataField(pos=7)
    private String city;

    @DataField(pos=8)
    private String state;

    @DataField(pos=9)
    private String zip;

    @DataField(pos=10)
    private String timezone;

    @DataField(pos=11)
    private int weeklyHours;

    @DataField(pos=12)
    private String affiliation;

    @DataField(pos=13)
    private String login;

    @DataField(pos=14)
    private String pass;

    // Accessor Methods
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getAffiliation() {
        return affiliation;
    }

    public void setAffiliation(String affiliation) {
        this.affiliation = affiliation;
    }

    public int getWeeklyHours() {
        return weeklyHours;
    }

    public void setWeeklyHours(int weeklyHours) {
        this.weeklyHours = weeklyHours;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getWorkSkillList() {
        return workSkillList;
    }

    public void setWorkSkillList(String workSkillList) {
        this.workSkillList = workSkillList;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}
