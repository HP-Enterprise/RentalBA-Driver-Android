package com.gjmgr.activity.user;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baidu.mapapi.map.MapView;
import com.gjmgr.annotation.ContentWidget;
import com.gjmgr.app.R;
import com.gjmgr.data.adapter.OrderList_Adapter;
import com.gjmgr.data.adapter.OrderList_Adapter_Doing;
import com.gjmgr.data.bean.Order;
import com.gjmgr.data.data.Public_Api;
import com.gjmgr.data.data.Public_Param;
import com.gjmgr.data.data.Public_SP;
import com.gjmgr.data.helper.ListHelper;
import com.gjmgr.utils.AnnotationViewFUtils;
import com.gjmgr.utils.HandlerHelper;
import com.gjmgr.utils.HttpHelper;
import com.gjmgr.utils.IntentHelper;
import com.gjmgr.utils.SharedPreferenceHelper;
import com.gjmgr.view.helper.LoadAnimateHelper_Fragment;
import com.gjmgr.view.listview.XListView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class Fragment_order_doing extends Fragment{
	
	@ContentWidget(id = R.id.listview) XListView listview;
	private OrderList_Adapter_Doing adapter;
	
	/*Handler*/
	private Handler handler;
	private final static int Request_notdo = 1;
	private final static int Request_doing = 2;
	private final static int Request_car = 6;
	
	private final static int Click = 3;
	private final static int Show = 4;
	private int count = 4;
	
	/*数据*/
	private ArrayList<Order> orderlist = new ArrayList<Order>();
	
	/*动画*/
	private LoadAnimateHelper_Fragment loadHelper = new LoadAnimateHelper_Fragment();
	
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.fragment_order_doing, null);		
		AnnotationViewFUtils.injectObject(this, getActivity(), view);
		
		/*listview*/
		initXListView();
		
		/*handler*/
		initHandler();
		
