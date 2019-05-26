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
import com.example.niptictfeedback.profile_fragment.PrivateFragment;
import com.example.niptictfeedback.profile_fragment.PublicFragment;
import com.example.niptictfeedback.profile_fragment.ResponseFragment;

public class ProfilePageAdapter extends FragmentPagerAdapter {
    int numTab;
    public ProfilePageAdapter(Context context, FragmentManager fm, int numTab) {
        super(fm);
        this.numTab = numTab;
    }


    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                return new PublicFragment();
            case 1:
                return new PrivateFragment();
            case 2:
                return new ResponseFragment();

            default:
                return new PublicFragment();
        }
    }

    @Override
    public int getCount() {
        return this.numTab;
    }
}
