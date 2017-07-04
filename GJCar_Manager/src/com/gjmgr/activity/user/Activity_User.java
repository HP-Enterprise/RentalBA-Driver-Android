package com.gjmgr.activity.user;

import java.util.List;

import org.apache.http.Header;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gjmgr.annotation.ContentView;
import com.gjmgr.annotation.ContentWidget;
import com.gjmgr.app.R;
import com.gjmgr.data.bean.User;
import com.gjmgr.data.data.Public_Api;
import com.gjmgr.data.data.Public_Param;
import com.gjmgr.data.data.Public_SP;
import com.gjmgr.utils.AnnotationViewUtils;
import com.gjmgr.utils.SharedPreferenceHelper;
import com.gjmgr.utils.SystemUtils;
import com.gjmgr.view.helper.TitleBarHelper;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * @author Administrator
 *
 */
@ContentView(R.layout.activity_user)
public class Activity_User extends Activity{
	
	/*初始化控件*/
	@ContentWidget(id = R.id.user_realname) TextView user_realname;
	@ContentWidget(id = R.id.user_phone) TextView user_phone;
	@ContentWidget(id = R.id.user_storename) TextView user_storename;
	@ContentWidget(id = R.id.user_card_type) TextView user_card_type;
	@ContentWidget(id = R.id.user_card_number) TextView user_card_number;
	@ContentWidget(id = R.id.version_code) TextView version_code;
	@ContentWidget(click = "onClick") Button user_loginout_btn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){

		super.onCreate(savedInstanceState);
		
		AnnotationViewUtils.injectObject(this, this);
	
		/*标题*/
		TitleBarHelper.Back(this, "个人信息", 0);
		
		Http_Get_Driver();
		
		/*版本号*/
		version_code.setText(SystemUtils.getVersion(Activity_User.this));
	}
	
	public void onClick(View view){

		switch (view.getId()) {
		
			case R.id.user_loginout_btn:
				
				finish();
				Public_Param.activity.finish();
				
				startActivity(new Intent(Activity_User.this,Login_Activity.class));
				break;
				
		}
	}
	
	private void Http_Get_Driver() {
		
		String api = "api/dispatch/driver-list?phone="+SharedPreferenceHelper.getString(this, Public_SP.Account, "phone");//已确认

		/* 向服务器登陆 */
		AsyncHttpClient httpClient = new AsyncHttpClient();

		RequestParams params = new RequestParams();// 设置请求参数

		String url = Public_Api.appWebSite + api;
		
		httpClient.get(url, params, new AsyncHttpResponseHandler() {

			/* 处理请求成功 */
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {

				String backData = new String(arg2);
				System.out.println("请求处理成功:" + backData);

				JSONObject statusjobject = JSON.parseObject(backData);

				boolean status = statusjobject.getBoolean("status");System.out.println("aa1");
				String message = statusjobject.getString("message");System.out.println("aa2");

				if (status) {
					
					//JSONObject j = JSON.parseObject(message);System.out.println("aa");
//					User user = (User)JSON.parseObject(message, new TypeReference<User>() {});
					List<User> list_user = JSONArray.parseArray(message, User.class);System.out.println("aa3");
//					String name = j.getString("name");
//					String phone = j.getString("phone");
//					String storeName = j.getString("storeName");
//					int drivingLicenseLevel = j.getIntValue("drivingLicenseLevel");//0 C2 1 C1 2 B2 3 B1 4 A1
//					String drivingLicenseNumber = j.getString("drivingLicenseNumber");
					User user = list_user.get(0);System.out.println("aa4");
					user_realname.setText(user.name);System.out.println("aa5");
					user_phone.setText(user.phone);System.out.println("aa6");
					user_storename.setText(user.storeName);System.out.println("aa9");
					user_card_type.setText(new String[]{"C2","C1","B2","A1"}[user.drivingLicenseLevel]);System.out.println("aa7");
					user_card_number.setText(user.drivingLicenseNumber);System.out.println("aa8");
				}
				
			}

			/* 5.处理请求失败 */
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {

			}
		});
	}
	
	
}
