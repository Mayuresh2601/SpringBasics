package com.bridgelabz.fundoonotes.user.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

import com.bridgelabz.fundoonotes.user.dto.ForgetDTO;
import com.bridgelabz.fundoonotes.user.dto.LoginDTO;
import com.bridgelabz.fundoonotes.user.dto.RegisterDTO;
import com.bridgelabz.fundoonotes.user.dto.ResetDTO;
import com.bridgelabz.fundoonotes.user.dto.UpdateDTO;
import com.bridgelabz.fundoonotes.user.model.User;

public interface UserServiceI {
	
	public String createUser(RegisterDTO regdto);
	
	public Optional<User> findUserById(String id);
	
	public List<User> showUsers();
	
	public String deleteUserById(String id);
	
	public String updateUser(UpdateDTO updatedto, String token);

	public String login(LoginDTO logindto, String token);
	
	public String forgetPassword(ForgetDTO forget);
	
	public String resetPassword(ResetDTO reset, String token);
	
	public String verify(String token);
	
	public String uploadProfilePicture(String token, MultipartFile file) throws IOException;
	
	public String updateProfilePicture(String token, MultipartFile file) throws IOException;
	
	public String removeProfilePicture(String token);
}
