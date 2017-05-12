package com.gjmgr.utils;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;


/**1.添加桌面快捷方式
 * @author google
 *
 */
public class SystemUtils {
	
	/**获取当前版本号
	 * @return */
	public static String getVersion(Context context){
	
		if(context == null){
			
			return "";
		}
		
		try {
			PackageManager manager = context.getPackageManager();
			PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
			return info.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return "";
		}
		
	}
	
	/**获取手机的版本号
	 * @return
	 */
	public static String getModel(){
		return Build.MODEL;
	}

}
