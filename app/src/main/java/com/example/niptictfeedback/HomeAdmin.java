package com.example.niptictfeedback;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.example.niptictfeedback.sqlite.UserDBHelper;

public class HomeAdmin extends AppCompatActivity {

    private LinearLayout linearLayoutMember,linearLayoutPublic,linearLayoutPrivate,linearLayoutNews;
    private Intent intent;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_admin);

        toolbar = findViewById(R.id.toolbar_admin_home);
        setSupportActionBar(toolbar);

//        linearLayoutMember = findViewById(R.id.ll_member);
        linearLayoutPublic = findViewById(R.id.ll_public_post);
        linearLayoutPrivate = findViewById(R.id.ll_private);
        linearLayoutNews = findViewById(R.id.ll_new);
        linearLayoutPublic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(HomeAdmin.this,PublicFeedbackAdminActivity.class);
                startActivity(intent);
            }
        });
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.app_activit_action_bar,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.logout){
            Intent intent = new Intent(HomeAdmin.this,LoginActivity.class);
            UserDBHelper userDBHelper = new UserDBHelper(getApplicationContext());
            userDBHelper.dropTable();
            ((MyApplication) getApplicationContext()).setAuthorization(null);

            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
