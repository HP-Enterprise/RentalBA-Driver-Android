package com.gjmgr.utils;


import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.client.HttpClient;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.AbstractHttpClient;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.gjmgr.data.data.Public_Api;
import com.gjmgr.data.data.Public_SP;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class HttpHelper {

	/*����*/
	private static final int Timeout = 8000;
	
	public static final String Method_Post = "POST";
	public static final String Method_Put = "PUT";
	public static final String Method_Get = "GET";
	public static final String Method_Delete = "DELETE";
	
	/*�������*/
	private AsyncHttpClient httpClient;
	private Context context;
	private String url;
	private JSONObject jsonObject;
	private RequestParams params;
	private Handler handler;
	private int what;
	private int String_Object;
	
/**********************************************************************************************************************************************************************	
��һ������				*******************************************************************************************************************************************************
 				    ***************************************************************************************************************************************
***********************************************************************************************************************************************************************/
	/**
	 *�������ݣ�{status:'',message''}
	 *
	 *Message:����messageЯ������Ϣ
	 * @param <T>
	 */
	public <T> void initData(String method, Context context, String api, JSONObject jsonObject, RequestParams params, Handler handler, int what, int String_Object, TypeReference<T> type) {//1λObject,2ΪString
		System.out.println("5��18��--"+Public_Api.appWebSite+api);
		System.out.println("���뷽��:����url"+api);
		if(context == null){System.out.println("Http----contextΪnull");
			HandlerHelper.sendString(handler, what, HandlerHelper.DataFail);
			return;
		}
		
		/*��ʼ������*/
		httpClient = new AsyncHttpClient();
		httpClient.setTimeout(Timeout);
		
		this.context = context;
		this.url = Public_Api.appWebSite + api;
		this.jsonObject = jsonObject;
		this.params = params;
		this.handler = handler;
		this.what = what;
		this.String_Object = String_Object;
				
		if(Method_Post.equals(method)){ Post(type);return;}
		if(Method_Get.equals(method)){ System.out.println("��ʼ");Get(type);return;}
		if(Method_Put.equals(method)){ Put(type);return;}
		if(Method_Delete.equals(method)){ return;}
		
	}

	/**
	 * Post
	 * @param <T>
	 */
	private <T> void Post(final TypeReference<T> type){
		HttpHelper.AddCookies(httpClient, context);
		
		StringEntity requestentity = null;System.out.println("aaaaaaaaaaaaaaaa");
		
		if(jsonObject != null){
			
			try {
				requestentity = new StringEntity(jsonObject.toString(), "utf-8");
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
			System.out.println("bbb"+url);
			System.out.println("ccc:"+jsonObject.toString());
		}
		
		httpClient.post(context, url, requestentity, "application/json", new AsyncHttpResponseHandler() {

			/* ��������ɹ�  */
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] data) {
				System.out.println("2");
				if(data==null ||new String(data)==null||new String(data).equals("")){
					HandlerHelper.sendStringData(handler, what, HandlerHelper.Fail, "");
					return;
				}
				String databack = new String(data);System.out.println("������ɹ�:" + databack);
				JSONObject datajobject = JSONObject.parseObject(databack);
				
				boolean status = datajobject.getBoolean("status");
				String message = datajobject.getString("message");
				System.out.println("22222");  
				
				Message msg = new Message();				
				Bundle bundle = new Bundle();
				bundle.putString("message", message);
				msg.setData(bundle);
				
				if(String_Object == 1){//�����ַ������Ƕ���
					
					if(status){
						
						if(message.equals("[]")){
							HandlerHelper.sendString(handler, what, HandlerHelper.Empty);
							return;
						}
						System.out.println("��ʼ����");
						HandlerHelper.sendStringObject(handler, what, HandlerHelper.Ok, JSON.parseObject(message, type));	
						System.out.println("��½����true");
						
					}else{	
						
						HandlerHelper.sendStringData(handler, what, HandlerHelper.Fail, message);
						System.out.println("��½����false");
					}
					
				}else{
					
					if(status){
						
						HandlerHelper.sendStringData(handler, what, HandlerHelper.Ok, message);
						
						System.out.println("��½����true");
					}else{	
						HandlerHelper.sendStringData(handler, what, HandlerHelper.Fail, message);
						System.out.println("��½����false");
					}
				}

			}
			
			/* 5.��������ʧ��  */
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,Throwable arg3) {System.out.println("ʧ��");
				httpClient.cancelAllRequests(true);
				HandlerHelper.sendString(handler, what, HandlerHelper.DataFail);
			}
		});	
	}
	
	/**
	 * Put
	 * @param <T>
	 */
	private <T> void Put(final TypeReference<T> type){
		HttpHelper.AddCookies(httpClient, context);
		
		StringEntity requestentity = null;System.out.println("aaaaaaaaaaaaaaaa");
		try {
			
			if(jsonObject != null){
				requestentity = new StringEntity(jsonObject.toString(), "utf-8");
			}
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}

		httpClient.put(context, url, requestentity, "application/json", new AsyncHttpResponseHandler() {

			/* ��������ɹ�  */
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] data) {
				System.out.println("2");
				if(data==null ||new String(data)==null||new String(data).equals("")){
					HandlerHelper.sendStringData(handler, what, HandlerHelper.DataFail, "");
					return;
				}
				String databack = new String(data);System.out.println("������put�ɹ�:" + databack);
				JSONObject datajobject = JSONObject.parseObject(databack);
				
				boolean status = datajobject.getBoolean("status");
				String message = datajobject.getString("message");
				System.out.println("22222");  
				
				Message msg = new Message();				
				Bundle bundle = new Bundle();
				bundle.putString("message", message);
				msg.setData(bundle);
				
				if(String_Object == 1){//�����ַ������Ƕ���
					
					if(status){
						
						if(message.equals("[]")){
							HandlerHelper.sendString(handler, what, HandlerHelper.Empty);
							return;
						}
						System.out.println("��ʼ����");
						HandlerHelper.sendStringObject(handler, what, HandlerHelper.Ok, JSON.parseObject(message, type));	
						System.out.println("��½����true");
						
					}else{	
						
						HandlerHelper.sendStringData(handler, what, HandlerHelper.Fail, message);
						System.out.println("��½����false");
					}
					
				}else{
					
					if(status){
						
						HandlerHelper.sendStringData(handler, what, HandlerHelper.Ok, message);
						
						System.out.println("��½����true");
					}else{	
						HandlerHelper.sendStringData(handler, what, HandlerHelper.Fail, message);
						System.out.println("��½����false");
					}
				}

			}
			
			/* 5.��������ʧ��  */
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,Throwable arg3) {System.out.println("ʧ��");
				httpClient.cancelAllRequests(true);
				HandlerHelper.sendString(handler, what, HandlerHelper.DataFail);
			}
		});	
	}
	
	/**
	 * Get
	 * @param <T>
	 */
	private <T> void Get(final TypeReference<T> type){System.out.println("get����");
		HttpHelper.AddCookies(httpClient, context);
	
		httpClient.get(url, params, new AsyncHttpResponseHandler() {
			
			/* ��������ɹ�  */
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] data) {
				System.out.println("5��18��--����--");
				System.out.println("2");
				String databack = new String(data);
				if(databack == null || databack.equals("") || databack.equals("null")){
					HandlerHelper.sendString(handler, what, HandlerHelper.Empty);
					return;
				}
				System.out.println("������ɹ�:" + databack);
				JSONObject datajobject = JSONObject.parseObject(databack);
				
				boolean status = datajobject.getBoolean("status");
				String message = datajobject.getString("message");
				System.out.println("22222");  
				
				if(String_Object == 1){
					
					if(status){
						
						if(message == null || message.equals("[]") || message.equals("null")){
							HandlerHelper.sendString(handler, what, HandlerHelper.Empty);
							return;
						}
						System.out.println("��ʼ����");
						HandlerHelper.sendStringObject(handler, what, HandlerHelper.Ok, JSON.parseObject(message, type));	
						System.out.println("��½����true");
						
					}else{	
						
						HandlerHelper.sendStringData(handler, what, HandlerHelper.Fail, message);
						System.out.println("��½����false");
					}
					
				}else{
					
					if(status){
						
						HandlerHelper.sendStringData(handler, what, HandlerHelper.Ok, message);
						
						System.out.println("��½����true");
					}else{	
						HandlerHelper.sendStringData(handler, what, HandlerHelper.Fail, message);
						System.out.println("��½����false");
					}
				}

			}
			
			/* 5.��������ʧ��  */
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] data,Throwable arg3) {
				
				if(data==null ||new String(data)==null||new String(data).equals("")){
					HandlerHelper.sendStringData(handler, what, HandlerHelper.DataFail, "");
					return;
				}

				HandlerHelper.sendString(handler, what, HandlerHelper.DataFail);
			}
		});	
	}
	
	
	 /**
		 * ��ȡcookie�е�token
		 */
		public String getCookie(HttpClient httpClient) {
			
			 String token = "";
			
			 List<Cookie> cookies = ((AbstractHttpClient) httpClient).getCookieStore().getCookies();
			 
			 for (int i = 0; i < cookies.size(); i++) {
				 
				 Cookie cookie = cookies.get(i);
				 String cookieName = cookie.getName();
				 String cookieValue = cookie.getValue();
				 System.out.println(cookieName);
				 System.out.println(cookieValue);
				 if (!TextUtils.isEmpty(cookieName)&& !TextUtils.isEmpty(cookieValue)) {
					 if(cookieName.equals("token")){
						 token = cookieValue;
						 
					 }
				 }
			 }
			 
			 return token;
		}
		public String getDriverCookie(HttpClient httpClient) {
			
			 String token = "";
			
			 List<Cookie> cookies = ((AbstractHttpClient) httpClient).getCookieStore().getCookies();
			 
			 for (int i = 0; i < cookies.size(); i++) {
				 
				 Cookie cookie = cookies.get(i);
				 String cookieName = cookie.getName();
				 String cookieValue = cookie.getValue();
				 System.out.println(cookieName);
				 System.out.println(cookieValue);
				 if (!TextUtils.isEmpty(cookieName)&& !TextUtils.isEmpty(cookieValue)) {
					 if(cookieName.equals("driver_token")){
						 token = cookieValue;
						 
					 }
				 }
			 }
			 
			 return token;
		}
		
		/**
	     * ����Cookie
	     * @param request
	     */
	    public static void AddCookies(AsyncHttpClient httpClient,Context context)
	    {
	          StringBuilder sb = new StringBuilder();
//	          
	          String key = "driver_token";
	          String val = SharedPreferenceHelper.getString(context, Public_SP.Account, key);
//	          String key = "token_staff";
//	          String val = "01DB4C43D9994A4E2BF8B9A4B87E98B0";
	          
	          sb.append(key);
	          sb.append("=");
	          sb.append(val);
	          sb.append(";");
	          
	          httpClient.addHeader("cookie", sb.toString());
	          
	          System.out.println("cookie---------------"+sb);
	    }
}
