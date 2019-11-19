package com.bridgelabz.service;

import java.util.List;

import com.bridgelabz.model.Student;

public interface RecordServiceI {
	
	String Login();
	
	String Register();
	
	String Autentication(String userName,String password);
	
	String AddRecord(Student student);
	
	String UpdateRecord(int id, Student student);
	
	String DeleteRecord(String userName);
	
	List<Student> getAllRecord();
	
	Student getRecordById(int id);
	
	

}
