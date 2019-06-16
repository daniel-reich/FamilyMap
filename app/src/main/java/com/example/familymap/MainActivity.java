package com.example.familymap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.Toast;

import com.example.familymap.Fragments.LoginFragment;
import com.example.familymap.Fragments.MapFragment;
import com.example.familymap.Models.DataModel;
import com.example.familymap.RequestResponse.AllEventResponse;
import com.example.familymap.RequestResponse.AllPersonResponse;
import com.example.familymap.RequestResponse.RegisterResponse;
import com.example.familymap.Tasks.GetAllEventsAsyncTask;
import com.example.familymap.Tasks.GetAllPersonsAsyncTask;

public class MainActivity extends AppCompatActivity implements GetAllPersonsAsyncTask.PersonDataListener, GetAllEventsAsyncTask.EventDataListener {

    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FragmentManager frag = getSupportFragmentManager();
        FragmentTransaction transaction = frag.beginTransaction();
        LoginFragment loginFragment = new LoginFragment();
        transaction.add(R.id.loginFragment, loginFragment).commit();

        loginFragment.setLoginOrRegisterResponseListener(new LoginFragment.LoginOrRegisterResponseListener() {
            @Override
            public void onLoginOrRegisterResponseRecieved(RegisterResponse response) {
                if (response.isSuccess()) {
                    DataModel.getInstance().setLoggedInPersonId(response.getData().getPersonId());
                    GetAllEventsAsyncTask getAllEvents = new GetAllEventsAsyncTask(MainActivity.this);
                    getAllEvents.execute(response.getData().getAuthToken());
                    GetAllPersonsAsyncTask getAllPersonsAsyncTask = new GetAllPersonsAsyncTask(MainActivity.this);
                    getAllPersonsAsyncTask.execute(response.getData().getAuthToken());
                } else {
                    Toast.makeText(getApplication(), response.getError().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        toolbar.setTitle("Family Map");

        getSupportActionBar().setIcon(R.drawable.ic_launcher_foreground);
        return true;
    }

    @Override
    public void onGetPersonsResponse(AllPersonResponse response) {
        if (response.isSuccess()) {
            DataModel.getInstance().mPersons = response.getData();
            Toast.makeText(getApplication(), "Welcome "+DataModel.getInstance().getName(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplication(), "Error retrieving person data: "+response.getError().getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onGetEventsResponseRecieved(AllEventResponse response) {
        if (response.isSuccess()) {
            DataModel.getInstance().mEvents = response.getData();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.loginFragment, new MapFragment());
            transaction.commit();
        } else {
            Toast.makeText(getApplication(), "Error retrieving event data: "+response.getError().getMessage(), Toast.LENGTH_LONG).show();

        }
    }
}
