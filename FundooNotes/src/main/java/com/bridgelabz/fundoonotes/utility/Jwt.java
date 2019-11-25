package com.bridgelabz.fundoonotes.utility;

import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;

@Component
public class Jwt {
	
	private static final String SECRET_KEY="SECRET";
	
	public String createToken(String email) {
		
		Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
		return JWT.create().withClaim("emailId", email).sign(algorithm);
	}
	
	public String getToken(String token) {
		
		Claim claim=JWT.require(Algorithm.HMAC256(SECRET_KEY)).build().verify(token).getClaim("emailId");
		return claim.asString();
	}

}