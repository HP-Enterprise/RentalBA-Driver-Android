package com.gjmgr.data.bean;

public class Car_Veichel {

	/*展示信息*/
	public String plate;//车牌号,沪SH0063

	public VehicleModelShow vehicleModelShow ;//model车型:豪华GL8-2.0CT
	public String colour;//颜色绿色
	public String mileage;//12当前里程数

	public String state;//车辆状态(unused:未投入运营  ready:待租赁  rented:租赁中 locked:已失效 unavailable: 不可用)

	/*参数*/
	public String id;
	public String modelId;
}
