package com.bridgelabz.model;

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
	@NotBlank(message = "Username is Mandatory")
	private String userName;
	@NotBlank(message = "Password is Mandatory")
	private String password;
	@NotBlank(message = "MobileNumber is Mandatory")
	private long mobileNumber;
	
	private boolean isvalidate;
	
	public long getMobileNumber() {
		return mobileNumber;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setMobileNumber(long mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

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
	
	public String getUserName() {
		return userName;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
}
