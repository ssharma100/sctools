package com.oracle.ofsc.routes;

import com.oracle.ofsc.etadirect.camel.beans.AcostaFunctions;
import com.oracle.ofsc.etadirect.camel.beans.DebugOnly;
import com.oracle.ofsc.etadirect.camel.beans.Resource;
import com.oracle.ofsc.etadirect.camel.beans.ResourceAdjustAggregationStrategy;
import org.apache.camel.LoggingLevel;
import org.apache.camel.Predicate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.restlet.data.Status;

/**
 * Contains Acosta Routes That Address Route Plan Building
 */
public class AcostaRoutes  extends RouteBuilder {
    private static final String LOG_CLASS = "com.oracle.ofsc.routes.AcostaRoutes";
    private Predicate isGet = header("CamelHttpMethod").isEqualTo("GET");
    private Predicate isDelete = header("CamelHttpMethod").isEqualTo("DELETE");
    private Predicate isPost = header("CamelHttpMethod").isEqualTo("POST");
    private Predicate routesFound = header("route_count").isGreaterThan(0);
    private Predicate resource_exists = header("ofsc_resource_exists").isEqualTo(true);
    private Predicate sunday_route = exchangeProperty("has_sunday_shift").isNotNull();
    private Predicate saturday_route = exchangeProperty("has_saturday_shift").isNotNull();

    private JacksonDataFormat jacksonDataFormat = new JacksonDataFormat();

