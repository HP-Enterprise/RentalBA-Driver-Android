package com.gjmgr.utils;

import android.content.Context;
import android.widget.Toast;

public class ToastHelper {

	public static void showToastLodng(Context context, String message){
		Toast d =	Toast.makeText(context, message, Toast.LENGTH_SHORT);
		d.show();
	}
	
	/** Toast消息提示框,Long*/
	public static void showToastLong(Context context, String message){
		Toast.makeText(context, message, Toast.LENGTH_LONG).show();
	}
	
	/** Toast消息提示框,Short*/
	public static void showToastShort(Context context, String message){
		Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
	}
	
	/** 网络不可用*/
	public static void showNoNetworkToast(Context context){
	    
	    Toast.makeText(context, "当前网络不可用，请检查网络...", Toast.LENGTH_SHORT).show();
			
	}
	
	/** 发送数据失败*/
	public static void showSendDataFailToast(Context context){
	    
	    Toast.makeText(context, "数据发送失败...", Toast.LENGTH_SHORT).show();
			
	}
	
}
