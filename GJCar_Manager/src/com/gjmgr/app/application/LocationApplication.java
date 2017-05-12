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
 * 主Application，所有百度定位SDK的接口说明请参考线上文档：http://developer.baidu.com/map/loc_refer/index.html
 *
 * 百度定位SDK官方网站：http://developer.baidu.com/map/index.php?title=android-locsdk
 * 
 * 直接拷贝com.baidu.location.service包到自己的工程下，简单配置即可获取定位结果，也可以根据demo内容自行封装
 */
public class LocationApplication extends Application {

	
    @Override
    public void onCreate() {
        super.onCreate();
        
        /* 初始化定位sdk，建议在Application中创建*/
        SDKInitializer.initialize(getApplicationContext());  
        
        JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);     		// 初始化 JPush
    }
    	
/**退出app******************************************************************/ 
}
