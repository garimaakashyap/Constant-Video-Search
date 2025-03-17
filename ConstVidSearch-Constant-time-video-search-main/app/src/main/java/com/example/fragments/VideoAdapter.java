package com.example.fragments;

import android.content.Context;
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
import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {
    private Context context;
    private List<VideoItem> videoItems;

    public VideoAdapter(Context context, List<VideoItem> videoItems) {
        this.context = context;
        this.videoItems = videoItems;
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_video, parent, false);
        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        VideoItem videoItem = videoItems.get(position);  // ✅ Define videoItem here
        holder.videoTitle.setText(videoItem.getTitle());


        // Load Thumbnail with Glide
        Glide.with(context).load(videoItem.getThumbnailUrl()).into(holder.videoThumbnail);

        // ✅ Use videoItem inside clickListener
        View.OnClickListener clickListener = v -> {
            Log.d("VideoAdapter", "Clicked Video URL: " + videoItem.getVideoUrl());

            if (videoItem.getVideoUrl() == null || videoItem.getVideoUrl().isEmpty()) {
                Log.e("VideoAdapter", "Error: videoUrl is null or empty");
                Toast.makeText(context, "Error: Video URL is missing", Toast.LENGTH_SHORT).show();
                return;  // Prevent crash
            }

            // Create an intent to open VideoPlayerActivity
            Intent intent = new Intent(context, VideoPlayerActivity.class);

            // Create a Bundle to pass data
            Bundle bundle = new Bundle();
            bundle.putString("videoUrl", videoItem.getVideoUrl());
            bundle.putString("videoTitle", videoItem.getTitle());

            // Attach the Bundle to the intent
            intent.putExtras(bundle);

            // Start the activity
            context.startActivity(intent);
        };

        holder.videoThumbnail.setOnClickListener(clickListener);
        holder.videoTitle.setOnClickListener(clickListener);
    }

    @Override
    public int getItemCount() {
        return videoItems.size();
    }

    public static class VideoViewHolder extends RecyclerView.ViewHolder {
        ImageView videoThumbnail;
        TextView videoTitle;

        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);
            videoThumbnail = itemView.findViewById(R.id.videoThumbnail);
            videoTitle = itemView.findViewById(R.id.videoTitle);
        }
    }
}
