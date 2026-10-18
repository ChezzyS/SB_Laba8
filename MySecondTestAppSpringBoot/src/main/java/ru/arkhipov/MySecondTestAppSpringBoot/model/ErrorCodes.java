package ru.arkhipov.MySecondTestAppSpringBoot.model;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ErrorCodes {

    EMPTY(""),
    VALIDAION_EXCEPTION("ValidationException"),
    UNKNOWN_EXCEPTION("UnknownException"),
    UNSUPPORTED_EXCETPION("UnsupportedException");

    private final String name;

    ErrorCodes(String name) {
        this.name = name;
    }

    @JsonValue
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
