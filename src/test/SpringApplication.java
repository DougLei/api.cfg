package test;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringApplication {
	private static final ClassPathXmlApplicationContext cp = new ClassPathXmlApplicationContext("spring.xml");
	
	public static ClassPathXmlApplicationContext getApplicationContext(){
		return cp;
	}
}
