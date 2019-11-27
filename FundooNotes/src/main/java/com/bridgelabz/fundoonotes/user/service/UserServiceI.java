package com.bridgelabz.fundoonotes.user.service;

import java.util.List;
import java.util.Optional;

import com.bridgelabz.fundoonotes.user.dto.LoginDTO;
import com.bridgelabz.fundoonotes.user.dto.RegisterDTO;
import com.bridgelabz.fundoonotes.user.dto.ResetDTO;
import com.bridgelabz.fundoonotes.user.model.User;

public interface UserServiceI {
	
	String createUser(RegisterDTO regdto);
	
	Optional<User> findUserById(String id);
	
	List<User> showUsers();
	
	String deleteUserById(String id);
	
	String updateUser(RegisterDTO registerdto, String id);

	String login(LoginDTO logindto, String token);
	
	String forgetPassword(RegisterDTO logindto);
	
	String resetPassword(ResetDTO reset, String token);
	
	String verify(String token);
}
