package com.example.cuahangdientuonline.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.cuahangdientuonline.R;
import com.example.cuahangdientuonline.activity.GioHang;
import com.example.cuahangdientuonline.activity.MainActivity;
import com.example.cuahangdientuonline.model.Giohang;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class GioHangAdapter extends BaseAdapter {
    Context context;
    ArrayList<Giohang> arraygiohang;

    public GioHangAdapter(Context context, ArrayList<Giohang> arraygiohang) {
        this.context = context;
        this.arraygiohang = arraygiohang;
    }

    @Override
    public int getCount() {
        return arraygiohang.size();
    }

    @Override
    public Object getItem(int position) {
        return arraygiohang.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class ViewHolder {
        public TextView tengiohang;
        public TextView giagiohang;
        public ImageView imggiohang;
        public Button btthemgiohang;
        public Button btgiamgiohang;
        public ImageView imageView_trash;
        private Button giatrigiohang;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.dong_giohang, null);
            viewHolder.tengiohang = (TextView) convertView.findViewById(R.id.textviewtengiohang);
            viewHolder.giagiohang = (TextView) convertView.findViewById(R.id.textviewgiagiohang);
            viewHolder.imggiohang = (ImageView) convertView.findViewById(R.id.imagivewanhgiohang);
            viewHolder.btthemgiohang = (Button) convertView.findViewById(R.id.buttongiohangplus);
            viewHolder.btgiamgiohang = (Button) convertView.findViewById(R.id.buttongiohangminus);
            viewHolder.giatrigiohang = (Button) convertView.findViewById(R.id.buttongiohangvalues);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
//        Giohang giohang = (Giohang) getItem(position);
        Giohang giohang = arraygiohang.get(position);
        viewHolder.tengiohang.setText(giohang.getTensp());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        viewHolder.giagiohang.setText(decimalFormat.format(giohang.getGiasp()) + "VNĐ");
        Picasso.with(context).load(giohang.getHinhanhsp())
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.error)
                .into(viewHolder.imggiohang);
        viewHolder.giatrigiohang.setText(giohang.getSoluongsp() + "");
        //button +-
        int sl = Integer.parseInt(viewHolder.giatrigiohang.getText().toString());
        if (sl>=10) {
            viewHolder.btthemgiohang.setVisibility(View.INVISIBLE);

        }else if(sl<=1) {
            viewHolder.btgiamgiohang.setVisibility(View.INVISIBLE);

        }else if(sl>=1){
            viewHolder.btgiamgiohang.setVisibility(View.VISIBLE);
            viewHolder.btthemgiohang.setVisibility(View.VISIBLE);
        }else {
            viewHolder.btgiamgiohang.setVisibility(View.VISIBLE);
            viewHolder.btthemgiohang.setVisibility(View.VISIBLE);
        }
        //update giá số lượng

        final ViewHolder finalViewHolder = viewHolder;

        viewHolder.btthemgiohang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int slmoi = Integer.parseInt(finalViewHolder.giatrigiohang.getText().toString()) + 1;
                int slhientai = MainActivity.manggiohang.get(position).getSoluongsp();
                long giaht = MainActivity.manggiohang.get(position).getGiasp();
                MainActivity.manggiohang.get(position).setSoluongsp(slmoi);
                //công thức tính
                long giamoi = (giaht * slmoi) / slhientai;
                MainActivity.manggiohang.get(position).setGiasp(giamoi);
                DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
                finalViewHolder.giagiohang.setText(decimalFormat.format(giamoi) + "Đ");
                GioHang.EventUtil();
                if (slmoi > 9) {
                    finalViewHolder.btthemgiohang.setVisibility(View.INVISIBLE);
                    finalViewHolder.btgiamgiohang.setVisibility(View.VISIBLE);
                    finalViewHolder.giatrigiohang.setText(String.valueOf(slmoi));
                } else {
                    finalViewHolder.btgiamgiohang.setVisibility(View.VISIBLE);
                    finalViewHolder.btthemgiohang.setVisibility(View.VISIBLE);
                    finalViewHolder.giatrigiohang.setText(String.valueOf(slmoi));
                }
            }
        });
        viewHolder.btgiamgiohang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int slmoi = Integer.parseInt(finalViewHolder.giatrigiohang.getText().toString()) - 1;
                int slhientai = MainActivity.manggiohang.get(position).getSoluongsp();
                long giaht = MainActivity.manggiohang.get(position).getGiasp();
                MainActivity.manggiohang.get(position).setSoluongsp(slmoi);
                //công thức tính
                long giamoi = (giaht * slmoi) / slhientai;

                MainActivity.manggiohang.get(position).setGiasp(giamoi);
                DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
                finalViewHolder.giagiohang.setText(decimalFormat.format(giamoi) + "Đ");
                GioHang.EventUtil();
                if (slmoi < 2) {
                    finalViewHolder.btgiamgiohang.setVisibility(View.INVISIBLE);
                    finalViewHolder.btthemgiohang.setVisibility(View.VISIBLE);
                    finalViewHolder.giatrigiohang.setText(String.valueOf(slmoi));
                } else {
                    finalViewHolder.btgiamgiohang.setVisibility(View.VISIBLE);
                    finalViewHolder.btthemgiohang.setVisibility(View.VISIBLE);
                    finalViewHolder.giatrigiohang.setText(String.valueOf(slmoi));
                }
            }
        });
        return convertView;
    }
}
