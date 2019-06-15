package com.example.familymap.Models;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class DataModel {

    private static DataModel instance;

    private DataModel() { }

    public static DataModel getInstance() {
        if (instance == null) {
            instance = new DataModel();
        }
        return instance;
    }
    private URL baseUrl;


    public URL getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl (String baseUrl) throws MalformedURLException {
        this.baseUrl = new URL(baseUrl);
    }

    public ArrayList<Event> mEvents;
    public ArrayList<Person> mPersons;

    public String getLoggedInPersonId() {
        return loggedInPersonId;
    }

    public void setLoggedInPersonId(String loggedInPersonId) {
        this.loggedInPersonId = loggedInPersonId;
    }

    private String loggedInPersonId;

    public String getName() {
        for(Person p: mPersons) {
            if (p.Id.equals(loggedInPersonId)) {
                return p.getFirstName()+" "+p.getLastName();
            }
        }
        return null;
    }

}
