package com.gjmgr.data.data;

public class Public_Api {
	
	/****************************************************************************************************************************************
	** app                     **************************************************************************************************************
	*****************************************************************************************************************************************/
	//public static String apkUrl = "http://p.gdown.baidu.com/822f7de6274e58ecce2656045e1ba7d7158e184e9a59279abaaf4f3019b0977f7e99707ca42bad4489d1771c058e7c89d297fdd6ec090d1adc455087f1f7ca68bcd62ce634710f74e678651322e6f6a97d4b2d946616f64247bf48c4355f1d445ed45048fe81e417050799224f0157ba9ed80a7afdb60ee640dfc1ab711960ac573dbc3eda94218f7071afd6ca562a6670dc444058abfa79d005f1ba72df4eee0f73f6865ffab0984525c578d42752cafb2dbde202b29469463a036cae66d9e16e05700e3e5fd4f6355bae1781a2503dc92812bb063d1e71c6e405bd5483f7ba1538926fee90cd00dc09dfeaa018c8582b4fb761ab47c5816c678bf8b572d7d75f6a7ad62a18a82b030a519323039f3a263a75c6522f8f9ce0f1c4166cef4df2b3c902393a579ec9";

	/****************************************************************************************************************************************
	** Ip:port                      **************************************************************************************************************
	*****************************************************************************************************************************************/
	
	/* 网址*/
//	public static String appWebSite = "http://10.0.12.202:41234/";
//	public static String appWebSite = "http://10.0.12.199:51234/";
//	public static String appWebSite = "http://121.40.157.200:41234/";
																			
//	public static String appWebSite = "http://10.0.12.178:41234/";
//	public static String appWebSite = "http://182.61.22.80";//:8081
	public static String appWebSite = "http://www.gjcar.com/";
//	public static String appWebSite = "http://182.61.35.59:51234/";
//	public static String appWebSite = "http://182.61.22.80/";
//	public static String appWebSite_test = "http://182.61.22.80/";
	public static String appWebSite_test = "http://www.gjcar.com/";
//	public static String appWebSite = "http://gjwap.ngrok.cc";
	//	public static String appWebSite = "http://www.rental.hpecar.com/";
	/******************************************************************************************************************************************
	** api                     **************************************************************************************************************
	*****************************************************************************************************************************************/	

	/**一。用户信息*/
	
	/* 发送短信*/
	public static String api_smsCode = "api/verify";
	
	/* 登录*/
	public static String api_login = "api/dispatch/city-driver/login";
	public static String api_updatepwd = "api/dispatch/city-driver/password";
	
	/**二。产品信息*/
	
	public static String api_task_list = "api/dispatch/task-list";//任务列表
	
	public static String api_task_confirm = "api/dispatch/task-confirm";//确定任务
	public static String api_task_reject = "api/dispatch/task-reject";//拒绝任务
	
	public static String api_task_doing = "api/dispatch/task-doing";//执行任务
	
}
