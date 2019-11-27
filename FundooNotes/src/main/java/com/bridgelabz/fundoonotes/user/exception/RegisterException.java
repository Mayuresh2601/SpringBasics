package com.bridgelabz.fundoonotes.user.exception;

public class RegisterException extends RuntimeException{

	/**
	 * Method: To throw RegisterException
	 */
	private static final long serialVersionUID = 1L;
	
	public RegisterException(String message) {
		
		super(message);
	}

}
