package com.oracle.ofsc.etadirect.camel.beans;

import com.google.common.collect.ImmutableList;
import com.oracle.ofsc.transforms.RouteReportData;
import org.apache.camel.Exchange;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by xxx_sharma on 10/20/16.
 */
public class ArcBestBulk {
    private static final Logger LOGGER = LoggerFactory.getLogger(ArcBestBulk.class.getName());

    public void allResourcesOfStation(Exchange exchange) {
        String station = (String) exchange.getIn().getHeader("station");

        if (StringUtils.isBlank(station)) {
            LOGGER.error("No Station Information Provided In The Request");
            return;
        }
        List<String> resourceList=null;
        switch (station) {
            case "019":
                LOGGER.error("Processing Station: St Joseph, KS");
                resourceList= ImmutableList.of("STJOE20192", "STJOE20193");
            default:
                LOGGER.warn("Unsupported Station: " + station);
        }

        exchange.getIn().setBody(resourceList);
    }

    public void aggregateBulkRoutes(List<RouteReportData> data) {
        LOGGER.info("Pushing Aggregated Results");


    }
}
