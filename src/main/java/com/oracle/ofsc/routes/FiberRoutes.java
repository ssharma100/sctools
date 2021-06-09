package com.oracle.ofsc.routes;

import com.oracle.ofsc.etadirect.camel.beans.Activity;
import com.oracle.ofsc.etadirect.rest.ActivityList;
import com.oracle.ofsc.etadirect.utils.DateUtils;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;

public class FiberRoutes extends RouteBuilder {
    private JacksonDataFormat jsonDataFormat = new JacksonDataFormat(ActivityList.class);

    @Override
    public void configure() throws Exception {

        from("direct://fiber/bucket/clear/mci")
                .routeId("gf-mci-bucketclear")
                .bean(Activity.class, "authOnly")
                .bean(DateUtils.class, "getCentralDateForward30Day")
                .to("direct://etadirectrest/activity/bucket")
                // Now have the ETADirect Response with a list of items.
                .unmarshal(jsonDataFormat)
                // Have A List Of Activities
                .bean(Activity.class, "convertToJustActivities")
                .split(body())
                    .bean(Activity.class, "mapMoveToVendor")
                    .to("direct://etadirectrest/activity/move")
                .end();
    }
}
