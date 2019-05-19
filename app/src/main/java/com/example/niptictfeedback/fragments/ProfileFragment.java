package com.example.niptictfeedback.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.niptictfeedback.R;
import com.example.niptictfeedback.adapter.page_adapter.PageAdapter;
import com.example.niptictfeedback.adapter.page_adapter.ProfilePageAdapter;

public class ProfileFragment extends Fragment {
    public ProfileFragment() {
    }

    TabLayout tabLayout;
    TabItem publicTabItem,privateTabItem,responseTabItem;
    ViewPager viewPager;
    ProfilePageAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile,container,false);


        tabLayout = v.findViewById(R.id.tabLayout_id);
        publicTabItem = v.findViewById(R.id.tab_public);
        privateTabItem = v.findViewById(R.id.tab_private);
        responseTabItem= v.findViewById(R.id.tab_response);
        viewPager = v.findViewById(R.id.viewPager_id);


        adapter = new ProfilePageAdapter(getActivity(), getChildFragmentManager(),tabLayout.getTabCount());
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
        return v;
    }
}
