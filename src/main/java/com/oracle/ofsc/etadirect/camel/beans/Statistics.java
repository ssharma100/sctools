package com.oracle.ofsc.etadirect.camel.beans;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oracle.ofsc.etadirect.rest.InsertActivity;
import com.oracle.ofsc.etadirect.rest.WorkStatsDefinition;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.converter.stream.InputStreamCache;
import org.apache.camel.impl.DefaultMessage;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Camel Bean Support Class
 * Methods in this class support the Stats routes and endpoint functionality
 *
 */
public class Statistics {

    private static final Logger LOGGER = LoggerFactory.getLogger(Statistics.class.getName());
    private static final ObjectMapper activityMapper = new ObjectMapper();

    /**
     * The Status load of activities should have the following header
     * id: the resourceId of a "activated" resource
     * firstAptTime: the tme at which to start the appoints (first)
     * targetDate: the number of appointments to create
     * numberOfApt: the number of appointments to create
     * duration: minute of duration each appointment should be
     * activityType:
     * customerType:
     * services:
     * tv:
     * phoneCount:
     * repairType:
     * address:
     * city:
     * state:
     * zip:
     * @param exchange
     */
    public void extractStatsParams(Exchange exchange) {

        LOGGER.info("Generate Stats Loading Headers");
        HashMap<String, String> params = Security.extractURLQueryParameters((String) exchange.getIn().getHeader("CamelHttpQuery"));

        String username = params.get("user") + "@" + params.get("company");
        String passwd = params.get("passwd");

        // Set Values For HTTP4:
        exchange.getIn().setHeader("username", username);
        exchange.getIn().setHeader("passwd", passwd);

        LOGGER.info("Authentication Completed For User: " + exchange.getIn().getHeader("username"));
        params.keySet().forEach(param -> exchange.getIn().setHeader(param, params.get(param)));
        LOGGER.info("Attribute Load Completed For Statistics Load For Target ID {}: {}", exchange.getIn().getHeader("id"), params);
    }

    /**
     * Generates a list of headers with the required offsets and staging for iterative processing
     * by the route.
     *
     * @return
     */
    public List<Message> splitToMessageList(Exchange exchange) {
        List<Message> answer = new ArrayList<>();
        // Splitting Logic Has To Set Up The Number Of Object + Adjustments To Headers As Required
        int activityCount = Integer.parseInt((String )exchange.getIn().getHeader("numberOfApt"));
        for (int index=0; index < activityCount; index++) {
            DefaultMessage message = new DefaultMessage();
            message.setHeaders(exchange.getIn().getHeaders());

            try {
                // Create A Body For The Activity Creation
                com.oracle.ofsc.etadirect.rest.InsertActivity activity = bootStrapDefaults(exchange.getIn().getHeaders());
                message.setBody(activityMapper.writeValueAsString(activity));
            } catch (JsonProcessingException e) {
                LOGGER.error("Failed To Marshal JSON Object: {}", e.getMessage());
            } catch (UnsupportedEncodingException e) {
                LOGGER.error("Failed To Generate JSON Object: {}", e.getMessage());
            }

            answer.add(message);
        }
        return answer;
    }

    /**
     *
     * @param exchange
     */
    public void buildStartFromCreatedActivity(Exchange exchange) {
        Map<String, Object> headers = exchange.getIn().getHeaders();
        try {
            com.oracle.ofsc.etadirect.rest.InsertActivity insertActivity =
                    activityMapper.readValue((InputStream)exchange.getIn().getBody(), com.oracle.ofsc.etadirect.rest.InsertActivity.class);
            exchange.getIn().setHeader("activityId", insertActivity.getActivityId());
            int splitIndex = (int )exchange.getProperty("CamelSplitIndex");
            LOGGER.info("Generating Start Activity For {} For Increment {}", insertActivity.getActivityId(), splitIndex);
            // Build The Request Based On The Date/Time
            // Generate Timestamp OffSet From The First Time
            String time = (String )headers.get("firstAptTime");
            String[] timeComponents = StringUtils.split(time, ":");
            DateTime dateTime = new DateTime()
                    .withHourOfDay(Integer.parseInt(timeComponents[0]))
                    .withMinuteOfHour(Integer.parseInt(timeComponents[1]));
            int duration = Integer.parseInt((String )headers.get("duration"));
            int minuteIncrement = duration * splitIndex;
            dateTime = dateTime.plusMinutes(minuteIncrement);
            String startBody = String.format("{ \"time\": \"%s %02d:%02d\" }",
                    headers.get("targetDate"), dateTime.getHourOfDay(), dateTime.getMinuteOfHour());
            DefaultMessage message = new DefaultMessage();
            message.setHeaders(exchange.getIn().getHeaders());
            message.setBody(startBody);
            exchange.setOut(message);

        } catch (JsonProcessingException e) {
            LOGGER.error("Failed To Marshal JSON Object: {}", e.getMessage());
        } catch (IOException e) {
            LOGGER.error("Failed To Read I/O Stream: {}", e.getMessage());
        }
        exchange.getIn().setBody(exchange.getOut().getBody());
    }

