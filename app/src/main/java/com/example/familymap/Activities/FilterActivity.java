package com.example.familymap.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.familymap.Models.DataModel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.example.familymap.R;

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

        getSupportActionBar().setTitle("FamilyMap: Filter");
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
