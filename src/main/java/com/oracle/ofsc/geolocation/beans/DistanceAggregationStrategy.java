package com.oracle.ofsc.geolocation.beans;

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
public class DistanceAggregationStrategy implements AggregationStrategy {
    private static final Logger LOGGER = LoggerFactory.getLogger(DistanceAggregationStrategy.class.getName());

    @Override
    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
        LOGGER.info("Entered Aggregator For Distance");
        if (oldExchange == null) {
            // the first time we aggregate we only have the new exchange,
            // Just add this one!
            List<RouteReportData> oldResultList = new ArrayList<>();
            RouteReportData  currentResultList = newExchange.getIn().getBody(RouteReportData.class);
            if (null != currentResultList)
            oldResultList.add(currentResultList);
            newExchange.getIn().setBody(oldResultList);
            return newExchange;
        }

        List<RouteReportData> oldResultList = oldExchange.getIn().getBody(List.class);
        RouteReportData  currentResultList = newExchange.getIn().getBody(RouteReportData.class);

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
            LOGGER.info("New Exchange List: " + oldResultList.size());
            // put items together
            oldResultList.add(currentResultList);
            // put combined List back on old to preserve it
            oldExchange.getIn().setBody(oldResultList);
        }
        LOGGER.info("Updated Old Exchange List: " + oldResultList.size());

        // return old as this is the one that has all the orders gathered until now
        return oldExchange;
    }
}
