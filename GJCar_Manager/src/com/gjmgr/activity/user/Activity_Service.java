package com.gjmgr.activity.user;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.gjmgr.annotation.ContentView;
import com.gjmgr.annotation.ContentWidget;
import com.gjmgr.app.R;
import com.gjmgr.data.helper.JSONHelper;
import com.gjmgr.data.helper.Loginhelper;
import com.gjmgr.utils.AnnotationViewUtils;
import com.gjmgr.utils.NetworkHelper;
import com.gjmgr.utils.StringHelper;
import com.gjmgr.utils.ToastHelper;
import com.gjmgr.view.dialog.SubmitDialog;

@ContentView(R.layout.activity_startservice)
public class Activity_Service extends Activity{

	@ContentWidget(click = "onClick") Button start,stop;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		AnnotationViewUtils.injectObject(this, this);
	}
	
	public void onClick(View view) {

		switch (view.getId()) {


			case R.id.start:
				Intent intent = new Intent(Activity_Service.this, Servcie_Gps.class);
				startService(intent);
				break;
				
			case R.id.stop:
				Intent intent_stop = new Intent(Activity_Service.this, Servcie_Gps.class);
				stopService(intent_stop);
				break;
				
			default:
				break;
		}
	}
}
