package com.gjmgr.activity.user;

import java.sql.Time;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.gjmgr.annotation.ContentView;
import com.gjmgr.annotation.ContentWidget;
import com.gjmgr.app.R;
import com.gjmgr.data.bean.LocationMessage;
import com.gjmgr.data.bean.Order;
import com.gjmgr.data.data.Public_Flag;
import com.gjmgr.data.data.Public_Param;
import com.gjmgr.framwork.BaiduMapHelper;
import com.gjmgr.utils.AnnotationViewUtils;
import com.gjmgr.utils.HandlerHelper;
import com.gjmgr.utils.HttpHelper;
import com.gjmgr.utils.NetworkHelper;
import com.gjmgr.utils.StringHelper;
import com.gjmgr.utils.TimeHelper;
import com.gjmgr.utils.ToastHelper;
import com.gjmgr.utils.ValidateHelper;

import com.gjmgr.view.dialog.DateTimePickerHelper;
import com.gjmgr.view.dialog.SelectDailog;
import com.gjmgr.view.dialog.SubmitDialog;
import com.gjmgr.view.helper.TitleBarHelper;
import com.gjmgr.view.helper.ViewInitHelper;

@ContentView(R.layout.activity_upcar)
public class Activity_UpCar extends Activity{
	
	/*控件*/
	@ContentWidget(id = R.id.lin_address) LinearLayout lin_address;//上下车地址
	@ContentWidget(id = R.id.tv_up_down) TextView tv_up_down;
	@ContentWidget(id = R.id.address) EditText address;
	@ContentWidget(click = "onClick")  LinearLayout lin_location;
	
	@ContentWidget(id = R.id.tv_time) TextView tv_time;//时间
	@ContentWidget(click = "onClick") EditText time;
	
	@ContentWidget(id = R.id.tv_oil) TextView tv_oil;//油量
	@ContentWidget(click = "onClick") EditText oil;
	
	@ContentWidget(id = R.id.tv_distance) TextView tv_distance;//距离
	@ContentWidget(id = R.id.distance) EditText distance;
	
	@ContentWidget(id = R.id.lin_isreturn) LinearLayout lin_isreturn;//是否还车
	@ContentWidget(id = R.id.ok) ToggleButton ok;
	
	@ContentWidget(id = R.id.lin_name) LinearLayout lin_name;//用车人姓名
	@ContentWidget(id = R.id.name) EditText name;
	
	@ContentWidget(click = "onClick") Button submit;//提交
	
	/*参数*/
	private String from = "up";
	private String dispatchOrigin = "";
	
	/*Handler*/
	private Handler handler;
	private final static int Request = 1;
	private final static int Request_vehicleId = 2;
	private final static int Baidu_Location_Address = 3;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		AnnotationViewUtils.injectObject(this, this);
		
		/*消息*/
		initHandler();
		
		/*参数*/
		initParma();
		
		time.setText(TimeHelper.getNowTime_YMDHM());
		
		/*下车显示选择按钮*/
		if(!from.equals("up")){
			
			lin_isreturn.setVisibility(View.GONE);
		}
		
		/*标题*/
		if(dispatchOrigin.equals("1")){
			
			TitleBarHelper.Back(this, from.equals("up")?"上车回录单":"下车回录单", 0);
		}else{
			
			TitleBarHelper.Back(this, from.equals("up")?"上车回录单":"下车回录单", 0);System.out.println("a上车"+Public_Param.order_up.triptype);
//			if(!(from.equals("up"))){//!(from.equals("up")&&Public_Param.order_up.triptype != 2)
//				
//				lin_address.setVisibility(View.VISIBLE);
//				new BaiduMapHelper().startLocationClient(this, handler, Baidu_Location_Address);
//			}
			
			tv_up_down.setText(from.equals("up")?"上车地址":"下车地址");
			lin_address.setVisibility(View.VISIBLE);			
			new BaiduMapHelper().startLocationClient(this, handler, Baidu_Location_Address);
			
		}

