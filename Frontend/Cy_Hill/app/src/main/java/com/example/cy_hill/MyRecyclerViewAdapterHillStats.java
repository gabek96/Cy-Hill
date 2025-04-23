package com.example.cy_hill;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyRecyclerViewAdapterHillStats extends RecyclerView.Adapter<MyRecyclerViewAdapterHillStats.ViewHolder> {

    private List<Hill> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    MyRecyclerViewAdapterHillStats(Context context, List<Hill> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // inflates the row layout from xml when needed
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_row, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView and handles menu actions
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        try {
            Hill item = mData.get(Math.max(0, position));

            holder.textHillName.setText(item.getName());
            holder.progressBar.setProgress(item.getProgress());
            Log.d("TOTAL PROGRESS", String.valueOf(item.getProgress()));
            holder.textCurrentLeader.setText("Leader: " + item.getLeader());
            holder.textTimeLeft.setText(item.getAllPoints() + "/" + item.getGoal());

            if (item.isExpanded()) {
                holder.RelativeLayout.setVisibility(View.VISIBLE);
            } else {
                holder.RelativeLayout.setVisibility(View.GONE);
            }

            holder.vetProgressBar.setProgress(item.getVetPoints());
            holder.gradProgressBar.setProgress(item.getGradPoints());
            holder.engProgressBar.setProgress(item.getEngPoints());
            holder.agProgressBar.setProgress(item.getAgPoints());
            holder.busProgressBar.setProgress(item.getBusPoints());
            holder.desProgressBar.setProgress(item.getDesPoints());
            holder.humProgressBar.setProgress(item.getHumPoints());
            holder.libProgressBar.setProgress(item.getLibPoints());
            holder.eduProgressBar.setProgress(item.getEduPoints());
        } catch (Exception e) {
            e.printStackTrace();
        }





    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textHillName;
        TextView textTimeLeft;
        TextView textCurrentLeader;
        ProgressBar progressBar;

        RelativeLayout RelativeLayout;
        ProgressBar vetProgressBar;
        ProgressBar gradProgressBar;
        ProgressBar engProgressBar;
        ProgressBar agProgressBar;
        ProgressBar busProgressBar;
        ProgressBar desProgressBar;
        ProgressBar humProgressBar;
        ProgressBar libProgressBar;
        ProgressBar eduProgressBar;


        ViewHolder(View itemView) {
            super(itemView);
            textHillName = itemView.findViewById(R.id.textHillName);
            textTimeLeft = itemView.findViewById(R.id.textTimeLeft);
            textCurrentLeader = itemView.findViewById(R.id.textCurrentLeader);
            progressBar = itemView.findViewById(R.id.progressBar);

            RelativeLayout = itemView.findViewById(R.id.progressBarLayout);
            vetProgressBar = itemView.findViewById(R.id.vetProgressBar);
            gradProgressBar = itemView.findViewById(R.id.gradProgressBar);
            engProgressBar = itemView.findViewById(R.id.EngProgressBar);
            agProgressBar = itemView.findViewById(R.id.agProgressBar);
            busProgressBar = itemView.findViewById(R.id.busProgressBar);
            desProgressBar = itemView.findViewById(R.id.desProgressBar);
            humProgressBar = itemView.findViewById(R.id.humProgressBar);
            libProgressBar = itemView.findViewById(R.id.libProgressBar);
            eduProgressBar = itemView.findViewById(R.id.eduProgressBar);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    Hill getItem(int id) {
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

