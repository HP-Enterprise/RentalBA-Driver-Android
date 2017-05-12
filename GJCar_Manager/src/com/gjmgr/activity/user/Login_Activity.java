package com.gjmgr.activity.user;


import java.sql.Date;

import cn.jpush.android.api.JPushInterface;

import com.gjmgr.annotation.ContentView;
import com.gjmgr.annotation.ContentWidget;
import com.gjmgr.app.R;
import com.gjmgr.app.activity.MainActivity;
import com.gjmgr.data.data.Public_SP;
import com.gjmgr.data.helper.JSONHelper;
import com.gjmgr.data.helper.Loginhelper;
import com.gjmgr.utils.AnnotationViewUtils;
import com.gjmgr.utils.HandlerHelper;
import com.gjmgr.utils.IntentHelper;
import com.gjmgr.utils.NetworkHelper;
import com.gjmgr.utils.SharedPreferenceHelper;
import com.gjmgr.utils.StringHelper;
import com.gjmgr.utils.ToastHelper;
import com.gjmgr.view.dialog.SubmitDialog;
import com.gjmgr.view.helper.EditTextHelper;


import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 1.��¼�������˻���Ϣ
 *     a.ֻ�����¼��һ���˻����Ͳ��ñ�������¼���˺���
 * 
 *
 */
@ContentView(R.layout.activity_login)
public class Login_Activity extends Activity{

	/*��ʼ���ؼ�*/
	@ContentWidget(id = R.id.login_phone) EditText login_phone;
	@ContentWidget(id = R.id.login_phone_delete) ImageView login_phone_delete;

	@ContentWidget(id = R.id.login_pwd) EditText login_pwd;
	@ContentWidget(id = R.id.login_pwd_delete) ImageView login_pwd_delete;
	@ContentWidget(id = R.id.login_pwd_show) ImageView login_pwd_show;
	
	@ContentWidget(click = "onClick") TextView login_forgetpwd;
	
	@ContentWidget(click = "onClick") Button login_login,login_register;
	
	/*Handler*/
	private Handler handler;
	
	private final static int Login = 1;//��¼����ʧ��
	private final static int SetAlias = 2;//����Alias
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		AnnotationViewUtils.injectObject(this, this);
		
		initListener();
		
		initHander(); 
		
		initAccount();
	}

	@Override
	protected void onResume() {
	
		super.onResume();
		
		initAccount();
		
		JPushInterface.onResume(this);
	}
	
	@Override
	protected void onPause() {
		
		super.onPause();
		
		JPushInterface.onPause(this);
	}
	
	private void initAccount() {
			
		login_phone.setText(SharedPreferenceHelper.getString(this, Public_SP.Account, "phone"));
		login_pwd.setText(SharedPreferenceHelper.getString(this, Public_SP.Account, "password"));
		login_pwd_delete.setVisibility(View.GONE);
	}
	
	private void initListener() {
		
		new EditTextHelper().setEditText_Clear(login_phone, login_phone_delete);
		
		new EditTextHelper().setEditText_Password(login_pwd, login_pwd_delete, login_pwd_show, new int[]{R.drawable.register_pwd_show, R.drawable.register_pwd_hide});
	}

	private void initHander() {
		
		handler = new Handler(){
	
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				
				switch (msg.what) {
				
					case Login:
						
						SubmitDialog.closeSubmitDialog();
						
						/*��¼�ɹ�*/
						if(HandlerHelper.getString(msg).equals(HandlerHelper.Ok)){
								
							SharedPreferenceHelper.putString(Login_Activity.this, Public_SP.Account, "password", login_pwd.getText().toString().trim());
							Loginhelper.setAlias(Login_Activity.this, SharedPreferenceHelper.getString(Login_Activity.this, Public_SP.Account, "phone"), handler, SetAlias);System.out.println("��¼�ɹ���"+SharedPreferenceHelper.getString(Login_Activity.this, Public_SP.Account, "phone"));//����Alias
							finish();
							IntentHelper.startActivity(Login_Activity.this, Activity_Order_AllList.class);
							
							return;            
						}
						
						/*��¼ʧ��*/						
						ToastHelper.showToastShort(Login_Activity.this, "�û��������ڻ��������");
						break;
					
					case SetAlias:
						
						/*���óɹ�*/
						if(HandlerHelper.getString(msg).equals(HandlerHelper.Ok)){
							System.out.println("���ñ����ɹ�");
							
							return;            
						}
						
						/*����ʧ��*/
						this.postDelayed(new Runnable() {
							
							@Override
							public void run() {
								Loginhelper.setAlias(Login_Activity.this, SharedPreferenceHelper.getString(Login_Activity.this, Public_SP.Account, "phone"), handler, SetAlias);//����Alias								
							}
						}, 1000*60);
						
						break;
						
					default:
						break;
				}
			}
		};
		
	}
	
	public void onClick(View view) {

		switch (view.getId()) {


			case R.id.login_forgetpwd:
				IntentHelper.startActivity(Login_Activity.this, UpdatePwd_Activity.class);
				break;
				
			case R.id.login_login:

				/*����ֻ�����Ϣ*/
				if(!ValidationHelper.Validate_Phone(this, login_phone)){
					return;
				}
				
				/*���������Ϣ*/
				if(!ValidationHelper.Validate_Password(this, login_pwd)){
					return;
				}
				
				/*�������*/
				if(!NetworkHelper.isNetworkAvailable(this)){
					ToastHelper.showNoNetworkToast(this);
					return;
				}
				
				/*�����ύ�Ի���*/
				SubmitDialog.showSubmitDialog(this);
				
				/*��������*///new Object[]{"15827653951",StringHelper.encryption("q12345678"),"android"}
	            new Loginhelper().login(Login_Activity.this, JSONHelper.getJSONObject(new String[]{"phone","password"}, new Object[]{login_phone.getText().toString().trim(),StringHelper.encryption(login_pwd.getText().toString().trim())}), 
	            		handler, Login);
				System.out.println("��½���룺"+StringHelper.encryption(login_pwd.getText().toString().trim()));				
				break;
				
			default:
				break;
		}
	}
		
}
