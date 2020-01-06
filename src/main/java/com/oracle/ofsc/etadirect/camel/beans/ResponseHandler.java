package com.oracle.ofsc.etadirect.camel.beans;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oracle.ofsc.etadirect.rest.Fault;
import com.oracle.ofsc.etadirect.rest.InsertActivity;
import com.oracle.ofsc.transforms.ActivityInsertedItem;
import com.oracle.ofsc.transforms.FiberActivityData;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Exchanger;

/**
 * Handles Responses Coming Back From The API Call To OFSC.
 */
public class ResponseHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(ResponseHandler.class.getName());
    private static final ObjectMapper activityResponseMapper = new ObjectMapper();

    /**
     * Handles the output formatting of the result.  Since the processing is successful
     * if we got a HTTP response (may be 200 or something else).  In either case, the
     * response came back and not a 500 or connection error (handled in the exception
     * case).
     *
     * @param exchange
     */
    public void restResponse(Exchange exchange) {
        Integer httpCode = (Integer )exchange.getIn().getHeader("CamelHttpResponseCode");
        String httpMsg = (String )exchange.getIn().getHeader("CamelHttpResponseText");
        InputStream responseIS = (InputStream ) exchange.getIn().getBody();

        FiberActivityData originalRecord = exchange.getIn().getHeader("origin_activity", FiberActivityData.class);
        // Build The Activity Summary
        ActivityInsertedItem inserted = new ActivityInsertedItem();
        inserted.setRecIndex(exchange.getProperty("CamelSplitIndex", Integer.class));
        inserted.setAddressId(originalRecord.getAddressId());
        inserted.setTicketId(originalRecord.getActivityKey());
        inserted.setAccountId(originalRecord.getAccountId());
        inserted.setActivityType(originalRecord.getActivityType());
        inserted.setHttpCode(httpCode);
        inserted.setHttpMessage(exchange.getIn().getHeader("CamelHttpResponseText", String.class));
        // Based On Response Process The Result For The Response
        try {
            switch (httpCode) {
                case 200:
                case 201:
                    LOGGER.info("Successfully Processed OFSC Request");
                    InsertActivity insertedActivity = activityResponseMapper.readValue(responseIS, InsertActivity.class);
                    inserted.setDuration(insertedActivity.getDuration());
                    inserted.setOfscId(Long.parseLong(insertedActivity.getActivityId()));
                    inserted.setResourceId(insertedActivity.getResourceId());
                    break;
                default:
                    LOGGER.warn("OFSC Response Returned Non-200 HTTP Response Code: {}", httpCode);
                    Fault faultResponse = activityResponseMapper.readValue(responseIS, Fault.class);
                    inserted.setError(faultResponse.getDetail());
                    inserted.setDuration(originalRecord.getDuration());
            }
        } catch (IOException e) {
            LOGGER.error("Failed To Marshal JSON Object: {}", e.getMessage());
        }

        exchange.getIn().setBody(inserted);
    }
}
