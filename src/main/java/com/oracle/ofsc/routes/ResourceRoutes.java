package com.oracle.ofsc.routes;


import org.apache.camel.builder.RouteBuilder;

/**
 * Provides SOAP Models and routes for executing calls to the configured ETAdirect
 * Server.
 *
 */
public class ResourceRoutes extends RouteBuilder {

    private static final String LOG_CLASS = "com.oracle.ofsc.routes.ResourceRoutes";


    @Override
    public void configure() throws Exception {
        from("direct://etadirectsoap/get/resource")
                .to("log:" + LOG_CLASS + "?level=INFO")
                .onException(Exception.class)
                    .to("log:" + LOG_CLASS + "?showAll=true&multiline=true&level=ERROR")
                .handled(true)
                .end()
                // Send actual request to endpoint of Web Service.
                .to("spring-ws:https://api.etadirect.com/soap/resource-management/v3")
                .to("log:" + LOG_CLASS + "?level=INFO");
    }
}


