package com.gjmgr.utils;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class StringHelper {

	public static  String getString(String str){
    	
    	if(str == null || str.equals("") || str.equals("null")){
    		return "";
    	}
    	
    	return str;
    }
	
	public static  String getClassName(String className){
    	
    	String[] strs = className.split("\\.");
    	return strs[strs.length-1];
    }
	
	/*id:0,name:'�������֤'},{id:1,name:'�۰�ͨ��֤'},{id:2,name:'̨��ͨ��֤'},{id:3,name:'����'}*/
	public static String getcredentialType(String strtype){
		int type = -1;
		
		if(strtype.equals("0")){type=0;};
		if(strtype.equals("1")){type=1;};
		if(strtype.equals("2")){type=2;};
		if(strtype.equals("3")){type=3;};
		
		String type_str = "";
		
		switch (type) {
		
			case 0:
				type_str = "�������֤";
				break;
			
			case 1:
				type_str = "�۰�ͨ��֤";
				break;
	
			case 2:
				type_str = "̨��ͨ��֤";
				break;
				
			case 3:
				type_str = "����";
				break;
				
			default:
				break;
		}
		
		return type_str;
	}
	//1���ŵ�POSˢ�� 2���������� 3������֧����
	public static String getPayWay(Integer payway){
		
		String payway_str = "";
		
		switch (payway.intValue()) {
		
			case 0:
				payway_str = "�ŵ��ֽ�֧��";
				break;
			
			case 1:
				payway_str = "�ŵ�ˢ��֧��";
				break;
	
			case 2:
				payway_str = "��������֧��";
				break;
				
			case 3:
				payway_str = "����֧����֧��";
				break;
				
			default:
				break;
		}
		
		return payway_str;
	}

    public static String getStringType(int index, String[] strs){
		
		String mystr = "";
		
		for (int i = 0; i < strs.length; i++) {
			if(index == i){
				mystr = strs[i];
				break;
			}
		}
		
		return mystr;
	}
	
	public static String getCarGroup(String model){
		
		if (model.equals("����ɸѡ") || model.equals("����")) {
			return "";
		}
		if(model.equals("������")){
			return "1";
		}
		if(model.equals("������")){
			return "2";
		}
		if(model.equals("������")){
			return "3";
		}
		if(model.equals("SUV")){
			return "4";
		}
		if(model.equals("MPV")){
			return "5";
		}
		return "";
	}
	
	public static String getCarGroup(Integer carGroup){

		if(carGroup.intValue() == 1){
			return "������";
		}
		if(carGroup.intValue() == 2){
			return "������";
		}
		if(carGroup.intValue() == 3){
			return "������";
		}
		if(carGroup.intValue() == 4){
			return "SUV";
		}
		if(carGroup.intValue() == 5){
			return "MPV";
		}
		return "������";
	}
	
	public static String getCarTrunk(Integer carTrunk){

		if(carTrunk.intValue() == 1){
			return "3";
		}else{
			return carTrunk.toString();
		}
	}
	/**�������
	 * @param distance
	 * @return
	 */
	public static String getDistance(Double distance){
		   
		   if(distance < 100){
			   return "100��";
		   }
		   
		   if(100 <= distance && distance < 950){
			   DecimalFormat df=new DecimalFormat("0");	
			   Double d = Double.parseDouble(df.format(distance/100))*100;
			   return df.format(d)+"��";
		   }
		   if(distance >= 1000){
			   DecimalFormat df=new DecimalFormat("0.0");
			   Double d = Double.parseDouble(df.format(distance/1000));
			   return d+"����";
		   }
		   return null;
	}
	
	/**
	 *
	 * @return 32λ����
	 */
	public static String encryption(String plainText) {
	    String re_md5 = new String();
	    try {
	        MessageDigest md = MessageDigest.getInstance("md5");
	        md.update(plainText.getBytes());
	        byte b[] = md.digest();

	        int i;

	        StringBuffer buf = new StringBuffer("");
	        for (int offset = 0; offset < b.length; offset++) {
	            i = b[offset];
	            if (i < 0)
	                i += 256;
	            if (i < 16)
	                buf.append("0");
	            buf.append(Integer.toHexString(i));
	        }

	        re_md5 = buf.toString();

	    } catch (NoSuchAlgorithmException e) {
	        e.printStackTrace();
	    }
	    return re_md5;
	}
	
	public static List<String> getBusList(String busstr){
		
		List<String> list = new ArrayList<String>();
		
		String[] arr_bus = busstr.split(";");
		for (int i = 0; i < arr_bus.length; i++) {
			String hi = getBusNumber(arr_bus[i]);
			list.add(hi);
		}
		
		for(String i : list){
			
			System.out.println(""+i);
		}
		return list;
	}
	
	public static String getBusNumber(String number){
		String result = "";
		
		//String ss = "fadsfhsjk423986fadfdas9692h5kj28";
		List<String> is = new ArrayList<String>();
		for(int i = 0; i < number.length(); i++) {
		final char c = number.charAt(i);
		if(Character.isDigit(c)) {
			is.add(String.valueOf(c));
		}
		}
		for(String i : is) {
			result += i;
		}
		
		return result;
	}

	public static int getCurrentStation(List<String> list, String currentStation){
		
		int index = 0;
		for(int i = 0; i < list.size(); i++){
			
			if(list.get(i).equals(currentStation)){
				index = i;
				break;
			}
		}
			
		return index;
	}
	
	/**null���""����Ϊnull��ʾ�ַ���ʱ����null
	 * @param str
	 * @return
	 */
	public static String Null2Empty(String str){
    	if(str == null){
    		return "";
    	}else{
    		return str;
    	}
    }
	
   
    public static String getMoney(String money){
		
		String[] array_money = money.split("\\.");System.out.println("����"+array_money.length);
		System.out.println(""+array_money[0]);
		
		if(array_money.length == 1){
						
			return money;
		}else{
			
			String small_moeny = array_money[1];

			if(small_moeny.length() == 1){
				
				String a_money = new String(new char[]{small_moeny.charAt(0)});
				if(a_money.equals("0")){
					return array_money[0];
				}else{
					return money;
				}
			}else{
				
				String a_money = new String(new char[]{small_moeny.charAt(0)});
				String b_money = new String(new char[]{small_moeny.charAt(1)});
				
				if(a_money.equals("0") && b_money.equals("0")){
					return array_money[0];
				}else{
					return money;
				}
			}
		}

	}
    
    public static String[] getOils(){
    	
    	String[] oils = new String[16];
    	
    	for (int i = 0; i < 16; i++) {
			
    		oils[i] = (i + 1)+"/16"; 
		}
    	
    	return oils;
    }
}
