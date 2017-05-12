package com.gjmgr.activity.user;

import java.util.ArrayList;

import com.alibaba.fastjson.TypeReference;
import com.gjmgr.annotation.ContentView;
import com.gjmgr.annotation.ContentWidget;
import com.gjmgr.app.R;
import com.gjmgr.data.adapter.OrderList_Adapter;
import com.gjmgr.data.bean.Order;
import com.gjmgr.utils.AnnotationViewUtils;
import com.gjmgr.utils.HandlerHelper;
import com.gjmgr.utils.HttpHelper;

import com.gjmgr.utils.SharedPreferenceHelper;
import com.gjmgr.view.helper.LoadAnimateHelper;
import com.gjmgr.view.listview.XListView;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

@ContentView(R.layout.fragment_order)
public class Activity_Order_List extends Activity{
	
	@ContentWidget(id = R.id.listview) XListView listview;
	private OrderList_Adapter adapter;
	
	/*Handler*/
	private Handler handler;
	private final static int Request = 1;	
	private final static int Click = 2;
	private final static int Show = 3;
	/*数据*/
	private ArrayList<Order> orderlist = new ArrayList<Order>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		AnnotationViewUtils.injectObject(this, this);
		System.out.println("aaaaaaaaaaaaa");
		/*listview*/
		initXListView();
		
		/*handler*/
		initHandler();
		
//		/*标题*/
//		TitleBarHelper.Back(this, "订单列表", 0);
		
		/*加载动画*/
		LoadAnimateHelper.Search_Animate(this, R.id.activity, handler, Click, true,true,1);
		
		/*初始化数据*/
		initData();
		
	}
	
	@TargetApi(Build.VERSION_CODES.CUPCAKE)
	@SuppressLint("NewApi")
	private void initXListView() {
		listview.setPullLoadEnable(false);
		listview.setPullRefreshEnable(false);
		listview.setFooterDividersEnabled(false);
		listview.setHeaderDividersEnabled(false);
	}

	@Override
	protected void onResume() {
		
		super.onResume();
		
		initData();
	}
	
	private void initData() {System.out.println("1");

		String userId = new Integer(SharedPreferenceHelper.getUid(this)).toString();
		
		String api = "api/user/"+userId+"/order?currentPage=1&pageSize=100";
		
		new HttpHelper().initData(HttpHelper.Method_Get, this, api, null, null, handler, Request, 1, new TypeReference<ArrayList<Order>>() {});
		
	}
	
	@SuppressLint("HandlerLeak")
	private void initHandler() {

		handler = new Handler() {
			@SuppressWarnings("unchecked")
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);

				switch (msg.what) {

					case Request:
						
						if(HandlerHelper.getString(msg).equals(HandlerHelper.Ok)){

							orderlist = (ArrayList<Order>)msg.obj;
							System.out.println("size"+orderlist.size());	      
				            //	System.out.println("解析结束"+orderlist.get(0).model );	
				           	if(orderlist.size() == 0){
				           		LoadAnimateHelper.load_empty_animation();
				           	}
				           	handler.sendEmptyMessage(Show);
				           	return;
						}
						if(HandlerHelper.getString(msg).equals(HandlerHelper.Empty)){
							System.out.println("yyyyyyyyyyyyyyyyyyyyyyyyyyy");
							LoadAnimateHelper.load_empty_animation();
				           	return;
						}
						LoadAnimateHelper.load_fail_animation();
						System.out.println("请求失败");	 
																								
						break;
					
					case Click:
						initData();
						break;
						
					case Show:
						OrderList_Adapter adapter = new OrderList_Adapter(Activity_Order_List.this, orderlist, handler, 4,5);
						listview.setAdapter(adapter);
						
						LoadAnimateHelper.load_success_animation();
						
						listview.setOnItemClickListener(new OnItemClickListener() {

							@Override
							public void onItemClick(AdapterView<?> arg0,
									View arg1, int position, long arg3) {
//								Public_Param.order = orderlist.get(position-1);
//								//IntentHelper.startActivity(Activity_Order_List.this, Activity_Order_Detail.class);
//								Class<?> cls = null;
//								if(getIntent().getStringExtra("way").equals("freeride")){
//									cls = Activity_FreeRide_Order_Detail.class;
//								}else{
//									cls = Activity_Order_Detail.class;
//								}
//								Intent intent = new Intent(Activity_Order_List.this, cls);//跳转				
//								intent.putExtra("way", getIntent().getStringExtra("way"));
//								startActivity(intent);
							}
						});
						break;
						
					default:
						break;
				}
			}
		};
	}
		
}
