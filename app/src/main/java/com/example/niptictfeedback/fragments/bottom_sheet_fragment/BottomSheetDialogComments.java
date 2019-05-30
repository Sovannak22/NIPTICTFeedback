package com.example.niptictfeedback.fragments.bottom_sheet_fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.niptictfeedback.CommentActivity;
import com.example.niptictfeedback.MyApplication;
import com.example.niptictfeedback.R;
import com.example.niptictfeedback.apis.CommentApi;
import com.example.niptictfeedback.models.Comment;
import com.example.niptictfeedback.models.User;
import com.example.niptictfeedback.sqlite.UserDBHelper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BottomSheetDialogComments extends BottomSheetDialogFragment {

    private LinearLayout btnCancel,btnDeleteComment,noPermission;
    private Bundle bundle;
    private CommentApi commentApi;
    private UserDBHelper userDBHelper;


    public static BottomSheetDialogComments newInstance() {
        return new BottomSheetDialogComments();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bottom_sheet_layout_comments,container,false);
        bundle = getArguments();

        userDBHelper = new UserDBHelper(getContext());
        User user = userDBHelper.getLoginUser();

        btnCancel = v.findViewById(R.id.btn_cancel);
        btnDeleteComment = v.findViewById(R.id.btn_delete_comment);
        noPermission = v.findViewById(R.id.can_not_do_action_comment);

        if (bundle.getString("CommentUserID").equals(user.getId())){
            noPermission.setVisibility(View.GONE);
        }

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = getActivity().getSupportFragmentManager().findFragmentByTag("comment_more_option_dialog");
                getActivity().getSupportFragmentManager().beginTransaction().remove(fragment).commit();
            }
        });

        btnDeleteComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("DELETE THIS NEWS");
                builder.setMessage("You will no longer show this news anymore.");
                builder.setPositiveButton("DELETE",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteComment(bundle.getString("CommentID"));
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

        String baseUrl=((MyApplication)getActivity().getApplication()).getBaseUrl();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        commentApi = retrofit.create(CommentApi.class);

        return v;
    }


    public void deleteComment(String commentId){
        String auth=((MyApplication)getActivity().getApplication()).getAuthorization();
        Call<Comment> call = commentApi.deleteComment(auth,commentId,"DELETE");

        call.enqueue(new Callback<Comment>() {
            @Override
            public void onResponse(Call<Comment> call, Response<Comment> response) {
                if (!response.isSuccessful()){
                    Log.w("Upload Profile:: ",response.code()+""+response.message());
                    Toast.makeText(getContext(),"News delete unsuccessfully",Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(getContext(),"News Delete successfully",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(), CommentActivity.class);
                intent.putExtra("FeedbackID",bundle.getString("FeedbackID"));
                startActivity(intent);
                getActivity().finish();
            }

            @Override
            public void onFailure(Call<Comment> call, Throwable t) {
                Log.w("Upload Profile fail::","Fail "+t.getMessage());
            }
        });
    }
}
