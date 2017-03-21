package com.oracle.ofsc.etadirect.camel.beans;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oracle.ofsc.etadirect.soap.GetActivity;
import com.oracle.ofsc.etadirect.soap.Property;
import com.oracle.ofsc.etadirect.soap.User;
import com.oracle.ofsc.transforms.GenericActivityData;
import com.oracle.ofsc.transforms.TransportationActivityData;
import org.apache.camel.Body;
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
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Conversion Bean - Given an input of the specific type, will convert the bindy object
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
                Security.extractAuthInfo((String )exchange.getIn().getHeader("CamelHttpQuery"));

        String username = authInfo.get("user") + "@" + authInfo.get("company");
        String passwd =   authInfo.get("passwd");

        com.oracle.ofsc.etadirect.rest.InsertActivity activityIns = null;
        switch (category) {
        case "transportation":
            LOGGER.debug("Generating Activity Request From Transportation Activity");
            activityIns = this.generateTransportationActivity(exchange.getIn().getBody(), bucketId);
            break;
        case "generic":
            LOGGER.debug("Generating Activity Request From Generic Activity");
            activityIns = this.generateGenericActivity(exchange.getIn().getBody(), bucketId);
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
     * Mapping between the inbound parsed Bindy object, for Generic activity types.
     * @param inObject
     * @param bucketId
     * @return
     */
    private com.oracle.ofsc.etadirect.rest.InsertActivity generateGenericActivity(Object inObject, String bucketId) {
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

        activityIns.setTimeSlot(activityData.getTimeSlot());
        activityIns.setSlaWindowStart(activityData.getActivityStartDate() + " " + activityData.getActivityStartTime());
        activityIns.setSlaWindowEnd(activityData.getActivityEndDate() + " " + activityData.getActivityEndTime());

        return activityIns;
    }
}

