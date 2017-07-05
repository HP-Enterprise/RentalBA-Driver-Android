package com.gjmgr.activity.user;


import com.alibaba.fastjson.TypeReference;
import com.gjmgr.annotation.ContentView;
import com.gjmgr.annotation.ContentWidget;
import com.gjmgr.app.R;
import com.gjmgr.data.bean.Order_Detail;
import com.gjmgr.data.data.Public_Api;
import com.gjmgr.data.data.Public_Param;
import com.gjmgr.utils.AnnotationViewUtils;
import com.gjmgr.utils.HandlerHelper;
import com.gjmgr.utils.HttpHelper;
import com.gjmgr.utils.ImageLoaderHelper;
import com.gjmgr.utils.StringHelper;
import com.gjmgr.utils.TimeHelper;
import com.gjmgr.view.helper.LoadAnimateHelper;
import com.gjmgr.view.helper.TitleBarHelper;
import com.gjmgr.view.helper.ViewInitHelper;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

@ContentView(R.layout.activity_order_detail)
public class Activity_Order_Detail extends Activity{
	
	/*�ؼ�*/
	@ContentWidget(id = R.id.a_1) TextView a_1;
	@ContentWidget(id = R.id.a_2) TextView a_2;
	
	@ContentWidget(id = R.id.b_1) ImageView b_1;
	@ContentWidget(id = R.id.b_2) TextView b_2;
	@ContentWidget(id = R.id.b_3) TextView b_3;
	
	@ContentWidget(id = R.id.c_1) TextView c_1;
	@ContentWidget(id = R.id.c_2) TextView c_2;
	@ContentWidget(id = R.id.c_3) TextView c_3;
	
	@ContentWidget(id = R.id.d_1) TextView d_1;
	@ContentWidget(id = R.id.d_2) TextView d_2;
	@ContentWidget(id = R.id.d_3) TextView d_3;
	@ContentWidget(id = R.id.d_4) TextView d_4;
	@ContentWidget(id = R.id.d_5) TextView d_5;	
	@ContentWidget(id = R.id.d_6) TextView d_6;
	@ContentWidget(id = R.id.d_7) TextView d_7;
	
	@ContentWidget(id = R.id.e_1) TextView e_1;
	@ContentWidget(id = R.id.e_2) TextView e_2;
	
	@ContentWidget(id = R.id.d_up_address_lin) LinearLayout d_up_address_lin;
	@ContentWidget(id = R.id.d_up_address_static) TextView d_up_address_static;
	@ContentWidget(id = R.id.d_up_address) TextView d_up_address;
	@ContentWidget(id = R.id.d_up_address_line) View d_up_address_line;
	@ContentWidget(id = R.id.tv_address_name) TextView tv_address_name;
	
	@ContentWidget(id = R.id.d_order_describe_lin) LinearLayout d_order_describe_lin;//��������
	@ContentWidget(id = R.id.d_order_describe_param) TextView d_order_describe_param;
	@ContentWidget(id = R.id.d_order_describe_line) View d_order_describe_line;
	
	@ContentWidget(id = R.id.d_2_lin) LinearLayout d_2_lin;
	@ContentWidget(id = R.id.d_3_lin) LinearLayout d_3_lin;
	@ContentWidget(id = R.id.d_4_lin) LinearLayout d_4_lin;
	@ContentWidget(id = R.id.d_2_line) View d_2_line;
	@ContentWidget(id = R.id.d_3_line) View d_3_line;
	@ContentWidget(id = R.id.d_4_line) View d_4_line;
	
	/*Handler*/
	private Handler handler;
	private final static int Request_Data = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		AnnotationViewUtils.injectObject(this, this);

		/*Handler*/
		initHandler();
		
		/*����*/
		TitleBarHelper.Back(this, "��������", 0);
		
		/*���ض���*/
		//LoadAnimateHelper.Search_Animate(this, R.id.activity, handler, 0, false,true,1);
		
