package com.gjmgr.activity.user;

import java.util.ArrayList;

import com.alibaba.fastjson.TypeReference;
import com.gjcar.view.wheelview.ArrayWheelAdapter;
import com.gjcar.view.wheelview.WheelView;
import com.gjmgr.annotation.ContentView;
import com.gjmgr.annotation.ContentWidget;
import com.gjmgr.app.R;
import com.gjmgr.data.adapter.Car_Allot_Adapter;
import com.gjmgr.data.adapter.Car_Model_Adapter;
import com.gjmgr.data.bean.CarModel;
import com.gjmgr.data.bean.Car_Veichel;
import com.gjmgr.data.data.Public_Param;
import com.gjmgr.data.data.Public_SP;
import com.gjmgr.utils.AnnotationViewUtils;
import com.gjmgr.utils.HandlerHelper;
import com.gjmgr.utils.HttpHelper;
import com.gjmgr.utils.IntentHelper;
import com.gjmgr.utils.NetworkHelper;
import com.gjmgr.utils.SharedPreferenceHelper;
import com.gjmgr.utils.StringHelper;
import com.gjmgr.utils.TimeHelper;
import com.gjmgr.utils.ToastHelper;
import com.gjmgr.utils.ValidateHelper;

import com.gjmgr.view.dialog.DateTimePickerHelper;
import com.gjmgr.view.dialog.SelectDailog;
import com.gjmgr.view.dialog.SubmitDialog;
import com.gjmgr.view.helper.FgDialog_Msg;
import com.gjmgr.view.helper.LoadAnimateHelper;
import com.gjmgr.view.helper.LoadAnimateHelper_Car;
import com.gjmgr.view.helper.TitleBarHelper;
import com.gjmgr.view.listview.XListView;
import com.gjmgr.view.listview.XListView.IXListViewListener;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnKeyListener;
import android.graphics.LinearGradient;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

@ContentView(R.layout.activity_car_allot)
public class Activity_Car_Allot extends Activity implements IXListViewListener{
	
	@ContentWidget(id = R.id.listview) XListView listview;
//	@ContentWidget(id = R.id.model) TextView model;
	
	@ContentWidget(click = "onClick") LinearLayout f1_form_modelselect_lin;
	@ContentWidget(id = R.id.f1_form_modelselect_carmodel) TextView f1_form_modelselect_carmodel;
	@ContentWidget(click = "onClick") LinearLayout f1_form_state_lin;
	@ContentWidget(id = R.id.f1_form_state) TextView f1_form_state;
		
	private Car_Allot_Adapter adapter;
	
	/*Handler*/
	private Handler handler;
	private final static int Request = 1;	
	private final static int Click = 2;
	private final static int Show = 3;
	private final static int Item_Select = 4;
	private final static int Click_Dialog = 5;
	private final static int Update_Model = 6;	
	private final static int Http_Get_CarModel = 7;	
	private int currentPage = 1;
	
	/*数据*/
	private ArrayList<Car_Veichel> orderlist = new ArrayList<Car_Veichel>();
	private int index = -1;
	
	ArrayList<CarModel> model_list = new ArrayList<CarModel>();
	
	private int default_model = -1;
	private int default_state = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		AnnotationViewUtils.injectObject(this, this);
		System.out.println("aaaaaaaaaaaaa");
		/*listview*/
		initXListView();
		
		/*handler*/
		initHandler();
		
		/*标题*/
		TitleBarHelper.Back_ok(this, "车辆分配", handler, Update_Model, 0,false);
		
		/*加载动画*/
		LoadAnimateHelper_Car.Search_Animate(this, R.id.activity, handler, Click, false,true,3);
		
		/*初始化数据*/
		initData(default_state);
		
