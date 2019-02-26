package com.TP2.game.android;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private List<Trial> data;
    private LayoutInflater inflater;


    // data is passed into the constructor
    RecyclerViewAdapter(Context context, List<Trial> data) {
        this.inflater = LayoutInflater.from(context);
        this.data = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recyclerview_row, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Trial ti = data.get(position);
        holder.Trial.setText(MainActivity.resources.getString(R.string.trial, ti.trial));
        holder.Details.setText(MainActivity.resources.getString(R.string.difficulty_time, ti.difficulty, ti.time));

    }

    // total number of rows
    @Override
    public int getItemCount() {
        return data.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView Trial;
        TextView Details;

        ViewHolder(View itemView) {
            super(itemView);
            Trial = itemView.findViewById(R.id.TrialNumber);
            Details = itemView.findViewById(R.id.DifficultyAndTime);
        }

    }

}
