package com.example.cuahangdientuonline.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.example.cuahangdientuonline.R;
import com.example.cuahangdientuonline.adapter.LoaispAdapter;
import com.example.cuahangdientuonline.adapter.SanphamAdapter;
import com.example.cuahangdientuonline.adapter.girdviewsanpham;
import com.example.cuahangdientuonline.model.Giohang;
import com.example.cuahangdientuonline.model.Loaisp;
import com.example.cuahangdientuonline.model.Sanpham;
import com.example.cuahangdientuonline.ultil.CheckConnection;
import com.example.cuahangdientuonline.ultil.Server;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView tv_Tentk, tv_Sdt;
    private ImageButton imgbt_Hinhtk;
    private Button btTrangDT, btTrangLT;
    private ViewFlipper viewFlipper;
    private GridView gridView;
    private LinearLayout taiKhoan;
    public  static EditText edtdc, edtsdt, edttenkh, edthokh;
    // private RecyclerView recyclerViewmanhinhchinh;
    private NavigationView navigationView;
    private ListView listViewmanhinhchinh;
    private DrawerLayout drawerLayout;
    private ArrayList<Loaisp> mangloaisp;
    private LoaispAdapter loaispAdapter;
    private int id=0;
    private String tenloaisp="";
    private String hinhanhloaisp="";
    private ArrayList<Sanpham> mangsanpham;
    private SanphamAdapter sanphamAdapter;
    private girdviewsanpham girdviewadapter;
    public static ArrayList<Giohang> manggiohang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Anhxa();
        if(CheckConnection.haveNetworkConnection(getApplicationContext())){
            ActionBar();
            ActionViewFlipper();
            LayHoTenKH();
            LaySDTKH();
            GetDuLieuLoaisp();
            //   GetDuLieuSanPhamNew();
            girdview();
            CatchOnItemListView();
            ChangeIntent();
        }else{
            CheckConnection.ShowToast_short(getApplicationContext(),"Bạn Hãy Kiểm Tra Lại Kết Nối");
            finish();
        }

    }


    private void ChangeIntent() {
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), ChiTietSanPham.class);
                intent.putExtra("thongtinsanpham",mangsanpham.get(position));
                startActivity(intent);
            }
        });
    }

    private void CatchOnItemListView() {
        listViewmanhinhchinh.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                switch (position){
                    case 0:
                        if(CheckConnection.haveNetworkConnection(getApplicationContext())){
                            Intent intent=new Intent(MainActivity.this,MainActivity.class);
                        }else{
                            CheckConnection.ShowToast_short(getApplicationContext(),"Lỗi");
                        }
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case 1:
                        if(CheckConnection.haveNetworkConnection(getApplicationContext())){
                            Intent intent=new Intent(MainActivity.this, DienThoaiActivity.class);
                            intent.putExtra("idloaisanpham",mangloaisp.get(position).getId());
                            startActivity(intent);
                        }else{
                            CheckConnection.ShowToast_short(getApplicationContext(),"Lỗi");
                        }
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case 2:
                        if(CheckConnection.haveNetworkConnection(getApplicationContext())){
                            Intent intent=new Intent(MainActivity.this, LapTopActivity.class);
                            intent.putExtra("idloaisanpham",mangloaisp.get(position).getId());
                            startActivity(intent);
                        }else{
                            CheckConnection.ShowToast_short(getApplicationContext(),"Lỗi");
                        }
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case 3:
                        if(CheckConnection.haveNetworkConnection(getApplicationContext())){
                            Intent intent=new Intent(MainActivity.this, ThongTinActivity.class);
                            startActivity(intent);
                        }else{
                            CheckConnection.ShowToast_short(getApplicationContext(),"Lỗi");
                        }
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case 4:
                        if(CheckConnection.haveNetworkConnection(getApplicationContext())){
                            Intent intent=new Intent(MainActivity.this, SanPhamDaDat.class);
                            startActivity(intent);
                        }else{
                            CheckConnection.ShowToast_short(getApplicationContext(),"Lỗi");
                        }
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;


                }
            }
        });
    }

    private void girdview() {
        final RequestQueue requestQueue=Volley.newRequestQueue(getApplicationContext());
        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(Server.duongdansanpham, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                if(response !=null){
                    int ID=0;
                    String Tensanpham="";
                    Integer Giasanpham=0;
                    String Hinhanhsanpham="";
                    String Motasanpham="";
                    int IDsanpham=0;
                    for(int i = 0;i<response.length();i++){
                        try {
                            JSONObject jsonObject=response.getJSONObject(i);
                            ID=jsonObject.getInt("id");
                            Tensanpham=jsonObject.getString("tensp");
                            Giasanpham=jsonObject.getInt("giasp");
                            Hinhanhsanpham=jsonObject.getString("hinhanhsp");
                            Motasanpham=jsonObject.getString("motasp");
                            IDsanpham=jsonObject.getInt("idsanpham");
                            mangsanpham.add(new Sanpham(ID,Tensanpham,Giasanpham,Hinhanhsanpham,Motasanpham,IDsanpham));
                            girdviewadapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                CheckConnection.ShowToast_short(getApplicationContext(),error.toString());
                finish();
            }
        });
        requestQueue.add(jsonArrayRequest);

    }

    private void capnhatdiachi(){
        final RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest=new StringRequest(Request.Method.POST, Server.capnhatdiachi, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> param=new HashMap<String, String>();
                param.put("MaTaiKhoan", String.valueOf(DangNhapActivity.id));
                param.put("diachi",edtdc.getText().toString().trim());
                return param;
            }
        };
        requestQueue.add(stringRequest);

    }
    private void capnhatsdt(){
        final RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest=new StringRequest(Request.Method.POST, Server.capnhatsdt, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> param=new HashMap<String, String>();
                param.put("MaTaiKhoan", String.valueOf(DangNhapActivity.id));
                param.put("SDT",edtsdt.getText().toString().trim());
                return param;
            }
        };
        requestQueue.add(stringRequest);

    }
    private void capnhatten(){
        final RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest=new StringRequest(Request.Method.POST, Server.capnhatten, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> param=new HashMap<String, String>();
                param.put("MaTaiKhoan", String.valueOf(DangNhapActivity.id));
                param.put("Ten",edttenkh.getText().toString().trim());
                return param;
            }
        };
        requestQueue.add(stringRequest);

    }
    private void capnhatho(){
        final RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest=new StringRequest(Request.Method.POST, Server.capnhatho, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> param=new HashMap<String, String>();
                param.put("MaTaiKhoan", String.valueOf(DangNhapActivity.id));
                param.put("Ho",edthokh.getText().toString().trim());
                return param;
            }
        };
        requestQueue.add(stringRequest);

    }
    private void DangXuat() {
        final Dialog d = new Dialog(this);
        LayTenKH();
        LayHoKH();
        LaySDT();
        LayDC();
        d.setContentView(R.layout.dialog_dangxuat);
        edttenkh=(EditText)d.findViewById(R.id.edttenkh);
        edthokh=(EditText)d.findViewById(R.id.edthokh);
        edtdc=(EditText)d.findViewById(R.id.edtdiachhi);
        edtsdt=(EditText)d.findViewById(R.id.edtsdtkhachhang);
        Button b1 = (Button) d.findViewById(R.id.buttonhuy);
        Button b2 = (Button) d.findViewById(R.id.buttonxacnhan);
        Button b3 = (Button) d.findViewById(R.id.buttondangxuat);
        b1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                d.dismiss();
                LayHoTenKH();
                LaySDTKH();
            }
        });
        b2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Xác nhận thay đổi thông tin");
                builder.setMessage("Bạn có chắc muốn thay đổi thông tin! ");
                builder.setPositiveButton("có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(edttenkh.getText().toString().isEmpty()){
                            edttenkh.setError("Vui lòng nhập tên!");
                        }else {
                            if (edthokh.getText().toString().isEmpty()) {
                                edthokh.setError(" Vui lòng nhập họ!");
                            } else {
                                if (edtsdt.getText().toString().isEmpty()) {
                                    edtsdt.setError(" Vui lòng nhập SĐT!");
                                } else {
                                    if (edtdc.getText().toString().isEmpty()) {
                                        edtdc.setError(" Vui lòng nhập dịa chỉ!");
                                    } else {
                                        if (validateSDT()) {
                                            capnhatten();
                                            capnhatho();
                                            capnhatsdt();
                                            capnhatdiachi();
                                            Toast.makeText(MainActivity.this, "Đổi thông tin thành công!", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                }
                            }
                        }

                    }
                });
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                builder.setNegativeButton("Không ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();

            }
        });

        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Xác nhận đăng xuất");
                builder.setMessage("Bạn có chắc muốn đăng xuất! ");
                builder.setPositiveButton("có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent=new Intent(MainActivity.this,DangNhapActivity.class);
                        startActivity(intent);
                        Toast.makeText(MainActivity.this, "Bạn đã đăng xuất!", Toast.LENGTH_LONG).show();
                        MainActivity.this.finish();
                    }
                });
                builder.setNegativeButton("Không ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();

            }
        });
        d.show();
    }

    private void LayTenKH(){
        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest=new StringRequest(Request.Method.POST, Server.laytenkh, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                edttenkh.setText(response);
                
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> param=new HashMap<String, String>();
                param.put("MaTaiKhoan", String.valueOf(DangNhapActivity.id));
                return param;
            }
        };
        requestQueue.add(stringRequest);

    }
    private void LayHoKH(){
        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest=new StringRequest(Request.Method.POST, Server.layhokh, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                edthokh.setText(response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> param=new HashMap<String, String>();
                param.put("MaTaiKhoan", String.valueOf(DangNhapActivity.id));
                return param;
            }
        };
        requestQueue.add(stringRequest);

    }
    private boolean validateSDT() {
        String sdt = edtsdt.getText().toString().trim();
                if (sdt.length() <= 9) {
                    edtsdt.setError("Số điện thoại không hợp lệ!");
                    return false;
                }
        return  true;
    }
    private void LayHoTenKH(){
        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest=new StringRequest(Request.Method.POST, Server.layhotenkh, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                tv_Tentk.setText(response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> param=new HashMap<String, String>();
                param.put("MaTaiKhoan", String.valueOf(DangNhapActivity.id));
                return param;
            }
        };
        requestQueue.add(stringRequest);

    }
    private void LaySDTKH(){
        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest=new StringRequest(Request.Method.POST, Server.laysdt, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                tv_Sdt.setText(response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> param=new HashMap<String, String>();
                param.put("MaTaiKhoan", String.valueOf(DangNhapActivity.id));
                return param;
            }
        };
        requestQueue.add(stringRequest);

    }
    private void LaySDT(){
        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest=new StringRequest(Request.Method.POST, Server.laysdt, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                edtsdt.setText(response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> param=new HashMap<String, String>();
                param.put("MaTaiKhoan", String.valueOf(DangNhapActivity.id));
                return param;
            }
        };
        requestQueue.add(stringRequest);

    }
    private void LayDC(){
        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest=new StringRequest(Request.Method.POST, Server.laydiachi, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                edtdc.setText(response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> param=new HashMap<String, String>();
                param.put("MaTaiKhoan", String.valueOf(DangNhapActivity.id));
                return param;
            }
        };
        requestQueue.add(stringRequest);

    }


    private void GetDuLieuLoaisp() {
        final RequestQueue requestQueue=Volley.newRequestQueue(getApplicationContext());
        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(Server.locahost, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                if(response !=null){
                    for(int i= 0 ;i<response.length();i++){
                        try {
                            JSONObject jsonObject=response.getJSONObject(i);
                            id=jsonObject.getInt("id");
                            tenloaisp=jsonObject.getString("tenLoaisp");
                            hinhanhloaisp=jsonObject.getString("hinhanhloaisp");
                            mangloaisp.add(new Loaisp(id,tenloaisp,hinhanhloaisp));
                            loaispAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    mangloaisp.add(3,new Loaisp(0,"Thông tin","https://cdn.icon-icons.com/icons2/1055/PNG/128/6-phone-cat_icon-icons.com_76682.png"));
                    mangloaisp.add(4,new Loaisp(0,"Sản phẩm đã đặt","https://cdn.icon-icons.com/icons2/1055/PNG/128/17-cart-cat_icon-icons.com_76693.png"));
                    loaispAdapter.notifyDataSetChanged();

                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                CheckConnection.ShowToast_short(getApplicationContext(),error.toString());
                finish();
            }
        });
        requestQueue.add(jsonArrayRequest);

    }


    private void ActionViewFlipper() {
        ArrayList<String> mangquangcao=new ArrayList<>();
        mangquangcao.add("https://img3.thuthuatphanmem.vn/uploads/2019/10/08/banner-quang-cao-dien-thoai_103211774.jpg");
        mangquangcao.add("https://www.mayphotocopyhaiphong.com/wp-content/uploads/2019/04/ban-laptop-tai-hai-phong.jpg");
        mangquangcao.add("https://bizweb.dktcdn.net/100/372/934/articles/banner-gram-pc-1600-x-800-min.png?v=1587609760090");
        mangquangcao.add("https://cdn.fptshop.com.vn/Uploads/Originals/2018/6/6/636638758716655000_C1.png");
        mangquangcao.add("https://thietkehaithanh.com/wp-content/uploads/2019/01/thietkehaithanh-banner-laptop.png");
        for(int i=0;i<mangquangcao.size();i++){
            ImageView imageView=new ImageView(getApplicationContext());
            Picasso.with(getApplicationContext()).load(mangquangcao.get(i)).into(imageView);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            viewFlipper.addView(imageView);
            viewFlipper.setAutoStart(true);
            Animation animation_slide_in=AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_in_right);
            Animation animation_slide_out=AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_out_right);
            viewFlipper.setInAnimation(animation_slide_in);
            viewFlipper.setOutAnimation(animation_slide_out);
        }
    }

    private void ActionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(android.R.drawable.ic_menu_sort_by_size);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_trangchinh,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menugiohang :
                Intent intent=new Intent(getApplicationContext(),GioHang.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }


    private void Anhxa(){
        taiKhoan = findViewById(R.id.taikhoan);
        taiKhoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DangXuat();
            }
        });
        tv_Sdt = (TextView)findViewById(R.id.tv_sdt);
        imgbt_Hinhtk = (ImageButton)findViewById(R.id.imgbtn_hinhtk);
        tv_Tentk = (TextView)findViewById(R.id.tv_tentk);
        tv_Tentk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DangXuat();
            }
        });
        toolbar=(Toolbar)findViewById(R.id.toobarmanhinhchinh);
        viewFlipper=(ViewFlipper)findViewById(R.id.viewlipper);
        //  recyclerViewmanhinhchinh=(RecyclerView)findViewById(R.id.recyclerView);
        navigationView=(NavigationView)findViewById(R.id.navigationview);
        listViewmanhinhchinh=(ListView)findViewById(R.id.listviewmanhinhchinh);
        drawerLayout=(DrawerLayout)findViewById(R.id.drawerlayout);
        mangloaisp=new ArrayList<>();
        mangloaisp.add(0,new Loaisp(0,"Trang chính","https://cdn.icon-icons.com/icons2/1411/PNG/512/g12-places-home_97323.png"));
        loaispAdapter=new LoaispAdapter(mangloaisp,getApplicationContext());
        listViewmanhinhchinh.setAdapter(loaispAdapter);
        mangsanpham=new ArrayList<>();
        sanphamAdapter=new SanphamAdapter(getApplicationContext(),mangsanpham);
        girdviewadapter=new girdviewsanpham(getApplicationContext(),mangsanpham);
        gridView=(GridView)findViewById(R.id.girdviewsanpham);
        gridView.setAdapter(girdviewadapter);
//        recyclerViewmanhinhchinh.setHasFixedSize(true);
//        recyclerViewmanhinhchinh.setLayoutManager(new GridLayoutManager(getApplicationContext(),2));
//        recyclerViewmanhinhchinh.setAdapter(sanphamAdapter);
        if(manggiohang !=null){

        }else{
            manggiohang=new ArrayList<>();
        }
    }
}
