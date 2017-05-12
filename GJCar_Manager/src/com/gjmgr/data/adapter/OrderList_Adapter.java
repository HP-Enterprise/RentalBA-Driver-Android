package com.gjmgr.data.adapter;


import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gjmgr.activity.user.Activity_Order_Detail;
import com.gjmgr.activity.user.Activity_Order_Finish_Detail;
import com.gjmgr.app.R;
import com.gjmgr.data.bean.Order;
import com.gjmgr.data.data.Public_Api;
import com.gjmgr.data.data.Public_Data;
import com.gjmgr.data.data.Public_Param;
import com.gjmgr.data.data.Public_SP;
import com.gjmgr.utils.HttpHelper;
import com.gjmgr.utils.IntentHelper;
import com.gjmgr.utils.SharedPreferenceHelper;
import com.gjmgr.utils.StringHelper;
import com.gjmgr.utils.TimeHelper;
import com.gjmgr.utils.ToastHelper;
import com.gjmgr.view.dialog.SubmitDialog;
import com.gjmgr.view.widget.SingleLineZoomTextView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
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
public class OrderList_Adapter extends BaseAdapter {

	private ArrayList<Order> orderlist = new ArrayList<Order>();
	private Context context;
	private Handler handler;
	private int what_confirm;
	private int what_reject;
	
