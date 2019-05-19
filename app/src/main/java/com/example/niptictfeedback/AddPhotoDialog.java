package com.example.niptictfeedback;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class AddPhotoDialog extends Dialog implements View.OnClickListener {

    public Activity activity;
    public Dialog dialog;
    public LinearLayout btnOpenGallery;

    public AddPhotoDialog(Activity activity) {
        super(activity);
        this.activity = activity;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.uplaod_image_popup);

        btnOpenGallery = findViewById(R.id.btn_open_gallery);
        btnOpenGallery.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_open_gallery:

        }
    }
}
