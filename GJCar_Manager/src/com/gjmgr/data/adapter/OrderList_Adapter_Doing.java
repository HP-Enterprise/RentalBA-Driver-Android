package com.gjmgr.data.adapter;


import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.gjmgr.activity.user.Activity_Car_Allot;
import com.gjmgr.activity.user.Activity_Car_Return;
import com.gjmgr.activity.user.Activity_Order_Detail;
import com.gjmgr.activity.user.Activity_UpCar;
import com.gjmgr.app.R;
import com.gjmgr.data.bean.Order;
import com.gjmgr.data.data.Public_Api;
import com.gjmgr.data.data.Public_Param;
import com.gjmgr.utils.HandlerHelper;
import com.gjmgr.utils.HttpHelper;
import com.gjmgr.utils.IntentHelper;
import com.gjmgr.utils.TimeHelper;
import com.gjmgr.utils.ToastHelper;
import com.gjmgr.view.widget.SingleLineZoomTextView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.webkit.DownloadListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
/**
 *
 * 
 */
public class OrderList_Adapter_Doing extends BaseAdapter {

	private ArrayList<Order> orderlist = new ArrayList<Order>();
	private Context context;
	
	private Handler handler;
	private Handler contractHandler;
	private Handler downHandler;
	private boolean isRequest = false;
	
	String[] vendorIds;
	String[] modelIds;
	String[] models;
	int[] hasContacts;
	//int[] downMilleages;
	
	int takecar_ordertype = 0;
	
	public OrderList_Adapter_Doing(Context context, ArrayList<Order> orderlist, boolean isDoorToDoor) {
		
		this.orderlist = orderlist;System.out.println("�޸�bug��ʼ"+orderlist.size());
		this.context = context;
		
		this.vendorIds = new String[orderlist.size()];
		this.modelIds = new String[orderlist.size()];
		this.models = new String[orderlist.size()];
		this.hasContacts = new int[orderlist.size()];
//		this.downMilleages = new int[orderlist.size()];
		
		initHandler();
		
		initHandler2();
		
		initHandler3();
	}

	@Override
	public int getCount() {
		
		return orderlist.size();
	}
		
	@Override
	public Object getItem(int position) {
		
		return orderlist.get(position);
	}

