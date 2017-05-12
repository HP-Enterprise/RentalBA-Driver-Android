package com.gjmgr.data.adapter;


import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.gjmgr.app.R;
import com.gjmgr.data.bean.CarModel;
import com.gjmgr.data.bean.Order;
import com.gjmgr.data.bean.VehicleBrandShow;
import com.gjmgr.data.bean.VehicleSeriesShow;
import com.gjmgr.data.data.Public_Api;
import com.gjmgr.data.data.Public_Data;
import com.gjmgr.data.data.Public_SP;
import com.gjmgr.utils.HandlerHelper;
import com.gjmgr.utils.HttpHelper;
import com.gjmgr.utils.SharedPreferenceHelper;
import com.gjmgr.utils.StringHelper;
import com.gjmgr.utils.TimeHelper;
import com.gjmgr.utils.ToastHelper;
import com.gjmgr.view.dialog.SubmitDialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
/**
 * 
 * �����б�
 * 0����֧����1�����µ� 2�������� 3���ѻ��� 4������� 5����ȡ�� 6��NoShow
 * һ����֧��0
 * �������µ�123
 * ���������4
 * �ģ���ȡ��5
 * @author Administrator
 * 
 */
public class Car_Model_Adapter extends BaseAdapter {

	private ArrayList<CarModel> orderlist = new ArrayList<CarModel>();
	private Context context;
	private Handler handler;
	private int what;
	
	public Car_Model_Adapter(Context context, ArrayList<CarModel> orderlist, Handler handler, int what) {
		
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
			convertView = View.inflate(context, R.layout.list_car_item, null);
			
			holder.a_1 = (TextView) convertView.findViewById(R.id.a_1);				
			holder.a_2 = (TextView) convertView.findViewById(R.id.a_2);	
			
			holder.b_1 = (TextView) convertView.findViewById(R.id.b_1);			
			holder.b_2 = (TextView) convertView.findViewById(R.id.b_2);	
			holder.b_3 = (TextView) convertView.findViewById(R.id.b_3);	
			holder.b_4 = (TextView) convertView.findViewById(R.id.b_4);	
			holder.b_5 = (TextView) convertView.findViewById(R.id.b_5);	
			holder.b_6 = (TextView) convertView.findViewById(R.id.b_6);	
			
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		
		holder.a_1.setText(orderlist.get(position).model);
		
		holder.b_1.setText("Ʒ�ƣ�"+orderlist.get(position).vehicleBrandShow.brand);
		holder.b_2.setText("ϵ�У�"+orderlist.get(position).vehicleSeriesShow.series);
		holder.b_3.setText("�����䣺"+new String[]{"MT","AT"}[orderlist.get(position).gear.intValue() - 1]);
		String carTrunk = orderlist.get(position).carTrunk==null ? "":orderlist.get(position).carTrunk;
		holder.b_4.setText("��ʽ��"+carTrunk+"��");
		holder.b_5.setText("��λ��"+orderlist.get(position).seats);
		holder.b_6.setText("�����飺"+StringHelper.getCarGroup(orderlist.get(position).carGroup));
		
		holder.a_2.setOnClickListener(new OnClickListener() {
				
			@Override
			public void onClick(View view) {
				
				HandlerHelper.sendInt(handler, what, position);
			}
		});
				
		return convertView;
		
	}

	public static class Holder {
		
		/*��ʼ���ؼ�*/
		private TextView a_1;		
		private TextView a_2;
		
		private TextView b_1;
		private TextView b_2;		
		private TextView b_3;		
		private TextView b_4;
		private TextView b_5;
		private TextView b_6;
		
	}

	public void setDataChanged(ArrayList<CarModel> list) {
		this.orderlist = list;
		this.notifyDataSetChanged();
	}

}
