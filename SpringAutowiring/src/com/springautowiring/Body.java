package com.springautowiring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class Body {
	
	@Autowired
	@Qualifier("Heart")
	private Heart heart;
	
	public Body(Heart heart) {
		
		this.heart = heart;
	}
	
	public void setHeart(Heart heart) {
		this.heart = heart;
		System.out.println("Setter Method is Called");
	}
	
	public void ispumping() {
		
		if (heart == null) {
			System.out.println("You are Dead");
		}
		else {
			heart.pump();
		}
	}

}