	@Override
	public long getItemId(int position) {
		
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {System.out.println("aaaaaaaaaaaaaaaaa1");
		
		Holder holder;System.out.println("aaaaaaaaaaaaaaaaa1");
		holder = new Holder();
		convertView = View.inflate(context, R.layout.orderlist_item_doing_test, null);
		
		holder.a_lin  = (LinearLayout) convertView.findViewById(R.id.a_lin); 
		
		holder.a_order_ty  = (TextView) convertView.findViewById(R.id.a_order_ty);	System.out.println("a2");   
		holder.a_order_time  = (TextView) convertView.findViewById(R.id.a_order_time);	System.out.println("a3");

		holder.a_orderId = (TextView) convertView.findViewById(R.id.a_orderId);	System.out.println("a4");
		holder.a_order_distance = (TextView) convertView.findViewById(R.id.a_order_distance);	System.out.println("a5");
		holder.b_air_message = (SingleLineZoomTextView) convertView.findViewById(R.id.b_air_message);	System.out.println("a6");
		holder.b_start_address = (TextView) convertView.findViewById(R.id.b_start_address);	System.out.println("a7");
		holder.b_car = (TextView) convertView.findViewById(R.id.b_car);	System.out.println("a8");
		holder.b_end_address = (TextView) convertView.findViewById(R.id.b_end_address);	System.out.println("a9");

		holder.c_getcar = (TextView) convertView.findViewById(R.id.c_getcar);	
		holder.c_ok = (TextView) convertView.findViewById(R.id.c_ok);	
		holder.c_cancle = (TextView) convertView.findViewById(R.id.c_cancle);
		holder.c_returncar = (TextView) convertView.findViewById(R.id.c_returncar);
		
		holder.c_getcar.setVisibility(View.VISIBLE);
		holder.c_ok.setVisibility(View.VISIBLE);
		holder.c_cancle.setVisibility(View.VISIBLE);
		holder.c_returncar.setVisibility(View.VISIBLE);
//		if (convertView == null) {
//			holder = new Holder();
//			convertView = View.inflate(context, R.layout.orderlist_item_doing_test, null);
//			
//			holder.a_lin  = (LinearLayout) convertView.findViewById(R.id.a_lin); 
//			
//			holder.a_order_ty  = (TextView) convertView.findViewById(R.id.a_order_ty);	System.out.println("a2");   
//			holder.a_order_time  = (TextView) convertView.findViewById(R.id.a_order_time);	System.out.println("a3");
//
//			holder.a_orderId = (TextView) convertView.findViewById(R.id.a_orderId);	System.out.println("a4");
//			holder.a_order_distance = (TextView) convertView.findViewById(R.id.a_order_distance);	System.out.println("a5");
//			holder.b_air_message = (SingleLineZoomTextView) convertView.findViewById(R.id.b_air_message);	System.out.println("a6");
//			holder.b_start_address = (TextView) convertView.findViewById(R.id.b_start_address);	System.out.println("a7");
//			holder.b_car = (TextView) convertView.findViewById(R.id.b_car);	System.out.println("a8");
//			holder.b_end_address = (TextView) convertView.findViewById(R.id.b_end_address);	System.out.println("a9");
//
//			holder.c_getcar = (TextView) convertView.findViewById(R.id.c_getcar);	
//			holder.c_ok = (TextView) convertView.findViewById(R.id.c_ok);	
//			holder.c_cancle = (TextView) convertView.findViewById(R.id.c_cancle);
//			holder.c_returncar = (TextView) convertView.findViewById(R.id.c_returncar);
//			
//			holder.c_getcar.setVisibility(View.VISIBLE);
//			holder.c_ok.setVisibility(View.VISIBLE);
//			holder.c_cancle.setVisibility(View.VISIBLE);
//			holder.c_returncar.setVisibility(View.VISIBLE);
//			
//			convertView.setTag(holder);
//		} else {
//			holder = (Holder) convertView.getTag();
//		}
		System.out.println("aaaaaaaaaaaaaaaaa5");
		holder.a_order_ty.setText(orderlist.get(position).orderTypeName);System.out.println("a11");
		
		holder.a_orderId.setText("������ţ�"+orderlist.get(position).orderCode+"");System.out.println("a12");
		//holder.a_order_distance.setText("Ԥ����̣�"+orderlist.get(position).tripDistance+"����"+"");System.out.println("a13");
		//holder.b_air_message.setText("������Ϣ��"+orderlist.get(position).airport+"+"+orderlist.get(position).flightNumber);	System.out.println("a14");
//		holder.b_start_address.setText(TimeHelper.getDateTime_YM(TimeHelper.getTimemis_to_StringTime(orderlist.get(position).expectEndTime)));
		holder.b_car.setText(orderlist.get(position).modelName+"");System.out.println("a15");
		holder.b_end_address.setText(orderlist.get(position).tripAddress+"");	System.out.println("a16");
		
		holder.c_getcar.setVisibility(View.GONE);
		holder.c_ok.setVisibility(View.GONE);
		holder.c_cancle.setVisibility(View.GONE);
		holder.c_returncar.setVisibility(View.GONE);
		System.out.println("�޸�bug:"+orderlist.get(position).orderCode);
		System.out.println("�޸�bug-position:"+position);
		reqest_Data(orderlist.get(position).orderType, orderlist.get(position).orderCode,position,
				
				holder.a_order_ty,holder.a_order_distance,holder.b_air_message,holder.a_order_time,holder.b_start_address,holder.b_end_address,orderlist.get(position).dispatchStatus,
				
				holder.c_ok,holder.c_cancle,holder.c_getcar,holder.c_returncar);		
		
		button_status(orderlist.get(position).orderCode,orderlist.get(position).orderType,
				holder.c_ok,holder.c_cancle,holder.c_getcar,holder.c_returncar);
		//reqest_Data(orderlist.get(position).orderCode, position, holder.c_ok,holder.c_cancle,holder.c_getcar,holder.c_returncar, orderlist.get(position).dispatchStatus);
		System.out.println("aaaaaaaaaaaaaaaaa11");
		//reqest_Contackt(orderlist.get(position).orderCode, holder.b_car);
			
		holder.a_lin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {System.out.println("xxxxxxxxxxxxxxxxxx");
				
				Public_Param.orderdeaail_orderType = orderlist.get(position).orderType.intValue();
				Public_Param.orderId_detail = orderlist.get(position).orderCode;
				
				IntentHelper.startActivity(context, Activity_Order_Detail.class);

			}
		});
		
