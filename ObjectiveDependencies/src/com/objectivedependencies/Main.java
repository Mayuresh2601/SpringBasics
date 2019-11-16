package com.objectivedependencies;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {

	public static void main(String[] args) {
		
		ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
		
		Student stu = context.getBean("Student", Student.class);
		
		stu.cheats();
		
		AnotherStudent a = context.getBean("AnotherStudent", AnotherStudent.class);
		
		a.cheats();

	}

}
