package com.example.niptictfeedback.adapter.page_adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.niptictfeedback.fragments.HomeFragment;
import com.example.niptictfeedback.fragments.NewsFragment;
import com.example.niptictfeedback.fragments.NotificationFragment;
import com.example.niptictfeedback.fragments.PostFragment;
import com.example.niptictfeedback.fragments.ProfileFragment;

public class PageAdapter extends FragmentPagerAdapter {

    int numTab;
    public PageAdapter(Context context, FragmentManager fm, int numTab) {
        super(fm);
        this.numTab = numTab;
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                return new HomeFragment();
            case 1:
                return new NewsFragment();
            case 2:
                return new PostFragment();
            case 3:
                return new ProfileFragment();
            case 4:
                return new NotificationFragment();
            default:
                return new HomeFragment();
        }
    }

    @Override
    public int getCount() {
        return this.numTab;
    }
}
