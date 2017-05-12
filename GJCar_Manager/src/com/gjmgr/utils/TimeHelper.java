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
	
	
	
	/**时间戳转化为时间
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
	 * 时间格式转换："2014-03-05 12:03:19" "03-05"
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
	 * 时间格式转换："2014-03-05 12:03:19" "2014-03-05+12:03:1"
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
	 * 2016-09-01 -------3（获取周几的数字：3代表周二，周日开始）
	 */
	public static int getDayOfWeek(String datetime){

		/* 获取时间 */
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

		/* 获取周日 */
		int week = cal.get(Calendar.DAY_OF_WEEK); // 得到星期
		
		return week;
	}
	
	/**
	 * 时间格式转换："2014-03-05 12:03:19" "03-05 周三"
	 * 
	 * @param time
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static String getDateWeekTime(String datetime) {

		/* 获取时间 */
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

		/* 获取时间 */
		SimpleDateFormat formattime = new SimpleDateFormat("MM-dd");
		String time = formattime.format(date);

		/* 获取周日 */
		int week = cal.get(Calendar.DAY_OF_WEEK); // 得到星期
		String weekString = "周";

		switch (week) {

		case 1:
			weekString += "日";
			break;

		case 2:
			weekString += "一";
			break;

		case 3:
			weekString += "二";
			break;

		case 4:
			weekString += "三";
			break;

		case 5:
			weekString += "四";
			break;

		case 6:
			weekString += "五";
			break;

		case 7:
			weekString += "六";
			break;

		default:
			break;

		}

		return time + " " + weekString;
	}

	/**
	 * 时间格式转换："2014-03-05 12:03:19" "周三 19:00"
	 * 
	 * @param time
	 * @return
	 */
	public static String getWeekTime(String datetime) {

		/* 获取时间 */
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

		/* 获取时间 */
		SimpleDateFormat formattime = new SimpleDateFormat("HH:mm");
		String time = formattime.format(date);

		/* 获取周日 */
		int week = cal.get(Calendar.DAY_OF_WEEK); // 得到星期
		String weekString = "周";

		switch (week) {

		case 1:
			weekString += "日";
			break;

		case 2:
			weekString += "一";
			break;

		case 3:
			weekString += "二";
			break;

		case 4:
			weekString += "三";
			break;

		case 5:
			weekString += "四";
			break;

		case 6:
			weekString += "五";
			break;

		case 7:
			weekString += "六";
			break;

		default:
			break;

		}

		return weekString + " " + time;
	}

	/**
	 * 获取时间"2016-03-05 08:00的时间按戳
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
	 * 获取时间"2016-03-05"的后n天的时间戳
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
	 * 获取时间"2016-03-05"的后n天
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
	 * 获取时间"2016-03-05 10:00"的后n天
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
	 * 获取时间"2016-08-09" 得到Date
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
	 * 获取时间"2016-08-09 23:00" 得到Date
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
	 * 获取时间"12:32" 得到Date
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
	 * 获取当前时间，"2016-03-05"的后n天
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
	 * 当前时间的后n天， 03-05
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
	 * 当前时间的后n天 ，然后通过getHourTime_ten转化为 周三 10:00
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
	 * 当前时间的后n天 ，然后通过getHourTime_ten转化为 周三 10:00
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
	 * 获取时间"2016-03-05 13:09"的后n天
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
	 * 获取时间"2016-03-05 13:09"的后n天
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
	 * 时间格式转换："2014-03-05 12:03" "2014-03-05"
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
	 * 时间格式转换："2014-03-05 12:03" "20140305"
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
	 * 时间格式转换："2014-03-05 12:03:19" "03-05"
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
	 * 获取当前时间
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
	 * 时间格式转换："2014-03-05 12:03:19" "03-05"
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
	 * 时间格式转换："2014-03-05 12:03:19" "周日 10：00"
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

			/* 获取周日 */
			int week = cal.get(Calendar.DAY_OF_WEEK); // 得到星期
			String weekString = "周";

			switch (week) {

			case 1:
				weekString += "日";
				break;

			case 2:
				weekString += "一";
				break;

			case 3:
				weekString += "二";
				break;

			case 4:
				weekString += "三";
				break;

			case 5:
				weekString += "四";
				break;

			case 6:
				weekString += "五";
				break;

			case 7:
				weekString += "六";
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
	 * 时间格式转换："2014-03-05 12:03:19" "周日 18：00"
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

			/* 获取周日 */
			int week = cal.get(Calendar.DAY_OF_WEEK); // 得到星期
			String weekString = "周";

			switch (week) {

			case 1:
				weekString += "日";
				break;

			case 2:
				weekString += "一";
				break;

			case 3:
				weekString += "二";
				break;

			case 4:
				weekString += "三";
				break;

			case 5:
				weekString += "四";
				break;

			case 6:
				weekString += "五";
				break;

			case 7:
				weekString += "六";
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
	 * 获取各种格式的时间：yyyy-MM-dd HH:mm:ss
	 * 
	 * @param date
	 *            :日期
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
	 * 获取订餐天数
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
	 * 时间是否位于2个时间
	 * @return
	 */
	public static boolean isTimeOfRental(String start,String end,String time){
		
		boolean isTime = false;
		
		/* 获取时间 */
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
	 * 获取延时的小时数
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
	 * 获取延时的小时数
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
	 * 取车时间<1个月
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
			System.out.println("时间" + hours);
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
	 * 判断订单时间是否小于20小时或大于24*89小时
	 * 1:表示少于1天
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
	 * 1:大于等于89天
	 * 2：等于88天-89
	 * 3:小于88:1-87天
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
	 * 1:大于等于89天
	 * 2：等于88天-89
	 * 3:小于88:1-87天
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
	 * 是否超过30天*/
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

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");

		String date = sdf.format(new Date(new Long(longtime)));

		return date;
	}
	/*获取顺风车订单*/
	public static String[] getFreeTimes(String starttime, String endtime){
		/*获取天数*/
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
		/*获取天数*/
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
	 * 如果时间小于10:00,就显示10:00
	 * @return
	 */
	public static String getInitTime_10(String time) {

		String mytime = "10:00";
		
		Date nowDate;
		SimpleDateFormat format_now = new SimpleDateFormat("HH:mm");
		try {
			nowDate = format_now.parse(time);
			int hour = nowDate.getHours();
			System.out.println("时间"+hour);
			
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
