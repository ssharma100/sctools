package com.oracle.ofsc.geolocation.beans;

import com.oracle.ofsc.transforms.ResourceLocationData;
import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

/**
 * Created by ssharma on 3/23/17.
 */
public class ResourceLocationDataAggregationStrategy implements AggregationStrategy {

    private static final Logger LOGGER = LoggerFactory.getLogger(DistanceAggregationStrategy.class.getName());

    /**
     * For each response we should have a Json Object (unmarshalled) that needs to be
     * merged into the Running List Of ResrouceLocationData entries
     *
     * @param oldExchange
     * @param newExchange
     * @return
     */
    @Override public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
        // put order together in old exchange by adding the order from new exchange

        // Handle First Time Processing And Set Up
        if (null == oldExchange || null == oldExchange.getProperty("entry_list")) {
            // 1. Add The Empty List Of RDLs
            ArrayList<ResourceLocationData> list = new ArrayList<>(40);
            ResourceLocationData rdl = (ResourceLocationData )newExchange.getProperty("original_rld");
            list.add(rdl);
            newExchange.setProperty("entry_list", list);
            LOGGER.info("Starting New Aggregation Of RDL");
            return newExchange;
        }

        ResourceLocationData rdl = (ResourceLocationData )newExchange.getProperty("original_rld");
        LOGGER.debug("Apply Updates To RDL For Resource: {}", rdl.getResourceId());
        ArrayList<ResourceLocationData> list = (ArrayList<ResourceLocationData> )oldExchange.getProperty("entry_list");
        list.add(rdl);
        oldExchange.setProperty("entry_list", list);

        return oldExchange;
    }
}
