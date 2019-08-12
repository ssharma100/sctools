package com.oracle.ofsc.routes;

import com.oracle.ofsc.etadirect.camel.beans.Activity;
import com.oracle.ofsc.etadirect.camel.beans.AcostaFunctions;
import com.oracle.ofsc.etadirect.camel.beans.AggregatorStrategy;
import com.oracle.ofsc.etadirect.camel.beans.ArcBestBulk;
import com.oracle.ofsc.etadirect.camel.beans.Resource;
import com.oracle.ofsc.geolocation.beans.DistanceAggregationStrategy;
import com.oracle.ofsc.geolocation.beans.Location;
import com.oracle.ofsc.geolocation.beans.ResourceLocationDataAggregationStrategy;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.camel.dataformat.bindy.csv.BindyCsvDataFormat;
import org.apache.camel.spi.DataFormat;


public class ETAdirectCommonRoutes extends RouteBuilder {

    private static final String LOG_CLASS = "com.oracle.ofsc.routes.ETAdirectRoutes";
    private DataFormat routeReport = new BindyCsvDataFormat(com.oracle.ofsc.transforms.RouteReportData.class);
    private DataFormat locationsList = new BindyCsvDataFormat(com.oracle.ofsc.transforms.LocationListData.class);
    private DataFormat resourceLocation = new BindyCsvDataFormat(com.oracle.ofsc.transforms.ResourceLocationData.class);
    private DataFormat resourceAssignment = new BindyCsvDataFormat(com.oracle.ofsc.transforms.ResourceAssignment.class);

    private JacksonDataFormat jacksonDataFormat = new JacksonDataFormat();

    @Override public void configure() throws Exception {

        from("direct://common/get/route")
                .routeId("etaDirectRouteGet")
                .bean(Resource.class, "authOnly")
                .to("direct://etadirectrest/getRoute")
                .bean(Resource.class, "extractRoutes")
                .marshal(routeReport);

        // Queries For Route And Loads In The DB Table
        from("direct://common/get/route/route_plan/db_store")
                .routeId("etaDirectRouteGetForRoutePlan")
                .bean(Resource.class, "authOnly")
                .setHeader("CamelJacksonUnmarshalType", constant("com.oracle.ofsc.etadirect.rest.RouteList"))
                .to("direct://etadirectrest/getRoute")
                .unmarshal(jacksonDataFormat)
                .setProperty("routeListJson", simple("${in.body}"))
                .bean(AcostaFunctions.class, "extractRoutesToSQL")
                .to("jdbc:acostaDS?useHeadersAsParameters=true&outputType=StreamList");

        // Configured specifically for the bulk extraction of routes for the given day
        from("direct://common/get/route/bulk")
                .routeId("etaDirectBulkRouteGet")
                .bean(ArcBestBulk.class, "allResourcesOfStation")
                .bean(Resource.class, "authOnly")
                .split(body(), new AggregatorStrategy())
                    .setHeader("id", simple("${in.body}"))
                    .to("direct://etadirectrest/getRoute")
                    .bean(Resource.class, "extractRoutes")
                .end()
                .bean(ArcBestBulk.class, "aggregateBulkRoutes")
                .marshal(routeReport);

        from("direct://common/get/route/enhance/distance")
                .routeId("googleDistanceAPI")
                .unmarshal(routeReport)
                .bean(Location.class, "extractOriginDestination")
                .split(body(), new DistanceAggregationStrategy())
                    .setHeader(Exchange.HTTP_METHOD, constant(org.apache.camel.component.http4.HttpMethods.GET))
                    .bean(Location.class, "loadGoogleHeaders")
                    .toD("https4:maps.googleapis.com/maps/api/distancematrix/json?bridgeEndpoint=true&throwExceptionOnFailure=false")
                    .bean(Location.class, "covertJsonToRouteReport")
                .end()
                .marshal(routeReport);

        from("direct://common/set/locations")
                .routeId("setLocations")
                .unmarshal(locationsList)
                .split(body())
                    .bean(Location.class, "loadInsertLocation")
                    .to("direct://etadirectrest/setLocation")
                .end();

        from("direct://common/get/locations")
                .routeId("getLocations")
                .bean(Resource.class, "authOnly")
                .to("direct://etadirectrest/getLocation");

        // Performs Assignment Fetch
        from("direct://common/get/assignedLocations")
                .routeId("getAssignedLocations")
                .unmarshal(resourceLocation)
                .bean(Resource.class, "authOnly")
                .setHeader("CamelJacksonUnmarshalType", constant("com.oracle.ofsc.etadirect.rest.ResourceLocationResponse"))
                .split(body(), new ResourceLocationDataAggregationStrategy())
                    .bean(Location.class, "extractResource")
                    .to("direct://etadirectrest/getLocation")
                    .unmarshal(jacksonDataFormat)
                .end()
                .bean(Location.class, "buildResourceLocationData")
                .marshal(resourceLocation);


        from("direct://common/get/assignLocations")
                .routeId("applyLocationAssignment")
                .unmarshal(resourceLocation)
                .setHeader("CamelJacksonUnmarshalType", constant("com.oracle.ofsc.etadirect.rest.ResourceLocationResponse"))
                .split(body())
                    .bean(Location.class, "associateResourceLocations")
                    .to("direct://etadirectrest/assignLocation")
                    .unmarshal(jacksonDataFormat)
                .end();

        from("direct://common/set/assignResource")
                .routeId("assignResToActivity")
                .unmarshal(resourceAssignment)
                .split(body())
                    .bean(Activity.class, "assignResource")
                    .to("direct://etadirectrest/assignResource")
                .end();

        /**
         * Makes a request to get all children resources under the given root.
         * Note that this is broken and may not work.  Also limited to 100 responses.
         */
        from("direct://common/get/resource/children")
                .routeId("getResourceChildren")
                .to("direct://etadirectrest/getResourceChildren");

    }
}
