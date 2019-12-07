package com.bridgelabz.fundoonotes.usertestcase;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.bridgelabz.fundoonotes.user.dto.RegisterDTO;
import com.bridgelabz.fundoonotes.user.model.User;
import com.bridgelabz.fundoonotes.user.repository.UserRepositoryI;
import com.bridgelabz.fundoonotes.user.response.Response;
import com.bridgelabz.fundoonotes.user.service.UserService;
import com.bridgelabz.fundoonotes.user.utility.Jms;
import com.bridgelabz.fundoonotes.user.utility.Jwt;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class UserServiceTests {
	
	@InjectMocks
	private UserService userService;
	
	@Mock
	private UserRepositoryI userRepo;
	
	@Mock
	private User user;
	
	@Mock
	private Jwt jwt;
	
	@Mock
	private Jms jms;
	
	@Mock
	private ModelMapper mapper;
	
	@Mock
	private BCryptPasswordEncoder passwordEncoder;
	
	@Mock
	private RegisterDTO regdto;
	
	private String userId = "5dea5dee836c8f441d888494";
	private String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJlbWFpbElkIjoibXNzb25hcjI2QGdtYWlsLmNvbSJ9.PnmJiMaZVOJ03T5WgZU8k0VNEK-Osgj-mCtWe2whkUQ";
	private String emailId = "mssonar26@gmail.com";
	
	
	/**
	 * Method: Initialize Before Each Test Case
	 */
	@BeforeEach
	public void init() {
		regdto = new RegisterDTO();
		user = new User();
	}
	
	/**
	 * Method: Test Case to Create User
	 */
	@Test
	public void testCreateUser() {
		
		regdto = new RegisterDTO();
		regdto.setFirstName("Mayuresh");
		regdto.setLastName("Sonar");
		regdto.setEmail("mssonar26@gmail.com");
		regdto.setPassword("123456");
		regdto.setConfirmPassword("123456");
		regdto.setMobileNumber("9426594637");
		
		when(mapper.map(regdto, User.class)).thenReturn(user);
		when(jwt.createToken(emailId)).thenReturn(token);
		
		Response response = userService.createUser(regdto);
		assertEquals(200, response.getStatus());
	}
	
	
	/**
	 * Method: Test Case to Find User By Id
	 */
	@Test
	public void testFindUserById() {
		
		//when(jwt.getEmailId(token)).thenReturn(emailId);
		//doNothing().when(userRepo.findById(userId));
		when(jwt.createToken(emailId)).thenReturn(token);

		Response response = userService.findUserById(userId);
		assertEquals(200, response.getStatus());
		
	}
	
	
	/**
	 * Method: Test Case to Show All Users 
	 */
	public void testShowUsers(){
		
	}
	
	
	/**
	 * Method: Test Case to Delete User 
	 */
	public void testDeleteUserById() {
		
	}
	
	
	/**
	 * Method: Test Case to Update User
	 */
	public void testUpdateUser() {
		
	}

	
	/**
	 * Method: Test Case to Login
	 */
	public void testLogin() {
		
	}
	
	
	/**
	 * Method: Test Case to Forget Password
	 */
	public void testForgetPassword() {
		
	}
	
	
	/**
	 * Method: Test Case to Reset Password
	 */
	public void testResetPassword() {
		
	}
	
	
	/** 
	 * Method: Test Case to Verify EmailId and Password
	 */
	public void testVerify() {
		
	}
	
	
	/**
	 * Method: Test Case to Upload Profile Picture
	 */
	public void testUploadProfilePicture() {
		
	}
	
	
	/**
	 * Method: Test Case to Update Profile Picture
	 */
	public void testUpdateProfilePicture() {
		
	}
	
	
	/**
	 * Method: Test Case to Remove Profile Picture
	 */
	public void testRemoveProfilePicture(String token) {
		
	}
	
}