    public void buildCompleteFromStartedActivity(Exchange exchange) {
        Map<String, Object> headers = exchange.getIn().getHeaders();
        int splitIndex = (int )exchange.getProperty("CamelSplitIndex");
        LOGGER.info("Generating Start Activity For {} For Increment {}", exchange.getIn().getHeader("activityId"), splitIndex);

        // Build The Request Based On The Date/Time
        // Generate Timestamp OffSet From The First Time
        String time = (String )headers.get("firstAptTime");
        String[] timeComponents = StringUtils.split(time, ":");
        DateTime dateTime = new DateTime()
                .withHourOfDay(Integer.parseInt(timeComponents[0]))
                .withMinuteOfHour(Integer.parseInt(timeComponents[1]));
        int duration = Integer.parseInt((String )headers.get("duration"));
        int minuteIncrement = duration * (splitIndex + 1);
        dateTime = dateTime.plusMinutes(minuteIncrement);
        String startBody = String.format("{ \"time\": \"%s %02d:%02d\" }",
                headers.get("targetDate"), dateTime.getHourOfDay(), dateTime.getMinuteOfHour());
        DefaultMessage message = new DefaultMessage();
        message.setHeaders(exchange.getIn().getHeaders());
        message.setBody(startBody);
        exchange.setOut(message);
    }

    /**
     * Given the Exchange object of a Json Stats array, the given resourceId will be used
     * to update the given id, and generate the override Json body.
     *
     * @param exchange
     */
    public void buildStatsModel(Exchange exchange) throws IOException {
        String overrideResourceId = (String )exchange.getIn().getHeader("resourceId");
        InputStreamCache overrideBody = (InputStreamCache )exchange.getIn().getBody();
        if (null == overrideBody) {
            exchange.getIn().setBody("No JSon Override Body Provided");
            return;
        }
        WorkStatsDefinition wsd = activityMapper.readValue(overrideBody, WorkStatsDefinition.class);

        LOGGER.info("Overriding Resources To: {}", overrideResourceId);
        // Perform Override Of The Resources Mentioned:
        wsd.getItems().stream()
                .forEach(item -> item.setResourceId(overrideResourceId));

        // Convert Back To String
        exchange.getIn().setBody(activityMapper.writeValueAsString(wsd));
        LOGGER.debug("Overrides Body: {}", exchange.getIn().getBody());
    }

    private InsertActivity bootStrapDefaults(Map<String, Object> headers) throws UnsupportedEncodingException {
        com.oracle.ofsc.etadirect.rest.InsertActivity activity = new com.oracle.ofsc.etadirect.rest.InsertActivity();
        activity.setResourceId((String )headers.get("id"));
        activity.setActivityType((String )headers.get("activityType"));
        activity.setDate((String )headers.get("targetDate"));
        activity.setApptNumber("Stats-" + new Date().getTime());
        activity.setDuration(Integer.parseInt((String )headers.get("duration")));
        activity.setgCustomerType((String )headers.get("customerType"));
        activity.setgServices((String )headers.get("services"));
        if (null != headers.get("tv")) {
            activity.setgTvCount(Integer.parseInt((String) headers.get("tv")));
        }
        activity.setgRepairType(URLDecoder.decode((String )headers.get("repairType"),Charset.defaultCharset().name()));
        activity.setgPhoneCount((String )headers.get("phoneCount"));
        activity.setTimeSlot("1630");
        activity.setCustomerName("Stats Bulkload");
        activity.setStreetAddress(URLDecoder.decode((String)headers.get("address"), Charset.defaultCharset().name()));
        activity.setCity(URLDecoder.decode((String)headers.get("city"), Charset.defaultCharset().name()));
        activity.setStateProvince((URLDecoder.decode((String)headers.get("state"), Charset.defaultCharset().name())));
        activity.setPostalCode((String )headers.get("zip"));
        activity.setCustomerNumber("3505148091");
        return activity;
    }
}
