package com.springannotation.classannotation;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main{
	
	public static void main(String[] args) {
		
		ApplicationContext context = new AnnotationConfigApplicationContext(CollegeConfig.class);
		
		College colege = context.getBean("college", College.class);
		
		colege.test();
	}
}