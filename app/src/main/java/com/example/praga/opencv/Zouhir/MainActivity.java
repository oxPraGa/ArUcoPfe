package com.example.praga.opencv.Zouhir;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.praga.opencv.R;
import com.warkiz.widget.IndicatorSeekBar;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;

import java.io.IOException;

public class MainActivity extends Activity {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    /**
     * Check the opencv if it works and
     * Prevent the app from downloading the opencvManager from the store
     */
    private static final String TAG = "MyActivity";
    static {
        if (!OpenCVLoader.initDebug()) {
            Log.e(TAG, "Cannot load OpenCV library");
        }
    }


    // variables :
    ImageView myImageView;
    Uri uri_image;
    Bitmap result_Bitmap,src_Bitmap;

    public  int  mysegment_number = 3;
    IndicatorSeekBar myIndicatorSeekBar ;
    boolean next = false,choosed=false;
    private Context mycontext = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

     /*
        if(OpenCVLoader.initDebug()){
            Toast.makeText(getApplicationContext(),"Success",Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Failed",Toast.LENGTH_SHORT).show();
        }
     */

        myImageView = (ImageView) findViewById(R.id.imageView);

        myImageView.setDrawingCacheEnabled(true);
        myImageView.buildDrawingCache(true);

        //
        myIndicatorSeekBar = (IndicatorSeekBar) findViewById(R.id.view_myIndicatorSeekBar);


        myIndicatorSeekBar.setOnSeekChangeListener(new IndicatorSeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(IndicatorSeekBar seekBar, int progress, float progressFloat, boolean fromUserTouch) {
            }

            @Override
            public void onSectionChanged(IndicatorSeekBar seekBar, int thumbPosOnTick, String textBelowTick, boolean fromUserTouch) {
                //only callback on discrete series SeekBar type.
                //Toast.makeText(getApplicationContext(),"hello"+textBelowTick,Toast.LENGTH_LONG).show();
                switch (thumbPosOnTick){

                    case 0:
                        mysegment_number = 4;
                    break;

                    case 1:
                        mysegment_number = 5;
                    break;

                    case 2:
                        mysegment_number = 6;
                    break;

                    case 3:
                        mysegment_number = 8;
                    break;

                    case 4:
                        mysegment_number = 9;
                    break;
                }

                //Toast.makeText(getApplicationContext(),"-*-"+mysegment_number,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onStartTrackingTouch(IndicatorSeekBar seekBar, int thumbPosOnTick) {
            }

            @Override
            public void onStopTrackingTouch(IndicatorSeekBar seekBar) {
            }
        });
    }


    /**
     *
     * Load the image :
     *
     */
    public void opengallery(View view){

        Intent myIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(myIntent,100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 100 && resultCode == RESULT_OK && data != null)
        {
            uri_image = data.getData();


            try
            {
                src_Bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),uri_image);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

            myImageView.setImageBitmap(src_Bitmap);
            choosed = true;
           // new Picasso.LoadedFrom(uri_image).into(myImageView);
        }
        else
        {

        }
    }


    /**
     *
     * start the Segmentation :
     *
     */
    public void To_Gray(View view){

        if(!choosed){
            Toast.makeText(getApplicationContext(),"please choose an image",Toast.LENGTH_LONG).show();
        }else{

            //Toast.makeText(getApplicationContext(),"-*-"+mysegment_number,Toast.LENGTH_LONG).show();

            Segmentation mySegmentation = new Segmentation(src_Bitmap);

            result_Bitmap = mySegmentation.SLIC_Segmentation(src_Bitmap , mysegment_number);

            Bitmap  res;
            //res = Bitmap.createBitmap(result_Bitmap.getWidth(),result_Bitmap.getHeight(),Bitmap.Config.RGB_565);

            res=Bitmap.createBitmap(Bind_image(src_Bitmap,mySegmentation.binded_image));

            mySegmentation.setCombined_image(res);


            //myImageView.setImageBitmap(mySegmentation.choosed_image);
            // myImageView.setImageBitmap(mySegmentation.binded_image);
            // myImageView.setImageBitmap(mySegmentation.segmented_image);
            myImageView.setImageBitmap(res);
            //Toast.makeText(getApplicationContext(),"number of the superpixel is",Toast.LENGTH_LONG).show();
            next = true;
        }
        //String text = ""+Resources.getSystem().getDisplayMetrics().widthPixels;
        //String text = ""+Resources.getSystem().getDisplayMetrics().heightPixels;
        //Toast.makeText(getApplicationContext(),"lol"+text,Toast.LENGTH_LONG).show();
}

    public  Bitmap Bind_image(Bitmap img1 , Bitmap img2){

        Bitmap  res;
        int width,height;

        width = img1.getWidth();
        height = img1.getHeight();
        res = Bitmap.createBitmap(width,height,Bitmap.Config.RGB_565);


        Mat mat1,mat2,mat3;

        mat1=new Mat();
        mat2=new Mat();
        mat3=new Mat();

        Utils.bitmapToMat(img1,mat1);
        Utils.bitmapToMat(img2,mat2);
        Utils.bitmapToMat(res,mat3);


        Core.addWeighted(mat1,0.5,mat2,0.15,0,mat3);


        Utils.matToBitmap(mat3,res);

        return  res;
    }

    public void Bf_next(View view){

        if(!next){
            Toast.makeText(getApplicationContext(),"please segment the image",Toast.LENGTH_LONG).show();
        }else{

            myImageView.buildDrawingCache();
            Bitmap bitmap = myImageView.getDrawingCache();
            Intent myIntent = new Intent(this,Make_pass.class);
            //myIntent.putExtra("chosed_image",bitmap);
            startActivity(myIntent);
        }


    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
}
