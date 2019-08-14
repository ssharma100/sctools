package com.oracle.ofsc.etadirect.camel.beans;

import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

/**
 * Camel Bean Support Class
 * Methods in this class support the Stats routes and endpoint functionality
 *
 */
public class Statistics {

    private static final Logger LOGGER = LoggerFactory.getLogger(Statistics.class.getName());

    /**
     * The Status load of activities should have the following header
     * id: the resourceId of a "activated" resource
     * firstAptTime: the tme at which to start the appoints (first)
     * numberOfApt: the number of appointments to create
     * duration: minute of duration each appointment should be
     * activity
     * customerType:
     * services:
     * Tv:
     * RepairType:
     * @param exchange
     */
    public void extractActivityLoadParams(Exchange exchange) {

        LOGGER.info("Generate Stats Loading Headers");
        HashMap<String, String> params = Security.extractURLInfo((String) exchange.getIn().getHeader("CamelHttpQuery"));
        // Map should contain NVP of the URL query parameters and values

        // Populate The Exchange Header With The Elements That Dictate What Is To Be Done In This Run

    }

}
