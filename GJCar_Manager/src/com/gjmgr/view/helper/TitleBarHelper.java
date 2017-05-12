package com.gjmgr.view.helper;

import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.gjmgr.app.R;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TitleBarHelper {
	
	public static void Back(final Context context, String titleName, int index){
		
		//添加标题
		View titleView = View.inflate(context, R.layout.titlebar_back2, null);

		LinearLayout lin = (LinearLayout)((Activity)context).findViewById(R.id.activity);
		
		lin.addView(titleView,index);//从0开始：0在最上面
		
		ImageView back = (ImageView)titleView.findViewById(R.id.back);
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				((Activity)context).finish();
			}
		});
		
		TextView title = (TextView)titleView.findViewById(R.id.title);
		title.setText(titleName);
		
	}
	
	public static void Back_ok(final Activity activity, String titleName, final Handler handler, final int what,int index){
		
		//添加标题
		View titleView = View.inflate(activity, R.layout.titlebar_back_ok, null);

		LinearLayout lin = (LinearLayout)activity.findViewById(R.id.activity);
		
		lin.addView(titleView,index);//从0开始：0在最上面
		
		/*返回*/
		ImageView back = (ImageView)titleView.findViewById(R.id.back);
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				activity.finish();
			}
		});
		
		/*标题*/
		TextView title = (TextView)titleView.findViewById(R.id.title);
		title.setText(titleName);
		
		/*确定*/
		TextView ok = (TextView)titleView.findViewById(R.id.ok_true);
		ok.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				handler.sendEmptyMessage(what);
			}
		});
	}
	
}
