package com.test.hello;
/**
 * 相机调用
 */
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;

public class UserCamera implements SurfaceHolder.Callback{
	public Camera sCamera=null;
	private SurfaceHolder holder = null;
    private int width,height;
    private Camera.PreviewCallback previewCallback;
    
    public UserCamera(SurfaceHolder holder,int w,int h,Camera.PreviewCallback previewCallback) {
		this.holder = holder;  
		this.holder.addCallback(this);  
        width=w;
        height=h;
        this.previewCallback=previewCallback;
	}
    
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		int bestWidth=0;
		int bestHeight=0;
		int LARGEST_WIDTH=600;
		int LARGEST_HEIGHT=800;
		Camera.Parameters parameters = sCamera.getParameters();  
		List<Camera.Size> previewSizes = parameters.getSupportedPreviewSizes();
		if (previewSizes.size() > 1)  
		{  
		    Iterator<Camera.Size> cei = previewSizes.iterator();
		    while (cei.hasNext())  
		    { 
		        Camera.Size aSize = cei.next(); 
		        
		        Log.v("SNAPSHOT","Checking " + aSize.width + " x " + aSize.height);
		        if (aSize.width > bestWidth && aSize.width <= LARGEST_WIDTH  
		            && aSize.height > bestHeight && aSize.height <= LARGEST_HEIGHT)
		       {
		            // 到目前为止它是最大的而且没有超过屏幕尺寸
		            bestWidth = aSize.width; 
		            bestHeight = aSize.height;
		       }
		    }
		        
		    if (bestHeight != 0 && bestWidth != 0)
		    { 
		        Log.v("SNAPSHOT", "Using " + bestWidth + " x " + bestHeight);
		        parameters.setPreviewSize(bestWidth, bestHeight);
		      //  cameraView.setLayoutParams(new LinearLayout.LayoutParams( bestWidth,bestHeight));
		    }
		} 
		        
        //parameters.setPreviewSize(480,854);//设置尺寸  
        parameters.setPictureFormat(ImageFormat.JPEG);
        parameters.setPictureSize(this.width, this.height); // 设置保存的图片尺寸
        sCamera.setParameters(parameters);  
        sCamera.startPreview();//开始预览
        Log.e("Camera","surfaceChanged");
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		sCamera = Camera.open();//启动服务  
        try {  
        	 sCamera.setDisplayOrientation(90);
            sCamera.setPreviewDisplay(holder);//设置预览 
            Log.e("Camera","surfaceCreated");
        } catch (IOException e) {  
            sCamera.release();//释放  
            sCamera = null;  
        }
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		sCamera.setPreviewCallback(null);
		sCamera.stopPreview();//停止预览  
		sCamera.release();//释放 
        sCamera = null;
        Log.e("Camera","surfaceDestroyed");
	}
	
	/**
	 * 自动对焦并回调Camera.PreviewCallback
	 */
	public void AutoFocusAndPreviewCallback()
	{
		if(sCamera!=null)
			sCamera.autoFocus(mAutoFocusCallBack);
	}
	
	private Camera.AutoFocusCallback mAutoFocusCallBack=new Camera.AutoFocusCallback() {
		@Override
		public void onAutoFocus(boolean success, Camera camera) {
			// TODO Auto-generated method stub
			 if (success) {  //对焦成功，回调Camera.PreviewCallback
				 System.out.println("call back focus");
	            	sCamera.setOneShotPreviewCallback(previewCallback); 
	            }  
		}
	};
	
	
}
