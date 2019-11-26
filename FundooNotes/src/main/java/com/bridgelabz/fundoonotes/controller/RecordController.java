/******************************************************************************
*  
*  Purpose: To Implement Fundoo Notes App
*  @class Record Controller
*  @author  Mayuresh Sunil Sonar
*
******************************************************************************/
package com.bridgelabz.fundoonotes.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.fundoonotes.dto.LoginDTO;
import com.bridgelabz.fundoonotes.dto.RegisterDTO;
import com.bridgelabz.fundoonotes.dto.ResetDTO;
import com.bridgelabz.fundoonotes.model.User;
import com.bridgelabz.fundoonotes.response.Response;
import com.bridgelabz.fundoonotes.service.RecordService;

@RestController
public class RecordController {
	
	@Autowired
	private RecordService recordService;
	
	
	/**Method: To Add New User in Database
	 * @param regdto
	 * @return Add New User implementation Logic
	 */
	@PostMapping("/createuser")
	public Response addNewUser(@RequestBody RegisterDTO regdto) {
		
		String result = recordService.addNewUser(regdto); 
		return new Response(100, "Adding New User", result);
	}
	
	
	/**Method: To Find User By Id from database
	 * @param id
	 * @return Find User by Id implementation Logic
	 */
	@GetMapping("/users")
	public Response findUserById(@RequestHeader String id) {
		
		return new Response(105, "Processing Data", recordService.findUserById(id));
	}
	
	
	/**Method: To Update User in database
	 * @param regdto
	 * @param token
	 * @return Update User Details implementation Logic
	 */
	@PutMapping("/updateuser")
	public Response updateUser(@RequestBody RegisterDTO regdto,@RequestHeader String token) {
		
		String result = recordService.updateUser(regdto, token);
		return new Response(110, "Updating Data", result);
	}
	
	
	/**Method: To Delete User from database
	 * @param id
	 * @return Delete user Implementation Logic
	 */
	@DeleteMapping("/deleteuser")
	public Response deleteUser(@RequestHeader String id) {
		
		String result = recordService.deleteUserById(id);
		return new Response(115, "Deleting Data", result);
	}
	
	
	/**Method: To Show All Data present in database
	 * @return Display All Data Implemetation Logic
	 */
	@GetMapping("/show")
	public Response show() {

		List<User> list = recordService.show();

		return new Response(120, "Showing Details", list);
	}
	
	
	/**Method: To Login in database
	 * @param logindto
	 * @return Implementation Logic of login
	 */
	@PostMapping("/login")
	public Response login(@RequestBody LoginDTO logindto) {
		
		String result =  recordService.login(logindto);
		return new Response(125, "Login User ", result);
	}
	
	
	/**Method: To check forget password implementation
	 * @param registerdto
	 * @return Implementation Logic of forget password
	 */
	@PostMapping("/forget")
	public Response forgetPassword(@RequestBody RegisterDTO registerdto) {
		
		String result = recordService.forgetPassword(registerdto);
		return new Response(130, "Forget Password", result);
	}
	
	
	/**Method: To check reset password implementation
	 * @param reset
	 * @param token
	 * @return Implementation Logic of reset password
	 */
	@PostMapping("/reset")
	public Response resetPassword(@RequestBody ResetDTO reset,@RequestHeader String token) {
		
		String result = recordService.resetPassword(reset, token);
		return new Response(135, "Reset Password", result);
	}
	
	
	/**Method: To Verify EmailId and Password in the Database
	 * @param token
	 * @return Implementation Logic of verifying user
	 */
	@PostMapping("/verify")
	public Response verifyUser(@RequestHeader String token) {
		
		return new Response(200, recordService.verify(token), token);
	}
}
