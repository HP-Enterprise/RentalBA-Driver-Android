package com.gjmgr.activity.user;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.TypeReference;
import com.baidu.mapapi.map.MapView;
import com.gjmgr.annotation.ContentWidget;
import com.gjmgr.app.R;
import com.gjmgr.data.adapter.OrderList_Adapter;
import com.gjmgr.data.bean.Order;
import com.gjmgr.data.data.Public_Api;
import com.gjmgr.data.data.Public_Data;
import com.gjmgr.data.data.Public_Flag;
import com.gjmgr.data.data.Public_Param;
import com.gjmgr.data.data.Public_SP;
import com.gjmgr.utils.AnnotationViewFUtils;
import com.gjmgr.utils.HandlerHelper;
import com.gjmgr.utils.HttpHelper;
import com.gjmgr.utils.IntentHelper;
import com.gjmgr.utils.SharedPreferenceHelper;
import com.gjmgr.utils.TimeHelper;
import com.gjmgr.utils.ToastHelper;
import com.gjmgr.view.dialog.SubmitDialog;
import com.gjmgr.view.helper.LoadAnimateHelper_Fragment;
import com.gjmgr.view.listview.XListView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class Fragment_order extends Fragment{

	@ContentWidget(id = R.id.listview) XListView listview;
	private OrderList_Adapter  adapter;

	/*Handler*/
	private Handler handler;
	private final static int Request = 1;	
	private final static int Click = 2;
	private final static int Show = 3;
	private final static int Order_Confirm = 4;
	private final static int Order_Reject = 5;
	
	private Handler myHandler;
	private boolean isOut = false;
	/*数据*/
	private ArrayList<Order> orderlist = new ArrayList<Order>();
	
	/*动画*/
	private LoadAnimateHelper_Fragment loadHelper = new LoadAnimateHelper_Fragment();
	
	@Override
	public void onResume() {
		System.out.println("page1------------------onResume");
		
		super.onResume();
	}
	
	@Override
	public void onPause() {
		System.out.println("page1------------------onPause");
		super.onPause();
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
	
		View view = inflater.inflate(R.layout.fragment_order, null);		
		AnnotationViewFUtils.injectObject(this, getActivity(), view);
		
		/*listview*/
		initXListView();
		
		/*handler*/
		initHandler();
		
		initTaskHander();
		
//		/*标题*/
//		TitleBarHelper.Back(this, "订单列表", 0);
		System.out.println("标题TTTTTTTTTTTTTTTTT");

		
		/*加载动画*/
		loadHelper.Search_Animate(getActivity(), view, R.id.activity, handler, Click, true,true,0,"您还没有待接单的订单任务");
		
		/*初始化数据*/
		initData();
		
		return view;
	}
	
	private void initXListView() {
		listview.setPullLoadEnable(false);
		listview.setPullRefreshEnable(false);
		listview.setFooterDividersEnabled(false);
		listview.setHeaderDividersEnabled(false);
	}
	
	public void initData() {System.out.println("1");
	
		listview.setVisibility(View.GONE);	
		String driverId = SharedPreferenceHelper.getString(getActivity(), Public_SP.Account, "id");
	
		String api = Public_Api.api_task_list+"?currentPage=1&pageSize=10&orderBy=2&dispatchStatus=20&driverId="+driverId;//已分配
		
		new HttpHelper().initData(HttpHelper.Method_Get, getActivity(), api, null, null, handler, Request, 1, new TypeReference<ArrayList<Order>>() {});
		
	}
	
	@SuppressLint("HandlerLeak")
	private void initHandler() {

		handler = new Handler() {
			@SuppressWarnings("unchecked")
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);

				switch (msg.what) {

					case Order_Confirm:
					
						SubmitDialog.closeSubmitDialog();
						
						if(HandlerHelper.getString(msg).equals(HandlerHelper.Ok)){
							
							ToastHelper.showToastShort(getActivity(), "接受任务成功");
							Public_Param.viewPager.setCurrentItem(1);
							
				           	return;
						}
						
						ToastHelper.showToastShort(getActivity(), "接受任务失败");
																								
						break;
				
					case Order_Reject:
						
						SubmitDialog.closeSubmitDialog();
						
						if(HandlerHelper.getString(msg).equals(HandlerHelper.Ok)){
	
							ToastHelper.showToastShort(getActivity(), "拒绝任务成功");
							initData();
				           	return;
						}
						
						ToastHelper.showToastShort(getActivity(), "拒绝任务失败");
																								
						break;
						
					case Request:
						listview.setVisibility(View.GONE);
						
						if(HandlerHelper.getString(msg).equals(HandlerHelper.Ok)){

							orderlist = (ArrayList<Order>)msg.obj;
							System.out.println("size"+orderlist.size());	      
				            //	System.out.println("解析结束"+orderlist.get(0).model );	
				           	if(orderlist.size() == 0){
				           		loadHelper.load_empty_animation();
				           	}
				           	
				           	handler.sendEmptyMessage(Show);
				           	return;
						}
						if(HandlerHelper.getString(msg).equals(HandlerHelper.Empty)){
							System.out.println("yyyyyyyyyyyyyyyyyyyyyyyyyyy");
							loadHelper.load_empty_animation();
				           	return;
						}
						loadHelper.load_fail_animation();
						System.out.println("请求失败");	 
																								
						break;
					
					case Click:
						initData();
						break;
						
					case Show:
						listview.setVisibility(View.VISIBLE);
						adapter = new OrderList_Adapter(getActivity(), orderlist, handler, Order_Confirm, Order_Reject);
						listview.setAdapter(adapter);
						listview.setVisibility(View.VISIBLE);
//						listview.setOnItemClickListener(new OnItemClickListener() {
//
//							@Override
//							public void onItemClick(AdapterView<?> arg0,
//									View arg1, int poisition, long arg3) {
//								System.out.println("点击事件设置");
//								Public_Param.orderId_detail = orderlist.get(poisition-1).orderCode;
//								IntentHelper.startActivity(getActivity(), Activity_Order_Detail.class);
//							}
//						});
						loadHelper.load_success_animation();

						break;
						
					default:
						break;
				}
			}
		};
	}
	
	private void initTaskHander() {
		
		myHandler = new Handler(){
			
			@Override
			public void handleMessage(Message msg) {
				System.out.println("handlerMessage");
				
				flush();
				super.handleMessage(msg);
			}
		};
		
		flush();
	}
	
	private void flush(){
		
		myHandler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				
				System.out.println("当前时间-------------"+TimeHelper.getNowDateTime());
				if(Public_Data.isReceiveTask){
					
					initData();
					Public_Data.isReceiveTask = false;
					System.out.println("收到通知，刷新数据");
				}
				
				if(!isOut){
					myHandler.postDelayed(this, 1*1000);
				}
				
			}
		}, 1*1000);
	}
	
	@Override
	public void onDestroy() {
		
		isOut = true;
		super.onDestroy();
	}
}
