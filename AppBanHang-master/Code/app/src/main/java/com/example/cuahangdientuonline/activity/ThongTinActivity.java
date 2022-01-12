package com.example.cuahangdientuonline.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.cuahangdientuonline.R;
import com.example.cuahangdientuonline.ultil.CheckConnection;
import com.example.cuahangdientuonline.ultil.Server;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ThongTinActivity extends AppCompatActivity implements OnMapReadyCallback {
    private Toolbar toolbar;
    private GoogleMap mMap;
    private Button buttoncall,buttonemail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_tin);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        toolbar = (Toolbar) findViewById(R.id.toolbargooglemap);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Actiontoolbar();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        buttoncall=(Button) findViewById(R.id.buttoncall);
        buttonemail=(Button)findViewById(R.id.buttonemail);
        buttoncall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isPermissionGranted()){
                    call_action();
                }
            }
        });
        buttonemail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendEmail();
            }
        });

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        LatLng DHMo = new LatLng(10.817127, 106.677183);
        mMap.addMarker(new MarkerOptions().position(DHMo).title("SH_Shop cửa hàng thiết bị di động").snippet("371 Nguyễn Kiệm, Gò Vấp, TP.HCM").icon(BitmapDescriptorFactory.defaultMarker()));
        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        CameraPosition cameraPosition = new CameraPosition.Builder().target(DHMo).zoom(15).build();
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    public void SendEmail(){
        final RequestQueue requestQueue= Volley.newRequestQueue(this);
        StringRequest stringRequest=new StringRequest(Request.Method.POST, Server.layemail, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equals("error")) {
                    CheckConnection.ShowToast_short(ThongTinActivity.this,"lỗi");

                }else{

                    Intent intent = new Intent(Intent.ACTION_SENDTO);
                    intent.setType("message/rfc822");
                    intent.putExtra(Intent.EXTRA_EMAIL, response);
                    intent.setData(Uri.parse("mailto:"+"nguyenthanhsang131199@gmail.com"));
                    intent.putExtra(Intent.EXTRA_SUBJECT, "");
                    intent.putExtra(Intent.EXTRA_TEXT, "");
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_FROM_BACKGROUND);
                    try {

                        startActivity(intent);
                    } catch (android.content.ActivityNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                        Log.d("Email error:",e.toString());
                    }

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params=new HashMap<>();
                params.put("MaTaiKhoan", String.valueOf(DangNhapActivity.id));
                return params;
            }
        };
        requestQueue.add(stringRequest);
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
    public void call_action(){
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:0886967808" ));
        startActivity(callIntent);
    }
    public  boolean isPermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.CALL_PHONE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("TAG","Permission is granted");
                return true;
            } else {

                Log.v("TAG","Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v("TAG","Permission is granted");
            return true;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {

            case 1: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "Permission granted", Toast.LENGTH_SHORT).show();
                    call_action();
                } else {
                    Toast.makeText(getApplicationContext(), "Permission denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
