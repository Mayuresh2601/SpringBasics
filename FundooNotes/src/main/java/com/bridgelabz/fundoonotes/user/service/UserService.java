/******************************************************************************
*  
*  Purpose: To Implement Fundoo Notes App
*  @class Record Service
*  @author  Mayuresh Sunil Sonar
*
******************************************************************************/
package com.bridgelabz.fundoonotes.user.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.bridgelabz.fundoonotes.user.dto.ForgetDTO;
import com.bridgelabz.fundoonotes.user.dto.LoginDTO;
import com.bridgelabz.fundoonotes.user.dto.RegisterDTO;
import com.bridgelabz.fundoonotes.user.dto.ResetDTO;
import com.bridgelabz.fundoonotes.user.dto.UpdateDTO;
import com.bridgelabz.fundoonotes.user.exception.FileNotAcceptingException;
import com.bridgelabz.fundoonotes.user.exception.LoginException;
import com.bridgelabz.fundoonotes.user.exception.ProfilePictureException;
import com.bridgelabz.fundoonotes.user.model.User;
import com.bridgelabz.fundoonotes.user.repository.UserRepositoryI;
import com.bridgelabz.fundoonotes.user.utility.Jms;
import com.bridgelabz.fundoonotes.user.utility.Jwt;

@Service
@PropertySource("classpath:message.properties")
public class UserService implements UserServiceI{
	
	@Autowired
	UserRepositoryI userrepository;
	
	@Autowired
	JavaMailSender mailSender;
	
	@Autowired
	Jwt jwt;
	
	@Autowired
	Jms jms;

	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	ModelMapper mapper;
	
	@Autowired
	Environment userEnvironment;
	
	/**
	 *Method: To Add New User into Database
	 */
	@Override
	public String createUser(RegisterDTO regdto) {
		try {
			User user = mapper.map(regdto, User.class);
			
			if(user != null) {
				user.setFirstName(regdto.getFirstName());
				user.setLastName(regdto.getLastName());
				user.setEmail(regdto.getEmail());
				user.setMobileNumber(regdto.getMobileNumber());
				
				boolean checker = regdto.getPassword().equals(regdto.getConfirmPassword());
				if(checker) {
					user.setPassword(bCryptPasswordEncoder.encode(regdto.getPassword()));
				}
				else {
					return userEnvironment.getProperty("PASSWORD_NOT_MATCH");
				}
				
				String token = jwt.createToken(user.getEmail());
				jms.sendMail(user.getEmail(),token);
				userrepository.save(user);
				return userEnvironment.getProperty("ADD_USER");
			}
			throw new LoginException(userEnvironment.getProperty("UNAUTHORIZED_USER"));
		}
		catch(NullPointerException e) {
			return userEnvironment.getProperty("Null_Pointer_Exception");
		}
	}

	
	/**
	 *Method: To find User By Id in Database
	 */
	@Override
	public Optional<User> findUserById(String id) {
		
		return userrepository.findById(id); 
	}

	
	/**
	 *Method: Display All User Details Present in Database
	 */
	@Override
	public List<User> showUsers() {
		
		return userrepository.findAll(); 
	}

	
	/**
	 *Method: To Delete a User by its Id 
	 */
	@Override
	public String deleteUserById(String id) {
		
		userrepository.deleteById(id);
		return userEnvironment.getProperty("DELETE_USER");
	}

	
	/**
	 *Method: Update User By its Id
	 */
	@Override
	public String updateUser(UpdateDTO updatedto, String token) {
		String email = jwt.getToken(token);
		User user = userrepository.findByEmail(email);
		
		if(email.equals(user.getEmail())){
			user.setFirstName(updatedto.getFirstName());
			user.setLastName(updatedto.getLastName());
			user.setMobileNumber(updatedto.getMobileNumber());
				
			userrepository.save(user);
			return userEnvironment.getProperty("UPDATE_USER");
		}
		throw new LoginException(userEnvironment.getProperty("UNAUTHORIZED_USER"));
	}
	
	
	/**
	 *Method: Login User if password matches the existing user record
	 */
	@Override
	public String login(LoginDTO logindto, String token) {
		String email = jwt.getToken(token);
		User user = userrepository.findByEmail(email);
		if(logindto.getEmail().equals(user.getEmail())) {
				
			boolean isValid = bCryptPasswordEncoder.matches(logindto.getPassword(), user.getPassword());
			
			if(isValid ) {	
				user.setValidate(true);
				userrepository.save(user);
				return userEnvironment.getProperty("USER_LOGIN_SUCCESSFUL");
			}
		}
		throw new LoginException(userEnvironment.getProperty("UNAUTHORIZED_USER"));
	}
	
	
	/**
	 *Method: To generate JWT token of emailId and send it on mail
	 */
	@SuppressWarnings("unlikely-arg-type")
	@Override
	public String forgetPassword(ForgetDTO forget) {
		
		List<User> userlist = showUsers();
		
		if(userlist.contains(forget.getEmail())) {
			User user = mapper.map(forget, User.class);
			String token = jwt.createToken(user.getEmail());
			System.out.println("Recieved token:::::::  "+token);
			jms.sendMail(user.getEmail(), token);
			return userEnvironment.getProperty("CHECK_YOUR_MAIL");
		}
		throw new LoginException(userEnvironment.getProperty("UNAUTHORIZED_USER"));
	}

	
	/**
	 *Method: To Reset your password using the obtained token from email
	 */
	@Override
	public String resetPassword(ResetDTO resetdto, String token) {
		try {
			String email=jwt.getToken(token);
			User user = userrepository.findByEmail(email);
			if(email.equals(user.getEmail()))
			{
				user.setPassword(resetdto.getNewPassword());
			
				if(resetdto.getNewPassword().contentEquals(resetdto.getConfirmPassword()))
				{
					user.setPassword(bCryptPasswordEncoder.encode(resetdto.getNewPassword()));
					userrepository.save(user);
					return userEnvironment.getProperty("PASSWORD_UPDATED");
				}
				return userEnvironment.getProperty("PASSWORD_NOT_MATCHED");
			}
			throw new LoginException(userEnvironment.getProperty("UNAUTHORIZED_USER"));
		}
		catch(NullPointerException e) {
			return userEnvironment.getProperty("Null_Pointer_Exception");
		}
	}

	
	/**
	 *Method: To Verify the EmailId and Password  
	 */
	@Override
	public String verify(String token) {
		try {
			String email=jwt.getToken(token);
			User user = userrepository.findByEmail(email);
			
			if(email.equals(user.getEmail()))
			{
				user.setValidate(true);
				userrepository.save(user);
				return email;
			}
			throw new LoginException(userEnvironment.getProperty("UNAUTHORIZED_USER"));
		}
		catch (NullPointerException e) {
			return userEnvironment.getProperty("Null_Pointer_Exception");
		}
	}


