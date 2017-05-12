package com.gjmgr.app.application;


import java.util.LinkedList;
import java.util.List;

import cn.jpush.android.api.JPushInterface;

import com.baidu.mapapi.SDKInitializer;

import android.app.Activity;
import android.app.Application;
import android.app.Service;
import android.os.Vibrator;

/**
 * ��Application�����аٶȶ�λSDK�Ľӿ�˵����ο������ĵ���http://developer.baidu.com/map/loc_refer/index.html
 *
 * �ٶȶ�λSDK�ٷ���վ��http://developer.baidu.com/map/index.php?title=android-locsdk
 * 
 * ֱ�ӿ���com.baidu.location.service�����Լ��Ĺ����£������ü��ɻ�ȡ��λ�����Ҳ���Ը���demo�������з�װ
 */
public class LocationApplication extends Application {

	
    @Override
    public void onCreate() {
        super.onCreate();
        
        /* ��ʼ����λsdk��������Application�д���*/
        SDKInitializer.initialize(getApplicationContext());  
        
        JPushInterface.setDebugMode(true); 	// ���ÿ�����־,����ʱ��ر���־
        JPushInterface.init(this);     		// ��ʼ�� JPush
    }
    	
/**�˳�app******************************************************************/ 
}
