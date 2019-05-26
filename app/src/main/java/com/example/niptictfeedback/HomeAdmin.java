package com.example.niptictfeedback;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class HomeAdmin extends AppCompatActivity {

    private LinearLayout linearLayoutMember,linearLayoutPublic,linearLayoutPrivate,linearLayoutNews;
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_admin);

        linearLayoutMember = findViewById(R.id.ll_member);
        linearLayoutPublic = findViewById(R.id.ll_public_post);
        linearLayoutPrivate = findViewById(R.id.ll_private);
        linearLayoutNews = findViewById(R.id.ll_new);

        linearLayoutNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(HomeAdmin.this,NewsAdminActivity.class);
                startActivity(intent);
            }
        });
        linearLayoutPrivate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(HomeAdmin.this,PrivateFeedbackAdminActivity.class);
                startActivity(intent);
            }
        });
    }
}
