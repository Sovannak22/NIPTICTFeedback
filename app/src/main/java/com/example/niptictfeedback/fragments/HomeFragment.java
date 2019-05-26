package com.example.niptictfeedback.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.niptictfeedback.R;

public class HomeFragment extends Fragment {

    private Button btnDorm,btnCanteen;

    public HomeFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home,container,false);
        btnDorm = v.findViewById(R.id.btn_dorm_home_user);
        btnCanteen = v.findViewById(R.id.btn_canteen_home_user);
        FragmentFeedbackDorm fragmentFeedbackDorm = new FragmentFeedbackDorm();
        getChildFragmentManager().beginTransaction().add(R.id.feedbacks_user_container, fragmentFeedbackDorm).commit();
        btnDorm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnDorm.setBackground(getResources().getDrawable(R.drawable.blue_white_without_radius));
                btnCanteen.setBackground(getResources().getDrawable(R.drawable.white_blue_background_without_radius));
                btnDorm.setTextColor(getResources().getColor(R.color.colorAccent));
                btnCanteen.setTextColor(getResources().getColor(R.color.colorPrimary));
                FragmentFeedbackDorm fragmentFeedbackDorm = new FragmentFeedbackDorm();
                getChildFragmentManager().beginTransaction().replace(R.id.feedbacks_user_container, fragmentFeedbackDorm).commit();

            }
        });

        btnCanteen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnDorm.setBackground(getResources().getDrawable(R.drawable.white_blue_background_without_radius));
                btnCanteen.setBackground(getResources().getDrawable(R.drawable.blue_white_without_radius));
                btnDorm.setTextColor(getResources().getColor(R.color.colorPrimary));
                btnCanteen.setTextColor(getResources().getColor(R.color.colorAccent));
                FragmentFeedbackCanteen fragmentFeedbackCanteen = new FragmentFeedbackCanteen();
                getChildFragmentManager().beginTransaction().replace(R.id.feedbacks_user_container, fragmentFeedbackCanteen).commit();
            }
        });
        return v;
    }
}
