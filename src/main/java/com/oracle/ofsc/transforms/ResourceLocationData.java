package com.oracle.ofsc.transforms;

import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;

/**
 * Input and output format (CSV) for the creation of the resource location information
 *
 */
@CsvRecord(separator = ",", skipFirstLine = true)
public class ResourceLocationData {

    @DataField(pos=1)
    private String resourceId;
    @DataField(pos=2)
    private String  locations;
    @DataField(pos=3)
    private Integer monStart;
    @DataField(pos=4)
    private Integer monEnd;
    @DataField(pos=5)
    private Integer monHome;
    @DataField(pos=6)
    private Integer tueStart;
    @DataField(pos=7)
    private Integer tuesEnd;
    @DataField(pos=8)
    private Integer tuesHome;
    @DataField(pos=9)
    private Integer wedStart;
    @DataField(pos=10)
    private Integer wedEnd;
    @DataField(pos=11)
    private Integer wedHome;
    @DataField(pos=12)
    private Integer thursStart;
    @DataField(pos=13)
    private Integer thursEnd;
    @DataField(pos=14)
    private Integer thursHome;
    @DataField(pos=15)
    private Integer friStart;
    @DataField(pos=16)
    private Integer firEnd;
    @DataField(pos=17)
    private Integer friHome;
    @DataField(pos=18)
    private Integer satStart;
    @DataField(pos=19)
    private Integer satEnd;
    @DataField(pos=20)
    private Integer satHome;
    @DataField(pos=21)
    private Integer sunStart;
    @DataField(pos=22)
    private Integer sunEnd;
    @DataField(pos=23)
    private Integer sunHome;

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getLocations() {
        return locations;
    }

    public void setLocations(String locations) {
        this.locations = locations;
    }

    public Integer getMonStart() {
        return monStart;
    }

    public void setMonStart(Integer monStart) {
        this.monStart = monStart;
    }

    public Integer getMonEnd() {
        return monEnd;
    }

    public void setMonEnd(Integer monEnd) {
        this.monEnd = monEnd;
    }

    public Integer getMonHome() {
        return monHome;
    }

    public void setMonHome(Integer monHome) {
        this.monHome = monHome;
    }

    public Integer getTueStart() {
        return tueStart;
    }

    public void setTueStart(Integer tueStart) {
        this.tueStart = tueStart;
    }

    public Integer getTuesEnd() {
        return tuesEnd;
    }

    public void setTuesEnd(Integer tuesEnd) {
        this.tuesEnd = tuesEnd;
    }

    public Integer getTuesHome() {
        return tuesHome;
    }

    public void setTuesHome(Integer tuesHome) {
        this.tuesHome = tuesHome;
    }

    public Integer getWedStart() {
        return wedStart;
    }

    public void setWedStart(Integer wedStart) {
        this.wedStart = wedStart;
    }

    public Integer getWedEnd() {
        return wedEnd;
    }

    public void setWedEnd(Integer wedEnd) {
        this.wedEnd = wedEnd;
    }

    public Integer getWedHome() {
        return wedHome;
    }

    public void setWedHome(Integer wedHome) {
        this.wedHome = wedHome;
    }

    public Integer getThursStart() {
        return thursStart;
    }

    public void setThursStart(Integer thursStart) {
        this.thursStart = thursStart;
    }

    public Integer getThursEnd() {
        return thursEnd;
    }

    public void setThursEnd(Integer thursEnd) {
        this.thursEnd = thursEnd;
    }

    public Integer getThursHome() {
        return thursHome;
    }

    public void setThursHome(Integer thursHome) {
        this.thursHome = thursHome;
    }

    public Integer getFriStart() {
        return friStart;
    }

    public void setFriStart(Integer friStart) {
        this.friStart = friStart;
    }

    public Integer getFirEnd() {
        return firEnd;
    }

    public void setFirEnd(Integer firEnd) {
        this.firEnd = firEnd;
    }

    public Integer getFriHome() {
        return friHome;
    }

    public void setFriHome(Integer friHome) {
        this.friHome = friHome;
    }

    public Integer getSatStart() {
        return satStart;
    }

    public void setSatStart(Integer satStart) {
        this.satStart = satStart;
    }

    public Integer getSatEnd() {
        return satEnd;
    }

    public void setSatEnd(Integer satEnd) {
        this.satEnd = satEnd;
    }

    public Integer getSatHome() {
        return satHome;
    }

    public void setSatHome(Integer satHome) {
        this.satHome = satHome;
    }

    public Integer getSunStart() {
        return sunStart;
    }

    public void setSunStart(Integer sunStart) {
        this.sunStart = sunStart;
    }

    public Integer getSunEnd() {
        return sunEnd;
    }

    public void setSunEnd(Integer sunEnd) {
        this.sunEnd = sunEnd;
    }

    public Integer getSunHome() {
        return sunHome;
    }

    public void setSunHome(Integer sunHome) {
        this.sunHome = sunHome;
    }
}
