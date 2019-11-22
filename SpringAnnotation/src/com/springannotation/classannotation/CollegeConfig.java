package com.springannotation.classannotation;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan(basePackages = "com.springannotation.classannotation")
@PropertySource("classpath:collegeinfo.properties")
public class CollegeConfig {

	@Bean(name = "call")
	public College collegeBean() {
		
		return new College();
	}
	
	@Bean(name = {"princi","principledemo"})    //You can create multiple object variable of single class
	public Principle principle() {
		
		return new Principle();
	}

}
