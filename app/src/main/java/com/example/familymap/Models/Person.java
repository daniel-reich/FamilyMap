package com.example.familymap.Models;

import com.google.gson.annotations.SerializedName;

import java.util.UUID;

public class Person {

    /**
     * Constructor with only required person fields
     * Generates random UUID for id
     * @param associatedUserName application assigned id of user
     * @param firstName person's first name
     * @param lastName person/s last name
     * @param gender Gender enum: 'm' or 'f'
     */
    public Person(String associatedUserName, String firstName, String lastName, Gender gender) {
        AssociatedUserName = associatedUserName;
        FirstName = firstName;
        LastName = lastName;
        Gender = gender;
    }

    /**
     * Constructor with all possible person fields
     * Generates random UUID for id
     * @param userId application assigned id of user
     * @param firstName person's first name
     * @param lastName person's last name
     * @param gender Gender enum: 'm' or 'f'
     * @param spouseId application assigned id of person's spouse
     * @param fatherId application assigned id of person's father
     * @param motherId application assigned id of person's mother
     */
    public Person(String userId, String firstName, String lastName, Gender gender, String spouseId, String fatherId, String motherId) {
        Id = userId;
        AssociatedUserName = userId;
        FirstName = firstName;
        LastName = lastName;
        Gender = gender;
        SpouseId = spouseId;
        FatherId = fatherId;
        MotherId = motherId;
    }

    /**
     * Empty constructor
     */
    public Person() {}

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getAssociatedUserName() {
        return AssociatedUserName;
    }

    public void setAssociatedUserName(String username) {
        AssociatedUserName = username;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public Gender getGender() {
        return Gender;
    }

    public void setGender(Gender gender) {
        Gender = gender;
    }

    public String getSpouseId() {
        return SpouseId;
    }

    public void setSpouseId(String spouseId) {
        SpouseId = spouseId;
    }

    public String getFatherId() {
        return FatherId;
    }

    public void setFatherId(String fatherId) {
        FatherId = fatherId;
    }

    public String getMotherId() {
        return MotherId;
    }

    public void setMotherId(String motherId) {
        MotherId = motherId;
    }

    @SerializedName(value = "personID", alternate = {"PersonID", "personId", "personid"})
    String Id;
    @SerializedName(value = "associatedUserName", alternate = {"AssociatedUserName", "associatedusername", "associatedUsername"})
    String AssociatedUserName;
    @SerializedName(value = "firstName", alternate = {"firstname", "Firstname", "FirstName"})
    String FirstName;
    @SerializedName(value = "lastName", alternate = {"lastname", "Lastname", "LastName"})
    String LastName;
    @SerializedName(value = "gender", alternate = {"Gender"})
    Gender Gender;
    @SerializedName(value = "spouseId", alternate = {"SpouseId", "spouseid", "spouseID"})
    String SpouseId;
    @SerializedName(value = "fatherId", alternate = {"FatherId", "fatherid", "fatherID"})
    String FatherId;
    @SerializedName(value = "motherId", alternate = {"MotherId", "motherid", "motherID"})
    String MotherId;

}
