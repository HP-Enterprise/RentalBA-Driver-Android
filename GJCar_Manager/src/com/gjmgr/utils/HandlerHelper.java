package com.gjmgr.utils;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

/**
 * message为：ok,fail,empty,datafail等
 * data:为{status:true,message:data}中的data
 * object:为List<City>或User类型
 */
public class HandlerHelper {

	public final static String NoNet = "noNet";
	public final static String Ok = "ok";
	public final static String Empty = "empty";
	public final static String Fail = "fail";
	public final static String DataFail = "datafail";
	public final static String Load = "load";//加载信息
	public final static String Cancle = "cancle";//加载信息
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
