package com.gjmgr.utils;

import com.gjmgr.data.data.Public_SP;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharedPreferenceHelper {

	public final static int Type_Int = 1;
	public final static int Type_String = 2;
	public final static int Type_Boolean = 3;
	public final static int Type_Float = 4;
	public final static int Type_Long = 5;
	
	/**获取token*/
	public static String getToken(Context context){
		SharedPreferences preferences = context.getSharedPreferences(Public_SP.Account, Context.MODE_PRIVATE);
		String data = preferences.getString("token", "");
		return data;
	}
	
	/**判断是否登录*/
	public static boolean isLogin(Context context){
		SharedPreferences preferences = context.getSharedPreferences(Public_SP.Account, Context.MODE_PRIVATE);
		int uid = preferences.getInt("uid", 0);
		boolean loginState = false;
		if(uid != 0){
			loginState = true;
		}
		return loginState;
	}
	
	/**获取uid*/
	public static int getUid(Context context){
		SharedPreferences preferences = context.getSharedPreferences(Public_SP.Account, Context.MODE_PRIVATE);
		int uid = preferences.getInt("uid", 0);
		return uid;
	}
	
	/**获取int
	 * @return
	 */
	public static int getInt(Context context, String spName, String key,int defaultValue){
		SharedPreferences preferences = context.getSharedPreferences(spName, Context.MODE_PRIVATE);
		int value = preferences.getInt(key, defaultValue);
		return value;
	}
	
	/**获取String
	 * @param context
	 * @return
	 */
	public static String getString(Context context, String spName, String key){
		
		SharedPreferences preferences = context.getSharedPreferences(spName, Context.MODE_PRIVATE);
		String value = preferences.getString(key, "");
		return value;
	}
	
	/**获取Float
	 */
	public static float getFloat(Context context, String spName, String key){
		
		SharedPreferences preferences = context.getSharedPreferences(spName, Context.MODE_PRIVATE);
		float value = preferences.getFloat(key, (float) 0.0);
		return value;
	}
	
	/**写入String
	 * @param context
	 * @return
	 */
	public static void putString(Context context, String spName, String key,String value){
		
		SharedPreferences preferences = context.getSharedPreferences(spName, Context.MODE_PRIVATE);
		Editor editor = preferences.edit();

		editor.putString(key, value);
	
		editor.commit();

	}
	
	/**写入int
	 * @param context
	 * @return
	 */
	public static void putInt(Context context, String spName, String key,int value){
		
		SharedPreferences preferences = context.getSharedPreferences(spName, Context.MODE_PRIVATE);
		Editor editor = preferences.edit();

		editor.putInt(key, value);
		
		editor.commit();
	}
	
	/**
	 * 保存实体
	 */
	public static void putBean(Context context, String spName, String[] keys, Object[] values, int[] types){
		
		if(spName == null){System.out.println("putBean--spName有nullxxxxxxxxxxxxxxxxxx");
			return;
		}
		
		if(context == null ){System.out.println("putBean--context有nullxxxxxxxxxxxxxxxxxx");
			return;
		}
		SharedPreferences preferences = context.getSharedPreferences(spName, Context.MODE_PRIVATE);
		
		if(preferences == null){System.out.println("putBean--preferences有nullxxxxxxxxxxxxxxxxxx");
			return;
		}
		Editor editor = preferences.edit();

		for (int i = 0; i < types.length; i++) {
			
			switch (types[i]) {
			
				case Type_Int:
					editor.putInt(keys[i], (Integer)values[i]);
					break;
	
				case Type_String:
					editor.putString(keys[i], (String)values[i]);
					break;
					
				case Type_Boolean:
					editor.putBoolean(keys[i], (Boolean)values[i]);
					break;
					
				case Type_Float:
					editor.putFloat(keys[i], (Float)values[i]);
					break;
					
				case Type_Long:
					editor.putLong(keys[i], (Long)values[i]);
					break;
					
				default:
					break;
			}
		}
	
		editor.commit();
	}
	
	/**
	 * 保存值
	 */
	public static void putValue(Context context, String spName, String key, Object value, int type){
		
		SharedPreferences preferences = context.getSharedPreferences(spName, Context.MODE_PRIVATE);
		Editor editor = preferences.edit();

		switch (type) {
		
			case Type_Int:
				editor.putInt(key, (Integer)value);
				break;
	
			case Type_String:
				editor.putString(key, (String)value);
				break;
				
			case Type_Boolean:
				editor.putBoolean(key, (Boolean)value);
				break;
				
			case Type_Float:
				editor.putFloat(key, (Float)value);
				break;
				
			case Type_Long:
				editor.putLong(key, (Long)value);
				break;
				
			default:
				break;
		}
	
		editor.commit();
	}
	
	/**清空
	 */
	public static void clear(Context context, String spName){
		
		SharedPreferences preferences = context.getSharedPreferences(spName, Context.MODE_PRIVATE);
		Editor editor = preferences.edit();

		editor.clear();
		editor.commit();
	}
}
