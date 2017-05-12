package com.gjmgr.activity.user;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.fastjson.TypeReference;
import com.gjmgr.annotation.ContentView;
import com.gjmgr.annotation.ContentWidget;
import com.gjmgr.app.R;
import com.gjmgr.data.bean.Order;
import com.gjmgr.data.data.Public_Param;
import com.gjmgr.data.helper.JSONHelper;
import com.gjmgr.data.helper.Loginhelper;
import com.gjmgr.utils.AnnotationViewUtils;
import com.gjmgr.utils.HandlerHelper;
import com.gjmgr.utils.HttpHelper;
import com.gjmgr.utils.NetworkHelper;
import com.gjmgr.utils.StringHelper;
import com.gjmgr.utils.TimeHelper;
import com.gjmgr.utils.ToastHelper;
import com.gjmgr.view.dialog.SubmitDialog;
import com.gjmgr.view.helper.TitleBarHelper;
import com.gjmgr.view.helper.ViewInitHelper;

@ContentView(R.layout.activity_order_finish_detail)
public class Activity_Order_Finish_Detail extends Activity{

	@ContentWidget(id = R.id.a_orderId) TextView a_orderId;
	
	@ContentWidget(id = R.id.b_start_date) TextView b_start_date;
	@ContentWidget(id = R.id.b_start_time) TextView b_start_time;
	@ContentWidget(id = R.id.b_start_address) TextView b_start_address;

	@ContentWidget(id = R.id.b_car) TextView b_car;

	@ContentWidget(id = R.id.b_end_date) TextView b_end_date;
	@ContentWidget(id = R.id.b_end_time) TextView b_end_time;
	@ContentWidget(id = R.id.b_end_address) TextView b_end_address;

	/*油量*/
	
	@ContentWidget(id = R.id.c_takecar_oil) TextView c_takecar_oil;
	@ContentWidget(id = R.id.c_start_oil) TextView c_start_oil;
	@ContentWidget(id = R.id.c_end_oil) TextView c_end_oil;
	@ContentWidget(id = R.id.c_return_oil) TextView c_return_oil;
	
	
	@ContentWidget(id = R.id.d_takecar_distance) TextView d_takecar_distance;
	@ContentWidget(id = R.id.d_start_distance) TextView d_start_distance;
	@ContentWidget(id = R.id.d_end_distance) TextView d_end_distance;
	@ContentWidget(id = R.id.d_returncar_distance) TextView d_returncar_distance;
	
	/*Handler*/
	private Handler handler;
	private final static int Request_Contact = 100;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		AnnotationViewUtils.injectObject(this, this);
		
		TitleBarHelper.Back(this, "订单详情", 0);
		
		initView();
		
		initHandler();
		
