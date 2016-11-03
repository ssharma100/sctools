package com.oracle.ofsc.transforms;

import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;

/**
 * Created by xxx_s_000 on 11/3/2016.
 */
@CsvRecord(separator = ",", skipFirstLine = true)
public class LocationListData {
    @DataField(pos=1)
    private String externalId;
    @DataField(pos=2)
    private String name;
    @DataField(pos=3)
    private String street;
    @DataField(pos=4)
    private String city;
    @DataField(pos=5)
    private String state;
    @DataField(pos=6)
    private String zip;
    @DataField(pos=7)
    private String country;
    @DataField(pos=8)
    private String phone;
    @DataField(pos=9)
    private String pager;
    @DataField(pos=10)
    private String email;

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPager() {
        return pager;
    }

    public void setPager(String pager) {
        this.pager = pager;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
