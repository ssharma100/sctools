package com.oracle.ofsc.etadirect.camel.beans;

import com.oracle.ofsc.etadirect.soap.GetActivity;
import com.oracle.ofsc.etadirect.soap.GetResource;
import com.oracle.ofsc.etadirect.soap.User;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Samir on 10/9/2016.
 */
public class Activity {
    private static final Logger LOGGER = LoggerFactory.getLogger(Activity.class.getName());
    // Used for internal testing
    private static final String USER = "soap";
    private static final String COMPANY = "sunrise3166.demo";
    private static final String PASSWD = "R2OGQreIZp";

    private static final boolean USE_MD5 = true;
    /**
     * Generates body for resource "get" request
     * @param exchange
     */
    public void mapToGetRequest (Exchange exchange) {
        String activityId = (String )exchange.getIn().getHeader("activityId");
        LOGGER.info("Generate Body For ResourceID: {}", activityId);
        // TODO: The request should have the information for the request, however, this is hardcoded for now:
        User userBlock = Security.generateUserAuth(COMPANY, USER, PASSWD, USE_MD5);
        GetActivity activity = new GetActivity();
        activity.setUser(userBlock);
        activity.setActivity_id(activityId);
        exchange.getIn().setBody(activity);
    }
}

