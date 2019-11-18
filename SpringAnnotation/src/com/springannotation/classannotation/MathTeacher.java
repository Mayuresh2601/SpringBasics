package com.springannotation.classannotation;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
public class MathTeacher implements Teacher{

	@Override
	public void teaches() {
		
		System.out.println("Hi, I am Sourabh teacher");
	}

}
