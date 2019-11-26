/******************************************************************************
*  
*  Purpose: To Implement Fundoo Notes App
*  @interface To connect Mongo Repository
*  @author  Mayuresh Sunil Sonar
*
******************************************************************************/
package com.bridgelabz.fundoonotes.notes.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.bridgelabz.fundoonotes.notes.model.Note;

@Repository
public interface NoteRepositoryI extends MongoRepository<Note, String>{
	
	public List<Note> findByEmailId(String email);

}
