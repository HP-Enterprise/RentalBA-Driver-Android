package com.gjmgr.view.dialog;

import java.util.Calendar;
import java.util.Date;

import com.gjmgr.app.R;
import com.gjcar.view.wheelview.NumericWheelAdapter;
import com.gjcar.view.wheelview.OnWheelScrollListener;
import com.gjcar.view.wheelview.WheelView;
import com.gjmgr.utils.TimeHelper;
import com.gjmgr.utils.ToastHelper;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DateTimePickerHelper {
	
	/*ѡ��ʱ��*/
	private static WheelView year;
	private static WheelView month;
	private static WheelView day;

	/*ѡ��ʱ��*/
	private static WheelView hour;
	private static WheelView minute;
	
	public void pickTime(Context context,TextView textiView,String datetime,String name){

		/* �������� */
		final Dialog dialog = new Dialog(context, R.style.delete_dialog);

		/* ��ʼ����ͼ */
		View view = View.inflate(context,R.layout.dialog_day, null);

		initView_DateTime(context,view,textiView, dialog,datetime,name);

		/* ��ʼ�������� */
		dialog.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(DialogInterface arg0, int arg1, KeyEvent arg2) {

				return false;
			}

		});

		/* ��ʼ�������� */
		dialog.getWindow().setContentView(view);
		dialog.getWindow().setGravity(Gravity.BOTTOM);
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		dialog.getWindow().setLayout(wm.getDefaultDisplay().getWidth(),LinearLayout.LayoutParams.WRAP_CONTENT);
		dialog.setCancelable(true);
		dialog.setCanceledOnTouchOutside(true);

		/* ��ʾ */
		dialog.show();
	
	}
	
	private static void initView_DateTime(final Context context,final View view, final TextView textiView,final Dialog dialog, final String datetime,String name) {

		/* ��ʼ��ʱ�� */
		Calendar c = Calendar.getInstance();
		
		c.setTime(TimeHelper.getTransferDate(datetime));

		int nowYear = Calendar.getInstance().get(Calendar.YEAR);//������Ϊ��ǰ���
		int nowMonth = c.get(Calendar.MONTH) + 1;// ͨ��Calendar���������Ҫ+1
		int nowDay = c.get(Calendar.DAY_OF_MONTH);
		int nowHour = c.get(Calendar.HOUR_OF_DAY);
		int nowMinute = c.get(Calendar.MINUTE);

		
		System.out.println("�������"+Calendar.getInstance().get(Calendar.YEAR));		
System.out.println("nowYear"+nowYear);
System.out.println("nowMonth"+nowMonth);
System.out.println("nowDay"+nowDay);

		int curYear = c.get(Calendar.YEAR) - (nowYear - 20);
		int curMonth = nowMonth - 1;
		int curDay = nowDay - 1;
		int curHour = nowHour - 1;
		int curMinute = nowMinute-1;
		
		year = (WheelView) view.findViewById(R.id.year);
		year.setAdapter(new NumericWheelAdapter(nowYear - 20, nowYear + 20));// (2016-20,
																				// 2016
																				// +
																				// 20)
		year.setLabel("��");
		year.setCyclic(true);
		year.addScrollingListener(scrollListener);
		year.setCurrentItem(curYear);

		month = (WheelView) view.findViewById(R.id.month);
		month.setAdapter(new NumericWheelAdapter(1, 12, "%02d"));
		month.setLabel("��");
		month.setCyclic(true);
		month.addScrollingListener(scrollListener);
		month.setCurrentItem(curMonth);

		day = (WheelView) view.findViewById(R.id.day);
		initDay(curYear, nowMonth);
		day.setLabel("��");
		day.setCyclic(true);
		day.setCurrentItem(curDay);
		
		hour = (WheelView) view.findViewById(R.id.hour);
		hour.setAdapter(new NumericWheelAdapter(0, 23, "%02d"));
		hour.setLabel("ʱ");
		hour.setCyclic(true);
		hour.setCurrentItem(curHour-0+1);System.out.println("curHour"+curHour);
			
		minute = (WheelView) view.findViewById(R.id.minute);
		minute.setAdapter(new NumericWheelAdapter(0, 59, "%02d"));
		minute.setLabel("��");
		minute.setCyclic(true);
		minute.setCurrentItem(curMinute-0+1);System.out.println("curMinute"+nowMinute);
		
		/*�м����*/
		TextView title = (TextView) view.findViewById(R.id.takecar_datepicker_takereturn);
		title.setText(name);
		
		/* ȷ�� */
		TextView ok = (TextView) view.findViewById(R.id.takecar_datepicker_ok);
		ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				/* ��ȡʱ�� */
				String date = new StringBuilder()
						.append(year.getCurrentItem()
								+ Calendar.getInstance().get(Calendar.YEAR)
								- 20)
						.append("-")
						.append((month.getCurrentItem() + 1) < 10 ? "0"
								+ (month.getCurrentItem() + 1) : (month
								.getCurrentItem() + 1))
						.append("-")
						.append(((day.getCurrentItem() + 1) < 10) ? "0"
								+ (day.getCurrentItem() + 1) : (day
								.getCurrentItem() + 1)).toString();
				
				/* ��ȡʱ�� */
				String time = new StringBuilder()
				.append((hour.getCurrentItem()) < 10 ? "0"
						+ (hour.getCurrentItem()) : (hour
						.getCurrentItem()))
				.append(":")	
				.append((minute.getCurrentItem()) < 10 ? "0"
						+ (minute.getCurrentItem()) : (minute
						.getCurrentItem())).toString();
						System.out.println("date"+date);				
						System.out.println("year)"+year.getCurrentItem());	
						System.out.println("now"+Calendar.getInstance().get(Calendar.YEAR));	
						
				/*�ж�ʱ��ѡ���Ƿ���ȷ*/
//				Calendar.getInstance().setTime(new Date());
//				
//				if(year.getCurrentItem() > 20){
//					ToastHelper.showToastShort(context, "��ѡ����ȷ�����");
//					return;
//				}
//				if(year.getCurrentItem()==20 && month.getCurrentItem() > Calendar.getInstance().get(Calendar.MONTH)){
//					ToastHelper.showToastShort(context, "��ѡʱ��Ӧ�����ڵ�ǰʱ��");
//					return;
//				}
//				if(year.getCurrentItem()==20 && month.getCurrentItem() == Calendar.getInstance().get(Calendar.MONTH) && day.getCurrentItem()+1 >  Calendar.getInstance().get(Calendar.DAY_OF_MONTH)){
//					ToastHelper.showToastShort(context, "��ѡʱ��Ӧ�����ڵ�ǰʱ��");System.out.println("item"+day.getCurrentItem());System.out.println("day"+Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
//					return;
//				}
//				if(year.getCurrentItem()==20 && month.getCurrentItem() == Calendar.getInstance().get(Calendar.MONTH) && day.getCurrentItem()+1 ==  Calendar.getInstance().get(Calendar.DAY_OF_MONTH) && hour.getCurrentItem() >  Calendar.getInstance().get(Calendar.HOUR_OF_DAY)){
//					ToastHelper.showToastShort(context, "��ѡʱ��Ӧ�����ڵ�ǰʱ��");System.out.println("item"+day.getCurrentItem());System.out.println("day"+Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
//					return;
//				}
//				if(year.getCurrentItem()==20 && month.getCurrentItem() == Calendar.getInstance().get(Calendar.MONTH) && day.getCurrentItem()+1 ==  Calendar.getInstance().get(Calendar.DAY_OF_MONTH) && hour.getCurrentItem() ==  Calendar.getInstance().get(Calendar.HOUR_OF_DAY) && minute.getCurrentItem()> Calendar.getInstance().get(Calendar.MINUTE)){
//					ToastHelper.showToastShort(context, "��ѡʱ��Ӧ�����ڵ�ǰʱ��");System.out.println("item"+day.getCurrentItem());System.out.println("day"+Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
//					return;
//				}
				
				/*����ʱ��*/
				textiView.setText(date+" "+time);

				if (null != dialog) {System.out.println("ok");
					dialog.dismiss();System.out.println("fail");
				}
			}
		});
		
		/*ȡ��*/
		TextView cancel = (TextView) view
				.findViewById(R.id.takecar_datepicker_cancle);
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				if (null != dialog) {
					dialog.dismiss();
				}
			}
		});

	}

	static OnWheelScrollListener scrollListener = new OnWheelScrollListener() {
		@Override
		public void onScrollingStarted(WheelView wheel) {

		}

		@Override
		public void onScrollingFinished(WheelView wheel) {
			int n_year = year.getCurrentItem();//
			System.out.println("ѡ��" + year.getCurrentItem() + "---"
					+ Calendar.YEAR);
			int n_month = month.getCurrentItem() + 1;//
			// �����շ����仯ʱ��Ҫ�ı���
			initDay(n_year,n_month);
			String birthday = new StringBuilder()
					.append(n_year + Calendar.getInstance().get(Calendar.YEAR)
							- 20)
					.append("-")
					.append((month.getCurrentItem() + 1) < 10 ? "0"
							+ (month.getCurrentItem() + 1) : (month
							.getCurrentItem() + 1))
					.append("-")
					.append(((day.getCurrentItem() + 1) < 10) ? "0"
							+ (day.getCurrentItem() + 1) : (day
							.getCurrentItem() + 1)).toString();
			System.out.println("ѡ������" + birthday);

		}
	};
	
	/**
	 * ��ʼ����
	 */
	private static void initDay(int arg1, int arg2) {
		day.setAdapter(new NumericWheelAdapter(1, getDay(arg1, arg2), "%02d"));
	}
	
	/**
	 * 
	 * @param year
	 * @param month
	 * @return
	 */
	private static int getDay(int year, int month) {
		System.out.println("month"+month);
		int day = 30;
		boolean flag = false;
		switch (year % 4) {
		case 0:
			flag = true;
			break;
		default:
			flag = false;
			break;
		}
		switch (month) {
		case 1:
		case 3:
		case 5:
		case 7:
		case 8:
		case 10:
		case 12:
			day = 31;
			break;
		case 2:
			day = flag ? 29 : 28;
			break;
		default:
			day = 30;
			break;
		}
		return day;
	}

}
