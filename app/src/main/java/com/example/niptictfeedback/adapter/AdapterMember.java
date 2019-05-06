package com.example.niptictfeedback.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.niptictfeedback.R;
import com.example.niptictfeedback.model.Profilemodel;

import java.util.ArrayList;
import java.util.List;

public class AdapterMember extends BaseAdapter {
    private Context context;
    private List<Profilemodel> modelList;

    public AdapterMember(Context context,List<Profilemodel> modelList){
        this.context = context;
        this.modelList = modelList;
    }
    public AdapterMember(ArrayList<Profilemodel> arrayList){

    }

    @Override
    public int getCount() {
        return modelList.size();
    }

    @Override
    public Object getItem(int position) {
        return modelList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = View.inflate(context, R.layout.listview_notifi,null);
        ImageView imgCar = v.findViewById(R.id.img_pro);
        TextView textViewCarName =v.findViewById(R.id.tv_view1);
        TextView textViewPrice = v.findViewById(R.id.tv_view2);

        Profilemodel profilemodel = modelList.get(position);

        imgCar.setImageResource(profilemodel.getImg_id());
        textViewCarName.setText(profilemodel.getPro_name());
        textViewCarName.setText(profilemodel.getDescription()+"");

        return v;




    }


}
