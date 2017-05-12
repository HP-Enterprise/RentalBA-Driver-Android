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
	 * 获取控件id
	 * @return
	 */
	public int id() default 0;
	/**
	 * 控件点击事件
	 * @return
	 */
	public String click() default "";
	public String longClick() default "";
	public String itemClick() default "";
	public String itemLongClick() default "";
}