package com.example.familymap.Models;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;

import com.example.familymap.R;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

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
    private HashMap<String, ArrayList<Event>> displayPersonEvent;
    private MapColors familyTreeColor = MapColors.Black;
    private MapColors spouseColor = MapColors.Yellow;
    private MapColors lifeStoryColor = MapColors.Red;
    private boolean showFamilyTree = true;
    private boolean showSpouse = true;
    private boolean showlifeStory = true;
    private String authToken;

    public HashMap<String, ArrayList<Event>> getDisplayPersonEvent() {
        return displayPersonEvent;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public MapColors getFamilyTreeColor() {
        return familyTreeColor;
    }

    public void setFamilyTreeColor(MapColors familyTreeColor) {
        this.familyTreeColor = familyTreeColor;
    }

    public MapColors getSpouseColor() {
        return spouseColor;
    }

    public void setSpouseColor(MapColors spouseColor) {
        this.spouseColor = spouseColor;
    }

    public MapColors getLifeStoryColor() {
        return lifeStoryColor;
    }

    public void setLifeStoryColor(MapColors lifeStoryColor) {
        this.lifeStoryColor = lifeStoryColor;
    }

    public boolean isShowFamilyTree() {
        return showFamilyTree;
    }

    public void setShowFamilyTree(boolean showFamilyTree) {
        this.showFamilyTree = showFamilyTree;
    }

    public boolean isShowSpouse() {
        return showSpouse;
    }

    public void setShowSpouse(boolean showSpouse) {
        this.showSpouse = showSpouse;
    }

    public boolean isShowlifeStory() {
        return showlifeStory;
    }

    public void setShowlifeStory(boolean showlifeStory) {
        this.showlifeStory = showlifeStory;
    }

    public HashMap<String, Person> getMasterPersonList() {
        return masterPersonList;
    }

    public HashMap<String, Event> getMasterEventList() {
        return masterEventList;
    }

    public ArrayList<Event> getDisplayEvents() {
        displayEvents = new HashMap<>(masterEventList);
        for(String filter: filterDictionary.keySet()) {
            if (!currentFilterSettings.get(filter)) {
                for (String removeKey: filterDictionary.get(filter)) {
                    displayEvents.remove(removeKey);
                }
            }
        }
        calculateDisplayPersonEvent();
        return new ArrayList<>(displayEvents.values());
    }

    public HashMap<String, Boolean> getCurrentFilterSettings() {
        return currentFilterSettings;
    }

    public HashMap<String, ArrayList<String>> getFilterDictionary() {
        return filterDictionary;
    }

    public void setMasterPersonList(ArrayList<Person> persons) {
        if (persons == null) {
            this.masterPersonList = null;
        } else {
            HashMap<String, Person> map = new HashMap<>();
            for(Person person: persons) {
                map.put(person.getId(), person);
            }
            this.masterPersonList = map;
            calculateData();
        }

    }

    public void setMasterEventsList(ArrayList<Event> events) {
        if (events ==null) {
            this.masterEventList = null;
        } else {
            HashMap<String, Event> map = new HashMap<>();
            for(Event event: events) {
                map.put(event.getId(), event);
            }
            this.masterEventList = map;
            calculateData();
        }
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

    private <T> void addToListOrCreate(String key, T event, HashMap<String, ArrayList<T>> map) {
        if (map.containsKey(key)) {
            map.get(key).add(event);
        } else {
            ArrayList<T> list = new ArrayList<>();
            list.add(event);
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

    private void calculateDisplayPersonEvent() {
        displayPersonEvent = new HashMap<>();
        for(Event event: displayEvents.values()) {
            addToListOrCreate(event.getPersonId(), event, displayPersonEvent);
        }

        for (ArrayList<Event> events: displayPersonEvent.values()){
            Collections.sort(events, new Comparator<Event>() {
                @Override
                public int compare(Event event, Event t1) {
                    if (event.getEventType().toLowerCase().equals("birth")) {
                        return -1;
                    } else if (t1.getEventType().toLowerCase().equals("birth")) {
                        return 1;
                    } else if (event.getEventType().toLowerCase().equals("death")) {
                        return 1;
                    } else if (t1.getEventType().toLowerCase().equals("death")) {
                        return -1;
                    } else if (event.getYear() < t1.getYear()) {
                        return -1;
                    } else if (event.getYear() > t1.getYear()) {
                        return 1;
                    } else {
                        return event.getEventType().toLowerCase().compareTo(t1.getEventType().toLowerCase());
                    }
                }
            });
        }
    }

    public ArrayList<PolylineOptions> calculateMapLines(String eventId) {
        boolean spouse = true;
        boolean tree = true;
        boolean life = true;
        ArrayList<PolylineOptions> lines = new ArrayList<>();

        Event start = DataModel
                .getInstance()
                .getMasterEventList()
                .get(eventId);

        String personId = start.getPersonId();

        if (showSpouse) {
            String spouseId = masterPersonList.get(personId).SpouseId;
            if (spouseId != null) {
                Event spouseEvent = displayPersonEvent.get(spouseId).get(0);

                lines.add(new PolylineOptions().add(
                        new LatLng(start.getLatitude(), start.getLongitude()),
                        new LatLng(spouseEvent.getLatitude(), spouseEvent.getLongitude())).color(getColor(spouseColor)));
            }

        }
        if (showlifeStory) {
            PolylineOptions options = new PolylineOptions();
            for(Event event: displayPersonEvent.get(personId)) {
                options.add(new LatLng(event.getLatitude(), event.getLongitude()));
            }
            options.color(getColor(lifeStoryColor));
            lines.add(options);
        }
        if (showFamilyTree) {
            Person person = masterPersonList.get(personId);
            recursiveFamilyTreeLines(person, start, 25, lines);
        }
        return lines;
    }

    private void recursiveFamilyTreeLines(Person person, Event start, int width, ArrayList<PolylineOptions> lines) {
        ArrayList<Event> fatherEventList = displayPersonEvent.get(person.getFatherId());
        ArrayList<Event> motherEventList = displayPersonEvent.get(person.getMotherId());

        if (fatherEventList != null && fatherEventList.size()>0) {
            Event fatherEvent = fatherEventList.get(0);

            lines.add(
                    new PolylineOptions().add(
                            new LatLng(start.getLatitude(), start.getLongitude()),
                            new LatLng(fatherEvent.getLatitude(), fatherEvent.getLongitude()))
                    .color(getColor(familyTreeColor))
                    .width(width)
            );

            if (masterPersonList.get(person.getFatherId()) != null) {
                Person father = masterPersonList.get(person.getFatherId());
                recursiveFamilyTreeLines(father, fatherEvent, width-4, lines);
            }
        }

        if (motherEventList != null && motherEventList.size()>0) {
            Event motherEvent = motherEventList.get(0);

            lines.add(
                    new PolylineOptions().add(
                            new LatLng(start.getLatitude(), start.getLongitude()),
                            new LatLng(motherEvent.getLatitude(), motherEvent.getLongitude()))
                            .color(getColor(familyTreeColor))
                            .width(width)
            );

            if (masterPersonList.get(person.getMotherId()) != null) {
                Person mother = masterPersonList.get(person.getMotherId());
                recursiveFamilyTreeLines(mother, motherEvent, width-4, lines);
            }
        }


    }

    private int getColor(MapColors color) {
        switch (color) {
            case Blue: return Color.BLUE;
            case Red: return Color.RED;
            case Green: return Color.GREEN;
            case Black: return Color.BLACK;
            case White: return Color.WHITE;
            case Yellow: return Color.YELLOW;
            default: return R.color.white;
        }
    }

    public void reset() {
        instance = null;
    }

    public ArrayList<PersonEventListItem> convertEventsToListItems(ArrayList<Event> events, Context context) {
        ArrayList<PersonEventListItem> items = new ArrayList<>();
        for(Event event: events) {
            Person person = masterPersonList.get(event.getPersonId());
            String topText = event.getEventType()+": "+event.City+", "+event.getCountry()+" ("+event.getYear()+")";
            String bottomText = person.getFirstName()+" "+person.getLastName();
            Drawable icon = new IconDrawable(context, FontAwesomeIcons.fa_map_marker).
                    colorRes(R.color.red).sizeDp(40);

            items.add(new PersonEventListItem("event", topText, bottomText, event.getId(), icon));
        }
        return items;
    }

    public ArrayList<PersonEventListItem> getFamilyItems(String personId, Context context) {
        ArrayList<PersonEventListItem> items = new ArrayList<>();
        Person person = masterPersonList.get(personId);
        if (person.getSpouseId() != null) {
            Person spouse = masterPersonList.get(person.getSpouseId());
            items.add(createFamilyMember("Spouse", spouse, context));
        }
        if (person.getFatherId() != null) {
            Person father = masterPersonList.get(person.getFatherId());
            items.add(createFamilyMember("Father", father, context));
        }
        if (person.getMotherId() != null) {
            Person mother = masterPersonList.get(person.getMotherId());
            items.add(createFamilyMember("Mother", mother, context));
        }
        for (Person p: masterPersonList.values()) {
            if (compare(p.getMotherId(), person.getId()) || compare(p.getFatherId(),person.getId())) {
                Person child = masterPersonList.get(p.getId());
                items.add(createFamilyMember("Child", child, context));
            }
        }
        return items;
    }

    private PersonEventListItem createFamilyMember(String relation, Person person, Context context) {
        String textTop = person.FirstName+" "+person.LastName;
        String bottomText = relation;
        Drawable icon;
        if (person.getGender() == Gender.f) {
            icon = new IconDrawable(context, FontAwesomeIcons.fa_female).
                    colorRes(R.color.female_color).sizeDp(40);
        } else {
            icon = new IconDrawable(context, FontAwesomeIcons.fa_male).
                    colorRes(R.color.male_color).sizeDp(40);
        }
        return new PersonEventListItem("person", textTop, bottomText, person.getId(), icon);

    }

    private static boolean compare(String first, String second) {
        return (first == null ? second == null : first.equals(second));
    }




}
