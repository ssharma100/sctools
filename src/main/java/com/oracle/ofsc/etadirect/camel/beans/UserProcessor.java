package com.oracle.ofsc.etadirect.camel.beans;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oracle.ofsc.etadirect.rest.User;
import com.oracle.ofsc.etadirect.rest.UserResponse;
import com.oracle.ofsc.transforms.UserLoginData;
import org.apache.camel.Exchange;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.oracle.ofsc.routes.ETAdirectGenericRoutes.PROP_RESULT_COUNT;

/**
 * Processing class that takes the OFSC responses RE:users and
 * converts them to POJOs.
 *
 */
public class UserProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserProcessor.class.getName());
    private ObjectMapper mapper = new ObjectMapper();
    /**
     * Since there is no filtering in the OFSC, we have the need to process
     * each response to inspect if we need to make a follow up "offset" request to
     * get the next batch of entries.
     * This processing extracts there results into the Exchange Properties and sets
     * up the properties in the exchange to indicate if the next offset is require.
     *
     * @param exchange
     */
    public void partialResponse(Exchange exchange) {

    }

    /**
     * Response should be a list of Users that resulted from querying all
     * Users in OFSC.
     *
     * Inactive Users will be filtered out to provide an active user list
     *
     * @param exchange
     */
    public void mapOFSCUsers(Exchange exchange) {
        // For Now Extract The Run Number...
        UserResponse response = exchange.getIn().getBody(UserResponse.class);

        if (null == exchange.getProperty("allResults")) {
            List<User> userList = new ArrayList<User>(600);
            exchange.setProperty("allResults", userList );
        }

        List<User> userList = exchange.getProperty("allResults", List.class);

        Integer headerOffset = exchange.getIn().getHeader("offset" , Integer.class);
        if (response.getItems().size() == 100) {
            int updatedOffset = headerOffset + response.getItems().size();
            LOGGER.info("Currently Found {} Items Reported Offset {} - Current Offset {} Setting Offset To {}",
                    response.getItems().size(), response.getOffset(), headerOffset, updatedOffset);
            exchange.getIn().setHeader("offset", updatedOffset);
        }
        else {
            LOGGER.info("STOP! Currently Found {} Items Reported Offset {} - Current Offset {}",
                    response.getItems().size(), response.getOffset(), headerOffset);
            exchange.setProperty(PROP_RESULT_COUNT, response.getItems().size());
        }

        userList.addAll(response.getItems());

        // Clear The Body For The Next Call
        exchange.getIn().setBody(null);
    }

    /**
     * Method will filter the consolidated result of the user and filter out the inactivate
     * users to generate a body for bindy processing
     *
     */
    public void processAllUserResults(Exchange exchange) {
        if (null == exchange.getProperty("allResults")) {
            LOGGER.warn("No Results Found - Skipping Output Finalization");
            return;
        }

        List<User> userList = exchange.getProperty("allResults", List.class);

        List<User> activeUsers = userList.stream()
                .filter(user -> StringUtils.equals(user.getStatus(), "active"))
                .collect(Collectors.toList());

        List<UserLoginData> bindyList = new ArrayList<>(600);
        for (User user: activeUsers) {
            UserLoginData uld = new UserLoginData();
            uld.setName(user.getName());
            uld.setLogin(user.getLogin());
            uld.setStatus(user.getStatus());
            uld.setLastLoginTime(user.getLastLoginTime());
            uld.setCreatedTime(user.getCreatedTime());
            uld.setUserType(user.getUserType());
            uld.setTimezone(user.getTimeZone());
            uld.setLastUpdatedTime(user.getLastUpdatedTime());
            uld.setLanguage(user.getLanguage());
            // Generate a String For The Given Array Of Resources
            if (null == user.getResources()) {
                uld.setVendor("Unknown");
            }
            String resourceArrayToStrng = String.format("%s", user.getResources());
            if (StringUtils.containsIgnoreCase(resourceArrayToStrng,"prn")) {
                uld.setVendor("PRINCE");
            }
            else if (StringUtils.containsIgnoreCase(resourceArrayToStrng,"itc")) {
                uld.setVendor("ITC");
            }
            else if (StringUtils.containsIgnoreCase(resourceArrayToStrng,"onp")) {
                uld.setVendor("OnePath");
            }
            else {
                uld.setVendor("Unknown");
            }
            bindyList.add(uld);
        }

        LOGGER.info("Setting Body: Filtered Active Users ({}) From Total Users ({})", activeUsers.size(), userList.size());
        exchange.getIn().setBody(bindyList);
    }
}
