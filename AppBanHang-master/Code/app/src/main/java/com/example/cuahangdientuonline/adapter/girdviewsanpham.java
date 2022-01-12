package com.example.cuahangdientuonline.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.cuahangdientuonline.R;
import com.example.cuahangdientuonline.model.Sanpham;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class girdviewsanpham extends BaseAdapter {
    private Context context;
    private ArrayList<Sanpham> arraysanpham;

    public girdviewsanpham(Context context, ArrayList<Sanpham> arraysanpham) {
        this.context = context;
        this.arraysanpham = arraysanpham;
    }

    @Override
    public int getCount() {
        return arraysanpham.size();
    }

    @Override
    public Object getItem(int position) {
        return arraysanpham.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    private class Viewhorder{
        TextView txttensp,txtgiasp;
        ImageView imgsp;
    }
    @Override
    public View getView(int i, View view, ViewGroup parent) {
        Viewhorder ViewHorder;
        if(view == null) {
            ViewHorder=new Viewhorder();
            LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view=inflater.inflate(R.layout.girdview_sanpham,null);
            ViewHorder.imgsp=(ImageView)view.findViewById(R.id.imageviewsanpham1);
            ViewHorder.txttensp=(TextView)view.findViewById(R.id.textviewtensanpham1);
            ViewHorder.txtgiasp=(TextView)view.findViewById(R.id.textviewgiasanpham1);
            view.setTag(ViewHorder);
        }else{
            ViewHorder= (Viewhorder) view.getTag();
        }
        Sanpham sanpham=(Sanpham) getItem(i);
        DecimalFormat decimalFormat=new DecimalFormat("###,###,###");
        ViewHorder.txttensp.setText(sanpham.getTensanpham());
        ViewHorder.txtgiasp.setText("Giá : "+decimalFormat.format(sanpham.getGiasanpham())+" Đ");
        Picasso.with(context).load(sanpham.getHinhanhsanpham())
                .placeholder(R.drawable.noimage)
                .error(R.drawable.error)
                .into(ViewHorder.imgsp);
        return view;
    }
}
