package com.example.fragments;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class VideoTitleAdapter extends RecyclerView.Adapter<VideoTitleAdapter.VideoViewHolder> {

    private List<String> titles;

    // Constructor
    public VideoTitleAdapter(List<String> titles) {
        System.out.println(titles.toArray().length);
        this.titles = titles;

    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for each item in RecyclerView
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_video_title, parent, false);
        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        // Set the title text for each item
        holder.titleTextView.setText(titles.get(position));

    }

    @Override
    public int getItemCount() {
        return titles.size();
    }

    // ViewHolder to hold references to the views in the item layout
    public static class VideoViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;

        public VideoViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
        }
    }
}
