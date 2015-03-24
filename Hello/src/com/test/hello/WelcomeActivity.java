package com.test.hello;
import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;

import android.os.Message;
import android.provider.Settings;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;



public class WelcomeActivity extends Activity {
	
	private Context mcontext=null;
	private LocationManager lm;
	private TextView usertv;
	private TextView pwtv;
	private TextView loadingtv;
	private EditText useret;
	private EditText pwet;
	private ProgressBar pg;
	private Button login;
	private ConnectionChangeReceiver mNetwork = new ConnectionChangeReceiver();
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
	 
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome);
		
		usertv=(TextView)findViewById(R.id.usertv);
		useret=(EditText)findViewById(R.id.useret);
		pwtv=(TextView)findViewById(R.id.pwtv);
		pwet=(EditText)findViewById(R.id.pwet);
		loadingtv=(TextView)findViewById(R.id.loadingtv);
		pg=(ProgressBar)findViewById(R.id.progressBar1);
		login=(Button)findViewById(R.id.loginBtn);
		loadingtv.setVisibility(View.INVISIBLE);
		pg.setVisibility(View.INVISIBLE);
		mcontext=this;
		IntentFilter filer=new IntentFilter();
        filer.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		registerReceiver(mNetwork, filer);
	((LocationApplication) getApplication()).mLocationClient.start();
 	lm=(LocationManager)getSystemService(Context.LOCATION_SERVICE);
	    //判断GPS是否正常启动
	    if(!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)){
	        Toast.makeText(mcontext, "请开启GPS导航...", Toast.LENGTH_SHORT).show();
	        //返回开启GPS导航设置界面
	        new AlertDialog.Builder(mcontext).setTitle("提示").setMessage("请打开GPS定位。").setPositiveButton("确定", null).setNegativeButton("取消", null).show();  
	        Intent settingIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);   
	        startActivityForResult(settingIntent, 0); 
	    }

login.setOnClickListener(new OnClickListener() {
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (useret.getText().toString().equals(pwet.getText().toString())&&useret.getText().toString().equals("admin")){
			System.out.println("登录");
			pg.setVisibility(View.VISIBLE);
			loadingtv.setVisibility(View.VISIBLE);
			startT.start();
			
		}
		else {
			Toast.makeText(mcontext, "用户名或密码错误", Toast.LENGTH_LONG).show();
		}
			
	}
});

}
	
	
	
	Thread startT=new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {

					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				mHandler.sendMessage(mHandler.obtainMessage(0));
			}
		});
	
Handler mHandler=new Handler(){
	
		@Override
		public void handleMessage(Message msg) {
			System.out.println("getin");
			startActivity(new Intent(mcontext,MainActivity.class));
		    finish();
		}
	};

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(mNetwork);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		//load OpenCV engine and init OpenCV library  
		OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_3, getApplicationContext(), iLoaderCallback);
        //OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_9, this, mLoaderCallback);
	}
	
	private BaseLoaderCallback iLoaderCallback = new BaseLoaderCallback(this) {

        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                   System.out.println("加载opencv库成功");
                } break;
                default:
                {
                    super.onManagerConnected(status);
                } break;
            }
        }
    };
}
