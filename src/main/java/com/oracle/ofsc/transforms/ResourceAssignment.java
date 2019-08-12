package com.oracle.ofsc.transforms;

import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;

/**
 * CSV Translation/Mapping Object for Resource To Activity Assignment
 *
 */
@CsvRecord(separator = ",", skipFirstLine = true)
public class ResourceAssignment {
    @DataField(pos=1)
    private String activityKey;
    @DataField(pos=2)
    private String requiredResource;
    @DataField(pos=3)
    private Long etaId;


    public String getActivityKey() {
        return activityKey;
    }

    public void setActivityKey(String activityKey) {
        this.activityKey = activityKey;
    }

    public Long getEtaId() {
        return etaId;
    }

    public void setEtaId(Long etaId) {
        this.etaId = etaId;
    }

    public String getRequiredResource() {
        return requiredResource;
    }

    public void setRequiredResource(String requiredResource) {
        this.requiredResource = requiredResource;
    }
}
