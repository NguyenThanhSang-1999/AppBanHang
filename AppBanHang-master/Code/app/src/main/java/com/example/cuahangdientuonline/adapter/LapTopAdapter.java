package com.example.cuahangdientuonline.adapter;

import android.content.Context;
import android.text.TextUtils;
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
import java.util.Locale;

public class LapTopAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Sanpham> arraylaptop;
    private ArrayList<Sanpham> arrayList;

    public LapTopAdapter(Context context, ArrayList<Sanpham> arraylaptop) {
        this.context = context;
        this.arraylaptop = arraylaptop;
        arrayList=new ArrayList<Sanpham>();
        this.arrayList.addAll(arraylaptop);
    }

    @Override
    public int getCount() {

        return arraylaptop.size();
    }

    @Override
    public Object getItem(int position) {
        return arraylaptop.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    public class ViewHolder{
        private ImageView imghinhlaptop;
        private TextView txtlaptop,txtgialaptop,txtmotalaptop;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder=null;
        if(convertView==null){
            viewHolder=new ViewHolder();
            LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.dong_laptop,null);
            viewHolder.imghinhlaptop=(ImageView)convertView.findViewById(R.id.imageviewlaptop);
            viewHolder.txtlaptop=(TextView) convertView.findViewById(R.id.textviewtenlaptop);
            viewHolder.txtgialaptop=(TextView)convertView.findViewById(R.id.textviewgialaptop);
            viewHolder.txtmotalaptop=(TextView)convertView.findViewById(R.id.textviewmotalaptop);
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder= (ViewHolder) convertView.getTag();
        }
        Sanpham sanpham= (Sanpham) getItem(position);
        viewHolder.txtlaptop.setText(sanpham.getTensanpham());
        DecimalFormat decimalFormat=new DecimalFormat("###,###,###");
        viewHolder.txtgialaptop.setText("Giá: "+decimalFormat.format(sanpham.getGiasanpham())+" Đ");
        Picasso.with(context).load(sanpham.getHinhanhsanpham())
                .placeholder(R.drawable.noimage)
                .error(R.drawable.error)
                .into(viewHolder.imghinhlaptop);
        viewHolder.txtmotalaptop.setMaxLines(2);
        viewHolder.txtmotalaptop.setEllipsize(TextUtils.TruncateAt.END);
        viewHolder.txtmotalaptop.setText(sanpham.getMotasanpham());

        return convertView;
    }
    //Filter Class
    public void  filter(String charText){
        charText=charText.toLowerCase(Locale.getDefault());
        arraylaptop.clear();
        if(charText.length() == 0){
            arraylaptop.addAll(arrayList);
        }else{
            for(Sanpham sp : arrayList){
                if(sp.Tensanpham.toLowerCase(Locale.getDefault()).contains(charText)){
                    arraylaptop .add(sp);
                }
            }
        }
        notifyDataSetChanged();
    }
    public void theogia(Integer gia){
        arraylaptop.clear();
        if(gia==1){
            for(Sanpham sp : arrayList){
                if(sp.getGiasanpham() <=2000000){
                    arraylaptop.add(sp);
                }
            }

        }else if(gia==2){
            for(Sanpham sp : arrayList){
                if(sp.getGiasanpham() >=2000000 && sp.getGiasanpham()<=5000000){
                    arraylaptop.add(sp);
                }
            }

        }else if(gia==3){
            for(Sanpham sp : arrayList){
                if(sp.getGiasanpham() >5000000 && sp.getGiasanpham()<=10000000){
                    arraylaptop.add(sp);
                }
            }


        }else if(gia==4){
            for(Sanpham sp : arrayList){
                if(sp.getGiasanpham() >10000000){
                    arraylaptop.add(sp);
                }
            }

        }else if(gia==5){
            arraylaptop.addAll(arrayList);
        }
        notifyDataSetChanged();
    }
}
