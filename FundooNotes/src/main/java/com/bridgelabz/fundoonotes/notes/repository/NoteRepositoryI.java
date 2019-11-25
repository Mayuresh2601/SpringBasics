package com.bridgelabz.fundoonotes.notes.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.bridgelabz.fundoonotes.notes.model.Note;

@Repository
public interface NoteRepositoryI extends MongoRepository<Note, String>{
	
	public Note findByEmailId(String email);

}
