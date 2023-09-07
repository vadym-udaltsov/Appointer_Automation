package com.commons.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum DepartmentType {
    GENERAL("general");

    private final String title;

    public static DepartmentType fromName(String name) {
        for (DepartmentType value : values()) {
            if (value.name().equals(name)) {
                return value;
            }
        }
        throw new RuntimeException("Department type not found for name: " + name);
    }
}
