package com.gjmgr.data.data;

import java.util.Date;

import android.support.v4.view.ViewPager;

import com.gjmgr.activity.user.Activity_Order_AllList;
import com.gjmgr.data.bean.Order;
import com.gjmgr.data.bean.Order_Down;
import com.gjmgr.data.bean.Order_Road;
import com.gjmgr.data.bean.Order_Up;

public class Public_Param {

	public static Order_Up order_up = new Order_Up();
	public static Order_Down order_down = new Order_Down();
	
	public static Order order;

	public static Activity_Order_AllList activity;
	
	/**版本信息*/
	public static String Version_Name = "";
	public static String Version_Content = "";
	
	/*订单详情*/
	public static String orderId_detail;
	
	/*车型选择*/
	public static boolean isSelected = false;
	public static String selected_modelId;
	
	/*提车参数*/
	public static String plate;//用于显示
	
	public static String orderId;
	
	public static String vendorId;
	public static String modelId;
	public static String model;
	public static String vehicleId;
	
	public static int takecar_orderType = 0;
	public static int upcar_orderType = 0;
	public static int downcar_orderType = 0;
	public static int return_orderType = 0;
	public static int orderdeaail_orderType = 0;

	/*详情*/
	public static String model_ok;
	
	public static String myModel;
	
	public static int returncar_Melliage = 0;
	public static int takecar_Melliage = 0;
	public static Long returncar_date = 0l;
	
	public static String doortodoor_upaddress;//门到门详情
	public static String doortodoor_downaddress;
	
	public static String doortodoor_typename;
	public static Integer doortodoor_type;
	public static String doortodoor_time;
	
	/*跳转*/
	public static ViewPager viewPager;
	public static boolean isReturnCarOK = false;
	
	public static int Way_Order_Add = 1;
	public static int Way_Order_Edit = 2;
	public static Order_Road order_edit;
	
	/*路单参数*/
	public static String road_vehicleId;
	public static int road_type = 0;
	public static String road_orderId;
	
	public static int Way_Doing = 1;
	public static int Way_Ok = 2;
	public static int way = 0;
}
