package com.oracle.ofsc.routes;

import com.oracle.ofsc.etadirect.camel.beans.*;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.bindy.csv.BindyCsvDataFormat;
import org.apache.camel.spi.DataFormat;

/**
 * Created by ssharma on 3/7/17.
 */
public class ETAdirectGenericRoutes extends RouteBuilder {
    private static final String LOG_CLASS = "com.oracle.ofsc.routes.ETAdirectGenericRoutes";
    private DataFormat resourceAndUserInsert = new BindyCsvDataFormat(com.oracle.ofsc.transforms.GenericResourceData.class);
    private DataFormat resource       = new BindyCsvDataFormat(com.oracle.ofsc.transforms.ResourceData.class);
    private DataFormat activityInsert = new BindyCsvDataFormat(com.oracle.ofsc.transforms.GenericActivityData.class);
    private DataFormat fiberActivityInsert = new BindyCsvDataFormat(com.oracle.ofsc.transforms.FiberActivityData.class);

    @Override
    public void configure() {

        /* Populates The Body With The SOAP Call Needed To Call The Server */

        // Makes the request to the RESTful Route For ETA Direct API and sends back the native response
        from("direct://generic/resource/get")
                .routeId("etaDirectGenResourceGet")
                // Extract The Headers
                .bean(Resource.class, "authOnly")
                .to("direct://etadirectrest/resource/get");

        // Makes the ETA Direct RESTful request and generates a CSV response
        from("direct://generic/resources/get")
                .routeId("etaDirectGenResourcesGet")
                // Extract The Headers
                .bean(Resource.class, "authOnly")
                .to("direct://etadirectrest/resources/get")
                .bean(Resource.class, "mapResourceListToBeanList")
                .marshal(resource);

        // Performs creation of the resource REST object from the input list items (CSV)
        from("direct://generic/resource/insert")
                .routeId("etaDirectGenResourceInsert")
                .unmarshal(resource)
                .split(body())
                    .to("log:" + LOG_CLASS + "?level=INFO")
                    .setHeader("resource_category", constant("generic"))
                    .bean(Resource.class, "authOnly")
                    .bean(Resource.class, "generateRESTfulResource")
                    .to("direct://etadirectrest/resources/put");

        from("direct://generic/user/insert")
                .routeId("etaDirectGenUserInsert")
                .unmarshal(resourceAndUserInsert)
                .split(body())
                .to("log:" + LOG_CLASS + "?level=INFO")
                .setHeader("resource_category", constant("generic"))
                .bean(Resource.class, "mapToInsertUser")
                .to("direct://etadirectsoap/resource");

        // Performs a Activity Insertion & Start/Stop Cycle For Each Activity On A Given Resource
        from("direct://generic/activity/statsbatch")
                .routeId("statsBatchLoad")
                .bean(Statistics.class, "extractActivityLoadParams")
                .split().method(Statistics.class, "splitToMessageList").stopOnException()
                    .log("Performing Iterative Processing Of Activity:  ${exchangeProperty[CamelSplitIndex]}")
                    // Generate A Start + Stop Time
                    .to("direct://etadirectrest/activity")
                    .bean(Statistics.class, "buildStartFromCreatedActivity")
                    .to("direct://etadirectrest/activity/start")
                    .bean(Statistics.class, "buildCompleteFromStartedActivity")
                    .to("direct://etadirectrest/activity/complete")
                .end();

        from("direct://generic/activity/get")
                .routeId("etaGenActivityGet")
                .to("log:" + LOG_CLASS + "?level=INFO")
                .bean(Activity.class, "mapToGetRequest")
                .to("direct://etadirectsoap/activity");

        from("direct://generic/activity/insert")
                .routeId("etaGenActivityInsert")
                .unmarshal(activityInsert)
                .split(body())
                .to("log:" + LOG_CLASS + "?level=DEBUG")
                .setHeader("activity_category", constant("generic"))
                .bean(Activity.class, "mapToInsertRestRequest")
                .to("direct://etadirectrest/activity")
                .bean(ResponseHandler.class, "restResponse");

        from("direct://fiber/activity/insert")
                .routeId("etaFiberActivityInsert")
                .unmarshal(fiberActivityInsert)
                .split(body(), new ActivityInsertAggregator())
                .to("log:" + LOG_CLASS + "?level=DEBUG")
                .setHeader("activity_category", constant("fiber"))
                .setHeader("origin_activity", simple("${body}"))    // Store For Summary Use
                .bean(Activity.class, "mapToInsertRestRequest")
                .to("direct://etadirectrest/activity")
                .bean(ResponseHandler.class, "restResponse");

        from("direct://generic/activity/search/appNumber")
                .routeId("etaSearchActivity")
                .bean(Activity.class, "authOnly")
                .to("direct://etadirectrest/activity/search/apptNumber");

        from("direct://common/get/patchAssignedResource")
                .routeId("patchAssignedResources")
                .unmarshal(activityInsert)
                .split(body())
                    .bean(Activity.class, "mapToWOSearch")
                    .to("direct://etadirectrest/activity/search/apptNumber")
                    .bean(Activity.class, "assignResourceFromActivityResponse")
                    .to("direct://etadirectrest/assignResource");

    }

}
