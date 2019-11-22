package com.bridgelabz.fundoonotes.utility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

public class JMS {
	
	@Autowired
	JavaMailSender javamailsender;
	public void sendMail(String emailId,String token)
	{
		SimpleMailMessage mail=new SimpleMailMessage();
		mail.setTo(emailId);
		mail.setText(token);
		mail.setSubject("Verification token:");
		
		javamailsender.send(mail);
		
	}

}