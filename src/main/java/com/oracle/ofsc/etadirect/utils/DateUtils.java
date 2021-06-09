package com.oracle.ofsc.etadirect.utils;

import org.apache.camel.Exchange;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.TimeZone;

public class DateUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(DateUtils.class.getName());
    /**
     * Small utility to set the date for today + 31 days
     * @param exchange
     */
    public void getCentralDateForward30Day(Exchange exchange) {
        DateTime rightNow = new DateTime(DateTimeZone.forTimeZone(TimeZone.getTimeZone("America/Chicago")));
        DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd");

        DateTimeFormatter dtfOut = DateTimeFormat.forPattern("MM/dd/yyyy");
        exchange.setProperty("FROMDATE", dtf.print(rightNow));
        exchange.setProperty("TODATE", dtf.print(rightNow.plusDays(30)));
        LOGGER.debug("Date From: {} To: {}", exchange.getProperty("FROMDATE"), exchange.getProperty("TODATE"));
    }
}
