/******************************************************************************
*  
*  Purpose: To Implement Autowiring Annotation
*  @author  Mayuresh Sunil Sonar
*
******************************************************************************/
package com.springautowiring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class HumanMain {
	
	public static void main(String[] args) {
		
		ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
		
		Body body = context.getBean("Body", Body.class);
		
		body.ispumping();
	}

}
