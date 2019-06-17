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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.familymap.Models.DataModel;
import com.example.familymap.Models.PersonEventListItem;
import com.example.familymap.R;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    EditText searchText;
    ImageButton searchButton;
    ImageButton clearButton;
    RecyclerView results;
    ArrayList<PersonEventListItem> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        searchText = findViewById(R.id.search_text);
        searchButton = findViewById(R.id.search_button);
        clearButton = findViewById(R.id.clear_button);
        results = findViewById(R.id.search_recycle_view);
        items = new ArrayList<>();
        results.setLayoutManager(new LinearLayoutManager(this));
        final EventAdapter searchAdapter = new EventAdapter(items);
        results.setAdapter(searchAdapter);

        getSupportActionBar().setTitle("FamilyMap: Search");

        searchButton.setImageDrawable(new IconDrawable(this, FontAwesomeIcons.fa_search).
                colorRes(R.color.black).sizeDp(30));

        clearButton.setImageDrawable(new IconDrawable(this, FontAwesomeIcons.fa_close).
                colorRes(R.color.black).sizeDp(30));


        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchString = searchText.getText().toString().toLowerCase();
                if (searchString != null && !searchString.equals("")) {
                    items.clear();
                    items.addAll(DataModel.getInstance().search(searchString, SearchActivity.this));
                    searchAdapter.notifyDataSetChanged();
                }
            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchText.setText("");
                items.clear();
                searchAdapter.notifyDataSetChanged();
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

    private class EventAdapter extends RecyclerView.Adapter<SearchActivity.EventViewHolder> {
        private ArrayList<PersonEventListItem> items;

        public EventAdapter(ArrayList<PersonEventListItem> items) {
            this.items = items;
        }

        @Override
        public SearchActivity.EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

            return new SearchActivity.EventViewHolder(layoutInflater.inflate(R.layout.person_event_list_item, parent, false));
        }

        @Override
        public void onBindViewHolder(SearchActivity.EventViewHolder holder, final int position) {
            PersonEventListItem item  = items.get(position);
            holder.textTop.setText(item.getTopText());
            holder.textBottom.setText(item.getBottomText());
            holder.icon.setImageDrawable(item.getIcon());
            holder.row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PersonEventListItem selected = items.get(position);
                    if (selected.getType().equals("person")) {
                        Intent intent = new Intent(SearchActivity.this, PersonActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("PERSON_ID", selected.getId());
                        intent.putExtras(bundle);
                        startActivity(intent);
                    } else if (selected.getType().equals("event")) {
                        Intent intent = new Intent(SearchActivity.this, EventActivity.class);
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
