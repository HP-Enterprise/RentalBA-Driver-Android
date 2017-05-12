package com.gjmgr.activity.user;

import com.gjmgr.utils.ToastHelper;

import android.content.Context;
import android.widget.EditText;

public class ValidationHelper {
/**一：类型判断
	1，必填项
	2，非空数据
	3，仅数字
	4，电子邮件
	5，二选一必填验证
	6，重复密码验证
	7，HTTPURL验证
	8，手机号码格式验证等
	9.身份证
	
	二：复合型业务判断：
	1。手机号码：各式判断，非空判断
	2.密码：各式判断，非空判断
	**/
	
/**********************************************************************************************************
**********************************************************************************************************
*
*类型判断
*
***********************************************************************************************************	
*/
	
	/**
	 * 1，必填项2，非空数据
	 */
	public static boolean isNull(EditText edit){
		
		if(edit.getText().toString().equals("") || edit.getText().toString() == null){
			System.out.println("为空");		
			return true;
		}
		return false;
	}
	
	/**
	 * 1，必填项2，非空数据
	 */
	public static String isNull(String note, EditText edit){
		
		if(edit.getText().toString().equals("") || edit.getText().toString() == null){
					
			return note + "必须填写";
		}
		return "";
	}
	public static String format(String type, EditText edit){
		
		/*1.手机格式*/
		if(type.equals("phone")){
			if(!edit.getText().toString().trim().matches("^[1][3578][0-9]{9}$")){
				return "手机号码" + "格式不正确";
			}else{
				return "";
			}
		}
		
		/*2.验证码格式：6位数字*/
		if(type.equals("code")){
			if(!edit.getText().toString().trim().matches("^[0-9]{6}$")){
				return "验证码" + "格式不正确";
			}else{
				return "";
			}
		}
		
		/*3.密码格式：*/
		if(type.equals("password")){
			if(!edit.getText().toString().trim().matches("^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,18}$")){
				return "密码" + "必须同时包含字母数字并且是6-18位";
			}else{
				return "";
			}
		}
		
		/*4.旧密码格式：*/
		if(type.equals("oldpassword")){
			if(!edit.getText().toString().trim().matches("^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,18}$")){
				return "旧密码" + "必须同时包含字母数字并且是6-18位";
			}else{
				return "";
			}
		}
		
		/*5.新密码格式：*/
		if(type.equals("newpassword")){
			if(!edit.getText().toString().trim().matches("^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,18}$")){
				return "新密码" + "必须同时包含字母数字并且是6-18位";
			}else{
				return "";
			}
		}
		
		/*6.身份证格式：number.getText().toString().trim().matches("^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$")||number.getText().toString().trim().matches("^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9]|X|x)$")*/
		if(type.equals("identity")){
			if(!edit.getText().toString().trim().matches("\\d{14}[0-9a-zA-Z])|(\\d{17}[0-9a-zA-Z]")){
				return "身份证" + "格式不正确";
			}else{
				return "";
			}
		}
		
		/*7.驾驶证格式：*/
		if(type.equals("drive")){
			if(!edit.getText().toString().trim().matches("(/^[\u4e00-\u9fa5]{1}[A-Z0-9]{6}$/")){
				return "驾驶证" + "格式不正确";
			}else{
				return "";
			}
		}
		
		/*8.验证邮箱格式：*/
		if(type.equals("email")){
			if(!edit.getText().toString().trim().matches("^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$")){
				return "邮箱" + "格式不正确";
			}else{
				return "";
			}
		}
		
		return "";
	}
	
	
	/*判断姓名：只能包含中文*/
	public static boolean IsChineseName(String str){
		
		for (int i = 0; i < str.length(); i++) {
			
			char oneChar = str.charAt(i);
			if((oneChar >= '\u4e00' && oneChar <= '\u9fa5')||(oneChar >= '\uf900' && oneChar <='\ufa2d')){
				
			}else{
				return false;
				
			}
			
		}
		
		return true;
		
	}
/**********************************************************************************************************
**********************************************************************************************************
*
*复合判断
*
***********************************************************************************************************	
*/	
	public static boolean Validate_Phone(Context context, EditText edit){
		
		if(!isNull("手机号码", edit).equals("")){
			ToastHelper.showToastShort(context, isNull("手机号码", edit));
			return false;
		}
		
		if(!format("phone", edit).equals("")){
			ToastHelper.showToastShort(context, format("phone", edit));
			return false;
		}
		return true;
	}
	
	public static boolean Validate_Password(Context context, EditText edit){
		
		if(!isNull("密码", edit).equals("")){
			ToastHelper.showToastShort(context, isNull("密码", edit));
			return false;
		}
		
//		if(!format("password", edit).equals("")){
//			ToastHelper.showToastShort(context, format("password", edit));
//			return false;
//		}
		return true;
	}
	
	
}
