package com.example.praga.opencv.Zouhir;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.praga.opencv.R;
import com.example.praga.opencv.Views.Customeview;

import java.util.ArrayList;

public class Make_pass extends Activity {

    //variables :
    private ImageView myImageView;
    private Bitmap mybiBitmap;

    String password;
    private int  x,y ,r , g,  b,change;
    Customeview myCustomeview;

    public ArrayList<String> paint_list;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_pass);

        //myholder = (LinearLayout) findViewById(R.id.linearLayout);
        //myholder.bringToFront();

        this.myImageView = (ImageView) findViewById(R.id.img_view2);
        this.myImageView.setImageBitmap(Segmentation.choosed_image);

        myCustomeview = (Customeview) findViewById(R.id.mycustomeview);

        //res = (Bitmap) getIntent().getParcelableExtra("chosed_image");


        password = "";
        change = 1;

        myImageView.setDrawingCacheEnabled(true);
        myImageView.buildDrawingCache(true);

        myImageView.setOnTouchListener(new ImageView.OnTouchListener(){

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if(event.getAction() == event.ACTION_DOWN){

                    myImageView.setImageBitmap(Segmentation.combined_image);
                }
                else
                if(event.getAction() == event.ACTION_UP)
                {
                    myImageView.setImageBitmap(Segmentation.segmented_image);

                    do{
                        x = ( int ) event.getX();
                        y = ( int ) event.getY();

                        mybiBitmap = myImageView.getDrawingCache();
                        int pixel = mybiBitmap.getPixel(x,y);

                        // getActionBar() the color of the clicked pixel (region);

                         r = Color.red(pixel);
                         g = Color.green(pixel);
                         b = Color.blue(pixel);

                    }while(r==0 && g==0 && b==0);




                    myImageView.setImageBitmap(Segmentation.combined_image);

                    String password = "#"+Integer.toHexString(r)+""+Integer.toHexString(g)+""+Integer.toHexString(b);

                    myCustomeview.create(r,g,b,password);
                    Toast.makeText(getApplicationContext(),"the color is r: "+r+"g: "+g+"b: "+b+password,Toast.LENGTH_LONG).show();


                }

                return true;
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

        //myImageView.setImageBitmap(res);
    }

    @Override
    protected void onResume() {
        super.onResume();

        //myImageView.setImageBitmap(res);
    }


    public  void show_img(View view){
       change *=-1;
        //myImageView.setImageBitmap(Segmentation.segmented_image);

       switch (change) {
           case -1:
               myImageView.setImageBitmap(Segmentation.segmented_image);
               break;

           case 1:
               myImageView.setImageBitmap(Segmentation.combined_image);
               break;
       }


    }

    public  void reset(View view){

        String txt = "";
        txt = txt+myCustomeview.paint_list.size();
        Toast.makeText(getApplicationContext(),"the width is"+txt, Toast.LENGTH_LONG).show();
        myCustomeview.paint_list.clear();
        myCustomeview.reset();

        txt = ""+myCustomeview.paint_list.size();
        Toast.makeText(getApplicationContext(),"the width is"+txt, Toast.LENGTH_LONG).show();

    }

    public void next(View view) {

        if(myCustomeview.paint_list.size() == 0 )
        {
            Toast.makeText(getApplicationContext(),"Entre your password",Toast.LENGTH_LONG).show();

        }else
        {
            Intent myIntent = new Intent(this,safe_code.class);
            startActivity(myIntent);
        }
    }



}
