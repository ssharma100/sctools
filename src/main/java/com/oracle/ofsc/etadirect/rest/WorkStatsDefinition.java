package com.oracle.ofsc.etadirect.rest;

import java.util.List;

/**
 * POJO For The WorkStats Definition
 *
 */
public class WorkStatsDefinition {
    List<WorkDurations> items;

    public WorkStatsDefinition() {}

    public List<WorkDurations> getItems() {
        return items;
    }

    public void setItems(List<WorkDurations> items) {
        this.items = items;
    }
}