		initData();
	}

	private void initView() {
		
		/*订单信息*/
		TextView[] tvs = new TextView[]{a_orderId, 
				b_start_date,b_start_time,b_start_address, 
				b_car, 
				b_end_date,b_end_time,b_end_address};
				
		String[] values = new String[]{Public_Param.order.orderCode,
				TimeHelper.getDateTime_YM(TimeHelper.getTimemis_to_StringTime(Public_Param.order.realStartTime.toString())),
				TimeHelper.getWeekTime(TimeHelper.getTimemis_to_StringTime(Public_Param.order.realStartTime.toString())),
				Public_Param.order.customerAddress,
				
				Public_Param.model_ok == null ? Public_Param.order.modelName : Public_Param.model_ok,
						
				TimeHelper.getDateTime_YM(TimeHelper.getTimemis_to_StringTime(Public_Param.order.realEndTime.toString())),
				TimeHelper.getWeekTime(TimeHelper.getTimemis_to_StringTime(Public_Param.order.realEndTime.toString())),
				Public_Param.order.customerAddress};
				
		ViewInitHelper.initTextViews(tvs, values);
		
		/*油量里程信息*/
		if(Public_Param.order.dispatchOrigin.equals("1")){
			
			//ViewInitHelper.initTextViews(new TextView[]{tv_start_oil,tv_end_oil,tv_start_distance,tv_end_distance}, new String[]{"取车油量","还车油量","取车里程","还车里程"});
		}else{
			
			//默认
		}
	}
	
	private void initData(){
		
		
		String api = "";
		
		switch (Public_Param.orderdeaail_orderType) {
			case 2://门到门
				api = "api/door/"+Public_Param.order.orderCode+"/contract";
				break;
		
			case 3://代驾
				api = "api/contract/"+Public_Param.order.orderCode+"/contractDetail";
				break;
	
			case 4://接送机
				api = "api/airportTrip/"+Public_Param.order.orderCode+"/contract";
				break;
			default:
				break;
		}

		new HttpHelper().initData(HttpHelper.Method_Get, this, api, null, null, handler, Request_Contact, 1, new TypeReference<Order>() {});		
		
	}
	
	private void initHandler() {

		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				
				if(msg.getData().getString("message").equals(HandlerHelper.Ok)){
					
					Order order = (Order)msg.obj;
					
					if(order != null ){System.out.println("clientUpFuel"+order.clientUpFuel);
					
						if(Public_Param.order.dispatchOrigin.equals("1")){
							
							if(Public_Param.order.taskType.intValue() == 1){//送车：司机把车送给用户 
								
								//时间和地址
								if(order.takeCarActualDate == null){
									System.out.println("xxxxxxxxxxxxxxxx");
								}
					
								b_start_date.setText(TimeHelper.getDateTime_YM(TimeHelper.getTimemis_to_StringTime(order.takeCarActualDate.toString())));
								b_start_time.setText(TimeHelper.getWeekTime(TimeHelper.getTimemis_to_StringTime(order.takeCarActualDate.toString())));
								b_end_date.setText(TimeHelper.getDateTime_YM(TimeHelper.getTimemis_to_StringTime(order.clientTakeCarDate.toString())));	
								b_end_time.setText(TimeHelper.getWeekTime(TimeHelper.getTimemis_to_StringTime(order.clientTakeCarDate.toString())));
								b_start_address.setText(order.takeCarAddress);
								b_end_address.setText(order.clientTakeCarAddress);
								
								//油量和里程
								c_start_oil.setText(order.takeCarFuel);
								c_end_oil.setText(order.clientTakeCarFuel);
								d_start_distance.setText(order.takeCarMileage.toString()+"公里");
								d_end_distance.setText(order.clientTakeCarMileage.toString()+"公里");	
								
							}else{//取车：司机从用户取车
									
								//时间
								b_start_date.setText(TimeHelper.getDateTime_YM(TimeHelper.getTimemis_to_StringTime(order.clientReturnCarDate.toString())));
								b_start_time.setText(TimeHelper.getWeekTime(TimeHelper.getTimemis_to_StringTime(order.clientReturnCarDate.toString())));
								b_end_date.setText(TimeHelper.getDateTime_YM(TimeHelper.getTimemis_to_StringTime(order.returnCarActualDate.toString())));	
								b_end_time.setText(TimeHelper.getWeekTime(TimeHelper.getTimemis_to_StringTime(order.returnCarActualDate.toString())));
								b_start_address.setText(order.clientReturnCarAddress);
								b_end_address.setText(order.returnCarAddress);
								
								//油量和里程
								c_start_oil.setText(order.clientReturnCarFuel);
								c_end_oil.setText(order.returnCarFuel);
								d_start_distance.setText(order.clientReturnCarMileage.toString()+"公里");
								d_end_distance.setText(order.returnCarMileage.toString()+"公里");
							}
							
						}else{
							
							/*更新地址*/
							switch (Public_Param.orderdeaail_orderType) {
							
								case 2://门到门
									
									break;
							
								case 3://代驾
									b_start_address.setText(order.takeCarAddress);
									b_end_address.setText(order.clientActualDebusAddress == null ? order.returnCarAddress : order.clientActualDebusAddress);
									c_takecar_oil.setText(StringHelper.getString(order.driverTakeCarFuel));
//									c_return_oil.setText(StringHelper.getString(order.returnCarFuelString));
//									c_takecar_oil.setText(StringHelper.getString(order.returnCarFuel));
									c_return_oil.setText(StringHelper.getString(order.returnCarFuel));
									
									d_takecar_distance.setText(order.driverTakeCarMileage == null ? order.takeCarMileage.toString()+"公里" : order.driverTakeCarMileage.toString()+"公里");
									
									break;
					
								case 4://接送机	
									c_takecar_oil.setText(order.takeCarFuel == null ? "" : order.takeCarFuel);
									c_return_oil.setText(order.returnCarFuel == null ? "" : order.returnCarFuel);
									
									d_takecar_distance.setText(order.takeCarMileage == null ? "" : order.takeCarMileage.toString()+"公里");
									
									if(order.tripType.intValue() == 1 || order.tripType.intValue() == 3){
										
										b_start_address.setText(order.transferPointShow.pointName);
										//b_end_address.setText(order.tripAddress);System.out.println("cccccccc"+order.tripAddress);  
										b_end_address.setText(order.clientActualDebusAddress == null ? order.tripAddress : order.clientActualDebusAddress);System.out.println("cccccccc"+order.tripAddress);  
									}else{
										
										//b_start_address.setText(order.tripAddress);
										b_start_address.setText(""+order.tripAddress);
										b_end_address.setText(order.clientActualDebusAddress == null ? order.tripAddress : order.clientActualDebusAddress);
									}
									break;
								default:
									break;
							}
														
							c_start_oil.setText(order.clientUpFuel);
							c_end_oil.setText(order.clientDownFuel);							
														
							d_start_distance.setText(order.clientUpMileage.toString()+"公里");
							d_end_distance.setText(order.clientDownMileage.toString()+"公里");
							d_returncar_distance.setText(order.returnCarMileage == null ? "" : order.returnCarMileage.toString()+"公里");
														
						}
						
					}
				}	
								
			}
		};
	}
}
