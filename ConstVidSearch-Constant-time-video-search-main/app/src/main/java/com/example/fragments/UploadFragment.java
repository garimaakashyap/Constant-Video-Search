package com.example.fragments;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import org.conscrypt.Conscrypt;

import java.io.IOException;
import java.security.Security;

import io.pinecone.clients.Index;
import io.pinecone.clients.Inference;
import io.pinecone.clients.Pinecone;
import org.openapitools.inference.client.ApiException;
import org.openapitools.inference.client.model.Embedding;


import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.BooleanSupplier;
import java.util.stream.Collectors;
import java.util.UUID;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Callback;
import okhttp3.Protocol;

import java.util.Collections;
import java.lang.Float;

import java.util.HashMap;
import java.util.Map;

import okhttp3.*;

import com.amazonaws.regions.Region;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.*;
import java.util.concurrent.TimeUnit;


//import org.conscrypt.Conscrypt;
//import java.security.Security;
//import javax.net.ssl.SSLContext;
//import java.security.Provider;


import android.content.Context;
import android.util.Log;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;
import java.io.File;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.regions.Regions;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.datastore.generated.model.Video;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



public class UploadFragment extends Fragment {

    EditText editTextInput, editTextDescription, editTextVideo;
    Button btnPickThumbnail, btnPickVideo, btnSubmit;
    ImageView imageViewThumbnail;
    Uri thumbnailUri, videoUri;
    Boolean flag = true;
    private ActivityResultLauncher<Intent> thumbnailPickerLauncher;
    private ActivityResultLauncher<Intent> videoPickerLauncher;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_upload, container, false);

        init(view);

        btnSubmit.setOnClickListener(v -> {

            String inputText = editTextInput.getText().toString().trim();
            String description = editTextDescription.getText().toString().trim();
            String thumbnail = (thumbnailUri != null) ? thumbnailUri.toString() : "No thumbnail selected";
            String video = editTextVideo.getText().toString().trim();



            if (inputText.isEmpty()) {
                Toast.makeText(requireContext(), "Title cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }
            else if (description.isEmpty()) {
                Toast.makeText(requireContext(), "Description cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }
            else if (thumbnail.isEmpty()) {
                Toast.makeText(requireContext(), "Please Pick a Thumbnail", Toast.LENGTH_SHORT).show();
                return;
            }
            else if (video.isEmpty()) {
                Toast.makeText(requireContext(), "Please Pick a Video", Toast.LENGTH_SHORT).show();
                return;
            }

            Toast.makeText(requireContext(),
                    "Title: " + inputText + "\nDescription: " + description +
                            "\nThumbnail: " + thumbnail + "\nVideo: " + video,
                    Toast.LENGTH_LONG).show();


            //Generate uuid
            UUID uuid = UUID.randomUUID();
            String unique_id = uuid.toString();

            //Store in S3
             uploadInCloud(unique_id, inputText, description, thumbnail, video);


            // Initialize Amplify
            try {
                Amplify.addPlugin(new AWSCognitoAuthPlugin());
                Amplify.addPlugin(new AWSApiPlugin());
                Amplify.configure(requireActivity().getApplicationContext());
                Log.i("Amplify", "Initialized Amplify");
            } catch (AmplifyException e) {
                Log.e("Amplify", "Initialization failed", e);
            }


        });

        return view;
    }


    public void uploadFileToS3(Context context, String filePath, String s3Key, UploadCallback callback) {

        String BUCKET_NAME = "video-streaming-app-01";
        String COGNITO_POOL_ID = "ap-south-1:c88302d6-13bf-48ba-86ee-94cdc2d8f82d"; // Get from AWS Console
        Regions AWS_REGION = Regions.AP_SOUTH_1; // Change to your region


        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                context,
                COGNITO_POOL_ID, // Identity Pool ID
                AWS_REGION // Region
        );

        AmazonS3Client s3Client = new AmazonS3Client(credentialsProvider);
        TransferUtility transferUtility = TransferUtility.builder()
                .context(context)
                .s3Client(s3Client)
                .build();

        File file = new File(filePath);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    TransferObserver observer = transferUtility.upload(BUCKET_NAME, s3Key, file);

                    observer.setTransferListener(new TransferListener() {
                        @Override
                        public void onStateChanged(int id, TransferState state) {
                            Log.d("S3_UPLOAD", "Upload state: " + state);
                            if (state == TransferState.COMPLETED) {
                                String fileUrl = "https://video-streaming-app-01.s3.ap-south-1.amazonaws.com/" + s3Key;
                                // Upload completed, notify callback with the URL
                                callback.onUploadComplete(fileUrl);
                            } else if (state == TransferState.FAILED) {
                                Log.e("S3_UPLOAD", "Upload failed");
                            }
                        }

                        @Override
                        public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                            if (bytesTotal > 0) {
                                float progress = (100.0f * bytesCurrent) / bytesTotal;
                                Log.d("S3_UPLOAD", "Upload Progress: " + progress + "% (" + bytesCurrent + "/" + bytesTotal + ")");
                            }
                        }

                        @Override
                        public void onError(int id, Exception ex) {
                            Log.e("S3_UPLOAD", "Upload Error: " + ex.getMessage());
                        }
                    });
                } catch (Exception e) {
                    Log.e("UPLOAD", "Exception during upload: " + e.getMessage());
                }
            }
        }).start();
    }

    public static String getFilePathFromURI(Context context, Uri contentUri) {
        String filePath = null;

        String[] projection = { MediaStore.Video.Media.DATA };
        Cursor cursor = context.getContentResolver().query(contentUri, projection, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            filePath = cursor.getString(columnIndex);
            cursor.close();
        }
        return filePath;
    }


    private void uploadInCloud(String unique_id, String inputText, String description, String thumbnail, String video) {
        // Thumbnail setup
        Uri thumbnailURI = Uri.parse(thumbnail);
        String thumbnailPath = getFilePathFromURI(requireContext(), thumbnailURI);
        String thumbnailKey = "thumbnails/" + unique_id + ".jpg";

        // Video setup
        Uri VideoURI = Uri.parse(video);
        String videoPath = getFilePathFromURI(requireContext(), VideoURI);
        String videoKey = "videos/" + unique_id + ".mp4";

        // Counter to track the number of uploads completed
        final int[] uploadCount = {0};
        final String[] thumbnailUrl = new String[1];
        final String[] videoUrl = new String[1];

        // Upload thumbnail
        uploadFileToS3(requireContext(), thumbnailPath, thumbnailKey, new UploadCallback() {
            @Override
            public void onUploadComplete(String fileUrl) {
                Log.d("Thumbnail Upload", "File URL: " + fileUrl);
                thumbnailUrl[0] = fileUrl;
                uploadCount[0]++;
                checkUploadsComplete(unique_id, inputText, description, thumbnailUrl[0], videoUrl[0], uploadCount[0]);
            }
        });

        // Upload video
        uploadFileToS3(requireContext(), videoPath, videoKey, new UploadCallback() {
            @Override
            public void onUploadComplete(String fileUrl) {
                Log.d("Video Upload", "File URL: " + fileUrl);
                videoUrl[0] = fileUrl;
                uploadCount[0]++;
                checkUploadsComplete(unique_id, inputText, description, thumbnailUrl[0], videoUrl[0], uploadCount[0]);
            }
        });
    }


    private void checkUploadsComplete(String unique_id, String inputText, String description, String thumbnailUrl, String videoUrl, int uploadCount) {
        if (uploadCount == 2) {
            Toast.makeText(requireContext(), "Submission in S3 successfully", Toast.LENGTH_SHORT).show();

            // Passing callback to StoreInDynamodb
            StoreInDynamodb(unique_id, inputText, description, thumbnailUrl, videoUrl, new PartitionKeyCallback() {
                @Override
                public void onPartitionKeyReceived(String partitionKey) {
                    Log.i("PartitionKey", "Received Partition Key: " + partitionKey);

                    generateEmbedding(inputText, partitionKey);


                    // Run the code on the UI thread
                    new Handler(Looper.getMainLooper()).post(() -> {
                        Toast.makeText(requireContext(), "Vector submission successful\n" + partitionKey, Toast.LENGTH_SHORT).show();
                    });
                }

                @Override
                public void onError(String errorMessage) {
                    Log.e("PartitionKey", "Error: " + errorMessage);

                    // Run the error message on the UI thread
                    new Handler(Looper.getMainLooper()).post(() -> {
                        Toast.makeText(requireContext(), "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
                    });
                }
            });
        }
    }

    // Callback Interface
    public interface PartitionKeyCallback {
        void onPartitionKeyReceived(String partitionKey);
        void onError(String errorMessage);
    }

    // Method with callback
    public void StoreInDynamodb(String unique_id, String inputText, String description, String thumbnailUrl, String videoUrl, PartitionKeyCallback callback) {
        ZoneId indiaZone = ZoneId.of("Asia/Kolkata");
        LocalDateTime indiaTime = LocalDateTime.now(indiaZone);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String formattedTime = indiaTime.format(formatter);

        Video video = Video.builder()
                .uniqueId(unique_id)
                .inputText(inputText)
                .description(description)
                .thumbnailUrl(thumbnailUrl)
                .videoUrl(videoUrl)
                .uploadingTime(formattedTime)
                .build();

        Amplify.API.mutate(
                ModelMutation.create(video),
                response -> {
                    String partitionKey = response.getData().getId(); // Fetch partition key
                    Log.i("AmplifyAddVideo", "Video added with Partition Key (ID): " + partitionKey);

                    // Make sure callback is executed on UI thread
                    if (callback != null) {
                        new Handler(Looper.getMainLooper()).post(() -> {
                            Toast.makeText(requireContext(), "DynamoDb submission successful\n" + partitionKey, Toast.LENGTH_SHORT).show();
                        });
                        callback.onPartitionKeyReceived(partitionKey); // Pass partition key to callback
                    }
                },
                error -> {
                    Log.e("AmplifyAddVideo", "Failed to add video", error);

                    // Ensure callback is not null before calling onError
                    if (callback != null) {
                        callback.onError(error.getMessage()); // Handle error and pass to callback
                    }
                }
        );
    }


    private void generateEmbedding(String inputText, String unique_id) {
        // Start a background thread to prevent blocking UI

        new Thread(() -> {
            try {
                // Initialize Pinecone client
                Pinecone pc = new Pinecone.Builder("pcsk_5z4ZCf_JiczxACWK2p4Du6HJUvLs9zKkqg175vQTDi7nT6w8kHsPKnueP15y9KL9CkXVa4").build(); // Ensure your API key is correct
                Inference inference = pc.getInferenceClient();

                // Prepare the input data
                List<DataObject> data = Arrays.asList(new DataObject("vec1", inputText));
                List<String> inputs = data.stream().map(DataObject::getText).collect(Collectors.toList());

                // Set the embedding model and parameters
                String embeddingModel = "multilingual-e5-large";
                Map<String, Object> parameters = new HashMap<>();
                parameters.put("input_type", "passage");
                parameters.put("truncate", "END");

                // Generate embeddings
                List<Embedding> embeddingsList = inference.embed(embeddingModel, parameters, inputs).getData();

                //Insertion in Vector Pinecone
                Index index = pc.getIndexConnection("vector-of-title");

                for (int i = 0; i < embeddingsList.size(); i++) {
                    List<Float> embedded = convertBigDecimalToFloat(embeddingsList.get(i).getValues());
                    index.upsert(unique_id, embedded, "example-namespace");
                }


            } catch (Exception e) {
                flag = false;
                Log.e("Exception", e.getMessage());
                requireActivity().runOnUiThread(() -> Toast.makeText(requireContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }
        }).start();
    }

    public static List<Float> convertBigDecimalToFloat(List<BigDecimal> values) {
        List<Float> result = new ArrayList<>();
        for (BigDecimal value : values) {
            result.add(value.floatValue());
        }
        return result;
    }

    public void init(View view) {
        editTextInput = view.findViewById(R.id.editTextTitle);
        editTextDescription = view.findViewById(R.id.editTextDescription);
        editTextVideo = view.findViewById(R.id.editTextVideo);
        btnPickThumbnail = view.findViewById(R.id.btnPickThumbnail);
        btnPickVideo = view.findViewById(R.id.btnPickVideo);
        btnSubmit = view.findViewById(R.id.btnSubmit);
        imageViewThumbnail = view.findViewById(R.id.imageViewThumbnail);



        thumbnailPickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        thumbnailUri = result.getData().getData();
                        try {
                            // Convert URI to Bitmap and display in ImageView
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), thumbnailUri);
                            imageViewThumbnail.setImageBitmap(bitmap);
                            imageViewThumbnail.setVisibility(View.VISIBLE);
                        } catch (IOException e) {
                            e.printStackTrace();
                            Toast.makeText(requireContext(), "Failed to load image", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        // Register video picker launcher
        videoPickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        videoUri = result.getData().getData();
                        editTextVideo.setText(videoUri.toString()); // Show path in EditText
                    }
                });


        // Pick Video Button Click Listener
        btnPickVideo.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("video/*"); // Allow only video selection
            videoPickerLauncher.launch(intent);
        });

        // Pick Thumbnail Button Click Listener
        btnPickThumbnail.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*"); // Allow only image selection
            thumbnailPickerLauncher.launch(intent);
        });
    }
}

class DataObject {
    private String id;
    private String text;

    public DataObject(String id, String text) {
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
