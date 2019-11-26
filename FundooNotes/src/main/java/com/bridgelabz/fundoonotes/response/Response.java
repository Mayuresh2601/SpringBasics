package com.bridgelabz.fundoonotes.response;

import lombok.Data;

@Data
public class Response {
	
	private int status;
	private String message;
	private Object data;
	
	public Response(int status, String message, Object data) {
		super();
		this.status = status;
		this.message = message;
		this.data = data;
	}
	
}
