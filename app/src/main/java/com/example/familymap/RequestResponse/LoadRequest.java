package com.example.familymap.RequestResponse;


import com.example.familymap.Models.Event;
import com.example.familymap.Models.Person;
import com.example.familymap.Models.User;

/**
 * Represents a request to load users, persons and events into the application
 */
public class LoadRequest {
    /**
     * Initializes request to load users, persons, events into the application
     * @param users Array of user objects to add
     * @param persons Array of person objects to add
     * @param events Array of event objects to add
     */
    public LoadRequest(User[] users, Person[] persons, Event[] events) {
        this.users = users;
        this.persons = persons;
        this.events = events;
    }

    public User[] getUsers() {
        return users;
    }

    public void setUsers(User[] users) {
        this.users = users;
    }

    public Person[] getPersons() {
        return persons;
    }

    public void setPersons(Person[] persons) {
        this.persons = persons;
    }

    public Event[] getEvents() {
        return events;
    }

    public void setEvents(Event[] events) {
        this.events = events;
    }

    User[] users;
    Person[] persons;
    Event[] events;
}
