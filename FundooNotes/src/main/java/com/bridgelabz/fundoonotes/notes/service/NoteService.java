/******************************************************************************
*  
*  Purpose: To Implement Fundoo Notes App
*  @class Note Service
*  @author  Mayuresh Sunil Sonar
*
******************************************************************************/
package com.bridgelabz.fundoonotes.notes.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bridgelabz.fundoonotes.exception.NoteException;
import com.bridgelabz.fundoonotes.notes.dto.NoteDTO;
import com.bridgelabz.fundoonotes.notes.model.Note;
import com.bridgelabz.fundoonotes.notes.repository.NoteRepositoryI;
import com.bridgelabz.fundoonotes.utility.Jms;
import com.bridgelabz.fundoonotes.utility.Jwt;

@Service
public class NoteService implements NoteServiceI{
	
	@Autowired
	Jwt jwt;
	
	@Autowired
	Jms jms;
	
	@Autowired
	NoteRepositoryI repository;
	
	/**
	 *Method: To Create a Note for User
	 */
	@Override
	public String createNote(String token, NoteDTO notedto) {
		
		String email = jwt.getToken(token);
		if(email != null) {
			ModelMapper mapper = new ModelMapper();
			Note note = mapper.map(notedto, Note.class);
			note.setTitle(notedto.getTitle());
			note.setDescription(notedto.getDescription());
			note.setEmailId(notedto.getEmailId());
		
			LocalDateTime now = LocalDateTime.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
			String date = now.format(formatter);
		
			note.setCreateDate(date);
			repository.save(note);
		}
		return NoteMessageReference.CREATE_NOTE;
		
	}

	/**
	 *Method: To Update a Note for User
	 */
	@Override
	public String updateNote(String id, NoteDTO notedto) {
		
		Note noteupdate = repository.findById(id).get();

		if(noteupdate != null) {
			noteupdate.setTitle(notedto.getTitle());
			noteupdate.setDescription(notedto.getDescription());
			
			LocalDateTime now = LocalDateTime.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
			
			String date = now.format(formatter);
			noteupdate.setEditDate(date);
			repository.save(noteupdate);
			return NoteMessageReference.UPDATE_NOTE;
		}
		return NoteMessageReference.ID_NOT_FOUND;
	}

	/**
	 *Method: To Delete a Note
	 */
	@Override
	public String deleteNote(String id) {
		
		repository.deleteById(id);
		return NoteMessageReference.DELETE_NOTE;
	}

	/**
	 *Method: Search a Note using Id
	 */
	@Override
	public Optional<Note> findNoteById(String id) {
		
		Optional<Note> note =  repository.findById(id);
		return note;
	}

	/**
	 *Method: To Pin/Unpin Note for User
	 */
	@Override
	public boolean isPin(String id, String token) {
		
		String emailId = jwt.getToken(token);
		List<Note> listofNote= repository.findByEmailId(emailId);
		Note note =listofNote.stream().filter(i->i.getId().equals(id)).findAny().orElse(null);
		if(note!=null)
		{
			note.setPin(!(note.isPin()));
			repository.save(note);
			return note.isPin();
		}
		throw new NoteException(NoteMessageReference.UNAUTHORIZED_USER);
	}

	/**
	 *Method: To Trash/Recover Note for User
	 */
	@Override
	public boolean isTrash(String id, String token) {
		
		String emailId = jwt.getToken(token);
		List<Note> listofNote= repository.findByEmailId(emailId);
		Note note =listofNote.stream().filter(i->i.getId().equals(id)).findAny().orElse(null);
		if(note!=null)
		{
			note.setTrash(!(note.isTrash()));
			repository.save(note);
			return note.isTrash();
		}
		throw new NoteException(NoteMessageReference.UNAUTHORIZED_USER);
	}

	/**
	 *Method: To Archieve/Unarchieve Note for User
	 */
	@Override
	public boolean isArchieve(String id, String token) {
		
		String emailId = jwt.getToken(token);
		List<Note> listofNote= repository.findByEmailId(emailId);
		Note note =listofNote.stream().filter(i->i.getId().equals(id)).findAny().orElse(null);
		if(note!=null)
		{
			note.setArchieve(!(note.isArchieve()));
			repository.save(note);
			return note.isArchieve();
		}
		throw new NoteException(NoteMessageReference.UNAUTHORIZED_USER);
	}

}
