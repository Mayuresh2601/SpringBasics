package com.bridgelabz.service;

import java.util.List;
import java.util.Optional;

import com.bridgelabz.dto.RegisterDTO;
import com.bridgelabz.model.User;

public interface RecordServiceI {
	
	String addNewUser(RegisterDTO regdto);
	
	Optional<User> findUserById(String id);
	
	List<User> show();
	
	String deleteUserById(String id);
	
	String updateUser(User student, String id);

}
