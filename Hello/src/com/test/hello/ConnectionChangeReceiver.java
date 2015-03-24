package com.test.hello;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;
import android.widget.Toast;

public class ConnectionChangeReceiver extends BroadcastReceiver {
	private static final String TAG=ConnectionChangeReceiver.class.getSimpleName();
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Toast.makeText(context, "网络状态改变！",Toast.LENGTH_LONG).show();
		boolean success=false;
		//获得网络连接服务
		ConnectivityManager connManager=(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		//获取wifi网络连接状态
		State state=connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
		//判断是否正在使用wifi网络
		if (State.CONNECTED==state){
			success=true;
			Toast.makeText(context, "wifi网络已连接。", Toast.LENGTH_LONG).show();
		}
		//获取GPRS网络连接状态
		state=connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
		//判断是否正在使用GPRS网络
		if (State.CONNECTED==state){
			success=true;
			Toast.makeText(context, "gprs网络已连接。", Toast.LENGTH_LONG).show();
		}
		if (!success){
			Toast.makeText(context, "网络没有连接上！", Toast.LENGTH_LONG).show();
		}
	}

}
