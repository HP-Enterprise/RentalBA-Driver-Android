package com.gjmgr.utils;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

/**
 * messageΪ��ok,fail,empty,datafail��
 * data:Ϊ{status:true,message:data}�е�data
 * object:ΪList<City>��User����
 */
public class HandlerHelper {

	public final static String NoNet = "noNet";
	public final static String Ok = "ok";
	public final static String Empty = "empty";
	public final static String Fail = "fail";
	public final static String DataFail = "datafail";
	public final static String Load = "load";//������Ϣ
	public final static String Cancle = "cancle";//������Ϣ
	public static void sendObject(Handler handler, int what, Object object){
		Message msg = new Message();
		msg.obj = object;
		msg.what = what;
		handler.sendMessage(msg);
	}
	
	public static void sendInt(Handler handler, int what, int message){
		Message msg = new Message();	
		msg.what = what;
		Bundle bundle = new Bundle();
		bundle.putInt("message", message);
		msg.setData(bundle);
		handler.sendMessage(msg);
	}
	
	public static void sendString(Handler handler, int what, String message){
		Message msg = new Message();	
		msg.what = what;
		Bundle bundle = new Bundle();
		bundle.putString("message", message);
		msg.setData(bundle);
		handler.sendMessage(msg);
	}
	
	public static void sendStringData(Handler handler, int what, String message, String data){
		Message msg = new Message();	
		msg.what = what;
		Bundle bundle = new Bundle();
		bundle.putString("message", message);
		bundle.putString("data", data);
		msg.setData(bundle);
		handler.sendMessage(msg);
	}
	
	public static String getString(Message msg){
		
		return msg.getData().getString("message");
	}
	
	public static void sendStringObject(Handler handler, int what, String message, Object object){
		Message msg = new Message();	
		msg.what = what;
		msg.obj = object;
		Bundle bundle = new Bundle();
		bundle.putString("message", message);
		msg.setData(bundle);
		handler.sendMessage(msg);
	}
	
	public static void sendBundle(Handler handler, int what, Bundle bundle){
		Message msg = new Message();	
		msg.what = what;
		msg.setData(bundle);
		handler.sendMessage(msg);
	}
}
