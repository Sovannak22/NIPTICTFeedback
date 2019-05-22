package com.example.niptictfeedback.fragments;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.niptictfeedback.MyApplication;
import com.example.niptictfeedback.R;
import com.squareup.picasso.Picasso;

public class NewsInfoFragment extends Fragment {
    public NewsInfoFragment() {
    }

    TextView tvTitle,tvDescription,tvClose;
    ImageView imgNews;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_news,container,false);
        String baseUrl=((MyApplication)getActivity().getApplication()).getBaseUrl();
        String tilte = getArguments().getString("Title");
        String description = getArguments().getString("Description");
        String imageUrl = baseUrl+(getArguments().getString("ImageUrl"));
//        imgNews = v.findViewById(R.id.img_news);
//        tvTitle = v.findViewById(R.id.tv_title);
//        tvDescription = v.findViewById(R.id.tv_description);
        tvTitle.setText(tilte);
        tvDescription.setText(description);
        Picasso.get().load(imageUrl).into(imgNews);
        tvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dismiss();
            }
        });
        return v;
    }

    public void dismiss(){
        Toast.makeText(getContext(),"Dismiss clicked",Toast.LENGTH_LONG).show();
        int fragmentSize = getFragmentManager().getFragments().size();
        Fragment fragment = getFragmentManager().getFragments().get(fragmentSize-1);
        getFragmentManager().beginTransaction().remove(fragment).commit();
    }
}
