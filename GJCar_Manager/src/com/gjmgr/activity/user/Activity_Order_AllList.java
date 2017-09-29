package com.gjmgr.activity.user;

import java.lang.reflect.Field;

import cn.jpush.android.api.JPushInterface;

import com.alibaba.fastjson.TypeReference;
import com.gjmgr.app.R;
import com.gjmgr.data.bean.ApkInfo;
import com.gjmgr.data.data.Public_Api;
import com.gjmgr.data.data.Public_Param;
import com.gjmgr.data.helper.PageIndicatorHelper;
import com.gjmgr.utils.HandlerHelper;
import com.gjmgr.utils.HttpHelper;
import com.gjmgr.utils.SystemUtils;
import com.gjmgr.utils.TimeHelper;
import com.gjmgr.utils.UpdateUtils;
import com.gjmgr.utils.UpdateUtils.Update_Notify;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Activity_Order_AllList extends FragmentActivity implements Update_Notify{
	
	public ViewPager viewPager;
	
	private FragmentPagerAdapter pagerAdapter;
	private Fragment[] pages;
	private FragmentManager fragmentManager;
	
	private FixedSpeedScroller scroller; 
	
	private TextView t_notuse,t_use,t_timeout;
	private View l_notuse,l_use,l_timeout;
	
	private Handler handler;
	private final static int Request_Version = 101;
	private final static int Page_Indicator = 3;
	
	private Fragment_order page_0;
	private Fragment_order_doing page_1;
	private Fragment_order_ok page_2;
	
	private LinearLayout menu_out;/*ͷ��*/
	private TextView ok_true;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_order_alllist);
				
		initHandler();
		
		initPageInditor();
		
		/*��ͼ*/		
		PageIndicatorHelper.initIndicator(this, new TextView[]{t_notuse,t_use,t_timeout}, new View[]{l_notuse,l_use,l_timeout}, R.color.page_text_select, R.color.page_text_normal2, handler, Page_Indicator);
		
		initView();
		
		/*�ж�gps�Ƿ����У���̨gps��ɱ���ˣ��޷�֪��������ֻ�ܱ�����һ�ε�ʱ�����ж��Ƿ���*/
		if(!TimeHelper.isGps(this)){System.out.println("gps-------");
			Intent intent = new Intent(Activity_Order_AllList.this, Servcie_Gps.class);
			startService(intent);
		}
		
		/*�汾���*/
		new HttpHelper().initData(HttpHelper.Method_Get, this, "api/appManage/latest?appType=2", null, null, handler, Request_Version, 1, new TypeReference<ApkInfo>() {});
		
		Public_Param.activity = Activity_Order_AllList.this;
		
	}

	private void initPageInditor() {
		
		t_notuse = (TextView)findViewById(R.id.t_notuse);
		t_use = (TextView)findViewById(R.id.t_use);
		t_timeout = (TextView)findViewById(R.id.t_timeout);
		l_notuse = (View)findViewById(R.id.l_notuse);
		l_use = (View)findViewById(R.id.l_use);
		l_timeout = (View)findViewById(R.id.l_timeout);
	}

	private void initView(){
		
		/*��ť*/
		menu_out = (LinearLayout)findViewById(R.id.menu_out);
		menu_out.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				
				startActivity(new Intent(Activity_Order_AllList.this, Activity_User.class));
			}
		});
		
		ok_true = (TextView)this.findViewById(R.id.ok_true);
		
		ok_true.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				
				startActivity(new Intent(Activity_Order_AllList.this, Activity_Transfer_Return.class));
			}
		});
		
		/*ViewPage*/
		viewPager = (ViewPager)findViewById(R.id.pager);
		
		//�ر�Ԥ���أ�Ĭ��һ��ֻ����һ��Fragment
		viewPager.setOffscreenPageLimit(2);
		
		pages = new Fragment[3];

		page_0 = new Fragment_order();
		page_1 = new Fragment_order_doing();
		page_2 = new Fragment_order_ok();
		
		pages[0] = page_0;
		pages[1] = page_1;
		pages[2] = page_2;
		
		fragmentManager = getSupportFragmentManager();
		pagerAdapter = new MyFragmentPagerAdapter(fragmentManager, pages);
		viewPager.setAdapter(pagerAdapter);
		
		setPageChangeListener();//ҳ�滬��������
				
		//setViewPagerScrollSpeed(800);
		
		Public_Param.viewPager = viewPager;
	}
	
	/* ����ViewPager�Ļ����ٶ� 
	    *  
	    * */  
    private void setViewPagerScrollSpeed(int duration){  
       try {  
    	   scroller = new FixedSpeedScroller( viewPager.getContext( ) );  
           scroller.setScrollDuration(duration);
    	   
           Field fieldScroller = null;  
           fieldScroller = ViewPager.class.getDeclaredField("mScroller");  
           fieldScroller.setAccessible(true);                 
           fieldScroller.set( viewPager, scroller);
           
       }catch(NoSuchFieldException e){  
             
       }catch (IllegalArgumentException e){  
             
       }catch (IllegalAccessException e){  
             
       }  
    }
	  
	private void initHandler() {

		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);

				switch (msg.what) {

					case Page_Indicator:
						
						String state = new Integer(new Integer(HandlerHelper.getString(msg)).intValue()+2).toString();
						System.out.println("״̬��"+state);
						
						switch (new Integer(HandlerHelper.getString(msg)).intValue()) {
							case 0:
								viewPager.setCurrentItem(0);
								break;
	
							case 1:
								viewPager.setCurrentItem(1);
								break;
								
							case 2:
								viewPager.setCurrentItem(2);
								break;
								
							default:
								break;
						}
						break;
					
					case Request_Version:
						System.out.println("��������");
						if(HandlerHelper.getString(msg).equals(HandlerHelper.Ok)){
							System.out.println("1aaaaaaaaaaaaaaaaa+");
							ApkInfo apkInfo = (ApkInfo)msg.obj;
							System.out.println("2aaaaaaaaaaaaaaaaa+");
							if(apkInfo == null || apkInfo.appVersion == null || apkInfo.appVersion.equals("")){System.out.println("3aaaaaaaaaaaaaaaaa+");
								return;
							}
							
							System.out.println("4aaaaaaaaaaaaaaaaa+");
								System.out.println("�汾��Ϣ��"+apkInfo.appAddress);
							if(!(apkInfo.appAddress == null || apkInfo.appAddress.equals(""))){
								System.out.println("5aaaaaaaaaaaaaaaaa+");
								String[] str = apkInfo.appVersion.split("-");
								Public_Param.Version_Content = apkInfo.updateContent;
								
								System.out.println("6aaaaaaaaaaaaaaaaa+"+str[0]);
								System.out.println("7aaaaaaaaaaaaaaaaa�ֻ��汾+"+SystemUtils.getVersion(Activity_Order_AllList.this));
								if(isUp(str[0], SystemUtils.getVersion(Activity_Order_AllList.this))){System.out.println("8aaaaaaaaaaaaaaaaa+");
								
									if(str.length == 2){
										System.out.println("10aaaaaaaaaaaaaaaaa+");
										Public_Param.Version_Name = str[0];
										update(Public_Api.appWebSite+apkInfo.appAddress,str[1], apkInfo.forceUpdate.intValue() == 0 ? false : true);System.out.println("���ص�ַ"+Public_Api.appWebSite+apkInfo.appAddress);
										System.out.println("aaaaaaaaaaaaaaaaaaaaa_apksize"+str[1]);
									}
								}	
								//update(Public_Api.appWebSite+apkInfo.appAddress, );System.out.println("���ص�ַ"+Public_Api.appWebSite+apkInfo.appAddress);
							}

				           	return;
						}
						
						break;	
						
					default:
						break;
				}
			}
		};
	}
	
	private void setPageChangeListener(){
		
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				PageIndicatorHelper.oldIndex = position;
				PageIndicatorHelper.setSelected(Activity_Order_AllList.this, new TextView[]{t_notuse,t_use,t_timeout}, new View[]{l_notuse,l_use,l_timeout}, R.color.page_text_select, R.color.page_text_normal2);
				
				switch (position) {
					case 0:
						page_0.initData();
						break;
						
					case 1:
						page_1.initData();
						break;
						
					case 2:
						page_2.initData();	
						break;
						
					default:
						break;
				}
				System.out.println("onPageSelected--------------"+position);
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				
			}
			
		});
	}
	
	@Override
	protected void onResume() {
	
		super.onResume();
		
		JPushInterface.onResume(this);
	}
	
	@Override
	protected void onPause() {
		
		super.onPause();
		
		JPushInterface.onPause(this);
	}

	/** ����*/
	private void update(String url,String size,boolean isForceUpdate){
		UpdateUtils update = new UpdateUtils(this, url, size, isForceUpdate);
		update.setListener(this);
		update.UpdateManager_do();
	}
	
	@Override
	public void update_finish() {	
		
	}
	
	@Override
	public void closeApp() {
		System.out.println("ǿ�Ƹ����Ƴ�xxxxxxxxxxxxxx");
		finish();				
	}
	
