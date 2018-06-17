package com.example.praga.opencv;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.praga.opencv.Praga.Main2Activity;
import com.example.praga.opencv.Zouhir.MainActivity;
import com.google.zxing.qrcode.encoder.QRCode;

public class Transition extends Activity {
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private static final String TAG = "OCVSample::Activity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transition);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);


        // No explanation needed; request the permission
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.INTERNET , Manifest.permission.ACCESS_FINE_LOCATION , Manifest.permission.ACCESS_WIFI_STATE},
                1);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_WIFI_STATE)
                != PackageManager.PERMISSION_GRANTED) {

            Log.d(TAG,"hh");
        }else{
            Log.d(TAG,"granted");
        }
    }

    public  void go_Seg(View view){
        Intent myIntent = new Intent(this,MainActivity.class);
        startActivity(myIntent);

    }

    public  void go_chess(View view){
        Intent myIntent = new Intent(this,Main2Activity.class);
        startActivity(myIntent);

    }
    public  void go_qr(View view){
        Intent myIntent = new Intent(this,Main3Activity.class);
        startActivity(myIntent);
    }
}
