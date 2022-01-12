package com.example.cuahangdientuonline.activity;

import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.cuahangdientuonline.R;
import com.example.cuahangdientuonline.adapter.SanPhamDaDatAdapter;
import com.example.cuahangdientuonline.model.Sanphamdadat;
import com.example.cuahangdientuonline.ultil.CheckConnection;
import com.example.cuahangdientuonline.ultil.Server;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SanPhamDaDat extends AppCompatActivity {
    private Toolbar toolbardt;
    private ListView lvsp;
    private Integer tongtien=0;
    private TextView txtvtongtien,txthangdatrong;
    private SanPhamDaDatAdapter sanPhamDaDatAdapter;
    private ArrayList<Sanphamdadat> mangdadat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_san_pham_da_dat);
        anhxa();
        txthangdatrong.setVisibility(View.INVISIBLE);
        if(CheckConnection.haveNetworkConnection(getApplicationContext())) {
            Actiontoolbar();
            GetDataDaDat();

        }else{
            CheckConnection.ShowToast_short(getApplicationContext(),"Lỗi kết nói");

        }

    }
    private void GetDataDaDat() {
        tongtien=0;
       final DecimalFormat decimalFormat=new DecimalFormat("###,###,###");
       RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
       StringRequest stringRequest=new StringRequest(Request.Method.POST, Server.duongdangetsanphamdadat, new Response.Listener<String>() {
           @Override
           public void onResponse(String response) {
               if(response!=null && response.length() != 2){
                   try {
                       JSONArray jsonArray=new JSONArray(response);
                       for(int i=0;i<jsonArray.length();i++){
                           JSONObject object=jsonArray.getJSONObject(i);
                           mangdadat.add(new Sanphamdadat(object.getString("tensp"),
                                   object.getString("hinhanhsp"),
                                   object.getInt("giasp"),
                                   object.getInt("soluongsanpham"),
                                   object.getInt("tientungsanpham")));
                               tongtien+= object.getInt("tientungsanpham");
                                sanPhamDaDatAdapter.notifyDataSetChanged();
                       }
                        txtvtongtien.setText(decimalFormat.format(tongtien)+" Đ");
                   } catch (JSONException e) {
                       e.printStackTrace();
                   }
               }else{
                   txthangdatrong.setVisibility(View.VISIBLE);
               }

           }
       }, new Response.ErrorListener() {
           @Override
           public void onErrorResponse(VolleyError error) {

           }
       }){
           @Override
           protected Map<String, String> getParams() throws AuthFailureError {
               HashMap<String, String> map=new HashMap<>();
               map.put("idkhachhang", String.valueOf(DangNhapActivity.id));
               return map;
           }
       };
        requestQueue.add(stringRequest);

    }

    public void Actiontoolbar() {
        setSupportActionBar(toolbardt);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbardt.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    public void anhxa(){
        mangdadat=new ArrayList<>();
        sanPhamDaDatAdapter=new SanPhamDaDatAdapter(getApplicationContext(),mangdadat);
        lvsp=(ListView)findViewById(R.id.listviewsanphamdadat);
        lvsp.setAdapter(sanPhamDaDatAdapter);
        toolbardt=(Toolbar)findViewById(R.id.toolbarsanphamdadat);
        txtvtongtien=(TextView)findViewById(R.id.textviewtongtien);
        txthangdatrong=(TextView)findViewById(R.id.sanphamdadattrong);
    }

}