//	public interface OnTabChangedListener{
//		void OnTabChanged(int index);
//	}
//	
//	private OnTabChangedListener listener;
//
//	public void setOnTabChangedListener(OnTabChangedListener listener){
//		this.listener = listener;
//	}
	public boolean isUp(String v_gjcar, String v_local){
		
		boolean isUp = false;
		System.out.println(""+v_gjcar.substring(1, v_gjcar.length()));
		String[] v_gjcars = v_gjcar.substring(1, v_gjcar.length()).split("\\.");System.out.println(""+v_gjcars.length);
		
		String[] v_locals = v_local.substring(1, v_local.length()).split("\\.");
		
		int g_1 = Integer.parseInt(v_gjcars[0]);
		int g_2 = Integer.parseInt(v_gjcars[1]);
		int g_3 = Integer.parseInt(v_gjcars[2]);
		
		int l_1 = Integer.parseInt(v_locals[0]);
		int l_2 = Integer.parseInt(v_locals[1]);
		int l_3 = Integer.parseInt(v_locals[2]);
		
		if(g_1 > l_1){
			
			isUp = true;
		}else{
			
			if(g_1 == l_1 && g_2 > l_2){
				
				isUp = true;
			}else{
				
				if(g_1 == l_1 && g_2 == l_2 && g_3 > l_3){
					
					isUp = true;
				}
			}
		}
		return isUp;
		
	}
}
