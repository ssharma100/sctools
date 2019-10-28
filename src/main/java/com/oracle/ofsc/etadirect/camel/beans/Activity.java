package com.oracle.ofsc.etadirect.camel.beans;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oracle.ofsc.etadirect.rest.ActivityItem;
import com.oracle.ofsc.etadirect.rest.ActivitySearchResponse;
import com.oracle.ofsc.etadirect.rest.ResourceAssignmentItem;
import com.oracle.ofsc.etadirect.rest.ResourceAssignmentItems;
import com.oracle.ofsc.etadirect.soap.GetActivity;
import com.oracle.ofsc.etadirect.soap.Property;
import com.oracle.ofsc.etadirect.soap.User;
import com.oracle.ofsc.transforms.FiberActivityData;
import com.oracle.ofsc.transforms.GenericActivityData;
import com.oracle.ofsc.transforms.ResourceAssignment;
import com.oracle.ofsc.transforms.TransportationActivityData;
import org.apache.camel.Exchange;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Conversion Bean - Given an input of the specific type, will convert the Bindy object
 * into the required object for a SOAP or RESTful call to ETAdirect.
 */
public class Activity {
    private static final Logger LOGGER = LoggerFactory.getLogger(Activity.class.getName());
    private static final ObjectMapper activityMapper = new ObjectMapper();

    private static final String SOAP_WRAPPER_HEADER =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:urn=\"urn:toa:activity\">\n" +
                    "   <soapenv:Header/>\n" +
                    "   <soapenv:Body>";

    private static final String SOAP_WRAPPER_FOOTER =
                    "   </soapenv:Body>\n" +
                    "</soapenv:Envelope>";

    private static final boolean USE_MD5 = true;

    /**
     * Method supporting the generation of a call to assign a resource to an activity
     * @param exchange
     */
    @SuppressWarnings("unused")
    public void assignResource(Exchange exchange) {

        ResourceAssignment ra = (ResourceAssignment )exchange.getIn().getBody();

        LOGGER.info("Generate Assignment For a_id {} With Resource ID {}" , ra.getEtaId(), ra.getRequiredResource());
        HashMap<String, String> authInfo =
                Security.extractURLQueryParameters((String )exchange.getIn().getHeader("CamelHttpQuery"));

        String username = authInfo.get("user") + "@" + authInfo.get("company");
        String passwd =   authInfo.get("passwd");

        exchange.getIn().setHeader("id", ra.getEtaId());

        ResourceAssignmentItems assignments = new ResourceAssignmentItems();
        ResourceAssignmentItem item = new ResourceAssignmentItem();
        item.setPreferenceType("required");
        item.setResourceId(ra.getRequiredResource());
        ArrayList<ResourceAssignmentItem> list = new ArrayList<>(3);
        list.add(item);
        assignments.setItems(list);

        // Skip The Pos In Route - Default To Unordered.
        String restBody = null;
        try {
            restBody = activityMapper.writeValueAsString(assignments);
        } catch (JsonProcessingException e) {
            LOGGER.error("Failed To Marshal JSON Object: {}", e.getMessage());
        }
        // Set Values For HTTP4:
        exchange.getIn().setHeader("username", username);
        exchange.getIn().setHeader("passwd", passwd);
        exchange.getIn().setHeader(Exchange.CONTENT_TYPE, "application/json");
        exchange.getIn().setBody(restBody);
    }

