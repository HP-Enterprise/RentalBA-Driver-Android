package com.gjmgr.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import com.gjmgr.activity.user.Servcie_Gps;
import com.gjmgr.data.data.Public_SP;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.util.TimeUtils;

public class TimeHelper {
	
	
	
	/**ʱ���ת��Ϊʱ��
	 */
	public static String getTime(String timeCurrent){
		String format = "yyyy-MM-dd HH:mm:ss";  
        SimpleDateFormat sdf = new SimpleDateFormat(format); 
       
        return sdf.format(new Date(Long.valueOf(timeCurrent)));  
	}
	
	/**
	 * 1000137-201608080315
	 */
	public static String getTradeCode(String orderId){
		Date nowDate = new Date();
		SimpleDateFormat format_now = new SimpleDateFormat("yyyyMMddHHmmss");
		String misdate = format_now.format(nowDate);
		
		return orderId+"-"+misdate;
	}
	
	/**08:00 --- 8
	 */
	public static int getTime2_Number(String time){

		String hour = time.split(":")[0];
		int h = Integer.parseInt(hour);
		return h;
	}
	
	/**
	 * ʱ���ʽת����"2014-03-05 12:03:19" "03-05"
	 * 
	 * @param time
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static String getDateTime_YM(String time) {

		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			Date date = sdf.parse(time);

			SimpleDateFormat format = new SimpleDateFormat("MM-dd");
			String str_time = format.format(date);
			return str_time;
		} catch (ParseException e) {
			
			e.printStackTrace();
		}
		return "";

	}
	/**
	 * ʱ���ʽת����"2014-03-05 12:03:19" "2014-03-05+12:03:1"
	 * 
	 * @param time
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static String getTicketStart_EndTime(String mytime) {

		String time = "";
		
		String[] split = mytime.split(" ");
		
		time = split[0]+"+"+split[1];
		
		return time;

	}
	/**
	 * 2016-09-01 -------3����ȡ�ܼ������֣�3�����ܶ������տ�ʼ��
	 */
	public static int getDayOfWeek(String datetime){

		/* ��ȡʱ�� */
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		try {
			date = format.parse(datetime);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);

		/* ��ȡ���� */
		int week = cal.get(Calendar.DAY_OF_WEEK); // �õ�����
		
		return week;
	}
	
	/**
	 * ʱ���ʽת����"2014-03-05 12:03:19" "03-05 ����"
	 * 
	 * @param time
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static String getDateWeekTime(String datetime) {

		/* ��ȡʱ�� */
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date date = new Date();
		try {
			date = format.parse(datetime);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);

		/* ��ȡʱ�� */
		SimpleDateFormat formattime = new SimpleDateFormat("MM-dd");
		String time = formattime.format(date);

		/* ��ȡ���� */
		int week = cal.get(Calendar.DAY_OF_WEEK); // �õ�����
		String weekString = "��";

		switch (week) {

		case 1:
			weekString += "��";
			break;

		case 2:
			weekString += "һ";
			break;

		case 3:
			weekString += "��";
			break;

		case 4:
			weekString += "��";
			break;

		case 5:
			weekString += "��";
			break;

		case 6:
			weekString += "��";
			break;

		case 7:
			weekString += "��";
			break;

		default:
			break;

		}

