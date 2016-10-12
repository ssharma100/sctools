package com.oracle.ofsc.etadirect.camel.beans;

import com.oracle.ofsc.etadirect.soap.GetActivity;
import com.oracle.ofsc.etadirect.soap.GetResource;
import com.oracle.ofsc.etadirect.soap.User;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;

/**
 * Provides mapping of the current request to the required XML that should be
 * sent as "SOAP" request to server.
 */
public class Resource {
    private static final Logger LOGGER = LoggerFactory.getLogger(Resource.class.getName());
    private static final String SOAP_WRAPPER_HEADER =
            "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:urn=\"urn:toatech:ResourceManagement:1.0\">\n" +
                    "   <soapenv:Header/>\n" +
                    "   <soapenv:Body>";

    private static final String SOAP_WRAPPER_FOOTER =
            "   </soapenv:Body>\n" +
                    "</soapenv:Envelope>";

    // Used for internal testing
    private static final String USER = "soap";
    private static final String COMPANY = "sunrise3166.demo";
    private static final String PASSWD = "R2OGQreIZp";

    private static final boolean USE_MD5 = true;
    /**
     * Generates body for resource "get" request
     * @param exchange
     */
    public void mapToGetRequest (Exchange exchange) {
        String externalId = (String )exchange.getIn().getHeader("externalId");
        LOGGER.info("Generate Body For ResourceID: {}", externalId);
        // TODO: The request should have the information for the request, however, this is hardcoded for now:
        User userBlock = Security.generateUserAuth(COMPANY, USER, PASSWD, USE_MD5);
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
}
