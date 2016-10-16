package com.oracle.ofsc.transforms;

import org.apache.camel.dataformat.bindy.annotation.CsvRecord;

/**
 * CSV Parsing object - used to capture information provided by the
 * user in their call into the tool.
 * This data will be used by the system to generate the calls to ETAdirect
 * when creating an activity.
 */
@CsvRecord(separator = ",")
public class TransportationActivityData {



}
