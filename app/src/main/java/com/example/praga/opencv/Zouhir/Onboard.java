package com.example.praga.opencv.Zouhir;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.praga.opencv.R;

public class Onboard extends Activity {


    private ViewPager myslidevieViewPager;
    private LinearLayout myDotsLayout;

    private TextView [] mDots;

    private  Slider_Adapter mySlider_adapter;
    private int myCurrent_page;
    private boolean left , right;
    private Button next,previous;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboard);

        myslidevieViewPager = (ViewPager) findViewById(R.id.myslidevieViewPager);
        myDotsLayout=(LinearLayout) findViewById(R.id.mydotlinearLayout);

        mySlider_adapter = new Slider_Adapter(this);

        myslidevieViewPager.setAdapter(mySlider_adapter);

        left = false;
        right = false;

        next = (Button) findViewById(R.id.mynext);
        previous = (Button) findViewById(R.id.myprevious);
        previous.setVisibility(View.INVISIBLE);


        myslidevieViewPager.addOnPageChangeListener(viewlistner);

        //addDotsIndicator();
    }

    ViewPager.OnPageChangeListener viewlistner = new ViewPager.OnPageChangeListener(){

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

            myCurrent_page = position;
           // int max = mySlider_adapter.tutorials.length;

            switch (myCurrent_page){
                case 0 :

                    //left = false;
                    previous.setEnabled(false);
                    next.setEnabled(true);
                    previous.setVisibility(View.INVISIBLE);

                    next.setVisibility(View.VISIBLE);

                    previous.setText("");
                    next.setText("Next");

                break;
                //Max page number :
                case 2 :

                    //left = false;

                    previous.setEnabled(true);
                    next.setEnabled(true);
                    previous.setVisibility(View.VISIBLE);
                    next.setVisibility(View.VISIBLE);

                    previous.setText("Back");
                    next.setText("Finnish");

                break;

                default:
                    previous.setEnabled(true);
                    next.setEnabled(true);

                    previous.setVisibility(View.VISIBLE);
                    next.setVisibility(View.VISIBLE);

                    previous.setText("Back");
                    next.setText("Next");
                break;
            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }

    };


    public void previous(View view){
        myslidevieViewPager.setCurrentItem(myCurrent_page -1);
    }
    public void next(View view){
        String text;
        text = (String) next.getText();
        //Toast.makeText(this, "loool"+text,Toast.LENGTH_LONG ).show();
        Intent myIntent = new Intent(this,MainActivity.class);

        if(text.equals("Finnish")){
            startActivity(myIntent);
        }
        else{
            myslidevieViewPager.setCurrentItem(myCurrent_page + 1);
        }

    }


    /*
    public  void addDotsIndicator(){
        mDots = new TextView[mySlider_adapter.tutorials.length];

        for(int i=0;i<mySlider_adapter.tutorials.length;i++){

            mDots[i] = new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226;"));
            mDots[i].setTextSize(35);z
            mDots[i].setTextColor(getResources().getColor(R.color.blue_light ));

            myDotsLayout.addView(mDots[i]);
        }
    }
    */

}
