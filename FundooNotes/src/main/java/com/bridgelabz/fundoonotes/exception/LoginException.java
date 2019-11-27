package com.bridgelabz.fundoonotes.exception;

public class LoginException extends RuntimeException{

	/**
	 * Method: To throw LoginException
	 */
	private static final long serialVersionUID = 1L;
	
	public LoginException(String message) {
		
		super(message);
	}

}