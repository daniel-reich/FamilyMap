package com.example.familymap.Models;

import com.google.gson.annotations.SerializedName;

import java.util.UUID;

public class Event {

    /**
     * Copy constructor, used mostly for marriages
     * @param toCopy
     * @param personId
     */
    public Event(Event toCopy, String personId)
    {
        Id = UUID.randomUUID().toString();
        AssociatedUserName = toCopy.AssociatedUserName;
        PersonId = personId;
        Latitude = toCopy.Latitude;
        Longitude = toCopy.Longitude;
        Country = toCopy.Country;
        City = toCopy.City;
        EventType = toCopy.EventType;
        Year = toCopy.Year;
    }

    /**
     * Contructor with all params, used in unit tests
     * @param associatedUserName
     * @param personId
     * @param latitude
     * @param longitude
     * @param country
     * @param city
     * @param eventType
     * @param year
     */
    public Event(String associatedUserName, String personId, double latitude, double longitude, String country, String city, String eventType, int year) {
        Id = UUID.randomUUID().toString();
        AssociatedUserName = associatedUserName;
        PersonId = personId;
        Latitude = latitude;
        Longitude = longitude;
        Country = country;
        City = city;
        EventType = eventType;
        Year = year;
    }

    /**
     * Default Constructor
     */
    public Event() {
        Id = UUID.randomUUID().toString();
    }

    public String getId() {
        return Id;
    }

    public void setId(String eventId) {
        Id = eventId;
    }

    public String getAssociatedUserName() {
        return AssociatedUserName;
    }

    public void setAssociatedUserName(String username) {
        AssociatedUserName = username;
    }

    public String getPersonId() {
        return PersonId;
    }

    public void setPersonId(String personId) {
        PersonId = personId;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getEventType() {
        return EventType;
    }

    public void setEventType(String eventType) {
        EventType = eventType;
    }

    public int getYear() {
        return Year;
    }

    public void setYear(int year) {
        Year = year;
    }

    @SerializedName(value = "eventID", alternate = {"eventId", "eventid", "EventID"})
    String Id;
    @SerializedName(value = "associatedUserName", alternate = {"AssociatedUserName", "associatedusername", "associatedUsername"})
    String AssociatedUserName;
    @SerializedName(value = "personId", alternate = {"personID", "personid", "PersonID", "PersonId"})
    String PersonId;
    @SerializedName(value = "latitude", alternate = {"Latitude"})
    double Latitude;
    @SerializedName(value = "longitude", alternate = {"Longitude"})
    double Longitude;
    @SerializedName(value = "country", alternate = {"Country"})
    String Country;
    @SerializedName(value = "city", alternate = {"City"})
    String City;
    @SerializedName(value = "eventType", alternate = {"eventtype", "EventType", "Eventtype"})
    String EventType;
    @SerializedName(value = "year", alternate = {"Year"})
    int Year;

}


