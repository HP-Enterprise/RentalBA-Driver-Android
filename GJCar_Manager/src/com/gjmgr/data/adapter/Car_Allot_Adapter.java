package com.gjmgr.data.adapter;


import java.util.ArrayList;

import com.gjmgr.app.R;
import com.gjmgr.data.bean.Car_Veichel;
import com.gjmgr.utils.HandlerHelper;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
/**
 * 
 * 订单列表
 * 0：待支付，1：已下单 2：租赁中 3：已还车 4：已完成 5：已取消 6：NoShow
 * 一：待支付0
 * 二：已下单123
 * 三：已完成4
 * 四：已取消5
 * @author Administrator
 * 
 */
public class Car_Allot_Adapter extends BaseAdapter {

	private ArrayList<Car_Veichel> orderlist = new ArrayList<Car_Veichel>();
	private Context context;
	private Handler handler;
	private int what;
	
	public Car_Allot_Adapter(Context context, ArrayList<Car_Veichel> orderlist, Handler handler, int what) {
		
		this.orderlist = orderlist;
		this.context = context;		
		this.handler = handler;
		this.what = what;
	}
	
	@Override
	public int getCount() {
		
		return orderlist.size();
	}

	@Override
	public Object getItem(int position) {
		
		return orderlist.get(position);
	}

	@Override
	public long getItemId(int position) {
		
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		
		Holder holder;System.out.println("aaaaaaaaaaaaaaaaa");
		
		if (convertView == null) {
			holder = new Holder();
			convertView = View.inflate(context, R.layout.list_car_allot_item, null);
			
			holder.a_1 = (TextView) convertView.findViewById(R.id.a_1);				
			holder.a_2 = (TextView) convertView.findViewById(R.id.a_2);	
			
			holder.b_1 = (TextView) convertView.findViewById(R.id.b_1);			
			holder.b_2 = (TextView) convertView.findViewById(R.id.b_2);	
			holder.b_3 = (TextView) convertView.findViewById(R.id.b_3);	
			holder.b_4 = (TextView) convertView.findViewById(R.id.b_4);	
			
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		System.out.println("as1");
		holder.a_1.setText(orderlist.get(position).plate);System.out.println("as1");
		
		holder.b_1.setText("车型："+orderlist.get(position).vehicleModelShow.model);System.out.println("as2");
		holder.b_2.setText("颜色："+orderlist.get(position).colour);System.out.println("as3");
		holder.b_3.setText("当前里程数："+orderlist.get(position).mileage);		System.out.println("as4");
		
		String state = orderlist.get(position).state.equals("rented") ? "租赁中" : "待租赁";
		holder.b_4.setText("车辆状态："+state);System.out.println("as5");
	
		holder.a_2.setOnClickListener(new OnClickListener() {
				
			@Override
			public void onClick(View view) {
				
				HandlerHelper.sendInt(handler, what, position);
			}
		});
				
		return convertView;
		
	}

	public static class Holder {
		
		/*初始化控件*/
		private TextView a_1;		
		private TextView a_2;
		
		private TextView b_1;
		private TextView b_2;		
		private TextView b_3;		
		private TextView b_4;
		
	}



}
