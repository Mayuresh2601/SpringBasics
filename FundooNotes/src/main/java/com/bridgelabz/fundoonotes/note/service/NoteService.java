/******************************************************************************
*  
*  Purpose: To Implement Fundoo Notes App
*  @class Note Service
*  @author  Mayuresh Sunil Sonar
*
******************************************************************************/
package com.bridgelabz.fundoonotes.note.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bridgelabz.fundoonotes.exception.NoteException;
import com.bridgelabz.fundoonotes.label.model.Label;
import com.bridgelabz.fundoonotes.label.repository.LabelRepositoryI;
import com.bridgelabz.fundoonotes.note.dto.NoteDTO;
import com.bridgelabz.fundoonotes.note.model.Note;
import com.bridgelabz.fundoonotes.note.repository.NoteRepositoryI;
import com.bridgelabz.fundoonotes.user.utility.Jms;
import com.bridgelabz.fundoonotes.user.utility.Jwt;

@Service
public class NoteService implements NoteServiceI{
	
	@Autowired
	Jwt jwt;
	
	@Autowired
	Jms jms;
	
	@Autowired
	NoteRepositoryI noterepository;
	
	@Autowired
	LabelRepositoryI labelrepository;
	
	@Autowired
	ModelMapper mapper;
	
	
	/**
	 *Method: To Create a Note for User
	 */
	@Override
	public String createNote(String token, NoteDTO notedto) {
		
		String email = jwt.getToken(token);
		if(email != null) {
			Note note = mapper.map(notedto, Note.class);
		
			LocalDateTime now = LocalDateTime.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
			String date = now.format(formatter);
		
			note.setCreateDate(date);
			noterepository.save(note);
		}
		return NoteMessageReference.CREATE_NOTE;
	}

	
	/**
	 *Method: To Update a Note for User
	 */
	@Override
	public String updateNote(String id, NoteDTO notedto) {
		
		Note noteupdate = noterepository.findById(id).get();

		if(noteupdate != null) {
			noteupdate.setTitle(notedto.getTitle());
			noteupdate.setDescription(notedto.getDescription());
			
			LocalDateTime now = LocalDateTime.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
			String date = now.format(formatter);
			
			noteupdate.setEditDate(date);
			noterepository.save(noteupdate);
			return NoteMessageReference.UPDATE_NOTE;
		}
		return NoteMessageReference.ID_NOT_FOUND;
	}

	
	/**
	 *Method: To Delete a Note
	 */
	@Override
	public String deleteNote(String id) {
		
		noterepository.deleteById(id);
		return NoteMessageReference.DELETE_NOTE;
	}

	
	/**
	 *Method: Search a Note using Id
	 */
	@Override
	public Optional<Note> findNoteById(String id) {
		
		Optional<Note> note =  noterepository.findById(id);
		return note;
	}

	@Override
	public List<Note> showNotes() {
		
		return noterepository.findAll();
	}
	
	/**
	 *Method: To Pin/Unpin Note for User
	 */
	@Override
	public boolean isPin(String id, String token) {
		
		String emailId = jwt.getToken(token);
		List<Note> listofNote= noterepository.findByEmailId(emailId);
		Note note =listofNote.stream().filter(i->i.getId().equals(id)).findAny().orElse(null);
		if(note!=null)
		{
			note.setPin(!(note.isPin()));
			noterepository.save(note);
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
		List<Note> listofNote= noterepository.findByEmailId(emailId);
		Note note =listofNote.stream().filter(i->i.getId().equals(id)).findAny().orElse(null);
		if(note!=null)
		{
			note.setTrash(!(note.isTrash()));
			noterepository.save(note);
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
		List<Note> listofNote= noterepository.findByEmailId(emailId);
		Note note =listofNote.stream().filter(i->i.getId().equals(id)).findAny().orElse(null);
		if(note!=null)
		{
			note.setArchieve(!(note.isArchieve()));
			noterepository.save(note);
			return note.isArchieve();
		}
		throw new NoteException(NoteMessageReference.UNAUTHORIZED_USER);
	}


	/**
	 *Method: To Add Label to Note and Vice-versa
	 */
	@Override
	public String addLabelToNote(String noteid, String labelid, String token) {
		String email = jwt.getToken(token);
		
		if(email != null) {
			List<Note> notelist = noterepository.findByEmailId(email);
			Note note = notelist.stream().filter(i -> i.getId().equals(noteid)).findAny().orElse(null);
			Label label = labelrepository.findById(labelid).get();
			
			if(note != null && label != null) {
				
				note.getLabellist().add(label);
				label.getNotelist().add(note);
				noterepository.save(note);
				labelrepository.save(label);
			}
			return NoteMessageReference.CREATE_NOTE;
		}
		throw new NoteException(NoteMessageReference.UNAUTHORIZED_USER);
	}
}
