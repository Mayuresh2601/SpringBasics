package com.bridgelabz.response;

public class Response {
	
	private int status;
	private String message;
	private Object data;
	
	public Response(int status, String message, Object data) {
		
		this.status = status;
		this.message = message;
		this.data = data;
	}

}
