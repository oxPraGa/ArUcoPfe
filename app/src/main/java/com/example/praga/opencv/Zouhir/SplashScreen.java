package com.example.praga.opencv.Zouhir;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.praga.opencv.R;
import com.example.praga.opencv.Transition;

public class SplashScreen extends Activity {


    private ImageView myImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        myImageView = (ImageView) findViewById(R.id.imageView2);

        Animation myAnimation = AnimationUtils.loadAnimation(this,R.anim.mytransition);
        myImageView.startAnimation(myAnimation);

          //final Intent myIntent = new Intent(this,MainActivity.class);
        final Intent myIntent = new Intent(this,Transition.class);
        Thread timer = new Thread(){
            public  void run(){
                try {
                    sleep(3500);
                }
                catch (Exception e ){
                    e.printStackTrace();
                }
                finally {
                    startActivity(myIntent);
                    finish();
                }
            }
        };

        timer.start();
    }


}
