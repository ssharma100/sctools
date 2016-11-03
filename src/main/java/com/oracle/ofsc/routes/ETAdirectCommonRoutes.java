package com.oracle.ofsc.routes;

import com.oracle.ofsc.etadirect.camel.beans.AggregatorStrategy;
import com.oracle.ofsc.etadirect.camel.beans.ArcBestBulk;
import com.oracle.ofsc.etadirect.camel.beans.Resource;
import com.oracle.ofsc.geolocation.beans.DistanceAggregationStrategy;
import com.oracle.ofsc.geolocation.beans.Location;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.bindy.csv.BindyCsvDataFormat;
import org.apache.camel.spi.DataFormat;

import java.util.List;
import java.util.Map;

/**
 * Created by ssharma on 10/20/16.
 */
public class ETAdirectCommonRoutes extends RouteBuilder {

    private static final String LOG_CLASS = "com.oracle.ofsc.routes.ETAdirectRoutes";
    private DataFormat routeReport = new BindyCsvDataFormat(com.oracle.ofsc.transforms.RouteReportData.class);
    private DataFormat locationsList = new BindyCsvDataFormat(com.oracle.ofsc.transforms.LocationListData.class);
    @Override public void configure() throws Exception {

        from("direct://common/get/route")
                .routeId("etaDirectRouteGet")
                .bean(Resource.class, "authOnly")
                .to("log:" + LOG_CLASS + "?level=INFO")
                .to("direct://etadirectrest/getRoute")
                .bean(Resource.class, "extractRoutes")
                .marshal(routeReport);

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
                    .bean(Location.class, "loadHeaders")
                    .toD("https4:maps.googleapis.com/maps/api/distancematrix/json?bridgeEndpoint=true&throwExceptionOnFailure=false")
                    .bean(Location.class, "covertJsonToRouteReport")
                .end()
                .marshal(routeReport);

        from("direct://common/set/locations")
                .routeId("setLocations")
                .unmarshal(locationsList)
                .split(body())
                    .bean(Location.class, "loadLocation")
                    .to("direct://etadirectrest/setLocation")
                .end();
    }
}
