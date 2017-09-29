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
import com.gjmgr.data.adapter.OrderList_Adapter_Ok;
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
import com.gjmgr.view.widget.MyListView;
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

public class Fragment_order_ok extends Fragment{

	@ContentWidget(id = R.id.listview) XListView listview;
	private OrderList_Adapter_Ok adapter;

	/*Handler*/
	private Handler handler;
	private final static int Request = 1;	
	private final static int Click = 2;
	private final static int Show = 3;
	
	/*����*/
	private ArrayList<Order> orderlist = new ArrayList<Order>();
	private ArrayList<Order> orderlist_show = new ArrayList<Order>();
	
	/*����*/
	private LoadAnimateHelper_Fragment loadHelper = new LoadAnimateHelper_Fragment();
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.fragment_order_ok, null);		
		AnnotationViewFUtils.injectObject(this, getActivity(), view);
		
		/*listview*/
		initXListView();
		
		/*handler*/
		initHandler();
		
//		/*����*/
//		TitleBarHelper.Back(this, "�����б�", 0);
		System.out.println("����TTTTTTTTTTTTTTTTT");

		/*���ض���*/
		loadHelper.Search_Animate(getActivity(), view, R.id.activity, handler, Click, true,true,0,"����û������ɵĶ�������");
		
		/*��ʼ������*/
		initData();System.out.println("�������ˢ��onCreateView");
		
		return view;
	}

	@Override
	public void onStart() {
		
		super.onStart();
		
		if(Public_Param.isReturnCarOK){

			Public_Param.isReturnCarOK = false;System.out.println("�������ˢ��onStart");
			//initData();
			return;
		}
		initData();System.out.println("�����������onStart");
		
	}
	
	private void initXListView() {
		listview.setPullLoadEnable(false);
		listview.setPullRefreshEnable(false);
		listview.setFooterDividersEnabled(false);
		listview.setHeaderDividersEnabled(false);
	}
	
	public void initData() {System.out.println("�������initData");
		
		listview.setVisibility(View.GONE);	
		orderlist_show.clear();
		orderlist.clear();
		String driverId = SharedPreferenceHelper.getString(getActivity(), Public_SP.Account, "id");
		
		String api = "api/dispatch/task-list?currentPage=1&pageSize=20&orderBy=4&dispatchStatus=50&driverId="+driverId;//�����
		
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

					case Request:
						loadHelper.load_success_animation();
						if(HandlerHelper.getString(msg).equals(HandlerHelper.Ok)){
							
							orderlist_show = new ArrayList<Order>();
							orderlist = (ArrayList<Order>)msg.obj;System.out.println("orderlist��С"+orderlist.size());
							orderlist = ListHelper.getListByOrderId(orderlist);System.out.println("orderlist��С-����"+orderlist.size());
							for (int i = 0; i < orderlist.size(); i++) {
								
//								reqest_Data(orderlist.get(i).orderCode, orderlist.get(i), orderlist.get(i).orderType.intValue());
								if(orderlist.get(i).orderType.intValue() == 3 || orderlist.get(i).orderType.intValue() == 4){
									System.out.println("ppp-"+i);
									reqest_Data(orderlist.get(i).orderCode,orderlist.get(i),orderlist.get(i).orderType.intValue());	
								}
							}

//							System.out.println("size"+orderlist.size());	      
//				            //	System.out.println("��������"+orderlist.get(0).model );	
//				           	if(orderlist.size() == 0){
//				           		loadHelper.load_empty_animation();
//				           	}
//				           	
//				           	handler.sendEmptyMessage(Show);
				           	return;
						}
						if(HandlerHelper.getString(msg).equals(HandlerHelper.Empty)){
							System.out.println("yyyyyyyyyyyyyyyyyyyyyyyyyyy");
							//loadHelper.load_empty_animation();
				           	return;
						}
						//loadHelper.load_fail_animation();
						System.out.println("����ʧ��");	 
																								
						break;
							
					case Click:
						initData();
						break;
							
					case Show:
						adapter = new OrderList_Adapter_Ok(getActivity(), orderlist, false);
						listview.setAdapter(adapter);
						listview.setVisibility(View.VISIBLE);	
						loadHelper.load_success_animation();
							
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
			case 3://����
				api = "api/driver/"+orderId+"/order";
				break;
	
			case 4://���ͻ�
				api = "api/airportTrip/"+orderId+"/order";
				break;
			default:
				break;
		}
		
		/* ���������½ */
		AsyncHttpClient httpClient = new AsyncHttpClient();

		RequestParams params = new RequestParams();// �����������

		String url = Public_Api.appWebSite + api;System.out.println("ppp-"+api);
		
		httpClient.get(url, params, new AsyncHttpResponseHandler() {

			/* ��������ɹ� */
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				
				if(arg2==null ||new String(arg2)==null||new String(arg2).equals("")){
					
					return;
				}
				
				String backData = new String(arg2);
				System.out.println("������ɹ�:" + backData);System.out.println("ppp-����");

				JSONObject statusjobject = JSON.parseObject(backData);

				boolean status = statusjobject.getBoolean("status");
				String message = statusjobject.getString("message");System.out.println("ppp-ok");

				if (status) {
					
					JSONObject j = JSON.parseObject(message);
					int hasContract = j.getIntValue("hasContract");
					int orderState = j.getIntValue("orderState");
					if(hasContract == 1 && orderState != 5){System.out.println("ppp-over");
						orderlist_show.add(order);System.out.println("orderlist_show��С"+orderlist_show.size());
					System.out.println("listɸѡ-orderid��type"+orderId+"--"+order.orderType);System.out.println("ppp-size");
						adapter = new OrderList_Adapter_Ok(getActivity(), ListHelper.getListByOrderId(orderlist_show), false);
						listview.setVisibility(View.VISIBLE);
						listview.setAdapter(adapter);
//						listview.setOnItemClickListener(new OnItemClickListener() {
//						
//							@Override
//							public void onItemClick(AdapterView<?> arg0,
//									View arg1, int poisition, long arg3) {
//								System.out.println("����¼�����");
//								Public_Param.orderId_detail = orderlist_show.get(poisition-1).orderCode;
//								
//								IntentHelper.startActivity(getActivity(), Activity_Order_Detail.class);
//							}
//						});
					}
				}				
				
			}

			/* 5.��������ʧ�� */
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {

			}
		});
	}
}
