package com.oracle.ofsc.etadirect.camel.beans;

import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by ssharma on 4/15/17.
 */
public class DebugOnly  {

    private static final Logger LOGGER = LoggerFactory.getLogger(DebugOnly.class);

    public void checkStatus (Exchange exchange) {
        LOGGER.info("Starting Debug");

    }
}
