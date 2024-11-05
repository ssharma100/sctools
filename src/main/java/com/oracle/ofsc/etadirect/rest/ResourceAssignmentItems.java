package com.oracle.ofsc.etadirect.rest;

import com.fasterxml.jackson.annotation.JsonRootName;
import java.util.ArrayList;

/**
 * Individual Items for the resource assignment list
 */
@JsonRootName("items")
public class ResourceAssignmentItems {

    private ArrayList<ResourceAssignmentItem> items;

    public ResourceAssignmentItems() {}

    public ArrayList<ResourceAssignmentItem> getItems() {
        return items;
    }

    public void setItems(ArrayList<ResourceAssignmentItem> items) {
        this.items = items;
    }
}
