package com.example.familymap.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.familymap.Models.DataModel;
import com.example.familymap.Models.Event;
import com.example.familymap.Models.Gender;
import com.example.familymap.Models.Person;
import com.example.familymap.Models.PersonEventListItem;
import com.example.familymap.R;

import java.util.ArrayList;

public class PersonActivity extends AppCompatActivity {

    String personId;
    ImageButton eventExpandButton;
    ImageButton familyExpandButton;
    boolean displayEvents = false;
    boolean displayFamily = false;
    TextView firstName;
    TextView lastName;
    TextView gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);
        getSupportActionBar().setTitle("FamilyMap: Person Details");

        eventExpandButton = findViewById(R.id.event_list_item_expand_arrow);
        familyExpandButton = findViewById(R.id.family_list_item_expand_arrow);
        firstName = findViewById(R.id.person_first_name);
        lastName = findViewById(R.id.person_last_name);
        gender = findViewById(R.id.person_gender);

        if (getIntent().hasExtra("PERSON_ID")) {
            personId = getIntent().getExtras().getString("PERSON_ID");
        }

        Person person = DataModel.getInstance().getMasterPersonList().get(personId);
        firstName.setText(person.getFirstName());
        lastName.setText(person.getLastName());

        if (person.getGender() == Gender.f) {
            gender.setText("Female");
        } else {
            gender.setText("Male");
        }

        ArrayList<Event> personEvents =  new ArrayList<>();
        if (DataModel.getInstance().getDisplayPersonEvent().get(personId) != null) {
            personEvents = DataModel.getInstance().getDisplayPersonEvent().get(personId);
        }

        ArrayList<PersonEventListItem> eventItems = DataModel.getInstance().convertEventsToListItems(
                personEvents, this);



        ArrayList<PersonEventListItem> familyItems = DataModel.getInstance().getFamilyItems(personId, this);

        final RecyclerView eventList = findViewById(R.id.collapsible_event_recycle_view);
        eventList.setLayoutManager(new LinearLayoutManager(this));

        final RecyclerView familyList = findViewById(R.id.collapsible_family_recycle_view);
        familyList.setLayoutManager(new LinearLayoutManager(this));

        EventAdapter eventAdapter = new EventAdapter(eventItems);
        eventList.setAdapter(eventAdapter);
        eventList.setVisibility(View.GONE);

        EventAdapter familyAdapter = new EventAdapter(familyItems);
        familyList.setAdapter(familyAdapter);
        familyList.setVisibility(View.GONE);

        eventExpandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (displayEvents) {
                    eventList.setVisibility(View.GONE);
                    displayEvents = false;
                    float deg = eventExpandButton.getRotation() + 180F;
                    eventExpandButton.animate().rotation(deg).setInterpolator(new AccelerateDecelerateInterpolator());
                } else {
                    eventList.setVisibility(View.VISIBLE);
                    displayEvents = true;
                    float deg = eventExpandButton.getRotation() + 180F;
                    eventExpandButton.animate().rotation(deg).setInterpolator(new AccelerateDecelerateInterpolator());
                }
            }
        });

        familyExpandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (displayFamily) {
                    familyList.setVisibility(View.GONE);
                    displayFamily = false;
                    float deg = familyExpandButton.getRotation() + 180F;
                    familyExpandButton.animate().rotation(deg).setInterpolator(new AccelerateDecelerateInterpolator());
                } else {
                    familyList.setVisibility(View.VISIBLE);
                    displayFamily = true;
                    float deg = familyExpandButton.getRotation() + 180F;
                    familyExpandButton.animate().rotation(deg).setInterpolator(new AccelerateDecelerateInterpolator());
                }
            }
        });
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

    private class EventViewHolder extends RecyclerView.ViewHolder {

        TextView textTop;
        TextView textBottom;
        ImageView icon;
        LinearLayout row;

        public EventViewHolder(View view) {
            super(view);
            textTop = view.findViewById(R.id.person_event_list_item_top);
            textBottom = view.findViewById(R.id.person_event_list_item_bottom);
            icon = view.findViewById(R.id.person_event_list_item_icon);
            row = view.findViewById(R.id.person_event_list_item_container);
        }
    }

    private class EventAdapter extends RecyclerView.Adapter<PersonActivity.EventViewHolder> {
        private ArrayList<PersonEventListItem> items;

        public EventAdapter(ArrayList<PersonEventListItem> items) {
            this.items = items;
        }

        @Override
        public PersonActivity.EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

            return new PersonActivity.EventViewHolder(layoutInflater.inflate(R.layout.person_event_list_item, parent, false));
        }

        @Override
        public void onBindViewHolder(PersonActivity.EventViewHolder holder, final int position) {
            PersonEventListItem item  = items.get(position);
            holder.textTop.setText(item.getTopText());
            holder.textBottom.setText(item.getBottomText());
            holder.icon.setImageDrawable(item.getIcon());
            holder.row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PersonEventListItem selected = items.get(position);
                    if (selected.getType().equals("person")) {
                        Intent intent = new Intent(PersonActivity.this, PersonActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("PERSON_ID", selected.getId());
                        intent.putExtras(bundle);
                        startActivity(intent);
                    } else if (selected.getType().equals("event")) {
                        Intent intent = new Intent(PersonActivity.this, EventActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("EVENT_ID", selected.getId());
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
                return items.size();
            }
    }
}

