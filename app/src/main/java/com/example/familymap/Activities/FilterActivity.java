package com.example.familymap.Activities;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.example.familymap.Models.DataModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.example.familymap.R;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import java.util.ArrayList;

public class FilterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        RecyclerView filterList = findViewById(R.id.filter_recycle_view);
        filterList.setLayoutManager(new LinearLayoutManager(this));

        FilterAdapter adapter = new FilterAdapter(
                new ArrayList<String>(DataModel.getInstance().getFilterDictionary().keySet()));
        filterList.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
//        this.menu = menu;
//        MenuItem settingsItem = menu.findItem(R.id.menu_settings);
//        MenuItem searchItem = menu.findItem(R.id.menu_search);
//        MenuItem filterItem = menu.findItem(R.id.menu_filter);
//        Drawable gearIcon = new IconDrawable(this, FontAwesomeIcons.fa_gear).
//                colorRes(R.color.white).sizeDp(40);
//
//        settingsItem.setIcon(gearIcon);
//
//        Drawable searchIcon = new IconDrawable(this, FontAwesomeIcons.fa_search).
//                colorRes(R.color.white).sizeDp(40);
//
//        searchItem.setIcon(searchIcon);
//
//        Drawable filterIcon = new IconDrawable(this, FontAwesomeIcons.fa_filter).
//                colorRes(R.color.white).sizeDp(40);
//
//        filterItem.setIcon(filterIcon);
//        setMenuIconsVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    private class FilterViewHolder extends RecyclerView.ViewHolder {

        TextView text;
        Switch toggleSwitch;
        public FilterViewHolder(View view) {
            super(view);
            text = view.findViewById(R.id.filter_item_label);
            toggleSwitch = view.findViewById(R.id.filter_item_toggle);
        }
    }

    private class FilterAdapter extends RecyclerView.Adapter<FilterViewHolder> {
        private ArrayList<String> filters;

        public FilterAdapter(ArrayList<String> filters) {
            this.filters = filters;
        }

        @Override
        public FilterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

            return new FilterViewHolder(layoutInflater.inflate(R.layout.filter_list_item, parent, false));
        }

        @Override
        public void onBindViewHolder(FilterViewHolder holder, final int position) {
            String filter = filters.get(position);
            holder.text.setText("Display "+filter);
            holder.toggleSwitch.setChecked(DataModel.getInstance().getCurrentFilterSettings().get(filter));
            holder.toggleSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    DataModel.getInstance().getCurrentFilterSettings().put(filters.get(position), b);
                }
            });
        }

        @Override
        public int getItemCount() {
            return filters.size();
        }
    }
}
