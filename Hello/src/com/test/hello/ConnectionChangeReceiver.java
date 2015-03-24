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
		Toast.makeText(context, "����״̬�ı䣡",Toast.LENGTH_LONG).show();
		boolean success=false;
		//����������ӷ���
		ConnectivityManager connManager=(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		//��ȡwifi��������״̬
		State state=connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
		//�ж��Ƿ�����ʹ��wifi����
		if (State.CONNECTED==state){
			success=true;
			Toast.makeText(context, "wifi���������ӡ�", Toast.LENGTH_LONG).show();
		}
		//��ȡGPRS��������״̬
		state=connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
		//�ж��Ƿ�����ʹ��GPRS����
		if (State.CONNECTED==state){
			success=true;
			Toast.makeText(context, "gprs���������ӡ�", Toast.LENGTH_LONG).show();
		}
		if (!success){
			Toast.makeText(context, "����û�������ϣ�", Toast.LENGTH_LONG).show();
		}
	}

}
