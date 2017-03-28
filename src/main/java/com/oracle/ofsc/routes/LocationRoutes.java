package com.oracle.ofsc.routes;

import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;

/**
 * Handles Routes for API Calls TO ETAdirect For Locations (Rest And SOAP)
 *
 */
public class LocationRoutes extends RouteBuilder {
    private static final String LOG_CLASS = "com.oracle.ofsc.routes.LocationRoutes";

    @Override
    public void configure() throws Exception {
        from("direct://etadirectrest/setLocation")
                .to("log:" + LOG_CLASS + "?level=INFO")
                .onException(Exception.class)
                .to("log:" + LOG_CLASS + "?showAll=true&multiline=true&level=ERROR")
                .log(LoggingLevel.ERROR, LOG_CLASS, exceptionMessage().toString())
                .handled(true)
                .end()

                // Send actual request to endpoint of Web Service.
                .setHeader(Exchange.HTTP_METHOD, constant(org.apache.camel.component.http4.HttpMethods.POST))
                .setHeader("CamelHttpQuery", constant(null))
                .toD("https4:api.etadirect.com/rest/ofscCore/v1/resources/${in.header[id]}/locations?bridgeEndpoint=true&throwExceptionOnFailure=false&authenticationPreemptive=true&authUsername=${in.header[username]}&authPassword=${in.header[passwd]}")
                .to("log:" + LOG_CLASS + "?level=INFO");

        from("direct://etadirectrest/getLocation")
                .to("log:" + LOG_CLASS + "?level=INFO")
                .onException(Exception.class)
                .to("log:" + LOG_CLASS + "?showAll=true&multiline=true&level=ERROR")
                .log(LoggingLevel.ERROR, LOG_CLASS, exceptionMessage().toString())
                .handled(true)
                .end()

                // Send actual request to endpoint of Web Service.
                .setHeader(Exchange.HTTP_METHOD, constant(org.apache.camel.component.http4.HttpMethods.GET))
                .setHeader("CamelHttpQuery", constant(null))
                .toD("https4:api.etadirect.com/rest/ofscCore/v1/resources/${in.header[id]}/locations?bridgeEndpoint=true&throwExceptionOnFailure=false&authenticationPreemptive=true&authUsername=${in.header[username]}&authPassword=${in.header[passwd]}")
                .to("log:" + LOG_CLASS + "?level=INFO");

        from("direct://etadirectrest/assignLocation")
                .to("log:" + LOG_CLASS + "?level=INFO")
                .onException(Exception.class)
                .to("log:" + LOG_CLASS + "?showAll=true&multiline=true&level=ERROR")
                .log(LoggingLevel.ERROR, LOG_CLASS, exceptionMessage().toString())
                .handled(true)
                .end()

                // Send actual request to endpoint of Web Service.
                .setHeader(Exchange.HTTP_METHOD, constant(org.apache.camel.component.http4.HttpMethods.PUT))
                .setHeader("CamelHttpQuery", constant(null))
                .toD("https4:api.etadirect.com/rest/ofscCore/v1/resources/${in.header[id]}/assignedLocations?bridgeEndpoint=true&throwExceptionOnFailure=false&authenticationPreemptive=true&authUsername=${in.header[username]}&authPassword=${in.header[passwd]}")
                .to("log:" + LOG_CLASS + "?level=INFO");
    }
}
