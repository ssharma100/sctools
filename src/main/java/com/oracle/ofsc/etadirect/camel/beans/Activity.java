package com.oracle.ofsc.etadirect.camel.beans;

import com.oracle.ofsc.etadirect.soap.GetActivity;
import com.oracle.ofsc.etadirect.soap.InsertActivity;
import com.oracle.ofsc.etadirect.soap.Property;
import com.oracle.ofsc.etadirect.soap.User;
import org.apache.camel.Exchange;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.StringWriter;
import java.util.ArrayList;

/**
 * Created by Samir on 10/9/2016.
 */
public class Activity {
    private static final Logger LOGGER = LoggerFactory.getLogger(Activity.class.getName());

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
    public void mapToGetRequest (Exchange exchange) {
        String activityId = (String )exchange.getIn().getHeader("id");
        LOGGER.info("Generate Body For ActivityID: {}", activityId);
        // TODO: The request should have the information for the request, however, this is hardcoded for now:
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

    public void mapToInsertRequest (Exchange exchange) {
        DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd");
        String bucketId = (String) exchange.getIn().getHeader("id");
        LOGGER.info("Generate Body For BucketID: {}", bucketId);
        // TODO: The request should have the information for the request, however, this is hardcoded for now:
        User userBlock =
                Security.generateUserAuth((String )exchange.getIn().getHeader("CamelHttpQuery"), !USE_MD5);

        InsertActivity activityIns = new InsertActivity();
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
            JAXBContext context = JAXBContext.newInstance(InsertActivity.class);
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

}

