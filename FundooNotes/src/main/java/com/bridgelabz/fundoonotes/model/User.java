package com.bridgelabz.fundoonotes.model;

import javax.validation.constraints.NotBlank;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
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

	@NotBlank(message = "Validation is Mandatory")
	private boolean isValidate;

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPassword() {
		return password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public long getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(long mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public boolean isValidate() {
		return isValidate;
	}

	public void setValidate(boolean isValidate) {
		this.isValidate = isValidate;
	}
	
	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

}
