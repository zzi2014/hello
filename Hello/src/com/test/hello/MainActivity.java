package com.test.hello;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfInt4;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.RotatedRect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class MainActivity extends Activity {
	 private ImageView imageview;  
	 private ImageView rectview;  
	 private Point center;
	 private Bitmap bm1;//处理图像
	 private Bitmap bm2;//原图像
	 private Bitmap bm3;//ROI区域图像
	 private int mwidth=0,mheigh=0;
	 private Mat img;
	 private Mat img2;
	 private Mat img3;
	 private Mat img4;
	 private Mat img5;
	 private Mat gray;
	 private Mat lines;
	 private Mat mcon;
	 private Point[] pLocate=new Point[4];
	 private Point[] nLocate=new Point[4];
	 private Context mContext=null;
	 private SeekBar seekbar;
	 private SeekBar seekbar2;
	 private TextView t1;
	 private TextView t2;
	 private TextView t3;
	 private TextView t4;
	 private Rect box;
	 int a=3 ,b=250;
	 private String value="";
	 private Button btnopen=null;
	 private Button btnfind=null;
	 private Button btnre=null;
	 private Button btnCom=null;

	    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mContext=this;
		btnopen=(Button)findViewById(R.id.btnOpen);
		btnre=(Button)findViewById(R.id.btnInit);
		btnfind=(Button)findViewById(R.id.btnFind);
		btnCom=(Button)findViewById(R.id.btnCom);
		t1=(TextView)findViewById(R.id.textView1);
		t2=(TextView)findViewById(R.id.textView2);
		t3=(TextView)findViewById(R.id.textView3);
		t4=(TextView)findViewById(R.id.textView4);
		imageview =  (ImageView)findViewById(R.id.imgrew);  
	    rectview=(ImageView)findViewById(R.id.rectView);

	    t4.setText("");
		Intent intent = getIntent();
	    value= intent.getStringExtra("selectFile");
	   
	    BitmapFactory.Options opts=new BitmapFactory.Options();
		opts.inJustDecodeBounds=true;
         if (ScanActivity.msBitmap!=null){
        	bm1=ScanActivity.msBitmap;
         }
         else{
        	 if (value!=null){
               bm1=BitmapFactory.decodeFile(value,opts);
               opts.inSampleSize=BitmapSimple.computeSampleSize(opts, 600, 800*600);
               opts.inJustDecodeBounds=false;
        	   bm1=BitmapFactory.decodeFile(value,opts);
        	}
        	 else {
                 bm1=BitmapFactory.decodeResource(getResources(), R.drawable.test, opts);
                 opts.inSampleSize=BitmapSimple.computeSampleSize(opts, 600, 800*600);
                 opts.inJustDecodeBounds=false;
                 bm1=BitmapFactory.decodeResource(getResources(), R.drawable.test, opts);
        	 }
        	   System.out.println("采样率:"+opts.inSampleSize);
         }
         bm2=Bitmap.createBitmap(bm1);
         bm2=bm1.copy(Config.ARGB_8888, true);
	    
        //感兴趣区域bitmap
       // bm3=BitmapFactory.decodeFile("/storage/sdcard0/DCIM/Camera/IMG_20140714_151303.jpg",opts);
        imageview.setImageBitmap(bm1); 
   
				 img =new Mat();
				 img2 =new Mat();
				 img3 =new Mat();
				 //感兴趣区域矩阵
				 img4=new Mat();
				 //ROI矩阵
				 img5=new Mat();
				 //临时矩阵
				 mcon=new Mat();
				 center=new Point(181,253);
				 //灰度化图像
				 gray=Processing.procSrc2Gray(bm2);
				 img=Processing.procSrc2Gray(bm1);
				 Log.v("mat size", img.size().toString());
				 img5=img.submat(new Rect(181,253,70,70));
				 bm3=Bitmap.createBitmap(70, 70, Config.ARGB_8888);
				// img5=procSrc2Gray(img5);
				 Utils.matToBitmap(img5, bm3);
				 Log.v("img5",img5.size().toString());
				 mwidth=bm1.getWidth();
				 mheigh=bm1.getHeight();
				 Imgproc.adaptiveThreshold(img5, img5, 100, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY_INV,21, 1);
				 int an=1;
				 Mat element = Imgproc.getStructuringElement(Imgproc.MORPH_RECT,new Size(an*2+1, an*2+1),new  Point(an, an));
				 Imgproc.morphologyEx(img5,img5,Imgproc.MORPH_CLOSE,element);
				 Utils.matToBitmap(img5, bm3);
				 rectview.setImageBitmap(bm3);
				// img4=procSrc2Gray(bm3);
				 Utils.matToBitmap(img, bm1);
				/* //canny边缘检测
				 Imgproc.Canny(img, img2, a,b,3,true);  
				 Imgproc.Canny(img4, img5, a,b,3,true);
				 img2.convertTo(img2, CvType.CV_8UC1); */
				 Imgproc.adaptiveThreshold(img, img2, 100, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY_INV,15, 1);
				 Imgproc.morphologyEx(img2,img2,Imgproc.MORPH_CLOSE,element);
				 Utils.matToBitmap(img2, bm1);
				 imageview.setImageBitmap(bm1);
				 System.out.println("bm1:"+bm1.getWidth()+"*"+bm1.getHeight());
	
     
        seekbar = (SeekBar)findViewById(R.id.seekBar1);
        seekbar.setMax(255);
        seekbar.setProgress(250);
        seekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				  b=progress;
				  Imgproc.Canny(img, img2, a,b,3,true);
				  int an=3;
				  Mat element = Imgproc.getStructuringElement(Imgproc.MORPH_RECT,new  Size(an*2+1, an*2+1),new  Point(an, an));
				  Imgproc.morphologyEx(img2,img2,Imgproc.MORPH_CLOSE,element);
				  Utils.matToBitmap(img2, bm1);
				  imageview.setImageBitmap(bm1);
			}
		});
        seekbar2 = (SeekBar)findViewById(R.id.seekBar2);
        seekbar2.setMax(20);
        seekbar2.setProgress(3);
        
        seekbar2.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
					a=progress;
					mcon=img5.clone();
					Processing.houghlinesp(mcon, lines, a);
				    Utils.matToBitmap(mcon, bm3);
				    rectview.setImageBitmap(bm3);
				    Log.v("avalue",Integer.toString(a));
			} 
		}
        );
        
        btnCom.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ScanActivity.msBitmap=null;
				startActivity(new Intent(mContext,ScanActivity.class));
	 			bm1.recycle();
	 			bm2.recycle();
	 			bm3.recycle();
	 			finish();
			}
		});
        
        btnre.setOnClickListener(new OnClickListener() {
 			@Override
 			public void onClick(View v) {
 				// TODO Auto-generated method stub
 				imageview.setImageBitmap(bm2);
 			}
 		});
		
		
        btnopen.setOnClickListener(new OnClickListener(){
	 		@Override
	 		public void onClick(View arg0) { 
	 			// TODO Auto-generated method stub
	 			ScanActivity.msBitmap=null;
	 			startActivity(new Intent(mContext,OpenImageActivity.class));
	 			bm1.recycle();
	 			bm2.recycle();
	 			bm3.recycle();
	 			finish();
	 		}});
        
        
        btnfind.setOnClickListener(new OnClickListener() {
			 
			@Override
			public void onClick(View v) {
				//感兴趣区域处理
				img5=Processing.findlunkuo(img5,center);
				mcon=img5.clone();
				lines=new Mat();
				Processing.houghlinesp(mcon, lines, 5);
				Utils.matToBitmap(mcon, bm3);
				rectview.setImageBitmap(bm3);
				
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
				Imgproc.findContours(img2, contours, hierarchy, mode, method);
				
				//Imgproc.cvtColor(mImg, mRgba, Imgproc.COLOR_GRAY2RGBA, 4);
				int contour=0;
				
				img3=img2.clone();
				
				img3=Mat.zeros(img.size(), CvType.CV_8UC1);
				
				MatOfPoint2f cf1=new MatOfPoint2f();
				MatOfPoint2f cf2=new MatOfPoint2f();
				int j=0;
				double area=0;
				Double ra=0.0;
				Double d1=0.0,d2=0.0,d3=0.0;
				for (contour=0;contour<contours.size();contour++){
					//获取最大轮廓的面积
					if(Imgproc.contourArea(contours.get(contour))>area)
					{
						area=Imgproc.contourArea(contours.get(contour));
						j=contour;
					}
					ra=Imgproc.contourArea(contours.get(j));
					if(Imgproc.contourArea(contours.get(contour))>300){
						
						if (d1==0.0)
							d1=Imgproc.contourArea(contours.get(contour));
						else 
							d2=Imgproc.contourArea(contours.get(contour));
						contours.get(contour).convertTo(cf1,CvType.CV_32FC2);
						Imgproc.approxPolyDP(cf1, cf2, 2, true);
						Imgproc.drawContours(img3, contours, contour, new Scalar(255,150,100));
					}
					d3=d1/d2;					
				}
				
				t1.setText(d1.toString());
				t2.setText(d2.toString());
				t3.setText(d3.toString());
				//Toast.makeText(mContext,ra.toString(),1).show();
				pLocate=Processing.locate(img3,lines, center, 100);
				nLocate=Processing.locate(img3,lines, center, 160);
				Core.circle(img3, center, 10, new Scalar(244,12,23),-1);
				
				
				Utils.matToBitmap(img3, bm1);
				imageview.setImageBitmap(bm1);	
				
				
				if(Processing.compare(gray, center, pLocate, nLocate)){
					Toast.makeText(mContext, "result:true",1).show();
					t4.setText("芯片识别结果：正确。");
				}
					
				else {
					Toast.makeText(mContext, "result:false", 1).show();
					t4.setText("芯片识别结果：错误。");
					}
				
			}
		});

	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);

		return true;
	}
	
	 @Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		 String filename=Environment.getExternalStorageDirectory()+"/aphoto/"+System.currentTimeMillis()+"p.jpg";
		 if (item.getItemId()==R.id.m1) {
			 File dirfile = new File(Environment.getExternalStorageDirectory()+"/aphoto");
			 if (!dirfile.exists())
				 dirfile.mkdir();
			 
			 File file = new File(filename);
			 try {
				 
				bm2.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(file));
			
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 Toast.makeText(mContext, "保存成功。保存路径："+filename, 1).show();
		 }
 
		return super.onOptionsItemSelected(item);
	}

	@Override  
	    protected void onResume() {  
	        // TODO Auto-generated method stub  
	        super.onResume();  
	     
	    }  

}