		holder.c_ok.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {

				if(isRequest){//����������󣬾Ͳ�Ȼ������
					
					return;
				}
				isRequest = true;				
				
				String api = "";
				switch (orderlist.get(position).orderType.intValue()) {
					case 2://�ŵ���
						api = "api/door/"+orderlist.get(position).orderCode+"/contract";
						break;
						
					case 3://����
						api = "api/contract/"+orderlist.get(position).orderCode+"/contractDetail";
						break;
			
					case 4://���ͻ�
						api = "api/airportTrip/"+orderlist.get(position).orderCode+"/contract";
						break;
					default:
						break;
				}

				takecar_ordertype = orderlist.get(position).orderType.intValue();
				
				Public_Param.upcar_orderType = orderlist.get(position).orderType.intValue();
				
				new HttpHelper().initData(HttpHelper.Method_Get, context, api, null, null, contractHandler, position, 1, new TypeReference<Order>() {});		
				
			}
		});
		
		holder.c_cancle.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				
				if(isRequest){//����������󣬾Ͳ�Ȼ������
					
					return;
				}
				isRequest = true;				
				
				String api = "";
				switch (orderlist.get(position).orderType) {
					case 2://�ŵ���
						api = "api/door/"+orderlist.get(position).orderCode+"/contract";
						break;
						
					case 3://����
						api = "api/contract/"+orderlist.get(position).orderCode+"/contractDetail";
						break;
			
					case 4://���ͻ�
						api = "api/airportTrip/"+orderlist.get(position).orderCode+"/contract";
						break;
					default:
						break;
				}
				
				Public_Param.upcar_orderType = orderlist.get(position).orderType.intValue();
								
				new HttpHelper().initData(HttpHelper.Method_Get, context, api, null, null, downHandler, position, 1, new TypeReference<Order>() {});		
				
			}
		});
		
		holder.c_getcar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				
				Public_Param.orderId = orderlist.get(position).orderCode;
				Public_Param.vendorId = vendorIds[position];
				Public_Param.modelId = modelIds[position];
				Public_Param.model = models[position];
				
				Public_Param.takecar_orderType = orderlist.get(position).orderType.intValue();
				is_HasContact(orderlist.get(position).orderCode,orderlist.get(position).orderType);
			}
		});
		
		holder.c_returncar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				
				Public_Param.return_orderType = orderlist.get(position).orderType.intValue();
				Public_Param.orderId = orderlist.get(position).orderCode;
				reqest_car_takemilleage(orderlist.get(position).orderType.intValue(),orderlist.get(position).orderCode);
			}
		});
		
		return convertView;
		
	}

	public static class Holder {
		
		/*��ʼ���ؼ�*/
		private LinearLayout a_lin;
		
		private TextView a_order_ty;  
		private TextView a_order_time;

		private TextView a_orderId;
		private TextView a_order_distance;
		private SingleLineZoomTextView b_air_message;
		private TextView b_start_address;
		private TextView b_car;
		private TextView b_end_address;

		private TextView c_getcar;
		private TextView c_ok;
		private TextView c_cancle;
		private TextView c_returncar;
	}

	private void initHandler() {

		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
		
				int position = msg.what;
				
				isRequest = false;
				
				if(msg.getData().getString("message").equals(HandlerHelper.Ok)){
					
					Order order = (Order)msg.obj;
					
					/*��δ���ɺ�ͬ*/
					if(order == null || order.hasContract.equals("0")){
						
						ToastHelper.showToastShort(context, "�������ɺ�ͬ��,���Ե�");
						return;
					}
					
					/*������id*/
					isRequest = true;

					String api = "api/airportTrip/"+orderlist.get(position).orderCode+"/contract";
					
					if(orderlist.get(position).dispatchOrigin.equals("1")){
						api = "api/door/"+orderlist.get(position).orderCode+"/contract";
					}
					
					new HttpHelper().initData(HttpHelper.Method_Get, context, api, null, null, contractHandler, position, 1, new TypeReference<Order>() {});		
					
				}
								
			}
		};
	}
		
	private void initHandler2() {
		
		contractHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
		
				isRequest = false;
				
				int position = msg.what;
				
				if(msg.getData().getString("message").equals(HandlerHelper.Ok)){
					
					Order order = (Order)msg.obj;
					
					if(order == null || order.vehicleId == null || order.vehicleId.equals("null") || order.vehicleId.equals("")){
						
						ToastHelper.showToastShort(context, "�ᳵ�����ִ�д˲���");						
						return;
					}
					
					IntentHelper.startActivity_StringExtras(context, Activity_UpCar.class, new String[]{"from","dispatchOrigin"}, new String[]{"up",orderlist.get(position).dispatchOrigin});

					Public_Param.order_up.id = orderlist.get(position).id;
					Public_Param.order_up.driverId = orderlist.get(position).driverId;
					Public_Param.order_up.orderCode = orderlist.get(position).orderCode;
					Public_Param.order_up.vehicleId = order.vehicleId;
					Public_Param.order_up.takeCarMileage =  order.takeCarMileage;					
					
					switch (takecar_ordertype) {
						case 2://�ŵ���
							
							break;
							
						case 3://����
							Public_Param.order_up.takeCarActualDate = order.driverTakeCarDate;
							Public_Param.order_up.takeCarMileage =( order.driverTakeCarMileage == null ? 1 : order.driverTakeCarMileage);
							
							break;
				
						case 4://���ͻ�
							Public_Param.order_up.takeCarActualDate = order.takeCarActualDate;
							break;
							
						default:
							break;
					}
										
					return;
				}
				
				ToastHelper.showToastShort(context, "���ɺ�ͬ�����ִ�д˲���");
			}
		};
	}
		private void initHandler3() {
			
			downHandler = new Handler() {
				@Override
				public void handleMessage(Message msg) {
					super.handleMessage(msg);
			
					isRequest = false;
					
					int position = msg.what;
					
					if(msg.getData().getString("message").equals(HandlerHelper.Ok)){
						
						Order order = (Order)msg.obj;
						
						if(order == null){
							
							ToastHelper.showToastShort(context, "���ʧ��");						
							return;
						}
						
						IntentHelper.startActivity_StringExtras(context, Activity_UpCar.class, new String[]{"from","dispatchOrigin"}, new String[]{"down",orderlist.get(position).dispatchOrigin});

						Public_Param.order_down.id = orderlist.get(position).id;
						Public_Param.order_down.driverId = orderlist.get(position).driverId;
						Public_Param.order_down.clientUpMileage = order.clientUpMileage;
						Public_Param.order_down.clientUpCarDate = order.clientUpCarDate;
						
						return;
					}
					
					ToastHelper.showToastShort(context, "���ɺ�ͬ�����ִ�д˲���");
			}
		};
		
	}
	
	private void reqest_Data(final int orderType, final String orderId, final int index,  final TextView tv_triptype, final TextView distance, final TextView tv_message, final TextView date,final TextView b_start_address, final TextView b_end_address,final String dispath,
			
			final TextView c_ok,final TextView c_cancle,final TextView c_getcar,final TextView c_returncar) {
		System.out.println("��������"+orderId);
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

		String url = Public_Api.appWebSite + api;
		
		httpClient.get(url, params, new AsyncHttpResponseHandler() {

			/* ��������ɹ� */
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {

				String backData = new String(arg2);
				System.out.println("����ֵ\n:" + backData);

				JSONObject statusjobject = JSON.parseObject(backData);

				boolean status = statusjobject.getBoolean("status");
				String message = statusjobject.getString("message");

				if (status) {
					/*������Ϣ*/
					JSONObject j1 = JSON.parseObject(message);
					
					if(orderType == 4){
						
						String airlineCompany = j1.getString("airlineCompany");
						String flightNumber = j1.getString("flightNumber");
						Integer tripType = j1.getInteger("tripType");
						String tripAddress = j1.getString("tripAddress");
						
						String tripDistance = (j1.getString("tripDistance")== null || j1.getString("tripDistance").equals("null")) ? "0":j1.getString("tripDistance");
						String takeCarDate = j1.getString("takeCarDate");
						String transferPointShow = j1.getString("transferPointShow");
						JSONObject j_trans = JSON.parseObject(transferPointShow);
						String pointName = j_trans.getString("pointName");
					
						tv_triptype.setText(tripType == null ? "" : new String[]{"�ӻ�","�ͻ�","�ӻ�վ","�ͻ�վ"}[tripType.intValue()-1]);			
						date.setText(""+TimeHelper.getTimemis_to_StringTime1(takeCarDate));//TimeHelper.getTimemis_to_StringTime(order.takeCarDate.toString())
						tv_message.setVisibility(View.VISIBLE);
						tv_message.setText("������Ϣ��"+airlineCompany+"/"+flightNumber);
						
						distance.setText("Ԥ����̣�"+tripDistance+"����");System.out.println("a13");
						//holder.b_air_message.setText("������Ϣ��"+orderlist.get(position).airport+"+"+orderlist.get(position).flightNumber);	System.out.println("a14");
	//					holder.b_start_address.setText(TimeHelper.getDateTime_YM(TimeHelper.getTimemis_to_StringTime(orderlist.get(position).expectEndTime)));

						if(tripType.intValue() == 2 || tripType.intValue() == 4){
							//�ͻ�
							b_start_address.setText(""+tripAddress);
							b_end_address.setText(""+pointName);
						}else{	
							b_start_address.setText(""+pointName);
							b_end_address.setText(""+tripAddress);
						}						
						
					}else{
						
						String takeCarAddress = j1.getString("takeCarAddress");
						String returnCarAddress = j1.getString("returnCarAddress");
						String takeCarDate = j1.getString("takeCarDate");
						String outsideDistance = (j1.getString("outsideDistance")== null || j1.getString("outsideDistance").equals("null")) ? "0":j1.getString("outsideDistance");
						
						//tv_triptype.setText(new String[]{"�ӻ�","�ͻ�","�ӻ�վ","�ͻ�վ"}[tripType-1]);		
						date.setText(""+TimeHelper.getTimemis_to_StringTime1(takeCarDate));//TimeHelper.getTimemis_to_StringTime(order.takeCarDate.toString())
						tv_message.setText("");
						tv_message.setVisibility(View.GONE);
						
						distance.setText("Ԥ����̣�"+outsideDistance+"����");System.out.println("a13");
						//holder.b_air_message.setText("������Ϣ��"+orderlist.get(position).airport+"+"+orderlist.get(position).flightNumber);	System.out.println("a14");
	//					holder.b_start_address.setText(TimeHelper.getDateTime_YM(TimeHelper.getTimemis_to_StringTime(orderlist.get(position).expectEndTime)));
						b_end_address.setText(""+returnCarAddress);
						b_start_address.setText(""+takeCarAddress);
					}
					
					/*״̬*/
					JSONObject j = JSON.parseObject(message);
					int hasContract = j.getIntValue("hasContract");
					int orderState = j.getIntValue("orderState");
					String vendorId = j.getString("vendorId");
					String modelId = j.getString("modelId");
					
					String vehicleModelShow = j.getString("vehicleModelShow");
					JSONObject j_modle = JSON.parseObject(vehicleModelShow);
					String model = j_modle.getString("model");
					vendorIds[index] = vendorId;
					modelIds[index] = modelId;
					models[index] = model;
					hasContacts[index] = hasContract;
					//downMilleages[index] = clientDownMileage;
					
					System.out.println("hasContract:"+hasContract);
					System.out.println("orderState:"+hasContract);
					System.out.println("dispath:"+hasContract);
					
					return;//��������жϰ�ť״̬
					
//					if(hasContract == 0){
//						
//						c_ok.setVisibility(View.GONE);
//						c_cancle.setVisibility(View.GONE);
//						c_getcar.setVisibility(View.VISIBLE);
//						c_returncar.setVisibility(View.GONE);
//						
//						return;
//					}
//					System.out.println("�޸�bug-"+hasContract+"-"+orderState);
//					System.out.println("�޸�bug״̬"+dispath);
//					if(hasContract == 1 && orderState == 2){
//						
//						c_ok.setVisibility(View.GONE);
//						c_cancle.setVisibility(View.GONE);
//						c_getcar.setVisibility(View.VISIBLE);
//						c_returncar.setVisibility(View.GONE);
//					}else{
//						if(hasContract == 1 && orderState == 5){
//							
//							c_ok.setVisibility(View.GONE);
//							c_cancle.setVisibility(View.GONE);
//							c_getcar.setVisibility(View.GONE);
//							c_returncar.setVisibility(View.VISIBLE);
//						}else{
//							
//							if(dispath.equals("30")){
//								
//								c_ok.setVisibility(View.VISIBLE);
//								c_cancle.setVisibility(View.GONE);
//								c_getcar.setVisibility(View.GONE);
//								c_returncar.setVisibility(View.GONE);
//							}else{
//								
//								if(dispath.equals("35")){
//									c_ok.setVisibility(View.GONE);
//									c_cancle.setVisibility(View.VISIBLE);
//									c_getcar.setVisibility(View.GONE);
//									c_returncar.setVisibility(View.GONE);
//								}
//								
//							}
//							
//						}
//	
//					}
				}
				
			}

			/* 5.��������ʧ�� */
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {

			}
		});
	}
	
	private void is_HasContact(final String orderId, final int orderType) {
		
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

		String url = Public_Api.appWebSite + api;
		
		httpClient.get(url, params, new AsyncHttpResponseHandler() {

			/* ��������ɹ� */
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {

				String backData = new String(arg2);
				System.out.println("�������ɹ�:" + backData);

				JSONObject statusjobject = JSON.parseObject(backData);

				boolean status = statusjobject.getBoolean("status");
				String message = statusjobject.getString("message");

				if (status) {
					
					JSONObject j = JSON.parseObject(message);
					int hasContract = j.getIntValue("hasContract");
					
					if(hasContract == 1){
					
						IntentHelper.startActivity(context, Activity_Car_Allot.class);
					}else{
						
						ToastHelper.showToastShort(context, "�������ɺ�ͬ");
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
	
	private void reqest_car_takemilleage(final int orderType, String orderId) {
		
		String api = "";
		switch (orderType) {
			case 3://����
				api = "api/contract/"+orderId+"/contractDetail";
				break;
	
			case 4://���ͻ�
				api = "api/airportTrip/"+orderId+"/contract";
				break;
			default:
				break;
		}
		
		/* ���������½ */
		AsyncHttpClient httpClient = new AsyncHttpClient();

		RequestParams params = new RequestParams();// �����������

		String url = Public_Api.appWebSite + api;
		
		httpClient.get(url, params, new AsyncHttpResponseHandler() {

			/* ��������ɹ� */
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {

				String backData = new String(arg2);
				System.out.println("�������ɹ�:" + backData);

				JSONObject statusjobject = JSON.parseObject(backData);

				boolean status = statusjobject.getBoolean("status");
				String message = statusjobject.getString("message");

				if (status) {
					
					JSONObject j = JSON.parseObject(message);
						
//					String vehicleModelShow = j.getString("vehicleModelShow");
//					JSONObject j_modle = JSON.parseObject(vehicleModelShow);
//					String model = j_modle.getString("model");
//					c_car.setText(model+"");System.out.println("���ӵ����ͣ�"+model);

					Public_Param.returncar_Melliage = j.getIntValue("clientDownMileage");
					Public_Param.returncar_date = j.getLong("clientDownDate");
					IntentHelper.startActivity(context, Activity_Car_Return.class);
				}
				
			}

			/* 5.��������ʧ�� */
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {

			}
		});
	}
	
	private void button_status(final String orderId, final int orderType,final TextView c_ok,final TextView c_cancle,final TextView c_getcar,final TextView c_returncar) {
		
		String api = "";
		switch (orderType) {
			case 3://����
				api = "api/contract/"+orderId+"/contractDetail";
				break;
	
			case 4://���ͻ�
				api = "api/airportTrip/"+orderId+"/contract";
				break;
			default:
				break;
		}
	
		/* ���������½ */
		AsyncHttpClient httpClient = new AsyncHttpClient();

		RequestParams params = new RequestParams();// �����������

		String url = Public_Api.appWebSite + api;
		
		httpClient.get(url, params, new AsyncHttpResponseHandler() {

			/* ��������ɹ� */
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {

				String backData = new String(arg2);
				System.out.println("�������ɹ�:" + backData);

				JSONObject statusjobject = JSON.parseObject(backData);

				boolean status = statusjobject.getBoolean("status");
				String message = statusjobject.getString("message");

				if (status) {
					
					JSONObject j = JSON.parseObject(message);
					int orderState = j.getIntValue("orderState");
					
					c_getcar.setVisibility(View.GONE);
					c_ok.setVisibility(View.GONE);
					c_cancle.setVisibility(View.GONE);
					c_returncar.setVisibility(View.GONE);
					
					switch (orderState) {
						case 2:
							c_getcar.setVisibility(View.VISIBLE);
							break;
						
						case 3:
							c_ok.setVisibility(View.VISIBLE);						
							break;
													
						case 4:
							c_cancle.setVisibility(View.VISIBLE);		
							break;
							
						case 5:
							c_returncar.setVisibility(View.VISIBLE);		
							break;
						default:
							break;
					}
					
				}else{
					
					c_getcar.setVisibility(View.VISIBLE);
				}
				
			}

			/* 5.��������ʧ�� */
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {

			}
		});
	}
	
	public void flush(ArrayList<Order> orderlist){
		
		this.orderlist = orderlist;
		
		this.notifyDataSetChanged();
	}
}