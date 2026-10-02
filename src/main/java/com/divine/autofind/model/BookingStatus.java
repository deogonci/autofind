package com.divine.autofind.model;

public enum BookingStatus {
    REQUESTED("New request"),
    ACCEPTED("Accepted"),
    DECLINED("Declined");

    private final String label;

    BookingStatus(String label) { this.label = label; }
    public String getLabel() { return label; }
}
