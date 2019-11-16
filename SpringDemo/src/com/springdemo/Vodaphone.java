package com.springdemo;

public class Vodaphone implements Sim{

	@Override
	public void calling() {
		
		System.out.println("Calling Vodaphone Sim");
	}

	@Override
	public void data() {
		
		System.out.println("Data of Vodaphone Sim");
	}

}
