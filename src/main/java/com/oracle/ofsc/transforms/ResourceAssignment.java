package com.oracle.ofsc.transforms;

import org.apache.camel.dataformat.bindy.annotation.CsvRecord;

/**
 * CSV Translation/Mapping Object for Resource To Activity Assignment
 *
 */
@CsvRecord(separator = ",", skipFirstLine = true)
public class ResourceAssignment {
    private String activityKey;
    private int etaId;

    public String getActivityKey() {
        return activityKey;
    }

    public void setActivityKey(String activityKey) {
        this.activityKey = activityKey;
    }

    public int getEtaId() {
        return etaId;
    }

    public void setEtaId(int etaId) {
        this.etaId = etaId;
    }
}
