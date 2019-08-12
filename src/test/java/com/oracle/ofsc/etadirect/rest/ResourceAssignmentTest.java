package com.oracle.ofsc.etadirect.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;

/**
 * Test For The Proper Generation of Json For Resource Assignment
 *
 */
@RunWith(JUnit4.class)
public class ResourceAssignmentTest {

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testResourceAssignmentItems() throws JsonProcessingException {

        // Top Level Object For Assignments
        ResourceAssignmentItems raItems = new ResourceAssignmentItems();

        // New Item
        ResourceAssignmentItem ra = new ResourceAssignmentItem();
        ra.setResourceId("1122232");
        ra.setPreferenceType("required");

        ArrayList<ResourceAssignmentItem> raItemsList = new ArrayList<>(4);
        raItemsList.add(ra);

        raItems.setItems(raItemsList);

        String restBody = mapper.writeValueAsString(raItems);

        System.out.println(restBody);
    }

}
