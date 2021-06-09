package com.oracle.ofsc.routes;

import com.oracle.ofsc.etadirect.camel.beans.Activity;
import org.apache.camel.builder.RouteBuilder;

public class FiberRoutes extends RouteBuilder {
    @Override
    public void configure() throws Exception {

        from("direct://fiber/bucket/clear/mci")
                .routeId("gf-mci-bucketclear")
                .bean(Activity.class, "authOnly")
                .to("direct://etadirectrest/activity/bucket")
                .end();
    }
}
