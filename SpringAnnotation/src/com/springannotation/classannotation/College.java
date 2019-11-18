package com.springannotation.classannotation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class College {
	
	@Value("${college.Name}")
	private String Name;
	
	@Autowired
	private Principle principle;
	
	@Autowired
	@Qualifier("scienceTeacher")
	private Teacher teacher;

	public void test() {
		System.out.println("Testing the Calling Method");
		principle.principleInfo();
		teacher.teaches();
		System.out.println("College name is "+ Name);
	}
}
