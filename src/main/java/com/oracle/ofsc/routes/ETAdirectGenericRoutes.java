package com.oracle.ofsc.routes;

import com.oracle.ofsc.etadirect.camel.beans.Activity;
import com.oracle.ofsc.etadirect.camel.beans.Resource;
import com.oracle.ofsc.etadirect.camel.beans.ResponseHandler;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.bindy.csv.BindyCsvDataFormat;
import org.apache.camel.spi.DataFormat;

/**
 * Created by ssharma on 3/7/17.
 */
public class ETAdirectGenericRoutes extends RouteBuilder {
    private static final String LOG_CLASS = "com.oracle.ofsc.routes.ETAdirectGenericRoutes";
    private DataFormat resourceInsert = new BindyCsvDataFormat(com.oracle.ofsc.transforms.GenericResourceData.class);
    private DataFormat activityInsert = new BindyCsvDataFormat(com.oracle.ofsc.transforms.GenericActivityData.class);

    @Override
    public void configure() throws Exception {

        /* Populates The Body With The SOAP Call Needed To Call The Server */

        from("direct://generic/resource/get")
                .routeId("etaDirectGenResourceGet")
                .to("log:" + LOG_CLASS + "?level=INFO")
                .bean(Resource.class, "mapToGetRequest")
                .to("direct://etadirectsoap/resource");

        from("direct://generic/resource/insert")
                .routeId("etaDirectGenResourceInsert")
                .unmarshal(resourceInsert)
                .split(body())
                .to("log:" + LOG_CLASS + "?level=INFO")
                .setHeader("resource_category", constant("generic"))
                .bean(Resource.class, "mapToInsertResource")
                .to("direct://etadirectsoap/resource");

        from("direct://generic/user/insert")
                .routeId("etaDirectGenUserInsert")
                .unmarshal(resourceInsert)
                .split(body())
                .to("log:" + LOG_CLASS + "?level=INFO")
                .setHeader("resource_category", constant("generic"))
                .bean(Resource.class, "mapToInsertUser")
                .to("direct://etadirectsoap/resource");

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

        from("direct://generic/activity/search/appNumber")
                .routeId("etaSearchActivity")
                .bean(Activity.class, "authOnly")
                .to("direct://etadirectrest/activity/search/apptNumber");
    }

}
