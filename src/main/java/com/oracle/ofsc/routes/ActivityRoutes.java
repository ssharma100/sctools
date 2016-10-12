package com.oracle.ofsc.routes;

import com.oracle.ofsc.etadirect.camel.beans.Activity;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;

/**
 * Created by Samir on 10/9/2016.
 */
public class ActivityRoutes extends RouteBuilder {
    private static final String LOG_CLASS = "com.oracle.ofsc.routes.ActivityRoutes";
    @Override
    public void configure() throws Exception {

        from("direct://etadirectsoap/get/activity")
                .to("log:" + LOG_CLASS + "?level=INFO")
                .onException(Exception.class)
                    .to("log:" + LOG_CLASS + "?showAll=true&multiline=true&level=ERROR")
                    .handled(true)
                .end()

                // Send actual request to endpoint of Web Service.
                .setHeader(Exchange.HTTP_METHOD, constant(org.apache.camel.component.http4.HttpMethods.POST))
                .to("https4:api.etadirect.com/soap/activity/v3/?bridgeEndpoint=true")
                .to("log:" + LOG_CLASS + "?level=INFO");
    }
}
