package com.bridgelabz.fundoonotes.service;

import java.util.List;
import java.util.Optional;

import com.bridgelabz.fundoonotes.dto.LoginDTO;
import com.bridgelabz.fundoonotes.dto.RegisterDTO;
import com.bridgelabz.fundoonotes.dto.ResetDTO;
import com.bridgelabz.fundoonotes.model.User;

public interface RecordServiceI {
	
	String addNewUser(RegisterDTO regdto);
	
	Optional<User> findUserById(String id);
	
	List<User> show();
	
	String deleteUserById(String id);
	
	String updateUser(User student, String id);

	String login(LoginDTO logindto);
	
	String forgetPassword(RegisterDTO logindto);
	
	String resetPassword(ResetDTO reset, String token);
	
	String verify(String token);
}
