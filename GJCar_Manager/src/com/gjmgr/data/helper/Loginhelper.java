package com.gjmgr.data.helper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Set;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.os.Handler;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.gjmgr.data.bean.Account;
import com.gjmgr.data.data.Public_Api;
import com.gjmgr.data.data.Public_SP;
import com.gjmgr.utils.HandlerHelper;
import com.gjmgr.utils.HttpHelper;
import com.gjmgr.utils.SharedPreferenceHelper;


public class Loginhelper {
	
	public void login(final Context context, final JSONObject jsonObject,
			final Handler handler, final int what) {// 1λObject,2ΪString

		new Thread() {
			public void run() {

				HttpClient httpCLient = new DefaultHttpClient();
				httpCLient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
				httpCLient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 6000);

				StringEntity requestentity = null;
				try {
					requestentity = new StringEntity(jsonObject.toString(),
							"utf-8");
				} catch (UnsupportedEncodingException e1) {
					e1.printStackTrace();
				}// ���������������
				requestentity.setContentEncoding("UTF-8");
				requestentity.setContentType("application/json");

				// ����get����ʵ��
				HttpPost httppost = new HttpPost(Public_Api.appWebSite
						+ Public_Api.api_login);
				httppost.setHeader("Content-Type",
						"application/json;charset=UTF-8");
				System.out.println("executing request " + httppost.getURI());
				httppost.setEntity(requestentity);
				try {

					// �ͻ���ִ��get���� ������Ӧʵ��
					HttpResponse response = httpCLient.execute(httppost);

					// ��������Ӧ״̬��
					System.out.println(response.getStatusLine());

					Header[] heads = response.getAllHeaders();
					// ��ӡ������Ӧͷ
					for (Header h : heads) {
						System.out.println(h.getName() + ":" + h.getValue());
					}

					// ��ȡ��Ӧ��Ϣʵ��
					HttpEntity responseentity = response.getEntity();
					String data = EntityUtils.toString(responseentity);
					if (responseentity != null) {

						// ��Ӧ����
						System.out.println("��Ӧ����"+data);

						// ��Ӧ���ݳ���
						System.out.println("��Ӧ���ݳ��ȣ�"
								+ responseentity.getContentLength());
					}
					System.out.println("s1111s");
					// �ж���Ӧ��Ϣ
					org.json.JSONObject datajobject;
					boolean status = false;
					String message = null;
					try {
						datajobject = new org.json.JSONObject(data);
						status = datajobject.getBoolean("status");
						message = datajobject.getString("message");

					} catch (org.json.JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					System.out.println("22222"+message);
					if (status) {
						System.out.println("33333" + message);

						// ��¼�ɹ�
						String token = new HttpHelper().getDriverCookie(httpCLient);						

						Account account = JSON.parseObject(message,
								new TypeReference<Account>() {
								});
						
						// ��������
						SharedPreferenceHelper.putBean(context,
								Public_SP.Account, new String[] { "id","name", "phone", "driver_token" }, 
								new Object[] {account.id, account.name,account.phone, token },
								new int[] { SharedPreferenceHelper.Type_String,
										SharedPreferenceHelper.Type_String,
										SharedPreferenceHelper.Type_String,
										SharedPreferenceHelper.Type_String});

						HandlerHelper.sendString(handler, what,
								HandlerHelper.Ok);
					} else {
						System.out.println("��¼ʧ��");
						HandlerHelper.sendString(handler, what,
								HandlerHelper.Fail);

					}

				} catch (ClientProtocolException e) {
					System.out.println("s555555");
					HandlerHelper.sendString(handler, what,
							HandlerHelper.DataFail);
				} catch (IOException e) {
					System.out.println("666666s");
					HandlerHelper.sendString(handler, what,
							HandlerHelper.DataFail);
				} catch (JSONException e) {

					HandlerHelper.sendString(handler, what,
							HandlerHelper.DataFail);
				} finally {
					httpCLient.getConnectionManager().shutdown();
					System.out.println("sssssssssss");
				}
			};
		}.start();
	}
	public static void setAlias(Context context, String alias, final Handler handler, final int what){
		
		JPushInterface.setAliasAndTags(context.getApplicationContext(), alias, null, new TagAliasCallback() {
			
	        @Override
	        public void gotResult(int code, String alias, Set<String> tags) {
	            
	            switch (code) {
		            case 0://���óɹ�
		                HandlerHelper.sendString(handler, what, HandlerHelper.Ok);
		                break;
		                
		            case 6002://����ʧ��
		            	HandlerHelper.sendString(handler, what, HandlerHelper.Fail);
		                break;
		            
		            default:
		            	HandlerHelper.sendString(handler, what, HandlerHelper.Fail);
		               break;
		        }
   
	        }
		    
		});//����alias
	}
	
	
}