    /**
     * Method supporting the generation of a call to assign a resource to an activity
     * @param exchange
     */
    @SuppressWarnings("unused")
    public void assignResourceFromActivityResponse(Exchange exchange) {
        LOGGER.info("Converting OFSC Activity Response To Assignment");

        ResourceAssignment ra = new ResourceAssignment();

        // Body Should Contain Json Response:
        String restBody = null;
        try {
            ActivitySearchResponse response =
                    activityMapper.readValue((InputStream) exchange.getIn().getBody(), ActivitySearchResponse.class);
            ActivityItem item = response.getItems().get(0);
            ra.setRequiredResource((String )exchange.getProperty("resourceId"));
            ra.setActivityKey((String )exchange.getProperty("activityKey"));
            ra.setEtaId(item.getActivityId());
            LOGGER.info("Mapped Work Order {} To A_ID {} For Resource {}", ra.getActivityKey(), ra.getActivityKey(), ra.getRequiredResource());
        } catch (IOException e) {
            LOGGER.error("Failed To UnMarshal JSON Object: {}", e.getMessage());
        }

        exchange.getIn().setBody(ra);
        exchange.getIn().setHeader("CamelHttpQuery", exchange.getProperty("authInfo"));
        this.assignResource(exchange);
    }
    /**
     * Based on a record from the standard Activity "list" (from Bindy Unmarshal)
     * will extract the WO information and date range needed to do a query
     * @param exchange
     */
    @SuppressWarnings("unused")
    public void mapToWOSearch (Exchange exchange) {
        GenericActivityData activityData = (GenericActivityData )exchange.getIn().getBody();
        LOGGER.info("Generate W/O Search For Activity Key {} With Resource ID {}" , activityData.getActivityKey(), activityData.getResourceId());
        HashMap<String, String> authInfo =
                Security.extractURLQueryParameters((String )exchange.getIn().getHeader("CamelHttpQuery"));

        String username = authInfo.get("user") + "@" + authInfo.get("company");
        String passwd =   authInfo.get("passwd");

        exchange.getIn().setHeader("username", username);
        exchange.getIn().setHeader("passwd", passwd);
        exchange.getIn().setHeader("apptNumber", activityData.getActivityKey());
        exchange.getIn().setHeader("dateFrom", activityData.getActivityStartDate());
        exchange.getIn().setHeader("dateTo", activityData.getActivityStartDate());
        exchange.setProperty("resourceId", activityData.getResourceId());
        exchange.setProperty("activityKey", activityData.getActivityKey());
        exchange.setProperty("authInfo", exchange.getIn().getHeader("CamelHttpQuery"));

        exchange.getIn().setBody(null);
    }

    /**
     * Generates body for resource "get" request
     * @param exchange
     */
    @SuppressWarnings("unused")
    public void mapToGetRequest (Exchange exchange) {
        String activityId = (String )exchange.getIn().getHeader("id");
        LOGGER.info("Generate Body For ActivityID: {}", activityId);
        User userBlock =
                Security.generateUserAuth((String )exchange.getIn().getHeader("CamelHttpQuery"), !USE_MD5);

        GetActivity activity = new GetActivity();
        activity.setUser(userBlock);
        activity.setActivity_id(activityId);

        // Convert To String As The Mapping For spring-ws will not correctly set the headers in the Soap Envelope
        String body = null;
        try {
            JAXBContext context = JAXBContext.newInstance(GetActivity.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, false);
            marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
            StringWriter sw = new StringWriter();
            marshaller.marshal(activity, sw);
            body = sw.toString();
        }catch (JAXBException e) {
            LOGGER.error("Failed To Marshal Object: {}", e.getMessage());
        }
        StringBuffer sb = new StringBuffer();
        sb.append(SOAP_WRAPPER_HEADER).append(body).append(SOAP_WRAPPER_FOOTER);
        exchange.getIn().setBody(sb.toString());
    }

