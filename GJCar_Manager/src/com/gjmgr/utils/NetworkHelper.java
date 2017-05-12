package com.gjmgr.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkHelper {

	/** ≈–∂œ «∑Ò”–Õ¯¬Á */
	public static boolean isNetworkAvailable(Context context){		
		
		boolean isNetOK = false;
		
		ConnectivityManager con = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);		
		if(con != null){
			NetworkInfo networkInfo = con.getActiveNetworkInfo();
			if(networkInfo != null){

				if(networkInfo.isAvailable()){
					isNetOK = true;
				}else{
					isNetOK = false;
				}
			}else{
				isNetOK = false;
			}
			
		}else{
			
			isNetOK = false;
		}
		
		if(!isNetOK){
			ToastHelper.showNoNetworkToast(context);
		}
		
		return isNetOK;
	}
}
