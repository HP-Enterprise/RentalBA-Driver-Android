package com.gjmgr.view.listview;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.annotation.SuppressLint;

//������ �������жϣ�ʱ�任�㣬ͼƬ���أ��ֱ����ж��ȣ�

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
	 * ��ȡϵͳ��ǰʱ�� add by gao yyyy-MM-dd HH:mm
	 */
	public static String getCurrentTime() {
		SimpleDateFormat tempDate = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String datetime = tempDate.format(new java.util.Date());
		return datetime;
	}

	/*
	 * ��ȡϵͳ��ǰʱ�� add by gao yyyyMMddHHmm
	 */
	public static String getCurrentTime2() {
		SimpleDateFormat tempDate = new SimpleDateFormat("yyyyMMddHHmmss");
		String datetime = tempDate.format(new java.util.Date());
		return datetime;
	}

	/*
	 * ��ȡϵͳ��ǰʱ�� add by gao yyyyMMddHHmm
	 */
	public static String getCurrentTime3() {
		SimpleDateFormat tempDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String datetime = tempDate.format(new java.util.Date());
		return datetime;
	}

	/*
	 * ת��ʱ�� add by gao yyyyMMddHHmm
	 */
	public static String getCurrentTim4(String time) {
		SimpleDateFormat tempDate = new SimpleDateFormat("MM-dd HH:mm");
		String datetime = tempDate
				.format(new Date(Long.parseLong(time) / 1000));
		return datetime;
	}

	// ʱ���ת��Ϊ�ַ���
	public static String gettimeBychu(String time) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		String sd = sdf.format(new Date(Long.parseLong(time) * 1000));

		return sd;
	}

	// ���Ѻ÷�ʽ��ʾʱ��
	public static String distanceBetweenCurren(String sdata) {
		String time = "";
		long time_temp = Long.parseLong(sdata) * 1000;
		long time_distance = System.currentTimeMillis() - time_temp;
		String day_temp = dataFormater4.format(new Date(time_temp));
		String day_current = dataFormater4.format(new Date(System
				.currentTimeMillis()));

		// �ж��Ƿ���ͬһ��
		if (day_temp.equals(day_current)) {
			if ((int) (time_distance / 3600000) == 0) {
				time = Math.max(time_distance / 60000, 1) + "����ǰ";
			} else {
				time = Math.max(time_distance / 3600000, 1) + "Сʱǰ";
			}
		}
		int days = (int) (System.currentTimeMillis() / 86400000 - time_temp / 86400000);
		if (days == 1) {
			time = "����  " + dataFormater3.format(time_temp);
		}
		if (days >= 2) {
			time = dateFormater2.format(time_temp);
		}
		return time;
	}

	/**
	 * ���Ѻõķ�ʽ��ʾʱ��
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

		// �ж��Ƿ���ͬһ��
		String curDate = dateFormater2.format(cal.getTime());
		String paramDate = dateFormater2.format(time);
		if (curDate.equals(paramDate)) {
			int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
			if (hour == 0)
				ftime = Math.max(
						(cal.getTimeInMillis() - time.getTime()) / 60000, 1)
						+ "����ǰ";
			else
				ftime = hour + "Сʱǰ";
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
						+ "����ǰ";
			else
				ftime = hour + "Сʱǰ";
		} else if (days == 1) {
			cal.setTimeInMillis(time.getTime());
			ftime = "����  " + dataFormater3.format(cal.getTime());
		} else if (days == 2) {
			ftime = "ǰ��";
		}
		// else if(days > 2 && days <= 10){
		// ftime = days+"��ǰ";
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
	 * ���ַ���תλ��������
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
