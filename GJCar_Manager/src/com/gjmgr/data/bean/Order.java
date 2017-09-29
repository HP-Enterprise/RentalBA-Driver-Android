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
	public	String	orderCode;//��˾��������orderCode���ں�ͬ����������orderId
	public	String	dispatchStatus;//Integer dispatchStatus ����״̬ 10=�����䣬20=�ѷ��䣬30=��ȷ�ϣ�40=�Ѿܾ���50=����ɣ�60=��ȡ��
	public	String dispatchOrigin;//������Դ 1=�ŵ��Ŷ�����2=�ŵ���� 3=���ͻ�����	
	public	Integer task_type; //�������ͣ�1=�ͳ���2=ȡ��,3=���ͻ�
	public	Integer orderType; //�������ͣ�1=�����Լݣ�2=�ŵ��ŷ���3=�������; 4=���ͻ�
	public	String	expectStartTime;	/*2016-01-11 05:00:00*/
	public	String	expectEndTime;	/*2016-04-11 15:00:00*/
	public	String	expectGetOnAddress;	/*�人*/
	public	String	expectGetOffAddress;	/*�人*/
	public	String	driverId;//˾��Id
	public	String	customerAddress;
	public	String	modelName;
	public	String	vehicleId;//����id
	public  Integer taskType; //�������ͣ�1=�ͳ���2=ȡ��
	
	public  String callOutStoreName;
	
	public  String airport;
	
	public Integer takeCarMileage;//ȡ�����
	public Integer clientTakeCarMileage;
	public Integer clientReturnCarMileage;
	public Integer returnCarMileage;
	public Integer driverTakeCarMileage;
	
	public Long createDate;
	public Long takeCarDate;
	public Long takeCarActualDate;//ʵ��ȡ��ʱ��
	public Long clientTakeCarDate;
	public Long clientReturnCarDate;
	public Long returnCarActualDate;//
	public Long driverTakeCarDate;
	
	public String takeCarAddress;
	public String clientTakeCarAddress;
	public String clientReturnCarAddress;
	public String returnCarAddress;
	
	public String takeCarFuel;//˾�����ŵ�ȡ��������
	public String clientTakeCarFuel;//�û��õ����ӵ�����
	public String clientUpFuel;//�ϳ�����
	public Integer clientUpMileage;//�ϳ����
	public Long clientUpCarDate;//�ϳ�ʱ��
	
	public String returnCarFuel;
	public String clientReturnCarFuel;//˾���ӿͻ�ȡ�� = �û�����������
	public String clientDownFuel;//�³�����
	public Integer clientDownMileage;//�³����
	public String clientActualDebusAddress;
	public String hasContract;//�Ƿ������˺�ͬ
	
	public TransferPointShow transferPointShow;
	public Integer tripType;//1�ӻ�2�ͻ�
	public String tripAddress;
	
	public Long realStartTime;//ʵ���ϳ�ʱ��
	public Long realEndTime;
	
	public String orderTypeName;
//	public String tripDistance;
//	public String airLineCompany;
//	public String flightNumber;
	
	public String driverTakeCarFuel;
	public String returnCarFuelString;
		
} 