	/**
	 *Method: To Upload Profile Picture
	 */
	@SuppressWarnings("resource")
	@Override
	public String uploadProfilePicture(String token, MultipartFile file) throws IOException {
		String email = jwt.getToken(token);
		User user = userrepository.findByEmail(email);
		if(email.equals(user.getEmail())) {
			
			File convertFile = new File("/home/admin1/Documents/"+file.getOriginalFilename());
			convertFile.createNewFile();
			boolean checker = file.getOriginalFilename().contains(".jpeg") || file.getOriginalFilename().contains(".jpg") || file.getOriginalFilename().contains(".png");
			if(!file.isEmpty() && checker) {
				
				FileOutputStream fout = new FileOutputStream(convertFile);
				String path = "/home/admin1/Documents/" + file.getOriginalFilename();
			
				if(user.getProfilePicture() == null) {
					user.setProfilePicture(path);
					userrepository.save(user);
		
					fout.write(file.getBytes());
					fout.close();
					return userEnvironment.getProperty("PROFILE_PICTURE_UPLOADED");
				}
				
				if(user.getProfilePicture().equals(path)) {
					throw new ProfilePictureException(userEnvironment.getProperty("PROFILE_PICTURE_EXISTED_EXCEPTION")) ;
				}
			}
			throw new FileNotAcceptingException(userEnvironment.getProperty("FILE_INVALID"));
		}
		throw new LoginException(userEnvironment.getProperty("UNAUTHORIZED_USER"));
	}


	/**
	 *Method: To Update Profile Picture
	 */
	@SuppressWarnings("resource")
	@Override
	public String updateProfilePicture(String token, MultipartFile file) throws IOException {
		
		String email = jwt.getToken(token);
		File convertFile = new File("/home/admin1/Documents/"+file.getOriginalFilename());
		convertFile.createNewFile();
		User user = userrepository.findByEmail(email);
		if(email.equals(user.getEmail()))
		{
			String photo = user.getProfilePicture();
			boolean checker = file.getOriginalFilename().contains(".jpeg") || file.getOriginalFilename().contains(".jpg") || file.getOriginalFilename().contains(".png");
			
			if(!file.isEmpty() && checker) {
				FileOutputStream fout = new FileOutputStream(convertFile);
				String path = "/home/admin1/Documents/" + file.getOriginalFilename();
				
				if(photo == null) {
					throw new ProfilePictureException(userEnvironment.getProperty("PROFILE_NOT_FOUND_EXCEPTION")) ;
				}
				
				if(user.getProfilePicture().equals(photo)) {
					throw new ProfilePictureException(userEnvironment.getProperty("PROFILE_PICTURE_EXISTED_EXCEPTION"));
				}
				
				user.setProfilePicture(path);
				userrepository.save(user);
	
				fout.write(file.getBytes());
				fout.close();
				return userEnvironment.getProperty("PROFILE_PICTURE_UPLOADED");
			}
			throw new FileNotAcceptingException(userEnvironment.getProperty("FILE_INVALID"));
		}
		throw new LoginException(userEnvironment.getProperty("UNAUTHORIZED_USER"));
	}
	
	
	/**
	 *Method: To Remove Profile Picture
	 */
	@Override
	public String removeProfilePicture(String token) {
		
		String email = jwt.getToken(token);
		User user = userrepository.findByEmail(email);
		
		if(email.equals(user.getEmail())) {
			
			if(user.getProfilePicture() == null) {
				throw new ProfilePictureException(userEnvironment.getProperty("PROFILE_PICTURE_REMOVE_EXCEPTION")) ;
			}
			user.setProfilePicture(null);
			userrepository.save(user);
			return userEnvironment.getProperty("PROFILE_PICTURE_DELETED");
		}
		throw new LoginException(userEnvironment.getProperty("UNAUTHORIZED_USER"));
	}

}

