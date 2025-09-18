package com.example.carus.utils.enums;

import com.example.carus.utils.interfaces.HasMessage;

public enum ExceptionMessage implements HasMessage {
    SOMETHING_WRONG("Something went wrong");

    private final String message;

    ExceptionMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
