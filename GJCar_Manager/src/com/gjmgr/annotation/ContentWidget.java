package com.gjmgr.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ContentWidget {
	//int value();
	/**
	 * ��ȡ�ؼ�id
	 * @return
	 */
	public int id() default 0;
	/**
	 * �ؼ�����¼�
	 * @return
	 */
	public String click() default "";
	public String longClick() default "";
	public String itemClick() default "";
	public String itemLongClick() default "";
}