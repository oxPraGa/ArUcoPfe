package com.example.praga.opencv.Praga;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.praga.opencv.R;

public class Main2Activity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
    }

    public void set_passowrd(View view){
        Intent intents = new Intent(getBaseContext(),PasswordSet.class);
        startActivity(intents);
    }

    public void remove_passowrd(View view){

    }
}
