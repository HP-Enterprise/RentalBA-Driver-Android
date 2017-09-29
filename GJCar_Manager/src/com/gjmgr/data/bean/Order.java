package com.gjmgr.data.bean;

import java.util.ArrayList;
import java.util.List;


/*** 
* 
* Json To JavaBean
* @author www.json123.com
* 
*/
public class Order{
	
	public	Integer	id;	/*1*/	
	//public	String	orderId;	/*1000001*/
	public	String	orderCode;//在司机任务是orderCode，在合同和其他就是orderId
	public	String	dispatchStatus;//Integer dispatchStatus 调度状态 10=待分配，20=已分配，30=已确认，40=已拒绝，50=已完成，60=已取消
	public	String dispatchOrigin;//调度来源 1=门到门订单，2=门店调拨 3=接送机订单	
	public	Integer task_type; //任务类型，1=送车，2=取车,3=接送机
	public	Integer orderType; //订单类型，1=短租自驾；2=门到门服务；3=短租代驾; 4=接送机
	public	String	expectStartTime;	/*2016-01-11 05:00:00*/
	public	String	expectEndTime;	/*2016-04-11 15:00:00*/
	public	String	expectGetOnAddress;	/*武汉*/
	public	String	expectGetOffAddress;	/*武汉*/
	public	String	driverId;//司机Id
	public	String	customerAddress;
	public	String	modelName;
	public	String	vehicleId;//车辆id
	public  Integer taskType; //任务类型，1=送车，2=取车
	
	public  String callOutStoreName;
	
	public  String airport;
	
	public Integer takeCarMileage;//取车里程
	public Integer clientTakeCarMileage;
	public Integer clientReturnCarMileage;
	public Integer returnCarMileage;
	public Integer driverTakeCarMileage;
	
	public Long createDate;
	public Long takeCarDate;
	public Long takeCarActualDate;//实际取车时间
	public Long clientTakeCarDate;
	public Long clientReturnCarDate;
	public Long returnCarActualDate;//
	public Long driverTakeCarDate;
	
	public String takeCarAddress;
	public String clientTakeCarAddress;
	public String clientReturnCarAddress;
	public String returnCarAddress;
	
	public String takeCarFuel;//司机从门店取车的油量
	public String clientTakeCarFuel;//用户拿到车子的油量
	public String clientUpFuel;//上车油量
	public Integer clientUpMileage;//上车里程
	public Long clientUpCarDate;//上车时间
	
	public String returnCarFuel;
	public String clientReturnCarFuel;//司机从客户取车 = 用户还车的油量
	public String clientDownFuel;//下车油量
	public Integer clientDownMileage;//下车里程
	public String clientActualDebusAddress;
	public String hasContract;//是否生产了合同
	
	public TransferPointShow transferPointShow;
	public Integer tripType;//1接机2送机
	public String tripAddress;
	
	public Long realStartTime;//实际上车时间
	public Long realEndTime;
	
	public String orderTypeName;
//	public String tripDistance;
//	public String airLineCompany;
//	public String flightNumber;
	
	public String driverTakeCarFuel;
	public String returnCarFuelString;
		
} 
