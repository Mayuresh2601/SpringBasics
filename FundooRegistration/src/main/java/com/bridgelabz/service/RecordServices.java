package com.bridgelabz.service;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bridgelabz.dto.RegisterDTO;
import com.bridgelabz.model.User;
import com.bridgelabz.repository.UserRepository;

@Service
public class RecordServices implements RecordServiceI{
	
	UserRepository repository;
	
	@Autowired
	ModelMapper mapper;
	
//	@Override
//	public String Login() {
//		
//		return "Login Successfully Done";
//	}
//
//	@Override
//	public String Register() {
//		
//		return "Registration Successfully Done";
//	}
//	
//	@Override
//	public String Autentication(String userName, String password) {
//		for (int i = 0; i < students.size(); i++) {
//			if(students.get(i).getUserName().equals(userName)) {
//				if (students.get(i).getPassword().equals(password)) {
//					return "Authorized User";
//				}
//			}
//		}
//		return "Unauthorized User";
//	}
//
//	@Override
//	public String AddRecord(Student student) {
//		students.add(student);
//		return "Record Successfully Added";
//	}
//	
//	@Override
//	public String UpdateRecord(int id,Student student) {
//		
//		for (int i = 0; i < students.size(); i++) {
//			Student s = students.get(i);
//			if(s.getId() == id) {
//				students.set(i, student);
//				return "Record Successfully Updated";
//			}
//		}
//		return "Problem";
//		
//	}
//
//	@Override
//	public String DeleteRecord(String userName) {
//		
//		if(students.removeIf(t -> t.getUserName().equals(userName))) {
//			return "Record Successfully Deleted";
//		}
//		else {
//			return "Problem";
//		}
//	}
//
//	@Override
//	public List<Student> getAllRecord() {
//		
//		return students;
//	}
//
//	@Override
//	public Student getRecordById(int id) {
//		
//		return students.stream().filter(t -> t.getId() == id).findFirst().get();
//		
//	}

	@Override
	public String addNewUser(RegisterDTO regdto) {
		User stu = mapper.map(regdto, User.class);
		repository.save(stu);
		return "User Added Successfully";
	}

	@Override
	public Optional<User> findUserById(String id) {
		
		return repository.findById(id);
	}

	@Override
	public List<User> show() {
		// TODO Auto-generated method stub
		return repository.findAll();
	}

	@Override
	public String deleteUserById(String id) {
		
		repository.findById(id);
		return "User Deleted Successfully";
	}

	@Override
	public String updateUser(User user,String id) {
		
		User userupdate = repository.findById(id).get();
		userupdate = user;
		repository.save(userupdate);
		return "User Updated Successfully";
	}


}
