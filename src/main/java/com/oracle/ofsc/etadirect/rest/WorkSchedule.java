package com.oracle.ofsc.etadirect.rest;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Created by ssharma on 4/14/17.
 */
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class WorkSchedule {

    private String recordType;
    private String startDate;
    private String endDate;
    private String workTimeStart;
    private String workTimeEnd;
    private String shiftType;
    private Recurrence recurrence;

    public String getRecordType() {
        return recordType;
    }

    public void setRecordType(String recordType) {
        this.recordType = recordType;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getWorkTimeStart() {
        return workTimeStart;
    }

    public void setWorkTimeStart(String workTimeStart) {
        this.workTimeStart = workTimeStart;
    }

    public String getWorkTimeEnd() {
        return workTimeEnd;
    }

    public void setWorkTimeEnd(String workTimeEnd) {
        this.workTimeEnd = workTimeEnd;
    }

    public String getShiftType() {
        return shiftType;
    }

    public void setShiftType(String shiftType) {
        this.shiftType = shiftType;
    }

    public Recurrence getRecurrence() {
        return recurrence;
    }

    public void setRecurrence(Recurrence recurrence) {
        this.recurrence = recurrence;
    }
}
