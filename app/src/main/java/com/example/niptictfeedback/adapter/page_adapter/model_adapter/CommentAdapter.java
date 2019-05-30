package com.example.niptictfeedback.adapter.page_adapter.model_adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.niptictfeedback.MyApplication;
import com.example.niptictfeedback.R;
import com.example.niptictfeedback.fragments.bottom_sheet_fragment.BottomSheetDialogComments;
import com.example.niptictfeedback.models.Comment;
import com.example.niptictfeedback.models.News;
import com.example.niptictfeedback.models.User;
import com.example.niptictfeedback.sqlite.UserDBHelper;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.MyViewHolder>{

    private List<Comment> comments;
    private View.OnLongClickListener mOnClickListener;
    private Context context;
    private RecyclerView recyclerView;
    private String baseUrl;
    private UserDBHelper userDBHelper;
    public CommentAdapter(List<Comment> comments, Context context,RecyclerView recyclerView) {
        this.comments = comments;
        this.context = context;
        this.recyclerView = recyclerView;
        this.baseUrl = ((MyApplication) context.getApplicationContext()).getBaseUrl();
    }

    @NonNull
    @Override
    public CommentAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, final int i) {

        View myView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custome_comment_recycler_view,viewGroup,false);
        mOnClickListener = new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                AppCompatActivity appCompatActivity  = (AppCompatActivity)v.getContext();
                int itemPos = recyclerView.getChildLayoutPosition(v);
                Comment comment = comments.get(itemPos);
                Bundle bundle = new Bundle();
                bundle.putString("CommentID",comment.getId());
                bundle.putString("CommentUserID",comment.getUser_id());
                bundle.putString("FeedbackID",comment.getFeedback_id());
                BottomSheetDialogComments bottomSheetDialogNewsInfo = BottomSheetDialogComments.newInstance();
                bottomSheetDialogNewsInfo.setArguments(bundle);
                bottomSheetDialogNewsInfo.show(((AppCompatActivity)context).getSupportFragmentManager(),"comment_more_option_dialog");
                return true;
            }
        };

        myView.setOnLongClickListener(mOnClickListener);
        return new MyViewHolder(myView);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.tvDescription.setText(comments.get(i).getDescription());
        myViewHolder.tvUsername.setText(comments.get(i).getUsername());
        userDBHelper = new UserDBHelper(context);
        User user = userDBHelper.getLoginUser();
        Log.e("comment ID",comments.get(i).getUser_id()+"");
        Log.e("user ID",user.getId()+"");
        if (comments.get(i).getUser_id().equals(user.getId())){

            myViewHolder.tvUsername.setTextColor(context.getResources().getColor(R.color.startblue));

        }
    }



    @Override
    public int getItemCount() {
        return comments.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tvDescription,tvUsername;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDescription = itemView.findViewById(R.id.tv_description_custom_comment);
            tvUsername = itemView.findViewById(R.id.tv_username_comment);

        }
    }

    private Uri getImageUri(Context context, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
}
