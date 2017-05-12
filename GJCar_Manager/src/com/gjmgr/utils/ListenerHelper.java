package com.gjmgr.utils;

import com.gjmgr.data.data.Public_Msg;

import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.ToggleButton;

public class ListenerHelper {

	public final static int Listener_ToggleButton_CheckedChanged = 1;
	
	public final static int Listener_ListView_OnItemClick = 2;
	
	public static void setListener(View view, int listenerType, final Handler handler, final int what){
		
		switch (listenerType) {
			case Listener_ToggleButton_CheckedChanged:
				((ToggleButton)view).setOnCheckedChangeListener(new OnCheckedChangeListener() {
					
					@Override
					public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
						if(isChecked){
							HandlerHelper.sendString(handler, what, Public_Msg.Msg_Checked);
						}else{
							HandlerHelper.sendString(handler, what, Public_Msg.Msg_UnChecked);
						}
					}
				});
				break;
	
			case Listener_ListView_OnItemClick:
				((ListView)view).setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int position, long arg3) {

						HandlerHelper.sendInt(handler, what, position);
					}
				});
				break;
				
			default:
				break;
		}
		
		
		
	
	}
}
