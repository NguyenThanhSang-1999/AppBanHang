package com.example.cuahangdientuonline.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.cuahangdientuonline.R;
import com.example.cuahangdientuonline.model.Loaisp;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class LoaispAdapter extends BaseAdapter {
    public ArrayList<Loaisp> arrayListLoaisp;
    public Context context;

    public LoaispAdapter(ArrayList<Loaisp> arrayListLoaisp, Context context) {
        this.arrayListLoaisp = arrayListLoaisp;
        this.context = context;
    }

    @Override
    public int getCount() {
        return arrayListLoaisp.size();
    }

    @Override
    public Object getItem(int i) {
        return arrayListLoaisp.get(i);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    private class Viewhorder{
        TextView txttenloaisp;
        ImageView imgloaisp;
    }
    @Override
    public View getView(int i, View view, ViewGroup parent) {
        Viewhorder ViewHorder;
        if(view == null) {
            ViewHorder=new Viewhorder();
            LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view=inflater.inflate(R.layout.dong_listview_loaisp,null);
            ViewHorder.imgloaisp=(ImageView)view.findViewById(R.id.imageviewloaisp);
            ViewHorder.txttenloaisp=(TextView)view.findViewById(R.id.textviewloaisp);
            view.setTag(ViewHorder);
        }else{
            ViewHorder= (Viewhorder) view.getTag();
        }
        Loaisp loaisp=(Loaisp)getItem(i);
        ViewHorder.txttenloaisp.setText(loaisp.getTenLoaisp());
        Picasso.with(context).load(loaisp.getHinhanhloaisp())
                .placeholder(R.drawable.noimage)
                .error(R.drawable.error)
                .into(ViewHorder.imgloaisp);

        return view;
    }
}
