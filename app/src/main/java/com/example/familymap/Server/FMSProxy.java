package com.example.familymap.Server;

import com.example.familymap.Models.Event;
import com.example.familymap.Models.Person;
import com.example.familymap.Models.User;
import com.example.familymap.RequestResponse.AllEventResponse;
import com.example.familymap.RequestResponse.AllPersonResponse;
import com.example.familymap.RequestResponse.ErrorResponse;
import com.example.familymap.RequestResponse.RegisterResponse;
import com.example.familymap.RequestResponse.RegisterResponseData;
import com.example.familymap.Utilities.Convert;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

public class FMSProxy {

    private URL baseUrl;

    public FMSProxy(URL url) {
        baseUrl = url;
    }


    public RegisterResponse RegisterUser(User user) throws IOException {
        URL url = new URL(baseUrl + "/user/register");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json; utf-8");
        connection.setRequestProperty("Accept", "application/json");
        connection.setDoOutput(true);
        Convert.toInputStream(user, connection.getOutputStream());
        RegisterResponse response = new RegisterResponse();
        if (connection.getResponseCode() == 201)
        {
            response.setData(Convert.fromInputStream(RegisterResponseData.class, connection.getInputStream()));
        } else {
            response.setError(Convert.fromInputStream(ErrorResponse.class, connection.getErrorStream()));
        }
        return response;
    }

    public RegisterResponse LoginUser(User user) throws IOException {
        URL url = new URL(baseUrl + "/user/login");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json; utf-8");
        connection.setRequestProperty("Accept", "application/json");
        connection.setDoOutput(true);
        Convert.toInputStream(user, connection.getOutputStream());
        RegisterResponse response = new RegisterResponse();
        if (connection.getResponseCode() == 200)
        {
            response.setData(Convert.fromInputStream(RegisterResponseData.class, connection.getInputStream()));
        } else {
            response.setError(Convert.fromInputStream(ErrorResponse.class, connection.getErrorStream()));
        }
        return response;
    }

    public AllEventResponse GetEvents(String authToken) throws IOException {
        URL url = new URL(baseUrl + "/event");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestProperty("Authorization", authToken);
        connection.connect();
        AllEventResponse response = new AllEventResponse();
        if (connection.getResponseCode() == 200)
        {
            response.setData(new ArrayList<Event>(Arrays.asList(Convert.fromInputStream(Event[].class, connection.getInputStream()))));
        } else {
            response.setError(Convert.fromInputStream(ErrorResponse.class, connection.getErrorStream()));
        }
        return response;
    }

    public AllPersonResponse GetPersons(String authToken) throws IOException {
        URL url = new URL(baseUrl + "/person");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestProperty("Authorization", authToken);
        connection.connect();
        AllPersonResponse response = new AllPersonResponse();
        if (connection.getResponseCode() == 200)
        {
            response.setData(new ArrayList<Person>(Arrays.asList(Convert.fromInputStream(Person[].class, connection.getInputStream()))));
        } else {
            response.setError(Convert.fromInputStream(ErrorResponse.class, connection.getErrorStream()));
        }
        return response;
    }



}