//		/*标题*/
//		TitleBarHelper.Back(this, "订单列表", 0);
		System.out.println("标题TTTTTTTTTTTTTTTTT");
		
		/*加载动画*/
		loadHelper.Search_Animate(getActivity(), view, R.id.activity, handler, Click, true,true,0,"您还没有待完成的订单任务");
		
		/*初始化数据*/
		initData();
		adapter = new OrderList_Adapter_Doing(getActivity(), orderlist, false);
		
		return view;
	}

	@Override
	public void onStart() {
		System.out.println("page1--d----------------onStart");
		super.onStart();
		System.out.println("Public_Param.isReturnCarOK"+Public_Param.isReturnCarOK);
		if(Public_Param.isReturnCarOK){

			//Public_Param.isReturnCarOK = false;
			Public_Param.viewPager.setCurrentItem(2);
			return;
		}
		
		initData();
	}

	private void initXListView() {
		
		listview.setPullLoadEnable(false);
		listview.setPullRefreshEnable(false);
		listview.setFooterDividersEnabled(false);
		listview.setHeaderDividersEnabled(false);
	}
	
	public void initData() {System.out.println("待完成--未上车--------------------------");
	
		
		listview.setVisibility(View.GONE);
		orderlist.clear();
		String driverId = SharedPreferenceHelper.getString(getActivity(), Public_SP.Account, "id");
		
		String api = "api/dispatch/task-list?currentPage=1&pageSize=20&orderBy=2&dispatchStatus=30&driverId="+driverId;;//已确认
		
		new HttpHelper().initData(HttpHelper.Method_Get, getActivity(), api, null, null, handler, Request_notdo, 1, new TypeReference<ArrayList<Order>>() {});
		
	}
	
	public void initData_car() {System.out.println("待完成--未上车--------------------------");

		String driverId = SharedPreferenceHelper.getString(getActivity(), Public_SP.Account, "id");
		
		String api = "api/dispatch/task-list?currentPage=1&pageSize=20&orderBy=2&dispatchStatus=50&driverId="+driverId;;//已确认
		
		new HttpHelper().initData(HttpHelper.Method_Get, getActivity(), api, null, null, handler, Request_car, 1, new TypeReference<ArrayList<Order>>() {});
		
	}
	
	@SuppressLint("HandlerLeak")
	private void initHandler() {

		handler = new Handler() {
			@SuppressWarnings("unchecked")
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				
				switch (msg.what) {

					case Request_notdo:
						
						if(HandlerHelper.getString(msg).equals(HandlerHelper.Ok)){
							
							orderlist = (ArrayList<Order>)msg.obj;
						}
						
						System.out.println("待完成--已上车--------------------------");
						String driverId = SharedPreferenceHelper.getString(getActivity(), Public_SP.Account, "id");
						
						String api = "api/dispatch/task-list?currentPage=1&pageSize=20&orderBy=4&dispatchStatus=35&driverId="+driverId;;//已确认
						
						new HttpHelper().initData(HttpHelper.Method_Get, getActivity(), api, null, null, handler, Request_doing, 1, new TypeReference<ArrayList<Order>>() {});
						initData_car();		
						break;
						
					case Request_doing:
						loadHelper.load_success_animation();
						if(HandlerHelper.getString(msg).equals(HandlerHelper.Ok)){
							
							ArrayList<Order> mylist = (ArrayList<Order>)msg.obj;
							
							for (int i = 0; i < mylist.size(); i++) {
								
								orderlist.add(mylist.get(i));
							}
							System.out.println("执行完xxxxxxxxxxxxxxxxxxxx"+orderlist.size());
				           	if( orderlist != null && orderlist.size() == 0){
				           		
//				           		loadHelper.load_empty_animation();
				           		return;
				           	}
				           	
//				           	loadHelper.load_success_animation();
				           	
				           	handler.sendEmptyMessage(Show);		
				           	
				           	return;
						}
							
						if( orderlist != null && orderlist.size() == 0){
			           		
//			           		loadHelper.load_empty_animation();
			           		return;
			           	}
			           	
//			           	loadHelper.load_success_animation();
						
						handler.sendEmptyMessage(Show);	
						
						break;
					
					case Request_car:
						
						if(HandlerHelper.getString(msg).equals(HandlerHelper.Ok)){
							
							ArrayList<Order> mylist = (ArrayList<Order>)msg.obj;
							
							for (int i = 0; i < mylist.size(); i++) {
								
								reqest_Data(mylist.get(i).orderCode,mylist.get(i),mylist.get(i).orderType.intValue());							
								
							}
						}	
						break;
			
					case Click:
						initData();
						break;
						
					case Show:
						adapter = new OrderList_Adapter_Doing(getActivity(), orderlist, false);
						listview.setVisibility(View.VISIBLE);
						listview.setAdapter(adapter);
//						listview.setOnItemClickListener(new OnItemClickListener() {
//
//							@Override
//							public void onItemClick(AdapterView<?> arg0,
//									View arg1, int poisition, long arg3) {
//								System.out.println("点击事件设置");
////								Public_Param.orderId_detail = orderlist.get(poisition-1).orderCode;
////								
////								IntentHelper.startActivity(getActivity(), Activity_Order_Detail.class);
//								Public_Param.viewPager.setCurrentItem(2);
//							}
//						});
										
						break;
						
					default:
						break;
				}
			}
		};
	}

	private void reqest_Data(final String orderId, final Order order, final int orderType) {
	
		System.out.println("xxxxxxxxxhu");
		String api = "";
		switch (orderType) {
			case 3://代驾
				api = "api/driver/"+orderId+"/order";
				break;
	
			case 4://接送机
				api = "api/airportTrip/"+orderId+"/order";
				break;
			default:
				break;
		}
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

				boolean status = statusjobject.getBoolean("status");
				String message = statusjobject.getString("message");

				if (status) {
					
					JSONObject j = JSON.parseObject(message);
					int hasContract = j.getIntValue("hasContract");
					int orderState = j.getIntValue("orderState");
					if(hasContract == 1 && orderState == 5){
						orderlist.add(order);
						adapter = new OrderList_Adapter_Doing(getActivity(), ListHelper.getListByOrderId(orderlist), false);
						listview.setVisibility(View.VISIBLE);
						listview.setAdapter(adapter);
//						listview.setOnItemClickListener(new OnItemClickListener() {
//						
//							@Override
//							public void onItemClick(AdapterView<?> arg0,
//									View arg1, int poisition, long arg3) {
//								System.out.println("点击事件设置");
//								Public_Param.orderId_detail = orderlist.get(poisition-1).orderCode;
//								
//								IntentHelper.startActivity(getActivity(), Activity_Order_Detail.class);
//							}
//						});
					}
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
