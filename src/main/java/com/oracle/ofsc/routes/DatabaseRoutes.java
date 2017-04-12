package com.oracle.ofsc.routes;

import org.apache.camel.builder.RouteBuilder;

/**
 * Performs enhancement function invocations for the Database Stored Information
 */
public class DatabaseRoutes extends RouteBuilder {

    private static final String LOG_CLASS = "com.oracle.ofsc.routes.WebDatabaseRoutes";

    @Override public void configure() throws Exception {


        // Requires
        from("restlet:http://localhost:8085/sctool/v1/db/enhance/distance/{googleKey}?restletMethod=post")
                .routeId("routingDBEnhanceDistance")
                .to("log:" + LOG_CLASS + "?showAll=true&multiline=true&level=INFO")
                .to("direct://common/get/route/db")
                .to("log:" + LOG_CLASS + "?showAll=true&multiline=true&level=DEBUG");
    }
}
