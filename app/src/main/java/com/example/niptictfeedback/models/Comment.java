package com.example.niptictfeedback.models;

public class Comment {

    private String feedback_id,description,user_id,username,id;

    public Comment(String feedback_id, String description) {
        this.feedback_id = feedback_id;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getFeedback_id() {
        return feedback_id;
    }

    public String getDescription() {
        return description;
    }

    public String getUser_id() {
        return user_id;
    }
}
