package com.springannotation.classannotation;

import org.springframework.stereotype.Component;

@Component
public class ScienceTeacher implements Teacher{

	@Override
	public void teaches() {
		
		System.out.println("Hie, I am Science Teacher");
		System.out.println("My Name is Vikrant");
	}
	

}
