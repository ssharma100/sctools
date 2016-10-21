package com.oracle.ofsc.etadirect.rest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xxx_sharma on 10/20/16.
 */
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RouteList {
    private int totalResults;
    private ArrayList<RouteInfo> items;

    public RouteList() {}

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public ArrayList<RouteInfo> getItems() {
        return items;
    }

    public void setItems(ArrayList<RouteInfo> items) {
        this.items = items;
    }
}