		/*�������*/
		Request_AmountDetail();
		
	}
	
	@Override
	protected void onPause() {

		super.onPause();

	}

	/*���󶩵�����*/
	private void Request_AmountDetail() {

		String api = "";
		
		switch (Public_Param.orderdeaail_orderType) {
		
			case 2://�ŵ��ţ�http://182.61.22.80/api/door/1025367/order
				api = "api/door/"+Public_Param.orderId_detail+"/order";System.out.println("����api"+api);
				break;
			
			case 3://����
				api = "api/driver/"+Public_Param.orderId_detail+"/order";
				break;
	
			case 4://���ͻ�
				api = "api/airportTrip/"+Public_Param.orderId_detail+"/order";
				break;
			default:
				break;
		}
		
		new HttpHelper().initData(HttpHelper.Method_Get, this, api, null, null, handler, Request_Data, 1, new TypeReference<Order_Detail>() {});		
		
	}

	private void initHandler() {

		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);

				switch (msg.what) {

					
					case Request_Data:
						
						if(HandlerHelper.getString(msg).equals(HandlerHelper.Ok)){
							
							//LoadAnimateHelper.load_success_animation();	
							Order_Detail order = (Order_Detail)msg.obj;
							
							switch (Public_Param.orderdeaail_orderType) {
							
								case 2://�ŵ��ţ�http://182.61.22.80/api/door/1025367/order
									init_door(order);
									break;
								
								case 3://����
									init_driver(order);
									break;
						
								case 4://���ͻ�
									init(order);
									break;
								default:
									break;
							}
	
				           	return;
						}else{
							
							//LoadAnimateHelper.load_fail_animation();
						}					
						break;

					default:
						break;
				}
			}
		};
	}
	
	private void init_door(Order_Detail order){
		
		ImageLoaderHelper.initImageLoader(this);
		ImageLoader.getInstance().displayImage(Public_Api.appWebSite + order.vehicleModelShow.picture , b_1, ImageLoaderHelper.initDisplayImageOptions());	
		
		d_2_lin.setVisibility(View.GONE);
		d_3_lin.setVisibility(View.GONE);
		d_4_lin.setVisibility(View.GONE);
		d_2_line.setVisibility(View.GONE);
		d_3_line.setVisibility(View.GONE);
		d_4_line.setVisibility(View.GONE);
		
		/*���³���ַ��ǰ�����*/
		tv_address_name.setText("�ͳ���ַ");
		d_up_address_static.setText("ȡ����ַ");
		d_up_address.setText(Public_Param.doortodoor_upaddress);
		d_6.setText(Public_Param.doortodoor_downaddress);
		
		/*�ؼ�*/
		TextView[] textViews = new TextView[]{
				
				a_1,a_2, 
				b_2,b_3,
				c_1, c_2,c_3,
				d_1,d_5,
				
				d_7,
				
				e_1,e_2
		};
		String carTrunk = order.vehicleModelShow.carTrunk == null ? "" : order.vehicleModelShow.carTrunk;
		
		/*����*/
		String[] datas = new String[]{
				
				order.orderId,Public_Param.doortodoor_typename,
				
				order.vehicleModelShow.model,carTrunk+"��/"+order.vehicleModelShow.seats+"��",
				
				order.userShow.realName,order.userShow.phone,  "",//�г̱�ע����
							
				Public_Param.doortodoor_type.intValue() == 1 ? order.takeCarCityName : order.returnCarAddress,TimeHelper.getTimemis_to_StringTime(Public_Param.doortodoor_time),
				
				order.outsideDistance == null ? "0����" : order.outsideDistance+"����",
			
				"���ͻ������շ�","��"
		};
				
		/*���*/
		ViewInitHelper.initTextViews(textViews, datas);
		
	}
	
	private void init(Order_Detail order){
		
		ImageLoaderHelper.initImageLoader(this);
		ImageLoader.getInstance().displayImage(Public_Api.appWebSite + order.vehicleModelShow.picture , b_1, ImageLoaderHelper.initDisplayImageOptions());	
		d_up_address_lin.setVisibility(View.GONE);
		d_up_address_line.setVisibility(View.GONE);
		
		if(order.tripType.intValue() == 2 || order.tripType.intValue() == 4){
			
			tv_address_name.setText("�ϳ���ַ");
		}else{
			
			tv_address_name.setText("�³���ַ");
		}
		
		/*�ؼ�*/
		TextView[] textViews = new TextView[]{
				
				a_1,a_2, 
				b_2,b_3,
				c_1, c_2,c_3,
				d_1, d_2,d_3,d_4,d_5,d_6,
				
				d_7,
				
				e_1,e_2
		};
		String carTrunk = order.vehicleModelShow.carTrunk == null ? "" : order.vehicleModelShow.carTrunk;
		
		/*����*/
		String[] datas = new String[]{
				
				order.orderId,new String[]{"�ӻ�","�ͻ�","�ӻ�վ","�ͻ�վ"}[order.tripType-1],
				
				order.vehicleModelShow.model,carTrunk+"��/"+order.vehicleModelShow.seats+"��",
				
				order.passengerName,order.passengerPhone,order.tripRemark,
								
				order.takeCarCity,order.transferPointShow.pointName,order.airlineCompany,order.flightNumber,
				TimeHelper.getTimemis_to_StringTime(order.takeCarDate.toString()),order.tripAddress,
				
				order.tripDistance == null ? "0����" : order.tripDistance+"����",
			
				"���ͻ������շ�","��"+order.payAmount
		};
				
		/*���*/
		ViewInitHelper.initTextViews(textViews, datas);
		
		
		/*��������*/
		d_order_describe_line.setVisibility(View.VISIBLE);
		d_order_describe_lin.setVisibility(View.VISIBLE);
		d_order_describe_param.setText(StringHelper.getString(order.airDescribe));
	}
	
	private void init_driver(Order_Detail order){
		
		ImageLoaderHelper.initImageLoader(this);
		ImageLoader.getInstance().displayImage(Public_Api.appWebSite + order.vehicleModelShow.picture , b_1, ImageLoaderHelper.initDisplayImageOptions());	
		
		d_2_lin.setVisibility(View.GONE);
		d_3_lin.setVisibility(View.GONE);
		d_4_lin.setVisibility(View.GONE);
		d_2_line.setVisibility(View.GONE);
		d_3_line.setVisibility(View.GONE);
		d_4_line.setVisibility(View.GONE);
		d_up_address.setText(order.takeCarAddress);
		d_6.setText(order.returnCarAddress);
		
		/*�ؼ�*/
		TextView[] textViews = new TextView[]{
				
				a_1,a_2, 
				b_2,b_3,
				c_1, c_2,c_3,
				d_1,d_5,
				
				d_7,
				
				e_1,e_2
		};
		String carTrunk = order.vehicleModelShow.carTrunk == null ? "" : order.vehicleModelShow.carTrunk;
		
		/*����*/
		String[] datas = new String[]{
				
				order.orderId,new String[]{"�����Լ�","�ŵ��ŷ���","�������","���ͻ�"}[Public_Param.orderdeaail_orderType-1],
				
				order.vehicleModelShow.model,carTrunk+"��/"+order.vehicleModelShow.seats+"��",
				
				order.passengerName,order.passengerPhone,order.tripRemark,
								
				order.takeCarCity,TimeHelper.getTimemis_to_StringTime(order.takeCarDate.toString()),
				
				order.outsideDistance == null ? "0����" : order.outsideDistance+"����",
			
				"���ͻ������շ�","��"+order.payAmount
		};
				
		/*���*/
		ViewInitHelper.initTextViews(textViews, datas);
		
	}
	
}
