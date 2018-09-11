package cn.gqy2012.sorm.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 常用反射操作
 * @author gqy2012
 *
 */
public class ReflectUtils {
	/**
	 * 调用obj对象对应属性fieldName的get方法
	 * @param c
	 * @param fieldName
	 * @param obj
	 * @return
	 */
	public static Object invokeGet(String fieldName,Object obj) {
		Method m;
		try {
			m = obj.getClass().getDeclaredMethod("get"+StringUtils.firstChar2UpperCase(fieldName),null);
			return m.invoke(obj, null);
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static void invokeSet(Object obj,String columnName,Object columnValue) {
		Method m;
		try {
			m = obj.getClass().getDeclaredMethod("set"+StringUtils.firstChar2UpperCase(columnName)
			, columnValue.getClass());
			m.invoke(obj, columnValue);
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
	
		
	}
}
