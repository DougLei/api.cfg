package com.king.tooth.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @author DougLei
 */
public class SpringContextHelper implements ApplicationContextAware {
	/*
	 * 需要在 spring.xml中配置
	 * <!-- spring容器启动时，将applicationContext注入到该类的属性中 -->
	 * <bean id="springContextHelper" class="com.king.tooth.util.SpringContextHelper" scope="singleton"></bean>
	 */

	// spring容器在启动的时候，会根据set方法自动注入
	private static ApplicationContext applicationContext;
	public void setApplicationContext(ApplicationContext ac) throws BeansException {
		applicationContext = ac;
	}
	
	
	/**
	 * 根据类型，获取spring容器中管理的bean对象
	 * @param classType
	 */
	public static <T> T getBean(Class<T> classType){
		return (T)applicationContext.getBean(classType);
	}
	
	/**
	 * 根据名称，获取spring容器中管理的bean对象
	 * @param name
	 */
	public static Object getBean(String name){
		return applicationContext.getBean(name);
	}
}