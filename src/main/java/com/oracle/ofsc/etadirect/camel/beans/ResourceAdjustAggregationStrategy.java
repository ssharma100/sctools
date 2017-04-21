package com.oracle.ofsc.etadirect.camel.beans;

import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by ssharma on 4/15/17.
 */
public class ResourceAdjustAggregationStrategy implements AggregationStrategy {
    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceAdjustAggregationStrategy.class.getName());
    private static final String AGGREGATE_DATA = "aggregate_data";

    @Override public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {

        LOGGER.info("Performing Aggregation Step");
        if (oldExchange == null) {
            // the first time we only have the new exchange so it wins the first round
            ProcessingResult results = new ProcessingResult();
            ArrayList<ResultItem> resultList = new ArrayList<>(51);
            resultList.add(loadInResult(newExchange));
            results.setResults(resultList);
            newExchange.setProperty(AGGREGATE_DATA, results);
            return newExchange;
        }

        ProcessingResult results = (ProcessingResult )oldExchange.getProperty(AGGREGATE_DATA);
        ArrayList<ResultItem> resultList = results.getResults();
        resultList.add(loadInResult(newExchange));
        newExchange.setProperty(AGGREGATE_DATA, results);
        return newExchange;
    }

    private ResultItem loadInResult(Exchange exchange) {
        Map employeeInfoMap = (Map)exchange.getProperty("employee_info");
        ResultItem r = new ResultItem();
        r.setResourceId((String )employeeInfoMap.get("Employee_No"));
        String error = (String )exchange.getIn().getHeader("errorMsg");
        r.setMessage(StringUtils.isBlank(error) ? "Completed Reset" : error);
        return r;
    }
}
