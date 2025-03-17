package com.example.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Video;


import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private VideoListAdapter videoListAdapter;
    private List<VideoData> videoList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        videoListAdapter = new VideoListAdapter(videoList);
        recyclerView.setAdapter(videoListAdapter);

        fetchVideos();

        return view;
    }

    private void fetchVideos() {
        Amplify.API.query(
                ModelQuery.list(Video.class),  // Fetch all videos
                response -> {
                    List<VideoData> videoData = new ArrayList<>();

                    for (Video video : response.getData()) {
                        VideoData item = new VideoData(
                                video.getInputText(),
                                video.getDescription(),
                                video.getThumbnailUrl(),
                                video.getVideoUrl()
                        );
                        videoData.add(item);
                    }

                    Log.d("DynamoDb", "Fetched Video Items: " + videoData);

                    // Update RecyclerView on UI Thread
                    requireActivity().runOnUiThread(() -> {
                        if (videoData.isEmpty()) {
                            Log.d("DynamoDb", "No videos found");
                        } else {
                            Log.d("DynamoDb", "Fetched Vdeo Items: " + videoData.size());
                            recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
                            recyclerView.setAdapter(new VideoListAdapter(videoData));  // Corrected here
                        }
                    });
                },
                error -> Log.e("DynamoDb", "Query failed", error)
        );
    }

}
