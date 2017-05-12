package com.gjmgr.activity.user;

import com.gjmgr.utils.ToastHelper;

import android.content.Context;
import android.widget.EditText;

public class ValidationHelper {
/**һ�������ж�
	1��������
	2���ǿ�����
	3��������
	4�������ʼ�
	5����ѡһ������֤
	6���ظ�������֤
	7��HTTPURL��֤
	8���ֻ������ʽ��֤��
	9.���֤
	
	����������ҵ���жϣ�
	1���ֻ����룺��ʽ�жϣ��ǿ��ж�
	2.���룺��ʽ�жϣ��ǿ��ж�
	**/
	
/**********************************************************************************************************
**********************************************************************************************************
*
*�����ж�
*
***********************************************************************************************************	
*/
	
	/**
	 * 1��������2���ǿ�����
	 */
	public static boolean isNull(EditText edit){
		
		if(edit.getText().toString().equals("") || edit.getText().toString() == null){
			System.out.println("Ϊ��");		
			return true;
		}
		return false;
	}
	
	/**
	 * 1��������2���ǿ�����
	 */
	public static String isNull(String note, EditText edit){
		
		if(edit.getText().toString().equals("") || edit.getText().toString() == null){
					
			return note + "������д";
		}
		return "";
	}
	public static String format(String type, EditText edit){
		
		/*1.�ֻ���ʽ*/
		if(type.equals("phone")){
			if(!edit.getText().toString().trim().matches("^[1][3578][0-9]{9}$")){
				return "�ֻ�����" + "��ʽ����ȷ";
			}else{
				return "";
			}
		}
		
		/*2.��֤���ʽ��6λ����*/
		if(type.equals("code")){
			if(!edit.getText().toString().trim().matches("^[0-9]{6}$")){
				return "��֤��" + "��ʽ����ȷ";
			}else{
				return "";
			}
		}
		
		/*3.�����ʽ��*/
		if(type.equals("password")){
			if(!edit.getText().toString().trim().matches("^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,18}$")){
				return "����" + "����ͬʱ������ĸ���ֲ�����6-18λ";
			}else{
				return "";
			}
		}
		
		/*4.�������ʽ��*/
		if(type.equals("oldpassword")){
			if(!edit.getText().toString().trim().matches("^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,18}$")){
				return "������" + "����ͬʱ������ĸ���ֲ�����6-18λ";
			}else{
				return "";
			}
		}
		
		/*5.�������ʽ��*/
		if(type.equals("newpassword")){
			if(!edit.getText().toString().trim().matches("^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,18}$")){
				return "������" + "����ͬʱ������ĸ���ֲ�����6-18λ";
			}else{
				return "";
			}
		}
		
		/*6.���֤��ʽ��number.getText().toString().trim().matches("^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$")||number.getText().toString().trim().matches("^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9]|X|x)$")*/
		if(type.equals("identity")){
			if(!edit.getText().toString().trim().matches("\\d{14}[0-9a-zA-Z])|(\\d{17}[0-9a-zA-Z]")){
				return "���֤" + "��ʽ����ȷ";
			}else{
				return "";
			}
		}
		
		/*7.��ʻ֤��ʽ��*/
		if(type.equals("drive")){
			if(!edit.getText().toString().trim().matches("(/^[\u4e00-\u9fa5]{1}[A-Z0-9]{6}$/")){
				return "��ʻ֤" + "��ʽ����ȷ";
			}else{
				return "";
			}
		}
		
		/*8.��֤�����ʽ��*/
		if(type.equals("email")){
			if(!edit.getText().toString().trim().matches("^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$")){
				return "����" + "��ʽ����ȷ";
			}else{
				return "";
			}
		}
		
		return "";
	}
	
	
	/*�ж�������ֻ�ܰ�������*/
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
*�����ж�
*
***********************************************************************************************************	
*/	
	public static boolean Validate_Phone(Context context, EditText edit){
		
		if(!isNull("�ֻ�����", edit).equals("")){
			ToastHelper.showToastShort(context, isNull("�ֻ�����", edit));
			return false;
		}
		
		if(!format("phone", edit).equals("")){
			ToastHelper.showToastShort(context, format("phone", edit));
			return false;
		}
		return true;
	}
	
	public static boolean Validate_Password(Context context, EditText edit){
		
		if(!isNull("����", edit).equals("")){
			ToastHelper.showToastShort(context, isNull("����", edit));
			return false;
		}
		
//		if(!format("password", edit).equals("")){
//			ToastHelper.showToastShort(context, format("password", edit));
//			return false;
//		}
		return true;
	}
	
	
}
