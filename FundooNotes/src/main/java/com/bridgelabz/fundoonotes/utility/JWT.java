package com.bridgelabz.fundoonotes.utility;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JWT {
	
	private static final String SECRET_KEY="SECRET";
	
	public String createToken(String email) {
		
		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
		return Jwts.builder().setId(email).signWith(signatureAlgorithm, SECRET_KEY).compact();
	}

}