    @Override public void configure() throws Exception {

        // Web End-Point
        from("restlet:http://localhost:8085/sctool/v1/acosta/route/baseline/{routeDay}/{resource_id}?restletMethods=get,delete")
            .routeId("invokeBuildBaseLineAcosta")
            .to("log:" + LOG_CLASS + "?showAll=true&multiline=true&level=INFO")
            .choice()
                .when(isGet)
                   .to("direct://buildImpactRouteForDay")
                .when(isDelete)
                    .to("direct://deleteRouteForDay")
                .end();

        // Web End-Point
        // Perform Reset For A Given Weekly Schedule (All Resources)
        from ("restlet:http://localhost:8085/sctool/v1/acosta/schedule/continuity/adjust/{routeDay}?restletMethods=post,get")
            .routeId("invokeContyScheduleReset")
            .choice()
                .when(isGet)
                    .to("direct://schedule/continuity/update")
                .when(isPost)
                    .to("direct://schedule/continuity/reset")
                    .to("direct://processAggregationResults").end();

        // Obtains the route list (ordered) for the given resource "id" on the given date
        // Specifically for Acosta processing the the output will be put into the Acosta DB
        // and targets the route_plan table.
        from("restlet:http://localhost:8085/sctool/v1/acosta/route/{id}/{routeDay}?restletMethod=get")
                .routeId("invokeRouteQueryToRoutePlan")
                .to("log:" + LOG_CLASS + "?showAll=true&multiline=true&level=INFO")
                .to("direct://common/get/route/route_plan");

        // Performs DOW route extraction and shift updates.
        // Will populate the route_plan table for the given DOW and then use that information
        // to sum the number of impact hours spent for that given day.
        from("direct://schedule/continuity/update")
                .routeId("scheduleExtractUpdate")
                .setBody(constant("select Employee_No, POSITION_HRS, IMPACT_HOURS, IMPACT_SUN_SHIFT, IMPACT_MON_SHIFT, "
                        + "IMPACT_TUES_SHIFT, IMPACT_WED_SHIFT, IMPACT_THURS_SHIFT, " + "IMPACT_FRI_SHIFT, IMPACT_SAT_SHIFT "
                        + "from continuity_associates_avail " + "where CONTINUITY = 1"))
                .to("jdbc:acostaDS?useHeadersAsParameters=true&outputType=StreamList")
                .split(body()).streaming()
                    .setProperty("resource_info", simple("${in.body}"))
                    .bean(AcostaFunctions.class, "prepForRouteExtract")
                    .to("direct://common/get/route/route_plan")
                    .choice()
                        .when(routesFound)
                            .bean(AcostaFunctions.class, "prepareResourceUpdate")
                    .endChoice().end();

        // Schedule Reseter: read all continuity resources from the DB.
        // For each one - set the Impactable Value if they have impact hours, and update remaining hours
        from("direct://schedule/continuity/reset")
                .routeId("RestContySchedules")
                .setProperty("original_headers", simple("${in.header[CamelHttpQuery]}"))
                .setBody(constant("select Employee_No, POSITION_HRS, IMPACT_HOURS, IMPACT_SUN_SHIFT, IMPACT_MON_SHIFT, "
                        + "IMPACT_TUES_SHIFT, IMPACT_WED_SHIFT, IMPACT_THURS_SHIFT, "
                        + "IMPACT_FRI_SHIFT, IMPACT_SAT_SHIFT,  CONTY_MON_SHIFT, CONTY_TUES_SHIFT, CONTY_WED_SHIFT, CONTY_THURS_SHIFT, "
                        + "CONTY_FRI_SHIFT, CONTY_SAT_SHIFT, CONTY_SUN_SHIFT " + "from continuity_associates_avail "
                        + "where CONTINUITY = 1"))

                .to("jdbc:acostaDS?useHeadersAsParameters=true&outputType=StreamList")
                .split(body(), new ResourceAdjustAggregationStrategy())
                .setProperty("employee_info", simple("${in.body}"))
                .setHeader("id", simple("${in.body[Employee_No]}")).setBody(constant(null))

                // Get The Resource Record
                .bean(Resource.class, "authOnly")
                .to("direct://generic/resource/get")
                .setHeader("CamelJacksonUnmarshalType", constant("com.oracle.ofsc.etadirect.rest.ResourceJson"))
                .unmarshal(jacksonDataFormat).setProperty("ofsc_resource", simple("${in.body}"))
                .bean(Resource.class, "checkOfscResponse")
                .choice()
                .when(resource_exists)

                // Extract The Resource Record
                // First Do The Shift Reset (Also Checks For Reset Run On Sunday)
                .bean(Resource.class, "resetShiftsForWeek")
                .to("direct://etadirectrest/resource/schedule")

                // Only Process Sundays When A Sunday Is Provided
                .choice()
                .when(sunday_route)
                    .bean(Resource.class, "resetSundayShift")
                    .to("direct://etadirectrest/resource/schedule")
                .otherwise()
                    .log(LoggingLevel.INFO, "Skipping Sunday - No Schedule For Continuity Associate")
                .endChoice()

                // Only Process Sundays When A Sunday Is Provided
                .choice()
                .when(saturday_route)
                .bean(Resource.class, "resetSaturdayShift")
                .to("direct://etadirectrest/resource/schedule")
                .otherwise()
                .log(LoggingLevel.INFO, "Skipping Saturday - No Schedule For Continuity Associate")
                .endChoice()


                // Update The Resource With The Reset Impactable Fields
                .bean(Resource.class, "updateImpactible")
                .to("direct://etadirectrest/resource/update")

                .endChoice()
                .otherwise()
                .setHeader("errorMsg", constant("No Resource In OFSC"))

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
                                + "and DATE(ICD.CALL_STARTED_LOCAL) = :?routeDay " + "and ICD.STATUS = 'Completed' "
                                + "and ICD.Store NOT LIKE 'Wal%' "
                                + "ORDER BY ICD.CALL_STARTED_LOCAL asc"))
                .to("jdbc:acostaDS?useHeadersAsParameters=true&outputType=StreamList")
                .split(body()).streaming()
                    .bean(AcostaFunctions.class, "insertRouteSql")
                    .to("jdbc:acostaDS?useHeadersAsParameters=false");

        // Performs the removal of the Route for the given user and the given day:
        from ("direct://deleteRouteForDay")
          .setBody(constant(
                        "delete from route_plan where resource_id = :?resource_id and route_day = :?routeDay"))
          .to("jdbc:acostaDS?useHeadersAsParameters=true");

        from("direct://processAggregationResults")
            .setBody(simple("${exchangeProperty.aggregate_data}"))
                .bean(DebugOnly.class, "checkStatus")
            .marshal(jacksonDataFormat);
    }
}
