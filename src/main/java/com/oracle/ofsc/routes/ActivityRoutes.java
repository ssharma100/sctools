package com.oracle.ofsc.routes;

import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;

/**
 * Created by Samir on 10/9/2016.
 */
public class ActivityRoutes extends RouteBuilder {
    private static final String LOG_CLASS = "com.oracle.ofsc.routes.ActivityRoutes";
    @Override
    public void configure() throws Exception {

        from("direct://etadirectsoap/activity")
                .to("log:" + LOG_CLASS + "?level=INFO")
                .onException(Exception.class)
                    .to("log:" + LOG_CLASS + "?showAll=true&multiline=true&level=ERROR")
                    .log(LoggingLevel.ERROR, LOG_CLASS, exceptionMessage().toString())
                    .handled(true)
                .end()

                // Send actual request to endpoint of Web Service.
                .setHeader(Exchange.HTTP_METHOD, constant(org.apache.camel.component.http4.HttpMethods.POST))
                .to("https4:api.etadirect.com/soap/activity/v3/?bridgeEndpoint=true")
                .to("log:" + LOG_CLASS + "?level=INFO");

        from ("direct://etadirectrest/activity")
                .to("log:" + LOG_CLASS + "?level=INFO")
                .onException(Exception.class)
                .to("log:" + LOG_CLASS + "?showAll=true&multiline=true&level=ERROR")
                .handled(true)
                .end()

                // Send Actual request to endpoint of Res Service (ETAdirect)
                .setHeader(Exchange.HTTP_METHOD, constant(org.apache.camel.component.http4.HttpMethods.POST))
                .setHeader("CamelHttpQuery", constant(null))

                .toD("https4:api.etadirect.com/rest/ofscCore/v1/activities/?bridgeEndpoint=true&throwExceptionOnFailure=false&authenticationPreemptive=true&authUsername=${in.header[username]}&authPassword=${in.header[passwd]}")
                .to("log:" + LOG_CLASS + "?level=DEBUG");

    }
}
