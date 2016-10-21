package com.oracle.ofsc.etadirect.camel.beans;

import com.oracle.ofsc.transforms.RouteReportData;
import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xxx_sharma on 10/20/16.
 */
public class AggregatorStrategy implements AggregationStrategy {
    private static final Logger LOGGER = LoggerFactory.getLogger(AggregatorStrategy.class.getName());

    @Override
    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
        if (oldExchange == null) {
            // the first time we aggregate we only have the new exchange,
            // so we just return it
            return newExchange;
        }

        List<RouteReportData> oldResultList = oldExchange.getIn().getBody(List.class);
        List<RouteReportData>  currentResultList = newExchange.getIn().getBody(List.class);

        // If the First Time Around - No Results Are Provided, Just Skip The Aggregation
        if (null == oldResultList) {
            LOGGER.info("No Original Results - Creating Blank");
            oldResultList = new ArrayList<>();
        }
        LOGGER.info("Old Exchange List: " + oldResultList.size());

        if (null == currentResultList) {
            LOGGER.info("Aborting Aggregation - No Current Results");
        }
        else {
            LOGGER.info("New Exchange List: " + currentResultList.size());
            // put items together
            oldResultList.addAll(currentResultList);
            // put combined List back on old to preserve it
            oldExchange.getIn().setBody(oldResultList);
        }
        LOGGER.info("Updated Old Exchange List: " + oldResultList.size());

        // return old as this is the one that has all the orders gathered until now
        return oldExchange;
    }
}
