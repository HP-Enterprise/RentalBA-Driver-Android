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

public class LoadAnimateHelper_Car {
	
	/*������*/
	private static Context mycontext;
	private static LinearLayout load;
	private static TextView loadtext;
	private static ImageView loadimage;
	private static Animation animation;
	private static boolean isClickable;
	/**
	 * ��ʾ3�֣�
	 * 1.ok
	 * 2.fail
	 * 3.noNet
	 */
	public static void Search_Animate(final Context context, int id, final Handler handler, final int what, boolean isClickableNeed,boolean isStart,int index){
		
		mycontext = context;
		
		//��ӱ���
		View loading = View.inflate(context, R.layout.loading, null);

		LinearLayout lin = (LinearLayout)((Activity)context).findViewById(id);
		
		lin.addView(loading,index);//��0��ʼ
	
		//������
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

	public static void Search_Animate_Dialog(final Context context, LinearLayout lin, final Handler handler, final int what, boolean isClickableNeed,boolean isStart,int index){
		
		//��ӱ���
		View loading = View.inflate(context, R.layout.loading, null);
		
		lin.addView(loading,index);//��0��ʼ
	
		//������
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
	
	public static void start_animation(){
		load.setVisibility(View.VISIBLE);
		load.setClickable(false);
		loadimage.setVisibility(View.VISIBLE);
		loadtext.setVisibility(View.VISIBLE);
		loadtext.setText("����������...");
		loadimage.startAnimation(animation);
	}
	public static void start_animation_again(){
		
		load.setVisibility(View.VISIBLE);	
		loadimage.setVisibility(View.VISIBLE);
		loadimage.startAnimation(animation);
	}
	public static void load_success_animation(){
		load.setVisibility(View.GONE);
		loadimage.clearAnimation();
		loadimage.setVisibility(View.GONE);
	}

	public static void load_empty_animation(){
		load.setVisibility(View.VISIBLE);
		if(isClickable){
			load.setClickable(true);
			loadtext.setText("��Ǹ��û���������ɷ���ĳ�����Ϣ");
		}else{
			load.setClickable(false);
			loadtext.setText("��Ǹ��û���������ɷ���ĳ�����Ϣ");
		}
	    loadimage.clearAnimation();
	    loadimage.setVisibility(View.GONE);
	}
	
	public static void load_fail_animation(){
		load.setVisibility(View.VISIBLE);
		if(isClickable){
			load.setClickable(true);
			loadtext.setText("��Ǹ������ʧ�ܣ���������");
		}else{
			load.setClickable(false);
			loadtext.setText("��Ǹ������ʧ��");
		}
	    loadimage.clearAnimation();
	    loadimage.setVisibility(View.GONE);
	}
	
	public static void load_noNetwork_animation(){
		load.setVisibility(View.VISIBLE);
		if(isClickable){
			load.setClickable(true);
			loadtext.setText("��ǰ���粻����,����������ٵ������");
		}else{
			load.setClickable(false);
			loadtext.setText("��ǰ���粻����,��������");
		}
		loadimage.clearAnimation();
		loadimage.setVisibility(View.GONE);
	}
}
