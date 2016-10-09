package com.oracle.ofsc.etadirect.camel.beans;

import com.oracle.ofsc.etadirect.soap.User;
import org.apache.camel.Exchange;

/**
 * Provides mapping of the current request to the required XML that should be
 * sent as "SOAP" request to server.
 */
public class Resource {
    // Used for internal testing
    private static final String USER = "soap";
    private static final String COMPANY = "sunrise3166.demo";
    private static final String PASSWD = "R2OGQreIZp";

    /**
     * Generates body for resource "get" request
     * @param exchange
     */
    public void mapToGetRequest (Exchange exchange) {

        // TODO: The request should have the information for the request, however, this is hardcoded for now:
        User userBlock = Security.generateUserAuth(COMPANY, USER, PASSWD);
    }
}
