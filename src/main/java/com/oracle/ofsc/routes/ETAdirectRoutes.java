package com.oracle.ofsc.routes;

import com.oracle.ofsc.etadirect.camel.beans.Resource;
import com.oracle.ofsc.etadirect.soap.GetResource;
import org.apache.camel.builder.RouteBuilder;

/**
 * Created by Samir on 10/3/2016.
 */
public class ETAdirectRoutes extends RouteBuilder{
    private static final String LOG_CLASS = "com.oracle.ofsc.routes.ETAdirectRoutes";

    @Override
    public void configure() throws Exception {

        /* Populates The Body With The SOAP Call Needed To Call The Server */
        from("direct://resource/get")
                .routeId("etaDirectResourceGet")
                .to("log:" + LOG_CLASS + "?level=INFO")
                .bean(Resource.class, "mapToSoapRequest")
                .to("direct://etadirectsoap/get/resource");
    }
}
