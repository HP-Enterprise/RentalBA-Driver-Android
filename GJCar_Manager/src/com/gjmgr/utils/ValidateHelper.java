package com.gjmgr.utils;

import android.content.Context;

public class ValidateHelper {

	public static boolean Validate(Context context, boolean[] oks, String[] messages){
		
		boolean isOk = true;
		System.out.println("length"+oks.length);
		for (int i = 0; i < oks.length; i++) {System.out.println("okz__"+oks[i]);
			if(oks[i]){
				isOk = false;
				ToastHelper.showToastShort(context, messages[i]);
				break;
			}
		}
		
		return isOk;
		
	}
	
	
}
