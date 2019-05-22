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
    public FeedbackAdapter(List<FeedBack> feedBacks, Context context,RecyclerView recyclerView) {
        this.feedBacks = feedBacks;
        this.context = context;
        this.recyclerView = recyclerView;
        this.baseUrl = ((MyApplication) context.getApplicationContext()).getBaseUrl();
    }

    @NonNull
    @Override
    public FeedbackAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, final int i) {

        View myView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custome_feedback,viewGroup,false);
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
        return new MyViewHolder(myView);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.tvDescription.setText(feedBacks.get(i).getDescription());
        Picasso.get().load(baseUrl+(feedBacks.get(i).getImg())).into(myViewHolder.imageFeedback);
    }



    @Override
    public int getItemCount() {
        return feedBacks.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tvDescription;
        ImageView imageFeedback;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDescription = itemView.findViewById(R.id.tv_description_custom_feedback);
            imageFeedback = itemView.findViewById(R.id.img_custom_feedback);

        }
    }

    private Uri getImageUri(Context context, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
}
