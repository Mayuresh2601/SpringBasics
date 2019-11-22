package com.bridgelabz.fundoonotes.service;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.bridgelabz.fundoonotes.dto.LoginDTO;
import com.bridgelabz.fundoonotes.dto.RegisterDTO;
import com.bridgelabz.fundoonotes.model.User;
import com.bridgelabz.fundoonotes.repository.UserRepositoryI;
import com.bridgelabz.fundoonotes.utility.JMS;
import com.bridgelabz.fundoonotes.utility.JWT;

@Service
public class RecordService implements RecordServiceI{
	
	@Autowired
	UserRepositoryI repository;
	
	@Autowired
	JWT jwt;
	
	@Autowired
	JMS jms;

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
		
		jms.sendMail(user.getEmail(), token);
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
	
	public String login(LoginDTO logindto) {
		ModelMapper mapper = new ModelMapper();
		User user = mapper.map(logindto, User.class);
		
		if(user != null) {
			
		}
		return "";
	}


}
