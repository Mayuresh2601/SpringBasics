package com.bridgelabz.dto;

public class RegisterDTO {
	
	private String firstName;
	private String lastName;
	private String userName;
	private String emailId;
	private String password;
	private boolean isvalidate;
	
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
	
	public String getEmailId() {
		return emailId;
	}
	
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public boolean isIsvalidate() {
		return isvalidate;
	}
	
	public void setIsvalidate(boolean isvalidate) {
		this.isvalidate = isvalidate;
	}

}
