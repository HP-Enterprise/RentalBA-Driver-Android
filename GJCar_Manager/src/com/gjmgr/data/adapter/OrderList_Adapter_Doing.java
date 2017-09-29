package com.gjmgr.data.adapter;


import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.gjmgr.activity.user.Activity_Car_Allot;
import com.gjmgr.activity.user.Activity_Car_Return;
import com.gjmgr.activity.user.Activity_Order_Add_List;
import com.gjmgr.activity.user.Activity_Order_Detail;
import com.gjmgr.activity.user.Activity_UpCar;
import com.gjmgr.app.R;
import com.gjmgr.data.bean.Order;
import com.gjmgr.data.data.Public_Api;
import com.gjmgr.data.data.Public_Param;
import com.gjmgr.utils.HandlerHelper;
import com.gjmgr.utils.HttpHelper;
import com.gjmgr.utils.IntentHelper;
import com.gjmgr.utils.StringHelper;
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
	private Handler handler_contract_raod;
	private boolean isRequest = false;
	
	String[] vendorIds;
	String[] modelIds;
	String[] models;
	int[] hasContacts;
	int[] tripTypes;
	//int[] downMilleages;
	
	int takecar_ordertype = 0;
	
	public OrderList_Adapter_Doing(Context context, ArrayList<Order> orderlist, boolean isDoorToDoor) {
		
		this.orderlist = orderlist;System.out.println("�޸�bug��ʼ"+orderlist.size());
		this.context = context;
		
		this.tripTypes = new int[orderlist.size()];
		this.vendorIds = new String[orderlist.size()];
		this.modelIds = new String[orderlist.size()];
		this.models = new String[orderlist.size()];
		this.hasContacts = new int[orderlist.size()];
//		this.downMilleages = new int[orderlist.size()];
		
		initHandler2();
		
		initHandler3();
		
		initHandler_Road_Contact();
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
		holder.c_road = (TextView) convertView.findViewById(R.id.c_road);
		
		holder.c_getcar.setVisibility(View.GONE);
		holder.c_ok.setVisibility(View.GONE);
		holder.c_cancle.setVisibility(View.GONE);
		holder.c_returncar.setVisibility(View.GONE);
		
		if(orderlist.get(position).orderType.intValue() == 2){//�ŵ���
			
			//����
			holder.a_order_ty.setText("�ŵ���-"+(orderlist.get(position).taskType.intValue() == 1 ? "�ͳ�" : "ȡ��"));
			holder.a_order_time.setText(""+TimeHelper.getTimemis_to_StringTime1(orderlist.get(position).expectStartTime));
			
			//�������
			holder.a_orderId.setText("������ţ�"+orderlist.get(position).orderCode+"");
			holder.a_order_distance.setVisibility(View.GONE);
			
			//������Ϣ			
			holder.b_air_message.setVisibility(View.GONE);
			
			//��ַ
			holder.b_start_address.setText((orderlist.get(position).taskType.intValue() == 1) ? StringHelper.getString(orderlist.get(position).callOutStoreName) : StringHelper.getString(orderlist.get(position).customerAddress));			
			holder.b_car.setText(orderlist.get(position).modelName+"");
			holder.b_end_address.setText((orderlist.get(position).taskType.intValue() == 1) ? StringHelper.getString(orderlist.get(position).customerAddress) : StringHelper.getString(orderlist.get(position).callOutStoreName));	
			
			//��ť
			button_status(false,orderlist.get(position).orderCode,orderlist.get(position).orderType,holder.c_ok,holder.c_cancle,holder.c_getcar,holder.c_returncar);
			
		}else{//���ͽӺͶ������
			
			holder.a_order_ty.setText(orderlist.get(position).orderTypeName);System.out.println("a11");
			
			holder.a_orderId.setText("������ţ�"+orderlist.get(position).orderCode+"");System.out.println("a12");
			//holder.a_order_distance.setText("Ԥ����̣�"+orderlist.get(position).tripDistance+"����"+"");System.out.println("a13");
			//holder.b_air_message.setText("������Ϣ��"+orderlist.get(position).airport+"+"+orderlist.get(position).flightNumber);	System.out.println("a14");
//			holder.b_start_address.setText(TimeHelper.getDateTime_YM(TimeHelper.getTimemis_to_StringTime(orderlist.get(position).expectEndTime)));
			holder.b_car.setText(orderlist.get(position).modelName+"");System.out.println("a15");
			holder.b_end_address.setText(orderlist.get(position).tripAddress+"");	System.out.println("a16");
			
			System.out.println("�޸�bug:"+orderlist.get(position).orderCode);
			System.out.println("�޸�bug-position:"+position);
			reqest_Data(orderlist.get(position).orderType, orderlist.get(position).orderCode,position,
					
					holder.a_order_ty,holder.a_order_distance,holder.b_air_message,holder.a_order_time,holder.b_start_address,holder.b_end_address,orderlist.get(position).dispatchStatus,
					
					holder.c_ok,holder.c_cancle,holder.c_getcar,holder.c_returncar);		
//			
//			button_status(orderlist.get(position).orderCode,orderlist.get(position).orderType,
//					holder.c_ok,holder.c_cancle,holder.c_getcar,holder.c_returncar);
			//reqest_Data(orderlist.get(position).orderCode, position, holder.c_ok,holder.c_cancle,holder.c_getcar,holder.c_returncar, orderlist.get(position).dispatchStatus);
			System.out.println("aaaaaaaaaaaaaaaaa11");
			//reqest_Contackt(orderlist.get(position).orderCode, holder.b_car);
			
		}
			
		holder.a_lin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {System.out.println("xxxxxxxxxxxxxxxxxx");
				
				if(orderlist.get(position).orderType.intValue() == 2){//�ŵ���
					
					Public_Param.doortodoor_upaddress = (orderlist.get(position).taskType.intValue() == 1) ? StringHelper.getString(orderlist.get(position).callOutStoreName) : StringHelper.getString(orderlist.get(position).customerAddress);						
					Public_Param.doortodoor_downaddress = (orderlist.get(position).taskType.intValue() == 1) ? StringHelper.getString(orderlist.get(position).customerAddress) : StringHelper.getString(orderlist.get(position).callOutStoreName);
					Public_Param.doortodoor_typename = "�ŵ���-"+(orderlist.get(position).taskType.intValue() == 1 ? "�ͳ�" : "ȡ��");	
					Public_Param.doortodoor_type = orderlist.get(position).taskType;
					Public_Param.doortodoor_time = orderlist.get(position).expectStartTime;
				}
			
				Public_Param.orderdeaail_orderType = orderlist.get(position).orderType.intValue();
				Public_Param.orderId_detail = orderlist.get(position).orderCode;
				
				IntentHelper.startActivity(context, Activity_Order_Detail.class);

			}
		});
		
		holder.c_road.setOnClickListener(new OnClickListener() {
			
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

				Public_Param.road_type = orderlist.get(position).orderType.intValue();
				Public_Param.road_orderId = orderlist.get(position).orderCode;
				
				new HttpHelper().initData(HttpHelper.Method_Get, context, api, null, null, handler_contract_raod, position, 1, new TypeReference<Order>() {});		
		
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
		private TextView c_road;
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
					
					Public_Param.order_up.id = orderlist.get(position).id;
					Public_Param.order_up.driverId = orderlist.get(position).driverId;
					Public_Param.order_up.orderCode = orderlist.get(position).orderCode;
					Public_Param.order_up.vehicleId = order.vehicleId;
					Public_Param.order_up.takeCarMileage =  order.takeCarMileage;					
					Public_Param.order_up.triptype = tripTypes[position];System.out.println("�ϳ�"+Public_Param.order_up.triptype);
					
					switch (takecar_ordertype) {
						case 2://�ŵ���
							
							break;
							
						case 3://����
							Public_Param.order_up.takeCarActualDate = order.driverTakeCarDate; 
							
							Public_Param.order_up.takeCarMileage =( order.driverTakeCarMileage == null ? 1 : order.driverTakeCarMileage);
							
							break;
				
						case 4://���ͻ�
							Public_Param.order_up.takeCarActualDate = order.takeCarActualDate;// == null ? order.createDate  : order.takeCarActualDate;;
							break;
							
						default:
							break;
					}
					
					IntentHelper.startActivity_StringExtras(context, Activity_UpCar.class, new String[]{"from","dispatchOrigin"}, new String[]{"up",orderlist.get(position).dispatchOrigin});

					return;
				}
				
				ToastHelper.showToastShort(context, "���ɺ�ͬ�����ִ�д˲���");
			}
		};
	}
	

	private void initHandler_Road_Contact() {
			
		handler_contract_raod = new Handler() {
				@Override
				public void handleMessage(Message msg) {
					super.handleMessage(msg);
			
					isRequest = false;
					
					if(msg.getData().getString("message").equals(HandlerHelper.Ok)){
						
						Order order = (Order)msg.obj;
						
						if(order == null){
							
							ToastHelper.showToastShort(context, "����������");						
							return;
						}
						
						if(order.vehicleId == null){
							
							ToastHelper.showToastShort(context, "�����ᳵ");						
							return;
						}
						
						Public_Param.road_vehicleId = order.vehicleId;
						Public_Param.way = Public_Param.Way_Doing;
						IntentHelper.startActivity(context, Activity_Order_Add_List.class);
						
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
		String api = "";System.out.println("req_2_orderType"+orderType);
//		switch (orderType) {
//			case 3://����
//				api = "api/driver/"+orderId+"/order";
//				break;
//	
//			case 4://���ͻ�
//				api = "api/airportTrip/"+orderId+"/order";
//				break;
//			default:
//				break;
//		}
		switch (orderType) {
		
			case 2://�ŵ���			
				api = "api/door/"+orderId+"/contract";			
				break;
			
			case 3://����
				api = "api/contract/"+orderId+"/contractDetail";
				break;
	
			case 4://���ͻ�
				api = "api/airportTrip/"+orderId+"/contract";
				break;
			default:
				break;
		}
		System.out.println("req_2_orderType_url"+api);
		/* ���������½ */
		AsyncHttpClient httpClient = new AsyncHttpClient();

		RequestParams params = new RequestParams();// �����������

		final String url = Public_Api.appWebSite + api;
		
		httpClient.get(url, params, new AsyncHttpResponseHandler() {

			/* ��������ɹ� */
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				
				if(arg2==null ||new String(arg2)==null||new String(arg2).equals("")){
					
					return;
				}
				
				String backData = new String(arg2);
				System.out.println("req_2_��ʼ");
				System.out.println("req_2_url"+url);
				System.out.println("req_2_����ֵ"+backData);
				JSONObject statusjobject = JSON.parseObject(backData);System.out.println("req_2_a1_0");
				
				boolean status = statusjobject.getBoolean("status");System.out.println("req_2_a1_1");
				String message = statusjobject.getString("message");System.out.println("req_2_a1_2");
				
				if (status) {
					/*������Ϣ*/
					JSONObject j1 = JSON.parseObject(message);
					System.out.println("req_2_a1");
					
					if(orderType == 4){//���ͻ�
						
						String vehicleId = j1.getString("vehicleId");
						boolean isVehicleIdBNull = StringHelper.isStringNull(vehicleId);
						
						String airlineCompany = j1.getString("airlineCompany");
						String flightNumber = j1.getString("flightNumber");
						Integer tripType = j1.getInteger("tripType");
						String tripAddress = j1.getString("tripAddress");
						
						String tripDistance = (j1.getString("tripDistance")== null || j1.getString("tripDistance").equals("null")) ? "0":j1.getString("tripDistance");
						String takeCarDate = j1.getString("takeCarDate");
						String transferPointShow = j1.getString("transferPointShow");
						JSONObject j_trans = JSON.parseObject(transferPointShow);
						String pointName = j_trans.getString("pointName");
					
						tripTypes[index] = (tripType == null ? 0 : tripType.intValue());
						tv_triptype.setText(tripType == null ? "" : new String[]{"�ӻ�","�ͻ�","�ӻ�վ","�ͻ�վ"}[tripType.intValue()-1]);			
						date.setText(""+TimeHelper.getTimemis_to_StringTime1(takeCarDate));//TimeHelper.getTimemis_to_StringTime(order.takeCarDate.toString())
						tv_message.setVisibility(View.VISIBLE);
						tv_message.setText("������Ϣ��"+airlineCompany+"/"+flightNumber);
						
						distance.setText("Ԥ����̣�"+tripDistance+"����");System.out.println("a13");System.out.println("req_2_a1_1");
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
						
						//��ť״̬
						button_status(isVehicleIdBNull,orderId,orderType,c_ok,c_cancle,c_getcar,c_returncar);
						
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
						b_start_address.setText(""+takeCarAddress);System.out.println("req_2_a1_2");
						
						//��ť״̬
						button_status(false,orderId,orderType,c_ok,c_cancle,c_getcar,c_returncar);
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

				}
				System.out.println("req_2_����");
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
//		switch (orderType) {
//			case 3://����
//				api = "api/driver/"+orderId+"/order";
//				break;
//	
//			case 4://���ͻ�
//				api = "api/airportTrip/"+orderId+"/order";
//				break;
//			default:
//				break;
//		}
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
				System.out.println("������ɹ�:" + backData);

				JSONObject statusjobject = JSON.parseObject(backData);

				boolean status = statusjobject.getBoolean("status");
				String message = statusjobject.getString("message");

				if (status) {
					
					IntentHelper.startActivity(context, Activity_Car_Allot.class);
//					JSONObject j = JSON.parseObject(message);
//					int hasContract = j.getIntValue("hasContract");
//					
//					if(hasContract == 1){
//					
//						IntentHelper.startActivity(context, Activity_Car_Allot.class);
//					}else{
//						
//						ToastHelper.showToastShort(context, "�������ɺ�ͬ");
//					}
					
				}else{
					
					ToastHelper.showToastShort(context, "�������ɺ�ͬ");
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
				System.out.println("������ɹ�:" + backData);

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
	
	private void button_status(final boolean isVelNull,final String orderId, final int orderType,final TextView c_ok,final TextView c_cancle,final TextView c_getcar,final TextView c_returncar) {
		
		String api = "";
		switch (orderType) {
		
			case 2://�ŵ���			
				api = "api/door/"+orderId+"/contract";			
				break;
			
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
				System.out.println("req_2_status_��ʼ");
				String backData = new String(arg2);
//				System.out.println("������ɹ�:" + backData);
				System.out.println("req_2_status_a0");
				JSONObject statusjobject = JSON.parseObject(backData);System.out.println("req_2_status_a1");

				boolean status = statusjobject.getBoolean("status");
				String message = statusjobject.getString("message");

				if (status) {
									
					c_getcar.setVisibility(View.GONE);
					c_ok.setVisibility(View.GONE);
					c_cancle.setVisibility(View.GONE);
					c_returncar.setVisibility(View.GONE);
					
					if(orderType == 2){//�ŵ���
						
						if(StringHelper.isStringNull(message)){//û���������ɺ�ͬ
							//����״̬��0����֧����1���µ������� 2:�ѵ��� 3:˾��ȡ��(�ŵ�) 4:�ͻ���ȡ�� 5:�ͻ��ѻ��� 6:���������� 7:˾��ȡ��(�ͻ�) 8:˾������ 9:ȡ������ 10:������� )
							c_ok.setVisibility(View.VISIBLE);	
							c_ok.setText("�ᳵ");
						}else{
							JSONObject j = JSON.parseObject(message);System.out.println("req_2_status_a3");
							int orderState = j.getIntValue("orderState");
							
							switch (orderState) {
								case 2:
									c_ok.setVisibility(View.VISIBLE);
									c_ok.setText("�ᳵ");
									break;
								
								case 3:
									c_cancle.setVisibility(View.VISIBLE);		
									c_cancle.setText("�ͳ�");
									break;
															
								case 6:
									c_ok.setVisibility(View.VISIBLE);
									c_ok.setText("ȡ��");
									break;
									
								case 7:
									c_cancle.setVisibility(View.VISIBLE);	
									c_cancle.setText("����");
									break;
								default:
									break;
							}
							
						}
						
					}else{//���ͽӺʹ���
						
						JSONObject j = JSON.parseObject(message);System.out.println("req_2_status_a3");
						int orderState = j.getIntValue("orderState");System.out.println("req_2_status_a4");
							
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
					}
					
					
				}else{//��û�����ɺ�ͬ
					
					if(orderType == 4){//���ͻ�
						
						if(!isVelNull){//��Ϊnull������ʾ �ϳ�
							
							c_ok.setVisibility(View.VISIBLE);
						}else{
							
							c_getcar.setVisibility(View.VISIBLE);
						}
						
					}else{
						
						c_getcar.setVisibility(View.VISIBLE);
					}
				}
				System.out.println("req_2_status_����");
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
	
	private void getcar_doortodoor(final String orderId){
		
		String api = "api/door/"+orderId+"/contract";	
		
		/* ���������½ */
		AsyncHttpClient httpClient = new AsyncHttpClient();

		RequestParams params = new RequestParams();// �����������

		String url = Public_Api.appWebSite + api;
		
		httpClient.get(url, params, new AsyncHttpResponseHandler() {

			/* ��������ɹ� */
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				System.out.println("req_2_status_��ʼ");
				String backData = new String(arg2);
//				System.out.println("������ɹ�:" + backData);
				System.out.println("req_2_status_a0");
				JSONObject statusjobject = JSON.parseObject(backData);System.out.println("req_2_status_a1");

				boolean status = statusjobject.getBoolean("status");
				String message = statusjobject.getString("message");

				if (status) {

					if(StringHelper.isStringNull(message)){//û���������ɺ�ͬ
						//����״̬��0����֧����1���µ������� 2:�ѵ��� 3:˾��ȡ��(�ŵ�) 4:�ͻ���ȡ�� 5:�ͻ��ѻ��� 6:���������� 7:˾��ȡ��(�ͻ�) 8:˾������ 9:ȡ������ 10:������� )
						ToastHelper.showToastShort(context, "���ɺ�ͬ�����ִ�д˲���");
					}else{
						
						Order order = JSON.parseObject(message, new TypeReference<Order>() {});
						
						if(order == null || order.vehicleId == null || order.vehicleId.equals("null") || order.vehicleId.equals("")){
							
							ToastHelper.showToastShort(context, "�ᳵ�����ִ�д˲���");						
							return;
						}
						
						Public_Param.order_up.id = order.id;
						Public_Param.order_up.driverId = order.driverId;
						Public_Param.order_up.orderCode = order.orderCode;
						Public_Param.order_up.vehicleId = order.vehicleId;
						Public_Param.order_up.takeCarMileage =  order.takeCarMileage;					
					
						IntentHelper.startActivity_StringExtras(context, Activity_UpCar.class, new String[]{"from","dispatchOrigin"}, new String[]{"up",order.dispatchOrigin});

					}
					
				}else{

				}
				System.out.println("req_2_status_����");
			}

			/* 5.��������ʧ�� */
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {

			}
		});
	}
}
