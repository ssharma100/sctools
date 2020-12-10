package com.oracle.ofsc.transforms;

import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;

/**
 * Bindy class that formats the output required for User Files/Information
 *
 */
@CsvRecord(separator = ",", skipFirstLine = false, generateHeaderColumns=true)
public class UserLoginData {

    @DataField(pos=1)
    private String login;
    @DataField(pos=2)
    private String name;
    @DataField(pos=3)
    private String lastLoginTime;
    @DataField(pos=4)
    private String lastUpdatedTime;
    @DataField(pos=5)
    private String userType;
    @DataField(pos=6)
    private String createdTime;
    @DataField(pos=7)
    private String status;
    @DataField(pos=8)
    private String language;
    @DataField(pos=9)
    private String timezone;
    @DataField(pos=10)
    private String vendor;


    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public String getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(String lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getLastUpdatedTime() {
        return lastUpdatedTime;
    }

    public void setLastUpdatedTime(String lastUpdatedTime) {
        this.lastUpdatedTime = lastUpdatedTime;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }
}