    /**
     *
     * @param exchange
     */
    @SuppressWarnings("unused")
    public void mapToInsertSoapRequest (Exchange exchange) {
        DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd");
        String bucketId = (String) exchange.getIn().getHeader("id");
        LOGGER.info("Generate Body For BucketID: {}", bucketId);
        User userBlock =
                Security.generateUserAuth((String )exchange.getIn().getHeader("CamelHttpQuery"), !USE_MD5);

        com.oracle.ofsc.etadirect.soap.InsertActivity activityIns = new com.oracle.ofsc.etadirect.soap.InsertActivity();
        activityIns.setUser(userBlock);

        activityIns.setBucketId(bucketId);
        activityIns.setDate(dtf.print(new DateTime()));

        ArrayList<Property> properties = new ArrayList<>(10);
        properties.add(new Property("type", "regular"));
        properties.add(new Property("status", "pending"));
        properties.add(new Property("time_zone", "Pacific"));
        properties.add(new Property("language", "en"));

        activityIns.setProperties(properties);

        // Skip The Pos In Route - Default To Unordered.
        // Convert To String As The Mapping For spring-ws will not correctly set the headers in the Soap Envelope
        String soapBody = null;
        try {
            JAXBContext context = JAXBContext.newInstance(com.oracle.ofsc.etadirect.soap.InsertActivity.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
            StringWriter sw = new StringWriter();
            marshaller.marshal(activityIns, sw);
            soapBody = sw.toString();
        }catch (JAXBException e) {
            LOGGER.error("Failed To Marshal Object: {}", e.getMessage());
        }
        StringBuffer sb = new StringBuffer();
        sb.append(SOAP_WRAPPER_HEADER).append(soapBody).append(SOAP_WRAPPER_FOOTER);
        exchange.getIn().setBody(sb.toString());
    }

    /**
     * Authentication Only
     * @param exchange
     */
    public void authOnly(Exchange exchange) {

        LOGGER.info("Generate Auth Only Headers");

        HashMap<String, String> authInfo = Security.extractURLQueryParameters((String) exchange.getIn().getHeader("CamelHttpQuery"));

        String username = authInfo.get("user") + "@" + authInfo.get("company");
        String passwd = authInfo.get("passwd");
        // Set Values For HTTP4:
        exchange.getIn().setHeader("username", username);
        exchange.getIn().setHeader("passwd", passwd);
        exchange.getIn().setHeader("dateFrom", authInfo.get("dateFrom"));
        exchange.getIn().setHeader("dateTo", authInfo.get("dateTo"));

        LOGGER.info("SetUp Completed For User: " + exchange.getIn().getHeader("username"));
    }

    /**
     * Performs the mapping from the read data used in the activity to load information
     * into the activity insertion
     *
     * @param exchange
     */
    @SuppressWarnings("unused")
    public void mapToInsertRestRequest (Exchange exchange) {
        String bucketId = (String) exchange.getIn().getHeader("id");
        LOGGER.info("Generate Body For BucketID: {}", bucketId);

        String category = (String) exchange.getIn().getHeader("activity_category");
        HashMap<String, String> authInfo =
                Security.extractURLQueryParameters((String) exchange.getIn().getHeader("CamelHttpQuery"));

        String username = authInfo.get("user") + "@" + authInfo.get("company");
        String passwd =   authInfo.get("passwd");

        Boolean isSLA = new Boolean(authInfo.get("SLA"));
        com.oracle.ofsc.etadirect.rest.InsertActivity activityIns = null;
        switch (category) {
        case "transportation":
            LOGGER.info("Generating Activity Request From Transportation Activity");
            activityIns = this.generateTransportationActivity(exchange.getIn().getBody(), bucketId);
            break;
        case "generic":
            LOGGER.info("Generating Activity Request From Generic Activity");
            activityIns = this.generateGenericActivity(exchange.getIn().getBody(), bucketId, isSLA);
            break;
        case "fiber":
            LOGGER.info("Generating Activity Request From Fiber Activity");
            activityIns = this.generateFiberActivity(exchange.getIn().getBody());
            break;
        default:
            LOGGER.error("Unrecognized Requesting Endpoint {} - No Activity Generator Found", category);
        }

        // Skip The Pos In Route - Default To Unordered.
        String restBody = null;
        try {
            restBody = activityMapper.writeValueAsString(activityIns);
        } catch (JsonProcessingException e) {
            LOGGER.error("Failed To Marshal JSON Object: {}", e.getMessage());
        }
        // Set Values For HTTP4:
        exchange.getIn().setHeader("username", username);
        exchange.getIn().setHeader("passwd", passwd);
        exchange.getIn().setHeader(Exchange.CONTENT_TYPE, "application/json");
        exchange.getIn().setBody(restBody);
    }

    /**
     * Specific mapping of input (prased object) holding a transportation activity - to the Rest object
     * @param inObject
     * @param bucketId
     * @return
     */
    private com.oracle.ofsc.etadirect.rest.InsertActivity generateTransportationActivity(Object inObject, String bucketId) {
        TransportationActivityData activityData = (TransportationActivityData )inObject;
        com.oracle.ofsc.etadirect.rest.InsertActivity activityIns = new com.oracle.ofsc.etadirect.rest.InsertActivity();

        // Build Activity For Insertion

        activityIns.setResourceId(bucketId);
        activityIns.setDate(activityData.getActivityDate());
        activityIns.setActivityType(activityData.getActivityType());
        activityIns.setApptNumber(activityData.getAppointmentKey());
        activityIns.setCustomerName("Joe Blow");
        activityIns.setTimeZone(activityData.getTimezone());
        activityIns.setDuration(activityData.getDuration());
        activityIns.setLatitude(activityData.getLatitude());
        activityIns.setLongitude(activityData.getLongitude());

        DateTimeFormatter inFormatter = DateTimeFormat.forPattern("HH:mm:ss");
        DateTimeFormatter outFormatter = DateTimeFormat.forPattern("HH:mm:ss");
        // Set Appointment Time Management
        if(StringUtils.isNotBlank(activityData.getPickUpTime())) {
            // Make the PickUp Window 1 Hour Long
            // We only get the start time - so set it first
            DateTime pickUpStart = inFormatter.parseDateTime(activityData.getPickUpTime());
            activityIns.setServiceWindowStart(outFormatter.print(pickUpStart));
            activityIns.setServiceWindowEnd(outFormatter.print(pickUpStart.plusHours(1)));
        } else {

            if (StringUtils.isNotBlank(activityData.getDeliveryStart())) {
                DateTime deliveryStart = inFormatter.parseDateTime(activityData.getDeliveryStart());
                activityIns.setServiceWindowStart(outFormatter.print(deliveryStart));
            }

            if (StringUtils.isNotBlank(activityData.getDeliveryEnd()))  {
                DateTime deliveryEnd = inFormatter.parseDateTime(activityData.getDeliveryEnd());
                activityIns.setServiceWindowEnd(outFormatter.print(deliveryEnd));
            }
        }
        String liftGate = activityData.getLiftGate();
        if (StringUtils.isNotBlank((liftGate)) && liftGate.equalsIgnoreCase("y")) {
            activityIns.setLift_gate(liftGate);
        }

        return activityIns;
    }

    /**
     * Mapping Between Parsed CSV Object model and the outbound RESTful object.
     *
     * @param inObject
     * @return
     */
    private com.oracle.ofsc.etadirect.rest.InsertActivity generateFiberActivity(Object inObject) {
        FiberActivityData activityData = (FiberActivityData )inObject;
        com.oracle.ofsc.etadirect.rest.InsertActivity activityIns = new com.oracle.ofsc.etadirect.rest.InsertActivity();

        // Map Fields For Outbound RESTful Activity Call:
        activityIns.setResourceId(activityData.getResourceId());
        activityIns.setDate(activityData.getStartDate());
        activityIns.setApptNumber(activityData.getActivityKey());
        activityIns.setActivityType(activityData.getActivityType());
        activityIns.setgCustomerType(activityData.getgCustomer());
        activityIns.setCustomerNumber("Migration Process");
        activityIns.setgRepairType(activityData.getRepairType());
        activityIns.setgServices(StringUtils.replace(activityData.getgServices(),"|", ","));
        activityIns.setgTvCount(activityData.getgTvCount());
        activityIns.setTimeSlot(activityData.getTimeslot());
        activityIns.setCustomerName(activityData.getAddressId());
        activityIns.setStreetAddress(activityData.getStreet());
        activityIns.setCity(activityData.getCity());
        activityIns.setPostalCode(activityData.getPostalCode());
        activityIns.setStateProvince(activityData.getState());
        activityIns.setCustomerNumber(activityData.getAccountId());
        activityIns.setCustomerCell(activityData.getPhoneNumber());

        return activityIns;
    }
    /**
     * Mapping between the inbound parsed Bindy object, for Generic activity types.
     * @param inObject
     * @param bucketId
     * @return
     */
    private com.oracle.ofsc.etadirect.rest.InsertActivity generateGenericActivity(Object inObject, String bucketId, boolean isSLA) {
        GenericActivityData activityData = (GenericActivityData)inObject;
        com.oracle.ofsc.etadirect.rest.InsertActivity activityIns = new com.oracle.ofsc.etadirect.rest.InsertActivity();

        activityIns.setResourceId(bucketId);
        // Date For The Activity - Leave Blank For Now
        // activityIns.setDate(activityData.getActivityDate());
        activityIns.setActivityType(activityData.getActivityType());
        activityIns.setApptNumber(activityData.getActivityKey());
        activityIns.setCustomerName(activityData.getStore());
        activityIns.setTimeZone(activityData.getTimezone());
        activityIns.setDuration(activityData.getDuration());
        activityIns.setLatitude(activityData.getLatitude());
        activityIns.setLongitude(activityData.getLongitude());
        activityIns.setStreetAddress(activityData.getStreetAddr());
        activityIns.setCity(activityData.getCity());
        activityIns.setStateProvince(activityData.getState());
        activityIns.setPostalCode(activityData.getZipCode());
        activityIns.setCountry(activityData.getCountry());

        activityIns.setTimeSlot(activityData.getTimeSlot());
        if (isSLA) {
            LOGGER.info("Using SLA Of Appointment For Loader");
            activityIns.setSlaWindowStart(activityData.getActivityStartDate() + " " + activityData.getActivityStartTime());
            activityIns.setSlaWindowEnd(activityData.getActivityEndDate() + " " + activityData.getActivityEndTime());
        }
        else {
            LOGGER.info("Using Day Of Appointment For Loader");
            activityIns.setDate(activityData.getActivityStartDate());
        }

        // Map The Days of The Week
        activityIns.setImpact_allowable_days(extractDOW(activityData.getAllowedDOW()));

        // Only Map The Linkage If Two Or More Resources Are Required
        if (activityData.getResourceCount() > 1) {
            LOGGER.info("Setting Additional Resource Requirement For {}", activityData.getActivityKey());
            activityIns.setLinked_sto(activityData.getActivityKey());
        }

        return activityIns;
    }

    /**
     * Converts the Vectored allowed day of week, to a named DoW string.
     *
     * @param allowedDowVector
     * @return
     */
    private String extractDOW(String allowedDowVector) {
        if (StringUtils.isBlank(allowedDowVector)) {
            LOGGER.info("No DOW Vectored Value Provided");
            return null;
        }

        String allowedDays[] = StringUtils.split(allowedDowVector, "|");
        // We Must Parse Out 7 Days, If Not, Don't Try To Parse It
        if (allowedDays.length != 7) {
            LOGGER.error ("Skipping Parsing Of DoW - Only Found {} Entries", allowedDays.length);
            return null;
        }

        StringBuilder sb = new StringBuilder();
        for (int day=0;day < allowedDays.length; day++) {
            boolean isDayAllowed = allowedDays[day].equals("1");
            switch (day) {
            case 0:
                if (isDayAllowed) sb.append("Mon,");
                break;
            case 1:
                if (isDayAllowed) sb.append("Tues,");
                break;
            case 2:
                if (isDayAllowed) sb.append("Wed,");
                break;
            case 3:
                if (isDayAllowed) sb.append("Thurs,");
                break;
            case 4:
                if (isDayAllowed) sb.append("Fri,");
                break;
            case 5:
                if (isDayAllowed) sb.append("Sat,");
                break;
            case 6:
                if (isDayAllowed) sb.append("Sun,");
                break;
            }
        }

        return StringUtils.stripEnd(sb.toString(), ",");
    }
}

