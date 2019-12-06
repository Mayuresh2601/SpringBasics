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
import com.bridgelabz.fundoonotes.user.response.Response;
import com.bridgelabz.fundoonotes.user.utility.Jms;
import com.bridgelabz.fundoonotes.user.utility.Jwt;

@Service
@PropertySource("classpath:message.properties")
public class UserService implements UserServiceI{
	
	@Autowired
	private UserRepositoryI userrepository;
	
	@Autowired
	private Jwt jwt;
	
	@Autowired
	private Jms jms;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	private ModelMapper mapper;
	
	@Autowired
	private Environment userEnvironment;
	
	/**
	 *Method: To Add New User into Database
	 */
	@Override
	public Response createUser(RegisterDTO regdto) {
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
					throw new LoginException(userEnvironment.getProperty("PASSWORD_NOT_MATCH"));
				}
				
				String token = jwt.createToken(user.getEmail());
				jms.sendMail(user.getEmail(),token);
				userrepository.save(user);
				return new Response(200, userEnvironment.getProperty("ADD_USER"), userEnvironment.getProperty("Add_User"));
			}
			throw new LoginException(userEnvironment.getProperty("UNAUTHORIZED_USER"));
		}
		catch(NullPointerException e) {
			return new Response(200, userEnvironment.getProperty("NULL_POINTER_EXCEPTION"), userEnvironment.getProperty("NULL_POINTER_EXCEPTION"));
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
	public Response deleteUserById(String id) {
		
		userrepository.deleteById(id);
		return new Response(200, userEnvironment.getProperty("DELETE_USER"), userEnvironment.getProperty("Delete_User"));
	}

	
	/**
	 *Method: Update User By its Id
	 */
	@Override
	public Response updateUser(UpdateDTO updatedto, String token) {
		String email = jwt.getEmailId(token);
		User user = userrepository.findByEmail(email);
		
		if(email.equals(user.getEmail())){
			user.setFirstName(updatedto.getFirstName());
			user.setLastName(updatedto.getLastName());
			user.setMobileNumber(updatedto.getMobileNumber());
				
			userrepository.save(user);
			return new Response(200, userEnvironment.getProperty("UPDATE_USER"), userEnvironment.getProperty("Update_User"));
		}
		throw new LoginException(userEnvironment.getProperty("UNAUTHORIZED_USER"));
	}
	
	
	/**
	 *Method: Login User if password matches the existing user record
	 */
	@Override
	public Response login(LoginDTO logindto, String token) {
		String email = jwt.getEmailId(token);
		User user = userrepository.findByEmail(email);
		if(logindto.getEmail().equals(user.getEmail())) {
				
			boolean isValid = bCryptPasswordEncoder.matches(logindto.getPassword(), user.getPassword());
			
			if(isValid ) {	
				user.setValidate(true);
				userrepository.save(user);
				return new Response(200, userEnvironment.getProperty("USER_LOGIN_SUCCESSFUL"), userEnvironment.getProperty("Login"));
			}
		}
		throw new LoginException(userEnvironment.getProperty("UNAUTHORIZED_USER"));
	}
	
	
	/**
	 *Method: To generate JWT token of emailId and send it on mail
	 */
	@SuppressWarnings("unlikely-arg-type")
	@Override
	public Response forgetPassword(ForgetDTO forget) {
		
		List<User> userlist = showUsers();
		
		if(userlist.contains(forget.getEmail())) {
			User user = mapper.map(forget, User.class);
			String token = jwt.createToken(user.getEmail());
			System.out.println("Recieved token:::::::  "+token);
			jms.sendMail(user.getEmail(), token);
			return new Response(200, userEnvironment.getProperty("CHECK_YOUR_MAIL"), userEnvironment.getProperty("Forget_Password"));
		}
		throw new LoginException(userEnvironment.getProperty("UNAUTHORIZED_USER"));
	}

	
	/**
	 *Method: To Reset your password using the obtained token from email
	 */
	@Override
	public Response resetPassword(ResetDTO resetdto, String token) {
		
		String email=jwt.getEmailId(token);
		User user = userrepository.findByEmail(email);
		if(email.equals(user.getEmail()))
		{
			user.setPassword(resetdto.getNewPassword());
			
			if(resetdto.getNewPassword().contentEquals(resetdto.getConfirmPassword()))
			{
				user.setPassword(bCryptPasswordEncoder.encode(resetdto.getNewPassword()));
				userrepository.save(user);
				return new Response(200, userEnvironment.getProperty("PASSWORD_UPDATED"), userEnvironment.getProperty("Reset_Password"));
			}
			throw new LoginException(userEnvironment.getProperty("PASSWORD_NOT_MATCHED"));
		}
		throw new LoginException(userEnvironment.getProperty("UNAUTHORIZED_USER"));
	}
	
	/**
	 *Method: To Verify the EmailId and Password  
	 */
	@Override
	public Response verify(String token) {

			String email=jwt.getEmailId(token);
			User user = userrepository.findByEmail(email);
			
			if(email.equals(user.getEmail()))
			{
				user.setValidate(true);
				userrepository.save(user);
				return new Response(200, userEnvironment.getProperty("PASSWORD_UPDATED"), userEnvironment.getProperty("PASSWORD_UPDATED"));
			}
			throw new LoginException(userEnvironment.getProperty("UNAUTHORIZED_USER"));
		}


	/**
	 *Method: To Upload Profile Picture
	 */
	@SuppressWarnings("resource")
	@Override
	public Response uploadProfilePicture(String token, MultipartFile file) throws IOException {
		String email = jwt.getEmailId(token);
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
					return new Response(200, userEnvironment.getProperty("PROFILE_PICTURE_UPLOADED"), userEnvironment.getProperty("Upload_Profile_Picture"));
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
	public Response updateProfilePicture(String token, MultipartFile file) throws IOException {
		
		String email = jwt.getEmailId(token);
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
				return new Response(200, userEnvironment.getProperty("PROFILE_PICTURE_UPDATED"), userEnvironment.getProperty("Update_Profile_Picture"));
			}
			throw new FileNotAcceptingException(userEnvironment.getProperty("FILE_INVALID"));
		}
		throw new LoginException(userEnvironment.getProperty("UNAUTHORIZED_USER"));
	}
	
	
	/**
	 *Method: To Remove Profile Picture
	 */
	@Override
	public Response removeProfilePicture(String token) {
		
		String email = jwt.getEmailId(token);
		User user = userrepository.findByEmail(email);
		
		if(email.equals(user.getEmail())) {
			
			if(user.getProfilePicture() == null) {
				throw new ProfilePictureException(userEnvironment.getProperty("PROFILE_PICTURE_REMOVE_EXCEPTION")) ;
			}
			user.setProfilePicture(null);
			userrepository.save(user);
			return new Response(200, userEnvironment.getProperty("PROFILE_PICTURE_DELETED"), userEnvironment.getProperty("Remove_Profile_Picture"));
		}
		throw new LoginException(userEnvironment.getProperty("UNAUTHORIZED_USER"));
	}

}

