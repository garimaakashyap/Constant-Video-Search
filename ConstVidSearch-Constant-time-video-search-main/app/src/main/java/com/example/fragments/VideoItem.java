package com.example.fragments;

public class VideoItem {
    private String title;
    private String description;
    private String thumbnailUrl;
    private String videoUrl;

    public VideoItem(String title, String description, String thumbnailUrl, String videoUrl) {
        this.title = title;
        this.description = description;
        this.thumbnailUrl = thumbnailUrl;
        this.videoUrl = videoUrl;
    }

    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getThumbnailUrl() { return thumbnailUrl; }
    public String getVideoUrl() { return videoUrl; }
}
