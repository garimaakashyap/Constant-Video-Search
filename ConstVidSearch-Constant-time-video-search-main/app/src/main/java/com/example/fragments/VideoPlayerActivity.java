package com.example.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;
import androidx.media3.common.MediaItem;

public class VideoPlayerActivity extends AppCompatActivity {

    private PlayerView playerView;
    private ExoPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        playerView = findViewById(R.id.player_view);

        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                String videoUrl = bundle.getString("videoUrl");
                String videoTitle = bundle.getString("videoTitle");

                player = new ExoPlayer.Builder(this).build();
                playerView.setPlayer(player);

                MediaItem mediaItem = MediaItem.fromUri(videoUrl);
                player.setMediaItem(mediaItem);

                // Prepare the player
                player.prepare();
                // Start playback
                player.play();

                // Request focus for Android TV D-pad navigation
                playerView.requestFocus();

                if (videoTitle != null) {
                    TextView titleTextView = findViewById(R.id.videoTitleTextView);
                    titleTextView.setText(videoTitle);
                } else {
                    Log.e("VideoPlayerActivity", "Video Title is null");
                }

                if (videoUrl != null && !videoUrl.isEmpty()) {
                    Log.d("VideoPlayerActivity", "Video URL: " + videoUrl);
                } else {
                    Log.e("VideoPlayerActivity", "Video URL is null or empty");
                }
            } else {
                Log.e("VideoPlayerActivity", "No data received in the bundle");
            }
        } else {
            Log.e("VideoPlayerActivity", "Intent is null");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Stop playback and release the player when the activity is paused
        if (player != null) {
            player.pause();  // Pause the video if it's playing
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Release the player when the activity is stopped
        if (player != null) {
            player.release();  // Release resources held by ExoPlayer
            player = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Ensure the player is released when the activity is destroyed
        if (player != null) {
            player.release();
            player = null;
        }
    }
}
