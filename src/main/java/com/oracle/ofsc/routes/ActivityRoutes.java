package com.oracle.ofsc.routes;

import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.http4.HttpMethods;

/**
 * Contains routes for Activity functionality
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

        // Obtain ALL Pending Activities In A Bucket
        from ("direct://etadirectrest/activity/bucket")
                .to("log:" + LOG_CLASS + "?level=INFO")
                .onException(Exception.class)
                .to("log:" + LOG_CLASS + "?showAll=true&multiline=true&level=ERROR")
                .handled(true)
                .end()

                // Send Actual request to endpoint of Res Service (ETAdirect)
                .setHeader(Exchange.HTTP_METHOD, constant(org.apache.camel.component.http4.HttpMethods.GET))
                .setHeader("CamelHttpQuery", constant(null))
                .setHeader(Exchange.HTTP_QUERY, simple("resources=${property[BUCKET]}&includeChildren=none&fields=activityId,status,apptNumber,resourceId,activityType,workZone&includeNonScheduled=false&dateFrom=${in.header[FROMDATE]}&dateTo=${in.header[TODATE]}&q=status+%3D%3D+%27pending%27"))

                .toD("https4:api.etadirect.com/rest/ofscCore/v1/activities?bridgeEndpoint=true&throwExceptionOnFailure=false&authenticationPreemptive=true&authUsername=${in.header[username]}&authPassword=${in.header[passwd]}")
                .to("log:" + LOG_CLASS + "?level=DEBUG");

        // Obtain ALL Pending Activities In A Bucket
        from ("direct://etadirectrest/activity/move")
                .log(LoggingLevel.INFO, "Requesting Move ActivityID: ${in.header[ACTIVITYID]} Ticket: ${in.header[TICKET]} To ${in.header[TARGETBUCKET]}")
                .onException(Exception.class)
                    .log(LoggingLevel.ERROR, "FAILED To Move ActivityID: ${in.header[ACTIVITYID]} Ticket: ${in.header[TICKET]} To ${in.header[TARGETBUCKET]}")
                    .handled(true)
                .end()

                // Send Actual request to endpoint of Res Service (ETAdirect)
                .setHeader(Exchange.HTTP_METHOD, constant(org.apache.camel.component.http4.HttpMethods.POST))
                .setHeader("CamelHttpQuery", constant(null))

                .toD("https4:api.etadirect.com//rest/ofscCore/v1/activities/${in.header[ACTIVITYID]}/custom-actions/move?bridgeEndpoint=true&throwExceptionOnFailure=false&authenticationPreemptive=true&authUsername=${in.header[username]}&authPassword=${in.header[passwd]}")
                .log(LoggingLevel.INFO, "Completed Move ActivityID: ${in.header[ACTIVITYID]} Ticket: ${in.header[TICKET]} To ${in.header[TARGETBUCKET]}");

        from ("direct://etadirectrest/activity/start")
                .to("log:" + LOG_CLASS + "?level=INFO")
                .onException(Exception.class)
                .to("log:" + LOG_CLASS + "?showAll=true&multiline=true&level=ERROR")
                .handled(true)
                .end()

                // Send Actual request to endpoint of Res Service (ETAdirect)
                .setHeader(Exchange.HTTP_METHOD, constant(org.apache.camel.component.http4.HttpMethods.POST))
                .setHeader("CamelHttpQuery", constant(null))
                .log("Show ${in.header[activityId]}")
                .toD("https4:api.etadirect.com/rest/ofscCore/v1/activities/${in.header[activityId]}/custom-actions/start?bridgeEndpoint=true&throwExceptionOnFailure=false&authenticationPreemptive=true&authUsername=${in.header[username]}&authPassword=${in.header[passwd]}")
                .to("log:" + LOG_CLASS + "?level=DEBUG");

        from ("direct://etadirectrest/activity/complete")
                .to("log:" + LOG_CLASS + "?level=INFO")
                .onException(Exception.class)
                    .to("log:" + LOG_CLASS + "?showAll=true&multiline=true&level=ERROR")
                    .handled(true)
                .end()

                // Send Actual request to endpoint of Res Service (ETAdirect)
                .setHeader(Exchange.HTTP_METHOD, constant(org.apache.camel.component.http4.HttpMethods.POST))
                .setHeader("CamelHttpQuery", constant(null))
                .log("Show ${in.header[activityId]}")
                .toD("https4:api.etadirect.com/rest/ofscCore/v1/activities/${in.header[activityId]}/" +
                        "custom-actions/complete?bridgeEndpoint=true&throwExceptionOnFailure=false" +
                        "&authenticationPreemptive=true&authUsername=${in.header[username]}" +
                        "&authPassword=${in.header[passwd]}")
                .to("log:" + LOG_CLASS + "?level=DEBUG");

        from ("direct://etadirectrest/stats/work/override")
                .to("log:" + LOG_CLASS + "?level=INFO")
                .onException(Exception.class)
                    .to("log:" + LOG_CLASS + "?showAll=true&multiline=true&level=ERROR")
                    .handled(true)
                .end()

                // Send Actual request to endpoint of Res Service (ETAdirect)
                .setHeader(Exchange.HTTP_METHOD, constant(HttpMethods.PATCH))
                .setHeader("CamelHttpQuery", constant(null))
                .toD("https4:api.etadirect.com/rest/ofscStatistics/v1/activityDurationStats" +
                        "?bridgeEndpoint=true&throwExceptionOnFailure=false" +
                        "&authenticationPreemptive=true&authUsername=${in.header[username]}" +
                        "&authPassword=${in.header[passwd]}")
                .to("log:" + LOG_CLASS + "?level=DEBUG");

        from("direct://etadirectrest/activity/search/apptNumber")
                .onException(Exception.class)
                    .to("log:" + LOG_CLASS + "?showAll=true&multiline=true&level=ERROR")
                    .handled(true)
                .end()

                .setHeader(Exchange.HTTP_METHOD, constant(org.apache.camel.component.http4.HttpMethods.GET))
                .setHeader("CamelHttpQuery", constant(null))

                .toD("https4:api.etadirect.com/rest/ofscCore/v1/activities/custom-actions/search?"
                        + "bridgeEndpoint=true&throwExceptionOnFailure=false&authenticationPreemptive=true"
                        + "&authUsername=${in.header[username]}&authPassword=${in.header[passwd]}"
                        + "&searchInField=apptNumber&searchForValue=${in.header[apptNumber]}"
                        + "&dateFrom=${in.header[dateFrom]}"
                        + "&dateTo=${in.header[dateTo]}")
                .to("log:" + LOG_CLASS + "?level=DEBUG");

        from("direct://etadirectrest/assignResource")
                .onException(Exception.class)
                    .to("log:" + LOG_CLASS + "?showAll=true&multiline=true&level=ERROR")
                    .handled(true)
                .end()

                .setHeader(Exchange.HTTP_METHOD, constant(org.apache.camel.component.http4.HttpMethods.PUT))
                .setHeader("CamelHttpQuery", constant(null))

                .toD("https4:api.etadirect.com/rest/ofscCore/v1/activities/${in.header[id]}/resourcePreferences?"
                        + "bridgeEndpoint=true&throwExceptionOnFailure=false&authenticationPreemptive=true"
                        + "&authUsername=${in.header[username]}&authPassword=${in.header[passwd]}")
                .to("log:" + LOG_CLASS + "?level=DEBUG");
    }
}
