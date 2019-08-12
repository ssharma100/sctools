package com.oracle.ofsc.routes;


import com.oracle.ofsc.etadirect.camel.beans.Resource;
import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;

/**
 * Provides SOAP Models and routes for executing calls to the configured ETAdirect
 * Server.
 *
 */
public class ResourceRoutes extends RouteBuilder {

    private static final String LOG_CLASS = "com.oracle.ofsc.routes.ResourceRoutes";

    @Override
    public void configure() {

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

        // Actual RESTful Call For A Specific Resource - Will Return OFSC Native API Json (Non-Translated)
        from("direct://etadirectrest/resource/get")
                // Send actual request to endpoint of RESTful Service.
                .setHeader(Exchange.HTTP_METHOD, constant(org.apache.camel.component.http4.HttpMethods.GET))
                .setHeader("CamelHttpQuery", constant(null))
                .toD("https4:api.etadirect.com/rest/ofscCore/v1/resources/${in.header[id]}"
                        + "?bridgeEndpoint=true&throwExceptionOnFailure=false&authenticationPreemptive=true"
                        + "&authUsername=${in.header[username]}&authPassword=${in.header[passwd]}");

        // Actual RESTful Call For All Children Resources At A Level Of The Resource Tree
        from("direct://etadirectrest/resources/get")
                // Send actual request to endpoint of RESTful Service.
                .setHeader(Exchange.HTTP_METHOD, constant(org.apache.camel.component.http4.HttpMethods.GET))
                .setHeader("CamelHttpQuery", constant(null))
                .toD("https4:api.etadirect.com/rest/ofscCore/v1/resources/${in.header[id]}/children"
                        + "?bridgeEndpoint=true&throwExceptionOnFailure=false&authenticationPreemptive=true"
                        + "&authUsername=${in.header[username]}&authPassword=${in.header[passwd]}");

        // Actual RESTful Call For The PUT (Creation Or Update)
        from("direct://etadirectrest/resources/put")
                .setHeader(Exchange.HTTP_METHOD, constant(org.apache.camel.component.http4.HttpMethods.PUT))
                .setHeader("CamelHttpQuery", constant(null))
                .toD("https4:api.etadirect.com/rest/ofscCore/v1/resources/${in.header[id]}"
                        + "?bridgeEndpoint=true&throwExceptionOnFailure=false&authenticationPreemptive=true"
                        + "&authUsername=${in.header[username]}&authPassword=${in.header[passwd]}");

        from("direct://etadirectrest/resource/update")
                .to("log:" + LOG_CLASS + "?level=INFO")
                .onException(Exception.class)
                    .to("log:" + LOG_CLASS + "?showAll=true&multiline=true&level=ERROR")
                    .log(LoggingLevel.ERROR, LOG_CLASS, exceptionMessage().toString())
                    .handled(true)
                .end()

                // Send actual request to endpoint of Web Service.
                .setHeader(Exchange.HTTP_METHOD, constant(org.apache.camel.component.http4.HttpMethods.PATCH))
                .setHeader("CamelHttpQuery", constant(null))

                .toD("https4:api.etadirect.com/rest/ofscCore/v1/resources/${in.header[id]}"
                        + "?bridgeEndpoint=true&throwExceptionOnFailure=false&authenticationPreemptive=true"
                        + "&authUsername=${in.header[username]}&authPassword=${in.header[passwd]}");

        from("direct://etadirectrest/resource/schedule")
                .to("log:" + LOG_CLASS + "?level=INFO")
                .onException(Exception.class)
                .to("log:" + LOG_CLASS + "?showAll=true&multiline=true&level=ERROR")
                .log(LoggingLevel.ERROR, LOG_CLASS, exceptionMessage().toString())
                .handled(true)
                .end()

                        // Send actual request to endpoint of Web Service.
                .setHeader(Exchange.HTTP_METHOD, constant(org.apache.camel.component.http4.HttpMethods.POST))
                .setHeader("CamelHttpQuery", constant(null))

                .toD("https4:api.etadirect.com/rest/ofscCore/v1/resources/${in.header[id]}/workSchedules"
                        + "?bridgeEndpoint=true&throwExceptionOnFailure=false&authenticationPreemptive=true"
                        + "&authUsername=${in.header[username]}&authPassword=${in.header[passwd]}");

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

                .toD("https4:api.etadirect.com/rest/ofscCore/v1/resources/${in.header[id]}/routes/${in.header[routeDay]}"
                        + "?bridgeEndpoint=true&throwExceptionOnFailure=false&authenticationPreemptive=true"
                        + "&authUsername=${in.header[username]}&authPassword=${in.header[passwd]}")
                .to("log:" + LOG_CLASS + "?level=INFO");

        from("direct://etadirectrest/getResourceChildren")
                .to("log:" + LOG_CLASS + "?level=INFO")
                .onException(Exception.class)
                    .to("log:" + LOG_CLASS + "?showAll=true&multiline=true&level=ERROR")
                    .log(LoggingLevel.ERROR, LOG_CLASS, exceptionMessage().toString())
                    .handled(true)
                .end()

                // Send actual request to endpoint of Web Service.
                .setHeader(Exchange.HTTP_METHOD, constant(org.apache.camel.component.http4.HttpMethods.GET))
                .setHeader("CamelHttpQuery", constant(null))

                .toD("https4:api.etadirect.com/rest/ofscCore/v1/resources/${in.header[root]}/children/?offset=0&limit=800?"
                        + "?bridgeEndpoint=true&throwExceptionOnFailure=false&authenticationPreemptive=true"
                        + "&authUsername=${in.header[username]}&authPassword=${in.header[passwd]}")
                .to("log:" + LOG_CLASS + "?level=INFO");

    }
}


