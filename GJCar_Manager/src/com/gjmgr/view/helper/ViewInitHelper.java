package com.gjmgr.view.helper;

import org.apache.http.Header;

import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import android.app.Activity;
import android.content.Context;
import android.widget.TextView;

public class ViewInitHelper {

	
	public static void initTextViews(TextView[] textviews, String[] values){
		
		for (int i = 0; i < textviews.length; i++) {
			textviews[i].setText(values[i]);
		}
	}
	
	/**
	 * {
	    	"status": "true",
	    	"message": {
		        "id": 22,
		        "nickName": "¶À¹Â¾Å½£",
		        "realName": null,
	         }
	    }
	 * */
		
}
