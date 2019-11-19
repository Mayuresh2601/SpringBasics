package com.bridgelabz.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.model.Student;
import com.bridgelabz.service.RecordServices;

@RestController
public class RecordController {
	
	@Autowired
	private RecordServices recordService;
	
	@RequestMapping("/")
	public String login() {
		
		return recordService.Login();
	}
	
	@RequestMapping("/reqister")
	public String Register() {
		
		return recordService.Register();
	}
	
	@RequestMapping("/all")
	public List<Student> getAllRecord() {
		
		return recordService.getAllRecord();
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/all")
	public String AddRecord(Student student) {
		
		return recordService.AddRecord(student);
	}
	
	@RequestMapping(method = RequestMethod.PUT, value = "/update")
	public String UpdateRecord(Student student) {
		
		return recordService.AddRecord(student);
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/delete")
	public String DeleteRecord(String userName) {
		
		return recordService.DeleteRecord(userName);
	}
	
	@RequestMapping("/all")
	public String Authentication(String userName, String password) {
		
		return recordService.Autentication(userName, password);
	}

	
	
	
	

}
