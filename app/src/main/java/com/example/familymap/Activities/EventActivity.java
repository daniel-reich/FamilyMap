package com.example.familymap.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.familymap.Fragments.MapFragment;
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
        getSupportActionBar().setTitle("FamilyMap: Event");
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        startTopActivity(this, false);
        return true;
    }

    public static void startTopActivity(Context context, boolean newInstance) {
        Intent intent = new Intent(context, MainActivity.class);
        if (newInstance) {
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }
        else {
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }
        context.startActivity(intent);
    }
}
