package com.example.praga.opencv.Views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;

public class Customeview extends View {

     Paint paint1,paint2;
     boolean draw = false;
     int i;
     public   ArrayList<Password> paint_list;
     Canvas mycanvas;

    public Customeview(Context context) {
        super(context);

        this.i = -1;
        mycanvas = new Canvas();
        init(null);

    }

    public Customeview(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init(attrs);
    }

    public Customeview(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(attrs);

    }

    public Customeview(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        init(attrs);

    }


    private void init(@Nullable AttributeSet set){
        paint1 = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint1.setStyle(Paint.Style.FILL);

        paint_list= new ArrayList<Password>();

    }

    public  void create(int r , int g,int b , String password){

        Password password1 = new Password(r, g, b,password);
        if((paint_list.size() < 9))
          paint_list.add(password1);
        draw = true;
        postInvalidate();
    }

    public  void reset(){
        postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas ) {

        int radius = ( getHeight()-10 )/2 ;
        int pad = 5;
        int x;
        int y = getHeight()/2;

        if(draw){

            for (int ii=0;ii<paint_list.size();ii++){
                i++;
                int u0 = radius +pad;
                 x = ii*(2*radius+pad) +  u0;

                paint1.setColor(Color.rgb(paint_list.get(ii).r, paint_list.get(ii).g, paint_list.get(ii).b));
                canvas.drawCircle(x,y,radius,paint1);
            }


        }


    }

    public  class  Password{
        int r,g,b;
        String word;

        public Password(int r , int g,int b , String password) {
            this.r = r;
            this.g = g;
            this.b = b;
            this.word = password;
        }
    }


}
