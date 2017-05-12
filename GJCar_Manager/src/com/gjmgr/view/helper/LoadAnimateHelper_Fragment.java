package com.gjmgr.view.helper;

import com.gjmgr.app.R;
import com.gjmgr.utils.HandlerHelper;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class LoadAnimateHelper_Fragment {
	
	/*进度条*/
	private Context mycontext;
	private LinearLayout load;
	private TextView loadtext;
	private ImageView loadimage;
	private Animation animation;
	private boolean isClickable;

	private String Empty;
	
	/**
	 * 显示3种：
	 * 1.ok
	 * 2.fail
	 * 3.noNet
	 */
	public void Search_Animate(final Context context, View view, int id, final Handler handler, final int what, boolean isClickableNeed,boolean isStart,int index, String Empty){
		
		mycontext = context;
		this.Empty = Empty;
		
		//添加标题
		View loading = View.inflate(context, R.layout.loading, null);

		LinearLayout lin = (LinearLayout)view.findViewById(id);
		
		lin.addView(loading,index);//从0开始
	
		//加载条
		load = (LinearLayout) loading.findViewById(R.id.loadlinearlayout);

		loadimage = (ImageView) loading.findViewById(R.id.loadiamge);
		loadtext = (TextView) loading.findViewById(R.id.loadtext);
		animation = AnimationUtils.loadAnimation(context, R.anim.load);

		isClickable = isClickableNeed;
		
		if(isClickable){
			load.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					
					HandlerHelper.sendString(handler, what, HandlerHelper.Load);
				}
			});
		}
		
		if(isStart){
			start_animation();
		}
	}

	public void Search_Animate_Dialog(final Context context, LinearLayout lin, final Handler handler, final int what, boolean isClickableNeed,boolean isStart,int index){
		
		//添加标题
		View loading = View.inflate(context, R.layout.loading, null);
		
		lin.addView(loading,index);//从0开始
	
		//加载条
		load = (LinearLayout) loading.findViewById(R.id.loadlinearlayout);

		loadimage = (ImageView) loading.findViewById(R.id.loadiamge);
		loadtext = (TextView) loading.findViewById(R.id.loadtext);
		animation = AnimationUtils.loadAnimation(context, R.anim.load);
		
		isClickable = isClickableNeed;
		
		if(isClickable){
			load.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					
					HandlerHelper.sendString(handler, what, HandlerHelper.Load);
				}
			});
		}
		
		if(isStart){
			start_animation();
		}
	}
	
	public void start_animation(){
		load.setVisibility(View.VISIBLE);
		load.setClickable(false);
		loadimage.setVisibility(View.VISIBLE);
		loadtext.setVisibility(View.VISIBLE);
		loadtext.setText("正在搜索中...");
		loadimage.startAnimation(animation);
	}
	public void start_animation_again(){
		
		load.setVisibility(View.VISIBLE);	
		loadimage.setVisibility(View.VISIBLE);
		loadimage.startAnimation(animation);
	}
	public void load_success_animation(){
		load.setVisibility(View.GONE);
		loadimage.clearAnimation();
		loadimage.setVisibility(View.GONE);
	}

	public void load_empty_animation(){
		load.setVisibility(View.VISIBLE);
		if(isClickable){
			load.setClickable(true);
			loadtext.setText(Empty);
		}else{
			load.setClickable(false);
			loadtext.setText(Empty);
		}
	    loadimage.clearAnimation();
	    loadimage.setVisibility(View.GONE);
	}
	
	public void load_fail_animation(){
		load.setVisibility(View.VISIBLE);
		if(isClickable){
			load.setClickable(true);
			loadtext.setText("抱歉，搜索失败，请点击重试");
		}else{
			load.setClickable(false);
			loadtext.setText("抱歉，搜索失败");
		}
	    loadimage.clearAnimation();
	    loadimage.setVisibility(View.GONE);
	}
	
	public void load_noNetwork_animation(){
		load.setVisibility(View.VISIBLE);
		if(isClickable){
			load.setClickable(true);
			loadtext.setText("当前网络不可用,请检查网络后，再点击重试");
		}else{
			load.setClickable(false);
			loadtext.setText("当前网络不可用,请检查网络");
		}
		loadimage.clearAnimation();
		loadimage.setVisibility(View.GONE);
	}
}
