package com.example.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.media3.common.MediaItem;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import io.pinecone.clients.Index;
import io.pinecone.clients.Inference;
import io.pinecone.clients.Pinecone;
import io.pinecone.unsigned_indices_model.QueryResponseWithUnsignedIndices;
import io.pinecone.unsigned_indices_model.ScoredVectorWithUnsignedIndices;

import org.openapitools.inference.client.ApiException;
import org.openapitools.inference.client.model.Embedding;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.datastore.AWSDataStorePlugin;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.datastore.generated.model.Video;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;


public class SearchFragment extends Fragment {
    private TextView idTextView, videoTitle, videoDescription;
    private ImageView videoThumbnail;
    private RecyclerView recyclerView;
    private List<String> videoIds = Arrays.asList("id1", "id2", "id3"); // Example IDs
    private RecyclerView recyclerRealTime;
    private EditText searchBox;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        EditText searchBox = view.findViewById(R.id.searchBox);
        Button searchButton = view.findViewById(R.id.searchButton);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchText = searchBox.getText().toString().trim();
                recyclerRealTime.setVisibility(View.GONE);
                if (!searchText.isEmpty()) {
                    generateEmbedding(searchText);
                }
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EditText searchBox = view.findViewById(R.id.searchBox);
        recyclerRealTime = view.findViewById(R.id.recyclerRealTime);

