package com.example.familymap.Tasks;

import android.os.AsyncTask;
import android.widget.Toast;

import com.example.familymap.Models.DataModel;
import com.example.familymap.RequestResponse.AllPersonResponse;
import com.example.familymap.RequestResponse.ErrorResponse;
import com.example.familymap.Server.FMSProxy;

import java.io.IOException;

public class GetAllPersonsAsyncTask extends AsyncTask<String, Integer, AllPersonResponse> {

    public interface PersonDataListener
    {
        void onGetPersonsResponse(AllPersonResponse response);
    }

    private PersonDataListener personDataListener;

    public GetAllPersonsAsyncTask(PersonDataListener personDataListener)
    {
        this.personDataListener = personDataListener;
    }

    @Override
    protected AllPersonResponse doInBackground(String... strings) {
        FMSProxy proxy = new FMSProxy(DataModel.getInstance().getBaseUrl());
        try {
            return proxy.GetPersons(strings[0]);
        } catch (IOException e) {
            AllPersonResponse response =  new AllPersonResponse();
            response.setError(new ErrorResponse(e.getMessage()));
            return response;
        }
    }

    @Override
    protected void onPostExecute(AllPersonResponse allPersonResponse) {
        personDataListener.onGetPersonsResponse(allPersonResponse);
    }
}
