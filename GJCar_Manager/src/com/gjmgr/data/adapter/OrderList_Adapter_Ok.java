package com.gjmgr.data.adapter;


import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;

import com.gjmgr.activity.user.Activity_Car_Return;
import com.gjmgr.activity.user.Activity_Order_Finish_Detail;
import com.gjmgr.annotation.ContentWidget;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.gjmgr.app.R;
import com.gjmgr.data.bean.Order;
import com.gjmgr.data.data.Public_Api;
import com.gjmgr.data.data.Public_Param;
import com.gjmgr.utils.DebugHelper;
import com.gjmgr.utils.HandlerHelper;
import com.gjmgr.utils.HttpHelper;
import com.gjmgr.utils.IntentHelper;
import com.gjmgr.utils.StringHelper;
import com.gjmgr.utils.TimeHelper;
import com.gjmgr.view.widget.SingleLineZoomTextView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
/**
 * 
 * 订单列表
 * 0：待支付，1：已下单 2：租赁中 3：已还车 4：已完成 5：已取消 6：NoShow
 * 一：待支付0
 * 二：已下单123
 * 三：已完成4
 * 四：已取消5
 * @author Administrator
 * 
 */
public class OrderList_Adapter_Ok extends BaseAdapter {

	private ArrayList<Order> orderlist = new ArrayList<Order>();
	private Context context;
	private boolean isDoorToDoor;
	String[] models;
	int[] returnCar_melliages;
	/*Handler*/
	private Handler handler;