        if (recyclerRealTime != null) {
            recyclerRealTime.setLayoutManager(new LinearLayoutManager(getContext()));
        } else {
            Log.e("SearchFragment", "RecyclerView is null");
        }

        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
                // No action needed here
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence.length() > 0) {
                    recyclerRealTime.setVisibility(View.VISIBLE);
                    Log.d("%%%%%%%%%%%%", charSequence.toString());
                    fetchTitlesFromDynamoDB(charSequence.toString());
                }
                else {
                    recyclerRealTime.setVisibility(View.GONE); // Keep RecyclerView hidden if no text
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // No action needed here
            }
        });
    }


    // Fetch titles from DynamoDB based on search query
    private void fetchTitlesFromDynamoDB(String query) {

        Log.d("SearchFragment", "Fetching titles for query: " + query);

        // Query DynamoDB to fetch video titles
        Amplify.API.query(
                ModelQuery.list(Video.class, Video.INPUT_TEXT.contains(query)), // Assuming you are searching based on title
                response -> {
                    List<String> titles = new ArrayList<>();
                    for (Video video : response.getData()) {
                        // Add the video title to the list
                        titles.add(video.getInputText());
                    }
                    // Log each title in Logcat
                    for (String title : titles) {
                        Log.d("SearchFragment", "Fetched Title: " + title);

                    }
                    updateRecyclerView( titles);
                },
                error -> Log.e("SearchFragment", "Failed to fetch titles: " + error.getMessage())
        );

    }


    private void updateRecyclerView(List<String> titles) {
        if (recyclerRealTime != null) {
            getActivity().runOnUiThread(() -> {
                VideoTitleAdapter adapter = new VideoTitleAdapter(titles);
                recyclerRealTime.setAdapter(adapter);
            });
            Log.d("###########",titles.toArray().toString());
        } else {
            Log.e("SearchFragment", "RecyclerView is null when updating");
        }
    }
    

    private void generateEmbedding(String inputText) {
        // Start a background thread to prevent blocking UI

//        progressBar.setVisibility(View.VISIBLE);
//
//        new Handler().postDelayed(() -> {
//            progressBar.setVisibility(View.GONE);

        new Thread(() -> {
            try {
                // Initialize Pinecone client
                Pinecone pc = new Pinecone.Builder("pcsk_5z4ZCf_JiczxACWK2p4Du6HJUvLs9zKkqg175vQTDi7nT6w8kHsPKnueP15y9KL9CkXVa4").build(); // Ensure your API key is correct
                Inference inference = pc.getInferenceClient();

                // Prepare the input data
                List<DataObj> data = Arrays.asList(new DataObj("vec1", inputText));
                List<String> inputs = data.stream().map(DataObj::getText).collect(Collectors.toList());

                // Set the embedding model and parameters
                String embeddingModel = "multilingual-e5-large";
                Map<String, Object> parameters = new HashMap<>();
                parameters.put("input_type", "passage");
                parameters.put("truncate", "END");

                // Generate embeddings
                List<Embedding> embeddingsList = inference.embed(embeddingModel, parameters, inputs).getData();

                Index index = pc.getIndexConnection("vector-of-title");

                List<String> query = Collections.singletonList(inputText);


                Map<String, Object> queryParameters = new HashMap<>();
                queryParameters.put("input_type", "query");
                queryParameters.put("truncate", "END");

                List<Embedding> queryVector = inference.embed(embeddingModel, queryParameters, query).getData();

                QueryResponseWithUnsignedIndices queryResponse = index.query(5, convertBigDecimalToFloat(queryVector.get(0).getValues()), null, null, null, "example-namespace", null, true, false);

                List<String> idList = new ArrayList<>();

                for (int i = 0; i < 5; i++) { // Assuming getMatchesCount() exists
                    ScoredVectorWithUnsignedIndices match = queryResponse.getMatches(i);
                    if (match != null) {
                        String matchId = match.getId();
                        idList.add(matchId);
                        Log.d("IDS", matchId);

                        requireActivity().runOnUiThread(() -> Toast.makeText(requireContext(), "Vector Conversion Successful", Toast.LENGTH_SHORT).show());
                    }
                }

                queryVideosByBatch(idList, recyclerView, requireContext());


            } catch (Exception e) {
                Log.e("Exception", e.getMessage());
                requireActivity().runOnUiThread(() -> Toast.makeText(requireContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }
        }).start();
//        }, 2000); // 2-second delay
    }

    public static List<Float> convertBigDecimalToFloat(List<BigDecimal> values) {
        List<Float> result = new ArrayList<>();
        for (BigDecimal value : values) {
            result.add(value.floatValue());
        }
        return result;
    }

    public void queryVideosByBatch(List<String> todoIds, RecyclerView recyclerView, Context context) {
        Log.d("AmplifyBatchQuery", "Querying multiple Todo IDs: " + todoIds);

        if (todoIds == null || todoIds.isEmpty()) {
            Log.e("AmplifyBatchQuery", "ID List is empty!");
            return;
        }

        // HashSet for faster lookup (O(1) complexity)
        Set<String> idSet = new HashSet<>(todoIds);
        List<VideoItem> videoItems = new ArrayList<>();
        Map<String, VideoItem> videoMap = new HashMap<>();

        // Fetch Videos from Amplify API
        Amplify.API.query(
                ModelQuery.list(Video.class),
                response -> {
                    for (Video video : response.getData()) {
                        if (idSet.contains(video.getId())) {
                            VideoItem item = new VideoItem(
                                    video.getInputText(),
                                    video.getDescription(),
                                    video.getThumbnailUrl(),
                                    video.getVideoUrl()
                            );
                            videoMap.put(video.getId(), item);
                        }
                    }

                    // Reorder results based on original `todoIds`
                    for (String id : todoIds) {
                        if (videoMap.containsKey(id)) {
                            videoItems.add(videoMap.get(id));
                        }
                    }

                    // Update RecyclerView on UI Thread
                    if (recyclerView != null && context != null) {
                        requireActivity().runOnUiThread(() -> {
                            if (videoItems.isEmpty()) {
                                Log.d("AmplifyBatchQuery", "No videos found");
                            } else {
                                Log.d("DynamoDb", "Fetched Video Items: " + videoItems);
                                recyclerView.setLayoutManager(new LinearLayoutManager(context));
                                recyclerView.setAdapter(new VideoAdapter(requireActivity(), videoItems));

                            }
                        });
                    }
                },
                error -> Log.e("AmplifyBatchQuery", "Query failed", error)
        );
    }



    private void playVideoInNewActivity() {
        // Get the video URL from the thumbnail's tag
        String videoUrl = (String) videoThumbnail.getTag();

        if (videoUrl != null) {
            // Pass the video URL to the new activity
            Intent intent = new Intent(getActivity(), VideoPlayerActivity.class);
            intent.putExtra("videoUrl", videoUrl);
            startActivity(intent);
        }
    }
}

class DataObj {
    private String id;
    private String text;

    public DataObj(String id, String text) {
        this.id = id;
        this.text = text;
    }

    public String getId() {
        return id;
    }
    public String getText() {
        return text;
    }
}
