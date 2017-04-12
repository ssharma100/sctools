package com.oracle.ofsc.geolocation.transforms.google;

/**
 * Created by xxx_sharma on 10/21/16.
 */
public class DistanceDetails {

    private DistanceNVP distance;
    private DistanceNVP duration;
    private String status;

    public DistanceDetails() {}

    public DistanceNVP getDistance() {
        return distance;
    }

    public void setDistance(DistanceNVP distance) {
        this.distance = distance;
    }

    public DistanceNVP getDuration() {
        return duration;
    }

    public void setDuration(DistanceNVP duration) {
        this.duration = duration;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
