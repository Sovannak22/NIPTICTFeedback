package com.example.niptictfeedback;

import android.app.Application;

public class MyApplication extends Application {

    private String authorization,accept="application/json";

    public String getAuthorization() {
        return authorization;
    }

    public void setAuthorization(String authorization) {
        this.authorization = authorization;
    }

    public String getAccept() {
        return accept;
    }

    public void setAccept(String accept) {
        this.accept = accept;
    }
}
