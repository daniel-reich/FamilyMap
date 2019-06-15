package com.example.familymap.RequestResponse;

import com.google.gson.annotations.SerializedName;

/**
 * Class representing a login request from user of the application
 */
public class LoginRequest {
    @SerializedName(value = "userName", alternate = {"UserName", "username"})
    String UserName;
    @SerializedName(value = "password", alternate = {"Password"})
    String Password;

    /**
     * Initializes a login request
     * @param username UserName of user
     * @param password Password of user
     */
    public LoginRequest(String username, String password) {
        UserName = username;
        Password = password;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
}
