package com.gjmgr.framwork;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.os.Handler;
import android.view.View;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.BaiduMap.OnMapStatusChangeListener;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolygonOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.utils.SpatialRelationUtil;
import com.gjmgr.data.bean.LocationMessage;
import com.gjmgr.utils.HandlerHelper;
import com.gjmgr.utils.NetworkHelper;

public class BaiduMapHelper {

	/* 第一步：定位 */
	private LocationClient mLocClient;
	private MyLocationListenner myListener = new MyLocationListenner();
	private boolean isFirstLoc = true;
	private LatLng latitude_longtitude = null;
	
	/*消息*/
	private Handler handler;
	private int msg;
	
	public void startLocationClient(Context context, Handler handler, int msg){
		
		this.handler = handler;
		this.msg = msg;
		
		// 创建对象
		mLocClient = new LocationClient(context);
		mLocClient.registerLocationListener(myListener);
		
		/*第一步：设置GPS参数*/
		initGPSOption();
		
		/*第二步：开启定位*/
		mLocClient.start();
	}
	
	/**
	 * 第一步：设置GPS参数
	 */
	private void initGPSOption() {

		// 定位初始化
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
		option.setScanSpan(1000);
		option.setTimeOut(10000);
		mLocClient.setLocOption(option);		
		System.out.println("2**************");
	}

	/**
	 * 定位SDK监听函数
	 */
	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// map view 销毁后不在处理新接收的位置
			// System.out.println("定位失败0");
			if (location == null) {
				System.out.println("定位失败1");
				
				HandlerHelper.sendStringObject(handler, msg, "fail", null);
				return;
			}

			if (isFirstLoc) {
				System.out.println("定位成功^^^^^^^^^^^^^^^^^^^^^^^^");
				isFirstLoc = false;
				System.out.println("location.getLatitude()"+ location.getLatitude());
				System.out.println("location.getLongtitude()"+ location.getLongitude());
				
				LocationMessage locationMessage = new LocationMessage();				
				locationMessage.Latitude = location.getLatitude();
				locationMessage.Longitude = location.getLongitude();
				locationMessage.address = location.getAddrStr();
				if(location.getAddrStr() == null || location.getAddrStr().equals("")){
					
				}else{
					System.out.println("地址"+location.getAddrStr());
				}

				mLocClient.stop();
				HandlerHelper.sendStringObject(handler, msg, "ok",locationMessage);
			}
		}

		public void onReceivePoi(BDLocation poiLocation) {
			System.out.println("定位失败3");
			HandlerHelper.sendString(handler, msg, HandlerHelper.Fail);
		}
	}

	/****************************************************************************************************************************
	 * ****************************************************************************************************************************
	 * 
	 * 地图相关操作
	 * 	
	 * ****************************************************************************************************************************
	 * ****************************************************************************************************************************
	 * */
	public void initBaiduMap(MapView mapView){
		
		mapView.showZoomControls(false);//是否显示放大缩小控件
		mapView.showScaleControl(false);//是否显示标尺
	}
	
	/**
	 * 根据经纬度来搜索城市
	 */
	public void startGeoCoder(LatLng latitude_longtitude, final Handler handler, final int what){
		
		final GeoCoder mSearch = GeoCoder.newInstance();
		
		/*设置监听器*/
		mSearch.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener(){

			@Override
			public void onGetGeoCodeResult(GeoCodeResult result) {
				
				if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {					
					// 没有检索到结果
					HandlerHelper.sendString(handler, what, HandlerHelper.Fail);
				}
				
				mSearch.destroy();
			}

			@Override
			public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
				
				mSearch.destroy();
				
				System.out.println("a");
				if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
					// 没有找到检索结果
					System.out.println("bnull");
					HandlerHelper.sendString(handler, what, HandlerHelper.Fail);
					
					return;
				}
				// 获取反向地理编码结果
				System.out.println("c:城市名称：" + result.getAddressDetail().city);
				
				//实际搜索不到时候，会是""字符串
				if(result.getAddressDetail().city.equals("") || result.getAddressDetail().city == null){
					HandlerHelper.sendString(handler, what, HandlerHelper.Fail);
					
					return;
				}
	
				HandlerHelper.sendStringData(handler, what, HandlerHelper.Ok, result.getAddressDetail().city);				
				
			}
			
		});
		
		/*开始搜索*/
		mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(latitude_longtitude));
	}
	
	/**
	 *地图移动
	 */
	public void ShowMap(LatLng point, BaiduMap baiduMap) {


		// 地图移动到定位处
		MapStatus.Builder builder = new MapStatus.Builder();
		builder.target(point)// 经纬度
				.zoom(12.0f);// 显示缩放的级别

		baiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder
				.build()));
	}
	
	public void moveListener(BaiduMap baiduMap, final Handler handler, final int what){
		baiduMap.setOnMapStatusChangeListener(new OnMapStatusChangeListener() {
			
			@Override
			public void onMapStatusChangeStart(MapStatus mapStatus) {
				System.out.println("OnMapStatusChange1********************");
				System.out.println("latitude********************"+mapStatus.target.latitude);
				System.out.println("longitude********************"+mapStatus.target.longitude);
			}
			
			@Override
			public void onMapStatusChangeFinish(MapStatus mapStatus) {
				
				System.out.println("OnMapStatusChange2********************");

				System.out.println("OnMapStatusChange3********************");
				System.out.println("latitude********************"+mapStatus.target.latitude);
				System.out.println("longitude********************"+mapStatus.target.longitude);
				HandlerHelper.sendStringObject(handler, what, HandlerHelper.Ok, mapStatus.target);
				
			}
			
			@Override
			public void onMapStatusChange(MapStatus mapStatus) {
				
			}
		});
	}
	
	/**
	 *定位图层
	 */
	public void ShowLocation(LatLng point, BaiduMap baiduMap) {

		// 定位图层
		baiduMap.setMyLocationEnabled(true);// 开启定位图层
		MyLocationData locData = new MyLocationData.Builder().accuracy(20)// 半径,设置定位数据的精度信息，单位：米
																			// ，图层的面积就是你的目标的可能的位置
				.direction(100)// 此处设置开发者获取到的方向信息，顺时针0-360
				.latitude(point.latitude)// 图层的纬度
				.longitude(point.longitude)// 图层的经度
				.build();

		baiduMap.setMyLocationData(locData);
	}
	
	public void Search_NearBy(LatLng latlng){
		
		final PoiSearch mPoiSearch = PoiSearch.newInstance();
		
		OnGetPoiSearchResultListener poiListener = new OnGetPoiSearchResultListener(){  
		    public void onGetPoiResult(PoiResult result){  
		    	System.out.println("poi:");
		    	if (result == null){
		    		System.out.println("poi:"+"null");
		    		return;
		    	}System.out.println("poi:1");
		    	if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
		            //详情检索失败
		            // result.error请参考SearchResult.ERRORNO 
		    		System.out.println("poi:"+"失败");
		    		return;
		        } System.out.println("poi:2");
		    	System.out.println("poi:3size"+result.getAllPoi().get(0).address);
		    	for (int i = 0; i < result.getAllPoi().size(); i++) {
					System.out.println(""+result.getAllPoi().get(i).address);
				}
		    }  
		    public void onGetPoiDetailResult(PoiDetailResult result){ 
		    	System.out.println("poiDetail:");
		    	if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
		            //详情检索失败
		            // result.error请参考SearchResult.ERRORNO 
		    		System.out.println("poiDetail:"+"失败");
		    		return;
		        } 
		    	System.out.println("poiDetail"+result.getName());
		    	/*释放*/
		    	mPoiSearch.destroy();
		    }  
		};
		
		mPoiSearch.setOnGetPoiSearchResultListener(poiListener);
		
		mPoiSearch.searchNearby(new PoiNearbySearchOption().location(latlng).keyword("银行").radius(10000));
	}
	
	public void ReverseGeoCode(Context context, LatLng latlng, final String cityName, final Handler handler, final int what){
		
		/*判断是否有网络*/
		if(!NetworkHelper.isNetworkAvailable(context)){
			HandlerHelper.sendStringObject(handler, what, "noNet", null);
			return;
		}
		
		final GeoCoder geoCoder = GeoCoder.newInstance();
						
		OnGetGeoCoderResultListener listener = new OnGetGeoCoderResultListener() {
			public void onGetGeoCodeResult(GeoCodeResult result) {
				if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
					HandlerHelper.sendStringObject(handler, what, "fail", null);
					return;
				}
			}

			@Override
			public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
				System.out.println("a");
				
				geoCoder.destroy();
				if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
					HandlerHelper.sendStringObject(handler, what, "fail", null);
					return;
				}

				// 获取反向地理编码结果
