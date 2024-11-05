package com.oracle.ofsc.etadirect.camel.beans;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.ArrayList;

/**
 * Holds a response processing output from the Camel route
 */
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class ProcessingResult {

    private ArrayList<ResultItem> results;

    public ArrayList<ResultItem> getResults() {
        return results;
    }

    public void setResults(ArrayList<ResultItem> results) {
        this.results = results;
    }
}
