package com.example.praga.opencv;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.aruco.Dictionary;
import org.opencv.calib3d.Calib3d;
import org.opencv.core.CvException;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.MatOfPoint3f;
import org.opencv.core.Point;
import org.opencv.core.Point3;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.FeatureDetector;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.opencv.aruco.Aruco.DICT_ARUCO_ORIGINAL;
import static org.opencv.aruco.Aruco.detectMarkers;
import static org.opencv.aruco.Aruco.drawDetectedMarkers;
import static org.opencv.aruco.Aruco.getPredefinedDictionary;
import static org.opencv.calib3d.Calib3d.drawChessboardCorners;
import static org.opencv.calib3d.Calib3d.findChessboardCorners;

public class Calibration extends Activity implements CvCameraViewListener2, OnTouchListener {
        private static final String TAG = "OCVSample::Activity";
        private int w, h;
        private CameraBridgeViewBase mOpenCvCameraView;
        ImageButton ImBtCl;
        Mat imToCal;
        Boolean calib=false;
        Boolean calibrated = false;
        Size patt;
    File photoFile = null; //fichier de la photo
    String mCurrentPhotoPath; //le chemin du fichier de la photo
    int flagsCalib = Calib3d.CALIB_ZERO_TANGENT_DIST
            | Calib3d.CALIB_FIX_PRINCIPAL_POINT
            | Calib3d.CALIB_FIX_K4
            | Calib3d.CALIB_FIX_K5;
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


