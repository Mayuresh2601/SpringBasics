/******************************************************************************
*  
*  Purpose: To Implement Fundoo Notes App
*  @class To Encrypt the Password
*  @author  Mayuresh Sunil Sonar
*
******************************************************************************/
package com.bridgelabz.fundoonotes.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class Config {
	
	/**Method: To Encrypt the password at backend
	 * @return Encrypting Password
	 */
	@Bean
	public BCryptPasswordEncoder bcyBCryptPasswordEncoder() {
		
		return new BCryptPasswordEncoder();
	}

}