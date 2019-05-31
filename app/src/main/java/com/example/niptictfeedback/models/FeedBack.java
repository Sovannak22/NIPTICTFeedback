package com.example.niptictfeedback.models;

public class FeedBack {
    private int place_id,feedback_type_id;
    private String user_id,title,description,id;
    private String img,comments_count,username,profile_img;

    public String getProfile_img() {
        return profile_img;
    }

    public String getUsername() {
        return username;
    }

    public String getComments_count() {
        return comments_count;
    }

    public String getId() {
        return id;
    }

    private String created_at;

    public FeedBack(int place_id, int feedback_type_id, String title, String description, String img) {
        this.place_id = place_id;
        this.feedback_type_id = feedback_type_id;
        this.title = title;
        this.description = description;
        this.img = img;
    }

    public String getUser_id() {
        return user_id;
    }

    public int getPlace_id() {
        return place_id;
    }

    public int getFeedback_type_id() {
        return feedback_type_id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getImg() {
        return img;
    }

    public String getCreated_at() {
        return created_at;
    }
}
