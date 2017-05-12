package com.gjmgr.utils;



import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.gjmgr.annotation.ContentView;
import com.gjmgr.annotation.ContentWidget;
import com.gjmgr.annotation.EventListener;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;

public class AnnotationViewFUtils {

	public static void injectObject(Object object, FragmentActivity factivity, View myview) {

		Class<?> classType = object.getClass();

		// �����Ƿ����ContentView���͵�ע��
		if (classType.isAnnotationPresent(ContentView.class)) {
			// ���ش��ڵ�ContentView���͵�ע��
			ContentView annotation = classType.getAnnotation(ContentView.class);

			try {

				// ����һ�� Method ��������ӳ�� Class ��������ʾ�����ӿڵ�ָ��������Ա������
				Method method = classType
						.getMethod("setContentView", int.class);
				method.setAccessible(true);
				int resId = annotation.value();
				method.invoke(object, resId);

			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		// ���� Field �����һ�����飬��Щ����ӳ�� Class �����ʾ�����ӿ������ĳ�Ա������
		// ����������������Ĭ�ϣ��������ʺ�˽�г�Ա���������������̳еĳ�Ա������
		Field[] fields = classType.getDeclaredFields();

		if (null != fields && fields.length > 0) {

			for (Field field : fields) {
				// �ó�Ա�����Ƿ����ContentWidget���͵�ע��
				if (field.isAnnotationPresent(ContentWidget.class)) {

					ContentWidget annotation = field.getAnnotation(ContentWidget.class);
					// int viewId = annotation.value();
					int viewId = annotation.id();// ��ȡid������ȡ����onclick��id
					if (viewId == 0)// ����ǻ�ȡonclick��id
						viewId = factivity.getResources().getIdentifier(field.getName(), "id",factivity.getPackageName());
					if (viewId == 0)
						Log.e("D3Activity", "field " + field.getName() + "not found");

					View view = myview.findViewById(viewId);
					if (null != view) {
						try {

							field.setAccessible(true);
							field.set(object, view);
							setListener(object, field, annotation.click(),
									Method1.Click);
							// setListener(activity,field,annotation.longClick(),Method1.LongClick);
							// setListener(activity,field,annotation.itemClick(),Method1.ItemClick);
							// setListener(activity,field,annotation.itemLongClick(),Method1.itemLongClick);

						} catch (IllegalArgumentException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

				}

			}

		}

	}

	private static void setListener(Object activity, Field field,
			String methodName, Method1 method) throws Exception {
		if (methodName == null || methodName.trim().length() == 0)
			return;

		Object obj = field.get(activity);

		switch (method) {
		case Click:
			if (obj instanceof View) {
				((View) obj).setOnClickListener(new EventListener(activity)
						.click(methodName));
			}
			break;
		case ItemClick:
			if (obj instanceof AbsListView) {
				((AbsListView) obj).setOnItemClickListener(new EventListener(
						activity).itemClick(methodName));
			}
			break;
		case LongClick:
			if (obj instanceof View) {
				((View) obj).setOnLongClickListener(new EventListener(activity)
						.longClick(methodName));
			}
			break;
		case itemLongClick:
			if (obj instanceof AbsListView) {
				((AbsListView) obj)
						.setOnItemLongClickListener(new EventListener(activity)
								.itemLongClick(methodName));
			}
			break;
		default:
			break;
		}
	}

	public enum Method1 {
		Click, LongClick, ItemClick, itemLongClick
	}

}
