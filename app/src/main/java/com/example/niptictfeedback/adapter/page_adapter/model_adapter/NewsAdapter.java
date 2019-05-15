package com.example.niptictfeedback.adapter.page_adapter.model_adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.niptictfeedback.R;
import com.example.niptictfeedback.fragments.NewsInfoFragment;
import com.example.niptictfeedback.models.News;
import com.example.niptictfeedback.models.User;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.MyViewHolder> {

    private List<News> newsList;
    private View.OnClickListener mOnClickListener;
    private Context context;
    private RecyclerView recyclerView;
    public NewsAdapter(List<News> newsList, Context context,RecyclerView recyclerView) {
        this.newsList = newsList;
        this.context = context;
        this.recyclerView = recyclerView;
    }

    @NonNull
    @Override
    public NewsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, final int i) {

        View myView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custome_news_layout,viewGroup,false);
        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity appCompatActivity  = (AppCompatActivity)v.getContext();
                int itemPos = recyclerView.getChildLayoutPosition(v);
                News news = newsList.get(itemPos);
                NewsInfoFragment newsInfoFragment = new NewsInfoFragment();
                Bundle args = new Bundle();
                args.putString("ImageUrl",news.getImage_url());
                args.putString("Title",news.getTitle());
                args.putString("Description",news.getDescription());
                newsInfoFragment.setArguments(args);
                FragmentManager fm = appCompatActivity.getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                fm.beginTransaction();
                newsInfoFragment.setArguments(args);
                ft.add(R.id.fragment_news_container,newsInfoFragment);
                ft.commit();
            }
        };
        myView.setOnClickListener(mOnClickListener);
        return new MyViewHolder(myView);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.tvTitle.setText(newsList.get(i).getTitle());
        myViewHolder.tvDescription.setText(newsList.get(i).getDescription());
        Picasso.get().load(newsList.get(i).getImage_url()).into(myViewHolder.imageNews);
    }



    @Override
    public int getItemCount() {
        return newsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tvTitle,tvDescription;
        ImageView imageNews;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvDescription = itemView.findViewById(R.id.tv_description);
            imageNews = itemView.findViewById(R.id.img_news);

        }
    }

    private Uri getImageUri(Context context, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }



}
