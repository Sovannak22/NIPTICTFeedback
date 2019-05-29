package com.example.niptictfeedback;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.niptictfeedback.apis.NewsApi;
import com.example.niptictfeedback.fragments.bottom_sheet_fragment.BottomSheetDialogNewsInfo;
import com.example.niptictfeedback.models.User;
import com.example.niptictfeedback.sqlite.UserDBHelper;
import com.squareup.picasso.Picasso;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class NewInfoActivity extends AppCompatActivity {

    private TextView tvAdminName,tvDate,tvTitle,tvDescription;
    private ImageView imageNews,moreOption;
    private Intent intent;
    private Toolbar toolbar;
    private UserDBHelper userDBHelper;
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
        moreOption = findViewById(R.id.more_option_button_news_info);

        String baseUrl=((MyApplication) getApplicationContext()).getBaseUrl();

        String imageUrl = baseUrl+(intent.getStringExtra("ImageUrl"));
        tvAdminName.setText(intent.getStringExtra("Username"));
        tvTitle.setText(intent.getStringExtra("Title"));
        tvDescription.setText(intent.getStringExtra("Description"));
        Picasso.get().load(imageUrl).into(imageNews);
        tvDate.setText(intent.getStringExtra("TimeAndDate"));
        final Bundle bundle = new Bundle();
        bundle.putString("NewsID",intent.getStringExtra("NewsID"));
        bundle.putString("Title",intent.getStringExtra("Title"));
        bundle.putString("Description",intent.getStringExtra("Description"));
        bundle.putString("ImageUrl",intent.getStringExtra("ImageUrl"));


        moreOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialogNewsInfo bottomSheetDialogNewsInfo = BottomSheetDialogNewsInfo.newInstance();
                bottomSheetDialogNewsInfo.setArguments(bundle);
                bottomSheetDialogNewsInfo.show(getSupportFragmentManager(),"news_more_option_dialog");
            }
        });

        //get Current user info and test user role id to show more option button
        User user;
        userDBHelper = new UserDBHelper(this);
        userDBHelper.createNewTable();
        user = userDBHelper.getLoginUser();
        if (user.getUser_role_id().equals("2")){
            moreOption.setVisibility(View.GONE);
        }
    }



    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
