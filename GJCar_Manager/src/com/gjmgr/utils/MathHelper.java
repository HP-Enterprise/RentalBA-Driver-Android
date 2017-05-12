package com.gjmgr.utils;

public class MathHelper {

	public static int getPersent(float oneAdd,float per){
		
		float myper = per * oneAdd;//16.33 * 100 * oneAdd
		
		return (int) myper;
		
	}
}
