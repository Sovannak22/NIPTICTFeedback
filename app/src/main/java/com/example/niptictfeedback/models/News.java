package com.example.niptictfeedback.models;

public class News {

    private String title,description,image_url,username,created_at,id;

    private int place_id;

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getImage_url() {
        return image_url;
    }

    public int getPlace_id() {
        return place_id;
    }
    public String getCreated_at() {
        return created_at;
    }

    public String getUsername() {
        return username;
    }

    public String getId() {
        return id;
    }
}
