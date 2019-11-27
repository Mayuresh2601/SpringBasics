/******************************************************************************
*  
*  Purpose: To Implement Fundoo Notes App
*  @class Record Controller
*  @author  Mayuresh Sunil Sonar
*
******************************************************************************/
package com.bridgelabz.fundoonotes.user.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.fundoonotes.response.Response;
import com.bridgelabz.fundoonotes.user.dto.LoginDTO;
import com.bridgelabz.fundoonotes.user.dto.RegisterDTO;
import com.bridgelabz.fundoonotes.user.dto.ResetDTO;
import com.bridgelabz.fundoonotes.user.model.User;
import com.bridgelabz.fundoonotes.user.service.UserService;

@RestController
public class UserController {
	
	@Autowired
	private UserService recordService;
	
	
	/**Method: To Create User in Database
	 * @param regdto
	 * @return Create User implementation Logic
	 */
	@PostMapping("/createuser")
	public Response createUser(@RequestBody RegisterDTO regdto) {
		
		String result = recordService.createUser(regdto); 
		return new Response(200, "Adding New User", result);
	}
	
	
	/**Method: To Find User By Id from database
	 * @param id
	 * @return Find User by Id implementation Logic
	 */
	@GetMapping("/users")
	public Response findUserById(@RequestHeader String id) {
		
		return new Response(200, "Finding User By ID", recordService.findUserById(id));
	}
	
	
	/**Method: To Update User in database
	 * @param regdto
	 * @param token
	 * @return Update User Details implementation Logic
	 */
	@PutMapping("/updateuser")
	public Response updateUser(@RequestBody RegisterDTO regdto,@RequestHeader String id) {
		
		String result = recordService.updateUser(regdto, id);
		return new Response(200, "Updating User", result);
	}
	
	
	/**Method: To Delete User from database
	 * @param id
	 * @return Delete user Implementation Logic
	 */
	@DeleteMapping("/deleteuser")
	public Response deleteUser(@RequestHeader String id) {
		
		String result = recordService.deleteUserById(id);
		return new Response(200, "Deleting User", result);
	}
	
	
	/**Method: To Show All Users present in database
	 * @return Display All Users Implementation Logic
	 */
	@GetMapping("/showusers")
	public Response showUsers() {

		List<User> list = recordService.showUsers();

		return new Response(200, "Showing All User", list);
	}
	
	
	/**Method: To Login in database
	 * @param logindto
	 * @return Implementation Logic of login
	 */
	@PostMapping("/login")
	public Response login(@RequestBody LoginDTO logindto,@RequestHeader String token) {
		
		String result =  recordService.login(logindto, token);
		return new Response(200, "Login User ", result);
	}
	
	
	/**Method: To check forget password implementation
	 * @param registerdto
	 * @return Implementation Logic of forget password
	 */
	@PostMapping("/forget")
	public Response forgetPassword(@RequestBody RegisterDTO registerdto) {
		
		String result = recordService.forgetPassword(registerdto);
		return new Response(200, "Forget Password", result);
	}
	
	
	/**Method: To check reset password implementation
	 * @param reset
	 * @param token
	 * @return Implementation Logic of reset password
	 */
	@PostMapping("/reset")
	public Response resetPassword(@RequestBody ResetDTO reset,@RequestHeader String token) {
		
		String result = recordService.resetPassword(reset, token);
		return new Response(200, "Reset Password", result);
	}
	
	
	/**Method: To Verify EmailId and Password in the Database
	 * @param token
	 * @return Implementation Logic of verifying user
	 */
	@PostMapping("/verify")
	public Response verifyUser(@RequestHeader String token) {
		
		String result = recordService.verify(token);
		return new Response(200, "EmailId & Password Verified Successful", result);
	}
}
