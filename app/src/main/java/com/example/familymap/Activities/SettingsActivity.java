package com.example.familymap.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.example.familymap.MainActivity;
import com.example.familymap.Models.DataModel;
import com.example.familymap.Models.Event;
import com.example.familymap.Models.MapColors;
import com.example.familymap.Models.Person;
import com.example.familymap.R;
import com.example.familymap.RequestResponse.AllEventResponse;
import com.example.familymap.RequestResponse.AllPersonResponse;
import com.example.familymap.Tasks.GetAllEventsAsyncTask;
import com.example.familymap.Tasks.GetAllPersonsAsyncTask;

import java.util.ArrayList;
import java.util.HashMap;

public class SettingsActivity extends AppCompatActivity implements GetAllPersonsAsyncTask.PersonDataListener, GetAllEventsAsyncTask.EventDataListener{

    private Spinner lifeStoryColors;
    private Spinner familyTreeColors;
    private Spinner spouseColors;
    private Switch showLifeStory;
    private Switch showFamilyTree;
    private Switch showSpouse;
    private LinearLayout logout;
    private LinearLayout resync;

    ArrayList<Person> personData;
    ArrayList<Event> eventData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);



        lifeStoryColors = findViewById(R.id.life_story_colors);
        familyTreeColors = findViewById(R.id.family_tree_colors);
        spouseColors = findViewById(R.id.spouse_colors);
        showLifeStory = findViewById(R.id.life_story_switch);
        showFamilyTree = findViewById(R.id.family_tree_switch);
        showSpouse = findViewById(R.id.spouse_switch);
        logout = findViewById(R.id.logout);
        resync = findViewById(R.id.resync);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.color_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        lifeStoryColors.setAdapter(adapter);
        familyTreeColors.setAdapter(adapter);
        spouseColors.setAdapter(adapter);

        lifeStoryColors.setSelection(1);
        familyTreeColors.setSelection(3);
        spouseColors.setSelection(5);
        showSpouse.setChecked(DataModel.getInstance().isShowSpouse());
        showLifeStory.setChecked(DataModel.getInstance().isShowlifeStory());
        showFamilyTree.setChecked(DataModel.getInstance().isShowFamilyTree());

        lifeStoryColors.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                DataModel.getInstance().setLifeStoryColor(MapColors.valueOf((String) adapterView.getItemAtPosition(i)));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        familyTreeColors.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                DataModel.getInstance().setFamilyTreeColor(MapColors.valueOf((String) adapterView.getItemAtPosition(i)));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spouseColors.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                DataModel.getInstance().setSpouseColor(MapColors.valueOf((String) adapterView.getItemAtPosition(i)));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        showLifeStory.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                DataModel.getInstance().setShowlifeStory(b);
            }
        });

        showFamilyTree.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                DataModel.getInstance().setShowFamilyTree(b);
            }
        });

        showSpouse.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                DataModel.getInstance().setShowSpouse(b);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataModel.getInstance().reset();
                Intent login = new Intent(SettingsActivity.this, MainActivity.class);
                login.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(login);
            }
        });

        resync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eventData = null;
                personData = null;
                GetAllEventsAsyncTask getAllEvents = new GetAllEventsAsyncTask(SettingsActivity.this);
                getAllEvents.execute(DataModel.getInstance().getAuthToken());
                GetAllPersonsAsyncTask getAllPersonsAsyncTask = new GetAllPersonsAsyncTask(SettingsActivity.this);
                getAllPersonsAsyncTask.execute(DataModel.getInstance().getAuthToken());
            }
        });
    }

    @Override
    public void onGetPersonsResponse(AllPersonResponse response) {
        if (response.isSuccess()) {
            personData = response.getData();
            syncData();
        } else {
            Toast.makeText(getApplication(), "Error retrieving person data: "+response.getError().getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onGetEventsResponseRecieved(AllEventResponse response) {
        if (response.isSuccess()) {
            eventData = response.getData();
            syncData();
        } else {
            Toast.makeText(getApplication(), "Error retrieving event data: "+response.getError().getMessage(), Toast.LENGTH_LONG).show();

        }
    }

    private void syncData() {
        if (eventData!= null && personData !=null) {
            DataModel.getInstance().setMasterEventsList(null);
            DataModel.getInstance().setMasterPersonList(null);
            DataModel.getInstance().setMasterEventsList(eventData);
            DataModel.getInstance().setMasterPersonList(personData);
            Intent intent = new Intent(this, MainActivity.class);
            Bundle bundle = new Bundle();
            bundle.putBoolean("LOGGED_IN", true);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }
}
