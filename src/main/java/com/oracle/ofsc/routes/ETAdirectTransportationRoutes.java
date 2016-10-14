package com.oracle.ofsc.routes;

import com.oracle.ofsc.etadirect.camel.beans.Activity;
import com.oracle.ofsc.etadirect.camel.beans.Resource;
import com.oracle.ofsc.etadirect.soap.GetResource;
import org.apache.camel.LoggingLevel;
import org.apache.camel.Predicate;
import org.apache.camel.builder.RouteBuilder;

/**
 * Created by Samir on 10/3/2016.
 */
public class ETAdirectTransportationRoutes extends RouteBuilder{
    private static final String LOG_CLASS = "com.oracle.ofsc.routes.ETAdirectRoutes";

    @Override
    public void configure() throws Exception {

        /* Populates The Body With The SOAP Call Needed To Call The Server */
        from("direct://transportation/resource/get")
                .routeId("etaDirectResourceGet")
                .to("log:" + LOG_CLASS + "?level=INFO")
                .bean(Resource.class, "mapToGetRequest")
                .to("direct://etadirectsoap/resource");

        from("direct://transportation/resource/insert")
                .routeId("etaDirectResourceInsert")
                .to("log:" + LOG_CLASS + "?level=INFO")
                .bean(Resource.class, "mapToInsertResource")
                .to("direct://etadirectsoap/resource");

        from("direct://transportation/activity/get")
                .routeId("etaDirectActivityGet")
                .to("log:" + LOG_CLASS + "?level=INFO")
                .bean(Activity.class, "mapToGetRequest")
                .to("direct://etadirectsoap/get/activity");
    }
}
