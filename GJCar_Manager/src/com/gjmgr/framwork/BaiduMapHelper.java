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

	/* ��һ������λ */
	private LocationClient mLocClient;
	private MyLocationListenner myListener = new MyLocationListenner();
	private boolean isFirstLoc = true;
	private LatLng latitude_longtitude = null;
	
	/*��Ϣ*/
	private Handler handler;
	private int msg;
	
	public void startLocationClient(Context context, Handler handler, int msg){
		
		this.handler = handler;
		this.msg = msg;
		
		// ��������
		mLocClient = new LocationClient(context);
		mLocClient.registerLocationListener(myListener);
		
		/*��һ��������GPS����*/
		initGPSOption();
		
		/*�ڶ�����������λ*/
		mLocClient.start();
	}
	
	/**
	 * ��һ��������GPS����
	 */
	private void initGPSOption() {

		// ��λ��ʼ��
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// ��gps
		option.setCoorType("bd09ll"); // ������������
		option.setIsNeedAddress(true);//��ѡ�������Ƿ���Ҫ��ַ��Ϣ��Ĭ�ϲ���Ҫ
		option.setScanSpan(1000);
		option.setTimeOut(10000);
		mLocClient.setLocOption(option);		
		System.out.println("2**************");
	}

	/**
	 * ��λSDK��������
	 */
	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// map view ���ٺ��ڴ����½��յ�λ��
			// System.out.println("��λʧ��0");
			if (location == null) {
				System.out.println("��λʧ��1");
				
				HandlerHelper.sendStringObject(handler, msg, "fail", null);
				return;
			}

			if (isFirstLoc) {
				System.out.println("��λ�ɹ�^^^^^^^^^^^^^^^^^^^^^^^^");
				isFirstLoc = false;
				System.out.println("location.getLatitude()"+ location.getLatitude());
				System.out.println("location.getLongtitude()"+ location.getLongitude());
				
				LocationMessage locationMessage = new LocationMessage();				
				locationMessage.Latitude = location.getLatitude();
				locationMessage.Longitude = location.getLongitude();
				locationMessage.address = location.getAddrStr();
				if(location.getAddrStr() == null || location.getAddrStr().equals("")){
					
				}else{
					System.out.println("��ַ"+location.getAddrStr());
				}

				mLocClient.stop();
				HandlerHelper.sendStringObject(handler, msg, "ok",locationMessage);
			}
		}

		public void onReceivePoi(BDLocation poiLocation) {
			System.out.println("��λʧ��3");
			HandlerHelper.sendString(handler, msg, HandlerHelper.Fail);
		}
	}

	/****************************************************************************************************************************
	 * ****************************************************************************************************************************
	 * 
	 * ��ͼ��ز���
	 * 	
	 * ****************************************************************************************************************************
	 * ****************************************************************************************************************************
	 * */
	public void initBaiduMap(MapView mapView){
		
		mapView.showZoomControls(false);//�Ƿ���ʾ�Ŵ���С�ؼ�
		mapView.showScaleControl(false);//�Ƿ���ʾ���
	}
	
	/**
	 * ���ݾ�γ������������
	 */
	public void startGeoCoder(LatLng latitude_longtitude, final Handler handler, final int what){
		
		final GeoCoder mSearch = GeoCoder.newInstance();
		
		/*���ü�����*/
		mSearch.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener(){

			@Override
			public void onGetGeoCodeResult(GeoCodeResult result) {
				
				if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {					
					// û�м��������
					HandlerHelper.sendString(handler, what, HandlerHelper.Fail);
				}
				
				mSearch.destroy();
			}

			@Override
			public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
				
				mSearch.destroy();
				
				System.out.println("a");
				if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
					// û���ҵ��������
					System.out.println("bnull");
					HandlerHelper.sendString(handler, what, HandlerHelper.Fail);
					
					return;
				}
				// ��ȡ������������
				System.out.println("c:�������ƣ�" + result.getAddressDetail().city);
				
				//ʵ����������ʱ�򣬻���""�ַ���
				if(result.getAddressDetail().city.equals("") || result.getAddressDetail().city == null){
					HandlerHelper.sendString(handler, what, HandlerHelper.Fail);
					
					return;
				}
	
				HandlerHelper.sendStringData(handler, what, HandlerHelper.Ok, result.getAddressDetail().city);				
				
			}
			
		});
		
		/*��ʼ����*/
		mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(latitude_longtitude));
	}
	
	/**
	 *��ͼ�ƶ�
	 */
	public void ShowMap(LatLng point, BaiduMap baiduMap) {


		// ��ͼ�ƶ�����λ��
		MapStatus.Builder builder = new MapStatus.Builder();
		builder.target(point)// ��γ��
				.zoom(12.0f);// ��ʾ���ŵļ���

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
	 *��λͼ��
	 */
	public void ShowLocation(LatLng point, BaiduMap baiduMap) {

		// ��λͼ��
		baiduMap.setMyLocationEnabled(true);// ������λͼ��
		MyLocationData locData = new MyLocationData.Builder().accuracy(20)// �뾶,���ö�λ���ݵľ�����Ϣ����λ����
																			// ��ͼ�������������Ŀ��Ŀ��ܵ�λ��
				.direction(100)// �˴����ÿ����߻�ȡ���ķ�����Ϣ��˳ʱ��0-360
				.latitude(point.latitude)// ͼ���γ��
				.longitude(point.longitude)// ͼ��ľ���
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
		            //�������ʧ��
		            // result.error��ο�SearchResult.ERRORNO 
		    		System.out.println("poi:"+"ʧ��");
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
		            //�������ʧ��
		            // result.error��ο�SearchResult.ERRORNO 
		    		System.out.println("poiDetail:"+"ʧ��");
		    		return;
		        } 
		    	System.out.println("poiDetail"+result.getName());
		    	/*�ͷ�*/
		    	mPoiSearch.destroy();
		    }  
		};
		
		mPoiSearch.setOnGetPoiSearchResultListener(poiListener);
		
		mPoiSearch.searchNearby(new PoiNearbySearchOption().location(latlng).keyword("����").radius(10000));
	}
	
	public void ReverseGeoCode(Context context, LatLng latlng, final String cityName, final Handler handler, final int what){
		
		/*�ж��Ƿ�������*/
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

				// ��ȡ������������
//				System.out.println("size:" + result.getPoiList().size());
//				for (int i = 0; i < result.getPoiList().size(); i++) {
//					System.out.println(""+result.getPoiList().get(i).name);
//					System.out.println(""+result.getPoiList().get(i).address);
//				}
				//�������˷֣�û�л���
				
				List<PoiInfo> data = result.getPoiList();
				if(data == null){
					HandlerHelper.sendStringObject(handler, what, "fail", null);
					return;
				}
				
				/*�жϳ����Ƿ�Ϊnull*/
				if(result.getAddressDetail() == null || result.getAddressDetail().city == null || result.getAddressDetail().city.equals("")){
					HandlerHelper.sendStringObject(handler, what, "fail", null);
					return;
				}
				
				/*�жϳ����Ƿ񳬳���Χ*/
				if(!result.getAddressDetail().city.equals(cityName+"��")){
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
