package com.springannotation;

import org.springframework.beans.factory.annotation.Value;

public class Student {
	
	@Value("Mayuresh Sonar")
	private String name;
	
	@Value("Java Developer")
	private String intrest;
	
	@Value("Dancing")
	private String hobbie;
	
	
	public void displayInfo() {
		
		System.out.println("Student Name: "+name);
		System.out.println("Student Intrest: "+intrest);
		System.out.println("Student Hobbie: "+hobbie);
	}
}
