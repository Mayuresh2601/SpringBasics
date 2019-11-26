/******************************************************************************
*  
*  Purpose: To Implement Fundoo Notes App
*  @interface To connect Mongo Repository
*  @author  Mayuresh Sunil Sonar
*
******************************************************************************/
package com.bridgelabz.fundoonotes.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.bridgelabz.fundoonotes.model.User;

@Repository
public interface UserRepositoryI extends MongoRepository<User, String>{
	
	public User findByEmail(String email);

}

