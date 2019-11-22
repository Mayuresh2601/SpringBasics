package com.bridgelabz.fundoonotes.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.fundoonotes.dto.RegisterDTO;
import com.bridgelabz.fundoonotes.model.User;
import com.bridgelabz.fundoonotes.response.Response;
import com.bridgelabz.fundoonotes.service.RecordService;

@RestController
public class RecordController {
	

	@Autowired
	public RecordService recordService;
	
	@PostMapping("/add")
	public Response addNewUser(@RequestBody RegisterDTO regdto) {
		
		String result = recordService.addNewUser(regdto); 
		return new Response(101, "Added New User", result);
	}
	
	@GetMapping("/users/{id}")
	public Response findUserById(@RequestParam String id) {
		
		return new Response(105, "Student Data", recordService.findUserById(id));
	}
	
	@PutMapping("/update/{id}")
	public Response updateUser(User student,@RequestParam String id) {
		
		String result = recordService.updateUser(student, id);
		return new Response(110, "Student Updated", result);
	}
	
	@DeleteMapping("/delete/{id}")
	public Response deleteUser(@RequestParam String id) {
		
		String result = recordService.deleteUserById(id);
		return new Response(115, "Student Deleted Successfully", result);
	}
	
	@GetMapping("/show")
	public Response show() {

		List<User> list = recordService.show();

		return new Response(120, "Show all details", list);
	}
}
