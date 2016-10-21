package com.oracle.ofsc.routes;


import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.Predicate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.processor.interceptor.Tracer;

/**
 * Provides SOAP Models and routes for executing calls to the configured ETAdirect
 * Server.
 *
 */
public class ResourceRoutes extends RouteBuilder {

    private static final String LOG_CLASS = "com.oracle.ofsc.routes.ResourceRoutes";

    @Override
    public void configure() throws Exception {

        from("direct://etadirectsoap/resource")
                .to("log:" + LOG_CLASS + "?level=INFO")
                .onException(Exception.class)
                    .to("log:" + LOG_CLASS + "?showAll=true&multiline=true&level=ERROR")
                .handled(true)
                .end()
                // Send actual request to endpoint of Web Service.
                .setHeader(Exchange.HTTP_METHOD, constant(org.apache.camel.component.http4.HttpMethods.POST))
                .to("https4:api.etadirect.com/soap/resource-management/v3/?bridgeEndpoint=true&throwExceptionOnFailure=false")
                .to("log:" + LOG_CLASS + "?level=INFO");

        from("direct://etadirectrest/getRoute")
                .to("log:" + LOG_CLASS + "?level=INFO")
                .onException(Exception.class)
                    .to("log:" + LOG_CLASS + "?showAll=true&multiline=true&level=ERROR")
                    .log(LoggingLevel.ERROR, LOG_CLASS, exceptionMessage().toString())
                    .handled(true)
                .end()

                // Send actual request to endpoint of Web Service.
                .setHeader(Exchange.HTTP_METHOD, constant(org.apache.camel.component.http4.HttpMethods.GET))
                .setHeader("CamelHttpQuery", constant(null))

                .toD("https4:api.etadirect.com/rest/ofscCore/v1/resources/${in.header[id]}/routes/${in.header[routeDay]}?bridgeEndpoint=true&throwExceptionOnFailure=false&authenticationPreemptive=true&authUsername=${in.header[username]}&authPassword=${in.header[passwd]}")
                .to("log:" + LOG_CLASS + "?level=INFO");

    }
}


