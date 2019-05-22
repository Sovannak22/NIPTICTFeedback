package com.example.niptictfeedback.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.niptictfeedback.CreatePostActivity;
import com.example.niptictfeedback.R;

public class PostFragment extends Fragment {

    LinearLayout btnDorm,btnCanteen;
    String place,placeId;
    Intent intent;

    public PostFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_post,container,false);
        btnDorm = v.findViewById(R.id.btn_dorm_post_fragment);
        btnCanteen = v.findViewById(R.id.btn_canteen_post_fragment);
        intent = new Intent(getActivity(),CreatePostActivity.class);
        btnDorm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                place = "Dorm";
                placeId="1";
                startActivity();
            }
        });
        btnCanteen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                place = "Canteen";
                placeId="2";
                startActivity();
            }
        });


        return v;
    }

    public void startActivity(){
        intent.putExtra("Place",place);
        intent.putExtra("PlaceId",placeId);
        startActivity(intent);
    }
}