		/*视图*/
		if(dispatchOrigin.equals("1")){
			
			ViewInitHelper.initTextViews(new TextView[]{tv_time,tv_oil,tv_distance}, from.equals("up")?new String[]{"取车时间","取车油量","取车里程"}:new String[]{"送车时间","送车油量","送车里程"});	
		}else{
			
			ViewInitHelper.initTextViews(new TextView[]{tv_time,tv_oil,tv_distance}, from.equals("up")?new String[]{"上车时间","上车油量","上车里程"}:new String[]{"下车时间","下车油量","下车里程"});
		}
		
	}
	
	public void initParma(){
		
		if(getIntent().hasExtra("from")){
			
			from = getIntent().getStringExtra("from");
		}

		if(getIntent().hasExtra("dispatchOrigin")){
			
			dispatchOrigin  = getIntent().getStringExtra("dispatchOrigin");
		}

		if(from.equals("down")&&dispatchOrigin.equals("1")){

			lin_name.setVisibility(View.VISIBLE);
		}
	}
	
	public void onClick(View view) {
		
		switch (view.getId()) {
		
			case R.id.lin_location:
				SubmitDialog.showSubmitDialog(this);
				new BaiduMapHelper().startLocationClient(this, handler, Baidu_Location_Address);
				break;
		
			case R.id.time:
				new DateTimePickerHelper().pickTime(this, time, TimeHelper.getNowTime_YMD(), "选择时间");
				break;
			
			case R.id.oil:
				new SelectDailog().select(this, "选择油量百分比", oil, StringHelper.getOils());
				break;	
			
			case R.id.submit:
				
				/*判断是否有网*/
				if(!NetworkHelper.isNetworkAvailable(this)){
					return;
				}
				
				/*判断输入是否正确*/
				if(!ValidateHelper.Validate(this, new boolean[]{!ValidationHelper.isNull("", address).equals(""),!ValidationHelper.isNull("", time).equals(""),!ValidationHelper.isNull("", oil).equals(""),!ValidationHelper.isNull("", distance).equals("")}, 
						new String[]{"地址必须填写","时间必须填写","油量必须填写","里程必须填写"})){
					return;
				}
				
				if(dispatchOrigin.equals("1")){
					
				}else{
					
					if(from.equals("up")){//上车先请求vehicleId
														
						if(Public_Param.order_up.takeCarActualDate != null && TimeHelper.isLateTakeCarTime(Public_Param.order_up.takeCarActualDate, time.getText().toString())){//先判断上车时间
							
							ToastHelper.showToastShort(Activity_UpCar.this, "时间必须晚于取车时间"+TimeHelper.getTimemis_to_StringTime(Public_Param.order_up.takeCarActualDate.toString()));
							
							return;
						}
						
						if(new Integer(distance.getText().toString().trim()).intValue() < Public_Param.order_up.takeCarMileage.intValue()){//先判断上车前的里程是否大于取车油量
							
							ToastHelper.showToastShort(Activity_UpCar.this, "输入的里程数必须大于"+Public_Param.order_up.takeCarMileage.toString());
							
							return;
						}

					}else{
					
						if(TimeHelper.isLateTakeCarTime(Public_Param.order_down.clientUpCarDate, time.getText().toString())){//先判断上车时间
							
							ToastHelper.showToastShort(Activity_UpCar.this, "时间必须晚于上车时间"+TimeHelper.getTimemis_to_StringTime(Public_Param.order_down.clientUpCarDate.toString()));
							
							return;
						}
						
						if(new Integer(distance.getText().toString().trim()).intValue() < Public_Param.order_down.clientUpMileage.intValue()){//先判断上车前的里程是否大于取车油量
							
							ToastHelper.showToastShort(Activity_UpCar.this, "输入的里程数必须大于"+Public_Param.order_down.clientUpMileage.toString());
							
							return;
						}
					}
				}
		
				if(from.equals("up")){//上车先请求vehicleId
					
					//门到门
					if(dispatchOrigin.equals("1")){
						
						/*弹出提交对话框*/
						SubmitDialog.showSubmitDialog(this);
						
						Request_door_vehicleId();//无需判断里程数
						
					}else{
						//接送机
						if(new Integer(distance.getText().toString().trim()).intValue() < Public_Param.order_up.takeCarMileage.intValue()){//先判断上车前的里程是否大于取车油量
							
							ToastHelper.showToastShort(Activity_UpCar.this, "输入的里程数必须大于"+Public_Param.order_up.takeCarMileage.toString());
							
							return;
						}
						
						/*弹出提交对话框*/
						SubmitDialog.showSubmitDialog(this);
						
						Request_vehicleId();
					}
					
				}else{
					//门到门
					if(dispatchOrigin.equals("1")){
						
						if(name.getText().toString() == null || name.getText().toString().trim().equals("")){
							
							ToastHelper.showToastShort(Activity_UpCar.this, "姓名必须填写");
							
							return;
						}
						
						if(!ValidationHelper.IsChineseName(name.getText().toString().trim())){
							
							ToastHelper.showToastShort(Activity_UpCar.this, "姓名填写不正确");
							
							return;
						}
						
						/*弹出提交对话框*/
						SubmitDialog.showSubmitDialog(this);
						
						Request_Submit("");//下车	

					}else{
						
						//接送机
						if(new Integer(distance.getText().toString().trim()).intValue() < Public_Param.order_down.clientUpMileage.intValue()){//先判断上车前的里程是否大于取车油量
							
							ToastHelper.showToastShort(Activity_UpCar.this, "输入的里程数必须大于"+Public_Param.order_down.clientUpMileage.toString());
							
							return;
						}
						
						/*弹出提交对话框*/
						SubmitDialog.showSubmitDialog(this);
						
						Request_Submit("");//下车
					}

				}

				break;
				
			default:
				break;
		}
	}
	
	@SuppressLint("HandlerLeak")
	private void initHandler() {

		handler = new Handler() {

			@SuppressLint("HandlerLeak")
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);

				switch (msg.what) {

					case Baidu_Location_Address:
						
						SubmitDialog.closeSubmitDialog();
						
						if(HandlerHelper.getString(msg).equals(HandlerHelper.Ok)){
							
							LocationMessage loc = (LocationMessage)msg.obj;
							if(loc.address != null && !loc.address.equals("")){
								
								if(loc.address.contains("中国")){
									
									loc.address = loc.address.substring(2, loc.address.length());
								}
								address.setText(""+loc.address);
							}else{
								
								if(from.equals("up")){
									
									address.setHint("请输入乘客上车地址");
								}else{
									
									address.setHint("请输入乘客下车地址");
								}
								
							}
				           	return;
						}else{
							
							if(from.equals("up")){
								
								address.setHint("请输入乘客上车地址");
							}else{
								
								address.setHint("请输入乘客下车地址");
							}
						}
						
						break;
				
					case Request:
						
						SubmitDialog.closeSubmitDialog();
						
						if(HandlerHelper.getString(msg).equals(HandlerHelper.Ok)){
							
							if(!from.equals("up") && ok.VISIBLE == View.VISIBLE && !ok.isChecked()){//接送机不还车
								
								Public_Param.isReturnCarOK = true;
							}
							
							ToastHelper.showToastShort(Activity_UpCar.this, "回录成功");
							finish();
				           	return;
						}
						
						ToastHelper.showToastShort(Activity_UpCar.this, "回录失败");
																								
						break;

					case Request_vehicleId:
						
						if(HandlerHelper.getString(msg).equals(HandlerHelper.Ok)){
							
							Order order = (Order)msg.obj;
							
							Request_Submit(order.vehicleId);
				           	return;
						}
						
						SubmitDialog.closeSubmitDialog();
						ToastHelper.showToastShort(Activity_UpCar.this, "回录失败");
						
					default:
						break;
				}
			}
		};
	}
	
	/** 提交信息  */
	private void  Request_Submit(String vehicleId){

		/*参数*/
		JSONObject jsonObject = new JSONObject(); 
		String api = "";
		
		if(from.equals("up")){
	
			jsonObject.put("id", Public_Param.order_up.id);//调度任务id
			jsonObject.put("driverId", Public_Param.order_up.driverId);//调度司机id
			jsonObject.put("vehicleId",  Public_Param.order_up.vehicleId);Public_Flag.flag(true);//调度车辆id
			jsonObject.put("realStartTime",  Long.parseLong(TimeHelper.getSearchTime_Mis(time.getText().toString())));//上车时间
			
			api = "api/dispatch/task-doing?getOnFuel="+oil.getText().toString()+"&getOnMileage="+distance.getText().toString();
			
//			if(Public_Param.order_up.triptype == 2){
//				
//				api = "api/dispatch/task-doing?onAddress="+address.getText().toString()+"&getOnFuel="+oil.getText().toString()+"&getOnMileage="+distance.getText().toString();
//			
//			}
			api = "api/dispatch/task-doing?onAddress="+address.getText().toString()+"&getOnFuel="+oil.getText().toString()+"&getOnMileage="+distance.getText().toString();
			
			System.out.println("vehicleId-------------------"+vehicleId);
			System.out.println("myapi-------------------"+api);
		}else{
			System.out.println("xxxxxxxxxxxxxx6");
			jsonObject.put("id", Public_Param.order_down.id);//调度任务id
			jsonObject.put("driverId", Public_Param.order_down.driverId);//调度司机id
			jsonObject.put("realEndTime",  Long.parseLong(TimeHelper.getSearchTime_Mis(time.getText().toString())));//下车时间
			jsonObject.put("clientActualDebusAddress",  address.getText().toString());//下车时间
			
			if(dispatchOrigin.equals("1")){
				jsonObject.put("recipientName",  name.getText().toString().trim());//接收人姓名
			}
										
			api = "api/dispatch/task-finish?getOffFuel="+oil.getText().toString()+"&getOffMileage="+distance.getText().toString()+"&returnCar="+ok.isChecked();
		}

		/*提交*/
		new HttpHelper().initData(HttpHelper.Method_Put, this, api, jsonObject, null, handler, Request, 2, null);
		
	}
	
	/**请求订单Id*/
	private void Request_vehicleId(){
			
		/*加载费用*/
		String api = "";
		switch (Public_Param.upcar_orderType) {
			case 3://代驾
				api = "api/driver/"+Public_Param.order_up.orderCode+"/order";
				break;
	
			case 4://接送机
				api = "api/airportTrip/"+Public_Param.order_up.orderCode+"/order";
				break;
			default:
				break;
		};
		System.out.println("vvv----1");
		new HttpHelper().initData(HttpHelper.Method_Get, this, api, null, null, handler, Request_vehicleId, 1, new TypeReference<Order>() {});		
		
	}
	
	/**请求订单Id*/
	private void Request_door_vehicleId(){
			
		/*加载费用*/

		String api = "api/door/"+Public_Param.order_up.orderCode+"/order";
		
		new HttpHelper().initData(HttpHelper.Method_Get, this, api, null, null, handler, Request_vehicleId, 1, new TypeReference<Order>() {});		
		
	}
}
