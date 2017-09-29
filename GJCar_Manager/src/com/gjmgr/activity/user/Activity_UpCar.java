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
	
	/*�ؼ�*/
	@ContentWidget(id = R.id.lin_address) LinearLayout lin_address;//���³���ַ
	@ContentWidget(id = R.id.tv_up_down) TextView tv_up_down;
	@ContentWidget(id = R.id.address) EditText address;
	@ContentWidget(click = "onClick")  LinearLayout lin_location;
	
	@ContentWidget(id = R.id.tv_time) TextView tv_time;//ʱ��
	@ContentWidget(click = "onClick") EditText time;
	
	@ContentWidget(id = R.id.tv_oil) TextView tv_oil;//����
	@ContentWidget(click = "onClick") EditText oil;
	
	@ContentWidget(id = R.id.tv_distance) TextView tv_distance;//����
	@ContentWidget(id = R.id.distance) EditText distance;
	
	@ContentWidget(id = R.id.lin_isreturn) LinearLayout lin_isreturn;//�Ƿ񻹳�
	@ContentWidget(id = R.id.ok) ToggleButton ok;
	
	@ContentWidget(id = R.id.lin_name) LinearLayout lin_name;//�ó�������
	@ContentWidget(id = R.id.name) EditText name;
	
	@ContentWidget(click = "onClick") Button submit;//�ύ
	
	/*����*/
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
		
		/*��Ϣ*/
		initHandler();
		
		/*����*/
		initParma();
		
		time.setText(TimeHelper.getNowTime_YMDHM());
		
		/*�³���ʾѡ��ť*/
		if(!from.equals("up")){
			
			lin_isreturn.setVisibility(View.GONE);
		}
		
		/*����*/
		if(dispatchOrigin.equals("1")){
			
			TitleBarHelper.Back(this, from.equals("up")?"�ϳ���¼��":"�³���¼��", 0);
		}else{
			
			TitleBarHelper.Back(this, from.equals("up")?"�ϳ���¼��":"�³���¼��", 0);System.out.println("a�ϳ�"+Public_Param.order_up.triptype);
//			if(!(from.equals("up"))){//!(from.equals("up")&&Public_Param.order_up.triptype != 2)
//				
//				lin_address.setVisibility(View.VISIBLE);
//				new BaiduMapHelper().startLocationClient(this, handler, Baidu_Location_Address);
//			}
			
			tv_up_down.setText(from.equals("up")?"�ϳ���ַ":"�³���ַ");
			lin_address.setVisibility(View.VISIBLE);			
			new BaiduMapHelper().startLocationClient(this, handler, Baidu_Location_Address);
			
		}

		/*��ͼ*/
		if(dispatchOrigin.equals("1")){
			
			ViewInitHelper.initTextViews(new TextView[]{tv_time,tv_oil,tv_distance}, from.equals("up")?new String[]{"ȡ��ʱ��","ȡ������","ȡ�����"}:new String[]{"�ͳ�ʱ��","�ͳ�����","�ͳ����"});	
		}else{
			
			ViewInitHelper.initTextViews(new TextView[]{tv_time,tv_oil,tv_distance}, from.equals("up")?new String[]{"�ϳ�ʱ��","�ϳ�����","�ϳ����"}:new String[]{"�³�ʱ��","�³�����","�³����"});
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
				if(!ValidateHelper.Validate(this, new boolean[]{!ValidationHelper.isNull("", address).equals(""),!ValidationHelper.isNull("", time).equals(""),!ValidationHelper.isNull("", oil).equals(""),!ValidationHelper.isNull("", distance).equals("")}, 
						new String[]{"��ַ������д","ʱ�������д","����������д","��̱�����д"})){
					return;
				}
				
				if(dispatchOrigin.equals("1")){
					
				}else{
					
					if(from.equals("up")){//�ϳ�������vehicleId
														
						if(Public_Param.order_up.takeCarActualDate != null && TimeHelper.isLateTakeCarTime(Public_Param.order_up.takeCarActualDate, time.getText().toString())){//���ж��ϳ�ʱ��
							
							ToastHelper.showToastShort(Activity_UpCar.this, "ʱ���������ȡ��ʱ��"+TimeHelper.getTimemis_to_StringTime(Public_Param.order_up.takeCarActualDate.toString()));
							
							return;
						}
						
						if(new Integer(distance.getText().toString().trim()).intValue() < Public_Param.order_up.takeCarMileage.intValue()){//���ж��ϳ�ǰ������Ƿ����ȡ������
							
							ToastHelper.showToastShort(Activity_UpCar.this, "�����������������"+Public_Param.order_up.takeCarMileage.toString());
							
							return;
						}

					}else{
					
						if(TimeHelper.isLateTakeCarTime(Public_Param.order_down.clientUpCarDate, time.getText().toString())){//���ж��ϳ�ʱ��
							
							ToastHelper.showToastShort(Activity_UpCar.this, "ʱ����������ϳ�ʱ��"+TimeHelper.getTimemis_to_StringTime(Public_Param.order_down.clientUpCarDate.toString()));
							
							return;
						}
						
						if(new Integer(distance.getText().toString().trim()).intValue() < Public_Param.order_down.clientUpMileage.intValue()){//���ж��ϳ�ǰ������Ƿ����ȡ������
							
							ToastHelper.showToastShort(Activity_UpCar.this, "�����������������"+Public_Param.order_down.clientUpMileage.toString());
							
							return;
						}
					}
				}
		
				if(from.equals("up")){//�ϳ�������vehicleId
					
					//�ŵ���
					if(dispatchOrigin.equals("1")){
						
						/*�����ύ�Ի���*/
						SubmitDialog.showSubmitDialog(this);
						
						Request_door_vehicleId();//�����ж������
						
					}else{
						//���ͻ�
						if(new Integer(distance.getText().toString().trim()).intValue() < Public_Param.order_up.takeCarMileage.intValue()){//���ж��ϳ�ǰ������Ƿ����ȡ������
							
							ToastHelper.showToastShort(Activity_UpCar.this, "�����������������"+Public_Param.order_up.takeCarMileage.toString());
							
							return;
						}
						
						/*�����ύ�Ի���*/
						SubmitDialog.showSubmitDialog(this);
						
						Request_vehicleId();
					}
					
				}else{
					//�ŵ���
					if(dispatchOrigin.equals("1")){
						
						if(name.getText().toString() == null || name.getText().toString().trim().equals("")){
							
							ToastHelper.showToastShort(Activity_UpCar.this, "����������д");
							
							return;
						}
						
						if(!ValidationHelper.IsChineseName(name.getText().toString().trim())){
							
							ToastHelper.showToastShort(Activity_UpCar.this, "������д����ȷ");
							
							return;
						}
						
						/*�����ύ�Ի���*/
						SubmitDialog.showSubmitDialog(this);
						
						Request_Submit("");//�³�	

					}else{
						
						//���ͻ�
						if(new Integer(distance.getText().toString().trim()).intValue() < Public_Param.order_down.clientUpMileage.intValue()){//���ж��ϳ�ǰ������Ƿ����ȡ������
							
							ToastHelper.showToastShort(Activity_UpCar.this, "�����������������"+Public_Param.order_down.clientUpMileage.toString());
							
							return;
						}
						
						/*�����ύ�Ի���*/
						SubmitDialog.showSubmitDialog(this);
						
						Request_Submit("");//�³�
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
								
								if(loc.address.contains("�й�")){
									
									loc.address = loc.address.substring(2, loc.address.length());
								}
								address.setText(""+loc.address);
							}else{
								
								if(from.equals("up")){
									
									address.setHint("������˿��ϳ���ַ");
								}else{
									
									address.setHint("������˿��³���ַ");
								}
								
							}
				           	return;
						}else{
							
							if(from.equals("up")){
								
								address.setHint("������˿��ϳ���ַ");
							}else{
								
								address.setHint("������˿��³���ַ");
							}
						}
						
						break;
				
					case Request:
						
						SubmitDialog.closeSubmitDialog();
						
						if(HandlerHelper.getString(msg).equals(HandlerHelper.Ok)){
							
							if(!from.equals("up") && ok.VISIBLE == View.VISIBLE && !ok.isChecked()){//���ͻ�������
								
								Public_Param.isReturnCarOK = true;
							}
							
							ToastHelper.showToastShort(Activity_UpCar.this, "��¼�ɹ�");
							finish();
				           	return;
						}
						
						ToastHelper.showToastShort(Activity_UpCar.this, "��¼ʧ��");
																								
						break;

					case Request_vehicleId:
						
						if(HandlerHelper.getString(msg).equals(HandlerHelper.Ok)){
							
							Order order = (Order)msg.obj;
							
							Request_Submit(order.vehicleId);
				           	return;
						}
						
						SubmitDialog.closeSubmitDialog();
						ToastHelper.showToastShort(Activity_UpCar.this, "��¼ʧ��");
						
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
			jsonObject.put("id", Public_Param.order_down.id);//��������id
			jsonObject.put("driverId", Public_Param.order_down.driverId);//����˾��id
			jsonObject.put("realEndTime",  Long.parseLong(TimeHelper.getSearchTime_Mis(time.getText().toString())));//�³�ʱ��
			jsonObject.put("clientActualDebusAddress",  address.getText().toString());//�³�ʱ��
			
			if(dispatchOrigin.equals("1")){
				jsonObject.put("recipientName",  name.getText().toString().trim());//����������
			}
										
			api = "api/dispatch/task-finish?getOffFuel="+oil.getText().toString()+"&getOffMileage="+distance.getText().toString()+"&returnCar="+ok.isChecked();
		}

		/*�ύ*/
		new HttpHelper().initData(HttpHelper.Method_Put, this, api, jsonObject, null, handler, Request, 2, null);
		
	}
	
	/**���󶩵�Id*/
	private void Request_vehicleId(){
			
		/*���ط���*/
		String api = "";
		switch (Public_Param.upcar_orderType) {
			case 3://����
				api = "api/driver/"+Public_Param.order_up.orderCode+"/order";
				break;
	
			case 4://���ͻ�
				api = "api/airportTrip/"+Public_Param.order_up.orderCode+"/order";
				break;
			default:
				break;
		};
		System.out.println("vvv----1");
		new HttpHelper().initData(HttpHelper.Method_Get, this, api, null, null, handler, Request_vehicleId, 1, new TypeReference<Order>() {});		
		
	}
	
	/**���󶩵�Id*/
	private void Request_door_vehicleId(){
			
		/*���ط���*/

		String api = "api/door/"+Public_Param.order_up.orderCode+"/order";
		
		new HttpHelper().initData(HttpHelper.Method_Get, this, api, null, null, handler, Request_vehicleId, 1, new TypeReference<Order>() {});		
		
	}
}
