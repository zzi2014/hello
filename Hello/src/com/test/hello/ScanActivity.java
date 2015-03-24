package com.test.hello;

/**
 * 扫描界面
 */
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.graphics.Bitmap.CompressFormat;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;



public class ScanActivity extends Activity {

	private SurfaceView surfaceview=null;
	private UserCamera uCamera=null;
	private ImageView imgView;
	private Button btnshot;
	public  static Bitmap msBitmap=null;
	public  static Bitmap psBitmap=null;
	private Timer mTimer;
	String strResult=" ";
	boolean ishot=false;
	public Context mcontext=this;
	private MyTimerTask mTimerTask;
	public int detLeft,detTop,detWidth,detHeight=0;
	boolean setLength=true;
	public int width,height;
	Point p = new Point();
	public Handler mhandler;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cameralayout);
		surfaceview =(SurfaceView)this.findViewById(R.id.surfaceView1);
		//centerView=(View)findViewById(R.id.detView);
		imgView=(ImageView)findViewById(R.id.imgrew);
		btnshot=(Button)findViewById(R.id.btnshot);
    	getWindowManager().getDefaultDisplay().getSize(p);
    	width=320;
		height=480;
    	System.out.println(p.x+" dfsdf "+p.y);
    	
	uCamera = new UserCamera(surfaceview.getHolder(), width, height,
				new MyPictureCallback(),previewCallback);
		mTimer = new Timer();
		mTimerTask = new MyTimerTask();
		//mTimer.schedule(mTimerTask, 2000, 800);
		btnshot.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mTimer.cancel();
				uCamera.sCamera.cancelAutoFocus();
				ishot=true;
				Parameters para=uCamera.sCamera.getParameters();
			//	para.setFlashMode(Parameters.FLASH_MODE_ON);
				uCamera.sCamera.setParameters(para);
				uCamera.AutoFocusAndPreviewCallback();
				
			}
		});
	
	}
	
	/**
	 * 任务计时器
	 */
	class MyTimerTask extends TimerTask {
		@Override
		public void run() {
			Parameters para=uCamera.sCamera.getParameters();
			para.setFlashMode(Parameters.FLASH_MODE_TORCH);
			uCamera.sCamera.setParameters(para);
			uCamera.AutoFocusAndPreviewCallback();
			
		}
	}

	 public final class MyPictureCallback implements PictureCallback {  
		  
	        @Override  
	        public void onPictureTaken(byte[] data, Camera camera) {  
	        		Camera.Parameters parameters = uCamera.sCamera.getParameters();
	        	    Size size = parameters.getPreviewSize();
	            	psBitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
			   	    System.out.println(psBitmap.getWidth()+"x"+psBitmap.getHeight());
			   	    Matrix matrix = new Matrix();  
		            matrix.reset();  
		            matrix.setRotate(90);  
					psBitmap=Bitmap.createBitmap(psBitmap, 0, 0, size.width, size.height, matrix, true);
					//显示灰度图
					imgView.setImageBitmap(psBitmap);	
					System.out.println("bitmapwide:"+psBitmap.getWidth()+"heigh"+psBitmap.getHeight());
					Intent intent=new Intent(mcontext,MainActivity.class);
					File file = new File(Environment.getExternalStorageDirectory(), "pp.jpg");
					System.out.println(file.toString());
					FileOutputStream outStream;
					if (ScanActivity.psBitmap!=null&&ishot){
					try {
						outStream = new FileOutputStream(file);
						ScanActivity.psBitmap.compress(CompressFormat.JPEG, 100, outStream);
						outStream.close();
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					setResult(RESULT_OK,intent);
					finish();	   

					}
	        } 
	    }  
	/**
	 *  自动对焦后输出图片
	 */
	private Camera.PreviewCallback previewCallback = new Camera.PreviewCallback() {
		@Override
		public void onPreviewFrame(byte[] data, Camera arg1) {
			//取得指定范围的帧的数据
					System.out.println("take photo:"+data.length);
			        Camera.Parameters parameters = uCamera.sCamera.getParameters();
			        Size size = parameters.getPreviewSize();
			        YuvImage image = new YuvImage(data, parameters.getPreviewFormat(),
			                size.width, size.height, null);
			        ByteArrayOutputStream outstr = new ByteArrayOutputStream();
			        image.compressToJpeg(
			                new Rect(0, 0, image.getWidth(), image.getHeight()), 50,
			                outstr);
			        Matrix matrix = new Matrix();  
		            matrix.reset();  
		            matrix.setRotate(90);  
			   	    msBitmap = BitmapFactory.decodeByteArray(outstr.toByteArray(), 0, outstr.size());
					msBitmap=Bitmap.createBitmap(msBitmap, 0, 0, msBitmap.getWidth(), msBitmap.getHeight()	, matrix, true);
					//显示灰度图
					imgView.setImageBitmap(msBitmap);	
					System.out.println("bitmapwide:"+msBitmap.getWidth()+"heigh"+msBitmap.getHeight());
					
					File file = new File(Environment.getExternalStorageDirectory(), "p.jpg");
					System.out.println(file.toString());
					FileOutputStream outStream;
					if (msBitmap!=null&&ishot){
					try {
						outStream = new FileOutputStream(file);
					msBitmap.compress(CompressFormat.JPEG, 100, outStream);
						outStream.close();
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					}
		}
	};

}
