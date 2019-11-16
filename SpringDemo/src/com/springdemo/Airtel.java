package com.springdemo;

public class Airtel implements Sim{
	
	Airtel(){
		System.out.println("constructor called");
	}

	@Override
	public void calling() {
		
		System.out.println("Calling Airtel Sim");
	}

	@Override
	public void data() {
		
		System.out.println("Data of Airtel Sim");
	}

}
