package com.test.hello;
import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;



public class WelcomeActivity extends Activity {
	private MyHandler mHandler = null;
	private Context mcontext=null;
	
	/*
	//OpenCV库加载并初始化成功后的回调函数  
    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    
                	Log.i("TAG", "OpenCV loaded successfully");

                } break;
                default:
                {
                    super.onManagerConnected(status);
                } break;
            }
        }
    }; 
 */
	 private BaseLoaderCallback iLoaderCallback = new BaseLoaderCallback(this) {

	        @Override
	        public void onManagerConnected(int status) {
	            switch (status) {
	                case LoaderCallbackInterface.SUCCESS:
	                {
	                   
	                   
	                } break;
	                default:
	                {
	                    super.onManagerConnected(status);
	                } break;
	            }
	        }
	    };
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome);
		mcontext=this;
		
	
	
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				mHandler = new MyHandler(Looper.getMainLooper());
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				mHandler.sendMessage(mHandler.obtainMessage(0));
			}
		}).start();
	}
	
	class MyHandler extends Handler {
		public MyHandler() {
		}
		public MyHandler(Looper looper) {
			super(looper);
		}
		
		public void handleMessage(Message msg) {
			startActivity(new Intent(mcontext,OpenImageActivity.class));
		    finish();
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		   //load OpenCV engine and init OpenCV library  
		OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_3, getApplicationContext(), iLoaderCallback);
        //OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_9, this, mLoaderCallback);
	}
	
	
}
