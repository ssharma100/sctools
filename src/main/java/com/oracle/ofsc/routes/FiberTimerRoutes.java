package com.oracle.ofsc.routes;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;

public class FiberTimerRoutes extends RouteBuilder {
    @Override
    public void configure() throws Exception {

        // Timer For The MCI Clear.
        from("quartz2://fiber/MCIClear?cron=0+0/1+*+*+*+?")
                .routeId("timer-MCIBucket")
                .log(LoggingLevel.INFO, "Timer Invoked Bucket Clear - MCI")
                .setProperty("BUCKET", constant("kansascity"))
                .setHeader("CamelHttpQuery", constant("user=2070955c7632d7e917ca04c58aa846456a94852a&company=google&passwd=cbdcf54ece721e21c8b0cab2eaf66ba82149850589a0576b7c18da86c300a6b6"))
                .to("direct://fiber/bucket/clear/mci");
    }
}
