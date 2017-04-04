package com.oracle.ofsc.routes;

import com.oracle.ofsc.etadirect.camel.beans.AcostaFunctions;
import org.apache.camel.Predicate;
import org.apache.camel.builder.RouteBuilder;

/**
 * Contains Acosta Routes That Address Route Plan Building
 */
public class AcostaRoutes  extends RouteBuilder {
    private static final String LOG_CLASS = "com.oracle.ofsc.routes.AcostaRoutes";
    private Predicate isGet = header("CamelHttpMethod").isEqualTo("GET");
    private Predicate isDelete = header("CamelHttpMethod").isEqualTo("DELETE");

    @Override public void configure() throws Exception {
        from("restlet:http://localhost:8085/sctool/v1/acosta/route/impact/baseline/{route_date}/{resource_id}?restletMethods=get,delete")
                .routeId("invokeBuildBaseLineAcosta")
                .to("log:" + LOG_CLASS + "?showAll=true&multiline=true&level=INFO")
                .choice()
                    .when(isGet)
                        .to("direct://buildImpactRouteForDay")
                    .when(isDelete)
                        .to("direct://deleteRouteForDay")
                    .end();

        from ("direct://buildImpactRouteForDay")
                .routeId("BuildRouteImpact")
                .setHeader("sequence", constant(0))
                .setBody(constant(
                        "select ICD.*, STORE.LATITUDE as Latitude, STORE.LONGITUDE as Longitude, ASSOC_INFO.LATITUDE as Home_Latitude, ASSOC_INFO.LONGITUDE as Home_Longitude "
                                + "from impact_actual_call_details AS ICD "
                                + "JOIN all_stores as STORE on STORE.acosta_no = ICD.acosta_no and STORE.STOREID = ICD.STOREID "
                                + "JOIN associates_info as ASSOC_INFO ON ASSOC_INFO.EMPLOYEE_NO = ICD.started_by_employee_no "
                                + "where ICD.completed_by_employee_no = :?resource_id "
                                + "and DATE(ICD.CALL_STARTED_LOCAL) = :?route_date " + "and ICD.STATUS = 'Completed' "
                                + "and ICD.Store NOT LIKE 'Wal%'"
                                + "ORDER BY ICD.CALL_STARTED_LOCAL asc"))
                .to("jdbc:acostaDS?useHeadersAsParameters=true&outputType=StreamList")
                .split(body()).streaming().bean(AcostaFunctions.class, "insertRouteSql").to("jdbc:acostaDS?useHeadersAsParameters=false");

        // Performs the removal of the Route for the given user and the given day:
        from ("direct://deleteRouteForDay")
                .setBody(constant(
                        "delete from route_plan where resource_id = :?resource_id and route_id = :?route_date"))
                .to("jdbc:acostaDS?useHeadersAsParameters=true");
    }
}