	public OrderList_Adapter_Ok(Context context, ArrayList<Order> orderlist, boolean isDoorToDoor) {

		this.orderlist = orderlist;
		this.context = context;
		this.isDoorToDoor = isDoorToDoor;
		this.models = new String[orderlist.size()];
		this.returnCar_melliages = new int[orderlist.size()];
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		
		final Holder holder;
		holder = new Holder();
		convertView = View.inflate(context, R.layout.orderlist_item_ok_test, null);
			
		holder.a_lin  = (LinearLayout) convertView.findViewById(R.id.a_lin);   
		
		holder.a_order_ty  = (TextView) convertView.findViewById(R.id.a_order_ty);	System.out.println("a2");   
		holder.a_order_time  = (TextView) convertView.findViewById(R.id.a_order_time);	System.out.println("a3");
		
		holder.a_orderId = (TextView) convertView.findViewById(R.id.a_orderId);	System.out.println("a4");
		holder.a_order_distance = (TextView) convertView.findViewById(R.id.a_order_distance);	System.out.println("a5");
		holder.b_air_message = (SingleLineZoomTextView) convertView.findViewById(R.id.b_air_message);	System.out.println("a6");
		holder.b_start_address = (TextView) convertView.findViewById(R.id.b_start_address);	System.out.println("a7");
		holder.b_car = (TextView) convertView.findViewById(R.id.b_car);	System.out.println("a8");
		holder.b_end_address = (TextView) convertView.findViewById(R.id.b_end_address);	System.out.println("a9");
		
		convertView.setTag(holder);
//		if (convertView == null) {
//			holder = new Holder();
//			convertView = View.inflate(context, R.layout.orderlist_item_ok_test, null);
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
//			convertView.setTag(holder);
//		} else {
//			holder = (Holder) convertView.getTag();
//		}
	
		//holder.a_order_ty.setText(new String[]{"送车","取车","接送机"}[orderlist.get(position).taskType.intValue()-1]);System.out.println("a11");//1接机 2送机 3接火车站 4 送火车站
		//holder.a_order_time.setText(orderlist.get(position).);//TimeHelper.getTimemis_to_StringTime(order.takeCarDate.toString())
		holder.a_order_ty.setText(orderlist.get(position).orderTypeName);
		holder.a_orderId.setText("订单编号："+orderlist.get(position).orderCode+"");System.out.println("a12");
		//holder.a_order_distance.setText("预估里程："+orderlist.get(position).tripDistance+"公里"+"");System.out.println("a13");
		//holder.b_air_message.setText("航班信息："+orderlist.get(position).airport+"+"+orderlist.get(position).flightNumber);	System.out.println("a14");
//		holder.b_start_address.setText(TimeHelper.getDateTime_YM(TimeHelper.getTimemis_to_StringTime(orderlist.get(position).expectEndTime)));
		holder.b_car.setText(orderlist.get(position).modelName+"");System.out.println("a15");
		holder.b_end_address.setText(orderlist.get(position).tripAddress+"");	System.out.println("a16");

		reqest_Data(orderlist.get(position).orderType.intValue(),orderlist.get(position).orderCode,position,
				
				holder.a_order_ty,holder.a_order_distance,holder.b_air_message,holder.a_order_time,holder.b_start_address,holder.b_end_address);
//		
//		reqest_Contackt(orderlist.get(position).orderCode, position,holder.b_car);		
		
		holder.a_lin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {System.out.println("xxxxxxxxxxxxxxxxxx");
	
				Public_Param.orderdeaail_orderType = orderlist.get(position).orderType.intValue();
				Public_Param.order = orderlist.get(position);
				Public_Param.model_ok = models[position];
				IntentHelper.startActivity(context, Activity_Order_Finish_Detail.class);
				
			}
		});
		
		return convertView;
		
	}

	public static class Holder {
	
		/*初始化控件*/
		private LinearLayout a_lin;
		
		private TextView a_order_ty;  
		private TextView a_order_time;

		private TextView a_orderId;
		private TextView a_order_distance;
		private SingleLineZoomTextView b_air_message;
		private TextView b_start_address;
		private TextView b_car;
		private TextView b_end_address;
		
	}
	
	private void reqest_Data(final int orderType,final String orderId, final int index,  final TextView tv_triptype, final TextView distance, final TextView tv_message, final TextView date,final TextView b_start_address,final TextView b_end_address) {
		System.out.println("发送请求"+orderId);
		
		String api = "";
		switch (orderType) {
			case 3://代驾
				api = "api/contract/"+orderId+"/contractDetail";
				break;
	
			case 4://接送机
				api = "api/airportTrip/"+orderId+"/contract";
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
				System.out.println("req_3_开始");

				JSONObject statusjobject = JSON.parseObject(backData);

				boolean status = statusjobject.getBoolean("status");
				String message = statusjobject.getString("message");

				if (status) {
					
					JSONObject j1 = JSON.parseObject(message);
					System.out.println("req_3_a1");
					if(orderType == 4){
						
						String airlineCompany = j1.getString("airlineCompany");
						String flightNumber = j1.getString("flightNumber");
						String clientActualDebusAddress = j1.getString("clientActualDebusAddress");
						Integer tripType = j1.getInteger("tripType");
						String tripAddress = j1.getString("tripAddress");
						
						String tripDistance = (j1.getString("tripDistance")== null || j1.getString("tripDistance").equals("null")) ? "0":j1.getString("tripDistance");
						String takeCarDate = j1.getString("takeCarDate");
						String transferPointShow = j1.getString("transferPointShow");
						JSONObject j_trans = JSON.parseObject(transferPointShow);
						String pointName = j_trans.getString("pointName");
					    
						tv_triptype.setText(tripType == null ? "" : new String[]{"接机","送机","接火车站","送火车站"}[tripType.intValue()-1]);		
						date.setText(""+TimeHelper.getTimemis_to_StringTime1(takeCarDate));//TimeHelper.getTimemis_to_StringTime(order.takeCarDate.toString())
						tv_message.setVisibility(View.VISIBLE);
						tv_message.setText("航班信息："+airlineCompany+"/"+flightNumber);
						
						distance.setText("预估里程："+tripDistance+"公里");System.out.println("a13");System.out.println("req_3_a1_1");
						//holder.b_air_message.setText("航班信息："+orderlist.get(position).airport+"+"+orderlist.get(position).flightNumber);	System.out.println("a14");
	//					holder.b_start_address.setText(TimeHelper.getDateTime_YM(TimeHelper.getTimemis_to_StringTime(orderlist.get(position).expectEndTime)));	
						
						if(tripType.intValue() == 2 || tripType.intValue() == 4){
							//送机
							b_start_address.setText(""+tripAddress);
							b_end_address.setText(clientActualDebusAddress != null ? clientActualDebusAddress:""+pointName);
						}else{
							
							b_start_address.setText(""+pointName);
							b_end_address.setText(clientActualDebusAddress != null ? clientActualDebusAddress:""+tripAddress);
						}
					}else{
						
						String takeCarAddress = j1.getString("takeCarAddress");
						String returnCarAddress = j1.getString("returnCarAddress");
						String takeCarDate = j1.getString("takeCarDate");
						String outsideDistance = (j1.getString("outsideDistance")== null || j1.getString("outsideDistance").equals("null")) ? "0":j1.getString("outsideDistance");
						String clientActualDebusAddress = j1.getString("clientActualDebusAddress");
						//tv_triptype.setText(new String[]{"接机","送机","接火车站","送火车站"}[tripType-1]);		
						date.setText(""+TimeHelper.getTimemis_to_StringTime1(takeCarDate));//TimeHelper.getTimemis_to_StringTime(order.takeCarDate.toString())
						tv_message.setText("");
						tv_message.setVisibility(View.GONE);
						
						distance.setText("预估里程："+outsideDistance+"公里");System.out.println("a13");
						//holder.b_air_message.setText("航班信息："+orderlist.get(position).airport+"+"+orderlist.get(position).flightNumber);	System.out.println("a14");
	//					holder.b_start_address.setText(TimeHelper.getDateTime_YM(TimeHelper.getTimemis_to_StringTime(orderlist.get(position).expectEndTime)));
						b_end_address.setText(clientActualDebusAddress == null ? ""+returnCarAddress : clientActualDebusAddress);
						b_start_address.setText(""+takeCarAddress);System.out.println("abok"+index);System.out.println("req_3_a1_2");
					}
				
				}
				System.out.println("req_3_结束");
			}

			/* 5.处理请求失败 */
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {

			}
		});
	}
	
	
	private void reqest_Contackt(String orderId, final int index,final TextView c_car) {
		
		String api = "api/airportTrip/"+orderId+"/contract";//已确认

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
						
					String vehicleModelShow = j.getString("vehicleModelShow");
					int melliage = j.getIntValue("clientDownMileage");
					
					JSONObject j_modle = JSON.parseObject(vehicleModelShow);
					String model = j_modle.getString("model");
					c_car.setText(model+"");System.out.println("甄子丹车型："+model);
					
					models[index] = model;
					
					returnCar_melliages[index] = melliage;

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
