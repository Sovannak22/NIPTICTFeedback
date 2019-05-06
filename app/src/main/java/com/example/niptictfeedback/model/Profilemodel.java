package com.example.niptictfeedback.model;

public class Profilemodel {
    private int img_id;
    private String pro_name,description;
    public Profilemodel(int img_id,String pro_name,String description){
        this.img_id = img_id;
        this.pro_name = pro_name;
        this.description = description;
    }
    public int getImg_id() {
        return img_id;
    }

    public void setImg_id(int img_id) {
        this.img_id = img_id;
    }

    public String getPro_name() {
        return pro_name;
    }

    public void setPro_name(String pro_name) {
        this.pro_name = pro_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
