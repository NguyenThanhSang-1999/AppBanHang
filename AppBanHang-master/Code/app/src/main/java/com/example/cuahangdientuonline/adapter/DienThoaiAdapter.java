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

public class DienThoaiAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Sanpham> arraydienthoai;
    private ArrayList<Sanpham> arrayList;


    public DienThoaiAdapter(Context context, ArrayList<Sanpham> arraydienthoai) {
        this.context = context;
        this.arraydienthoai = arraydienthoai;
        this.arrayList=new ArrayList<Sanpham>();
        this.arrayList.addAll(arraydienthoai);
    }

    @Override
    public int getCount() {
        return arraydienthoai.size();
    }

    @Override
    public Object getItem(int position) {
        return arraydienthoai.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    public class ViewHolder{
        private ImageView imghinhdt;
        private TextView txtdt,txtgiadt,txtmotadt;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder=null;
        if(convertView==null){
            viewHolder=new ViewHolder();
            LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.dong_dienthoai,null);
            viewHolder.imghinhdt=(ImageView)convertView.findViewById(R.id.imageviewdienthoai);
            viewHolder.txtdt=(TextView) convertView.findViewById(R.id.textviewtendienthoai);
            viewHolder.txtgiadt=(TextView)convertView.findViewById(R.id.textviewgiadienthoai);
            viewHolder.txtmotadt=(TextView)convertView.findViewById(R.id.textviewmotadienthoai);
           convertView.setTag(viewHolder);
        }
        else{
            viewHolder= (ViewHolder) convertView.getTag();
        }
        Sanpham sanpham= (Sanpham) getItem(position);
        viewHolder.txtdt.setText(sanpham.getTensanpham());
        DecimalFormat decimalFormat=new DecimalFormat("###,###,###");
        viewHolder.txtgiadt.setText("Giá: "+decimalFormat.format(sanpham.getGiasanpham())+" Đ");
        Picasso.with(context).load(sanpham.getHinhanhsanpham())
                .placeholder(R.drawable.noimage)
                .error(R.drawable.error)
                .into(viewHolder.imghinhdt);
        viewHolder.txtmotadt.setMaxLines(2);
        viewHolder.txtmotadt.setEllipsize(TextUtils.TruncateAt.END);
        viewHolder.txtmotadt.setText(sanpham.getMotasanpham());

        return convertView;
    }
    //Filter Class
    public void  filter(String charText){
        charText=charText.toLowerCase(Locale.getDefault());
        arraydienthoai.clear();
        if(charText.length() == 0){
            arraydienthoai.addAll(arrayList);
        }else{
            for(Sanpham sp : arrayList){
                if(sp.getTensanpham().toLowerCase(Locale.getDefault()).contains(charText)){
                    arraydienthoai.add(sp);
                }
            }
        }
        notifyDataSetChanged();
    }
    public void theogia(Integer gia){
        arraydienthoai.clear();
        if(gia==1){
            for(Sanpham sp : arrayList){
                if(sp.getGiasanpham() <=2000000){
                    arraydienthoai.add(sp);
                }
            }

        }else if(gia==2){
            for(Sanpham sp : arrayList){
                if(sp.getGiasanpham() >=2000000 && sp.getGiasanpham()<=5000000){
                    arraydienthoai.add(sp);
                }
            }

        }else if(gia==3){
            for(Sanpham sp : arrayList){
                if(sp.getGiasanpham() >5000000 && sp.getGiasanpham()<=10000000){
                    arraydienthoai.add(sp);
                }
            }


        }else if(gia==4){
            for(Sanpham sp : arrayList){
                if(sp.getGiasanpham() >10000000){
                    arraydienthoai.add(sp);
                }
            }

        }else if(gia==5){
            arraydienthoai.addAll(arrayList);
        }
        notifyDataSetChanged();
    }
}
