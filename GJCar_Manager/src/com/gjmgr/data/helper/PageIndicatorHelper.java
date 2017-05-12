package com.gjmgr.data.helper;

import java.util.ArrayList;
import java.util.List;

import com.gjmgr.utils.HandlerHelper;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class PageIndicatorHelper {
	
	public static int oldIndex = 0;
	
	public static void initIndicator(final Context context, TextView[] textViews, View[] views, final int colorSelect, final int colorNormal,final Handler handler, final int what) {
		
		for (int i = 0; i < textViews.length; i++) {System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa1");
			
			final TextView tv = textViews[i];
			final View view = views[i];
			final int index = i;
			
			final List<TextView> notSelectTvs = new ArrayList<TextView>();
			final List<View> notSelectViews = new ArrayList<View>();System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa2");
			
			for (int j = 0; j < views.length; j++) {
				
				if (index != j) {
					notSelectTvs.add(textViews[j]);
					notSelectViews.add(views[j]);
				}
				
			}
			System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa3");	

			textViews[i].setOnClickListener(new OnClickListener(){
				
				@Override
				public void onClick(View tview) {
					System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa4");
					if(oldIndex == index){
						return;
					}
					oldIndex = index;
					
					tv.setTextColor(context.getResources().getColor(colorSelect));
					view.setVisibility(View.VISIBLE);
					HandlerHelper.sendString(handler, what, new Integer(index).toString());
					System.out.println("·¢ËÍ"+index);
					for (int k = 0; k < notSelectTvs.size(); k++) {
						notSelectTvs.get(k).setTextColor(context.getResources().getColor(colorNormal));
						notSelectViews.get(k).setVisibility(View.GONE);
					}
					
					for (int j = 0; j < notSelectViews.size(); j++) {
						System.out.println(""+notSelectViews.get(j).getVisibility());
					}
				}
			});
		}
	}
	
	public static void setSelected(final Context context, TextView[] textViews, View[] views, final int colorSelect, final int colorNormal){
		
		for (int i = 0; i < textViews.length; i++) {
		
			if(oldIndex == i){
				textViews[i].setTextColor(context.getResources().getColor(colorSelect));
				views[i].setVisibility(View.VISIBLE);
			}else{
				textViews[i].setTextColor(context.getResources().getColor(colorNormal));
				views[i].setVisibility(View.GONE);
			}

		}
	}
}
