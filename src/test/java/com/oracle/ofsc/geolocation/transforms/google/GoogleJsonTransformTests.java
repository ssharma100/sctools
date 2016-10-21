package com.oracle.ofsc.geolocation.transforms.google;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.StringStartsWith.startsWith;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;

/**
 * Created by xxx_sharma on 10/21/16.
 */
@RunWith(JUnit4.class)
public class GoogleJsonTransformTests {

    private static final String SAMPLE_RESPONSE = "{\n" +
            "    \"destination_addresses\": [\n" +
            "        \"401-699 W Flint St, Laramie, WY 82072, USA\"\n" +
            "    ],\n" +
            "    \"origin_addresses\": [\n" +
            "        \"1132-1220 E Curtis St, Laramie, WY 82072, USA\"\n" +
            "    ],\n" +
            "    \"rows\": [\n" +
            "        {\n" +
            "            \"elements\": [\n" +
            "                {\n" +
            "                    \"distance\": {\n" +
            "                        \"text\": \"2.0 mi\",\n" +
            "                        \"value\": 3227\n" +
            "                    },\n" +
            "                    \"duration\": {\n" +
            "                        \"text\": \"6 mins\",\n" +
            "                        \"value\": 374\n" +
            "                    },\n" +
            "                    \"status\": \"OK\"\n" +
            "                }\n" +
            "            ]\n" +
            "        }\n" +
            "    ],\n" +
            "    \"status\": \"OK\"\n" +
            "}";

    private static ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testResponseParsing() throws IOException {
        DistanceJson distance = mapper.readValue(SAMPLE_RESPONSE, DistanceJson.class);
        assertThat("Distance Response Is Not Null", distance, notNullValue());
        // Check Values are set as expected:
        assertThat("Destination Address Is Provided", distance.getDestinations().isEmpty(), is(false));
        assertThat("Origin Address Is Provided", distance.getOrigins().isEmpty(), is(false));

        assertThat("Destination Address Is Correct", distance.getDestinations().get(0), startsWith("401-699"));
    }

}
