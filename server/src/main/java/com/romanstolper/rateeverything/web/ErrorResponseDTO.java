package com.romanstolper.rateeverything.web;

/**
 * Created by roman on 1/2/17.
 */
public class ErrorResponseDTO {
    private String message;

    public ErrorResponseDTO(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
