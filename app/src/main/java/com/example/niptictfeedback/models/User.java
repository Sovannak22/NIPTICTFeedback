package com.example.niptictfeedback.models;

public class User {
    private String id;
    private String name,email,password,user_role_id,phone_number,gender,dob,profile_img,moreInfo,cPassword,message,token,access_token,token_type,stu_id;

    public String getMessage() {
        return message;
    }

    public String getAccess_token() {
        return access_token;
    }

    public String getToken_type() {
        return token_type;
    }

    public String getToken() {
        return token;
    }
    public  User(){}

    public User(String stu_id, String name, String email, String password, String phone_number, String gender, String dob, String profile_img, String moreInfo, String cPassword) {
        this.stu_id = stu_id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone_number = phone_number;
        this.gender = gender;
        this.dob = dob;
        this.profile_img = profile_img;
        this.moreInfo = moreInfo;
        this.cPassword = cPassword;
    }

    public User(String id,String stu_id, String name, String password, String user_role_id,String profile_img) {
        this.id = id;
        this.stu_id = stu_id;
        this.name = name;
        this.password = password;
        this.user_role_id = user_role_id;
        this.profile_img = profile_img;
    }

    public String getcPassword() {
        return cPassword;
    }

    public String getId() {
        return id;
    }

    public String getStu_id() {
        return stu_id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getUser_role_id() {
        return user_role_id;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public String getGender() {
        return gender;
    }

    public String getDob() {
        return dob;
    }

    public String getProfileImg() {
        return profile_img;
    }

    public String getMoreInfo() {
        return moreInfo;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
