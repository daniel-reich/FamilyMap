package com.example.familymap.RequestResponse;

/**
 * Successful fill response data
 */
public class FillResponseData {
    public String getMessage() {
        return message;
    }

    /**
     * Creates an instance of fill response with success message
     * @param personsAdded number of people added
     * @param eventsAdded number of events added
     */
    public FillResponseData(int personsAdded, int eventsAdded)
    {
        message = "Successfully added "+personsAdded+" persons and "+eventsAdded+" events to the database.";
    }
    String message;
}
