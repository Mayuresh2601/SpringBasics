/******************************************************************************
*  
*  Purpose: To Implement Setter & Constructor Dependencies
*  @author  Mayuresh Sunil Sonar
*
******************************************************************************/
package com.setterconstructordependencies;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Exam {

	public static void main(String[] args) {
		
		ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
		
		Student stu = context.getBean("Student",Student.class);
		
		stu.displayStudentInfo();
		

	}

}
