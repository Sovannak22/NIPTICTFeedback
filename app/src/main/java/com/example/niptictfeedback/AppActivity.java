package com.example.niptictfeedback;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.niptictfeedback.adapter.page_adapter.PageAdapter;
import com.example.niptictfeedback.sqlite.UserDBHelper;

public class AppActivity extends AppCompatActivity {

    TabLayout tabLayout;
    TabItem homeTabItem,newsTabItem,postTabItem,profileTabItem,notificationTabItem;
    ViewPager viewPager;
    PageAdapter adapter;
    Toolbar toolbar;
    UserDBHelper userDBHelper;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userDBHelper = new UserDBHelper(this);

        setContentView(R.layout.activity_app);
        Log.e("Token",""+((MyApplication)this.getApplication()).getAuthorization());
        tabLayout = findViewById(R.id.app_tab_layout);
        toolbar = findViewById(R.id.toolbar_user);
        setSupportActionBar(toolbar);

        homeTabItem = findViewById(R.id.tab_home);
        newsTabItem = findViewById(R.id.tab_news);
        postTabItem = findViewById(R.id.tab_post);
        profileTabItem = findViewById(R.id.tab_profile);
        notificationTabItem = findViewById(R.id.tab_notification);
        viewPager = findViewById(R.id.app_view_pager);


        adapter = new PageAdapter(this,getSupportFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(adapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));


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
            Intent intent = new Intent(AppActivity.this,LoginActivity.class);

            userDBHelper.dropTable();
            ((MyApplication) getApplicationContext()).setAuthorization(null);

            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
