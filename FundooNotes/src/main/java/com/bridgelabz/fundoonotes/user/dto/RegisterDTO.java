package com.bridgelabz.fundoonotes.user.dto;

import lombok.Data;

@Data
public class RegisterDTO {

	private String firstName;
	
	private String lastName;

	private String email;

	private String password;
	
	private long mobileNumber;
	
	private boolean isValidate;

}
