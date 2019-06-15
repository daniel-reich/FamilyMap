package com.example.familymap.RequestResponse;

/**
 * Successful load response
 */
public class LoadResponseData {
    public String getMessage() {
        return message;
    }

    /**
     * Creates an instance of fill response with success message
     * @param usersAdded number of people added
     * @param personsAdded number of people added
     * @param eventsAdded number of events added
     */
    public LoadResponseData(int usersAdded, int personsAdded, int eventsAdded)
    {
        message = "Successfully added "+usersAdded+" users "+personsAdded+" persons and "+eventsAdded+" events to the database.";
    }
    String message;
}