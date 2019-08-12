package com.oracle.ofsc.etadirect.rest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.List;

/**
 * OFSC Response To Custom Query For Activity
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ActivitySearchResponse {

    private int totalResults;
    private int limit;
    private List<ActivityItem> items;

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public List<ActivityItem> getItems() {
        return items;
    }

    public void setItems(List<ActivityItem> items) {
        this.items = items;
    }
}
