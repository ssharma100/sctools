package com.oracle.ofsc.etadirect.utils;

/**
 * Simple mapping for Americas Time Zone DB values to OFSC's own specific
 * time zone labels.
 */
public enum OfscTimeZone {
    New_York("Eastern"),
    Chicago("Central");

    private String ofcsTimeZoneLabel;
    OfscTimeZone(String ofcsTimeZoneLabel) {
       this.ofcsTimeZoneLabel = ofcsTimeZoneLabel;
    }

    public String getOfscLabel() {
        return this.ofcsTimeZoneLabel;
    }

}
