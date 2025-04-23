package com.example.cy_hill;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyRecyclerViewAdapterLeaderBoard extends RecyclerView.Adapter<MyRecyclerViewAdapterLeaderBoard.ViewHolder> {

    private List<LeaderBoard> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    MyRecyclerViewAdapterLeaderBoard(Context context, List<LeaderBoard> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // inflates the row layout from xml when needed
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.leaderboard_row, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView and handles menu actions
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        LeaderBoard item = mData.get(Math.max(0, position));

        holder.place.setText(String.valueOf(position + 1));
        holder.name.setText(item.getName());
        holder.score.setText("Current Score: " + item.getScore());
        holder.college.setText("College: " + item.getCollege());
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView place;
        TextView name;
        TextView score;
        TextView college;

        ViewHolder(View itemView) {
            super(itemView);
            place = itemView.findViewById(R.id.textView10);
            name = itemView.findViewById(R.id.textContent);
            score = itemView.findViewById(R.id.textView12);
            college = itemView.findViewById(R.id.textView13);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    LeaderBoard getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}