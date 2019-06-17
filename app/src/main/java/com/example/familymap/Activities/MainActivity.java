package com.example.familymap.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.familymap.Fragments.LoginFragment;
import com.example.familymap.Fragments.MapFragment;
import com.example.familymap.Models.DataModel;
import com.example.familymap.R;
import com.example.familymap.RequestResponse.AllEventResponse;
import com.example.familymap.RequestResponse.AllPersonResponse;
import com.example.familymap.RequestResponse.RegisterResponse;
import com.example.familymap.Tasks.GetAllEventsAsyncTask;
import com.example.familymap.Tasks.GetAllPersonsAsyncTask;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

public class MainActivity extends AppCompatActivity implements GetAllPersonsAsyncTask.PersonDataListener, GetAllEventsAsyncTask.EventDataListener {

    private Menu menu;
    private boolean loggedIn = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Iconify.with(new FontAwesomeModule());

        if (getIntent().hasExtra("LOGGED_IN")) {
            loggedIn = getIntent().getExtras().getBoolean("LOGGED_IN");
        }

        if (loggedIn) {
            FragmentManager frag = getSupportFragmentManager();
            FragmentTransaction transaction = frag.beginTransaction();
            MapFragment mapFragment = new MapFragment();
            transaction.replace(R.id.loginFragment, mapFragment).commit();
            //setMenuIconsVisible(true);
        } else {
            FragmentManager frag = getSupportFragmentManager();
            FragmentTransaction transaction = frag.beginTransaction();
            LoginFragment loginFragment = new LoginFragment();
            transaction.add(R.id.loginFragment, loginFragment).commit();

            loginFragment.setLoginOrRegisterResponseListener(new LoginFragment.LoginOrRegisterResponseListener() {
                @Override
                public void onLoginOrRegisterResponseRecieved(RegisterResponse response) {
                    if (response.isSuccess()) {
                        setMenuIconsVisible(true);
                        DataModel.getInstance().setLoggedInPersonId(response.getData().getPersonId());
                        DataModel.getInstance().setAuthToken(response.getData().getAuthToken());
                        GetAllEventsAsyncTask getAllEvents = new GetAllEventsAsyncTask(MainActivity.this);
                        getAllEvents.execute(DataModel.getInstance().getAuthToken());
                        GetAllPersonsAsyncTask getAllPersonsAsyncTask = new GetAllPersonsAsyncTask(MainActivity.this);
                        getAllPersonsAsyncTask.execute(DataModel.getInstance().getAuthToken());
                    } else {
                        Toast.makeText(getApplication(), response.getError().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        this.menu = menu;
        MenuItem settingsItem = menu.findItem(R.id.menu_settings);
        MenuItem searchItem = menu.findItem(R.id.menu_search);
        MenuItem filterItem = menu.findItem(R.id.menu_filter);
        Drawable gearIcon = new IconDrawable(this, FontAwesomeIcons.fa_gear).
                colorRes(R.color.white).sizeDp(40);

        settingsItem.setIcon(gearIcon);

        Drawable searchIcon = new IconDrawable(this, FontAwesomeIcons.fa_search).
                colorRes(R.color.white).sizeDp(40);

        searchItem.setIcon(searchIcon);

        Drawable filterIcon = new IconDrawable(this, FontAwesomeIcons.fa_filter).
                colorRes(R.color.white).sizeDp(40);

        filterItem.setIcon(filterIcon);
        setMenuIconsVisible(loggedIn);
        return true;
    }

    public void setMenuIconsVisible(boolean visible) {
        MenuItem settingsItem = menu.findItem(R.id.menu_settings);
        MenuItem searchItem = menu.findItem(R.id.menu_search);
        MenuItem filterItem = menu.findItem(R.id.menu_filter);
        if (visible) {
            settingsItem.setVisible(true);
            searchItem.setVisible(true);
            filterItem.setVisible(true);
        } else {
            settingsItem.setVisible(false);
            searchItem.setVisible(false);
            filterItem.setVisible(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_search:
                Intent searchIntent = new Intent(this, SearchActivity.class);
                startActivity(searchIntent);
                return true;
            case R.id.menu_filter:
                Intent filterIntent = new Intent(this, FilterActivity.class);
                startActivity(filterIntent);
                return true;
            case R.id.menu_settings:
                Intent settingsIntent = new Intent(this, SettingsActivity.class);
                startActivity(settingsIntent);
                return true;
        }
        return true;
    }

    @Override
    public void onGetPersonsResponse(AllPersonResponse response) {
        if (response.isSuccess()) {
            DataModel.getInstance().setMasterPersonList(response.getData());
            Toast.makeText(getApplication(), "Welcome "+DataModel.getInstance().getName(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplication(), "Error retrieving person data: "+response.getError().getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onGetEventsResponseRecieved(AllEventResponse response) {
        if (response.isSuccess()) {
            DataModel.getInstance().setMasterEventsList(response.getData());
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.loginFragment, new MapFragment());
            transaction.commit();
        } else {
            Toast.makeText(getApplication(), "Error retrieving event data: "+response.getError().getMessage(), Toast.LENGTH_LONG).show();

        }
    }
}
