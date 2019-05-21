package com.example.niptictfeedback;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.niptictfeedback.R;
import com.squareup.picasso.Picasso;

public class NewInfoActivity extends AppCompatActivity {

    private TextView tvAdminName,tvDate,tvTitle,tvDescription;
    private ImageView imageNews;
    private Intent intent;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_info);

        toolbar = findViewById(R.id.toolbar_admin_news);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        intent = getIntent();
        tvAdminName = findViewById(R.id.tv_name_admin_news_info);
        tvDate = findViewById(R.id.tv_date_news_info);
        tvTitle = findViewById(R.id.tv_title_news_info);
        tvDescription = findViewById(R.id.tv_description_news_info);
        imageNews = findViewById(R.id.img_news_news_info);
        String baseUrl=((MyApplication) getApplicationContext()).getBaseUrl();
        String imageUrl = baseUrl+(intent.getStringExtra("ImageUrl"));
        tvAdminName.setText("Admin name");
        tvTitle.setText(intent.getStringExtra("Title"));
        tvDescription.setText(intent.getStringExtra("Description"));
        Picasso.get().load(imageUrl).into(imageNews);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
