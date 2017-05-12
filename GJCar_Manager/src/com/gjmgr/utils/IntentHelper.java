package com.gjmgr.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

public class IntentHelper {

	public final static int Type_String = 1;
	
	public final static int Type_Short = 2;
	public final static int Type_Int = 3;
	public final static int Type_Long = 4;

	public final static int Type_Float = 5;
	public final static int Type_Double = 6;
	
	public final static int Type_Boolean = 7;
	
	public static void startActivity(Context context, Class<?> cls){
		
		Intent intent = new Intent(context, cls);
		((Activity)context).startActivity(intent);

	}
	
	public static void startActivity_Extra(Context context, Class<?> cls, String[] keys, Object[] values, int[] types){
		
		Intent intent = new Intent(context, cls);
		
		for(int i=0; i<keys.length; i++){
			
			switch (types[i]) {
				case Type_String:
					intent.putExtra(keys[i], (String)values[i]);
					break;
					
				case Type_Int:
					intent.putExtra(keys[i], (Integer)values[i]);				
					break;
					
				case Type_Double:
					intent.putExtra(keys[i], (Double)values[i]);	
					break;
					
				case Type_Boolean:
					intent.putExtra(keys[i], (Boolean)values[i]);	
					break;
					
				case Type_Short:
					intent.putExtra(keys[i], (Short)values[i]);					
					break;
					
				case Type_Long:
					intent.putExtra(keys[i], (Long)values[i]);	
					break;
					
				case Type_Float:
					intent.putExtra(keys[i], (Float)values[i]);	
					break;
					
				default:
					break;
			}
			
		}
		
		context.startActivity(intent);
	}
	
	public static void Fragment_startActivityForResult(Context context, Fragment fragment, Class<?> cls, int requestCode){
		
		Intent intent = new Intent(context, cls);
			
		fragment.startActivityForResult(intent, requestCode);
	}
	
	public static void Fragment_startActivityForResult_Extra(Context context, Fragment fragment, Class<?> cls, int requestCode, String[] keys, Object[] values, int[] types){
		
		Intent intent = new Intent(context, cls);
		for(int i=0; i<keys.length; i++){
			
			switch (types[i]) {
				case Type_String:
					intent.putExtra(keys[i], (String)values[i]);
					break;
					
				case Type_Int:
					intent.putExtra(keys[i], (Integer)values[i]);				
					break;
					
				case Type_Double:					
					intent.putExtra(keys[i], (Double)values[i]);					
					break;
					
				case Type_Boolean:
					intent.putExtra(keys[i], (Boolean)values[i]);	
					break;
					
				case Type_Short:
					intent.putExtra(keys[i], (Short)values[i]);					
					break;
					
				case Type_Long:
					intent.putExtra(keys[i], (Long)values[i]);	
					break;
					
				case Type_Float:
					intent.putExtra(keys[i], (Float)values[i]);	
					break;
					
				default:
					break;
			}
			
		}

		fragment.startActivityForResult(intent, requestCode);
	}
	
	public static void startActivityForResult_Extra(Activity activity, Class<?> cls, int requestCode, String[] keys, Object[] values, int[] types){
		
		Intent intent = new Intent(activity, cls);
		for(int i=0; i<keys.length; i++){
			
			switch (types[i]) {
				case Type_String:
					intent.putExtra(keys[i], (String)values[i]);
					break;
					
				case Type_Int:
					intent.putExtra(keys[i], (Integer)values[i]);				
					break;
					
				case Type_Double:
					intent.putExtra(keys[i], (Double)values[i]);	
					break;
					
				case Type_Boolean:
					intent.putExtra(keys[i], (Boolean)values[i]);	
					break;
					
				case Type_Short:
					intent.putExtra(keys[i], (Short)values[i]);					
					break;
					
				case Type_Long:
					intent.putExtra(keys[i], (Long)values[i]);	
					break;
					
				case Type_Float:
					intent.putExtra(keys[i], (Float)values[i]);	
					break;
					
				default:
					break;
			}
			
		}
		
		activity.startActivityForResult(intent, requestCode);
	}
	
	public static void startActivity_StringExtras(Context context, Class<?> cls, String[] keys, String[] values){
		
		Intent intent = new Intent(context, cls);
		
		for (int i = 0; i < keys.length; i++) {
			intent.putExtra(keys[i], values[i]);
		}
		
		context.startActivity(intent);
	}
	public static void setResultStringExtras(Activity activity, int resultCode, String[] keys, String[] values){
		Intent intent = new Intent();//设置返回参数

		for (int i = 0; i < values.length; i++) {
			intent.putExtra(keys[i], values[i]);
		}
		
		activity.setResult(resultCode, intent);
		
		activity.finish();
	}
	
	public static void setResultExtras(Activity activity, int resultCode, String[] keys, Object[] values, int[] types){
		Intent intent = new Intent();//设置返回参数

		for(int i=0; i<keys.length; i++){
			
			switch (types[i]) {
				case Type_String:
					intent.putExtra(keys[i], (String)values[i]);
					break;
					
				case Type_Int:
					intent.putExtra(keys[i], (Integer)values[i]);				
					break;
					
				case Type_Double:
					intent.putExtra(keys[i], (Double)values[i]);	
					break;
					
				case Type_Boolean:
					intent.putExtra(keys[i], (Boolean)values[i]);	
					break;
					
				case Type_Short:
					intent.putExtra(keys[i], (Short)values[i]);					
					break;
					
				case Type_Long:
					intent.putExtra(keys[i], (Long)values[i]);	
					break;
					
				case Type_Float:
					intent.putExtra(keys[i], (Float)values[i]);	
					break;
					
				default:
					break;
			}
			
		}
		
		activity.setResult(resultCode, intent);
		
		activity.finish();
	}
}
