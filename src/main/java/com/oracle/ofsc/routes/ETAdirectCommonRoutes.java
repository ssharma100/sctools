package com.oracle.ofsc.routes;

import com.oracle.ofsc.etadirect.camel.beans.Resource;
import org.apache.camel.builder.RouteBuilder;

/**
 * Created by ssharma on 10/20/16.
 */
public class ETAdirectCommonRoutes extends RouteBuilder {

    private static final String LOG_CLASS = "com.oracle.ofsc.routes.ETAdirectRoutes";

    @Override public void configure() throws Exception {

        from("direct://common/get/route")
                .routeId("etaDirectRouteGet")
                .bean(Resource.class, "authOnly")
                .to("log:" + LOG_CLASS + "?level=INFO")
                .to("direct://etadirectrest/getRoute");
    }
}
