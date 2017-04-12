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
                break;
            case "189":
                LOGGER.error("Processing Station: Bockton, MA");
                resourceList= ImmutableList.of(
                        "BOCK8453", "BOCK8454", "BOCK8455", "BOCK8456", "BOCK8457", "BOCK8458",
                        "BOCK8459", "BOCK8460", "BOCK8461", "BOCK8462", "BOCK8463", "BOCK8464",
                        "BOCK8465", "BOCK8466", "BOCK8467", "BOCK8468", "BOCK8469", "BOCK8470",
                        "BOCK8471", "BOCK8472", "BOCK8473");
                break;

            case "063":
                LOGGER.error("Processing Station: Columbus, OH");
                resourceList= ImmutableList.of("COLO38271", "COLO38272", "COLO38273", "COLO38274", "COLO38275", "COLO38276",
                        "COLO38277", "COLO38278", "COLO38279", "COLO38280", "COLO38281", "COLO38282", "COLO38283", "COLO38284",
                        "COLO38285", "COLO38286", "COLO38287", "COLO38289", "COLO38290", "COLO38291", "COLO38292", "COLO38293",
                        "COLO38294");
                break;

            case "020":
                LOGGER.error("Processing Station: Erie, PA");
                resourceList= ImmutableList.of(
                        "ERPA2930", "ERPA2931", "ERPA2932", "ERPA2933",
                        "ERPA2934", "ERPA2935", "ERPA2936");
                break;

            case "022":
                LOGGER.error("Processing Station: Pittsburg, PA");
                resourceList= ImmutableList.of(
                        "PITT38423", "PITT38424", "PITT38425", "PITT38426",
                        "PITT38427", "PITT38428", "PITT38429", "PITT38430",
                        "PITT38431", "PITT38432", "PITT38433", "PITT38434",
                        "PITT38435", "PITT38436", "PITT38437", "PITT38438",
                        "PITT38439", "PITT38440", "PITT38441", "PITT38442");
                break;

            case "070":
                LOGGER.error("Processing Station: Irving, TX");
                resourceList= ImmutableList.of(
                        "IRVN09023", "IRVN09024", "IRVN09025", "IRVN09026", "IRVN09027", "IRVN09028", "IRVN09029",
                        "IRVN09030", "IRVN09031", "IRVN09032", "IRVN09033", "IRVN09034", "IRVN09035", "IRVN09036",
                        "IRVN09037", "IRVN09038", "IRVN09039", "IRVN09040", "IRVN09041", "IRVN09042", "IRVN09043",
                        "IRVN09044", "IRVN09045", "IRVN09046", "IRVN09047", "IRVN09048", "IRVN09049", "IRVN09050",
                        "IRVN09051", "IRVN09052", "IRVN09053", "IRVN09054", "IRVN09055", "IRVN09056", "IRVN09057",
                        "IRVN09058", "IRVN09059", "IRVN09060", "IRVN09061", "IRVN09062", "IRVN09063", "IRVN09064",
                        "IRVN09065", "IRVN09066", "IRVN09067", "IRVN09068", "IRVN09069", "IRVN09070", "IRVN09071",
                        "IRVN09072", "IRVN09073", "IRVN09074", "IRVN09075", "IRVN09076", "IRVN09077", "IRVN09078",
                        "IRVN09079", "IRVN09080", "IRVN09081", "IRVN09082", "IRVN09083", "IRVN09084", "IRVN09085",
                        "IRVN09086", "IRVN09087", "IRVN09088");
                break;


            case "489":
                LOGGER.error("Processing Station: Cayanne, WY");
                resourceList= ImmutableList.of(
                        "CAYA8112", "CAYA8113", "CAYA8114", "CAYA8115");
                break;

            default:
                LOGGER.warn("Unsupported Station: " + station);
        }

        exchange.getIn().setBody(resourceList);
    }

    public void aggregateBulkRoutes(List<RouteReportData> data) {
        LOGGER.info("Pushing Aggregated Results");


    }
}
