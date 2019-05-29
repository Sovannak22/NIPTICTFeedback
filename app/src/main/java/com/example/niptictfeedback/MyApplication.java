package com.example.niptictfeedback;

import android.app.Application;

public class MyApplication extends Application {

    private String urlShare="http://172.23.15.217:8000/";
    private String urlLocal="http://10.0.2.2:8000/";
    private String authorization,accept="application/json";
    private String baseUrl=urlLocal;

    public String getBaseUrl() {
        return baseUrl;
    }

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
