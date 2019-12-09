package com.bridgelabz.fundoonotes.usertestcase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.multipart.MultipartFile;

import com.bridgelabz.fundoonotes.user.dto.ForgetDTO;
import com.bridgelabz.fundoonotes.user.dto.LoginDTO;
import com.bridgelabz.fundoonotes.user.dto.RegisterDTO;
import com.bridgelabz.fundoonotes.user.dto.ResetDTO;
import com.bridgelabz.fundoonotes.user.dto.UpdateDTO;
import com.bridgelabz.fundoonotes.user.model.User;
import com.bridgelabz.fundoonotes.user.repository.UserRepositoryI;
import com.bridgelabz.fundoonotes.user.response.Response;
import com.bridgelabz.fundoonotes.user.service.UserService;
import com.bridgelabz.fundoonotes.user.utility.Jms;
import com.bridgelabz.fundoonotes.user.utility.Jwt;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@TestPropertySource("classpath:message.properties")
public class UserServiceTests {
	
	@InjectMocks
	private UserService userService;
	
	@Mock
	private UserRepositoryI userRepo;
	
	@Mock
	private Jwt jwt;
	
	@Mock
	private Jms jms;
	
	@Mock
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Mock
	private ModelMapper mapper;
	
	@Mock
	private Environment noteEnv;

	@Mock
	private RegisterDTO regdto;
	
	@Mock
	private LoginDTO logindto;
	
	@Mock
	private UpdateDTO updatedto;
	
	@Mock
	private ResetDTO resetdto;
	
	@Mock
	private ForgetDTO forgetdto;
	
	@Mock
	private MultipartFile file;
	
	User user = new User();
	String userId = "5dea5dee836c8f441d888494";
	String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJlbWFpbElkIjoibXNzb25hcjI2QGdtYWlsLmNvbSJ9.PnmJiMaZVOJ03T5WgZU8k0VNEK-Osgj-mCtWe2whkUQ";
	String email = "mssonar26@gmail.com";
	String password = "123456";
	String confirmPassword = "123456";
	String profilePicture = "myself,jpeg";
	boolean status = true;
	Optional<User> optionalUser = Optional.of(user);
	List<User> userlist = new ArrayList<>();

	
	/**
	 * Method: Test Case to Create User
	 */
	public void testCreateUser() {

		regdto = new RegisterDTO();
		regdto.setPassword(password);
		regdto.setConfirmPassword(confirmPassword);
		regdto.setEmail(email);

		when(mapper.map(regdto, User.class)).thenReturn(user);
		when(regdto.getPassword()).thenReturn(password);
		when(regdto.getConfirmPassword()).thenReturn(confirmPassword);
		when(bCryptPasswordEncoder.encode("password")).thenReturn(anyString());
		user.setPassword(password);
		jms.sendMail(email, token);
		when(userRepo.save(user)).thenReturn(user);
		
		Response response = userService.createUser(regdto);
		System.out.println(response.getStatus());
		assertEquals(200, response.getStatus());

	}
	
	
	/**
	 * Method: Test Case to Find User By Id
	 */
	@Test
	public void testFindUser() {
		
		when(jwt.getEmailId(token)).thenReturn(email);
		when(userRepo.findById((email))).thenReturn(optionalUser);
		
		Response response = userService.findUser(token);
		assertEquals(200, response.getStatus());
		
	}
	
	
	/**
	 * Method: Test Case to Show All Users 
	 */
	public void testShowUsers(){
		
//		List<User> userlist = userService.showUsers();
//		assertEquals(userlist, userRepo.findAll());
	}
	
	
	/**
	 * Method: Test Case to Delete User 
	 */
	@Test
	public void testDeleteUser() {
		
		 when(jwt.getEmailId(token)).thenReturn(email);
	     when(userRepo.findById(email)).thenReturn(optionalUser);
	        
	     Response response = userService.deleteUser(email);
	     assertEquals(200, response.getStatus());
		
	}
	
