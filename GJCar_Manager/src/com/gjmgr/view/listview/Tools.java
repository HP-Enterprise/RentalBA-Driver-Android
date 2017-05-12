package com.gjmgr.view.listview;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.annotation.SuppressLint;

//工具类 （网络判断，时间换算，图片下载，分辨率判定等）

@SuppressLint("SimpleDateFormat")
public class Tools {

	private final static SimpleDateFormat dateFormater2 = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm");
	private final static SimpleDateFormat dateFormater = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	private final static SimpleDateFormat dataFormater3 = new SimpleDateFormat(
			"HH:mm");
	private final static SimpleDateFormat dataFormater4 = new SimpleDateFormat(
			"yyyy-MM-dd");

	/*
	 * 获取系统当前时间 add by gao yyyy-MM-dd HH:mm
	 */
	public static String getCurrentTime() {
		SimpleDateFormat tempDate = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String datetime = tempDate.format(new java.util.Date());
		return datetime;
	}

	/*
	 * 获取系统当前时间 add by gao yyyyMMddHHmm
	 */
	public static String getCurrentTime2() {
		SimpleDateFormat tempDate = new SimpleDateFormat("yyyyMMddHHmmss");
		String datetime = tempDate.format(new java.util.Date());
		return datetime;
	}

	/*
	 * 获取系统当前时间 add by gao yyyyMMddHHmm
	 */
	public static String getCurrentTime3() {
		SimpleDateFormat tempDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String datetime = tempDate.format(new java.util.Date());
		return datetime;
	}

	/*
	 * 转换时间 add by gao yyyyMMddHHmm
	 */
	public static String getCurrentTim4(String time) {
		SimpleDateFormat tempDate = new SimpleDateFormat("MM-dd HH:mm");
		String datetime = tempDate
				.format(new Date(Long.parseLong(time) / 1000));
		return datetime;
	}

	// 时间戳转化为字符串
	public static String gettimeBychu(String time) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		String sd = sdf.format(new Date(Long.parseLong(time) * 1000));

		return sd;
	}

	// 以友好方式显示时间
	public static String distanceBetweenCurren(String sdata) {
		String time = "";
		long time_temp = Long.parseLong(sdata) * 1000;
		long time_distance = System.currentTimeMillis() - time_temp;
		String day_temp = dataFormater4.format(new Date(time_temp));
		String day_current = dataFormater4.format(new Date(System
				.currentTimeMillis()));

		// 判断是否是同一天
		if (day_temp.equals(day_current)) {
			if ((int) (time_distance / 3600000) == 0) {
				time = Math.max(time_distance / 60000, 1) + "分钟前";
			} else {
				time = Math.max(time_distance / 3600000, 1) + "小时前";
			}
		}
		int days = (int) (System.currentTimeMillis() / 86400000 - time_temp / 86400000);
		if (days == 1) {
			time = "昨天  " + dataFormater3.format(time_temp);
		}
		if (days >= 2) {
			time = dateFormater2.format(time_temp);
		}
		return time;
	}

	/**
	 * 以友好的方式显示时间
	 * 
	 * @param sdate
	 * @return
	 */
	public static String friendly_time(String sdate) {
		Date time = toDate(getTime(sdate));
		if (time == null) {
			return "Unknown";
		}
		String ftime = "";
		Calendar cal = Calendar.getInstance();

		// 判断是否是同一天
		String curDate = dateFormater2.format(cal.getTime());
		String paramDate = dateFormater2.format(time);
		if (curDate.equals(paramDate)) {
			int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
			if (hour == 0)
				ftime = Math.max(
						(cal.getTimeInMillis() - time.getTime()) / 60000, 1)
						+ "分钟前";
			else
				ftime = hour + "小时前";
			return ftime;
		}

		long lt = time.getTime() / 86400000;
		long ct = cal.getTimeInMillis() / 86400000;
		int days = (int) (ct - lt);
		if (days == 0) {
			int hour = (int) ((cal.getTimeInMillis() / 3600000 - time.getTime() / 3600000));
			if (hour == 0)
				ftime = Math
						.max((cal.getTimeInMillis() / 60000 - time.getTime()) / 60000,
								1)
						+ "分钟前";
			else
				ftime = hour + "小时前";
		} else if (days == 1) {
			cal.setTimeInMillis(time.getTime());
			ftime = "昨天  " + dataFormater3.format(cal.getTime());
		} else if (days == 2) {
			ftime = "前天";
		}
		// else if(days > 2 && days <= 10){
		// ftime = days+"天前";
		// }
		else if (days > 2) {
			ftime = dateFormater2.format(time);
		}
		return ftime;
	}

	public static String getTime(String time) {
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(Long.parseLong(time));
		return formatter.format(calendar.getTime());
	}

	/**
	 * 将字符串转位日期类型
	 * 
	 * @param sdate
	 * @return
	 */
	public static Date toDate(String sdate) {
		try {
			return dateFormater.parse(sdate);
		} catch (ParseException e) {
			return null;
		}
	}

}
