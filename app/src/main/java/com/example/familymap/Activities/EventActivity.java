package com.example.familymap.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.familymap.Fragments.MapFragment;
import com.example.familymap.Models.DataModel;
import com.example.familymap.Models.Event;
import com.example.familymap.R;

public class EventActivity extends AppCompatActivity {

    private String eventId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        if (getIntent().hasExtra("EVENT_ID")) {
            eventId = getIntent().getExtras().getString("EVENT_ID");
        }

        FragmentManager frag = getSupportFragmentManager();
        FragmentTransaction transaction = frag.beginTransaction();
        MapFragment mapFragment = new MapFragment();
        Bundle bundle = new Bundle();
        bundle.putString("EVENT_ID", eventId);
        mapFragment.setArguments(bundle);
        transaction.add(R.id.event_activity_map_container, mapFragment).commit();
        //setMenuIconsVisible(true);
    }
}