		return time + " " + weekString;
	}

	/**
	 * ʱ���ʽת����"2014-03-05 12:03:19" "���� 19:00"
	 * 
	 * @param time
	 * @return
	 */
	public static String getWeekTime(String datetime) {

		/* ��ȡʱ�� */
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date date = new Date();
		try {
			date = format.parse(datetime);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);

		/* ��ȡʱ�� */
		SimpleDateFormat formattime = new SimpleDateFormat("HH:mm");
		String time = formattime.format(date);

		/* ��ȡ���� */
		int week = cal.get(Calendar.DAY_OF_WEEK); // �õ�����
		String weekString = "��";

		switch (week) {

		case 1:
			weekString += "��";
			break;

		case 2:
			weekString += "һ";
			break;

		case 3:
			weekString += "��";
			break;

		case 4:
			weekString += "��";
			break;

		case 5:
			weekString += "��";
			break;

		case 6:
			weekString += "��";
			break;

		case 7:
			weekString += "��";
			break;

		default:
			break;

		}

		return weekString + " " + time;
	}

	/**
	 * ��ȡʱ��"2016-03-05 08:00��ʱ�䰴��
	 * 
	 * @param time
	 * @return
	 */
	public static String getSearchTime_Mis(String datetime) {

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		long starttime = 0;
		try {
			starttime = format.parse(datetime).getTime();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return new Long((starttime / 1000) * 1000).toString();

	}

	/**
	 * ��ȡʱ��"2016-03-05"�ĺ�n���ʱ���
	 * 
	 * @param time
	 * @return
	 */
	public static String getSearchTime_YMD(int addDays) {

		Date date = new Date();
		date.setDate(date.getDate() + addDays);
		date.setHours(10);
		date.setMinutes(0);
		date.setSeconds(0);
		return new Long((date.getTime() / 1000) * 1000).toString();

	}

	/**
	 * ��ȡʱ��"2016-03-05"�ĺ�n��
	 * 
	 * @param time
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static Date getTakeTime_YMD(int addDays) {

		Date date = new Date();
		date.setDate(date.getDate() + addDays);

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String str_time = format.format(date) + " " + "10:00:00";

		SimpleDateFormat takeformat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");

		Date takedate = new Date();
		try {
			takedate = takeformat.parse(str_time);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return takedate;

	}

	/**
	 * ��ȡʱ��"2016-03-05 10:00"�ĺ�n��
	 * 
	 * @param time
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static Date getReturnTime_YMD(int addDays, String time) {

		SimpleDateFormat returnformat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

		Date returndate = new Date();
		try {
			returndate = returnformat.parse(time);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		returndate.setDate(returndate.getDate() + addDays);

		return returndate;

	}

	/**
	 * ��ȡʱ��"2016-08-09" �õ�Date
	 * 
	 * @param time
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static Date getTransferDate(String mytime) {
		
		SimpleDateFormat returnformat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

		Date returndate = new Date();
		try {
			returndate = returnformat.parse(mytime);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		returndate.setDate(returndate.getDate());

		return returndate;

	}
	/**
	 * ��ȡʱ��"2016-08-09 23:00" �õ�Date
	 * 
	 * @param time
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static Date getDriveDate(String mytime) {
		
		SimpleDateFormat returnformat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

		Date returndate = new Date();
		try {
			returndate = returnformat.parse(mytime);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		returndate.setDate(returndate.getDate());

		return returndate;

	}
	/**
	 * ��ȡʱ��"12:32" �õ�Date
	 * 
	 * @param time
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static Date getTransferTime(String mytime) {

		Date nowDate = new Date();
		SimpleDateFormat format_now = new SimpleDateFormat("yyyy-MM-dd");
		String date = format_now.format(nowDate);
		
		String time = date+" "+mytime;
		
		SimpleDateFormat returnformat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

		Date returndate = new Date();
		try {
			returndate = returnformat.parse(time);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		returndate.setDate(returndate.getDate());

		return returndate;

	}
	
	public static String getNowTime_YMDHM() {

		Date nowDate = new Date();
		SimpleDateFormat format_now = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String time = format_now.format(nowDate);
		
		return time;
	}
	
	public static String getNowTime_YMD() {

		Date nowDate = new Date();
		SimpleDateFormat format_now = new SimpleDateFormat("yyyy-MM-dd");
		String time = format_now.format(nowDate);
		
		return time;
	}
	public static String getNowTime_Hm() {

		Date nowDate = new Date();
		SimpleDateFormat format_now = new SimpleDateFormat("HH:mm");
		String time = format_now.format(nowDate);
		
		return time;
	}
	
	/**
	 * ��ȡ��ǰʱ�䣬"2016-03-05"�ĺ�n��
	 * 
	 * @param time
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static String getDateTime_YMD(int addDays) {

		Date date = new Date();
		date.setDate(date.getDate() + addDays);

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String str_time = format.format(date);
		return str_time;

	}

	/**
	 * ��ǰʱ��ĺ�n�죬 03-05
	 * 
	 * @param time
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static String getDateTime_MD_XDays(int addDays) {

		Date date = new Date();
		date.setDate(date.getDate() + addDays);

		SimpleDateFormat format = new SimpleDateFormat("MM-dd");
		String str_time = format.format(date);
		return str_time;

	}

	/**
	 * ��ǰʱ��ĺ�n�� ��Ȼ��ͨ��getHourTime_tenת��Ϊ ���� 10:00
	 * 
	 * @param time
	 * @return
	 */
//	@SuppressLint("SimpleDateFormat")
//	public static String getDateTime_WT_XDays(int addDays) {
//
//		Date date = new Date();
//		date.setDate(date.getDate() + addDays);
//
//		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		String str_time = format.format(date);
//
//		String time = getHourTime_ten(str_time,hourTime);
//
//		return time;
//
//	}
	/**
	 * ��ǰʱ��ĺ�n�� ��Ȼ��ͨ��getHourTime_tenת��Ϊ ���� 10:00
	 * 
	 * @param time
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static String getDateTime_WT_XDays2(int addDays,String hourTime) {

		Date date = new Date();
		date.setDate(date.getDate() + addDays);

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String str_time = format.format(date)+" "+hourTime;System.out.println("str_time"+str_time);

		String time = getHourTime_ten(str_time,hourTime);System.out.println("time"+time);

		return time;

	}
	/**
	 * ��ȡʱ��"2016-03-05 13:09"�ĺ�n��
	 * 
	 * @param time
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static String getDateTime_YMD_XDays(int addDays, String time) {

		SimpleDateFormat returnformat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

		Date returndate = new Date();
		try {
			returndate = returnformat.parse(time);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		returndate.setDate(returndate.getDate() + addDays);

		return returnformat.format(returndate);

	}
	/**
	 * ��ȡʱ��"2016-03-05 13:09"�ĺ�n��
	 * 
	 * @param time
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static String getDateTime_Free_XDays(int addDays, String time) {

		SimpleDateFormat returnformat = new SimpleDateFormat("yyyy-MM-dd");

		Date returndate = new Date();
		try {
			returndate = returnformat.parse(time);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		returndate.setDate(returndate.getDate() + addDays);

		return returnformat.format(returndate);

	}
	/**
	 * ʱ���ʽת����"2014-03-05 12:03" "2014-03-05"
	 * 
	 * @param time
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static String get_YMD_Time(String time) {

		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			Date date = sdf.parse(time);

			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			String str_time = format.format(date);
			return str_time;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";

	}

	/**
	 * ʱ���ʽת����"2014-03-05 12:03" "20140305"
	 * 
	 * @param time
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static String get_YMD_Time2(String time) {

		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			Date date = sdf.parse(time);

			SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
			String str_time = format.format(date);
			return str_time;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";

	}
	
	/**
	 * ʱ���ʽת����"2014-03-05 12:03:19" "03-05"
	 * 
	 * @param time
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static String getDateTime(String time) {

		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = sdf.parse(time);

			SimpleDateFormat format = new SimpleDateFormat("MM-dd");
			String str_time = format.format(date);
			return str_time;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";

	}

	/**
	 * ��ȡ��ǰʱ��
	 * 
	 * @param time
	 * @return
	 */
	public static String getNowDateTime() {

		Date date = new Date();
		SimpleDateFormat fromat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return fromat.format(date);
		
	}
	
	/**
	 * ʱ���ʽת����"2014-03-05 12:03:19" "03-05"
	 * 
	 * @param time
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static boolean isGps(Context context) {
		
		String time = SharedPreferenceHelper.getString(context, Public_SP.Gps, "gps_time");
		System.out.println("time-------"+time);
		if(time.equals("")){
			
			return false;
		}
		
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = sdf.parse(time);
			Date nowDate = new Date();
			
			if((nowDate.getTime()- date.getTime()) > 100*1000){
				
				return false;
			}else{
				
				return true;
			}

		} catch (ParseException e) {
			
			e.printStackTrace();
			return false;
		}

	}
	
	/**
	 * ʱ���ʽת����"2014-03-05 12:03:19" "���� 10��00"
	 * 
	 * @param time
	 * @return
	 */
	public static String getHourTime_ten(String time,String hourTime) {

		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			Date date = sdf.parse(time);

			Calendar cal = Calendar.getInstance();
			cal.setTime(date);

			/* ��ȡ���� */
			int week = cal.get(Calendar.DAY_OF_WEEK); // �õ�����
			String weekString = "��";

			switch (week) {

			case 1:
				weekString += "��";
				break;

			case 2:
				weekString += "һ";
				break;

			case 3:
				weekString += "��";
				break;

			case 4:
				weekString += "��";
				break;

			case 5:
				weekString += "��";
				break;

			case 6:
				weekString += "��";
				break;

			case 7:
				weekString += "��";
				break;

			default:
				break;

			}

			return weekString + " " + hourTime;

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";

	}

	/**
	 * ʱ���ʽת����"2014-03-05 12:03:19" "���� 18��00"
	 * 
	 * @param time
	 * @return
	 */
	public static String getHourTime(String time) {

		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = sdf.parse(time);

			SimpleDateFormat format = new SimpleDateFormat("HH:mm");
			String str_hour = format.format(date);

			Calendar cal = Calendar.getInstance();
			cal.setTime(date);

			/* ��ȡ���� */
			int week = cal.get(Calendar.DAY_OF_WEEK); // �õ�����
			String weekString = "��";

			switch (week) {

			case 1:
				weekString += "��";
				break;

			case 2:
				weekString += "һ";
				break;

			case 3:
				weekString += "��";
				break;

			case 4:
				weekString += "��";
				break;

			case 5:
				weekString += "��";
				break;

			case 6:
				weekString += "��";
				break;

			case 7:
				weekString += "��";
				break;

			default:
				break;

			}

			return weekString + " " + str_hour;

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";

	}

	/**
	 * ��ȡ���ָ�ʽ��ʱ�䣺yyyy-MM-dd HH:mm:ss
	 * 
	 * @param date
	 *            :����
	 * @param str_format
	 *            :yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static String getDateTime(Date date, String str_format) {

		SimpleDateFormat format = new SimpleDateFormat(str_format);
		String format_time = format.format(date);
		return format_time;

	}

	/**
	 * ��ȡ��������
	 */
	public static Integer getOrderDays(String startdate, String enddate) {

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date start;
		try {
			start = format.parse(startdate);
			Date end = format.parse(enddate);
			long difference = end.getTime() - start.getTime();

			int hours = (int) (difference / (3600000));
			int days = hours / 24;
			int leaveHours = hours % 24;
			if (leaveHours <= 4) {
				return new Integer(days);
			} else {
				return new Integer(days + 1);
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return 2;
	}

	/**
	 * ʱ���Ƿ�λ��2��ʱ��
	 * @return
	 */
	public static boolean isTimeOfRental(String start,String end,String time){
		
		boolean isTime = false;
		
		/* ��ȡʱ�� */
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date startDate = null;
		Date endDate = null;
		Date timeDate = null;
		
		try {
			startDate = format.parse(start);
			endDate = format.parse(end);
			timeDate = format.parse(time);

			if((timeDate.getTime()-startDate.getTime() >= 0) && (timeDate.getTime()-endDate.getTime() <= 0)){
				isTime = true;
			}
			
		} catch (ParseException e) {
			
			e.printStackTrace();
		}
		
		return isTime;
	}
	
	/**
	 * ��ȡ��ʱ��Сʱ��
	 * 
	 * @param startdate
	 * @param enddate
	 * @return
	 */
	public static boolean isLate(String enddate) {

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date end;
		try {
			end = format.parse(enddate);
			Date start = new Date();
			long difference = end.getTime() - start.getTime();

			int second = (int) (difference / (1000));
			if( second > 0 ){
				
				return true;
			}else{
				
				return false;
			}
		} catch (ParseException e) {
			
			e.printStackTrace();
		}

		return true;
	}
	
	/**
	 * ��ȡ��ʱ��Сʱ��
	 * 
	 * @param startdate
	 * @param enddate
	 * @return
	 */
//	public static Integer getDelayHours(String startdate, String enddate) {
//
//		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//		Date start;
//		try {
//			start = format.parse(startdate);
//			Date end = format.parse(enddate);
//			long difference = end.getTime() - start.getTime();
//
//			int hours = (int) (difference / (3600000));
//			int leaveHours = hours % 24;
//			if (leaveHours <= 4) {
//				return new Integer(leaveHours);
//			} else {
//				return new Integer(0);
//			}
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		return 2;
//	}

	/**
	 * ȡ��ʱ��<1����
	 * 
	 * @param startdate
	 * @param enddate
	 * @return
	 */
	public static boolean ordaysIsShort(String startdate) {

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date start;
		try {
			start = format.parse(startdate);
			long difference = start.getTime() - new Date().getTime();

			int hours = (int) (difference / (3600000));
			System.out.println("ʱ��" + hours);
			if (hours > 24 * 29) {
				return false;
			} else {
				return true;
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * �ж϶���ʱ���Ƿ�С��20Сʱ�����24*89Сʱ
	 * 1:��ʾ����1��
	 * @param startdate
	 * @param enddate
	 * @return
	 */
	public static int ordaysIsShort(String startdate, String enddate) {
		System.out.println("" + startdate);
		System.out.println("" + enddate);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date start;
		try {
			start = format.parse(startdate);
			Date end = format.parse(enddate);
			long difference = end.getTime() - start.getTime();

			int hours = (int) (difference / (3600000));
			if (hours < 5) {
				return 1;
			} else if(hours > 24 * 89+4){
				return 3;
			}else{
				return 2;
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return 1;
	}

	/**
	 * 1:���ڵ���89��
	 * 2������88��-89
	 * 3:С��88:1-87��
	 * @return
	 */
	public static int ordaysIsLong88(String enddate) {

		System.out.println("" + enddate);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date start;
		try {
			start = new Date();
			Date end = format.parse(enddate);
			long difference = end.getTime() - start.getTime();

			int hours = (int) (difference / (3600000));
			if(hours >= 24 * 89){
				return 1;
			}else{
				if(24 * 88 <= hours && hours < 24 * 89){
					return 2;
				}else{
					return 3;
				}
			}
		} catch (ParseException e) {
			
			e.printStackTrace();
		}

		return 3;
	}
	/**
	 * 1:���ڵ���89��
	 * 2������88��-89
	 * 3:С��88:1-87��
	 * @return
	 */
	public static int ordaysIsLong89(String enddate) {

		System.out.println("" + enddate);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date start;
		try {
			start = new Date();
			Date end = format.parse(enddate);
			long difference = end.getTime() - start.getTime();

			int hours = (int) (difference / (3600000));
			if(hours >= 24 * 90){
				return 1;
			}else{
				if(24 * 89 <= hours && hours < 24 * 90){
					return 2;
				}else{
					return 3;
				}
			}
		} catch (ParseException e) {
			
			e.printStackTrace();
		}

		return 3;
	}
	/**
	 * �Ƿ񳬹�30��*/
	public static boolean ordaysIsLong30(String enddate) {

		System.out.println("" + enddate);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date start;
		try {
			start = new Date();
			Date end = format.parse(enddate);
			long difference = end.getTime() - start.getTime();

			int hours = (int) (difference / (3600000));
			if(hours >= 24 * 31){
				return true;
			}
		} catch (ParseException e) {
			
			e.printStackTrace();
		}

		return false;
	}
	/**
	 * 1462500000000---2016-05-06 10:00
	 * 
	 * @param longtime
	 * @return
	 */
	public static String getTimemis_to_StringTime(String longtime) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

		String date = sdf.format(new Date(new Long(longtime)));

		return date;
	}
	public static String getTimemis_to_StringTime1(String longtime) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy��MM��dd�� HH:mm");

		String date = sdf.format(new Date(new Long(longtime)));

		return date;
	}
	/*��ȡ˳�糵����*/
	public static String[] getFreeTimes(String starttime, String endtime){
		/*��ȡ����*/
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date start;
		int days = 0;
		try {
			start = format.parse(starttime);
			Date end = format.parse(endtime);
			long difference = end.getTime() - start.getTime();
			
			int hours = (int) (difference / (3600000));
			days = hours / 24;
			
		} catch (ParseException e) {
			
			e.printStackTrace();
		}

		String[] times = new String[days+1];
			
		for (int i = 0; i < times.length-1; i++) {System.out.println(""+getDateTime_YMD_XDays(i,starttime));
			times[i] = getDateTime_Free_XDays(i,starttime);
		}
		
		times[days+1-1] = getDateTime_Free_XDays(0,endtime);
		
		return times;
	}
	
	public static int[] getFreeTakeTimeIds(String starttime, String endtime){
		/*��ȡ����*/
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date start;
		int days = 0;
		try {
			start = format.parse(starttime);
			Date end = format.parse(endtime);
			long difference = end.getTime() - start.getTime();

			int hours = (int) (difference / (3600000));
			days = hours / 24;

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		int[] ids = new int[days+1];
		
		for (int i = 0; i < days; i++) {
			ids[i] = i;
		}
		
		ids[days] = days;
		return ids;
	}
	
	/**
	 * ���ʱ��С��10:00,����ʾ10:00
	 * @return
	 */
	public static String getInitTime_10(String time) {

		String mytime = "10:00";
		
		Date nowDate;
		SimpleDateFormat format_now = new SimpleDateFormat("HH:mm");
		try {
			nowDate = format_now.parse(time);
			int hour = nowDate.getHours();
			System.out.println("ʱ��"+hour);
			
			if(hour < 10){
				return mytime;
			}else{
				return time;
			}
		} catch (ParseException e) {
			e.printStackTrace();
			return mytime;
			
		}

	}
	
	public static boolean isLateTakeCarTime(Long takeTime, String selectTime){
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date selectDate;
		try {
			
			selectDate = format.parse(selectTime);
			long select = selectDate.getTime();
			
			if(takeTime.longValue() > select){
				
				return true;
			}
						
		} catch (ParseException e) {
			
			e.printStackTrace();
		}
		
		return false;
	}

}
