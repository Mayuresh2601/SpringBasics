package com.bridgelabz.fundoonotes.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
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
	 * @return UserInterface Response
	 */
	@PostMapping("/add")
	public Response addNewUser(@RequestBody RegisterDTO regdto) {
		
		String result = recordService.addNewUser(regdto); 
		return new Response(101, "Added New User", result);
	}
	
	/**Method: To Find User By Id from database
	 * @param id
	 * @return UserInterface Response
	 */
	@GetMapping("/users/{id}")
	public Response findUserById(@RequestParam String id) {
		
		return new Response(105, "Student Data", recordService.findUserById(id));
	}
	
	/**Method: To Update User in database
	 * @param student
	 * @param id
	 * @return UserInterface Response
	 */
	@PutMapping("/update/{id}")
	public Response updateUser(User student,@RequestParam String id) {
		
		String result = recordService.updateUser(student, id);
		return new Response(110, "Student Updated", result);
	}
	
	/**Method: To Delete User from database
	 * @param id
	 * @return UserInterface Response
	 */
	@DeleteMapping("/delete/{id}")
	public Response deleteUser(@RequestParam String id) {
		
		String result = recordService.deleteUserById(id);
		return new Response(115, "Student Deleted Successfully", result);
	}
	
	/**Method: To Show All Data present in database
	 * @return UserInterface Response
	 */
	@GetMapping("/show")
	public Response show() {

		List<User> list = recordService.show();

		return new Response(120, "Show all details", list);
	}
	
	/**Method: To Login in database
	 * @param logindto
	 * @return UserInterface Response
	 */
	@PostMapping("/login")
	public String login(@RequestBody LoginDTO logindto) {
		
		return recordService.login(logindto);
	}
	
	/**Method: To check forget password implementation
	 * @param registerdto
	 * @return Implementation Logic from service
	 */
	@PostMapping("/forget")
	public String forgetPassword(@RequestBody RegisterDTO registerdto) {
		
		return recordService.forgetPassword(registerdto);
	}
	
	/**Method: To check reset password implementation
	 * @param reset
	 * @param token
	 * @return Implementation Logic from service
	 */
	@PostMapping("/reset")
	public String resetPassword(@RequestBody ResetDTO reset,@RequestHeader String token) {
		
		return recordService.resetPassword(reset, token);
	}
	
	/**Method: To Verify EmailId and Password in the Database
	 * @param token
	 * @return Implementation Logic from service
	 */
	@PostMapping("/verify")
	public Response verifyUser(@RequestHeader String token) {
		
		return new Response(200, recordService.verify(token), token);
	}
}
