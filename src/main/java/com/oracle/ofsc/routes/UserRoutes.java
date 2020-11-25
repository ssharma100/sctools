package com.oracle.ofsc.routes;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;

public class UserRoutes extends RouteBuilder {
    @Override
    public void configure() throws Exception {

        // Actual RESTful Call For A Specific Resource - Will Return OFSC Native API Json (Non-Translated)
        from("direct://etadirectrest/user/get")
                // Send actual request to endpoint of RESTful Service.
                .setHeader(Exchange.HTTP_METHOD, constant(org.apache.camel.component.http4.HttpMethods.GET))
                .setHeader("CamelHttpQuery", constant(null))
                .toD("https4:api.etadirect.com/rest/ofscCore/v1/users"
                        + "?bridgeEndpoint=true&throwExceptionOnFailure=false&authenticationPreemptive=true"
                        + "&authUsername=${in.header[username]}&authPassword=${in.header[passwd]}");
    }
}
