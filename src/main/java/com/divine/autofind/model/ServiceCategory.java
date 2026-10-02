package com.divine.autofind.model;

public enum ServiceCategory {
    DETAILING("Detailing"),
    TYRES("Tyres"),
    MECHANIC("Mechanics"),
    JUMP_START("Jump starts"),
    CAR_WASH("Car washes");

    private final String label;

    ServiceCategory(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
