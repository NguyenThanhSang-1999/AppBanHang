package com.example.cuahangdientuonline.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cuahangdientuonline.R;
import com.example.cuahangdientuonline.model.Sanphamdadat;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class SanPhamDaDatAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Sanphamdadat> arraysanphamdadat;


    public SanPhamDaDatAdapter(Context context, ArrayList<Sanphamdadat> arraydienthoai) {
        this.context = context;
        this.arraysanphamdadat = arraydienthoai;
    }

    @Override
    public int getCount() {
        return arraysanphamdadat.size();
    }

    @Override
    public Object getItem(int position) {
        return arraysanphamdadat.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    public class ViewHolder{
        private ImageView imghinhdd;
        private TextView txtdd,txtgiadd,txtsldd;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder=null;
        if(convertView==null){
            viewHolder=new ViewHolder();
            LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.dong_san_pham_da_dat,null);
            viewHolder.imghinhdd=(ImageView)convertView.findViewById(R.id.imageviewdienthoaidadat);
            viewHolder.txtdd=(TextView) convertView.findViewById(R.id.textviewtendienthoaidadat);
            viewHolder.txtgiadd=(TextView)convertView.findViewById(R.id.textviewgiadienthoaidadat);
            viewHolder.txtsldd=(TextView)convertView.findViewById(R.id.textviewsoluongdadat);
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder= (ViewHolder) convertView.getTag();
        }
        Sanphamdadat sanPhamDaDat= (Sanphamdadat) getItem(position);
        viewHolder.txtdd.setText(sanPhamDaDat.getTensp());
        DecimalFormat decimalFormat=new DecimalFormat("###,###,###");
        viewHolder.txtgiadd.setText("Giá: "+decimalFormat.format(sanPhamDaDat.getGiasp())+" Đ");
        Picasso.with(context).load(sanPhamDaDat.getHinhanh())
                .placeholder(R.drawable.noimage)
                .error(R.drawable.error)
                .into(viewHolder.imghinhdd);
        viewHolder.txtsldd.setText("Số Lượng: "+sanPhamDaDat.getSl()+"");


        return convertView;
    }

}
