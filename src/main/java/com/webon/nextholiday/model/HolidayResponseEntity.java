package com.webon.nextholiday.model;

import java.util.Map;

public class HolidayResponseEntity {
    private int statusCode;

    private String statusMessage;

    private Map<String, String> nextHoliday;

    private String namespace;

    public HolidayResponseEntity(int statusCode, String statusMessage, String namespace) {
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
        this.namespace = namespace;
    }

    public HolidayResponseEntity(int statusCode, Map<String, String> nextHoliday) {
        this.statusCode = statusCode;
        this.nextHoliday = nextHoliday;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public Map<String, String> getNextHoliday() {
        return nextHoliday;
    }

    public void setNextHoliday(Map<String, String> nextHoliday) {
        this.nextHoliday = nextHoliday;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }
}
