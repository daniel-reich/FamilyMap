package com.example.familymap.Fragments;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.familymap.Models.DataModel;
import com.example.familymap.Models.Event;
import com.example.familymap.Models.Gender;
import com.example.familymap.Models.Person;
import com.example.familymap.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.map_fragment, container, false);
        icon = view.findViewById(R.id.male_female_icon);
        name = view.findViewById(R.id.person_name);
        eventAndYear = view.findViewById(R.id.event_and_year);
        eventLocation = view.findViewById(R.id.event_location);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment == null) {
            FragmentManager frag = getFragmentManager();
            FragmentTransaction transaction = frag.beginTransaction();
            mapFragment = SupportMapFragment.newInstance();
            transaction.replace(R.id.map, mapFragment).commit();
        }

        icon.setImageResource(R.drawable.ic_launcher_foreground);
        eventAndYear.setText("Choose an event");

        mapFragment.getMapAsync(this);

        linesToDelete = new ArrayList<>();
        return view;
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        drawMapMarkers();

    }

    private void drawMapMarkers() {
        for (Event event: DataModel.getInstance().getDisplayEvents()) {
            LatLng place = new LatLng(event.getLatitude(), event.getLongitude());
            Marker marker = mMap.addMarker(new MarkerOptions().position(place));
            marker.setTag(event.getId());
        }

//
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));


        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                drawMapLines(DataModel.getInstance().calculateMapLines((String)marker.getTag()));
                Event event = DataModel.getInstance().getMasterEventList().get((String)marker.getTag());
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
                return false;
            }
        });
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
        }
        super.onResume();

    }

    @Override
    public void onPause() {
        mMap.clear();
        super.onPause();
    }
}
