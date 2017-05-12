package com.gjmgr.activity.user;

import java.util.ArrayList;

import com.alibaba.fastjson.TypeReference;
import com.gjmgr.annotation.ContentView;
import com.gjmgr.annotation.ContentWidget;
import com.gjmgr.app.R;
import com.gjmgr.data.adapter.Car_Model_Adapter;
import com.gjmgr.data.bean.CarModel;
import com.gjmgr.data.data.Public_Param;
import com.gjmgr.utils.AnnotationViewUtils;
import com.gjmgr.utils.HandlerHelper;
import com.gjmgr.utils.HttpHelper;

import com.gjmgr.view.helper.FgDialog_Msg;
import com.gjmgr.view.helper.LoadAnimateHelper;
import com.gjmgr.view.helper.TitleBarHelper;
import com.gjmgr.view.listview.XListView;
import com.gjmgr.view.listview.XListView.IXListViewListener;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

@ContentView(R.layout.activity_car_select)
public class Activity_Car_Model extends Activity implements IXListViewListener{
	
	@ContentWidget(id = R.id.listview) XListView listview;
	private Car_Model_Adapter adapter;
	
	/*Handler*/
	private Handler handler;
	private final static int Request = 1;	
	private final static int Click = 2;
	private final static int Show = 3;
	private final static int Item_Select = 4;
	private final static int Click_Dialog = 5;
	private int currentPage = 1;
	
	/*数据*/
	private ArrayList<CarModel> orderlist = new ArrayList<CarModel>();
	private int index = -1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		AnnotationViewUtils.injectObject(this, this);
		System.out.println("aaaaaaaaaaaaa");
		Public_Param.isSelected = false;
		/*listview*/
		initXListView();
		
		/*handler*/
		initHandler();
		
		/*标题*/
		TitleBarHelper.Back(this, "选择车型", 0);
		
		/*加载动画*/
		LoadAnimateHelper.Search_Animate(this, R.id.activity, handler, Click, false,true,1);
		
		/*初始化数据*/
		initData();
		
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
	protected void onResume() {
		
		super.onResume();
		
		initData();
	}
	
	private void initData() {System.out.println("1");
		
		String api = "api/vehicle/brand/series/model?brand=&currentPage=3&fuzzy=1&model=&pageSize=30&series=";
		
		new HttpHelper().initData(HttpHelper.Method_Get, this, api, null, null, handler, Request, 1, new TypeReference<ArrayList<CarModel>>() {});
		
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

							orderlist = (ArrayList<CarModel>)msg.obj;
							System.out.println("size"+orderlist.size());	      
				            //	System.out.println("解析结束"+orderlist.get(0).model );	
				           	if(orderlist.size() == 0){
				           		LoadAnimateHelper.load_empty_animation();
				           	}
				           	handler.sendEmptyMessage(Show);
				           	return;
						}
						if(HandlerHelper.getString(msg).equals(HandlerHelper.Empty)){
							System.out.println("yyyyyyyyyyyyyyyyyyyyyyyyyyy");
							LoadAnimateHelper.load_empty_animation();
				           	return;
						}
						LoadAnimateHelper.load_fail_animation();
						System.out.println("请求失败");	 
																								
						break;
					
					case Click:
						initData();
						break;
						
					case Show:
						adapter = new Car_Model_Adapter(Activity_Car_Model.this, orderlist, handler, Item_Select);
						listview.setAdapter(adapter);
						
						LoadAnimateHelper.load_success_animation();
						
						break;
						
					case Item_Select:
						
						index = msg.getData().getInt("message");
						new FgDialog_Msg().ShowDialog(Activity_Car_Model.this, "您确定选择"+orderlist.get(index).model+"的车型吗？", handler, Click_Dialog);							
						break;
						
					case Click_Dialog:
						
						Public_Param.isSelected = true;
						Public_Param.selected_modelId = orderlist.get(index).id;
						Public_Param.model = orderlist.get(index).model;
						
						finish();
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