	public OrderList_Adapter(Context context, ArrayList<Order> orderlist, Handler handler, int what_confirm, int what_reject) {
		
		this.orderlist = orderlist;
		this.context = context;		
		this.handler = handler;
		this.what_confirm = what_confirm;
		this.what_reject = what_reject;
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
		
		Holder holder;System.out.println("aaaaaaaaaaaaaaaaa带毒的看不懂1");
		holder = new Holder();
		convertView = View.inflate(context, R.layout.orderlist_item_test, null);
		
		holder.a_lin  = (LinearLayout) convertView.findViewById(R.id.a_lin); 
		
		holder.a_order_ty  = (TextView) convertView.findViewById(R.id.a_order_ty);	System.out.println("a2");   
		holder.a_order_time  = (TextView) convertView.findViewById(R.id.a_order_time);	System.out.println("a3");

		holder.a_orderId = (TextView) convertView.findViewById(R.id.a_orderId);	System.out.println("a4");
		holder.a_order_distance = (TextView) convertView.findViewById(R.id.a_order_distance);	System.out.println("a5");
		holder.b_air_message = (SingleLineZoomTextView) convertView.findViewById(R.id.b_air_message);	System.out.println("a6");
		holder.b_start_address = (TextView) convertView.findViewById(R.id.b_start_address);	System.out.println("a7");
		holder.b_car = (TextView) convertView.findViewById(R.id.b_car);	System.out.println("a8");
		holder.b_end_address = (TextView) convertView.findViewById(R.id.b_end_address);	System.out.println("a9");

		holder.c_ok = (TextView) convertView.findViewById(R.id.c_ok);	
		holder.c_cancle = (TextView) convertView.findViewById(R.id.c_cancle);	
						
		convertView.setTag(holder);
//		if (convertView == null) {System.out.println("a1");
//			holder = new Holder();
//			convertView = View.inflate(context, R.layout.orderlist_item_test, null);
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
//			holder.c_ok = (TextView) convertView.findViewById(R.id.c_ok);	
//			holder.c_cancle = (TextView) convertView.findViewById(R.id.c_cancle);	
//							
//			convertView.setTag(holder);
//		} else {System.out.println("a111");
//			holder = (Holder) convertView.getTag();
//		}
	
		holder.a_order_ty.setText(orderlist.get(position).orderTypeName);System.out.println("a11");//1接机 2送机 3接火车站 4 送火车站
		//holder.a_order_time.setText(orderlist.get(position).);//TimeHelper.getTimemis_to_StringTime(order.takeCarDate.toString())
		
		holder.a_orderId.setText("订单编号："+orderlist.get(position).orderCode+"");System.out.println("a12");
		//holder.a_order_distance.setText("预估里程："+orderlist.get(position).tripDistance+"公里"+"");System.out.println("a13");
		//holder.b_air_message.setText("航班信息："+orderlist.get(position).airport+"+"+orderlist.get(position).flightNumber);	System.out.println("a14");
//		holder.b_start_address.setText(TimeHelper.getDateTime_YM(TimeHelper.getTimemis_to_StringTime(orderlist.get(position).expectEndTime)));
		holder.b_car.setText(orderlist.get(position).modelName+"");System.out.println("a15");
		//holder.b_end_address.setText(orderlist.get(position).tripAddress+"");	System.out.println("a16");
		
		reqest_Data(orderlist.get(position).orderType.intValue(),orderlist.get(position).orderCode,position,
				
				holder.a_order_ty,holder.a_order_distance,holder.b_air_message,holder.a_order_time,holder.b_start_address,holder.b_end_address);
		
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
				
				SubmitDialog.showSubmitDialog(context);
				String driverId = SharedPreferenceHelper.getString(context, Public_SP.Account, "id");
				System.out.println("确定任务id-----------"+orderlist.get(position).id.intValue());
				System.out.println("确定任务driverId-----------"+driverId);
				
				Request_Submit(true,orderlist.get(position).id.intValue(), driverId, "", Public_Api.api_task_confirm);
			}
		});
		
		holder.c_cancle.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				
				initDialog(position,context);
				
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

		private TextView c_ok;
		private TextView c_cancle;
	}

	public void setDataChanged(ArrayList<Order> list) {
		this.orderlist = list;
		this.notifyDataSetChanged();
	}
	
	private void reqest_Data(final int orderType, final String orderId, final int index,  final TextView tv_triptype, final TextView distance, final TextView tv_message, final TextView date,final TextView b_start_address,final TextView b_end_address) {
		System.out.println("发送请求"+orderId);
		
		String api = "";
		switch (orderType) {
			case 3://代驾
				api = "api/driver/"+orderId+"/order";System.out.println("订单api"+api);
				break;
	
			case 4://接送机
				api = "api/airportTrip/"+orderId+"/order";
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
				System.out.println("返回值\n:" + backData);

				JSONObject statusjobject = JSON.parseObject(backData);

				boolean status = statusjobject.getBoolean("status");
				String message = statusjobject.getString("message");

				if (status) {

					JSONObject j = JSON.parseObject(message);
					//int tripType = j.getIntValue("tripType");
					if(orderType == 4){
						
						String airlineCompany = j.getString("airlineCompany");
						String flightNumber = j.getString("flightNumber");
						Integer tripType = j.getInteger("tripType");
						String tripAddress = j.getString("tripAddress");
						
						String tripDistance = (j.getString("tripDistance")== null || j.getString("tripDistance").equals("null")) ? "0":j.getString("tripDistance");
						String takeCarDate = j.getString("takeCarDate");
						String transferPointShow = j.getString("transferPointShow");
						JSONObject j_trans = JSON.parseObject(transferPointShow);
						String pointName = j_trans.getString("pointName");
						
						tv_triptype.setText(tripType == null ? "" : new String[]{"接机","送机","接火车站","送火车站"}[tripType.intValue()-1]);		
						date.setText(""+TimeHelper.getTimemis_to_StringTime1(takeCarDate));//TimeHelper.getTimemis_to_StringTime(order.takeCarDate.toString())
						tv_message.setVisibility(View.VISIBLE);
						tv_message.setText("航班信息："+airlineCompany+"/"+flightNumber);
						
						distance.setText("预估里程："+tripDistance+"公里");System.out.println("a13");
						//holder.b_air_message.setText("航班信息："+orderlist.get(position).airport+"+"+orderlist.get(position).flightNumber);	System.out.println("a14");
	//					holder.b_start_address.setText(TimeHelper.getDateTime_YM(TimeHelper.getTimemis_to_StringTime(orderlist.get(position).expectEndTime)));
						if(tripType.intValue() == 2 || tripType.intValue() == 4){
							//送机
							b_start_address.setText(""+tripAddress);
							b_end_address.setText(""+pointName);
						}else{
							b_start_address.setText(""+pointName);
							b_end_address.setText(""+tripAddress);
							
						}
						
					}else{
						
						String takeCarAddress = j.getString("takeCarAddress");System.out.println("takeCarAddress"+takeCarAddress);
						String returnCarAddress = j.getString("returnCarAddress");
						String takeCarDate = j.getString("takeCarDate");
						String outsideDistance = (j.getString("outsideDistance")== null || j.getString("outsideDistance").equals("null")) ? "0":j.getString("outsideDistance");

						//tv_triptype.setText(new String[]{"接机","送机","接火车站","送火车站"}[tripType-1]);		
						date.setText(""+TimeHelper.getTimemis_to_StringTime1(takeCarDate));//TimeHelper.getTimemis_to_StringTime(order.takeCarDate.toString())
						tv_message.setText("");
						tv_message.setVisibility(View.GONE);
						
						distance.setText("预估里程："+outsideDistance+"公里");System.out.println("a13");
						//holder.b_air_message.setText("航班信息："+orderlist.get(position).airport+"+"+orderlist.get(position).flightNumber);	System.out.println("a14");
	//					holder.b_start_address.setText(TimeHelper.getDateTime_YM(TimeHelper.getTimemis_to_StringTime(orderlist.get(position).expectEndTime)));						
						b_start_address.setText(""+takeCarAddress);
						b_end_address.setText(""+returnCarAddress);
					}
				}
				
			}

			/* 5.处理请求失败 */
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {

			}
		});
	}
	
	/** 提交信息  */
	private void  Request_Submit(boolean isOk, int taskId, String operatorId, String operateDesc, String api){
		
		/*参数*/
		JSONObject jsonObject = new JSONObject(); //**********************注意json发送数据时，要这样
				
		jsonObject.put("taskId", taskId);
		jsonObject.put("operatorId", operatorId);//司机
		
		if(isOk){

			/*提交*/
			new HttpHelper().initData(HttpHelper.Method_Put, context, api, jsonObject, null, handler, what_confirm, 2, null);
		}else{
			
			jsonObject.put("operateDesc",operateDesc);//如果拒绝，则添加理由
			System.out.println("taskId"+jsonObject.get("taskId"));
			System.out.println("operatorId"+jsonObject.get("operatorId"));
			System.out.println("operateDesc"+jsonObject.get("operateDesc"));
			/*提交*/
			new HttpHelper().initData(HttpHelper.Method_Put, context, api, jsonObject, null, handler, what_reject, 2, null);
		}
		
	}
	
	private void initDialog(final int index, final Context context) {
		
		final Dialog updateDialog = new Dialog(context, R.style.scorechange_dialog);
		
		View view = View.inflate(context, R.layout.dialog_reject_reason, null);

		/*初始化控件*/
		final EditText reason = (EditText)view.findViewById(R.id.reason);

		TextView cancle = (TextView)view.findViewById(R.id.cancle);
		TextView ok = (TextView)view.findViewById(R.id.ok);
		
		/*设置事件*/		
		class MyOnClickListener implements OnClickListener{

			@Override
			public void onClick(View view) {
				
				switch (view.getId()) {
					
					case R.id.ok:
						
						if(reason.getText().toString() == null || reason.getText().toString().trim().equals("")){
							
							ToastHelper.showToastShort(context, "请输入拒绝任务的理由");
							return;
						}
						SubmitDialog.showSubmitDialog(context);
						updateDialog.dismiss();
						String driverId = SharedPreferenceHelper.getString(context, Public_SP.Account, "id");
						Request_Submit(false,orderlist.get(index).id.intValue(), driverId, reason.getText().toString().trim(), Public_Api.api_task_reject);
							
						break;
				
					case R.id.cancle:
						updateDialog.dismiss();
						break;

						
					default:
						break;
				}
			}
			
		}
		MyOnClickListener onClickListener = new MyOnClickListener();
		
		ok.setOnClickListener(onClickListener);
		cancle.setOnClickListener(onClickListener);
		
		/*对话框的设置*/
		updateDialog.getWindow().setContentView(view);
		updateDialog.setCancelable(false);
		updateDialog.setCanceledOnTouchOutside(false);
		updateDialog.show();

		WindowManager windowManager = ((Activity) context).getWindowManager();  
		Display display = windowManager.getDefaultDisplay();  
		WindowManager.LayoutParams lp = updateDialog.getWindow().getAttributes();  
		
		 final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, context.getResources()
	                .getDisplayMetrics());//旁边的margin
		
		lp.width = (int)(display.getWidth()-pageMargin); //设置宽度  
		updateDialog.getWindow().setAttributes(lp); 
	}
}
