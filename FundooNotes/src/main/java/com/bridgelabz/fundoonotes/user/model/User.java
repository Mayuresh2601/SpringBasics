package com.bridgelabz.fundoonotes.user.model;

import javax.validation.constraints.NotBlank;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document
@Data
public class User {

	@Id
	private String id;

	@NotBlank(message = "FirstName is Mandatory")
	private String firstName;

	@NotBlank(message = "LastName is Mandatory")
	private String lastName;

	@NotBlank(message = "EmailId is Mandatory")
	private String email;

	@NotBlank(message = "Password is Mandatory")
	private String password;
	
	@NotBlank(message = "Confirm Password is Mandatory")
	private String confirmPassword;

	@NotBlank(message = "MobileNumber is Mandatory")
	private long mobileNumber;

	private boolean isValidate;

	//List<Note> userlist = new ArrayList<>();
}
