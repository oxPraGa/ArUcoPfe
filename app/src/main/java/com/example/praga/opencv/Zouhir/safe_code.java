package com.example.praga.opencv.Zouhir;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.praga.opencv.R;

public class safe_code extends Activity {

    String safe_code1,safe_code2;
    EditText  myEditText1,myEditText2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_safe_code);

        myEditText1 = (EditText) findViewById(R.id.editText3);
        myEditText2 = (EditText) findViewById(R.id.editText4);


    }


    public void validate(View view){
        safe_code1 = myEditText1.getText().toString();
        safe_code2 = myEditText2.getText().toString();

        if(!safe_code1.equals(safe_code2)){

            Toast.makeText(getApplicationContext(),"password must be the same",Toast.LENGTH_LONG).show();
        }
        else {

            // show message password ;
            // show text :

        }
    }
}