	/**
	 * Method: Test Case to Update User
	 */
	@Test
	public void testUpdateUser() {
		
		updatedto.setFirstName("Mayuresh");
		updatedto.setLastName("Sonar");
		updatedto.setMobileNumber("9426594637");
		user.setEmail(email);
		
		when(mapper.map(regdto, User.class)).thenReturn(user);
		when(userRepo.save(user)).thenReturn(user);
		
		Response response = userService.updateUser(updatedto, token);
		assertEquals(200, response.getStatus());
	}

	
	/**
	 * Method: Test Case to Login
	 */
	@Test
	public void testLogin() {

		logindto.setEmail(email);
		logindto.setPassword(password);
		
		when(jwt.getEmailId(token)).thenReturn(email);
		when(mapper.map(logindto, User.class)).thenReturn(user);
		when(userRepo.findByEmail(email)).thenReturn(user);
		when(bCryptPasswordEncoder.matches(logindto.getPassword(), user.getPassword())).thenReturn(status);
		
		if (status) {
			Response response = userService.login(logindto, token);
			assertEquals(200, response.getStatus());
		}
	}
	
	
	/**
	 * Method: Test Case to Forget Password
	 */
	@Test
	public void testForgetPassword() {
		when(mapper.map(forgetdto, User.class)).thenReturn(user);
		when(jwt.createToken(email)).thenReturn(token);
		
		Response response = userService.forgetPassword(forgetdto);
		assertEquals(200, response.getStatus());
	}
	
	
	/**
	 * Method: Test Case to Reset Password
	 */
	@Test
	public void testResetPassword() {

		resetdto.setNewPassword(password);;
		resetdto.setConfirmPassword(confirmPassword);;
		
		when(jwt.getEmailId(token)).thenReturn(email);
		when(userRepo.findByEmail(email)).thenReturn(user);
		user.setPassword(password);
		
		when(resetdto.getNewPassword()).thenReturn(password);
		when(resetdto.getConfirmPassword()).thenReturn(confirmPassword);
		when(bCryptPasswordEncoder.encode(user.getPassword())).thenReturn(anyString());
		user.setPassword(password);
		user.setConfirmPassword(confirmPassword);
		when(userRepo.save(user)).thenReturn(user);

		Response response = userService.resetPassword(resetdto, token);
		assertEquals(200, response.getStatus());
	}
	
	
	/** 
	 * Method: Test Case to Verify EmailId and Password
	 */
	@Test
	public void testVerify() {
		
		when(jwt.getEmailId(token)).thenReturn(email);
		when(userRepo.findByEmail(email)).thenReturn(user);
		when(userRepo.save(user)).thenReturn(user);
		
		Response response = userService.verify(token);
		assertEquals(200, response.getStatus());
	}
	
	
	/**
	 * Method: Test Case to Upload Profile Picture
	 * @throws IOException 
	 */
	@Test
	public void testUploadProfilePicture() throws IOException {
		
		when(jwt.getEmailId(token)).thenReturn(email);
		when(userRepo.findByEmail(email)).thenReturn(user);
		
		if(profilePicture == null) {
			user.setProfilePicture(profilePicture);
			Response response = userService.uploadProfilePicture(token, file);
			assertEquals(200, response.getStatus());
		}
	}
	
	
	/**
	 * Method: Test Case to Update Profile Picture
	 * @throws IOException 
	 */
	@Test
	public void testUpdateProfilePicture() throws IOException {

		when(jwt.getEmailId(token)).thenReturn(email);
		when(userRepo.findByEmail(email)).thenReturn(user);
		assertTrue(profilePicture.contains(".jpeg") || profilePicture.contains(".jpg") || profilePicture.contains(".png"));
	
		if(profilePicture != null) {
		user.setProfilePicture(profilePicture);
		Response response = userService.updateProfilePicture(token, file);
		assertEquals(200, response.getStatus());
		}
	}

	/**
	 * Method: Test Case to Remove Profile Picture
	 */
	@Test
	public void testRemoveProfilePicture() {
		user.setEmail(email);
		when(jwt.getEmailId(token)).thenReturn(email);
		when(userRepo.findByEmail(email)).thenReturn(user);
		assertTrue(email.equals(user.getEmail()));
		
		user.setProfilePicture(null);
		when(userRepo.save(user)).thenReturn(user);
		Response response = userService.removeProfilePicture(token);
		assertEquals(200, response.getStatus());
		
	}
	
}
