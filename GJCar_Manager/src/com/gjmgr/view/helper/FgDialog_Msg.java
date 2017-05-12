package com.gjmgr.view.helper;

import com.gjmgr.app.R;
import com.gjmgr.utils.HandlerHelper;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class FgDialog_Msg {

	public void ShowDialog(Context context, String content, final Handler handler, final int what){
		
		
		final Dialog submitDialog = new Dialog(context, R.style.delete_dialog);
		
		View view = View.inflate(context, R.layout.fg_dialog_message, null);
		
		TextView message = (TextView)view.findViewById(R.id.dig_msg_message);
		TextView ok = (TextView)view.findViewById(R.id.dig_msg_ok);
		TextView cancle = (TextView)view.findViewById(R.id.dig_msg_cancle);
		
		message.setText(content);
		
		ok.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				
				handler.sendEmptyMessage(what);
				submitDialog.cancel();
			}
		});
		
		cancle.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {

				submitDialog.cancel();
			}
		});
		
	    submitDialog.setContentView(view);
	    submitDialog.setCancelable(true);
	    submitDialog.setCanceledOnTouchOutside(false);
	    submitDialog.show();
		
	}
	
}
