package com.example.praga.opencv.Zouhir;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.ximgproc.SuperpixelSLIC;
import org.opencv.ximgproc.Ximgproc;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Segmentation {

     static Bitmap binded_image,choosed_image,segmented_image,combined_image;

    public  Segmentation(Bitmap src){

        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inDither = false;
        o.inSampleSize=4;


        this.choosed_image = Bitmap.createBitmap(src);
        this.binded_image   = Bitmap.createBitmap( src.getWidth(), src.getHeight(), Bitmap.Config.ARGB_8888 );
        this.segmented_image = Bitmap.createBitmap( src.getWidth(), src.getHeight(), Bitmap.Config.ARGB_8888 );

    }

    public Bitmap Watershed(Bitmap src_Bitmap){

        Bitmap result_Bitmap;

        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inDither = false;
        o.inSampleSize=4;

        int width , height ;
        width  = src_Bitmap.getWidth();
        height = src_Bitmap.getHeight();



        Mat rgba = new Mat();
        Mat gray_mat= new Mat();
        Mat threeChannel = new Mat();



        Utils.bitmapToMat(src_Bitmap,gray_mat);


        Imgproc.cvtColor(gray_mat,rgba , Imgproc.COLOR_RGBA2RGB);


        Imgproc.cvtColor(rgba, threeChannel, Imgproc.COLOR_RGB2GRAY);
        Imgproc.threshold(threeChannel, threeChannel, 100, 255, Imgproc.THRESH_OTSU);


        Mat fg = new Mat(rgba.size(), CvType.CV_8U);
        Imgproc.erode(threeChannel,fg,new Mat(),new Point(-1,-1),2);

        Mat bg = new Mat(rgba.size(), CvType.CV_8U);
        Imgproc.dilate(threeChannel,bg,new Mat(),new Point(-1,-1),3);
        Imgproc.threshold(bg,bg,1, 128, Imgproc.THRESH_BINARY_INV);



        Mat markers = new Mat(rgba.size(), CvType.CV_8U, new Scalar(0));
        Core.add(fg, bg, markers);

        // Start the WaterShed Segmentation :


        Mat marker_tempo = new Mat();
        markers.convertTo(marker_tempo, CvType.CV_32S);

        Imgproc.watershed(rgba, marker_tempo);

        marker_tempo.convertTo(markers, CvType.CV_8U);



        result_Bitmap = Bitmap.createBitmap(width,height,Bitmap.Config.RGB_565);


        Imgproc.applyColorMap( markers, markers,2);
        Utils.matToBitmap( markers,result_Bitmap);


       return result_Bitmap;

    }

    public Bitmap SLIC_Segmentation(Bitmap src_Bitmap , int nbr){

          /***********/


        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inDither = false;
        o.inSampleSize=4;


        int width , height ;
        width  = src_Bitmap.getWidth();
        height = src_Bitmap.getHeight();


        Mat myRgb = new Mat();
        Mat blur = new Mat();
        Mat CieLAB_mat= new Mat();


        Utils.bitmapToMat(src_Bitmap,myRgb,true);


        Imgproc.cvtColor(myRgb,CieLAB_mat, Imgproc.COLOR_RGB2Lab);
        Imgproc.GaussianBlur(CieLAB_mat,blur,new Size(45,45), 0);


        /**
         *
         *
         * The first argument to this function is our image.
         * The third argument is the (approximate) number of segmentations we want from slic.
         * The final parameter is sigma which is the size of the Gaussian kernel applied
         * prior to the segmentation.
         *
         *
         **/

        //region_size[h/3,h/5];

        SuperpixelSLIC SLIC= Ximgproc.createSuperpixelSLIC(CieLAB_mat, Ximgproc.SLICO,height/nbr,(float)25);

        int num_iterations = 50;
          SLIC.iterate(num_iterations);

        int min_element_size = 75;

        if (min_element_size>0)
            SLIC.enforceLabelConnectivity(min_element_size);






        /**
         * getLabelContourMask
         * Return: CV_8U1 image mask where -1 indicates that the pixel is a superpixel border, and 0 otherwise.
         *
         * paramters :
         * ****************
         * thick_line :
         * If false,the border is only one pixel wide,
         * otherwise all pixels at the border are masked.
         *
         **/

        Mat mask=new Mat();
        SLIC.getLabelContourMask(mask,false);

        //colorise the superpixelregions :


        //-1- first step : creat a new image with white background:

        Mat dest__=new Mat();
        Bitmap destBitmap = Bitmap.createBitmap( width, height, Bitmap.Config.ARGB_8888 );
        destBitmap.eraseColor(Color.WHITE);

        //-2- second step : add the mask (with color black) to our image with the white background
        Utils.bitmapToMat(destBitmap,dest__);
        dest__.setTo( new Scalar(0,0,0),mask);

        Utils.matToBitmap(dest__,this.binded_image);

        //****************************//

        // -3- get the region to be filled with color :

        Mat grayMat = new Mat();
        Mat hierarchy = new Mat();


        Imgproc.cvtColor(dest__,grayMat, Imgproc.COLOR_RGB2GRAY);


        // Preparing the kernel matrix object
        Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_CROSS,new Size((1*1) + 1, (1*1)+1));

        // erode the image (the black color will fit through the whit color)
        // with this step we make sure that our controus Lines get Wider .

        Imgproc.erode(grayMat, grayMat, kernel);


        // find the contours and fill them with random color:
        List<MatOfPoint> contourList = new ArrayList<MatOfPoint>();
        Imgproc.findContours(grayMat, contourList, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);

        Mat contours = new Mat();
        contours.create(grayMat.rows(), grayMat.cols(), CvType.CV_8UC3);
        Random r = new Random();
        for (int i = 0; i < contourList.size(); i++) {

            int rr = 0 , gg = 0 , bb = 0;
            do{
                rr= r.nextInt(255);
                gg= r.nextInt(255);
                bb= r.nextInt(255);
            }while(rr==0 && gg==0 && bb==0);



            Imgproc.drawContours(contours, contourList, i, new Scalar(rr, gg, bb), -1);
        }



        Bitmap result_Bitmap;
        result_Bitmap = Bitmap.createBitmap(width,height,Bitmap.Config.RGB_565);

        Utils.matToBitmap(contours,result_Bitmap);
         this.segmented_image = Bitmap.createBitmap(result_Bitmap);

        /***********/
        return this.segmented_image;
    }

    public  void setCombined_image(Bitmap res){

        this.combined_image = Bitmap.createBitmap(res);
    }


}
