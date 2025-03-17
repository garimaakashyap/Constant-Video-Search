package com.example.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fragments.R;

import java.util.List;

public class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.VideoViewHolder> {
    private List<VideoData> videoList;

    public VideoListAdapter(List<VideoData> videoList) {
        Log.d("Inside Adapter", "Fetched Video Items: " + videoList.size());
        this.videoList = videoList;
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video_card, parent, false);
        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        VideoData video = videoList.get(position);
        holder.titleTextView.setText(video.getInputText());
        holder.descriptionTextView.setText(video.getDescription());

        // Load the thumbnail image using Glide
        Glide.with(holder.thumbnailImageView.getContext())
                .load(video.getThumbnailUrl())  // Assume thumbnailUrl is the image URL in VideoData
                .placeholder(R.drawable.ic_home)  // Default placeholder image
                .into(holder.thumbnailImageView);

        View.OnClickListener clickListener = v -> {
            Log.d("HomeAdapter", "Clicked Video URL: " + video.getVideoUrl());

            if (video.getVideoUrl() == null || video.getVideoUrl().isEmpty()) {
                Log.e("HomeAdapter", "Error: videoUrl is null or empty");
                Toast.makeText(holder.itemView.getContext(), "Error: Video URL is missing", Toast.LENGTH_SHORT).show();
                return;
            }

            // Create an intent to open VideoPlayerActivity
            Intent intent = new Intent(holder.itemView.getContext(), VideoPlayerActivity.class);

            // Create a Bundle to pass data
            Bundle bundle = new Bundle();
            bundle.putString("videoUrl", video.getVideoUrl());
            bundle.putString("videoTitle", video.getInputText());

            // Attach the Bundle to the intent
            intent.putExtras(bundle);

            // Start the activity
            holder.itemView.getContext().startActivity(intent);

        };
        holder.thumbnailImageView.setOnClickListener(clickListener);
        holder.titleTextView.setOnClickListener(clickListener);
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    static class VideoViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, descriptionTextView;
        ImageView thumbnailImageView;

        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.videoTitle);
            descriptionTextView = itemView.findViewById(R.id.videoDescription);
            thumbnailImageView = itemView.findViewById(R.id.videoThumbnail);  // ImageView for thumbnail
        }
    }
}
