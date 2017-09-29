package com.gjmgr.activity.user;

import java.util.Date;

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
	
@ContentView(R.layout.activity_car_return)
public class Activity_Car_Return extends Activity{

	@ContentWidget(click = "onClick") EditText time;
	@ContentWidget(click = "onClick") EditText oil;
	@ContentWidget(id = R.id.distance) EditText distance;
	
	@ContentWidget(click = "onClick") Button submit;
	
	/*参数*/
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
		
		Public_Param.isReturnCarOK = false;
		
		TitleBarHelper.Back(this, "还车", 0);

		/*消息*/
		initHandler();
		
	}
	
	public void onClick(View view) {
		
		switch (view.getId()) {
		
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
				if(!ValidateHelper.Validate(this, new boolean[]{!ValidationHelper.isNull("", time).equals(""),!ValidationHelper.isNull("", oil).equals(""),!ValidationHelper.isNull("", distance).equals("")}, 
						new String[]{"时间必须填写","油量必须填写","里程必须填写"})){
					return;
				}
				System.out.println("里程："+Public_Param.returncar_Melliage);
				
				if(TimeHelper.isLateTakeCarTime(Public_Param.returncar_date, time.getText().toString())){//先判断上车时间
				
					ToastHelper.showToastShort(Activity_Car_Return.this, "时间必须晚于下车时间"+TimeHelper.getTimemis_to_StringTime(Public_Param.returncar_date.toString()));
					
					return;
				}
				if(Public_Param.returncar_Melliage > new Integer(distance.getText().toString()).intValue()){
					
					ToastHelper.showToastShort(this, "还车里程至少为"+Public_Param.returncar_Melliage+"公里");
					
					return;
				}

				/*弹出提交对话框*/
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
							
							Public_Param.isReturnCarOK = true;
							
							ToastHelper.showToastShort(Activity_Car_Return.this, "还车成功");
							finish();
				           	return;
						}
						
						ToastHelper.showToastShort(Activity_Car_Return.this, "还车失败");
																								
						break;
						
					default:
						break;
				}
			}
		};
	}
	
	
	/** 提交信息  */
	private void  Request_Submit(){
		
		/*获取参数*/
//		String orderId = "1017260";
//		String modelId = "121";
//		String takeCarActualDate = "1492238100000";
//		String takeCarFuel = "121";
//		String takeCarMileage = "100";
//		String vehicleId = "461410";

		String orderId = Public_Param.orderId;
		String returnCarActualDate = TimeHelper.getSearchTime_Mis(time.getText().toString());
		String returnCarFuel = oil.getText().toString();
		String returnCarMileage = distance.getText().toString();
		
		/*api*/
		String api = "";
		switch (Public_Param.return_orderType) {
			case 3://代驾
				api = "api/return/"+orderId+"/driverContract?returnCarActualDate="+returnCarActualDate+"&returnCarFuel="+returnCarFuel+"&returnCarMileage="+returnCarMileage;			
				break;
	
			case 4://接送机
				api = "api/return/"+orderId+"/airContract?returnCarActualDate="+returnCarActualDate+"&returnCarFuel="+returnCarFuel+"&returnCarMileage="+returnCarMileage;			
				break;
				
			default:
				break;
		}
		/*提交*/
		new HttpHelper().initData(HttpHelper.Method_Put, this, api, null, null, handler, Request_Submit, 2, null);
		
	}
}
