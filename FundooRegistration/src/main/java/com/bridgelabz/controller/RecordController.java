package com.bridgelabz.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.dto.RegisterDTO;
import com.bridgelabz.model.User;
import com.bridgelabz.response.Response;
import com.bridgelabz.service.RecordServices;

@RestController
@RequestMapping("/api")
public class RecordController {
	

	@Autowired
	public RecordServices recordService;
	
//	@GetMapping("/")
//	public String login() {
//		
//		return recordService.Login();
//	}
//	
//	@GetMapping("/reqister")
//	public String Register() {
//		
//		return recordService.Register();
//	}
//	
//	@GetMapping("/all/{userName}/{password}")
//	public String Authentication(@PathVariable String userName,@PathVariable  String password) {
//		
//		return recordService.Autentication(userName, password);
//	}
//	
//	@GetMapping("/records")
//	public List<Student> getAllRecord() {
//		
//		return recordService.getAllRecord();
//	}
//	
//	@GetMapping("/records/{id}")
//	public Student getRecordById(@PathVariable int id) {
//		
//		return recordService.getRecordById(id);
//	}
//	
//	@PostMapping("/records")
//	public String AddRecord(@RequestBody RegisterDTO regdto) {
//		
//		String result = recordService.AddRecord(regdto);
//	}
//	
//	@PutMapping("/update")
//	public String UpdateRecord(@RequestBody Student student) {
//		
//		return recordService.AddRecord(student);
//	}
//
//	@DeleteMapping("/delete/{username}")
//	public String DeleteRecord(@PathVariable String userName) {
//		
//		return recordService.DeleteRecord(userName);
//	}
	@PostMapping("/adduser")
	public Response addNewStudent(@RequestBody RegisterDTO regdto) {
		
		String result = recordService.addNewUser(regdto); 
		return new Response(101, "Added New User", result);
	}
	
	@GetMapping("/adduser/{id}")
	public Response findUserById(@RequestParam String id) {
		
		return new Response(105, "Student Data", recordService.findUserById(id));
	}
	
	@PutMapping("update/{id}")
	public Response UpdateUser(User student,@RequestBody String id) {
		
		String result = recordService.updateUser(student, id);
		return new Response(110, "Student Updated", result);
	}
	
	@DeleteMapping("delete/{id}")
	public Response DeleteStudent(@RequestParam String id) {
		
		String result = recordService.deleteUserById(id);
		return new Response(115, "Student Deleted Successfully", result);
	}
	
	@GetMapping("/show")
	public Response show() {

		List<User> list = recordService.show();

		return new Response(120, "Show all details", list);
	}
}
