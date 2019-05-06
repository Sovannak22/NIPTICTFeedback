package com.example.niptictfeedback;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.example.niptictfeedback.adapter.AdapterMember;
import com.example.niptictfeedback.model.Profilemodel;

import java.util.ArrayList;

public class NotificationActivity extends AppCompatActivity {
    private ListView listView;
    private AdapterMember adapter;
    private ArrayList<Profilemodel> arrayList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.imgview_notifi);
        listView = (ListView) findViewById(R.id.lv_listview);
        arrayList = new ArrayList<Profilemodel>();
        String pro_name, description;
        arrayList.add(new Profilemodel(R.drawable.profile_notifi, "Krouch vanyda", "Posts a new feedback"));
        arrayList.add(new Profilemodel(R.drawable.profile_notifi, "Krouch vanyda", "Posts a new feedback"));
        arrayList.add(new Profilemodel(R.drawable.profile_notifi, "Krouch vanyda", "Posts a new feedback"));
        arrayList.add(new Profilemodel(R.drawable.profile_notifi, "Krouch vanyda", "Posts a new feedback"));
        arrayList.add(new Profilemodel(R.drawable.profile_notifi, "Krouch vanyda", "Posts a new feedback"));
        adapter = new AdapterMember(NotificationActivity.this, arrayList);
        listView.setAdapter(adapter);
    }
}
