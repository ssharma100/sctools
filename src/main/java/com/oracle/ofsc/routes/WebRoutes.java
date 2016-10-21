/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.oracle.ofsc.routes;

import org.apache.camel.Predicate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.bindy.csv.BindyCsvDataFormat;
import org.apache.camel.spi.DataFormat;
import org.apache.camel.spring.Main;
import org.restlet.data.Method;

/**
 * A Camel Router For Web Integration End-Points
 */
public class WebRoutes extends RouteBuilder {
    private static final String LOG_CLASS = "com.oracle.ofsc.routes.WebRoutes";
    private Predicate isPost = header("CamelHttpMethod").isEqualTo("POST");
    private DataFormat routeReport = new BindyCsvDataFormat(com.oracle.ofsc.transforms.RouteReportData.class);

    /**
     * Let's configure the Camel routing rules using Java code...
     */
    public void configure() {
        // RESTful End Point For Resource Management
        // - Get Resource
        // - Inser Resource
        from("restlet:http://localhost:8085/sctool/v1/transportation/resource/{id}?restletMethods=post,get")
                .routeId("invokeTransResourceCall")
                .to("log:" + LOG_CLASS + "?showAll=true&multiline=true&level=INFO")
                .choice()
                    .when(isPost)
                        .to("direct://transportation/resource/insert")
                    .otherwise()
                        .to("direct://transportation/resource/get");

        // RESTful End Point For Activity
        // - Get Activity
        // - Insert Activity
        from("restlet:http://localhost:8085/sctool/v1/transportation/activity/{id}?restletMethods=post,get")
                .routeId("invokeTransActivityCall")
                .to("log:" + LOG_CLASS + "?showAll=true&multiline=true&level=INFO")
                .choice()
                    .when(isPost)
                        .to("direct://transportation/activity/insert")
                    .otherwise()
                        .to("direct://transportation/activity/get");

        from("restlet:http://localhost:8085/sctool/v1/route/{id}/{routeDay}?restletMethod=get")
                .routeId("invokeRouteQueryCall")
                .to("log:" + LOG_CLASS + "?showAll=true&multiline=true&level=INFO")
                .to("direct://common/get/route");

        from("restlet:http://localhost:8085/sctool/v1/bulkroute/{station}/{routeDay}?restletMethod=get")
                .routeId("invokeBulkRouteQueryCall")
                .to("log:" + LOG_CLASS + "?showAll=true&multiline=true&level=INFO")
                .to("direct://common/get/route/bulk")
                .to("log:" + LOG_CLASS + "?showAll=true&multiline=true&level=DEBUG");

        // Call To Geolocation Information (Enhancement)
        from("restlet:http://localhost:8085/sctool/v1/route/enhance/distance/{googleKey}?restletMethod=post")
                .routeId("routingEnhanceDistance")
                .to("log:" + LOG_CLASS + "?showAll=true&multiline=true&level=INFO")
                .to("direct://common/get/route/enhance/distance")
                .to("log:" + LOG_CLASS + "?showAll=true&multiline=true&level=DEBUG");
    }
}
