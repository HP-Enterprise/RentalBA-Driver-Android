package com.gjmgr.data.helper;

import java.util.ArrayList;

import com.gjmgr.data.bean.Order;

public class ListHelper {

	public static ArrayList<Order> getListByOrderId(ArrayList<Order> orderlist_show){

		
		/*获取id*/
		int[] array = new int[orderlist_show.size()];
		
		for (int j = 0; j < orderlist_show.size(); j++) {
			System.out.println("pg1-"+orderlist_show.get(j).orderCode);
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
        
        boolean[] list_code = new boolean[orderlist_show.size()];
        for (int l = 0; l < list_code.length; l++) {
        	list_code[l] = false;
		}
        
        for (int i = 0; i < array.length; i++) {
            
        	for (int j = 0; j < orderlist_show.size(); j++) {
				
        		if((array[i]+"").equals(orderlist_show.get(j).orderCode)){// && (orderlist_show.get(j).orderType.intValue() == 3 || orderlist_show.get(j).orderType.intValue() == 4)
        			
        			if(!list_code[j]){
        				list.add(orderlist_show.get(j));
            			list_code[j] = true;
        			}
        			
//        			orderlist_show.remove(j);//删除，以免重复订单
//        			break;
        		}else{
        			
        			System.out.println("list筛选"+orderlist_show.get(j).orderType.intValue());
        		}
			}
        }
        
        return list;
	}
	
	public static ArrayList<Order> getMyListByOrderId(ArrayList<Order> orderlist_show){
		
		/*获取id*/
		int[] array = new int[orderlist_show.size()];
		
		for (int j = 0; j < orderlist_show.size(); j++) {
			System.out.println("pg1-"+orderlist_show.get(j).orderCode);
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
				
        		if((array[i]+"").equals(orderlist_show.get(j).orderCode)){// && (orderlist_show.get(j).orderType.intValue() == 3 || orderlist_show.get(j).orderType.intValue() == 4)
        			list.add(orderlist_show.get(j));
//        			break;
        		}else{
        			
        			System.out.println("list筛选"+orderlist_show.get(j).orderType.intValue());
        		}
			}
        }
        
        return list;
	}
}
