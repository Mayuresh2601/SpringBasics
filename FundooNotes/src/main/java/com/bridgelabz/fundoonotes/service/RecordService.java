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
	
	@Override
	public String addNewUser(RegisterDTO regdto) {
		ModelMapper mapper=new ModelMapper();
		User user = mapper.map(regdto, User.class);
		
		user.setFirstName(regdto.getFirstName());
		user.setLastName(regdto.getLastName());
		user.setEmail(regdto.getEmail());
		user.setMobileNumber(regdto.getMobileNumber());
		user.setPassword(bCryptPasswordEncoder.encode(regdto.getPassword()));
		String token = jwt.createToken(user.getEmail());
		
		jms.sendMail(user.getEmail(),token);
		repository.save(user);
		return "User Added Successfully";
	}

	@Override
	public Optional<User> findUserById(String id) {
		
		return repository.findById(id);
	}

	@Override
	public List<User> show() {
		
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
	
	@Override
	public String login(LoginDTO logindto) {
		ModelMapper mapper = new ModelMapper();
		User user = mapper.map(logindto, User.class);
		User user1 = repository.findByEmail(user.getEmail());
		
		boolean isValid = bCryptPasswordEncoder.matches(logindto.getPassword(), user1.getPassword());
		
		if(isValid ) {	
			user1.setValidate(true);
			repository.save(user1);
			return "Login Successful";
		}
		else {
			return "Login Unsuccessful";
		}
		
	}
	
	@Override
	public String forgetPassword(RegisterDTO logindto) {
		
		ModelMapper mapper = new ModelMapper();
		User user = mapper.map(logindto, User.class);
		String token = jwt.createToken(user.getEmail());
		System.out.println("Recieved token:::::::  "+token);
		jms.sendMail("demo.mayuresh@gmail.com", token);

		return "Check Your Mail for Validation Link";
	}

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
				return "Password Reset Successful";
			}
			
		}
		return "Password Reset Unsuccessful";
		
	}

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
				return "Verified Successful";
			}
		}
		return "Invalid UserName and Password";
	}
}

