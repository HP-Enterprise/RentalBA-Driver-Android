package com.gjmgr.view.dialog;

import com.gjmgr.app.R;
import com.gjcar.view.wheelview.ArrayWheelAdapter;
import com.gjcar.view.wheelview.WheelView;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SelectDailog {

	public void select(Context context, String title, final TextView textView, final String[] items){
		
		/* 创建对象 */
		final Dialog dialog = new Dialog(context, R.style.delete_dialog);
		
		/* 初始化视图 */
		View view = View.inflate(context,R.layout.dialog_select, null);
				
		final WheelView wheelView = (WheelView) view.findViewById(R.id.wheelView);
		ArrayWheelAdapter<String> arrayWheelAdapter = new ArrayWheelAdapter<String>(items,2);
		wheelView.setAdapter(arrayWheelAdapter);
		wheelView.setCurrentItem(0);
		
		TextView title_tv = (TextView) view.findViewById(R.id.title);
		title_tv.setText(title);
		title_tv.setTag(wheelView.getCurrentItem());
		
		/* 获取车型或价格 */
		TextView ok = (TextView) view.findViewById(R.id.ok);
		ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				/* 获取*/
				
				textView.setText(items[wheelView.getCurrentItem()]);		
				
				if (null != dialog) {
					dialog.dismiss();
				}
			}
		});
		
		TextView cancel = (TextView) view.findViewById(R.id.cancle);
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
		
				if (null != dialog) {
					dialog.dismiss();
				}
			}
		});
			
		/* 初始化监属性 */
		dialog.getWindow().setContentView(view);
		dialog.getWindow().setGravity(Gravity.BOTTOM);
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		dialog.getWindow().setLayout(wm.getDefaultDisplay().getWidth(),LinearLayout.LayoutParams.WRAP_CONTENT);
		dialog.setCancelable(true);
		dialog.setCanceledOnTouchOutside(true);
	
		/* 显示 */
		dialog.show();
	}
	
	
}
