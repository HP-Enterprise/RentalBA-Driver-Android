package com.gjmgr.data.data;

import java.util.Date;

import android.support.v4.view.ViewPager;

import com.gjmgr.activity.user.Activity_Order_AllList;
import com.gjmgr.data.bean.Order;
import com.gjmgr.data.bean.Order_Down;
import com.gjmgr.data.bean.Order_Up;

public class Public_Param {

	public static Order_Up order_up = new Order_Up();
	public static Order_Down order_down = new Order_Down();
	
	public static Order order;

	public static Activity_Order_AllList activity;
	
	/**�汾��Ϣ*/
	public static String Version_Name = "";
	public static String Version_Content = "";
	
	/*��������*/
	public static String orderId_detail;
	
	/*����ѡ��*/
	public static boolean isSelected = false;
	public static String selected_modelId;
	
	/*�ᳵ����*/
	public static String plate;//������ʾ
	
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
	
	/*����*/
	public static String model_ok;
	
	public static String myModel;
	
	public static int returncar_Melliage = 0;
	public static int takecar_Melliage = 0;
	public static Long returncar_date = 0l;
	
	/*��ת*/
	public static ViewPager viewPager;
	public static boolean isReturnCarOK = false;
}