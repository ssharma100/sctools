package com.oracle.ofsc.etadirect.rest;

import java.util.List;

/**
 * Over arching response object for the OFSC user query
 *
 */
public class UserResponse {

    private int totalResults;
    private int offset;
    private int limit;
    private List<User> items;

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
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

    public List<User> getItems() {
        return items;
    }

    public void setItems(List<User> items) {
        this.items = items;
    }
}
