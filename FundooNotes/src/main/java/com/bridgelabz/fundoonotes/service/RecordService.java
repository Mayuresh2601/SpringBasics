/******************************************************************************
*  
*  Purpose: To Implement Fundoo Notes App
*  @class Record Service
*  @author  Mayuresh Sunil Sonar
*
******************************************************************************/
package com.bridgelabz.fundoonotes.service;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.bridgelabz.fundoonotes.dto.LoginDTO;
import com.bridgelabz.fundoonotes.dto.RegisterDTO;
import com.bridgelabz.fundoonotes.dto.ResetDTO;
import com.bridgelabz.fundoonotes.exception.LoginException;
import com.bridgelabz.fundoonotes.exception.RegisterException;
import com.bridgelabz.fundoonotes.model.User;
import com.bridgelabz.fundoonotes.repository.UserRepositoryI;
import com.bridgelabz.fundoonotes.utility.Jms;
import com.bridgelabz.fundoonotes.utility.Jwt;

@Service
public class RecordService implements RecordServiceI{
	
	@Autowired
	UserRepositoryI repository;
	
	@Autowired
	JavaMailSender mailSender;
	
	@Autowired
	Jwt jwt;
	
	@Autowired
	Jms jms;

	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;
	
	
	/**
	 *Method: To Add New User into Database
	 */
	@Override
	public String addNewUser(RegisterDTO regdto) {
		ModelMapper mapper=new ModelMapper();
		User user = mapper.map(regdto, User.class);
		
		if(user != null) {
			user.setFirstName(regdto.getFirstName());
			user.setLastName(regdto.getLastName());
			user.setEmail(regdto.getEmail());
			user.setMobileNumber(regdto.getMobileNumber());
			user.setPassword(bCryptPasswordEncoder.encode(regdto.getPassword()));
			String token = jwt.createToken(user.getEmail());
		
			jms.sendMail(user.getEmail(),token);
			repository.save(user);
			return UserMessageReferance.ADD_USER;
		}
		throw new RegisterException(UserMessageReferance.USER_LOGIN_UNSUCCESSFUL);
	}

	
	/**
	 *Method: To find User By Id in Database
	 */
	@Override
	public Optional<User> findUserById(String id) {
		
		return repository.findById(id); 
	}

	
	/**
	 *Method: Display All User Details Present in Database
	 */
	@Override
	public List<User> show() {
		
		return repository.findAll(); 
	}

	
	/**
	 *Method: To Delete a User by its Id 
	 */
	@Override
	public String deleteUserById(String id) {
		
		repository.deleteById(id);
		return UserMessageReferance.DELETE_USER;
	}

	
	/**
	 *Method: Update User By its Id
	 */
	@Override
	public String updateUser(RegisterDTO registerdto,String token) {
		
		String email = jwt.getToken(token);
		if(email != null) {
			User userupdate = repository.findByEmail(email);
			userupdate.setFirstName(registerdto.getFirstName());
			userupdate.setLastName(registerdto.getLastName());
			userupdate.setMobileNumber(registerdto.getMobileNumber());
			
			repository.save(userupdate);
			return UserMessageReferance.UPDATE_USER;
		}
		throw new LoginException(UserMessageReferance.UNAUTHORIZED_USER);
	}
	
	
	/**
	 *Method: Login User if password matches the existing user record
	 */
	@Override
	public String login(LoginDTO logindto) {
		ModelMapper mapper = new ModelMapper();
		User user = mapper.map(logindto, User.class);
		User user1 = repository.findByEmail(user.getEmail());
		
		boolean isValid = bCryptPasswordEncoder.matches(logindto.getPassword(), user1.getPassword());
		
		if(isValid ) {	
			user1.setValidate(true);
			repository.save(user1);
			return UserMessageReferance.USER_LOGIN_SUCCESSFUL;
		}
		throw new LoginException(UserMessageReferance.USER_LOGIN_UNSUCCESSFUL);
	}
	
	
	/**
	 *Method: To generate JWT token of emailId and send it on mail
	 */
	@Override
	public String forgetPassword(RegisterDTO logindto) {
		
		ModelMapper mapper = new ModelMapper();
		User user = mapper.map(logindto, User.class);
		String token = jwt.createToken(user.getEmail());
		System.out.println("Recieved token:::::::  "+token);
		jms.sendMail("demo.mayuresh@gmail.com", token);

		return UserMessageReferance.CHECK_YOUR_MAIL;
	}

	
	/**
	 *Method: To Reset your password using the obtained token from email
	 */
	@Override
	public String resetPassword(ResetDTO resetdto, String token) {
	
		String email=jwt.getToken(token);
		if(email!=null)
		{
			User user = repository.findByEmail(email);
			user.setPassword(resetdto.getNewPassword());
		
			if(resetdto.getNewPassword().contentEquals(resetdto.getConfirmPassword()))
			{
				user.setPassword(bCryptPasswordEncoder.encode(resetdto.getNewPassword()));
				repository.save(user);
				return UserMessageReferance.PASSWORD_UPDATED;
			}
		}
		return UserMessageReferance.PASSWORD_NOT_MATCHED;
	}

	
	/**
	 *Method: To Verify the EmailId and Password  
	 */
	@Override
	public String verify(String token) {
		String email=jwt.getToken(token);
		
		if(email!=null)
		{
			User user = repository.findByEmail(email);
			if(user!=null)
			{
				user.setValidate(true);
				repository.save(user);
				return UserMessageReferance.VERIFIED_EMAILID_PASSWORD;
			}
		}
		throw new LoginException(UserMessageReferance.UNAUTHORIZED_USER);
	}
}

