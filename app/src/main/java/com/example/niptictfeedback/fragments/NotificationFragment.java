package com.example.niptictfeedback.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.niptictfeedback.R;
import com.example.niptictfeedback.adapter.AdapterMember;
import com.example.niptictfeedback.model.Profilemodel;

import java.util.ArrayList;

public class NotificationFragment extends Fragment {
    public NotificationFragment() {
    }
    private ListView listView;
    private AdapterMember adapter;
    private ArrayList<Profilemodel> arrayList;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_notification,container,false);
        listView = v.findViewById(R.id.lv_notify);
        arrayList = new ArrayList<Profilemodel>();
        String pro_name, description;
        arrayList.add(new Profilemodel(R.drawable.profile_notifi, "Krouch vanyda", "Posts a new feedback"));
        arrayList.add(new Profilemodel(R.drawable.profile_notifi, "Krouch vanyda", "Posts a new feedback"));
        arrayList.add(new Profilemodel(R.drawable.profile_notifi, "Krouch vanyda", "Posts a new feedback"));
        arrayList.add(new Profilemodel(R.drawable.profile_notifi, "Krouch vanyda", "Posts a new feedback"));
        arrayList.add(new Profilemodel(R.drawable.profile_notifi, "Krouch vanyda", "Posts a new feedback"));
        adapter = new AdapterMember(getContext(), arrayList);
        listView.setAdapter(adapter);
        return v;
    }
}
