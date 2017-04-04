package com.oracle.ofsc.etadirect.camel.beans;

import org.apache.camel.Exchange;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
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
        BigDecimal homeLatitude = (BigDecimal )resultFields.get("Home_Latitude");
        BigDecimal homeLongitude = (BigDecimal )resultFields.get("Home_Longitude");

        int sequence = (int ) exchange.getProperty("CamelSplitIndex");

        StringBuilder sqlStatement = new StringBuilder();
        sqlStatement.append("insert into route_plan (route_id, resource_id, appoint_id, start_time, end_time, latitude, longitude, route_order) ");
        if (0 == sequence) {
            DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
            DateTime stubStart = new DateTime(started_on).withHourOfDay(8).withMinuteOfHour(0);
            DateTime stubEnd = new DateTime(started_on).withHourOfDay(8).withMinuteOfHour(15);

            // Add The First Route + The Starting Route
            sqlStatement.append(
                    String.format("VALUES "
                                    + "(DATE('%s'), '%s', '%s', TIME('%s'), TIME('%s'), %s, %s, %d),", started_on, resource_id, "str" + appoint_id,
                            dtf.print(stubStart),
                            dtf.print(stubEnd),
                            homeLatitude, homeLongitude, -1));
            // First Appointment Of The Day
            sqlStatement.append(String.format(" (DATE('%s'), '%s', '%s', TIME('%s'), TIME('%s'), %s, %s, %d)",
                    started_on, resource_id, appoint_id, started_on, ended_on, latitude, longitude, sequence));
        }
        else {
            // Regular Single Addition
            sqlStatement.append(
                    String.format("VALUES (DATE('%s'), '%s', '%s', TIME('%s'), TIME('%s'), %s, %s, %d)", started_on, resource_id, appoint_id, started_on,
                            ended_on, latitude, longitude, sequence));
        }


        LOGGER.debug("Generated:\n{}", sqlStatement);

        exchange.getIn().setBody(sqlStatement);
    }


}
