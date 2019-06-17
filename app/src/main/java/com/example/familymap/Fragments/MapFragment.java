package com.example.familymap.Fragments;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.familymap.Activities.PersonActivity;
import com.example.familymap.Models.DataModel;
import com.example.familymap.Models.Event;
import com.example.familymap.Models.Gender;
import com.example.familymap.Models.Person;
import com.example.familymap.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import java.util.ArrayList;

public class MapFragment extends Fragment implements OnMapReadyCallback {
    private GoogleMap mMap;
    private ArrayList<Polyline> linesToDelete;

    private ImageView icon;
    private TextView name;
    private TextView eventAndYear;
    private TextView eventLocation;
    private LinearLayout selectedEvent;
    private String selectedEventId;
    Drawable questionIcon;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.map_fragment, container, false);
        icon = view.findViewById(R.id.male_female_icon);
        name = view.findViewById(R.id.person_name);
        eventAndYear = view.findViewById(R.id.event_and_year);
        eventLocation = view.findViewById(R.id.event_location);
        selectedEvent = view.findViewById(R.id.selected_map_event);

        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey("EVENT_ID")) {
            selectedEventId = bundle.getString("EVENT_ID");
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment == null) {
            FragmentManager frag = getFragmentManager();
            FragmentTransaction transaction = frag.beginTransaction();
            mapFragment = SupportMapFragment.newInstance();
            transaction.replace(R.id.map, mapFragment).commit();
        }

        questionIcon = new IconDrawable(getContext(), FontAwesomeIcons.fa_question).
                colorRes(R.color.black).sizeDp(40);

        icon.setImageDrawable(questionIcon);
        eventAndYear.setText("Click on a marker to see event details");

        mapFragment.getMapAsync(this);

        linesToDelete = new ArrayList<>();

        selectedEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedEventId != null) {
                    Intent intent = new Intent(getContext(), PersonActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("PERSON_ID", DataModel.getInstance().getMasterEventList().get(selectedEventId).getPersonId());
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });
        return view;
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(DataModel.getInstance().getMapType());
        drawMapMarkers();
        if (selectedEventId != null) {
            Event event = DataModel.getInstance().getMasterEventList().get(selectedEventId);
            LatLng location = new LatLng(event.getLatitude(), event.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
            onMarkerClicked();
        }

    }

    private void drawMapMarkers() {
        for (Event event: DataModel.getInstance().getDisplayEvents()) {
            LatLng place = new LatLng(event.getLatitude(), event.getLongitude());
            float color = DataModel.getInstance().getMarkerColors().get(event.getEventType().toLowerCase());
            Marker marker = mMap.addMarker(new MarkerOptions().position(place).icon(BitmapDescriptorFactory.defaultMarker(color)));
            marker.setTag(event.getId());
        }

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                selectedEventId = (String) marker.getTag();
                onMarkerClicked();
                return false;
            }
        });
    }

    private void onMarkerClicked() {
        drawMapLines(DataModel.getInstance().calculateMapLines(selectedEventId));
        Event event = DataModel.getInstance().getMasterEventList().get(selectedEventId);
        Person person = DataModel.getInstance().getMasterPersonList().get(event.getPersonId());
        if (person.getGender() == Gender.f) {
            Drawable femaleIcon = new IconDrawable(getContext(), FontAwesomeIcons.fa_female).
                    colorRes(R.color.female_color).sizeDp(40);
            icon.setImageDrawable(femaleIcon);
        } else {
            Drawable femaleIcon = new IconDrawable(getContext(), FontAwesomeIcons.fa_male).
                    colorRes(R.color.male_color).sizeDp(40);
            icon.setImageDrawable(femaleIcon);
        }
        name.setText(person.getFirstName()+" "+person.getLastName());
        String eventText = event.getEventType();
        if (event.getYear() != 0) eventText = eventText+" ("+event.getYear()+")";
        eventAndYear.setText(eventText);
        eventLocation.setText(event.getCity()+", "+event.getCountry());
    }

    private void drawMapLines(ArrayList<PolylineOptions> lines) {
        for (Polyline line: linesToDelete) {
            line.remove();
        }
        for(PolylineOptions line: lines) {
            Polyline polyline = mMap.addPolyline(line);
            linesToDelete.add(polyline);
        }
    }

    @Override
    public void onResume() {
        if (mMap!=null) {
            drawMapMarkers();
            mMap.setMapType(DataModel.getInstance().getMapType());
        }
        icon.setImageDrawable(questionIcon);
        eventAndYear.setText("Click on a marker to see event details");
        name.setText("");
        eventLocation.setText("");
        super.onResume();

    }

    @Override
    public void onPause() {
        mMap.clear();
        super.onPause();
    }
}
