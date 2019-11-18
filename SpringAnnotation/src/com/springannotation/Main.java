/******************************************************************************
*  
*  Purpose: To Implement Basic Annotation
*  @author  Mayuresh Sunil Sonar
*
******************************************************************************/
package com.springannotation;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {
	
	public static void main(String[] args) {
		
		ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
		
		Student stu = context.getBean("stu", Student.class);
		
		stu.displayInfo();
	}

}
