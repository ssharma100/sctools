package com.oracle.ofsc.routes;

import org.apache.camel.builder.RouteBuilder;

/**
 * Created by Samir on 10/3/2016.
 */
public class ETAdirectRoutes extends RouteBuilder{
    private static final String LOG_CLASS = "com.oracle.ofsc.routes.ETAdirectRoutes";

    @Override
    public void configure() throws Exception {

        from("direct://activity/get/list")
                .routeId("etaDirectActivityList")
                .to("log:" + LOG_CLASS + "?level=INFO");
//        .onException(Exception.class)
//                .handled(true)
//                .unmarshal(soapDF)
//                .end()
//                .marshal(soapDF)
//                .to(WS_URI)
//                .unmarshal(soapDF);

    }
}
