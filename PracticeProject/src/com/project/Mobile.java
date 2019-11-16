package com.project;

public class Mobile {
	private Service service;

	public void setService(Service service) {
		this.service = service;
	}
	
	public void activation() {
		service.display();
	}
}
