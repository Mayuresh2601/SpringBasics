/******************************************************************************
*  
*  Purpose: To Implement Spring Demo Project
*  @author  Mayuresh Sunil Sonar
*
******************************************************************************/
package com.springdemo;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Mobile {

	public static void main(String[] args) {
		
		ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
		
		Sim sim = context.getBean("sim",Sim.class);
		sim.calling();
		sim.data();

	}

}
