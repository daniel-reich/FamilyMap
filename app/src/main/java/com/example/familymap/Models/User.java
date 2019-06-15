package com.example.familymap.Models;

import com.google.gson.annotations.SerializedName;

import java.util.UUID;

public class User {

    public User(String userName, String password, String email, String firstName, String lastName, Gender gender) {
        UserName = userName;
        Password = password;
        Email = email;
        FirstName = firstName;
        LastName = lastName;
        Gender = gender;
    }

    public User(String userName, String password) {
        UserName = userName;
        Password = password;
    }

    public User() {

    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
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

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
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

    String Id;
    @SerializedName(value = "userName", alternate = {"UserName", "username"})
    String UserName;
    @SerializedName(value = "password", alternate = {"Password"})
    String Password;
    @SerializedName(value = "email", alternate = {"Email"})
    String Email;
    @SerializedName(value = "firstName", alternate = {"firstname", "Firstname", "FirstName"})
    String FirstName;
    @SerializedName(value = "lastName", alternate = {"lastname", "Lastname", "LastName"})
    String LastName;
    @SerializedName(value = "gender", alternate = {"Gender"})
    Gender Gender;

}
