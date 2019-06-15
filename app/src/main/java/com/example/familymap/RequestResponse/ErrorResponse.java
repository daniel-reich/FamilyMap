package com.example.familymap.RequestResponse;

/**
 * Class representing an error in a service request, to be displayed when request fails
 */
public class ErrorResponse {
    String message;

    /**
     * Error must be created with user-friendly message
     * @param message Message to return to user
     */
    public ErrorResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
