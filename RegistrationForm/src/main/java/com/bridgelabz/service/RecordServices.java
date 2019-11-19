package com.bridgelabz.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.bridgelabz.model.Student;

@Service
public class RecordServices implements RecordServiceI{

	List<Student> students = new ArrayList<>();
	
	@Override
	public String Login() {
		
		return "Login Successfully Done";
	}

	@Override
	public String Register() {
		
		return "Registration Successfully Done";
	}
	
	@Override
	public String Autentication(String userName, String password) {
		for (int i = 0; i < students.size(); i++) {
			if(students.get(i).getUserName().equals(userName)) {
				if (students.get(i).getPassword().equals(password)) {
					return "Authorized User";
				}
			}
		}
		return "Unauthorized User";
	}

	@Override
	public String AddRecord(Student student) {
		students.add(student);
		return "Record Successfully Added";
	}
	
	@Override
	public String UpdateRecord(int id,Student student) {
		
		for (int i = 0; i < students.size(); i++) {
			Student s = students.get(i);
			if(s.getId() == id) {
				students.set(i, student);
				return "Record Successfully Updated";
			}
		}
		return "Problem";
		
	}

	@Override
	public String DeleteRecord(String userName) {
		
		if(students.removeIf(t -> t.getUserName().equals(userName))) {
			return "Record Successfully Deleted";
		}
		else {
			return "Problem";
		}
	}

	@Override
	public List<Student> getAllRecord() {
		
		return students;
	}

	@Override
	public Student getRecordById(int id) {
		
		return students.stream().filter(t -> t.getId() == id).findFirst().get();
		
	}

}
