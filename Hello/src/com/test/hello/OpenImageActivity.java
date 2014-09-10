package com.test.hello;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;



public class OpenImageActivity extends Activity {
	
	  private ListView mflv=null;
	  private Context mContext=null;
	  private ArrayAdapter<String> myAdapter=null;
	  private String filepath="/";
	  private String[] allFileNames =null;
	  private String[] defaultPath={"/"};
	  private Button btnBack=null;
	  private TextView tvFile=null;
	  private Button retBtn=null;
	  @Override
		    public void onCreate(Bundle savedInstanceState) {
		        super.onCreate(savedInstanceState);
		        setContentView(R.layout.openfile);
		        mflv=(ListView)findViewById(R.id.listView1);
		        mContext=this;
		        myAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,getFileNames(filepath));
		        mflv.setAdapter(myAdapter);
		        allFileNames=getFileNames(filepath);
		        btnBack=(Button)findViewById(R.id.retBtn);
		        retBtn=(Button)findViewById(R.id.backBtn);
		        tvFile=(TextView)findViewById(R.id.dicView);
		        
		        
		        /*
		         * 返回上一�?
		         */
		        btnBack.setOnClickListener(new OnClickListener(){
		 		@Override
		 		public void onClick(View arg0) {
		 			// TODO Auto-generated method stub
		 			if (!filepath.equals("/")){
		 			File curFile=new File(filepath);
		 			filepath=curFile.getParent();
		 			if (!filepath.equals("/"))
		 				filepath+="/";
		 			tvFile.setText("当前路径:"+filepath);
		 			Log.d("parentpath", filepath);
		 			allFileNames=getFileNames(filepath);
		 			if(allFileNames==null) { 
		 				allFileNames=defaultPath;
		 			}
		 			myAdapter=new ArrayAdapter<String>(mContext,android.R.layout.simple_list_item_1,allFileNames);
		 			mflv.setAdapter(myAdapter);
		 		}
		 		}
		 		});
		        
		        
		        /*
		         * �?���?
		         */
		        retBtn.setOnClickListener(new OnClickListener(){
			 		@Override
			 		public void onClick(View arg0) {
			 			// TODO Auto-generated method stub
			 			startActivity(new Intent(mContext,MainActivity.class));
			 		}});
		        
		        
		        /*
		         * 选中文件
		         */
		       mflv.setOnItemClickListener(new OnItemClickListener(){

		 			@Override
		 			public void onItemClick(AdapterView<?> arg0, View arg1, int numb,
		 					long arg3) {
		 				String  selectpath="";
		 				if (allFileNames[numb].equals("/"))
		 				{
		 					btnBack.performClick();
		 					return;
		 				}
		 				
		 				selectpath=filepath+allFileNames[numb]+"/";
		 				File selectFile=new File(selectpath);
		 				

		 				//如果是文件夹
		 				if(selectFile.isDirectory()){
		 					filepath=selectpath;
		 					Log.d("isdirectory", filepath);
		 					if(selectFile.list()!=null) 
		 						//获取当前路径文件
		 					allFileNames=getFileNames(selectpath);
		 					else allFileNames=defaultPath;
		 					
		 					myAdapter=new ArrayAdapter<String>(mContext,android.R.layout.simple_list_item_1,allFileNames);
		 					mflv.setAdapter(myAdapter);
		 					tvFile.setText("当前路径:"+filepath);
		 				}
		 				else {	 					
		 					if(allFileNames[numb].endsWith(".jpg")||allFileNames[numb].endsWith(".jpeg")||allFileNames[numb].endsWith(".gif")||allFileNames[numb].endsWith(".JPG")||allFileNames[numb].endsWith(".png")||allFileNames[numb].endsWith(".jpeg"))
		 						{
		 						Intent intent = new Intent();
		 		                //Intent传�?参数
		 		                intent.putExtra("selectFile", selectFile.getAbsolutePath());
		 		                intent.setClass(mContext, MainActivity.class);
		 		                startActivity(intent);
		 		                finish();
		 						}
		 					else Toast.makeText(mContext, "文件无法识别", Toast.LENGTH_SHORT).show();
		 				
		 				}
		 			}});
		         
		     }
		         
		    
		    /*
		     * 获取路径下的�?��文件�?
		     */
		    @SuppressLint("DefaultLocale")
			private String[] getFileNames(String sr){
		    	
		    	List<File> files = Arrays.asList(new File(sr).listFiles((new MyFilter(".jpg"))));
		    	Collections.sort(files, new Comparator<File>(){
		    	    @SuppressLint("DefaultLocale")
					@Override
		    	    public int compare(File o1, File o2) {
		    		if(o1.isDirectory() && o2.isFile())
		    		    return -1;
		    		if(o1.isFile() && o2.isDirectory())
		    	    	    return 1;
		    		return o1.getName().toLowerCase().compareTo(o2.getName().toLowerCase());
		    	    }
		    	});
		    	
		    	
		    	
		    	ArrayList<String> fileName = new ArrayList<String>();
		
		    	for(File f : files)
		    		{fileName.add(f.getName());

		    		}
		    	/*
	
		    	String[] fileNames = new String[fileName.size()];
		    	for (int j=0;j<fileName.size();j++)
		    		fileNames[j]=fileName.get(j);
		    		
		    	*/
		    	
		    	//String[] fileNames=fileName.toArray(new String[]{});
		    	String[] fileNames = new String[fileName.size()];
		    	fileName.toArray(fileNames);
		        return fileNames;
		     	
		     	}
		     	
		    
		  
		    
		    
		    /*
		   	 *文件过滤�?
		     * */
		    static class MyFilter implements FilenameFilter{  
		        private String type;  
		        public MyFilter(String type){  
		            this.type = type;  
		        }  
		        public boolean accept(File dir,String name){  
		        	 String files = dir.getAbsolutePath();
		        	
		        	 files=files+"/"+name+"/";
		        
		        	 File nfile=new File(files);
		        	 if (name.startsWith("."))
		        		 return false;
		        	
		       return (name.toLowerCase().endsWith(type)||nfile.isDirectory());  
		        }  
		    }  
		}

