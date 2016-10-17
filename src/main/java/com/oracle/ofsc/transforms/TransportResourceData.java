package com.oracle.ofsc.transforms;

import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;

/**
 * Modeling for ArcBest for transportation provides
 * and the resources (usually trucks) that they model
 * as resources.
 *
 */
@CsvRecord(separator = ",", skipFirstLine = true)
public class TransportResourceData {
    @DataField(pos=1)
    private String parentId;

    @DataField(pos=2)
    private int truckLength;

    @DataField(pos=3)
    private String name;

    @DataField(pos=4)
    private String liftGate;

    @DataField(pos=5)
    private String weight;

    @DataField(pos=6)
    private String cubeCap;

    @DataField(pos=7)
    private String timezone;

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public int getTruckLength() {
        return truckLength;
    }

    public void setTruckLength(int truckLength) {
        this.truckLength = truckLength;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLiftGate() {
        return liftGate;
    }

    public void setLiftGate(String liftGate) {
        this.liftGate = liftGate;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getCubeCap() {
        return cubeCap;
    }

    public void setCubeCap(String cubeCap) {
        this.cubeCap = cubeCap;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }
}
