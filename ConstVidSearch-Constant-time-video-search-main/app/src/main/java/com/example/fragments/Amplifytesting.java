package com.example.fragments;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.api.graphql.model.ModelMutation;

import com.amplifyframework.datastore.generated.model.Video;  // Correct import

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Amplifytesting extends AppCompatActivity {

    private TextView resultTextView;
    Button queryButton;
    private Button addUserButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amplifytesting);

        addUserButton = findViewById(R.id.addUserButton);
        queryButton = findViewById(R.id.queryButton);

        resultTextView = findViewById(R.id.resultTextView); // TextView to display query result


        // Initialize Amplify
        try {
            Amplify.addPlugin(new AWSCognitoAuthPlugin());
            Amplify.addPlugin(new AWSApiPlugin());
            Amplify.configure(getApplicationContext());
            Log.i("Amplify", "Initialized Amplify");
        } catch (AmplifyException e) {
            Log.e("Amplify", "Initialization failed", e);
        }

        Button addUserButton = findViewById(R.id.addUserButton);
        addUserButton.setOnClickListener(v -> addVideo());

        Button queryButton = findViewById(R.id.queryButton);
        queryButton.setOnClickListener(v -> queryVideoById( "fd656b73-6a63-43ed-89e1-acc845a87cb5"));
    }

    public void queryVideoById(String videoId) {
        Amplify.API.query(
                ModelQuery.get(Video.class, videoId),  // Querying using the 'unique_id' as the ID
                response -> {
                    if (response.hasData()) {
                        Video fetchedVideo = response.getData();
                        runOnUiThread(() -> {
                            String result = "Unique ID: " + fetchedVideo.getUniqueId() // Adjusted field name
                                    + "\nInput Text: " + fetchedVideo.getInputText()
                                    + "\nDescription: " + fetchedVideo.getDescription()
                                    + "\nThumbnail URL: " + fetchedVideo.getThumbnailUrl()
                                    + "\nVideo URL: " + fetchedVideo.getVideoUrl()
//                                    + "\nUploading Time: " + fetchedVideo.getUploading_time()
                                    + "\n\n";
                            resultTextView.append(result);  // Append to display all results
                            Log.d("!!!!!!!!!!!!!!!!!!!",result);
                        });
                    }
                },
                error -> Log.e("AmplifyQuery", "Query failed for ID: " + videoId, error)
        );
    }

    public void addVideo() {

        Video video = Video.builder()
                .uniqueId("unique-id-300")
                .inputText("Sample Video")
                .description("This is a sample video")
                .thumbnailUrl("https://example.com/thumbnail.jpg")
                .videoUrl("https://example.com/video.mp4")
                .uploadingTime("2025-02-05")
                .build();

        Amplify.API.mutate(
                ModelMutation.create(video),
                response -> {

                    String partitionKey = response.getData().getId(); // Replace 'getId()' with the actual getter for your partition key
                    Log.i("AmplifyAddVideo", "Video added with Partition Key (ID): " + partitionKey);
                },
                error -> Log.e("AmplifyAddVideo", "Failed to add video", error)
        );
    }



    public void queryVideosByIds(List<String> videoIds) {
        if (videoIds == null || videoIds.isEmpty()) {
            resultTextView.setText("No Video IDs provided");
            return;
        }

        List<Video> fetchedVideos = new ArrayList<>();

        for (String videoId : videoIds) {
            Amplify.API.query(
                    ModelQuery.get(Video.class, videoId),  // Querying using the 'unique_id' as the ID
                    response -> {
                        if (response.hasData()) {
                            Video fetchedVideo = response.getData();
                            fetchedVideos.add(fetchedVideo);
                        }

                        // When all queries are done, update UI
                        if (fetchedVideos.size() == videoIds.size()) {
                            runOnUiThread(() -> {
                                StringBuilder result = new StringBuilder();
                                for (Video video : fetchedVideos) {
                                    result.append("Unique ID: ").append(video.getUniqueId()) // Adjusted field name
                                            .append("\nInput Text: ").append(video.getInputText())
                                            .append("\nDescription: ").append(video.getDescription())
                                            .append("\nThumbnail URL: ").append(video.getThumbnailUrl())
                                            .append("\nVideo URL: ").append(video.getVideoUrl())
//                                            .append("\nUploading Time: ").append(video.getUploading_time())
                                            .append("\n\n");
                                }
                                resultTextView.setText(result.toString());
                            });
                        }
                    },
                    error -> Log.e("AmplifyQuery", "Query failed for ID: " + videoId, error)
            );
        }
    }
}