        public Calibration() {

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
            setContentView(R.layout.activity_calibration);
            mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.tutorial1_activity_java_surface_view);
            mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
            mOpenCvCameraView.setCvCameraViewListener(this);


            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            ImBtCl =(ImageButton) findViewById(R.id.claib);
            ImBtCl.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Toast.makeText(getBaseContext(), (String)"Start calibration", Toast.LENGTH_LONG).show();
                    calib = true;
                }
            });
            patt = new Size(5, 5);

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


        MatOfPoint3f getCorner3f() {
            MatOfPoint3f corners3f = new MatOfPoint3f();
            double squareSize = 50;
            Point3[] vp = new Point3[(int) (patt.height *
                    patt.width)];
            int cnt = 0;
            for (int i = 0; i < patt.height; ++i)
                for (int j = 0; j < patt.width; ++j, cnt++)
                    vp[cnt] = new Point3(j * squareSize,
                            i * squareSize, 0.0d);
            corners3f.fromArray(vp);
            return corners3f;
        }
        public void calibss(Mat img){
            Imgproc.cvtColor(img, img, Imgproc.COLOR_RGB2GRAY);
            BufferedReader reader = null;
            ArrayList<Point> arPoi = new ArrayList<>();
            MatOfPoint2f imageCorners = new MatOfPoint2f();
            Point[] myPoints = { new Point(1246.3426513671875, 84.7620620727539), new Point(1257.864013671875, 247.18788146972656), new Point(1270.621337890625, 413.357666015625), new Point(1284.39453125, 582.3501586914062), new Point(1295.0, 751.0), new Point(1082.8798828125, 120.66477966308594), new Point(1092.669921875, 277.1543273925781), new Point(1102.5394287109375, 436.145751953125), new Point(1113.49658203125, 597.9751586914062), new Point(1126.10986328125, 764.3665161132812), new Point(933.14453125, 153.70089721679688), new Point(941.1329345703125, 304.60662841796875), new Point(949.3536987304688, 457.2171630859375), new Point(958.2457885742188, 612.3655395507812), new Point(967.4814453125, 771.9561157226562), new Point(794.3953857421875, 183.23707580566406), new Point(800.6200561523438, 328.4418640136719), new Point(808.0060424804688, 475.4723815917969), new Point(814.6397705078125, 625.6664428710938), new Point(821.8020629882812, 779.4938354492188), new Point(668.0, 214.0), new Point(669.445068359375, 349.7070007324219), new Point(675.9338989257812, 493.41339111328125), new Point(680.1878662109375, 637.9411010742188), new Point(685.5588989257812, 787.1668090820312)};
            imageCorners.fromList(Arrays.asList(myPoints));
            //Log.d(TAG, ">>"+imageCorners);
            ArrayList objectPoints  = new ArrayList(), imagePoints = new ArrayList();
            ArrayList vCorners;
            ArrayList vImg = new ArrayList();
            Mat cameraMatrix = Mat.eye(3, 3, CvType.CV_64F);
            Mat distCoeffs = Mat.zeros(8, 1, CvType.CV_64F);
            ArrayList rvecs = new ArrayList();
            ArrayList tvecs = new ArrayList();
            MatOfPoint3f corners3f = getCorner3f();
            objectPoints.add(imageCorners);
            imagePoints.add(corners3f);
            vImg.add(img);
            double errReproj = Calib3d.calibrateCamera(objectPoints,
                    imagePoints,img.size(), cameraMatrix,
                    distCoeffs, rvecs, tvecs,flagsCalib);

            Log.d(TAG, ">>"+cameraMatrix);
            Log.d(TAG, ">>"+distCoeffs);
        }
        public Mat finChessAndCalib(Mat img){

            Imgproc.cvtColor(img, img, Imgproc.COLOR_RGB2GRAY);
            Mat corners = new Mat();
            MatOfPoint2f imageCorners = new MatOfPoint2f();
            List<Point> cornerPoint= new ArrayList<>();
            Log.d(TAG, "In");
            boolean found = findChessboardCorners(img, patt, imageCorners, Calib3d.CALIB_CB_ADAPTIVE_THRESH | Calib3d.CALIB_CB_FILTER_QUADS );
            if (found) {
                drawChessboardCorners(img, patt, imageCorners, found);
                ArrayList objectPoints  = new ArrayList(), imagePoints = new ArrayList();
                ArrayList vCorners;
                ArrayList vImg = new ArrayList();
                Mat cameraMatrix = Mat.eye(3, 3, CvType.CV_64F);
                Mat distCoeffs = Mat.zeros(8, 1, CvType.CV_64F);
                ArrayList rvecs = new ArrayList();
                ArrayList tvecs = new ArrayList();
                MatOfPoint3f corners3f = getCorner3f();
                objectPoints.add(imageCorners);
                imagePoints.add(corners3f);
                vImg.add(img);
                double errReproj = Calib3d.calibrateCamera(objectPoints,
                        imagePoints,img.size(), cameraMatrix,
                        distCoeffs, rvecs, tvecs,flagsCalib);

                Log.d(TAG, ">>"+cameraMatrix);
                Log.d(TAG, ">>"+distCoeffs);
                //calib = false;
               // calibrated = true;
            } else {
                Log.d(TAG, "Not found hh");
                //   Toast.makeText(getBaseContext(), (String) "No chessBoard found", Toast.LENGTH_LONG).show();
            }
            return img;
        }
        Mat imgs = new Mat();
        public Mat onCameraFrame(final CvCameraViewFrame inputFrame) {

            if(calib) {
                Log.d(TAG, ">>");

                Mat seedsImage = inputFrame.rgba();
                Bitmap bmp = null;
                Mat tmp = new Mat (seedsImage.rows(), seedsImage.cols() , CvType.CV_8U, new Scalar(4));
                Log.d(TAG, ">> 2 ");

                try {
                    Log.d(TAG, ">> 3");

                    Imgproc.cvtColor(seedsImage, tmp, Imgproc.COLOR_RGB2BGRA);
                    //Imgproc.cvtColor(seedsImage, tmp, Imgproc.COLOR_GRAY2RGBA, 4);

                    bmp = Bitmap.createBitmap(tmp.cols(), tmp.rows(), Bitmap.Config.ARGB_8888);
                    Utils.matToBitmap(tmp, bmp);
                    File file = createImageFile();
                    FileOutputStream fOut = new FileOutputStream(file);
                    bmp.compress(Bitmap.CompressFormat.JPEG,100,fOut);
                    fOut.flush(); // Not really required
                    fOut.close(); // do not forget to close the stream
                    galleryAddPic();
                    Log.d(TAG,"saved");
                }
                catch (CvException e){Log.d("Exception",e.getMessage());} catch (IOException e) {
                    e.printStackTrace();
                }
                calib = false;
            }
            if(calibrated ) {
                return imgs;
            }
            return inputFrame.rgba();
        }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        return false;
    }

    // créer un fichier image
    private File createImageFile() throws IOException {
        Log.d(TAG,"praga =>");
        //generation du nom basé sur la date
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        //chemin de l'enregistrement du fichier
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        //definition du fichier
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // enregistrement du chemin du fichier
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }
    //fonction qui permet de rendre l'image visible au autres applications
    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }
}
