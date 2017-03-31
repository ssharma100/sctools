package com.oracle.ofsc.etadirect.camel.beans;

import org.apache.camel.Exchange;
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

        String sqlStatement = String.format("insert into route_plan (route_id, resource_id, appoint_id, start_time, end_time, latitude, longitude) "
                        + "VALUES (DATE('%s'), '%s', '%s', TIME('%s'), TIME('%s'), %s, %s)",
                started_on, resource_id, appoint_id, started_on, ended_on, latitude, longitude);

        LOGGER.debug("Generated:\n{}", sqlStatement);
        exchange.getIn().setBody(sqlStatement);
    }

}
