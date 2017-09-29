package com.gjmgr.app.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import com.gjmgr.activity.user.Activity_Order_AllList;
import com.gjmgr.app.R;
import com.gjmgr.data.data.Public_Data;

import java.io.IOException;
import java.util.Iterator;

import cn.jpush.android.api.JPushInterface;

/**
 * 自定义接收器
 * 
 * 如果不定义这�? Receiver，则�?
 * 1) 默认用户会打�?主界�?
 * 2) 接收不到自定义消�?
 */
public class MyReceiver extends BroadcastReceiver {
	
	private static final String TAG = "JPush";
	private MediaPlayer mp = new MediaPlayer();
	
	@Override
	public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
		Log.d(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " );
		System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
            //send the Registration Id to your server...
            System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa1");           
        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
        	Log.d(TAG, "[MyReceiver] 接收到推送下来的自定义消�?: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
        
        	System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa2");
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知");
            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);
            System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa3");
            
            Public_Data.isReceiveTask = true;
            
            mp.release();
         
            mp=MediaPlayer.create(context, R.raw.fv);//重新设置要播放的音频
 
            try {System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxx1");
//				mp.prepare();
				mp.start();//�?始播放hint.setText("正在播放音频...");
				System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxx2");
			} catch (IllegalStateException e) {
				
				e.printStackTrace();
			} catch (Exception e) {
				
				e.printStackTrace();
			}
            
                      
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户点击打开了�?�知");
            
            System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa4");
                 
        	//打开自定义的Activity
        	Intent i = new Intent(context, Activity_Order_AllList.class);
        	i.putExtras(bundle);
        	//i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        	i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
        	context.startActivity(i);
        	
        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
            //在这里根�? JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity�? 打开�?个网页等..
            System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa5");
        } else if(JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
        	boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
        	Log.w(TAG, "[MyReceiver]" + intent.getAction() +" connected state change to "+connected);
        	System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa6");
        } else {
        	Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
        	System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa7");
        }
	}

	
	
}
