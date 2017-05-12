package com.gjcar.activity.api;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.gjmgr.data.data.Public_Api;
import com.gjmgr.utils.SharedPreferenceHelper;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class Api {

	private void Http_Get_(String orderId,Context context) {
		
		/* ���� */		
		int p1 = SharedPreferenceHelper.getUid(context);
		int p2 = 1;
		String p3 = "2";
		
		String api = "api/airportTrip/" + orderId + "/order"+"?p1="+p1+"&p2="+p2+"&p3="+p3;
		
		/* ���������½ */
		AsyncHttpClient httpClient = new AsyncHttpClient();
		
		String url = Public_Api.appWebSite + api;System.out.println("url\n"+api);
		
		httpClient.get(url, null, new AsyncHttpResponseHandler() {
				
			/* ��������ɹ� */
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {

				String backData = new String(arg2);

				if (backData == null || backData.equals("") || backData.equals("null")) {

					return;
				}

				System.out.println("����ֵ\n" + backData);

				JSONObject statusjobject = JSON.parseObject(backData);

				boolean status = statusjobject.getBoolean("status");
				String message = statusjobject.getString("message");

				if (status) {

					/*�ֶ�*/
					JSONObject j = JSON.parseObject(message);
					int orderState = j.getIntValue("orderState");
					String modelId = j.getString("modelId");

					String vehicleModelShow = j.getString("vehicleModelShow");
					JSONObject j_modle = JSON.parseObject(vehicleModelShow);
					String model = j_modle.getString("model");

					/*����*/
					ArrayList<Object> list_park =  JSON.parseObject(message, new TypeReference<ArrayList<Object>>() {});
					Object object = JSON.parseObject(message, new TypeReference<Object>() {});
					
				} else {

				}

			}

			/* 5.��������ʧ�� */
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				
				String backData = new String(arg2);
				
				if (backData == null || backData.equals("") || backData.equals("null")) {

					return;
				}
				System.out.println("����ʧ�ܷ���ֵ\n" + backData);
			}
		});
	}
	
	private void Http_Post_(Context context){

		/*����*/
		JSONObject jsonObject = new JSONObject();
		
		jsonObject.put("parma1", 1);
		jsonObject.put("parma2", "2");	
		jsonObject.put("parma3", 3);

		String api = "api/airportTrip/uid/order";

		/* ���������½ */
		AsyncHttpClient httpClient = new AsyncHttpClient();
		
		String url = Public_Api.appWebSite + api;System.out.println("url\n"+api);
		
		StringEntity requestentity = null;
		try {
			requestentity = new StringEntity(jsonObject.toString(), "utf-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}

		System.out.println("json����\n"+jsonObject.toString());
		httpClient.post(context, url, requestentity, "application/json", new AsyncHttpResponseHandler() {

			/* ��������ɹ�  */
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] data) {

				String backData = new String(data);
				
				if (backData == null || backData.equals("") || backData.equals("null")) {

					return;
				}
				
				System.out.println("����ֵ\n" + backData);
				JSONObject datajobject = JSONObject.parseObject(backData);
				
				boolean status = datajobject.getBoolean("status");
				String message = datajobject.getString("message");
	
				if(status){
					
					if(message.equals("[]")){
						
						return;
					}
					
					/*�ֶ�*/
					JSONObject j = JSON.parseObject(message);
					int orderState = j.getIntValue("orderState");
					String modelId = j.getString("modelId");

					String vehicleModelShow = j.getString("vehicleModelShow");
					JSONObject j_modle = JSON.parseObject(vehicleModelShow);
					String model = j_modle.getString("model");

					/*����*/
					ArrayList<Object> list_park =  JSON.parseObject(message, new TypeReference<ArrayList<Object>>() {});
					Object object = JSON.parseObject(message, new TypeReference<Object>() {});
					
				}else{	
					
					
				}
				
			}
			
			/* 5.��������ʧ��  */
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,Throwable arg3) {

				String backData = new String(arg2);
				
				if (backData == null || backData.equals("") || backData.equals("null")) {

					return;
				}
				System.out.println("����ʧ�ܷ���ֵ\n" + backData);
			
			}
		});	
	}
}