//				System.out.println("size:" + result.getPoiList().size());
//				for (int i = 0; i < result.getPoiList().size(); i++) {
//					System.out.println(""+result.getPoiList().get(i).name);
//					System.out.println(""+result.getPoiList().get(i).address);
//				}
				//搜索到了分：没有或有
				
				List<PoiInfo> data = result.getPoiList();
				if(data == null){
					HandlerHelper.sendStringObject(handler, what, "fail", null);
					return;
				}
				
				/*判断城市是否为null*/
				if(result.getAddressDetail() == null || result.getAddressDetail().city == null || result.getAddressDetail().city.equals("")){
					HandlerHelper.sendStringObject(handler, what, "fail", null);
					return;
				}
				
				/*判断城市是否超出范围*/
				if(!result.getAddressDetail().city.equals(cityName+"市")){
					HandlerHelper.sendStringObject(handler, what, "out", null);
					return;
				}
				
				int size = 0;
				
				List<Map<String, Object>> list_data = new ArrayList<Map<String,Object>>();
				for (int i = 0; i < data.size() && size <= 10; i++) {
					
					if(data.get(i).location != null){
						
						Map<String ,Object> map = new HashMap<String, Object>();
						map.put("title", data.get(i).name);
						map.put("address", data.get(i).address);
						map.put("latitude", data.get(i).location.latitude);
						map.put("longitude", data.get(i).location.longitude);
						
						list_data.add(map);
						size = size + 1;	
					}
													
				}
				
				if(list_data.size() == 0){
					HandlerHelper.sendStringObject(handler, what, "fail", null);
				}else{
					HandlerHelper.sendStringObject(handler, what, "ok", list_data);
				}
			}
		};
		
		geoCoder.setOnGetGeoCodeResultListener(listener);
		
		geoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(latlng));
	}
	
	

}
