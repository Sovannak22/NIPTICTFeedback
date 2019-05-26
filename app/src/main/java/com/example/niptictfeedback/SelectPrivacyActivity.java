package com.example.niptictfeedback;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

public class SelectPrivacyActivity extends AppCompatActivity {


    private Toolbar toolbar;
    private RadioGroup radioGroupPrivacy;
    private String privacy,privacyString;
    private Button btnDone;

    private final int REQUEST_PRIVACY_CODE=4;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_privacy);

        toolbar = findViewById(R.id.tool_bar_privacy);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        btnDone = findViewById(R.id.btn_done_privacy);


        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addListenerButton();
                Intent intent = new Intent();
                intent.putExtra("Privacy",privacy);
                intent.putExtra("PrivacyString",privacyString);
                setResult(REQUEST_PRIVACY_CODE,intent);
                finish();
            }
        });
    }

    //    Add radio box lister
    public void addListenerButton(){
        radioGroupPrivacy = findViewById(R.id.radio_group_privacy);
        int selectedId = radioGroupPrivacy.getCheckedRadioButtonId();
        switch (selectedId){
            case R.id.public_privacy_post:
                privacy = "1";
                privacyString = "Public";
                return;
            case R.id.private_privacy_post:
                privacy = "2";
                privacyString = "Private";
                return;
            default:
                privacy = null;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
