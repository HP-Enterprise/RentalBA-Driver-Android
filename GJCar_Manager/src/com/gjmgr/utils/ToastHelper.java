package com.gjmgr.utils;

import android.content.Context;
import android.widget.Toast;

public class ToastHelper {

	public static void showToastLodng(Context context, String message){
		Toast d =	Toast.makeText(context, message, Toast.LENGTH_SHORT);
		d.show();
	}
	
	/** Toast��Ϣ��ʾ��,Long*/
	public static void showToastLong(Context context, String message){
		Toast.makeText(context, message, Toast.LENGTH_LONG).show();
	}
	
	/** Toast��Ϣ��ʾ��,Short*/
	public static void showToastShort(Context context, String message){
		Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
	}
	
	/** ���粻����*/
	public static void showNoNetworkToast(Context context){
	    
	    Toast.makeText(context, "��ǰ���粻���ã���������...", Toast.LENGTH_SHORT).show();
			
	}
	
	/** ��������ʧ��*/
	public static void showSendDataFailToast(Context context){
	    
	    Toast.makeText(context, "���ݷ���ʧ��...", Toast.LENGTH_SHORT).show();
			
	}
	
}
