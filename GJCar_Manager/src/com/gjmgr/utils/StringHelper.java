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
	
	/*id:0,name:'二代身份证'},{id:1,name:'港澳通行证'},{id:2,name:'台湾通行证'},{id:3,name:'护照'}*/
	public static String getcredentialType(String strtype){
		int type = -1;
		
		if(strtype.equals("0")){type=0;};
		if(strtype.equals("1")){type=1;};
		if(strtype.equals("2")){type=2;};
		if(strtype.equals("3")){type=3;};
		
		String type_str = "";
		
		switch (type) {
		
			case 0:
				type_str = "二代身份证";
				break;
			
			case 1:
				type_str = "港澳通行证";
				break;
	
			case 2:
				type_str = "台湾通行证";
				break;
				
			case 3:
				type_str = "护照";
				break;
				
			default:
				break;
		}
		
		return type_str;
	}
	//1：门店POS刷卡 2：在线网银 3：在线支付宝
	public static String getPayWay(Integer payway){
		
		String payway_str = "";
		
		switch (payway.intValue()) {
		
			case 0:
				payway_str = "门店现金支付";
				break;
			
			case 1:
				payway_str = "门店刷卡支付";
				break;
	
			case 2:
				payway_str = "在线网银支付";
				break;
				
			case 3:
				payway_str = "在线支付宝支付";
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
		
		if (model.equals("车型筛选") || model.equals("不限")) {
			return "";
		}
		if(model.equals("经济型")){
			return "1";
		}
		if(model.equals("舒适型")){
			return "2";
		}
		if(model.equals("豪华型")){
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
			return "经济型";
		}
		if(carGroup.intValue() == 2){
			return "舒适型";
		}
		if(carGroup.intValue() == 3){
			return "豪华型";
		}
		if(carGroup.intValue() == 4){
			return "SUV";
		}
		if(carGroup.intValue() == 5){
			return "MPV";
		}
		return "经济型";
	}
	
	public static String getCarTrunk(Integer carTrunk){

		if(carTrunk.intValue() == 1){
			return "3";
		}else{
			return carTrunk.toString();
		}
	}
	/**测算距离
	 * @param distance
	 * @return
	 */
	public static String getDistance(Double distance){
		   
		   if(distance < 100){
			   return "100米";
		   }
		   
		   if(100 <= distance && distance < 950){
			   DecimalFormat df=new DecimalFormat("0");	
			   Double d = Double.parseDouble(df.format(distance/100))*100;
			   return df.format(d)+"米";
		   }
		   if(distance >= 1000){
			   DecimalFormat df=new DecimalFormat("0.0");
			   Double d = Double.parseDouble(df.format(distance/1000));
			   return d+"公里";
		   }
		   return null;
	}
	
	/**
	 *
	 * @return 32位密文
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
	
	/**null变成""：因为null显示字符串时回事null
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
		
		String[] array_money = money.split("\\.");System.out.println("长度"+array_money.length);
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
