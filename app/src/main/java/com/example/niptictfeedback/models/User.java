package com.example.niptictfeedback.models;

public class User {
    private int id,stu_id;
    private String name,email,password,user_role_id,phone_number,gender,dob,profileImg,moreInfo,cPassword,message,token,access_token,token_type;

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

    public User(int stu_id, String name, String email, String password, String phone_number, String gender, String dob, String profileImg, String moreInfo, String cPassword) {
        this.stu_id = stu_id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone_number = phone_number;
        this.gender = gender;
        this.dob = dob;
        this.profileImg = profileImg;
        this.moreInfo = moreInfo;
        this.cPassword = cPassword;
    }

    public User(int stu_id, String name, String password, String cPassword,String gender) {
        this.stu_id = stu_id;
        this.name = name;
        this.password = password;
        this.cPassword = cPassword;
        this.gender = gender;
    }

    public String getcPassword() {
        return cPassword;
    }

    public int getId() {
        return id;
    }

    public int getStu_id() {
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
        return profileImg;
    }

    public String getMoreInfo() {
        return moreInfo;
    }
}
