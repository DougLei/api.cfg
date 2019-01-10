package com.api.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 反射操作工具类
 * @author DougLei
 */
public class ReflectUtil {
	
	/**
	 * 获取obj指定fieldName的Field对象
	 * @param obj
	 * @param fieldName
	 * @return
	 */
	private static Field getFieldByFieldName(Object obj, String fieldName) {
		for (Class<?> superClass = obj.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {
			try {
				return superClass.getDeclaredField(fieldName);
			} catch (NoSuchFieldException e) {
				Log4jUtil.debug("[ReflectUtil.getFieldByFieldName]方法出现异常信息:{}", ExceptionUtil.getErrMsg(e));
			}
		}
		return null;
	}

	/**
	 * 获取obj对象指定fieldName的属性值
	 * @param obj
	 * @param fieldName
	 * @return
	 */
	public static Object getValueByFieldName(Object obj, String fieldName){
		Field field = getFieldByFieldName(obj, fieldName);
		Object value = null;
		if(field!=null){
			try {
				if (field.isAccessible()) {
					value = field.get(obj);
				} else {
					field.setAccessible(true);
					value = field.get(obj);
					field.setAccessible(false);
				}
			} catch (IllegalArgumentException e) {
				Log4jUtil.debug("[ReflectUtil.getValueByFieldName]方法出现异常信息:{}", ExceptionUtil.getErrMsg(e));
			} catch (IllegalAccessException e) {
				Log4jUtil.debug("[ReflectUtil.getValueByFieldName]方法出现异常信息:{}", ExceptionUtil.getErrMsg(e));
			} catch (SecurityException e) {
				Log4jUtil.debug("[ReflectUtil.getValueByFieldName]方法出现异常信息:{}", ExceptionUtil.getErrMsg(e));
			}
		}
		return value;
	}

	/**
	 * 设置obj对象指定fieldName的属性值
	 * @param obj
	 * @param fieldName
	 * @param value
	 */
	public static void setValueByFieldName(Object obj, String fieldName, Object value){
		Field field = getFieldByFieldName(obj, fieldName);
		try {
			if (field.isAccessible()) {
				field.set(obj, value);
			} else {
				field.setAccessible(true);
				field.set(obj, value);
				field.setAccessible(false);
			}
		} catch (IllegalArgumentException e) {
			Log4jUtil.debug("[ReflectUtil.setValueByFieldName]方法出现异常信息:{}", ExceptionUtil.getErrMsg(e));
		} catch (IllegalAccessException e) {
			Log4jUtil.debug("[ReflectUtil.setValueByFieldName]方法出现异常信息:{}", ExceptionUtil.getErrMsg(e));
		} catch (SecurityException e) {
			Log4jUtil.debug("[ReflectUtil.setValueByFieldName]方法出现异常信息:{}", ExceptionUtil.getErrMsg(e));
		}
	}
	
	/**
	 * 调用obj对象的方法
	 * @param obj
	 * @param methodName
	 * @param clz
	 * @param params
	 * @return
	 */
	public static Object invokeMethod(Object obj, String methodName, Class<?>[] clz, Object[] params){
		Object resObj = null;
		try {
			Method method = obj.getClass().getDeclaredMethod(methodName, clz);
			resObj = method.invoke(obj, params);
		} catch (SecurityException e) {
			Log4jUtil.debug("[ReflectUtil.invokeMethod]方法出现异常信息:{}", ExceptionUtil.getErrMsg(e));
		} catch (IllegalArgumentException e) {
			Log4jUtil.debug("[ReflectUtil.invokeMethod]方法出现异常信息:{}", ExceptionUtil.getErrMsg(e));
		} catch (IllegalAccessException e) {
			Log4jUtil.debug("[ReflectUtil.invokeMethod]方法出现异常信息:{}", ExceptionUtil.getErrMsg(e));
		} catch (NoSuchMethodException e) {
			Log4jUtil.debug("[ReflectUtil.invokeMethod]方法出现异常信息:{}", ExceptionUtil.getErrMsg(e));
		} catch (InvocationTargetException e) {
			Log4jUtil.debug("[ReflectUtil.invokeMethod]方法出现异常信息:{}", ExceptionUtil.getErrMsg(e));
		}
		return resObj;
	}
	
	/**
	 * 获取一个类对象
	 * @param classPath 类的全路径
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static Class getClass(String classPath){
		Class clz = null;
		try {
			clz = Class.forName(classPath);
		} catch (ClassNotFoundException e) {
			Log4jUtil.debug("[ReflectUtil.getClass]方法出现异常信息:{}", ExceptionUtil.getErrMsg(e));
		}
		return clz;
	}
	
	/**
	 * 创建一个类的实例
	 * @param clz 类对象
	 * @return
	 */
	public static <T> T newInstance(Class<T> clz){
		T t = null;
		try {
			t = (T) clz.newInstance();
		} catch (InstantiationException e) {
			Log4jUtil.debug("[ReflectUtil.newInstance]方法出现异常信息:{}", ExceptionUtil.getErrMsg(e));
		} catch (IllegalAccessException e) {
			Log4jUtil.debug("[ReflectUtil.newInstance]方法出现异常信息:{}", ExceptionUtil.getErrMsg(e));
		}
		return t;
	}
	
	/**
	 * 创建一个类的实例
	 * @param classpath 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T newInstance(String classpath){
		Class<T> clz = getClass(classpath);
		return newInstance(clz);
	}
	
	/**
	 * 判断一个类是否存在
	 * @param classpath
	 * @return
	 */
	public static boolean classIsExists(String classpath){
		try {
			// 在反射创建这个类的时候，不会调用到类的static块，属性，和方法，效率高于class.forName
			// 和loadClass正好相反，在只是判断一个类是否存在的情况下，不需要执行类的static块，属性，和方法，以提高效率
			Thread.currentThread().getContextClassLoader().loadClass(classpath);
//			Class.forName(classpath);// 在创建类的实例的时候，会调用类的static块，属性，和方法
			return true;
		} catch (ClassNotFoundException e) {
			return false;
		}
	}
}