		f1_form_modelselect_carmodel.setText(Public_Param.model == null ? "" : ""+Public_Param.model);
		
	}
	
	@TargetApi(Build.VERSION_CODES.CUPCAKE)
	@SuppressLint("NewApi")
	private void initXListView() {
		listview.setPullLoadEnable(false);
		listview.setPullRefreshEnable(false);
		listview.setFooterDividersEnabled(false);
		listview.setHeaderDividersEnabled(false);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		super.onActivityResult(requestCode, resultCode, data);
		
		if(Public_Param.isSelected){
			
			Public_Param.isSelected = false;
			
			Public_Param.modelId = Public_Param.selected_modelId;
			f1_form_modelselect_carmodel.setText(Public_Param.model == null ? "" : ""+Public_Param.model);
			listview.setVisibility(View.GONE);
			initData(default_state);
		}
	}

	private void initData(int state) {System.out.println("1");//

		listview.setVisibility(View.GONE);
		LoadAnimateHelper_Car.start_animation();
		
		String parma_state = "&states="+"rented,ready";				
		
		switch (state) {
		
			case 0:
				parma_state = "&states="+"rented,ready";	
				break;
	
			case 1:
				parma_state = "&states="+"ready";	
				break;
				
			case 2:
				parma_state = "&states="+"rented";
				break;
				
			default:
				break;
		}
		
		String api = "";
		switch (Public_Param.takecar_orderType) {
		
			case 3://代驾
				api = "api/vendor/1/pool/1/vehicle?business=2&currentPage=1&modelId="+Public_Param.modelId+"&cid="+SharedPreferenceHelper.getString(this, Public_SP.Account, "cityId")+"&pageSize=10&storeType=2"+parma_state;
				break;
				       
			case 4://接送机
				api = "api/vendor/1/pool/1/vehicle?business=3&currentPage=1&modelId="+Public_Param.modelId+"&cid="+SharedPreferenceHelper.getString(this, Public_SP.Account, "cityId")+"&pageSize=10&storeType=2"+parma_state;
				break;
				
			default:
				break;
		}

		new HttpHelper().initData(HttpHelper.Method_Get, this, api, null, null, handler, Request, 1, new TypeReference<ArrayList<Car_Veichel>>() {});
		
	}
	
	public void onClick(View view) {
		
		switch (view.getId()) {
		
			case R.id.f1_form_modelselect_lin:
				
				String api = "api/vehicle/brand/series/model?brand=&currentPage=1&fuzzy=1&model=&pageSize=50&series="+"&cid="+SharedPreferenceHelper.getString(this, Public_SP.Account, "cityId");				
				new HttpHelper().initData(HttpHelper.Method_Get, this, api, null, null, handler, Http_Get_CarModel, 1, new TypeReference<ArrayList<CarModel>>() {});

				break;
			
			case R.id.f1_form_state_lin:
				showSelectDialog(2,new String[]{"全部","待租赁","租赁中"});
				break;	
				
			default:
				break;
		}
	}
	
	@SuppressLint("HandlerLeak")
	private void initHandler() {

		handler = new Handler() {
			@SuppressWarnings("unchecked")
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);

				switch (msg.what) {

					case Request:
							
						if(HandlerHelper.getString(msg).equals(HandlerHelper.Ok)){
								
							orderlist = (ArrayList<Car_Veichel>)msg.obj;
							System.out.println("size"+orderlist.size());	      
				            //	System.out.println("解析结束"+orderlist.get(0).model );	
				           	if(orderlist.size() == 0){
				           		LoadAnimateHelper_Car.load_empty_animation();
				           	}
				           	handler.sendEmptyMessage(Show);
				           	return;
						}
						if(HandlerHelper.getString(msg).equals(HandlerHelper.Empty)){
							System.out.println("yyyyyyyyyyyyyyyyyyyyyyyyyyy");
							LoadAnimateHelper_Car.load_empty_animation();
				           	return;
						}
						LoadAnimateHelper_Car.load_fail_animation();
						System.out.println("请求失败");	 
																								
						break;
						
					case Click:
						initData(default_state);
						break;
						
					case Show:
						
						listview.setVisibility(View.VISIBLE);
						adapter = new Car_Allot_Adapter(Activity_Car_Allot.this, orderlist, handler, Item_Select);
						listview.setAdapter(adapter);						
						LoadAnimateHelper_Car.load_success_animation();
						
						break;
						
					case Item_Select:
						
						index = msg.getData().getInt("message");
						new FgDialog_Msg().ShowDialog(Activity_Car_Allot.this, "您确定分配车牌为"+orderlist.get(index).plate+"的车辆吗？", handler, Click_Dialog);							
						break;
						
					case Click_Dialog:
						
						Public_Param.plate = orderlist.get(index).plate;
						Public_Param.modelId = orderlist.get(index).modelId;
						Public_Param.vehicleId = orderlist.get(index).id;
						Public_Param.takecar_Melliage = new Integer(orderlist.get(index).mileage).intValue();
						IntentHelper.startActivity(Activity_Car_Allot.this, Activity_Car_Take.class);
						finish();
						break;
					
					case Update_Model:
						
						Intent i = new Intent(Activity_Car_Allot.this,Activity_Car_Model.class);
						startActivityForResult(i, 200);
						break;
						
					case Http_Get_CarModel:
						
						if(HandlerHelper.getString(msg).equals(HandlerHelper.Ok)){
//							showSelectDialog(1);
							model_list = (ArrayList<CarModel>)msg.obj;
							System.out.println("size"+model_list.size());
							String[] models = new String[model_list.size()];
							for (int j = 0; j < model_list.size(); j++) {
								models[j] = model_list.get(j).model;
							}
							showSelectDialog(1,models);
				           	return;
						}
						
						break;
						
					default:
						break;
						
				}
			}
		};
	}
	
	
