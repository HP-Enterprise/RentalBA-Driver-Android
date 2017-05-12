package com.gjmgr.activity.user;

import java.util.ArrayList;

import com.alibaba.fastjson.TypeReference;
import com.gjmgr.annotation.ContentView;
import com.gjmgr.annotation.ContentWidget;
import com.gjmgr.app.R;
import com.gjmgr.data.adapter.Car_Allot_Adapter;
import com.gjmgr.data.adapter.Car_Model_Adapter;
import com.gjmgr.data.bean.CarModel;
import com.gjmgr.data.bean.Car_Veichel;
import com.gjmgr.data.data.Public_Param;
import com.gjmgr.utils.AnnotationViewUtils;
import com.gjmgr.utils.HandlerHelper;
import com.gjmgr.utils.HttpHelper;
import com.gjmgr.utils.IntentHelper;

import com.gjmgr.view.helper.FgDialog_Msg;
import com.gjmgr.view.helper.LoadAnimateHelper_Car;
import com.gjmgr.view.helper.TitleBarHelper;
import com.gjmgr.view.listview.XListView;
import com.gjmgr.view.listview.XListView.IXListViewListener;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

@ContentView(R.layout.activity_car_allot)
public class Activity_Car_Allot extends Activity implements IXListViewListener{
	
	@ContentWidget(id = R.id.listview) XListView listview;
	@ContentWidget(id = R.id.model) TextView model;
	
	private Car_Allot_Adapter adapter;
	
	/*Handler*/
	private Handler handler;
	private final static int Request = 1;	
	private final static int Click = 2;
	private final static int Show = 3;
	private final static int Item_Select = 4;
	private final static int Click_Dialog = 5;
	private final static int Update_Model = 6;	
	private int currentPage = 1;
	
	/*数据*/
	private ArrayList<Car_Veichel> orderlist = new ArrayList<Car_Veichel>();
	private int index = -1;
	
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
		TitleBarHelper.Back_ok(this, "车辆分配", handler, Update_Model, 0);
		
		/*加载动画*/
		LoadAnimateHelper_Car.Search_Animate(this, R.id.activity, handler, Click, false,true,2);
		
		/*初始化数据*/
		initData();
		
		model.setText(Public_Param.model == null ? "" : "租车车型："+Public_Param.model);
		
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
			model.setText(Public_Param.model == null ? "" : "租车车型："+Public_Param.model);
			listview.setVisibility(View.GONE);
			initData();
		}
	}

	private void initData() {System.out.println("1");//

		String api = "";
		switch (Public_Param.takecar_orderType) {
		
			case 3://代驾
				api = "api/vendor/1/pool/1/vehicle?business=2&currentPage=1&modelId="+Public_Param.modelId+"&pageSize=10&states=ready,rented&storeType=2";
				break;
				       
			case 4://接送机
				api = "api/vendor/1/pool/1/vehicle?business=3&currentPage=1&modelId="+Public_Param.modelId+"&pageSize=10&states=ready,rented&storeType=2";
				break;
			default:
				break;
		}

		new HttpHelper().initData(HttpHelper.Method_Get, this, api, null, null, handler, Request, 1, new TypeReference<ArrayList<Car_Veichel>>() {});
		
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
						initData();
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

}
