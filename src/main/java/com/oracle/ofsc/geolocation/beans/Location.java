package com.oracle.ofsc.geolocation.beans;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oracle.ofsc.etadirect.camel.beans.Security;
import com.oracle.ofsc.etadirect.rest.Assignment;
import com.oracle.ofsc.etadirect.rest.InsertLocation;
import com.oracle.ofsc.etadirect.rest.LocationAssignment;
import com.oracle.ofsc.etadirect.rest.RouteList;
import com.oracle.ofsc.geolocation.transforms.google.DistanceJson;
import com.oracle.ofsc.transforms.LocationListData;
import com.oracle.ofsc.transforms.ResourceLocationData;
import com.oracle.ofsc.transforms.RouteReportData;
import com.oracle.ofsc.transforms.TransportationActivityData;
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
    private static final String PROP_ORIGINALS = "Originals";

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

    /**
     * Extracts the Resource + makes a copy of the body for later use
     * @param exchange
     */
    public void extractResource(Exchange exchange) {
        LOGGER.info("Extract Resource ID From Resource Location Data List");
        ResourceLocationData rld = exchange.getIn().getBody(ResourceLocationData.class);
        exchange.getIn().setHeader("id", rld.getResourceId());
        exchange.setProperty("original_rld", rld);
        exchange.getIn().setBody(null);
    }

    /**
     * Extracts the aggregated response and creates an array of RDL entries for
     * marshalling
     *
     * @param exchange
     */
    public void buildResourceLocationData (Exchange exchange) {
        LOGGER.info("Extract Resource ID From Resource Location Data List");
        ArrayList<ResourceLocationData> list = (ArrayList<ResourceLocationData> )exchange.getProperty("entry_list");
        LOGGER.info("Extracting {} Entries From Aggregation List", list.size());
        exchange.getIn().setBody(list);
    }

    public void associateResourceLocations(Exchange exchange) {
        LOGGER.info("Loading Location Application Information To JSON From Resource Location Data");

        ResourceLocationData rld = exchange.getIn().getBody(ResourceLocationData.class);
        // Set Values For HTTP Call And Authentication To ETAdirect
        HashMap<String, String> authInfo =
                Security.extractAuthInfo((String )exchange.getIn().getHeader("CamelHttpQuery"));
        String username = authInfo.get("user") + "@" + authInfo.get("company");
        String passwd =   authInfo.get("passwd");
        exchange.getIn().setHeader("username", username);
        exchange.getIn().setHeader("passwd", passwd);
        exchange.getIn().setHeader(Exchange.CONTENT_TYPE, "application/json");

        exchange.getIn().setHeader("id", rld.getResourceId());
        LocationAssignment locationAssignment = new LocationAssignment();
        // Set Up The Items
        Assignment mon = new Assignment();
        mon.setStart(rld.getMonStart());
        mon.setEnd(rld.getMonEnd());
        mon.setHomeZoneCenter(rld.getMonHome());
        locationAssignment.setMon(mon);

        Assignment tue = new Assignment();
        tue.setStart(rld.getTueStart());
        tue.setEnd(rld.getTuesEnd());
        tue.setHomeZoneCenter(rld.getTuesHome());
        locationAssignment.setTue(tue);

        Assignment wed = new Assignment();
        wed.setStart(rld.getWedStart());
        wed.setEnd(rld.getWedEnd());
        wed.setHomeZoneCenter(rld.getWedHome());
        locationAssignment.setWed(wed);

        Assignment thu = new Assignment();
        thu.setStart(rld.getThursStart());
        thu.setEnd(rld.getThursEnd());
        thu.setHomeZoneCenter(rld.getThursHome());
        locationAssignment.setThu(thu);

        Assignment fri = new Assignment();
        fri.setStart(rld.getFriStart());
        fri.setEnd(rld.getFirEnd());
        fri.setHomeZoneCenter(rld.getFriHome());
        locationAssignment.setFri(fri);

        Assignment sat = new Assignment();
        sat.setStart(rld.getSatStart());
        sat.setEnd(rld.getSatEnd());
        sat.setHomeZoneCenter(rld.getSatHome());
        locationAssignment.setSat(sat);

        Assignment sun = new Assignment();
        sun.setStart(rld.getSunStart());
        sun.setEnd(rld.getSunEnd());
        sun.setHomeZoneCenter(rld.getSunHome());
        locationAssignment.setSun(sun);

        String restBody = null;
        try {
            restBody = locationMapper.writeValueAsString(locationAssignment);
        } catch (JsonProcessingException e) {
            LOGGER.error("Failed To Marshal JSON Object: {}", e.getMessage());
        }
        exchange.setProperty("original_rld", rld);
        exchange.getIn().setBody(restBody);
    }

    public void loadInsertLocation (Exchange exchange) {
        LOGGER.info("Loading Location Information To JSON From LocationData");

        LocationListData location = exchange.getIn().getBody(LocationListData.class);
        // Check For Property And Set If This Is The First Time Around
        List<LocationListData> originalLocations = exchange.getProperty(PROP_ORIGINALS, List.class);
        if (null == originalLocations) {
            originalLocations = new ArrayList<LocationListData>(10);
            exchange.setProperty(PROP_ORIGINALS, originalLocations);
        }
        originalLocations.add(location);

        // Generate Output Json
        InsertLocation jsonLocation = new InsertLocation();
        jsonLocation.setLabel(location.getName());
        jsonLocation.setAddress(location.getStreet());
        jsonLocation.setCity(location.getCity());
        jsonLocation.setState(location.getState());
        jsonLocation.setPostalCode(location.getZip());
        jsonLocation.setLatitude(location.getLatitude());
        jsonLocation.setLongitude(location.getLongitude());

        // Set Values For HTTP Call And Authentication To ETAdirect
        HashMap<String, String> authInfo =
                Security.extractAuthInfo((String )exchange.getIn().getHeader("CamelHttpQuery"));
        String username = authInfo.get("user") + "@" + authInfo.get("company");
        String passwd =   authInfo.get("passwd");
        exchange.getIn().setHeader("id", location.getExternalId());
        exchange.getIn().setHeader("username", username);
        exchange.getIn().setHeader("passwd", passwd);
        exchange.getIn().setHeader(Exchange.CONTENT_TYPE, "application/json");

        String restBody = null;
        try {
            restBody = locationMapper.writeValueAsString(jsonLocation);
        } catch (JsonProcessingException e) {
            LOGGER.error("Failed To Marshal JSON Object: {}", e.getMessage());
        }
        exchange.getIn().setBody(restBody);
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
