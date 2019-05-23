package com.example.niptictfeedback.adapter.page_adapter.model_adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.niptictfeedback.MyApplication;
import com.example.niptictfeedback.R;
import com.example.niptictfeedback.models.Comment;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.MyViewHolder>{

    private List<Comment> comments;
    private View.OnClickListener mOnClickListener;
    private Context context;
    private RecyclerView recyclerView;
    private String baseUrl;
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
//        mOnClickListener = new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                AppCompatActivity appCompatActivity  = (AppCompatActivity)v.getContext();
//                int itemPos = recyclerView.getChildLayoutPosition(v);
//                Comment news = newsList.get(itemPos);
//
//                Intent intent = new Intent(context, NewInfoActivity.class);
//                intent.putExtra("ImageUrl",news.getImage_url());
//                intent.putExtra("Title",news.getTitle());
//                intent.putExtra("Description",news.getDescription());
//                context.startActivity(intent);
//
//
//            }
//        };
//
//        myView.setOnClickListener(mOnClickListener);
        return new MyViewHolder(myView);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.tvDescription.setText(comments.get(i).getDescription());
        myViewHolder.tvUsername.setText(comments.get(i).getUsername());
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
