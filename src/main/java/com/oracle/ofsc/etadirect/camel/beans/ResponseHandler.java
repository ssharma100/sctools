package com.oracle.ofsc.etadirect.camel.beans;

import org.apache.camel.Exchange;

import java.util.concurrent.Exchanger;

/**
 * Created by xxx_sharma on 10/16/16.
 */
public class ResponseHandler {

    public void restResponse(Exchange exchange) {
        exchange.getIn().setBody(
                exchange.getOut().getBody()
        );
    }
}
