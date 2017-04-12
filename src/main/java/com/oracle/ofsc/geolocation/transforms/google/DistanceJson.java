package com.oracle.ofsc.geolocation.transforms.google;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

/**
 * Model for Faster Jackson UnMarshalling Of Google's Distance API Response
 */
public class DistanceJson {

    @JsonProperty("destination_addresses")
    private ArrayList<String> destinations ;

    @JsonProperty("origin_addresses")
    private ArrayList<String> origins;

    private String status;
    private ArrayList<DistanceRows> rows;


    public DistanceJson() {}

    public ArrayList<String> getDestinations() {
        return destinations;
    }

    public void setDestinations(ArrayList<String> destinations) {
        this.destinations = destinations;
    }

    public ArrayList<String> getOrigins() {
        return origins;
    }

    public void setOrigins(ArrayList<String> origins) {
        this.origins = origins;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<DistanceRows> getRows() {
        return rows;
    }

    public void setRows(ArrayList<DistanceRows> rows) {
        this.rows = rows;
    }
}