/**加载或刷新***************************************************************************************************/	
	
	@Override
	public void onRefresh() {
//		handler.postDelayed(new Runnable() {
//
//			@Override
//			public void run() {
//
//				getDownRefresh();
//			}
//		}, 1000);

	}

	@Override
	public void onLoadMore() {

//		handler.postDelayed(new Runnable() {
//
//			@Override
//			public void run() {
//				getUpLoadMore();
//			}
//		}, 100);
	}

	// 上拉加载更多
	public void getUpLoadMore() {
//		/*加载数据*/
//		fuzzy = fuzzy + 1;
//		getData(getData_LoadMore);//加载商品类别
	}

	/**弹出框***************************************************************************************************/		
	private void showSelectDialog(int type,final String[] cars) {

		/* 创建对象 */
		final Dialog dialog = new Dialog(Activity_Car_Allot.this, R.style.delete_dialog);
	
		/* 初始化视图 */
		View view = View.inflate(Activity_Car_Allot.this,
				R.layout.dialog_f1_takecar_select_wheelview, null);
				
		if(type == 1){
			initView_Select(view, dialog, type, default_model,cars);
		}else{
			initView_Select(view, dialog, type,default_state,cars);
		}
			
		/* 初始化监听器 */
		dialog.setOnKeyListener(new OnKeyListener() {
				
			@Override
			public boolean onKey(DialogInterface arg0, int arg1, KeyEvent arg2) {
				
				return false;
			}
				
		});
	
		/* 初始化监属性 */
		dialog.getWindow().setContentView(view);
		dialog.getWindow().setGravity(Gravity.BOTTOM);
		WindowManager wm = (WindowManager) this.getSystemService(
				Context.WINDOW_SERVICE);
		dialog.getWindow().setLayout(wm.getDefaultDisplay().getWidth(),
				LinearLayout.LayoutParams.WRAP_CONTENT);
		dialog.setCancelable(true);
		dialog.setCanceledOnTouchOutside(true);
	
		/* 显示 */
		dialog.show();
	}

	private void initView_Select(View view, final Dialog dialog, final int type, int defaultitem,final String[] cars) {
	
		final WheelView wheelView = (WheelView) view.findViewById(R.id.carmodel);
//		final String[] cars = {"不限","经济型","舒适型","豪华型","SUV","MPV"} ;
//		final String[] prices = {"全部","待租赁","租赁中"} ;
		ArrayWheelAdapter<String> arrayWheelAdapter;
		if(type == 1){
			arrayWheelAdapter = new ArrayWheelAdapter<String>(cars,5);
		}else{
			arrayWheelAdapter = new ArrayWheelAdapter<String>(cars,2);
		}
		wheelView.setAdapter(arrayWheelAdapter);
		wheelView.setCurrentItem(defaultitem);
		
		/* 获取车型或价格 */
		TextView ok = (TextView) view.findViewById(R.id.select_ok);
		ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				/* 获取车型或价格 */
				if(type == 1){
					
					String model = cars[wheelView.getCurrentItem()];
					default_model = wheelView.getCurrentItem();
					f1_form_modelselect_carmodel.setText(model);
					Public_Param.modelId = model_list.get(default_model).id;
					initData(default_state);
					
				}else{
					String state = cars[wheelView.getCurrentItem()];
					default_state = wheelView.getCurrentItem();
					f1_form_state.setText(state);
					initData(default_state);
				}
								
				if (null != dialog) {
					
					dialog.dismiss();
				}
			}
		});
		TextView cancel = (TextView) view
				.findViewById(R.id.select_cancle);
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (null != dialog) {
					dialog.dismiss();
				}
			}
		});
	}
	
}
