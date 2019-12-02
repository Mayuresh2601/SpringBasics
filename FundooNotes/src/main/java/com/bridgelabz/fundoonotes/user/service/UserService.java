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
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.bridgelabz.fundoonotes.user.dto.LoginDTO;
import com.bridgelabz.fundoonotes.user.dto.RegisterDTO;
import com.bridgelabz.fundoonotes.user.dto.ResetDTO;
import com.bridgelabz.fundoonotes.user.exception.FileNotAcceptingException;
import com.bridgelabz.fundoonotes.user.exception.LoginException;
import com.bridgelabz.fundoonotes.user.exception.ProfilePictureException;
import com.bridgelabz.fundoonotes.user.model.User;
import com.bridgelabz.fundoonotes.user.repository.UserRepositoryI;
import com.bridgelabz.fundoonotes.user.utility.Jms;
import com.bridgelabz.fundoonotes.user.utility.Jwt;

@Service
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
				user.setPassword(bCryptPasswordEncoder.encode(regdto.getPassword()));
				String token = jwt.createToken(user.getEmail());
			
				jms.sendMail(user.getEmail(),token);
				userrepository.save(user);
				return UserMessageReferance.ADD_USER;
			}
		}
		catch(NullPointerException e) {
			return "Null Pointer Exception";
		}
		throw new LoginException(UserMessageReferance.UNAUTHORIZED_USER);
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
		return UserMessageReferance.DELETE_USER;
	}

	
	/**
	 *Method: Update User By its Id
	 */
	@Override
	public String updateUser(RegisterDTO registerdto, String id) {
		
		User userupdate = userrepository.findById(id).get();
		if(userupdate != null) {
			userupdate.setFirstName(registerdto.getFirstName());
			userupdate.setLastName(registerdto.getLastName());
			userupdate.setMobileNumber(registerdto.getMobileNumber());
			
			userrepository.save(userupdate);
			return UserMessageReferance.UPDATE_USER;
		}
		throw new LoginException(UserMessageReferance.UNAUTHORIZED_USER);
	}
	
	
	/**
	 *Method: Login User if password matches the existing user record
	 */
	@Override
	public String login(LoginDTO logindto, String token) {
		try {
			String email = jwt.getToken(token);
			if(email != null) {
				User user = mapper.map(logindto, User.class);
				User user1 = userrepository.findByEmail(user.getEmail());
				
				boolean isValid = bCryptPasswordEncoder.matches(logindto.getPassword(), user1.getPassword());
				
				if(isValid ) {	
					user1.setValidate(true);
					userrepository.save(user1);
					return UserMessageReferance.USER_LOGIN_SUCCESSFUL;
				}
			}
		}
		catch(NullPointerException er) {
			return "Null Pointer Exception";
		}
		throw new LoginException(UserMessageReferance.UNAUTHORIZED_USER);
	}
	
	
	/**
	 *Method: To generate JWT token of emailId and send it on mail
	 */
	@Override
	public String forgetPassword(RegisterDTO logindto) {
		try {
			User user = mapper.map(logindto, User.class);
			String token = jwt.createToken(user.getEmail());
			System.out.println("Recieved token:::::::  "+token);
			jms.sendMail(user.getEmail(), token);
	
			return UserMessageReferance.CHECK_YOUR_MAIL;
		}
		catch(NullPointerException e) {
			return "Null Pointer Exception";
		}
	}

	
	/**
	 *Method: To Reset your password using the obtained token from email
	 */
	@Override
	public String resetPassword(ResetDTO resetdto, String token) {
		try {
			String email=jwt.getToken(token);
			if(email!=null)
			{
				User user = userrepository.findByEmail(email);
				user.setPassword(resetdto.getNewPassword());
			
				if(resetdto.getNewPassword().contentEquals(resetdto.getConfirmPassword()))
				{
					user.setPassword(bCryptPasswordEncoder.encode(resetdto.getNewPassword()));
					userrepository.save(user);
					return UserMessageReferance.PASSWORD_UPDATED;
				}
			}
		}
		catch(NullPointerException e) {
			return "Null Pointer Exception";
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
			User user = userrepository.findByEmail(email);
			if(user!=null)
			{
				user.setValidate(true);
				userrepository.save(user);
				return email;
			}
		}
		throw new LoginException(UserMessageReferance.UNAUTHORIZED_USER);
	}


	/**
	 *Method: To Upload Profile Picture
	 */
	@SuppressWarnings("resource")
	@Override
	public String uploadProfilePicture(String token, MultipartFile file) throws IOException {
		String email = jwt.getToken(token);
		if(email != null) {
			
			File convertFile = new File("/home/admin1/Documents/"+file.getOriginalFilename());
			convertFile.createNewFile();
			boolean checker = file.getOriginalFilename().contains(".jpeg") || file.getOriginalFilename().contains(".jpg") || file.getOriginalFilename().contains(".png");
			if(!file.isEmpty() && checker) {
				
				FileOutputStream fout = new FileOutputStream(convertFile);
				String string = "/home/admin1/Documents/" + file.getOriginalFilename();
				User user = userrepository.findByEmail(email);
			
				if(user.getProfilePicture() == null) {
					user.setProfilePicture(string);
					userrepository.save(user);
		
					fout.write(file.getBytes());
					fout.close();
					return UserMessageReferance.PROFILE_PICTURE_UPLOADED;
				}
				
				if(user.getProfilePicture().equals(string)) {
					throw new ProfilePictureException(UserMessageReferance.PROFILE_PICTURE_EXISTED_EXCEPTION) ;
				}
			}
			throw new FileNotAcceptingException(UserMessageReferance.FILE_INVALID);
		}
		throw new LoginException(UserMessageReferance.UNAUTHORIZED_USER);
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
		
		if(email != null)
		{
			User user = userrepository.findByEmail(email);
			String pic = user.getProfilePicture();
			boolean checker = file.getOriginalFilename().contains(".jpeg") || file.getOriginalFilename().contains(".jpg") || file.getOriginalFilename().contains(".png");
			if(!file.isEmpty() && checker) {
				FileOutputStream fout = new FileOutputStream(convertFile);
				String string = "/home/admin1/Documents/" + file.getOriginalFilename();
				if(pic == null) {
					throw new ProfilePictureException(UserMessageReferance.PROFILE_NOT_FOUND_EXCEPTION) ;
				}
				
				if(user.getProfilePicture().equals(string)) {
					throw new ProfilePictureException(UserMessageReferance.PROFILE_PICTURE_EXISTED_EXCEPTION);
				}
				
				user.setProfilePicture(string);
				userrepository.save(user);
	
				fout.write(file.getBytes());
				fout.close();
				return UserMessageReferance.PROFILE_PICTURE_UPLOADED;
			}
			throw new FileNotAcceptingException(UserMessageReferance.FILE_INVALID);
		}
		throw new LoginException(UserMessageReferance.UNAUTHORIZED_USER);
	}
	
	
	/**
	 *Method: To Remove Profile Picture
	 */
	@Override
	public String removeProfilePicture(String token) {
		
		String email = jwt.getToken(token);
		User user = userrepository.findByEmail(email);
		
		if(email != null) {
			
			if(user.getProfilePicture() == null) {
				throw new ProfilePictureException(UserMessageReferance.PROFILE_PICTURE_REMOVE_EXCEPTION) ;
			}
			
			user.setProfilePicture(null);
			userrepository.save(user);
			return UserMessageReferance.PROFILE_PICTURE_DELETED;
		}
		throw new LoginException(UserMessageReferance.UNAUTHORIZED_USER);
	}

}

