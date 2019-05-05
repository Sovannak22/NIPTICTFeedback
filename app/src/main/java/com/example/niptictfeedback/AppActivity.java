package com.example.niptictfeedback;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.niptictfeedback.adapter.page_adapter.PageAdapter;

public class AppActivity extends AppCompatActivity {

    TabLayout tabLayout;
    TabItem homeTabItem,newsTabItem,postTabItem,profileTabItem,notificationTabItem;
    ViewPager viewPager;
    PageAdapter adapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_app);
        Log.e("Token",""+((MyApplication)this.getApplication()).getAuthorization());
        tabLayout = findViewById(R.id.app_tab_layout);

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
}
