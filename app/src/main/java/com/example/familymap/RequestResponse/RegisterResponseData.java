package com.example.familymap.RequestResponse;

/**
 * Object returned to user as part of successful register request
 */
public class RegisterResponseData {

    /**
     * Initializes response object with required data
     * @param username the username of the person logging in or registering
     * @param token the token assigned to the user
     * @param personId the personId of this user
     */
    public RegisterResponseData(String username, String token, String personId) {
        Username = username;
        authToken = token;
        PersonId = personId;
    }

    String Username;
    String authToken;
    String PersonId;

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getPersonId() {
        return PersonId;
    }

    public void setPersonId(String personId) {
        PersonId = personId;
    }
}
