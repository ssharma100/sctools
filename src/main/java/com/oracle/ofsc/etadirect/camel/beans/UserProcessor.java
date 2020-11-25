package com.oracle.ofsc.etadirect.camel.beans;

import org.apache.camel.Exchange;

/**
 * Processing class that takes the OFSC responses RE:users and
 * converts them to POJOs.
 *
 */
public class UserProcessor {


    /**
     * Since there is no filtering in the OFSC, we have the need to process
     * each response to inspect if we need to make a follow up "offset" request to
     * get the next batch of entries.
     * This processing extracts there results into the Exchange Properties and sets
     * up the properties in the exchange to indicate if the next offset is require.
     *
     * @param exchange
     */
    public void partialResponse(Exchange exchange) {

    }

    /**
     * Response should be a list of Users that resulted from querying all
     * Users in OFSC.
     *
     * Inactive Users will be filtered out to provide an active user list
     *
     * @param exchange
     */
    public void mapOFSCUsers(Exchange exchange) {

    }
}
