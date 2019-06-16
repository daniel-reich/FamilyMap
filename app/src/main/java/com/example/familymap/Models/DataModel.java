package com.example.familymap.Models;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

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
        Person p = masterPersonList.get(loggedInPersonId);
        return p.getFirstName()+" "+p.getLastName();

    }

    private HashMap<String, Person> masterPersonList;
    private HashMap<String, Event> masterEventList;
    private HashMap<String, ArrayList<String>> filterDictionary;
    private Collection<String> mothersSide;
    private Collection<String> fathersSide;
    private HashMap<String, Boolean> currentFilterSettings;
    private HashMap<String, Event> displayEvents;

    public ArrayList<Event> getDisplayEvents() {
        displayEvents = new HashMap<>(masterEventList);
        for(String filter: filterDictionary.keySet()) {
            if (!currentFilterSettings.get(filter)) {
                for (String removeKey: filterDictionary.get(filter)) {
                    displayEvents.remove(removeKey);
                }
            }
        }
        return new ArrayList<>(displayEvents.values());
    }

    public HashMap<String, Boolean> getCurrentFilterSettings() {
        return currentFilterSettings;
    }

    public HashMap<String, ArrayList<String>> getFilterDictionary() {
        return filterDictionary;
    }

    public void setMasterPersonList(ArrayList<Person> persons) {
        HashMap<String, Person> map = new HashMap<>();
        for(Person person: persons) {
            map.put(person.getId(), person);
        }
        this.masterPersonList = map;
        calculateData();
    }

    public void setMasterEventsList(ArrayList<Event> events) {
        HashMap<String, Event> map = new HashMap<>();
        for(Event event: events) {
            map.put(event.getId(), event);
        }
        this.masterEventList = map;
        calculateData();
    }

    public void calculateData() {
        if (masterEventList != null && masterPersonList != null)
        {
            calculateFamilySides();
            calculateFilterDictionary();
        }

    }

    private void calculateFilterDictionary() {
        filterDictionary = new HashMap<>();
        for (String eventId: masterEventList.keySet()) {
            Event event = masterEventList.get(eventId);
            addToListOrCreate(event.EventType.toLowerCase()+" events", eventId, filterDictionary);
            if (mothersSide.contains(event.getPersonId()))
                addToListOrCreate("mother's side of family", eventId, filterDictionary);
            if (fathersSide.contains(event.getPersonId()))
                addToListOrCreate("father's side of family", eventId, filterDictionary);
            if (masterPersonList.get(event.getPersonId()).getGender() == Gender.f)
                addToListOrCreate("female events", eventId, filterDictionary);
            if (masterPersonList.get(event.getPersonId()).getGender() == Gender.m)
                addToListOrCreate("male events", eventId, filterDictionary);
        }
        currentFilterSettings = new HashMap<>();
        for (String key: filterDictionary.keySet()) {
            currentFilterSettings.put(key, true);
        }
    }

    private void addToListOrCreate(String key, String eventId, HashMap<String, ArrayList<String>> map) {
        if (map.containsKey(key)) {
            map.get(key).add(eventId);
        } else {
            ArrayList<String> list = new ArrayList<>();
            list.add(eventId);
            map.put(key, list);
        }
    }

    private void calculateFamilySides() {
        fathersSide = new HashSet<>();
        mothersSide = new HashSet<>();
        String father = masterPersonList.get(loggedInPersonId).getFatherId();
        String mother = masterPersonList.get(loggedInPersonId).getMotherId();
        if (father != null)
            addToTreeRecursive(father, fathersSide);
        if (mother != null)
            addToTreeRecursive(mother, mothersSide);

    }

    private void addToTreeRecursive(String personId, Collection<String> collection) {
        collection.add(personId);
        String father = masterPersonList.get(personId).getFatherId();
        String mother = masterPersonList.get(personId).getMotherId();
        if (father != null)
            addToTreeRecursive(father, collection);
        if (mother != null)
            addToTreeRecursive(mother, collection);

    }

}
