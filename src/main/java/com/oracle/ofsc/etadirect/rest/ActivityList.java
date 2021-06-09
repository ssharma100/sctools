package com.oracle.ofsc.etadirect.rest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * POJO Class For The Response From The OFSC Activities Call
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ActivityList {
    private String expression;
    List<ActivityItem> items;
    int offset;
    int limit;

    public ActivityList() {
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public List<ActivityItem> getItems() {
        return items;
    }

    public void setItems(List<ActivityItem> items) {
        this.items = items;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
}
