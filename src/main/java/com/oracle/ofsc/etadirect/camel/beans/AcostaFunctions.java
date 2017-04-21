package com.oracle.ofsc.etadirect.camel.beans;

import com.oracle.ofsc.etadirect.rest.ResourceJson;
import com.oracle.ofsc.etadirect.rest.RouteInfo;
import com.oracle.ofsc.etadirect.rest.RouteList;
import org.apache.camel.Exchange;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.HashMap;

/**
 * Functionality and data manipulations for Acosta Route processing
 */
public class AcostaFunctions {
    private static final Logger LOGGER = LoggerFactory.getLogger(Activity.class.getName());

    private class HomeLocation {
        protected BigDecimal longitude;
        protected BigDecimal latitude;
    }

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
        sqlStatement.append("insert into route_plan (route_day, resource_id, appoint_id, start_time, end_time, latitude, longitude, route_order) ");
        if (0 == sequence) {
            DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
            DateTime startStart = new DateTime(started_on).minus(Period.minutes(10));
            DateTime startEnd = new DateTime(startStart).plus(Period.minutes(5));

            // Add The First Route + The Starting Route
            sqlStatement.append(
                    String.format("VALUES "
                                    + "(DATE('%s'), '%s', '%s', TIME('%s'), TIME('%s'), %s, %s, %d),", started_on, resource_id, "str" + appoint_id,
                            dtf.print(startStart),
                            dtf.print(startEnd),
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

        // Check For This Being The Last Record, And Add The Home Route.
        if (null != exchange.getProperty("CamelSplitSize")) {
            DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
            DateTime endStart = new DateTime(ended_on).plus(Period.minutes(10));
            DateTime endEnd = new DateTime(endStart).plus(Period.minutes(5));

            // Add End Point/Home
            sqlStatement.append(String.format(",(DATE('%s'), '%s', '%s', TIME('%s'), TIME('%s'), %s, %s, %d)", started_on, resource_id, "end" + appoint_id,
                    dtf.print(endStart), dtf.print(endEnd), homeLatitude, homeLongitude, 1000));
        }
        LOGGER.debug("Generated:\n{}", sqlStatement);
        exchange.getIn().setBody(sqlStatement);
    }

    /**
     * Extracts the OFSC route from the Json response and formats it for te
     * DB table insertion
     * @param exchange
     */
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
        // Store Route Count for later inspection and processing.
        exchange.getIn().setHeader("route_count", routeList.getTotalResults());
        if (routeList.getTotalResults() == 0) {
            LOGGER.warn("Skipping Response Processing - No Routes For {}, On Route Date {}",
                    exchange.getIn().getHeader("id"),
                    exchange.getIn().getHeader("routeDay"));

            exchange.getIn().setBody(
                    "select 'No Routes Request Resource: " + exchange.getIn().getHeader("id") + " Route Day: " + exchange.getIn().getHeader("routeDay") + "'");
            return;
        }

        // Get The Home Location For This Resource Using JDBC
        HomeLocation home;
        try {
            home = getHomeLocation((String )exchange.getIn().getHeader("id"));
        } catch (SQLException e) {
            LOGGER.error("Failed To Make Home Location Query For {}", exchange.getIn().getHeader("id"));
            home = new HomeLocation();
            home.longitude = BigDecimal.ZERO;
            home.longitude = BigDecimal.ZERO;
        }
        StringBuilder insertStmt = new StringBuilder();
        insertStmt.append("insert into route_plan (route_day, resource_id, appoint_id, start_time, end_time, "
                + "latitude, longitude, route_order, ofsc_est_drive, ofsc_est_work) VALUES");
        RouteInfo lastRouteItem = null;
        for (RouteInfo routeItem: routeList.getItems()) {
            if (routeItem.getPositionInRoute() == 1) {
                // Insert Home Location
                DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
                DateTime startStart = dtf.parseDateTime(routeItem.getStartTime()).minus(Period.minutes(10));
                DateTime startEnd = dtf.parseDateTime(routeItem.getStartTime()).minus(Period.minutes(5));
                insertStmt.append(String.format(" ('%s', '%s', '%s', TIME('%s'), TIME('%s'), %s, %s, %s, %s, %s),",
                                routeItem.getDate(), routeItem.getResourceId(), "str" + routeItem.getApptNumber(),
                                dtf.print(startStart), dtf.print(startEnd), home.latitude,
                                home.longitude, -1, "0", "0"));
            }
            insertStmt.append(String.format(" ('%s', '%s', '%s', TIME('%s'), TIME('%s'), %s, %s, %s, %s, %s),", routeItem.getDate(), routeItem.getResourceId(),
                            routeItem.getApptNumber(), routeItem.getStartTime(), routeItem.getEndTime(), routeItem.getLatitude(), routeItem.getLongitude(),
                            routeItem.getPositionInRoute(), routeItem.getTravelTime(), routeItem.getDuration()));

            // Always Store The End time
            lastRouteItem = routeItem;
        }

        // Insert The Home Location
        // Insert Home Location
        DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        DateTime startStart = dtf.parseDateTime(lastRouteItem.getEndTime()).plus(Period.minutes(5));
        DateTime startEnd = dtf.parseDateTime(lastRouteItem.getEndTime()).plus(Period.minutes(10));
        insertStmt.append(String.format(" ('%s', '%s', '%s', TIME('%s'), TIME('%s'), %s, %s, %s, %s, %s)",
                lastRouteItem.getDate(), lastRouteItem.getResourceId(), "end" + lastRouteItem.getApptNumber(),
                dtf.print(startStart), dtf.print(startEnd), home.latitude,
                home.longitude, 1000, "0", "0"));

        exchange.getIn().setBody(insertStmt.toString());
    }

    public void prepareResourceUpdate(Exchange exchange) {
        LOGGER.info("Prepare Resource Update For  Route Day={}", exchange.getIn().getHeader("routeDay"));
        String id = (String )exchange.getIn().getHeader("id");
        String routeDay = (String )exchange.getIn().getHeader("routeDay");
        Integer hoursImpact = Integer.parseInt("0");
        try {
            hoursImpact = getImpactHoursWorked(id, routeDay);
        } catch (SQLException e) {
            LOGGER.error("Cannot Obtain Hours For Resource: {}, RouteDay={}", id, routeDay);
        }
        exchange.getIn().setHeader("impact_used_hours", hoursImpact);
    }
    /**
     * Based on a DB entry of a Resource, this method will store
     * the record information in the Exchange and make the follow on call
     * for the Fetch Of the routes.
     *
     * @param exchange
     */
    public void prepForRouteExtract (Exchange exchange) {
        // The routeDay - as required by the route_plan is already in place:
        LOGGER.info("Using Header Value Of Route Day={}", exchange.getIn().getHeader("routeDay"));
        // Store The Response Object In Case We Need It Later:

        HashMap<String, Object> resultFields = (HashMap<String, Object> )exchange.getIn().getBody();
        // Need To Store the RouteID From the table:
        String id = (String )resultFields.get("Employee_No");
        exchange.getIn().setHeader("id",id);

        // Blank Out The Body
        exchange.getIn().setBody(null);
    }

    /**
     * Query The DB for the resource's home location
     * @param resourceId
     * @return
     * @throws SQLException
     */
    private HomeLocation getHomeLocation(String resourceId) throws SQLException {
        Connection conn =
                DriverManager.getConnection(
                        "jdbc:mysql://acosta.c4ury24fv0lk.us-west-2.rds.amazonaws.com:3306/acosta",
                        "root", "etadirect123");
        Statement stmt = conn.createStatement();
        String sql = "select Longitude, Latitude from associates_info where Employee_no='" + resourceId + "'";
        ResultSet rs = stmt.executeQuery(sql);
        rs.next();
        HomeLocation home = new HomeLocation();
        home.latitude = rs.getBigDecimal("Latitude");
        home.longitude = rs.getBigDecimal("Longitude");
        LOGGER.info("Resource {} Has Home AT Lat:{}, Long:{}", resourceId, home.latitude, home.longitude);

        //STEP 6: Clean-up environment
        rs.close();
        stmt.close();
        conn.close();
        return home;
    }

    public int getImpactHoursWorked(String resourceId, String routeDay) throws SQLException {
        Connection conn =
                DriverManager.getConnection(
                        "jdbc:mysql://acosta.c4ury24fv0lk.us-west-2.rds.amazonaws.com:3306/acosta",
                        "root", "etadirect123");
        Statement stmt = conn.createStatement();
        String sql = "select sum(ofsc_est_work) AS ImpactHours from route_plan where resource_id = '" + resourceId
                + "' and route_day='" + routeDay + "' and appoint_id  like 'ImpA%'";
        ResultSet rs = stmt.executeQuery(sql);
        rs.next();
        int hours = rs.getInt("ImpactHours");

        LOGGER.info("Resource {} Has {} Impact Work Hours On {}", resourceId, hours, routeDay);

        rs.close();
        stmt.close();
        conn.close();
        return hours;
    }
}
