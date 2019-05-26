package com.example.niptictfeedback.adapter.page_adapter.model_adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.niptictfeedback.CommentActivity;
import com.example.niptictfeedback.MyApplication;
import com.example.niptictfeedback.PrivateFeedbackAdminActivity;
import com.example.niptictfeedback.R;
import com.example.niptictfeedback.models.FeedBack;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PrivateFeedbackAdapter extends RecyclerView.Adapter<PrivateFeedbackAdapter.MyViewHolder>{

    private List<FeedBack> feedBacks;
    private View.OnClickListener mOnClickListener;
    private Context context;
    private RecyclerView recyclerView;
    private String baseUrl;
    private Activity activity;
    //TODO:Activity that click to comment activity
    private String previousActivity;

    public PrivateFeedbackAdapter(List<FeedBack> feedBacks, Context context,RecyclerView recyclerView,Activity activity,String previousActivity) {
        this.feedBacks = feedBacks;
        this.context = context;
        this.recyclerView = recyclerView;
        this.baseUrl = ((MyApplication) context.getApplicationContext()).getBaseUrl();
        this.previousActivity = previousActivity;
        this.activity = activity;
    }

    @NonNull
    @Override
    public PrivateFeedbackAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, final int i) {

        View myView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custome_private_feedback,viewGroup,false);
        PrivateFeedbackAdapter.MyViewHolder holder = new PrivateFeedbackAdapter.MyViewHolder(myView,new PrivateFeedbackAdapter.MyViewHolder.MyClickListener(){

            @Override
            public void onResponce(int p) {
                Intent intent = new Intent(context, CommentActivity.class);
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
                activity.finish();

            }
        };

        return holder;

    }

    @Override
    public void onBindViewHolder(@NonNull PrivateFeedbackAdapter.MyViewHolder myViewHolder, int i) {
        myViewHolder.tvDescription.setText(feedBacks.get(i).getDescription());
        Picasso.get().load(baseUrl+(feedBacks.get(i).getImg())).into(myViewHolder.imageFeedback);
        myViewHolder.tvCommentsCount.setText(feedBacks.get(i).getComments_count());
        myViewHolder.tvUsername.setText(feedBacks.get(i).getUsername());
    }



    @Override
    public int getItemCount() {
        return feedBacks.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvDescription,tvCommentsCount,tvUsername;
        ImageView imageFeedback;
        LinearLayout btnComment;
        PrivateFeedbackAdapter.MyViewHolder.MyClickListener myClickListener;
        public MyViewHolder(@NonNull View itemView, PrivateFeedbackAdapter.MyViewHolder.MyClickListener listener) {
            super(itemView);
            tvDescription = itemView.findViewById(R.id.tv_description_custom_private_feedback);
            imageFeedback = itemView.findViewById(R.id.img_custom_private_feedback);
            btnComment = itemView.findViewById(R.id.btn_responce_custom_feedback);
            tvCommentsCount = itemView.findViewById(R.id.tv_responce_count);
            tvUsername = itemView.findViewById(R.id.tv_username_private_feedback);
            this.myClickListener = listener;
            btnComment.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_responce_custom_feedback:
                    myClickListener.onResponce(this.getLayoutPosition());
                    break;
                default:
                    break;
            }
        }

        public interface MyClickListener {
            void onResponce(int p);
        }
    }
}
