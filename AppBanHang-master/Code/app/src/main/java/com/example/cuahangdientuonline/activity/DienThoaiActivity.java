package com.example.cuahangdientuonline.activity;


import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.SearchView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.cuahangdientuonline.R;
import com.example.cuahangdientuonline.adapter.DienThoaiAdapter;
import com.example.cuahangdientuonline.model.Sanpham;
import com.example.cuahangdientuonline.ultil.CheckConnection;
import com.example.cuahangdientuonline.ultil.Server;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DienThoaiActivity extends AppCompatActivity {
    private Toolbar toolbardt;
    private ListView listView;
    private DienThoaiAdapter dienThoaiAdapter;
    private ArrayList<Sanpham> mangdienthoai;
    private int iddt=0;
    private int page=1;
    private boolean isLoading=false;
    private boolean limitdata=false;
    private View footerview;
    private mHandler mHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dien_thoai);
        anhxa();
        if(CheckConnection.haveNetworkConnection(getApplicationContext())) {
            GetIdloaisp();
            Actiontoolbar();
            GetData(page);
            LoadmoreData();
        }else {
            CheckConnection.ShowToast_short(getApplicationContext(),"lỗi");
        }
    }

    private void LoadmoreData() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(getApplicationContext(), ChiTietSanPham.class);
                intent.putExtra("thongtinsanpham",mangdienthoai.get(position));
                startActivity(intent);
            }
        });
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(firstVisibleItem +visibleItemCount == totalItemCount && totalItemCount != 0 && isLoading==false && limitdata ==false){
                    isLoading=true;
                    ThreadData threadData=new ThreadData();
                    threadData.start();
                }
            }
        });
    }

    private void GetData(int page) {
        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
        String duongdan = Server.duongdandienthoai+ String.valueOf(page);
        StringRequest stringRequest=new StringRequest(Request.Method.POST, duongdan, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response!=null && response.length() != 2){
                    listView.removeFooterView(footerview); //có dữ liệu thì sẽ mất biểu tưởng load
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject=jsonArray.getJSONObject(i);
                            mangdienthoai.add(new Sanpham(jsonObject.getInt("id")
                            ,jsonObject.getString("tensp")
                            ,jsonObject.getInt("giasp")
                            ,jsonObject.getString("hinhanhsp")
                            ,jsonObject.getString("motasp")
                            ,jsonObject.getInt("idsanpham")));
                           // dienThoaiAdapter.notifyDataSetChanged();

                        }
                        dienThoaiAdapter=new DienThoaiAdapter(getApplicationContext(),mangdienthoai);
                        listView.setAdapter(dienThoaiAdapter);
                    } catch (JSONException e) {
                        e.printStackTrace();

                    }
                }else{
                    limitdata=true;
                    listView.removeFooterView(footerview);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> param=new HashMap<String, String>();
                param.put("idsanpham", String.valueOf(iddt));
                return param;
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

    private void GetIdloaisp() {
        iddt=getIntent().getIntExtra("idloaisanpham",-1);
        Log.d("giatriloaisanpham",iddt+"");
    }

    public void anhxa(){
        toolbardt=(Toolbar)findViewById(R.id.toolbardienthoai);
        listView=(ListView)findViewById(R.id.listviewdienthoai);

        mangdienthoai=new ArrayList<>();
//       dienThoaiAdapter=new DienThoaiAdapter(getApplicationContext(),mangdienthoai);
//       listView.setAdapter(dienThoaiAdapter);
        LayoutInflater inflater= (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        footerview =inflater.inflate(R.layout.progreebar,null);
        mHandler=new mHandler();

    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        SearchView searchView= (SearchView) menu.findItem(R.id.menusearch).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                dienThoaiAdapter.filter(s.trim());
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menugiohang :
                Intent intent=new Intent(getApplicationContext(),GioHang.class);
                startActivity(intent);
                break;
            case R.id.menusearchdollar :
                 DialogTheoGia();
                   break;
        }
        return super.onOptionsItemSelected(item);
    }
    public class mHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    listView.addFooterView(footerview);
                    break;
                case 1:
                    GetData(++page);
                    isLoading=false;
                    break;
            }
            super.handleMessage(msg);
        }
    }
    public class ThreadData extends Thread {
        public void run(){
            mHandler.sendEmptyMessage(0);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Message message=mHandler.obtainMessage(1);
            mHandler.sendMessage(message);
            super.run();
        }
    }
    private void DialogTheoGia(){
        final Dialog dialog=new Dialog(this);
        dialog.setContentView(R.layout.tim_kiem_theo_gia);
        RadioGroup group=(RadioGroup)dialog.findViewById(R.id.radiogroud);
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.radiobuttonduoi2tr:
                        dienThoaiAdapter.theogia(1);
                        dialog.dismiss();
                        break;
                    case R.id.radiobuttonduoi2tr5tr:
                        dienThoaiAdapter.theogia(2);
                        dialog.dismiss();
                        break;
                    case R.id.radiobuttonduoi5tr10tr:
                        dienThoaiAdapter.theogia(3);
                        dialog.dismiss();
                        break;
                    case R.id.radiobuttontren10tr:
                        dienThoaiAdapter.theogia(4);
                        dialog.dismiss();
                        break;
                    case R.id.radiobuttontatca:
                        dienThoaiAdapter.theogia(5);
                        dialog.dismiss();
                        break;
                }
            }
        });
        dialog.show();

    }

}
