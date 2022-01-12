package com.example.cuahangdientuonline.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.cuahangdientuonline.R;
import com.example.cuahangdientuonline.ultil.Server;


import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class DangKyActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private EditText edtho,edtten,edtemail,edtmatkhau,edtsdt,edtdc;
    private RadioButton rdnam,rdnu;
    private Button bttdangkytaikhoan;
    private TextView textViewcanhbao;
    private String gtinh="Nam";
    private boolean ok=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_dang_ky);
        toolbar=(Toolbar)findViewById(R.id.toolbardangky);
        Actiontoolbar();
        anhxa();
        if(rdnu.isChecked()){
            gtinh="Nữ";
        }
        bttdangkytaikhoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(edtho.getText().toString().isEmpty() ||
                        edtten.getText().toString().isEmpty() ||
                        edtemail.getText().toString().isEmpty()||
                        edtmatkhau.getText().toString().isEmpty()||
                        edtsdt.getText().toString().isEmpty() ||
                        edtdc.getText().toString().isEmpty() ){
                    textViewcanhbao.setText(" Vui lòng điền đầy đủ thông tin!");

                }
                else if(!validatePassword() || !validateSDT() || !validateEmail() ){
                    textViewcanhbao.setText(null);
                }else {
                    themtaikhoan();
                }

            }
        });
    }
    private boolean validateEmail() {
        String emailInput = edtemail.getText().toString().trim();
        if (emailInput.isEmpty()) {
            edtemail.setError("Email không được bỏ trống!");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            edtemail.setError("Vui lòng nhập email hợp lệ!");
            return false;
        } else {
            edtemail.setError(null);
            return true;
        }
    }
    private boolean validatePassword() {
        String pass = edtmatkhau.getText().toString().trim();
        if (pass.length() == 0) {
            edtmatkhau.setError("Mật khẩu không được bỏ trống!");
            return false;
        } else {
            if (pass.length() <= 6 ) {
                edtmatkhau.setError("Mật khẩu phải có ít nhất 6 ký tự!");
                return false;
            }
        }

        return  true;
    }
    private boolean validateSDT() {
        String sdt = edtsdt.getText().toString().trim();
        if (sdt.length() == 0) {
            edtsdt.setError("Số điện thoại không được bỏ trống!");
            return false;
        } else {
            if (sdt.contains(" ")) {
                edtsdt.setError("SĐT không thêm khoảng trắng!!!");
                return false;
            } else {
                if (sdt.length() <= 9) {
                    edtsdt.setError("Số điện thoại không hợp lệ!");
                    return false;
                }
            }
        }
        return  true;
    }
    public void themtaikhoan(){
        final RequestQueue requestQueue=Volley.newRequestQueue(this);
        StringRequest stringRequest=new StringRequest(Request.Method.POST, Server.duongdandangkitaikhoan, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.trim().equals("success")) {
                    Toast.makeText(DangKyActivity.this, "Đăng ký thành công!", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(DangKyActivity.this, DangNhapActivity.class));
                }else if(!response.trim().equals("success")){
                    textViewcanhbao.setText("Email đã tồn tại! Vui lòng chọn email khác!");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DangKyActivity.this,"Lỗi Xảy Ra!",Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<>();
                params.put("Ho",edtho.getText().toString().trim());
                params.put("Ten",edtten.getText().toString().trim());
                params.put("Email",edtemail.getText().toString().trim());
                params.put("MatKhau",edtmatkhau.getText().toString().trim());
                params.put("SDT",edtsdt.getText().toString().trim());
                params.put("GioiTinh",gtinh);
                params.put("diachi",edtdc.getText().toString().trim());
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
    public void anhxa(){
        edtsdt=(EditText)findViewById(R.id.edt_sdt);
        edtdc=(EditText)findViewById(R.id.edt_diachi);
        textViewcanhbao=(TextView)findViewById(R.id.textviewcanhbao);
        edtho=(EditText)findViewById(R.id.edt_ho);
        edtten=(EditText)findViewById(R.id.edt_ten);
        edtemail=(EditText)findViewById(R.id.edt_email);
        edtmatkhau=(EditText)findViewById(R.id.edt_matkhau);
        bttdangkytaikhoan=(Button)findViewById(R.id.btn_signup);
        rdnam=(RadioButton)findViewById(R.id.radiobuttonnam);
        rdnu=(RadioButton)findViewById(R.id.radiobuttonnu);

    }
    public void Actiontoolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}