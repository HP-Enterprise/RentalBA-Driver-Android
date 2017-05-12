package com.gjmgr.activity.user;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;

import com.alibaba.fastjson.JSONObject;
import com.gjmgr.app.R;
import com.gjmgr.data.data.Public_Api;
import com.gjmgr.data.data.Public_Param;
import com.gjmgr.data.data.Public_SP;
import com.gjmgr.utils.NetworkHelper;
import com.gjmgr.utils.SharedPreferenceHelper;
import com.gjmgr.utils.StringHelper;
import com.gjmgr.utils.ToastHelper;
import com.gjmgr.view.dialog.SubmitDialog;
import com.gjmgr.view.helper.EditTextHelper;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class UpdatePwd_Activity extends Activity implements OnClickListener{

	/*初始化控件*/
	private EditText reset_pwd;
	private ImageView reset_pwd_delete;
	private ImageView reset_pwd_show;
	
	private EditText reset_againpwd;
	private ImageView reset_againpwd_delete;
	private ImageView reset_againpwd_show;
	
	private EditText reset_phone;
	private ImageView reset_phone_delete;
	private ImageView reset_phone_show;
	
	private Button reset_submit;
	
	/*Handler*/
	private Handler handler;
	
	private final static int sendPwdOk = 1;//发送短信成功
	private final static int sendPwdFail = 2;//发送短信失败
	private final static int sendPwdDataFail = 3;//发送短信请求失败
	
	/*其它*/
	private String errorMsg = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_update);
		//LocationApplication.getInstance().addActivity(this);
		
		initTitleBar();

		initView();
		
		initListener();
		
		initHandler();
	}

	private void initTitleBar() {

		ImageView iv = (ImageView)findViewById(R.id.iv_back);
		iv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
				
			}
		});

	}
	
	private void initView() {
		
		reset_pwd = (EditText)findViewById(R.id.reset_pwd);		 
		reset_pwd_delete = (ImageView)findViewById(R.id.reset_pwd_delete);
		reset_pwd_show = (ImageView)findViewById(R.id.reset_pwd_show); 
		
		reset_againpwd = (EditText)findViewById(R.id.reset_againpwd);
		reset_againpwd_delete = (ImageView)findViewById(R.id.reset_againpwd_delete);		
		reset_againpwd_show = (ImageView)findViewById(R.id.reset_againpwd_show);
		
		reset_phone = (EditText)findViewById(R.id.reset_phone);
		reset_phone_delete = (ImageView)findViewById(R.id.reset_phone_delete);		
		reset_phone_show = (ImageView)findViewById(R.id.reset_phone_show);
		
		reset_submit = (Button)findViewById(R.id.reset_submit);
	}

	private void initListener() {
		
		new EditTextHelper().setEditText_Password(reset_pwd, reset_pwd_delete, reset_pwd_show, new int[]{R.drawable.register_pwd_show, R.drawable.register_pwd_hide});
		new EditTextHelper().setEditText_Password(reset_againpwd, reset_againpwd_delete, reset_againpwd_show, new int[]{R.drawable.register_pwd_show, R.drawable.register_pwd_hide});
		new EditTextHelper().setEditText_Password(reset_phone, reset_phone_delete, reset_phone_show, new int[]{R.drawable.register_pwd_show, R.drawable.register_pwd_hide});
		
		reset_submit.setOnClickListener(this);
	}

	private void initHandler() {
		
		handler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				
				switch (msg.what) {
				
					case sendPwdOk:
						SubmitDialog.closeSubmitDialog();	
						SharedPreferences sp = getSharedPreferences(Public_SP.Account, Context.MODE_PRIVATE);
						Editor editor = sp.edit();
						editor.clear();
						editor.commit();
						
						SharedPreferenceHelper.putString(UpdatePwd_Activity.this, Public_SP.Account, "phone", reset_phone.getText().toString().trim());
						SharedPreferenceHelper.putString(UpdatePwd_Activity.this, Public_SP.Account, "password", reset_againpwd.getText().toString().trim());
						
						finish();
						
						ToastHelper.showToastShort(UpdatePwd_Activity.this, "修改密码成功");
	
						break;
						
					case sendPwdFail:
						SubmitDialog.closeSubmitDialog();
//						Notifycation.showToastLong(UpdatePwd_Activity.this, msg.getData().getString("errorMsg"));//显示发送失败的原因
						ToastHelper.showToastLong(UpdatePwd_Activity.this, "旧密码错误");//显示发送失败的原因
						break;
												
					case sendPwdDataFail:
						SubmitDialog.closeSubmitDialog();
						ToastHelper.showSendDataFailToast(UpdatePwd_Activity.this);
						break;
						
					default:
						break;
				}
								
			}
		};
		
	}
	
	@Override
	public void onClick(View view) {

		switch (view.getId()) {

			case R.id.reset_submit:
				
				checkPassword();
				if(errorMsg.equals("")){				
				
					//发送密码到服务器
					if(NetworkHelper.isNetworkAvailable(UpdatePwd_Activity.this)){System.out.println("bbbb");
						
					 	/*弹出对话框*/
						SubmitDialog.showSubmitDialog(this);
					
						/*提交数据*/
						new Thread(){
							public void run() {
								sendPassword();
							}
						}.start();
										
					}
										
				}else{
					Toast.makeText(UpdatePwd_Activity.this, errorMsg, Toast.LENGTH_LONG).show();
					errorMsg = "";
				}
				break;
				
			default:
				break;
		}
	}
	
	/**
	 * 检查密码是否正确
	 */
	private void checkPassword(){
		
		if(reset_phone.getText().toString().trim().equals("") || reset_phone.getText().toString().trim()== null){
			
			errorMsg = errorMsg +"手机号码不能为空";
		}else{

			if(!reset_phone.getText().toString().trim().matches("^[1][3578][0-9]{9}$")){
				
				errorMsg = errorMsg +"手机号码输入不正确";
			}else{
				
				if(reset_pwd.getText().toString().equals("") || reset_pwd.getText().toString() == null){
					
					errorMsg = errorMsg +"旧密码不能为空";		
					System.out.println("旧密码不能为空"+reset_pwd.getText().toString());
				}else{
					
					if(reset_againpwd.getText().toString().equals("") || reset_againpwd.getText().toString() == null){
						errorMsg = errorMsg +"新密码不能为空";		
						System.out.println("新密码不能为空"+reset_againpwd.getText().toString());
					}
					
				}
			}
		}
		
		
	
	}
	
	/** 发送密码  */
	private void sendPassword(){

        // 创建默认的客户端实例  
        HttpClient httpCLient = new DefaultHttpClient();
        
        JSONObject jsonObject = new JSONObject();      //**********************注意json发送数据时，要这样
        jsonObject.put("phone", reset_phone.getText().toString().trim()); 
        jsonObject.put("password", reset_pwd.getText().toString().trim());  
        jsonObject.put("newPassword", reset_againpwd.getText().toString().trim());   
                  
        StringEntity requestentity = null;
		try {
			requestentity = new StringEntity(jsonObject.toString(),"utf-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}//解决中文乱码问题    
		requestentity.setContentEncoding("UTF-8");    
		requestentity.setContentType("application/json");    
                  
        // 创建get请求实例  
		HttpPost httppost= new HttpPost(Public_Api.appWebSite + Public_Api.api_updatepwd);//**********************注意请求方法  

		httppost.setHeader("Content-Type", "application/json;charset=UTF-8");
		AddCookies(httppost);
		httppost.setEntity(requestentity);   
        try  
        {  
              
            // 客户端执行get请求 返回响应实体  
            HttpResponse response = httpCLient.execute(httppost);  
            if(response.getStatusLine().getStatusCode() == 200){//请求成功
            	
            	// 获取响应消息实体  
                HttpEntity responseentity = response.getEntity();  
                String data = EntityUtils.toString(responseentity);  	              	              

                //判断响应信息
                org.json.JSONObject datajobject = new org.json.JSONObject(data);
    			boolean status = datajobject.getBoolean("status");
    			String message = datajobject.getString("message");
    			System.out.println("22222"+datajobject);  
    			if(status){
    				System.out.println("33333");  
    				
    				//修改密码成功	
    				handler.sendEmptyMessage(sendPwdOk);  				
    				
    			}else{
    				System.out.println("4444");  
    				//修改密码失败
    				
    				Message msg = new Message();
    				msg.what = sendPwdFail;
    				Bundle bundle = new Bundle();
    				bundle.putString("errorMsg", message);
    				msg.setData(bundle);
    				handler.sendMessage(msg);
    				
    			}
            	
            }else{//请求失败
            	handler.sendEmptyMessage(sendPwdDataFail); System.out.println("1sssssssssss"); 
            }
            
              
        } catch (ClientProtocolException e){ //请求异常  
            e.printStackTrace(); handler.sendEmptyMessage(sendPwdDataFail);  System.out.println("2sssssssssss"); 
        } catch (IOException e){  //请求异常    
            e.printStackTrace(); handler.sendEmptyMessage(sendPwdDataFail);  System.out.println("3sssssssssss");   
        } catch (JSONException e) {			
			e.printStackTrace();handler.sendEmptyMessage(sendPwdDataFail); System.out.println("4sssssssssss"); 
		}finally{  
            httpCLient.getConnectionManager().shutdown();  System.out.println("sssssssssss");  
        }  
    
	}
	
	/**
     * 增加Cookie
     * @param request
     */
    public void AddCookies(HttpPost request)
    {
          StringBuilder sb = new StringBuilder();

          String key = "token";
          String val = SharedPreferenceHelper.getString(this, Public_SP.Account, key);
          sb.append(key);
          sb.append("=");
          sb.append(val);
          sb.append(";");

          request.addHeader("cookie", sb.toString());

          System.out.println(""+sb);
    }

}
