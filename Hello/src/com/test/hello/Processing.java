package com.test.hello;

import java.util.ArrayList;
import java.util.List;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import android.graphics.Bitmap;
import android.util.Log;

public class Processing {
	/*
	 * 霍夫直线检测
	 */
	public static void  houghlinesp(Mat thresholdImage, Mat lines, int threshold){

		   
	    Imgproc.HoughLinesP(thresholdImage, lines, 1, Math.PI/180, threshold+1, 10, 0);
		int a=1;
		while(lines.cols()==0&&a<20){
			Imgproc.HoughLinesP(thresholdImage, lines, 1, Math.PI/180, a, 10, 0);
			a++;
		}
	    for (int x = 0; x < lines.cols(); x++) 
	    {
	          double[] vec = lines.get(0, x);
	          double x1 = vec[0], 
	                 y1 = vec[1],
	                 x2 = vec[2],
	                 y2 = vec[3];
	          Point start = new Point(x1, y1);
	          Point end = new Point(x2, y2);
	          Core.line(thresholdImage, start, end, new Scalar(255,0,0), 8);
	    }
	}
	
	//灰度化	
		public static Mat	procSrc2Gray(Mat img){
		    double scale = 0.1;  
		   // Size dsize = new Size(img.width()*scale,img.height()*scale);  
		    Size ksize=new Size(3,3);
		    Mat img2 = new Mat();  
		    Mat img3 = new Mat();  
		   // Imgproc.Canny(img, img3, 123, 250); 
		   img.convertTo(img2, CvType.CV_8UC1);
		   Imgproc.cvtColor(img2, img2, Imgproc.COLOR_RGB2GRAY);
		   Imgproc.GaussianBlur(img2, img3, ksize, 3,3,1);
		  /*
		   //Imgproc.adaptiveBilateralFilter(img,img2, ksize,2/25);
		    Imgproc.cvtColor(img2, img3, Imgproc.COLOR_RGB2GRAY);
		   img3.convertTo(img3, CvType.CV_8UC1);
		  // Imgproc.threshold(img3,img4, 90, 200, Imgproc.THRESH_BINARY_INV);
		  // Imgproc.Laplacian(img, img4,CvType.CV_8SC1);*/
		   return img3;
		}
	//灰度化	
	public static Mat	procSrc2Gray(Bitmap bm){
	 
	    Mat img =new Mat();
	    Utils.bitmapToMat(bm,img);
	    double scale = 0.1;  
	   // Size dsize = new Size(img.width()*scale,img.height()*scale);  
	    Size ksize=new Size(3,3);
	    Mat img2 = new Mat();  
	    Mat img3 = new Mat();  
	   // Imgproc.Canny(img, img3, 123, 250); 
	   img.convertTo(img2, CvType.CV_8UC1);
	   Imgproc.cvtColor(img2, img2, Imgproc.COLOR_RGB2GRAY);
	   Imgproc.GaussianBlur(img2, img3, ksize, 3,3,1);
	  /*
	   //Imgproc.adaptiveBilateralFilter(img,img2, ksize,2/25);
	    Imgproc.cvtColor(img2, img3, Imgproc.COLOR_RGB2GRAY);
	   img3.convertTo(img3, CvType.CV_8UC1);
	   // Imgproc.threshold(img3,img4, 90, 200, Imgproc.THRESH_BINARY_INV);
	   // Imgproc.Laplacian(img, img4,CvType.CV_8SC1);*/
	   return img3;
	}
	
	
	/*
	 * 检测区域定位
	 */
	public static Point[] locate(Mat img,Mat lines,Point center,int distance){
		double b=0.0,k=0.0;
		double x,y;
		double rx=center.x;
		double ry=center.y;
		Point[] point=new Point[4];
		if (lines==null) return null;
		if (lines.cols()>0){
			int j=0;
			for (int i = 0; i < lines.cols(); i++) 
			{	
	          double[] vec = lines.get(0, i);
	          if (vec==null)
	        	  break;
	          double x1 = vec[0], 
	                 y1 = vec[1],
	                 x2 = vec[2],
	                 y2 = vec[3];
	          k=(y2-y1)/(x2-x1); 
	          x=Math.cos(Math.atan(k))*distance+(rx);
	          y=Math.sin(Math.atan(k))*distance+(ry);
	          point[j]=new Point(x,y);
	          Core.circle(img, point[j], 20, new Scalar(222,32,32),-1);
	          //同一直线相反方向取点
	          x=(rx)-Math.cos(Math.atan(k))*distance;
	          y=(ry)-Math.sin(Math.atan(k))*distance;
	          point[j+1]=new Point(x,y);
	          Core.circle(img, point[j+1], 20, new Scalar(222,32,32),-1);
	          j=j+2;
	          if (j>=4) break;
	         
	       }
		}
		else{
			point[0]=new Point(216,288);
			Core.circle(img, point[0], 20, new Scalar(222,32,32),-1);
		}
			return point;
	
	}
	
