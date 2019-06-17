package com.example.familymap.Tasks;

import android.os.AsyncTask;
import com.example.familymap.Models.DataModel;
import com.example.familymap.RequestResponse.AllEventResponse;
import com.example.familymap.RequestResponse.ErrorResponse;
import com.example.familymap.Utilities.FMSProxy;

import java.io.IOException;

public class GetAllEventsAsyncTask extends AsyncTask<String, Integer, AllEventResponse> {

    public interface EventDataListener {
        void onGetEventsResponseRecieved(AllEventResponse response);
    }

    private EventDataListener eventDataListener;

    public GetAllEventsAsyncTask(EventDataListener eventDataListener) {
        this.eventDataListener = eventDataListener;
    }

    @Override
    protected AllEventResponse doInBackground(String... strings) {
        FMSProxy proxy = new FMSProxy(DataModel.getInstance().getBaseUrl());
        try {
            return proxy.GetEvents(strings[0]);
        } catch (IOException e) {
            AllEventResponse response =  new AllEventResponse();
            response.setError(new ErrorResponse(e.getMessage()));
            return response;
        }
    }

    @Override
    protected void onPostExecute(AllEventResponse allEventResponse) {
        eventDataListener.onGetEventsResponseRecieved(allEventResponse);
    }
}
