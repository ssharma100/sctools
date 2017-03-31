package com.oracle.ofsc.routes;

import org.apache.camel.builder.RouteBuilder;

/**
 * Created by ssharma on 3/30/17.
 */
public class AcostaRoutes  extends RouteBuilder {
    private static final String LOG_CLASS = "com.oracle.ofsc.routes.AcostaRoutes";

    @Override public void configure() throws Exception {
        from("restlet:http://localhost:8085/sctool/v1/acosta/route/impact/baseline/{route_date}/{resource_id}?restletMethod=get")
                .routeId("invokeBuildBaseLineAcosta")
                .to("log:" + LOG_CLASS + "?showAll=true&multiline=true&level=INFO")
                .setBody(constant(
                        "select * from impact_actual_call_details where started_by_employee_no = :?resource_id and DATE(CALL_STARTED_LOCAL) = :?route_date ORDER BY CALL_STARTED_LOCAL"))
                .to("jdbc:acostaDS?useHeadersAsParameters=true");
    }
}
