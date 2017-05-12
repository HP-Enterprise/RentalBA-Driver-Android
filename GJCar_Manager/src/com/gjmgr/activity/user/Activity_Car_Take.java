package com.gjmgr.activity.user;



import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.gjmgr.annotation.ContentView;
import com.gjmgr.annotation.ContentWidget;
import com.gjmgr.app.R;
import com.gjmgr.data.bean.Order;
import com.gjmgr.data.bean.Order_Up;
import com.gjmgr.data.data.Public_Flag;
import com.gjmgr.data.data.Public_Param;
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
	
@ContentView(R.layout.activity_car_take)
public class Activity_Car_Take extends Activity{

	@ContentWidget(id = R.id.plate) EditText plate;
	@ContentWidget(click = "onClick") EditText time;
	@ContentWidget(click = "onClick") EditText oil;
	@ContentWidget(id = R.id.distance) EditText distance;
	
	@ContentWidget(click = "onClick") Button submit;
	
	/*����*/
	private String from = "up";
	private String dispatchOrigin = "";
	
	/*Handler*/
	private Handler handler;
	private final static int Request = 1;
	private final static int Request_vehicleId = 2;
	private final static int Request_Submit = 3;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		AnnotationViewUtils.injectObject(this, this);
		
		TitleBarHelper.Back(this, "��ͬ�ᳵ", 0);

		/*��Ϣ*/
		initHandler();
		
		plate.setText(Public_Param.plate);
	}
	
	public void onClick(View view) {
		
		switch (view.getId()) {
		
			case R.id.time:
				new DateTimePickerHelper().pickTime(this, time, TimeHelper.getNowTime_YMD(), "ѡ��ʱ��");
				break;
			
			case R.id.oil:
				new SelectDailog().select(this, "ѡ�������ٷֱ�", oil, StringHelper.getOils());
				break;	
			
			case R.id.submit:
				
				/*�ж��Ƿ�����*/
				if(!NetworkHelper.isNetworkAvailable(this)){
					return;
				}
				
				/*�ж������Ƿ���ȷ*/
				if(!ValidateHelper.Validate(this, new boolean[]{!ValidationHelper.isNull("", time).equals(""),!ValidationHelper.isNull("", oil).equals(""),!ValidationHelper.isNull("", distance).equals("")}, 
						new String[]{"ʱ�������д","����������д","��̱�����д"})){
					return;
				}
				
				if(Public_Param.takecar_Melliage > new Integer(distance.getText().toString()).intValue()){
					
					ToastHelper.showToastShort(this, "ȡ���������Ϊ"+Public_Param.takecar_Melliage+"����");
					
					return;
				}
				
				/*�����ύ�Ի���*/
				SubmitDialog.showSubmitDialog(this);
				
				Request_Submit();
					
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

					case Request_Submit:
						
						SubmitDialog.closeSubmitDialog();
						
						if(HandlerHelper.getString(msg).equals(HandlerHelper.Ok)){
							
							ToastHelper.showToastShort(Activity_Car_Take.this, "�ᳵ�ɹ�");
							finish();
				           	return;
						}
						
						ToastHelper.showToastShort(Activity_Car_Take.this, "�ᳵʧ��");
																								
						break;

					case Request_vehicleId:
						
						if(HandlerHelper.getString(msg).equals(HandlerHelper.Ok)){
							
							Order order = (Order)msg.obj;
							
							Request_Submit(order.vehicleId);
				           	return;
						}
						
						SubmitDialog.closeSubmitDialog();
						ToastHelper.showToastShort(Activity_Car_Take.this, "��¼ʧ��");
						
					default:
						break;
				}
			}
		};
	}
	
	/** �ύ��Ϣ  */
	private void  Request_Submit(String vehicleId){

		/*����*/
		JSONObject jsonObject = new JSONObject(); 
		String api = "";
		
		if(from.equals("up")){
	
			jsonObject.put("id", Public_Param.order_up.id);//��������id
			jsonObject.put("driverId", Public_Param.order_up.driverId);//����˾��id
			jsonObject.put("vehicleId",  Public_Param.order_up.vehicleId);Public_Flag.flag(true);//���ȳ���id
			jsonObject.put("realStartTime",  Long.parseLong(TimeHelper.getSearchTime_Mis(time.getText().toString())));//�ϳ�ʱ��
//			jsonObject.put("getOnFuel",  oil.getText().toString());//�ϳ�����
//			jsonObject.put("getOnMileage", distance.getText().toString());//�ϳ����
			System.out.println("vehicleId-------------------"+vehicleId);
			api = "api/dispatch/task-doing?getOnFuel="+oil.getText().toString()+"&getOnMileage="+distance.getText().toString();
			
		}else{
			System.out.println("xxxxxxxxxxxxxx6");
			jsonObject.put("id", Public_Param.order_down.id);//��������id
			jsonObject.put("driverId", Public_Param.order_down.driverId);//����˾��id
			jsonObject.put("realEndTime",  Long.parseLong(TimeHelper.getSearchTime_Mis(time.getText().toString())));//�³�ʱ��
			
			if(dispatchOrigin.equals("1")){
				//jsonObject.put("recipientName",  name.getText().toString().trim());//����������
			}
			
			api = "api/dispatch/task-finish?getOffFuel="+oil.getText().toString()+"&getOffMileage="+distance.getText().toString();
		}
		
		/*�ύ*/
		new HttpHelper().initData(HttpHelper.Method_Put, this, api, jsonObject, null, handler, Request, 2, null);
		
	}
	
	/*���󶩵�Id*/
	private void Request_vehicleId(){
			
		/*���ط���*/

		String api = "api/airportTrip/"+Public_Param.order_up.orderCode+"/order";
		
		new HttpHelper().initData(HttpHelper.Method_Get, this, api, null, null, handler, Request_vehicleId, 1, new TypeReference<Order>() {});		
		
	}
	
	/*���󶩵�Id*/
	private void Request_door_vehicleId(){
			
		/*���ط���*/

		String api = "api/door/"+Public_Param.order_up.orderCode+"/order";
		
		new HttpHelper().initData(HttpHelper.Method_Get, this, api, null, null, handler, Request_vehicleId, 1, new TypeReference<Order>() {});		
		
	}
	
	/** �ύ��Ϣ  */
	private void  Request_Submit(){
		
		/*��ȡ����*/
		String orderId = Public_Param.orderId;
		String modelId = Public_Param.modelId;
		String takeCarActualDate = TimeHelper.getSearchTime_Mis(time.getText().toString());;
		String takeCarFuel = oil.getText().toString();
		String takeCarMileage = distance.getText().toString();
		String vehicleId = Public_Param.vehicleId;
		
		/*api*/
		String api = "";
		switch (Public_Param.takecar_orderType) {
			case 3://����
				api = "api/take/"+orderId+"/driverContract?modelId="+modelId+"&takeCarActualDate="+takeCarActualDate+"&takeCarFuel="+takeCarFuel+"&takeCarMileage="+takeCarMileage+"&vehicleId="+vehicleId;				
				break;
	
			case 4://���ͻ�
				api = "api/take/"+orderId+"/airContract?modelId="+modelId+"&takeCarActualDate="+takeCarActualDate+"&takeCarFuel="+takeCarFuel+"&takeCarMileage="+takeCarMileage+"&vehicleId="+vehicleId;				
				break;
			default:
				break;
		}

		/*�ύ*/
		new HttpHelper().initData(HttpHelper.Method_Put, this, api, null, null, handler, Request_Submit, 2, null);
		
	}
}
