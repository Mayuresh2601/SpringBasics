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
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bridgelabz.fundoonotes.label.repository.LabelRepositoryI;
import com.bridgelabz.fundoonotes.note.dto.NoteDTO;
import com.bridgelabz.fundoonotes.note.model.Note;
import com.bridgelabz.fundoonotes.note.repository.NoteRepositoryI;
import com.bridgelabz.fundoonotes.user.exception.NoteException;
import com.bridgelabz.fundoonotes.user.repository.UserRepositoryI;
import com.bridgelabz.fundoonotes.user.utility.Jwt;

@Service
public class NoteService implements NoteServiceI{
	
	@Autowired
	Jwt jwt;
	
	@Autowired
	UserRepositoryI userrepository;
	
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
			
//			User user = userrepository.findByEmail(email);
//			user.getUserlist().add(note);
//			userrepository.save(user);
			return NoteMessageReference.CREATE_NOTE;
		}
		throw new NoteException(NoteMessageReference.UNAUTHORIZED_USER);
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
		throw new NoteException(NoteMessageReference.UNAUTHORIZED_USER);
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

	
	/**
	 *Method: To Display All Notes
	 */
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
	 *Method: To Sort Notes By Title
	 */
	@Override
	public List<?> sortNoteByTitle() {
		
		List<Note> list=showNotes();
        list= list.stream().sorted((list1,list2)->list1.getTitle().compareToIgnoreCase(list2.getTitle())).collect(Collectors.toList());
        return list;
	}


	/**
	 *Method: To Sort Notes By Date
	 */
	@Override
	public List<?> sortNoteByDate() {
		
		 List<Note> list=showNotes();
         list= list.stream().sorted((list1,list2)->list1.getCreateDate().compareToIgnoreCase(list2.getCreateDate())).collect(Collectors.toList());
         return list;
	}


	/**
	 *Method: To Add Collaborators using Token of User
	 */
	@Override
	public String addCollaboratorDemo(String id, String token) {
		
		Note note = noterepository.findById(id).get();
		if(note != null) {
			
			String email = jwt.getToken(token);
			if(email != null) {
				
				boolean isValid = note.getCollaboratorList().contains(email);
				System.out.println(isValid);
				if(isValid) {
					
					return "Collaborator Already Exists";
				}
				note.getCollaboratorList().add(email);
				noterepository.save(note);
				
//				User user = userrepository.findByEmail(email);
//				user.getUserlist().add(note);
//				userrepository.save(user);
				return NoteMessageReference.CREATE_COLLABORATOR;
			}
		}
		throw new NoteException(NoteMessageReference.UNAUTHORIZED_USER);
	}

	
	/**
	 *Method: To Add Collaborators using emailId
	 */
	@Override
	public String addCollaborator(String id, String collaboratorEmailId) {
		
		Note note = noterepository.findById(id).get();
		if(note != null) {
			
			boolean isValid = note.getCollaboratorList().contains(collaboratorEmailId);
			if(isValid) {
				
				return NoteMessageReference.COLLABORATOR_EXISTS;
			}
			note.getCollaboratorList().add(collaboratorEmailId);
			noterepository.save(note);
			return NoteMessageReference.CREATE_COLLABORATOR;
		}
		throw new NoteException(NoteMessageReference.UNAUTHORIZED_USER);
	}
	
	
	/**
	 *Method: To Remove Collaborators
	 */
	@Override
	public String removeCollaborator(String id, String collaboratorEmailId) {
		
		Note note = noterepository.findById(id).get();
		if(note != null) {	
			
			note.getCollaboratorList().remove(collaboratorEmailId);
			noterepository.save(note);
			return NoteMessageReference.REMOVE_COLLABORATOR;
		}
		throw new NoteException(NoteMessageReference.UNAUTHORIZED_USER);
	}


	/**
	 *Method: To Add Reminder
	 */
	@Override
	public String addReminder(String token,String id, int year, int month, int day, int hour, int minute, int second) {
		
		String email = jwt.getToken(token);
		
		if(email != null) {
			LocalDateTime now = LocalDateTime.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern(day+"-"+month+"-"+year+"   "+hour+":"+minute+":"+second);
			Note note = noterepository.findById(id).get();
			String date = now.format(formatter);
			
			if(note.getReminder() == null) {
				
				note.setReminder(date);
				noterepository.save(note);
				return NoteMessageReference.ADD_REMINDER;
			}
			
			if(note.getReminder().equals(date)) {
				throw new NoteException(NoteMessageReference.REMINDER_EXISTS_EXCEPTION);
			}
			
		}
		throw new NoteException(NoteMessageReference.UNAUTHORIZED_USER);
		
	}

	
	/**
	 *Method: To Edit Reminder
	 */
	@Override
	public String editReminder(String token, String id, int year, int month, int day, int hour, int minute, int second) {
		
		String email = jwt.getToken(token);
		
		if(email != null) {
			LocalDateTime now = LocalDateTime.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern(day+"-"+month+"-"+year+"   "+hour+":"+minute+":"+second);
			String date = now.format(formatter);
			Note note = noterepository.findById(id).get();
			String existingDate = note.getReminder();
			
			if(existingDate == null) {
				throw new NoteException(NoteMessageReference.REMINDER_NOT_FOUND_EXCEPTION);
			}
			
			if(note.getReminder().equals(date)) {
				throw new NoteException(NoteMessageReference.REMINDER_EXISTS_EXCEPTION);
			}
			
			note.setReminder(date);
			noterepository.save(note);
			return NoteMessageReference.EDIT_REMINDER;
		}
		throw new NoteException(NoteMessageReference.UNAUTHORIZED_USER);
	}
	
	
	/**
	 *Method: To Remove Reminder
	 */
	@Override
	public String removeReminder(String token, String id) {
		
		String email = jwt.getToken(token);
		
		if(email != null) {
			Note note = noterepository.findById(id).get();
			
			if(note.getReminder() == null) {
				throw new NoteException(NoteMessageReference.REMINDER_REMOVE_EXCEPTION);
			}
			
			note.setReminder(null);
			noterepository.save(note);
			return NoteMessageReference.REMOVE_REMINDER;
		}
		throw new NoteException(NoteMessageReference.UNAUTHORIZED_USER);
	}

}
