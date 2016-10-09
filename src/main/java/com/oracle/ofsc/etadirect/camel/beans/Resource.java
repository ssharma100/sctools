package com.oracle.ofsc.etadirect.camel.beans;

import com.oracle.ofsc.etadirect.soap.GetResource;
import com.oracle.ofsc.etadirect.soap.User;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provides mapping of the current request to the required XML that should be
 * sent as "SOAP" request to server.
 */
public class Resource {
    private static final Logger LOGGER = LoggerFactory.getLogger(Resource.class.getName());
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
        exchange.getIn().setBody(getResource);
    }
}
