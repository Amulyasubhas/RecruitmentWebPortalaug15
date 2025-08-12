package com.recruit.springboot.RecruitmentWebPortal.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ModeOfWork {
    WorkFromOffice("WorkFromOffice"),
    Remote("Remote"),
    Hybrid("Hybrid");

    private final String value;

    ModeOfWork(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static ModeOfWork fromValue(String value) {
        if (value == null) {
            return null;
        }
        for (ModeOfWork mode : ModeOfWork.values()) {
            if (mode.value.equalsIgnoreCase(value.replaceAll("\\s+", ""))) {
                return mode;
            }
        }
        throw new IllegalArgumentException("Invalid ModeOfWork: " + value);
    }
}
