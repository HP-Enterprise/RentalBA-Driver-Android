package com.gjmgr.activity.user;

import java.io.UnsupportedEncodingException;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.gjmgr.data.bean.LocationMessage;
import com.gjmgr.data.data.Public_Api;
import com.gjmgr.data.data.Public_Data;
import com.gjmgr.data.data.Public_SP;
import com.gjmgr.framwork.BaiduMapHelper.MyLocationListenner;
import com.gjmgr.utils.HandlerHelper;
import com.gjmgr.utils.HttpHelper;
import com.gjmgr.utils.SharedPreferenceHelper;
import com.gjmgr.utils.TimeHelper;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

public class Servcie_Gps extends Service{

	private boolean isQuite = false;
	/*����*/
	private static final int Timeout = 6000;
	
	/*�������*/
	private AsyncHttpClient httpClient;
	private boolean isSend = true;
	LocationClient mLocClient;
	
	private Handler handler = new Handler();
	
	@Override
	public IBinder onBind(Intent arg0) {
		
		System.out.println("onBind");
		
		return null;
	}

	@Override
    public boolean onUnbind(Intent intent) {
		
        System.out.println("onUnbind");
        
        return super.onUnbind(intent);
    }
	
	@Override
	public void onCreate() {
		
		System.out.println("onCreate");
		
		super.onCreate();
	}
	
	@Override
	public void onDestroy() {
		
		System.out.println("onDestroy");
		isQuite = true;
		super.onDestroy();
	}
	
	@Override
    public int onStartCommand(Intent intent, int flags, int startId) {
		
		System.out.println("onStartCommand");
		
        init();
        initBaiduGps();
        startThread();
        return super.onStartCommand(intent, flags, startId);
        
    }
	
	 /**
     * �����߳�ģ���ʱ����
     */
    public void startThread() {
        new Thread(new Runnable() {
 
            @Override
            public void run() {
                while (!isQuite) {
                    try {
                        Thread.sleep(10000);
                      
                        isSend = true;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
             
    }
    
    private void init(){
    	
		httpClient = new AsyncHttpClient();
		httpClient.setTimeout(Timeout);
    }
    
    public void initBaiduGps(){
    	
    	mLocClient = new LocationClient(this);
    	
    	// ��λ��ʼ��
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// ��gps
		option.setCoorType("bd09ll"); // ������������
		option.setIsNeedAddress(true);//��ѡ�������Ƿ���Ҫ��ַ��Ϣ��Ĭ�ϲ���Ҫ
		option.setScanSpan(1000);
		option.setTimeOut(10000);
		mLocClient.setLocOption(option);		
		System.out.println("2**************");
		
		/*������*/
		class MyLocationListenner implements BDLocationListener {

			@Override
			public void onReceiveLocation(final BDLocation location) {
				// map view ���ٺ��ڴ����½��յ�λ��
				System.out.println("��λ�ص�����-----------------");				

				if(isSend){
					
					SharedPreferenceHelper.putString(Servcie_Gps.this, Public_SP.Gps, "gps_time", TimeHelper.getNowDateTime());
					
					if (location != null) {
						
						System.out.println("location.getLatitude()"+ location.getLatitude());
						System.out.println("location.getLongtitude()"+ location.getLongitude());
						Request_Submit(location.getLatitude(), location.getLongitude());
						isSend = false;
						
						return;
					}
						
				}
						
			}
						
			public void onReceivePoi(BDLocation poiLocation) {
				System.out.println("��λʧ��3");
			}
		}
		
		/*���ü�����*/
		MyLocationListenner myListener = new MyLocationListenner();
		mLocClient.registerLocationListener(myListener);
		
		/*��ʼ��λ*/
		mLocClient.start();
    }
	private void  Request_Submit(double latitude, double longitude){

		/*����*/
		JSONObject jsonObject = new JSONObject(); //**********************ע��json��������ʱ��Ҫ����
			
		jsonObject.put("lat", latitude);
		jsonObject.put("lng", longitude);//˾��
		
		StringEntity requestentity = null;System.out.println("aaaaaaaaaaaaaaaa");
		
		if(jsonObject != null){
			
			try {
				requestentity = new StringEntity(jsonObject.toString(), "utf-8");
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}

			System.out.println("ccc:"+jsonObject.toString());
		}

		String driverId = SharedPreferenceHelper.getString(this, Public_SP.Account, "id");
		httpClient.post(this, Public_Api.appWebSite + "api/dispatch/city-driver/"+driverId+"/location", requestentity, "application/json", new AsyncHttpResponseHandler() {
													  
			/* ��������ɹ�  */
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] data) {
				System.out.println("2");
				if(data==null ||new String(data)==null||new String(data).equals("")){
					
					return;
				}
				String databack = new String(data);System.out.println("����gps�ɹ�:" + databack);
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				System.out.println("����ʧ��");
			}
		});
	}
  
}
