package com.example.niptictfeedback.fragments.bottom_sheet_fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentContainer;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.niptictfeedback.CommentActivity;
import com.example.niptictfeedback.EditPostActivity;
import com.example.niptictfeedback.MyApplication;
import com.example.niptictfeedback.R;
import com.example.niptictfeedback.apis.FeedbackApi;
import com.example.niptictfeedback.fragments.FragmentFeedbackDorm;
import com.example.niptictfeedback.models.FeedBack;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BottomSheetDialogFeedbacks extends BottomSheetDialogFragment {

    private LinearLayout btnEdit,btnDelete,btnCancel;
    private FeedbackApi feedbackApi;
    private Bundle bundle;

    public static BottomSheetDialogFeedbacks newInstance() {
        return new BottomSheetDialogFeedbacks();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bottom_sheet_layout_feedback,container,false);

        bundle = getArguments();

        btnDelete = v.findViewById(R.id.btn_delete_feedback);
        btnEdit = v.findViewById(R.id.btn_edit_feedback);
        btnCancel = v.findViewById(R.id.btn_cancel_feedback_bottom_sheet);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = getActivity().getSupportFragmentManager().findFragmentByTag("feedback_more_option_dialog");
                getActivity().getSupportFragmentManager().beginTransaction().remove(fragment).commit();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("DELETE THIS NEWS");
                builder.setMessage("You will no longer show this news anymore.");
                builder.setPositiveButton("DELETE",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
//                                Toast.makeText(getContext(),bundle.getString("FeedbackID"),Toast.LENGTH_LONG).show();
                                deleteFeedback(bundle.getString("FeedbackID"));
                            }
                        });
                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                final AlertDialog dialog = builder.create();
                dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {
                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.textColorAlert));
                        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorPrimary));
                    }
                });
                dialog.show();
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), EditPostActivity.class);
                intent.putExtra("FeedbackID",bundle.getString("FeedbackID"));
                intent.putExtra("PlaceID",bundle.getString("PlaceID"));
                intent.putExtra("Description",bundle.getString("Description"));
                startActivity(intent);
            }
        });


        String baseUrl=((MyApplication)getActivity().getApplication()).getBaseUrl();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        feedbackApi = retrofit.create(FeedbackApi.class);

        return v;
    }

    public void deleteFeedback(String id){
        String auth=((MyApplication)getActivity().getApplication()).getAuthorization();
        Call<FeedBack> call = feedbackApi.deletFeedback(auth,id,"DELETE");

        call.enqueue(new Callback<FeedBack>() {
            @Override
            public void onResponse(Call<FeedBack> call, Response<FeedBack> response) {
                if (!response.isSuccessful()){
                    Log.w("Delete feedback:: ",response.code()+""+response.message());
                    Toast.makeText(getContext(),"Feedback delete unsuccessfully",Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(getContext(),"News Delete successfully",Toast.LENGTH_SHORT).show();

                Fragment fragment = getActivity().getSupportFragmentManager().findFragmentByTag("feedback_more_option_dialog");
                getActivity().getSupportFragmentManager().beginTransaction().remove(fragment).commit();
//                fragment = getActivity().getSupportFragmentManager().findFragmentByTag("Fragment_feedback_dorm_first");
//                getActivity().getSupportFragmentManager().beginTransaction().detach(fragment);
//                getActivity().getSupportFragmentManager().beginTransaction().attach(fragment);
//                getActivity().getSupportFragmentManager().beginTransaction().commit();
            }


            @Override
            public void onFailure(Call<FeedBack> call, Throwable t) {
                Toast.makeText(getContext(),"Feedback delete unsuccessfully",Toast.LENGTH_SHORT).show();
            }
        });

    }
}
