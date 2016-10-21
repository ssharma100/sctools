package com.oracle.ofsc.geolocation.beans;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oracle.ofsc.etadirect.rest.RouteList;
import com.oracle.ofsc.geolocation.transforms.google.DistanceJson;
import com.oracle.ofsc.transforms.RouteReportData;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Created by xxx_sharma on 10/21/16.
 */
public class Location {
    private static final Logger LOGGER = LoggerFactory.getLogger(Location.class.getName());
    private static final ObjectMapper locationMapper = new ObjectMapper();

    /**
     * Based on the list of addresses pull out the origin destination pairs
     *
     * @param exchange
     */
    public void extractOriginDestination(Exchange exchange) {

        List<RouteReportData> data = exchange.getIn().getBody(List.class);
        LOGGER.info("Extracting Geolocation Coordinates");
        TreeMap<Integer, String> map = new TreeMap<>();

        Iterator<RouteReportData> iter = data.iterator();
        while (iter.hasNext()) {
            // Load the Tree Map
            RouteReportData row = iter.next();
            map.put(Integer.valueOf(row.getRoutePosition()), row.getLatitude() + "," + row.getLongitude());
        }

        // Now Build The List Of Origin Destinations
        List<String> routePoints = new ArrayList<>();
        Iterator<Integer> mapIterator = map.keySet().iterator();
        while (mapIterator.hasNext()) {
            Integer originPos = mapIterator.next();
            if (map.get(originPos+1) == null) {
                break;
            }
            routePoints.add("origins=" + map.get(originPos) + "&destinations=" + map.get(originPos + 1));

        }
        LOGGER.info("Loading Entries: " + routePoints.size() + " For Distance Calculations");
        exchange.getIn().setBody(routePoints);
    }


    public void loadHeaders (Exchange exchange) {
        LOGGER.info("Loading Origin Destination For Call Information for http4");
        String routeCords = exchange.getIn().getBody(String.class);
        exchange.getOut().setHeader(Exchange.HTTP_QUERY, "units=imperial&"+ routeCords + "&key=AIzaSyDG2GXoRuhBSAicyU1TpBJ8PpagJHIyNyk");
    }

    public void covertJsonToRouteReport (Exchange exchange) {
        LOGGER.info("Convert Response From Google Destination Call");
        InputStream is = (InputStream )exchange.getIn().getBody();
        // Marshal Json IS To Object
        DistanceJson distanceJson;
        try {
            distanceJson = locationMapper.readValue(is, DistanceJson.class);
            LOGGER.info("Completed Google Distance List Parsing");
        } catch (IOException e) {
            LOGGER.error("Failed To Parse Google Distance Response Json (InputStream)", e);
            return;
        }

        LOGGER.info("Loading Response Components For Origin: " + distanceJson.getOrigins().get(0));
        // Components
        String distance = distanceJson.getRows().get(0).getElements().get(0).getDistance().getText();
        String time = distanceJson.getRows().get(0).getElements().get(0).getDuration().getText();
        // Map To The RouteReportData
        RouteReportData routeData = new RouteReportData();
        routeData.setOriginAddr(distanceJson.getOrigins().get(0));
        routeData.setDestAddr(distanceJson.getDestinations().get(0));
        routeData.setDriveDistance(distance);
        routeData.setDriveTime(time);

        exchange.getIn().setBody(routeData);
    }
}