	/*
	 * 寻找轮廓
	 */
	@SuppressWarnings("null")
	public static Mat findlunkuo(Mat im,Point center){
		 Rect box;
		 Mat img4;
		List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
		Mat hierarchy = new Mat();
		//定义轮廓抽取模式
		//int mode = Imgproc.RETR_EXTERNAL;
	    //int mode = Imgproc.RETR_CCOMP;
		int mode = Imgproc.RETR_LIST;
		//int mode = Imgproc.RETR_TREE;
		//定义轮廓识别方法
		int method = Imgproc.CHAIN_APPROX_NONE;
		//轮廓识别
		Imgproc.findContours(im, contours, hierarchy, mode, method);
		
		//Imgproc.cvtColor(mImg, mRgba, Imgproc.COLOR_GRAY2RGBA, 4);
		int contour=0;
		
		img4=im.clone();
		
		img4=Mat.zeros(im.size(), CvType.CV_8UC1);
		
		MatOfPoint2f cf1=new MatOfPoint2f();
		MatOfPoint2f cf2=new MatOfPoint2f();
		int j=0;
		double area=0;
		for (contour=0;contour<contours.size();contour++){
			//获取最大轮廓的面积
			if(Imgproc.contourArea(contours.get(contour))>area)
			{
				area=Imgproc.contourArea(contours.get(contour));
				j=contour;
			}
			
			}
		contours.get(j).convertTo(cf1,CvType.CV_32FC2);
		Imgproc.approxPolyDP(cf1, cf2, 1, true);
		MatOfPoint c2=new MatOfPoint();
		cf2.convertTo(c2, contours.get(j).type());
		contours.set(j, c2);
		Imgproc.drawContours(img4, contours, j, new Scalar(255,150,100));
		box=Imgproc.boundingRect(contours.get(j));
		 center.x=181+box.x+box.width/2;
		 center.y=253+box.y+box.height/2;

		 
		return img4;
	}
	
	
	public static Boolean compareRGB(Mat img,Point center,Point[] plocate,Point[] nlocate){
		int flag=0;
		double[] drgb={0,0,0};
		List<Mat> list=new ArrayList<Mat>();
		List<Mat> rgb=new ArrayList<Mat>();
		Mat rhist=new Mat();
		Mat ghist=new Mat();
		Mat bhist=new Mat();
		MatOfInt channels=new MatOfInt(0);
		Mat hist=new Mat();
		MatOfInt histSize=new MatOfInt(256);
		MatOfFloat ranges=new MatOfFloat(0f,255f);
	
		Mat mask=Mat.zeros(img.size(), img.type());
		Core.circle(mask,center,15,new Scalar(255),-1);
		Core.split(img, rgb);
		list.add(rgb.get(0));
		Imgproc.calcHist(list, channels, mask, bhist, histSize, ranges);
		list.clear();
		list.add(rgb.get(1));
		Imgproc.calcHist(list, channels, mask, ghist, histSize, ranges);
		list.clear();
		list.add(rgb.get(2));
		Imgproc.calcHist(list, channels, mask, rhist, histSize, ranges);
		list.clear();
		Core.normalize(rhist, rhist, 0, 1, Core.NORM_MINMAX,-1);
		Core.normalize(rhist, ghist, 0, 1, Core.NORM_MINMAX,-1);
		Core.normalize(rhist, bhist, 0, 1, Core.NORM_MINMAX,-1);
		for (int z=0;z<3;z++){
		for (int i=0;i<255;i++){
			double temp=0;
			double[] histvalues=rhist.get(i, 0);
			for (int j=0;j<histvalues.length;j++){
				temp+=histvalues[j];
			}
			if (temp>drgb[z]) 
				drgb[z]=temp;
		}
		}
		return true;
	}

