package com.oracle.ofsc.etadirect.rest;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Created by ssharma on 4/14/17.
 */
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class Recurrence {

    private String recurrenceType;
    private Integer recurrEvery;

    public Integer getRecurrEvery() {
        return recurrEvery;
    }

    public void setRecurrEvery(Integer recurrEvery) {
        this.recurrEvery = recurrEvery;
    }

    public String getRecurrenceType() {

        return recurrenceType;
    }

    public void setRecurrenceType(String recurrenceType) {
        this.recurrenceType = recurrenceType;
    }
}
