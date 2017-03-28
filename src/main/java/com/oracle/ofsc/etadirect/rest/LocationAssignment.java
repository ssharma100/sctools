package com.oracle.ofsc.etadirect.rest;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Created by ssharma on 3/23/17.
 */
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class LocationAssignment {
    private Assignment mon;
    private Assignment tue;
    private Assignment wed;
    private Assignment thu;
    private Assignment fri;
    private Assignment sat;
    private Assignment sun;

    public Assignment getMon() {
        return mon;
    }

    public void setMon(Assignment mon) {
        this.mon = mon;
    }

    public Assignment getTue() {
        return tue;
    }

    public void setTue(Assignment tue) {
        this.tue = tue;
    }

    public Assignment getWed() {
        return wed;
    }

    public void setWed(Assignment wed) {
        this.wed = wed;
    }

    public Assignment getThu() {
        return thu;
    }

    public void setThu(Assignment thu) {
        this.thu = thu;
    }

    public Assignment getFri() {
        return fri;
    }

    public void setFri(Assignment fri) {
        this.fri = fri;
    }

    public Assignment getSat() {
        return sat;
    }

    public void setSat(Assignment sat) {
        this.sat = sat;
    }

    public Assignment getSun() {
        return sun;
    }

    public void setSun(Assignment sun) {
        this.sun = sun;
    }
}
