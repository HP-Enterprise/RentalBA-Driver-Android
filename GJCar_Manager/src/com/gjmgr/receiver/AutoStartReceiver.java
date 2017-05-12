package com.gjmgr.receiver;
import com.gjmgr.activity.user.Login_Activity;
import com.gjmgr.app.activity.MainActivity;

import android.content.BroadcastReceiver;  
import android.content.Context;  
import android.content.Intent;  
  
/** 
 * 实现开机启动 
 * @author Owner 
 */  
public class AutoStartReceiver extends BroadcastReceiver {  
	
    @Override  
    public void onReceive(Context context, Intent intent) {  
//        Intent i = new Intent(context, Login_Activity.class);  
//        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
//        context.startActivity(i);  
        System.out.println("开机启动项xxxxxxxxxxxxxxxx");
        
//    	  Intent i = context.getPackageManager().getLaunchIntentForPackage("com.gjmgr.app"); 
//    	  context.startActivity(i);
    }  
    
}  