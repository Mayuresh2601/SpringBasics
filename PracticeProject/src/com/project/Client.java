/******************************************************************************
*  
*  Purpose: To Implement Mini Project on Spring Core
*  @author  Mayuresh Sunil Sonar
*
******************************************************************************/
package com.project;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Client {
	
	public static void main(String[] args) {
	
	ApplicationContext context=new ClassPathXmlApplicationContext("project.xml");

	System.out.println("config loaded");
	Mobile mobiledemo = context.getBean("Mobiledemo", Mobile.class);
	mobiledemo.activation();
	}

}
