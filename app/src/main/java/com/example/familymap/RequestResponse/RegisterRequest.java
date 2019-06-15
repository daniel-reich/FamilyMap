package com.example.familymap.RequestResponse;


import com.example.familymap.Models.User;

/**
 * Represents a request to create a new user in the application
 */
public class RegisterRequest {
    /**
     * Must be instantiated with a user to register
     * @param userToRegister User object of user to register
     */
    public RegisterRequest(User userToRegister) {
        this.userToRegister = userToRegister;
    }

    User userToRegister;

    public User getUserToRegister() {
        return userToRegister;
    }

    public void setUserToRegister(User userToRegister) {
        this.userToRegister = userToRegister;
    }
}
