package com.gjmgr.data.helper;

import java.util.ArrayList;

import com.gjmgr.data.bean.Order;

public class ListHelper {

	public static ArrayList<Order> getListByOrderId(ArrayList<Order> orderlist_show){
		
		/*获取id*/
		int[] array = new int[orderlist_show.size()];
		
		for (int j = 0; j < orderlist_show.size(); j++) {
			
			array[j] = new Integer(orderlist_show.get(j).orderCode).intValue();
		}
		
		/*排序*/
        int temp;
        for (int i = 0; i < array.length; i++) {
            for (int j = i+1; j < array.length; j++) {
                if (array[i] < array[j]) {
                    temp = array[i];
                    array[i] = array[j];
                    array[j] = temp;  // 两个数交换位置
                }
            }
        }
        
        /*遍历*/ 
        ArrayList<Order> list = new ArrayList<Order>();
        
        for (int i = 0; i < array.length; i++) {
            
        	for (int j = 0; j < orderlist_show.size(); j++) {
				
        		if((array[i]+"").equals(orderlist_show.get(j).orderCode)){
        			list.add(orderlist_show.get(j));
        			break;
        		}
			}
        }
        
        return list;
	}
}
