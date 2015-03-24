package com.test.hello;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Vibrator;
import android.provider.Settings;
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

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.BDNotifyListener;//�����õ�λ�����ѹ��ܣ���Ҫimport����
//���ʹ�õ���Χ�����ܣ���Ҫimport������
import com.baidu.location.BDGeofence;
import com.baidu.location.BDLocationStatusCodes;
import com.baidu.location.GeofenceClient;
import com.baidu.location.GeofenceClient.OnAddBDGeofencesResultListener;
import com.baidu.location.GeofenceClient.OnGeofenceTriggerListener;
import com.baidu.location.GeofenceClient.OnRemoveBDGeofencesResultListener;
import com.baidu.location.LocationClientOption.LocationMode;

public class MainActivity extends Activity {
	 private LocationClient mLocationClient;
	 public GeofenceClient mGeofenceClient;
	 private BDLocation bdLocation;
	 private ImageView imageview;  
	 private ImageView rectview;  
	 private Point center;
	 private Bitmap bm1;//����ͼ��
	 private Bitmap bm2;//ԭͼ��
	 private Bitmap bm3;//ROI����ͼ��
	 private Bitmap bmSgd;//���������ͼ��
	 private Mat img;
	 private Mat img2;
	 private Mat img3;
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
	 int a=3 ,b=250;
	 int rf=0;
	 private String value="";
	 private Button btnopen=null;
	 private Button btnfind=null;
	 private Button btnre=null;
	 private Button btnCom=null;
	 private Button btnGeo;
	 private Button btnNotify;
	 private Vibrator mVibrator;
	 private NotifyLister mNotify;
	 private double ld=0,la=0;
	 

