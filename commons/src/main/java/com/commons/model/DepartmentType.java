package com.commons.model;

public enum DepartmentType {
    GENERAL;

    public static DepartmentType fromName(String name) {
        for (DepartmentType value : values()) {
            if (value.name().equals(name)) {
                return value;
            }
        }
        throw new RuntimeException("Department type not found for name: " + name);
    }
}
