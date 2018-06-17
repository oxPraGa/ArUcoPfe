package com.example.praga.opencv;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;


import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.OpenCVLoader;

import android.util.Log;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;


import com.example.praga.opencv.Praga.LockScreenService;

import org.opencv.android.BaseLoaderCallback;

import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.LoaderCallbackInterface;

import org.opencv.aruco.Board;
import org.opencv.aruco.CharucoBoard;
import org.opencv.aruco.Dictionary;

import org.opencv.calib3d.Calib3d;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

import org.opencv.core.Scalar;

import org.opencv.imgproc.Imgproc;
import org.xwalk.core.XWalkView;

import java.io.IOException;

import java.security.spec.ECField;
import java.util.ArrayList;

import java.util.List;

import static org.opencv.aruco.Aruco.DICT_ARUCO_ORIGINAL;
import static org.opencv.aruco.Aruco.calibrateCameraAruco;
import static org.opencv.aruco.Aruco.detectMarkers;
import static org.opencv.aruco.Aruco.drawAxis;
import static org.opencv.aruco.Aruco.drawDetectedMarkers;

import static org.opencv.aruco.Aruco.estimatePoseSingleMarkers;
import static org.opencv.aruco.Aruco.getPredefinedDictionary;
import static org.opencv.calib3d.Calib3d.Rodrigues;
import static org.opencv.calib3d.Calib3d.calibrate;
import static org.opencv.core.CvType.CV_64F;


public class Main3Activity extends Activity implements CameraBridgeViewBase.CvCameraViewListener2 {

    private static final String TAG = "OCVSample::Activity";
    private int w, h;
    private CameraBridgeViewBase mOpenCvCameraView;
    private XWalkView xWalkWebView;

    static {
        if (!OpenCVLoader.initDebug())
            Log.d("ERROR", "Unable to load OpenCV");
        else
            Log.d("SUCCESS", "OpenCV loaded");
    }

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS: {
                    Log.i(TAG, "OpenCV loaded successfully");
                    mOpenCvCameraView.enableView();
                    try {
                        initializeOpenCVDependencies();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
                default: {
                    super.onManagerConnected(status);
                }
                break;
            }
        }
    };

    private void initializeOpenCVDependencies() throws IOException {
        mOpenCvCameraView.enableView();


    }


    public Main3Activity() {

        Log.i(TAG, "Instantiated new " + this.getClass());
    }

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        Log.i(TAG, "called onCreate");
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_main3);
        mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.tutorial1_activity_java_surface_view);
        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
        mOpenCvCameraView.setCvCameraViewListener(this);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        xWalkWebView=(XWalkView)findViewById(R.id.xwalkWebView);
        xWalkWebView.clearCache(true);

        //xWalkWebView.load("file:///android_asset/index.html", null);
        xWalkWebView.load("file:///android_asset/cube.html", null);
        xWalkWebView.setBackgroundColor(Color.TRANSPARENT);
      //  WebView webView = (WebView) findViewById(R.id.webview);

      //  webView.loadUrl("file:///android_asset/index2.html");
       // webView.setBackgroundColor(Color.TRANSPARENT);
       // webView.getSettings().setJavaScriptEnabled(true);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback);
        } else {
            Log.d(TAG, "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    public void onDestroy() {
        super.onDestroy();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    public void onCameraViewStarted(int width, int height) {
        w = width;
        h = height;
    }

    public void onCameraViewStopped() {
    }

    public Mat recognize(Mat aInputFrame) {

        Mat B = aInputFrame.clone();
        Mat Frame = aInputFrame;
         Imgproc.cvtColor(aInputFrame, aInputFrame, Imgproc.COLOR_RGB2GRAY);
         Mat ids = new Mat();
         List<Mat> corners = new ArrayList<>();
         Dictionary dictionary = getPredefinedDictionary(DICT_ARUCO_ORIGINAL);
         detectMarkers(aInputFrame, dictionary, corners, ids);
         Mat matrixCamera = new Mat(3, 3, CV_64F , Scalar.all(0));
         matrixCamera.put(0,0 , 986.39698401);
         matrixCamera.put(0,2 , 627.89818108);
         matrixCamera.put(1,1 , 986.08089022);
         matrixCamera.put(1,2 , 342.04540423);
         matrixCamera.put(2,2 , 1);
         Mat distCoeffs = new Mat(1,5 ,CV_64F , Scalar.all(0));
         distCoeffs.put(0,0 , 2.02925847e-01);
         distCoeffs.put(0,1 , -1.37230101e+00);
         distCoeffs.put(0,2 , -2.07540091e-03);
         distCoeffs.put(0,3 , -2.20879095e-03);
         distCoeffs.put(0,4 , 3.72689513e+00);


         drawDetectedMarkers(aInputFrame,corners,ids,new Scalar( 255, 255, 0 ) );

        Mat rvecs = new Mat();
        Mat tvecs = new Mat();

         estimatePoseSingleMarkers(corners,0.04f,matrixCamera,distCoeffs,rvecs , tvecs);


         if(rvecs.size().height != 0 && tvecs.size().height != 0 ){

             Mat rotation = new Mat(0, 3, CvType.CV_64F);
             Rodrigues(rvecs, rotation);
             rotation = rotation.t().clone();
             final Mat rot = rotation.clone();
            // get translation vector
             Mat translation = new Mat(0, 3, CvType.CV_64F); // A matrix with 1 row and 3 columns
             translation.put(0, 0, tvecs.get(0,0)[0]); // Set row 1 , column 1
             translation.put(0, 1, tvecs.get(0,0)[1]); // Set row 1 , column 2
             translation.put(0, 2, tvecs.get(0,0)[2]); // Set row 1 , column

             final double x = tvecs.get(0,0)[0]*200, y = tvecs.get(0,0)[1]*200 ,z = tvecs.get(0,0)[2]*200;
             xWalkWebView.post(new Runnable() {
                 @Override
                 public void run() {
                     xWalkWebView.evaluateJavascript("(function() {return updateCam("+Double.toString(x)+","+Double.toString(y)+","+Double.toString(z)+" , " +
                             Double.toString(rot.get(0,0)[0])+","+Double.toString(rot.get(1,0)[0])+" ,"+Double.toString(rot.get(1,0)[0])+" , " +
                             Double.toString(rot.get(0,1)[0])+","+Double.toString(rot.get(1,1)[0])+" ,"+Double.toString(rot.get(0,1)[0])+" , " +
                             Double.toString(rot.get(0,2)[0])+","+Double.toString(rot.get(1,2)[0])+" ,"+Double.toString(rot.get(0,2)[0])+"); })();"                       , new ValueCallback<String>() {
                         @Override
                         public void onReceiveValue(String s) {
                         }
                     });
                 }
             });


             //

             Log.d(TAG, "\ntvec size =>"+tvecs.dump());


             try{
                 drawAxis(aInputFrame, matrixCamera, distCoeffs, rvecs , tvecs, 0.1f);
             }catch (Exception e){

             }
         }

         return aInputFrame;

    }

    public Mat onCameraFrame(CvCameraViewFrame inputFrame) {
        return recognize(inputFrame.rgba());

    }

}