	//中值滤波
	public static Mat medianBlur(Mat img){
		Mat dst=new Mat();
		Imgproc.medianBlur(img, dst, 5);
		return dst;
	}
	

	//比较灰度
	public static Boolean compare(Mat img,Point center,Point[] plocate,Point[] nlocate){
		double[] average={0,0,0};
		int flag=0;
		List<Mat> list = new ArrayList<Mat>();
        MatOfInt channels = new MatOfInt(0);
        Mat hist= new Mat();
        MatOfInt histSize = new MatOfInt(256);
        MatOfFloat ranges = new MatOfFloat(0f, 255f);
      
		Mat dst=Mat.zeros(img.size(),img.type());
		//提取中心区域
		Mat mask = Mat.zeros(img.size(), CvType.CV_8UC1);
		Core.circle(mask,center,15,new Scalar(255),-1);
		img.copyTo(dst, mask);

		list.add(img);
		//计算直方图
		double k=0.00000001;
		Imgproc.calcHist(list, channels, mask, hist, histSize, ranges);
		for (int i = 50; i< 200; i++) {
		    double[] histValues = hist.get(i, 0);
		    for (int j = 0; j < histValues.length; j++) {
		        average[0]+=(i*histValues[j]);
		        
		        if (histValues[j]>0) {
		        	Log.v("c","d"+average[0]);
		        	k++;
		        }
		    }
		}
		Log.v("k","k="+k);
        average[0]=average[0]/255;
        Log.v("average","data="+average[0]);
        
        
        //取4个方向上的检测区域
        for (int z =0 ;z<4;z++){
        //positive locate
        if(plocate[z]==null||nlocate[z]==null) break;
		mask = Mat.zeros(img.size(), CvType.CV_8UC1);
		Core.circle(mask,plocate[z],15,new Scalar(255),-1);
		hist=new Mat();
		//计算直方图
		Imgproc.calcHist(list, channels, mask, hist, histSize, ranges);
		k=0.00000001;
		for (int i = 50; i< 200; i++) {
		    double[] histValues = hist.get(i, 0);
		    for (int j = 0; j < histValues.length; j++) {
		        average[1]+=(i*histValues[j]);
		        if (histValues[j]>0) k=k+1;
		    }
		}
		Log.v("k1","k="+k);
        average[1]=average[1]/255;
        Log.v("average","data="+average[1]);
        
        
        //negative locate
        hist=new Mat();
		mask = Mat.zeros(img.size(), CvType.CV_8UC1);
		Core.circle(mask,nlocate[z],15,new Scalar(255),-1);
		//计算直方图
		
		Imgproc.calcHist(list, channels, mask, hist, histSize, ranges);
	    k=0.00000001;
		for (int i = 50; i< 200; i++) {
		    double[] histValues = hist.get(i, 0);
		    for (int j = 0; j < histValues.length; j++) {
		        average[2]+=(i*histValues[j]);
		        if (histValues[j]>0) k++;
		    }
		}
		Log.v("k2","k="+k);
        average[2]=average[2]/255;
        Log.v("average","data="+average[2]);
        
        if (Math.abs(average[2]-average[0])>Math.abs(average[2]-average[1]))
        	flag--;
        else
        	flag++;
        System.out.println("flag:"+flag);
	}
        
        if (flag>0)
        	return true;
        else 
        	return false;
	
	}
	
	public static boolean compareColor(Mat img){
		Mat dst=new Mat();
		dst=Processing.medianBlur(img);
		
		return true;
	}
	
}
