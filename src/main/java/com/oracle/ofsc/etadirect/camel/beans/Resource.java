package com.oracle.ofsc.etadirect.camel.beans;

import com.oracle.ofsc.etadirect.soap.*;
import com.oracle.ofsc.transforms.TransportResourceData;
import org.apache.camel.Exchange;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Provides mapping of the current request to the required XML that should be
 * sent as "SOAP" request to server.
 */
@SuppressWarnings("unused")
public class Resource {
    private static final Logger LOGGER = LoggerFactory.getLogger(Resource.class.getName());
    private static final String SOAP_WRAPPER_HEADER =
            "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:urn=\"urn:toatech:ResourceManagement:1.0\">\n" +
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
        String externalId = (String )exchange.getIn().getHeader("id");

        LOGGER.info("Generate Body For ResourceID: {}", externalId);
        // TODO: The request should have the information for the request, however, this is hardcoded for now:
        User userBlock =
                Security.generateUserAuth((String )exchange.getIn().getHeader("CamelHttpQuery"), !USE_MD5);

        GetResource getResource = new GetResource();
        getResource.setUser(userBlock);
        getResource.setId(externalId);

        String body = null;
        try {
            JAXBContext context = JAXBContext.newInstance(GetResource.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
            StringWriter sw = new StringWriter();
            marshaller.marshal(getResource, sw);
            body = sw.toString();
            LOGGER.info("Generated Body: \n {}", body);
        }catch (JAXBException e) {
            LOGGER.error("Failed To Marshal Object: {}", e.getMessage());
        }
        StringBuffer sb = new StringBuffer();
        sb.append(SOAP_WRAPPER_HEADER).append(body).append(SOAP_WRAPPER_FOOTER);
        exchange.getIn().setBody(sb.toString());
    }

    /**
     * Generates the request body and complete SOAP request for a resource creation
     *
     */
    public void mapToInsertResource (Exchange exchange) {
        String id = (String )exchange.getIn().getHeader("id");
        LOGGER.info("Generate Insert Resource Body For ResourceID: {}", id);
        // TODO: The request should have the information for the request, however, this is hardcoded for now:
        User userBlock =
                Security.generateUserAuth((String )exchange.getIn().getHeader("CamelHttpQuery"), !USE_MD5);
        TransportResourceData td = (TransportResourceData )exchange.getIn().getBody();

        InsertResource insertResource = new InsertResource();
        insertResource.setUser(userBlock);
        insertResource.setId(td.getName());


        // Mandatory Elements
        ArrayList<Property> properties = new ArrayList<>(10);
        insertResource.setProperties(properties);
        properties.add(new Property("status", "active"));
        properties.add(new Property("parent_id", id));
        properties.add(new Property("name", td.getName()));
        properties.add(new Property("language", "en"));
        properties.add(new Property("time_zone", td.getTimezone()));
        properties.add(new Property("weight_cap", td.getWeight()));
        properties.add(new Property("cubic_cap", td.getCubeCap()));
        // Overridden Later If This Is A Lift Gate Truck
        properties.add(new Property("type", "PR"));

        // Look For Any Work Skills
        if (StringUtils.isNotBlank(td.getLiftGate()) && td.getLiftGate().equalsIgnoreCase("y")) {
            WorkSkills workSkills = new WorkSkills();
            workSkills.setWorkskillGroup("TruckingGroup");
            ArrayList<WorkSkill> workskill = new ArrayList<>(5);
            workskill.add(new WorkSkill("LVL2", "50"));
            workSkills.setWorkskill(workskill);
            insertResource.setWorkskills(workSkills);
            properties.add(new Property("type", "LGT"));
        }

        String soapBody = null;
        try {
            JAXBContext context = JAXBContext.newInstance(InsertResource.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
            StringWriter sw = new StringWriter();
            marshaller.marshal(insertResource, sw);
            soapBody = sw.toString();
        }catch (JAXBException e) {
            LOGGER.error("Failed To Marshal Object: {}", e.getMessage());
        }

        StringBuffer sb = new StringBuffer();
        sb.append(SOAP_WRAPPER_HEADER).append(soapBody).append(SOAP_WRAPPER_FOOTER);
        exchange.getIn().setBody(sb.toString());
    }
}