		public class NotifyLister extends BDNotifyListener{
		    public void onNotify(BDLocation mlocation, float distance){
		    	mVibrator.vibrate(1000);//�������ѵ��趨λ�ø���
		    	Toast.makeText(mContext, "������", Toast.LENGTH_SHORT).show();
		    	mNotify.SetNotifyLocation(100,100,100,"bd09ll");
		    }
		}
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mGeofenceClient = ((LocationApplication) getApplication()).mGeofenceClient;
		mLocationClient = ((LocationApplication)getApplication()).mLocationClient;
		((LocationApplication) getApplication()).mLocationClient.stop();
		mLocationClient.start();
		btnGeo=(Button)findViewById(R.id.btnGeo);
		mContext=this;
		mVibrator =(Vibrator)getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
		btnopen=(Button)findViewById(R.id.btnOpen);
		btnre=(Button)findViewById(R.id.btnInit);
		btnfind=(Button)findViewById(R.id.btnFind);
		btnNotify=(Button)findViewById(R.id.btnNotify);
		btnCom=(Button)findViewById(R.id.btnCom);
		t1=(TextView)findViewById(R.id.loadingtv);
		t2=(TextView)findViewById(R.id.usertv);
		t3=(TextView)findViewById(R.id.pwtv);
		t4=(TextView)findViewById(R.id.textView4);
		imageview =  (ImageView)findViewById(R.id.imgrew);  
	    rectview=(ImageView)findViewById(R.id.rectView);
	    t4.setText("");
	    /*
	     * ����ͼƬ
	     */
        	 bm1=BitmapFactory.decodeResource(getResources(), R.drawable.te);
        	 bm2=Bitmap.createBitmap(bm1);
             bm2=bm1.copy(Config.ARGB_8888, true); 
             bmSgd=Bitmap.createBitmap(bm1);
             bmSgd=bm1.copy(Config.ARGB_8888, true); 
             imageview.setImageBitmap(bm1); 
        	  /*
        	   * ��������
        	   */
         /*
		//		 seekbar = (SeekBar)findViewById(R.id.seekBar1);
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
        */
	       
	        
	       
	        
         btnNotify.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (btnNotify.getText().toString().equals("����λ������")){
					mNotify = new NotifyLister();
					try{
					ld= mLocationClient.getLastKnownLocation().getLongitude();
					la = mLocationClient.getLastKnownLocation().getLatitude();
					}
					catch(NullPointerException e){
						ld=1.1;
						la=1.2;
					}
					System.out.println("setnotify:ld="+ld+"  la="+la);
					mNotify.SetNotifyLocation(la,ld, 500,"bd09ll");//4����������Ҫλ�����ѵĵ�����꣬���庬������Ϊ��γ�ȣ����ȣ����뷶Χ������ϵ����(gcj02,gps,bd09,bd09ll)
					((LocationApplication) getApplication()).mNotify=mNotify;
					btnNotify.setText("�ر�λ������");
				}
				else{
					btnNotify.setText("����λ������");
					mLocationClient.removeNotifyEvent(((LocationApplication) getApplication()).mNotify);
				}
			}
		});
         
         btnGeo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivityForResult(new Intent(mContext,GeoFenceActivity.class),2);
				//finish();
			}
		});
        //������ͷ
        btnCom.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				InitLocation();
				if (!mLocationClient.isStarted()){
					mLocationClient.start();
					System.out.println("mlc,Start.");
				}
				ScanActivity.msBitmap=null;
				startActivityForResult(new Intent(mContext,ScanActivity.class),1);
			}
		});
        
        //Ԥ����
        btnre.setOnClickListener(new OnClickListener() {
 			@Override
 			public void onClick(View v) {
 				// TODO Auto-generated method stub
 				
				 img =new Mat();
				 img2 =new Mat();
				 img3 =new Mat();
				 //ROI����
				 img5=new Mat();
				 //��ʱ����
				 mcon=new Mat();
				 center=new Point(181,253);
				 //�ҶȻ�ͼ��
				 gray=Processing.procSrc2Gray(bm1);
				 img=Processing.procSrc2Gray(bm1);
				//	Imgproc.equalizeHist(gray, gray);
				//	Imgproc.equalizeHist(img, img);
				 Log.v("mat size", img.size().toString());
				 //��ȡ����Ȥ����
				 img5=img.submat(new Rect(181,253,70,70));
				 bm3=Bitmap.createBitmap(70, 70, Config.ARGB_8888);
	
				 Log.v("img5",img5.size().toString());
				 //Processing.getHist(img5);
				 //Processing.medianBlur(img5);
				 int an=1;
				 Mat element = Imgproc.getStructuringElement(Imgproc.MORPH_RECT,new Size(an*2+1, an*2+1),new  Point(an, an));
				 //��̬ѧ����
				
				 //����Ӧ��ֵ������
				 Imgproc.adaptiveThreshold(img5, img5, 50, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY_INV,25, 1);
			//	 Imgproc.morphologyEx(img5,img5,Imgproc.MORPH_BLACKHAT,element);
				 Imgproc.morphologyEx(img5,img5,Imgproc.MORPH_CLOSE,element);
				// Processing.medianBlur(img);
				 //Imgproc.equalizeHist(img, img);
				 Imgproc.adaptiveThreshold(img, img2, 50, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY_INV,25, 1);
				// Imgproc.morphologyEx(img2,img2,Imgproc.MORPH_BLACKHAT,element);
				 Imgproc.morphologyEx(img2,img2,Imgproc.MORPH_CLOSE,element);
				
				 Utils.matToBitmap(img5, bm3);
				 //�˴�Ϊ����Ȥ����
				 rectview.setImageBitmap(bm3);
				 Utils.matToBitmap(img2, bm1);
				 imageview.setImageBitmap(bm1);
				 System.out.println("bm1:"+bm1.getWidth()+"*"+bm1.getHeight());
 			}
 		});
		
		//���ļ��е�ͼƬ
        btnopen.setOnClickListener(new OnClickListener(){
	 		@Override
	 		public void onClick(View arg0) { 
	 			// TODO Auto-generated method stub
	 			ScanActivity.msBitmap=null;
	 			startActivityForResult(new Intent(mContext,OpenImageActivity.class),3);
	 		}});
        
        //����ʶ��
        btnfind.setOnClickListener(new OnClickListener() {
			 
			@Override
			public void onClick(View v) {
				btnre.performClick();
				
				//����Ȥ������
				img5=Processing.findlunkuo(img5,center);
				mcon=img5.clone();
				lines=new Mat();
				Processing.houghlinesp(mcon, lines, 5);
				Utils.matToBitmap(mcon, bm3);
				rectview.setImageBitmap(bm3);
				List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
				Mat hierarchy = new Mat();
				//����������ȡģʽ
				int mode = Imgproc.RETR_LIST;
				//��������ʶ�𷽷�
				int method = Imgproc.CHAIN_APPROX_NONE;
				//����ʶ��
				Imgproc.findContours(img2, contours, hierarchy, mode, method);

				//Imgproc.cvtColor(mImg, mRgba, Imgproc.COLOR_GRAY2RGBA, 4);
				int contour=0;
				
				img3=img2.clone();
				img3=Mat.zeros(img.size(), CvType.CV_8UC1);
				MatOfPoint2f cf1=new MatOfPoint2f();
				MatOfPoint2f cf2=new MatOfPoint2f();
		
				double area=0;

				Double d1=0.0,d2=0.0,d3=0.0;
				for (contour=0;contour<contours.size();contour++){
					//��ȡ������������
					if(Imgproc.contourArea(contours.get(contour))>area)
					{
						area=Imgproc.contourArea(contours.get(contour));
					}
				
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
				Mat rgbimg=new Mat();
				img2.convertTo(rgbimg, CvType.CV_8UC3);
				Utils.bitmapToMat(bm2, rgbimg);
				int an=1;
				Mat element = Imgproc.getStructuringElement(Imgproc.MORPH_RECT,new Size(an*2+1, an*2+1),new  Point(an, an));
				Imgproc.morphologyEx(img,img,Imgproc.MORPH_DILATE,element); 
				Imgproc.morphologyEx(rgbimg,rgbimg,Imgproc.MORPH_CLOSE,element);
			    if(Processing.compare(gray, center, pLocate, nLocate)&&rf==3){
					Toast.makeText(mContext, "result:true",Toast.LENGTH_LONG).show();
					t4.setText(t4.getText()+"\nоƬʶ��������ȷ��");
					}
				else {
					Toast.makeText(mContext, "result:false", Toast.LENGTH_LONG).show();
					t4.setText(t4.getText()+"\nоƬʶ����������");
					}
				if(Processing.compareRGB(rgbimg, center, pLocate, nLocate)&&rf==3){
					Toast.makeText(mContext, "rgbresult:true",Toast.LENGTH_LONG).show();
					t4.setText(t4.getText()+"\nоƬʶ��������ȷ��");
					}
				else {
					Toast.makeText(mContext, "rgbresult:false", Toast.LENGTH_LONG).show();
					t4.setText(t4.getText()+"\nоƬʶ����������");
					}
				if(Processing.compareHSV(rgbimg, center, pLocate, nLocate)&&rf==3){
					Toast.makeText(mContext, "hsvresult:true",Toast.LENGTH_LONG).show();
					t4.setText(t4.getText()+"\nоƬʶ��������ȷ��");

					}
				else {
					Toast.makeText(mContext, "hsvresult:false", Toast.LENGTH_LONG).show();
					t4.setText(t4.getText()+"\nоƬʶ����������");
					}
				
				
				
			}
		});
	}
	

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode==RESULT_OK){
			//������ɺ�
			if (requestCode==1){
				if (btnNotify.getText().equals("�ر�λ������")){
					ld= mLocationClient.getLastKnownLocation().getLongitude();
					la = mLocationClient.getLastKnownLocation().getLatitude();
					System.out.println("setnotify:ld="+ld+"  la="+la);
					mNotify.SetNotifyLocation(la,ld, 500,"bd09ll");//4����������Ҫλ�����ѵĵ�����꣬���庬������Ϊ��γ�ȣ����ȣ����뷶Χ������ϵ����(gcj02,gps,bd09,bd09ll)
					((LocationApplication) getApplication()).mNotify=mNotify;
				}
				mLocationClient.registerNotify(((LocationApplication) getApplication()).mNotify);
				mLocationClient.requestLocation();		
				bdLocation=mLocationClient.getLastKnownLocation();
				try{
				t4.setText("��λλ��:"+bdLocation.getAddrStr());}
				catch(NullPointerException x){
					System.out.println("nullpointerexception");
				}
		         if (ScanActivity.msBitmap!=null){
		        	 bm1=ScanActivity.msBitmap;
		             bm2=Bitmap.createBitmap(bm1);
		             bm2=bm1.copy(Config.ARGB_8888, true); 
		           //  imageview.setImageBitmap(bm1); 
		          }
		         if (ScanActivity.psBitmap!=null){
		        	 bmSgd=ScanActivity.psBitmap;
		        	 imageview.setImageBitmap(bmSgd);
		         }
		       //  mLocationClient.stop();
			}
			//���ش򿪵�ͼƬ
			if (requestCode==3){
			
			    value= data.getStringExtra("selectFile");
			    BitmapFactory.Options opts=new BitmapFactory.Options();
				opts.inJustDecodeBounds=true;
		         if (ScanActivity.msBitmap!=null){
		        	bm1=ScanActivity.msBitmap;
		            bm2=Bitmap.createBitmap(bm1);
		            bm2=bm1.copy(Config.ARGB_8888, true); 
		            imageview.setImageBitmap(bm1); 
		         }
		         if (ScanActivity.psBitmap!=null){
		        	 bmSgd=ScanActivity.psBitmap;
		         }
		         if (value!=null){
		               bm1=BitmapFactory.decodeFile(value,opts);
		               opts.inSampleSize=BitmapSimple.computeSampleSize(opts, 600, 854*600);
		               opts.inJustDecodeBounds=false;
		        	   bm1=BitmapFactory.decodeFile(value,opts);
		        	  // bm1=BitmapFactory.decodeResource(getResources().getXml(),)
		               bm2=Bitmap.createBitmap(bm1);
		               bm2=bm1.copy(Config.ARGB_8888, true); 
		               bmSgd=Bitmap.createBitmap(bm1);
		               bmSgd=bm1.copy(Config.ARGB_8888, true); 
		               imageview.setImageBitmap(bm1); 
		               System.out.println("c������:"+opts.inSampleSize);
		               //Toast.makeText(mContext, "3", Toast.LENGTH_LONG);
		        	}
			}
		}
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
		 
		 if (item.getItemId()==R.id.m1) {
			 String filename=Environment.getExternalStorageDirectory()+"/aphoto/"+System.currentTimeMillis()+"p.jpg";
			 String sfilename=Environment.getExternalStorageDirectory()+"/aphoto/"+"s"+System.currentTimeMillis()+"p.jpg";
			 File dirfile = new File(Environment.getExternalStorageDirectory()+"/aphoto");
			 if (!dirfile.exists())
				 dirfile.mkdir();
			 
			 File file = new File(filename);
			 File sfile = new File(sfilename);
			 try {
				 
				bm2.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(file));
				bmSgd.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(sfile));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			 Toast.makeText(mContext, "����ɹ�������·����"+filename, Toast.LENGTH_LONG).show();
		 }
		 else if (item.getItemId()==R.id.item1){
			 imageview.setImageBitmap(bm2); 
		 }
		 else if (item.getItemId()==R.id.item2sgd){
			 Mat sgd=new Mat();
			 Mat sgdMat=new Mat();
			 Utils.bitmapToMat(bmSgd, sgd);
			 Mat element = Imgproc.getStructuringElement(Imgproc.MORPH_RECT,new Size(1*2+1, 1*2+1),new  Point(1, 1));
			 //��̬ѧ����
	         Imgproc.morphologyEx(sgd,sgd,Imgproc.MORPH_OPEN,element);
			 sgdMat=sgd.submat(new Rect(181,253,70,70)).clone();
			 bm3=Bitmap.createBitmap(70, 70, Config.ARGB_8888);
			 sgdMat=Processing.procSrc2Gray(sgdMat);
			 Utils.matToBitmap(sgdMat, bm3);
			 rf=Processing.getHist(sgdMat);
				if (rf==0){
					t4.setText("����Ӱ,ʪˮ��");
					}
				else if (rf==1) {
					t4.setText("����Ӱ,��ˮ��");
					}
					else if (rf==2) {
						t4.setText("û����Ӱ,ʪˮ��");
					}
					else if (rf==3){
						t4.setText("û����Ӱ,��ˮ��");
					}
				
			 rectview.setImageBitmap(bm3);
		 }
		 
		return super.onOptionsItemSelected(item);
	}

	@Override  
	    protected void onResume() {  
	        // TODO Auto-generated method stub  
	        super.onResume();  
	     
	    }  
	
	private void InitLocation(){
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);//���ö�λģʽ
		option.setCoorType("bd09ll");//���صĶ�λ����ǰٶȾ�γ�ȣ�Ĭ��ֵgcj02
		option.setScanSpan(5000);//���÷���λ����ļ��ʱ��Ϊ5000ms
		option.setIsNeedAddress(true);//���صĶ�λ���������ַ��Ϣ
		option.setNeedDeviceDirect(true);//���صĶ�λ��������ֻ���ͷ�ķ���
		//option.setOpenGps(true);
		mLocationClient.setLocOption(option);
	}

}
