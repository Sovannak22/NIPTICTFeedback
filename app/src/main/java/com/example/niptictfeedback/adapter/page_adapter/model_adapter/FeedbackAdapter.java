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
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.niptictfeedback.CommentActivity;
import com.example.niptictfeedback.MyApplication;
import com.example.niptictfeedback.R;
import com.example.niptictfeedback.models.FeedBack;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class FeedbackAdapter extends RecyclerView.Adapter<FeedbackAdapter.MyViewHolder>  {

    private List<FeedBack> feedBacks;
    private View.OnClickListener mOnClickListener;
    private Context context;
    private RecyclerView recyclerView;
    private String baseUrl;
    private String previousActivity;

    public FeedbackAdapter(List<FeedBack> feedBacks, Context context,RecyclerView recyclerView,String previousActivity) {
        this.feedBacks = feedBacks;
        this.context = context;
        this.recyclerView = recyclerView;
        this.baseUrl = ((MyApplication) context.getApplicationContext()).getBaseUrl();
        this.previousActivity = previousActivity;
    }

    @NonNull
    @Override
    public FeedbackAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, final int i) {

        View myView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custome_feedback,viewGroup,false);
        MyViewHolder holder = new MyViewHolder(myView,new MyViewHolder.MyClickListener(){

            @Override
            public void onComment(int p) {
                Intent intent = new Intent(context,CommentActivity.class);
                String id = feedBacks.get(p).getId();
                intent.putExtra("FeedbackID",id);
                context.startActivity(intent);
            }
        });
        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int itemPos = recyclerView.getChildLayoutPosition(v);
                FeedBack feedBack = feedBacks.get(itemPos);
                Intent intent = new Intent(context, CommentActivity.class);

                intent.putExtra("FeedbackID",feedBack.getId());
                intent.putExtra("PreviousActivity",previousActivity);
                context.startActivity(intent);

            }
        };
//        mOnClickListener = new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                AppCompatActivity appCompatActivity  = (AppCompatActivity)v.getContext();
//                int itemPos = recyclerView.getChildLayoutPosition(v);
//                News news = newsList.get(itemPos);
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
//        myView.setOnClickListener(mOnClickListener);
        return holder;

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.tvDescription.setText(feedBacks.get(i).getDescription());
        Picasso.get().load(baseUrl+(feedBacks.get(i).getImg())).into(myViewHolder.imageFeedback);
        Picasso.get().load(baseUrl+(feedBacks.get(i).getProfile_img())).into(myViewHolder.feedProfileImage);
        myViewHolder.tvCommentsCount.setText(feedBacks.get(i).getComments_count());
        myViewHolder.tvUsername.setText(feedBacks.get(i).getUsername());
    }



    @Override
    public int getItemCount() {
        return feedBacks.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvDescription,tvCommentsCount,tvUsername;
        ImageView imageFeedback,feedProfileImage;
        LinearLayout btnComment;
        MyClickListener myClickListener;
        public MyViewHolder(@NonNull View itemView,MyClickListener listener) {
            super(itemView);
            tvDescription = itemView.findViewById(R.id.tv_description_custom_feedback);
            imageFeedback = itemView.findViewById(R.id.img_custom_feedback);
            btnComment = itemView.findViewById(R.id.btn_comment_custom_feedback);
            tvCommentsCount = itemView.findViewById(R.id.tv_comments_count);
            tvUsername = itemView.findViewById(R.id.tv_username_feedback);
            feedProfileImage = itemView.findViewById(R.id.feed_profile_image);
            this.myClickListener = listener;
            btnComment.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_comment_custom_feedback:
                    myClickListener.onComment(this.getLayoutPosition());
                    break;
                default:
                    break;
            }
        }

        public interface MyClickListener {
            void onComment(int p);
        }
    }

}
