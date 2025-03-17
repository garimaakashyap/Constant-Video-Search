package com.example.fragments;

public class VideoData {
    private String inputText;
    private String description;
    private String thumbnailUrl;
    private String videoUrl;

    // ✅ Add this constructor
    public VideoData(String inputText, String description, String thumbnailUrl, String videoUrl) {
        this.inputText = inputText;
        this.description = description;
        this.thumbnailUrl = thumbnailUrl;
        this.videoUrl = videoUrl;
    }

    // Getters
    public String getInputText() { return inputText; }
    public String getDescription() { return description; }
    public String getThumbnailUrl() { return thumbnailUrl; }
    public String getVideoUrl() { return videoUrl; }

    // Setters
    public void setInputText(String inputText) { this.inputText = inputText; }
    public void setDescription(String description) { this.description = description; }
    public void setThumbnailUrl(String thumbnailUrl) { this.thumbnailUrl = thumbnailUrl; }
    public void setVideoUrl(String videoUrl) { this.videoUrl = videoUrl; }
}
