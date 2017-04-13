package com.oracle.ofsc.etadirect.camel.beans;

import com.oracle.ofsc.etadirect.rest.RouteInfo;
import com.oracle.ofsc.etadirect.rest.RouteList;
import org.apache.camel.Exchange;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashMap;

/**
 * Functionality and data manipulations for Acosta Route processing
 */
public class AcostaFunctions {
    private static final Logger LOGGER = LoggerFactory.getLogger(Activity.class.getName());

    public void insertRouteSql(Exchange exchange) {
        LOGGER.debug("Processing Stream Insertion Sequence: {}", exchange.getProperty("CamelSplitIndex"));
        HashMap<String, Object> resultFields = (HashMap<String, Object> )exchange.getIn().getBody();
        Timestamp started_on = (Timestamp )resultFields.get("CALL_STARTED_LOCAL");
        String resource_id = (String )resultFields.get("COMPLETED_BY_EMPLOYEE_NO");
        String appoint_id = (String )resultFields.get("ACTUAL_CALLID");
        Timestamp ended_on = (Timestamp )resultFields.get("COMPLETED_ON_LOCAL");
        BigDecimal latitude = (BigDecimal )resultFields.get("Latitude");
        BigDecimal longitude = (BigDecimal )resultFields.get("Longitude");

        String sqlStatement = String.format("insert into route_plan (route_day, resource_id, appoint_id, start_time, end_time, latitude, longitude) "
                        + "VALUES (DATE('%s'), '%s', '%s', TIME('%s'), TIME('%s'), %s, %s)",
                started_on, resource_id, appoint_id, started_on, ended_on, latitude, longitude);

        LOGGER.debug("Generated:\n{}", sqlStatement);
        exchange.getIn().setBody(sqlStatement);
    }

    public void extractRoutesToSQL(Exchange exchange) {
        LOGGER.debug("Processing Stream Insertion Sequence: {}", exchange.getProperty("CamelSplitIndex"));
        org.restlet.engine.adapter.HttpResponse response = (org.restlet.engine.adapter.HttpResponse) exchange.getIn().getHeader("CamelRestletResponse");
        if (null == response || !response.getStatus().isSuccess()) {
            LOGGER.warn("Skipping Response Processing - Failed Request For {}, On Route Date {}",
                    exchange.getIn().getHeader("id"),
                    exchange.getIn().getHeader("routeDay"));
            // Do Something useless for the SQL statement
            exchange.getIn().setBody("select 'Failed Request Resource: " + exchange.getIn().getHeader("id")
                    + " Route Day: " +exchange.getIn().getHeader("routeDay") + "'" );
            return;
        }

        // Get Response Object Of Routes
        RouteList routeList = (RouteList) exchange.getIn().getBody();
        if (routeList.getTotalResults() == 0) {
            LOGGER.warn("Skipping Response Processing - No Routes For {}, On Route Date {}",
                    exchange.getIn().getHeader("id"),
                    exchange.getIn().getHeader("routeDay"));
            exchange.getIn().setBody("select 'No Routes Request Resource: " + exchange.getIn().getHeader("id")
                    + " Route Day: " + exchange.getIn().getHeader("routeDay") + "'" );
            return;
        }

        StringBuilder insertStmt = new StringBuilder();
        insertStmt.append("insert into route_plan (route_day, resource_id, appoint_id, start_time, end_time, "
                + "latitude, longitude, route_order, ofsc_est_drive, ofsc_est_work) VALUES");
        for (RouteInfo routeItem: routeList.getItems()) {

            insertStmt.append(String.format(" ('%s', '%s', '%s', TIME('%s'), TIME('%s'), %s, %s, %s, %s, %s),",
                            routeItem.getDate(), routeItem.getResourceId(), routeItem.getApptNumber(),
                            routeItem.getStartTime(), routeItem.getEndTime(), routeItem.getLatitude(),
                            routeItem.getLongitude(), routeItem.getPositionInRoute(), routeItem.getTravelTime(),
                            routeItem.getDuration())
            );
        }
        // Strip Out The Training Comma
        exchange.getIn().setBody(StringUtils.stripEnd(insertStmt.toString(), ","));
    }
}
