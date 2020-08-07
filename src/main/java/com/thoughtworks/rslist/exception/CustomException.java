package com.thoughtworks.rslist.exception;

public class CustomException extends Exception {
    private final String errorMessage;

    public CustomException(String message) {
        super(message);
        this.errorMessage = message;
    }

    @Override
    public String toString() {
        return errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
