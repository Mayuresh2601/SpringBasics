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
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.bridgelabz.fundoonotes.note.dto.CollaboratorDTO;
import com.bridgelabz.fundoonotes.note.dto.NoteDTO;
import com.bridgelabz.fundoonotes.note.model.Note;
import com.bridgelabz.fundoonotes.note.repository.NoteRepositoryI;
import com.bridgelabz.fundoonotes.user.exception.NoteException;
import com.bridgelabz.fundoonotes.user.model.User;
import com.bridgelabz.fundoonotes.user.repository.UserRepositoryI;
import com.bridgelabz.fundoonotes.user.utility.Jwt;

@Service
public class NoteService implements NoteServiceI{
	
	@Autowired
	private Jwt jwt;
	
	@Autowired
	private UserRepositoryI userrepository;
	
	@Autowired
	private NoteRepositoryI noterepository;
	
	@Autowired
	private ModelMapper mapper;
	
	@Autowired
	private Environment noteEnvironment;
	
	
	/**
	 *Method: To Create a Note for User
	 */
	@Override
	public String createNote(String token, NoteDTO notedto) {

		String email = jwt.getEmailId(token);
		User user = userrepository.findByEmail(email);
		
		if(email != null) {
			Note note = mapper.map(notedto, Note.class);
			note.setEmailId(email);
			
			LocalDateTime now = LocalDateTime.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
			String date = now.format(formatter);
			note.setCreateDate(date);
			noterepository.save(note);
			
			user.getNotelist().add(note);
			userrepository.save(user);
			return noteEnvironment.getProperty("CREATE_NOTE");
		}
		throw new NoteException(noteEnvironment.getProperty("UNAUTHORIZED_USER_EXCEPTION"));
	}

	
	/**
	 *Method: To Update a Note for User
	 */
	@Override
	public String updateNote(String noteid, String token, NoteDTO notedto) {
		
		String email = jwt.getEmailId(token);
		User user = userrepository.findByEmail(email);
		if(email.equals(user.getEmail())) {
		
			Note noteupdate = noterepository.findById(noteid).get();
			if(noteupdate != null) {
				noteupdate.setTitle(notedto.getTitle());
				noteupdate.setDescription(notedto.getDescription());
				
				LocalDateTime now = LocalDateTime.now();
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
				String date = now.format(formatter);
				
				noteupdate.setEditDate(date);
				noterepository.save(noteupdate);
				
				Note note = noterepository.findById(noteid).get();
				user.getNotelist().removeIf(data -> data.getId().equals(note.getId()));
				user.getNotelist().add(noteupdate);
				userrepository.save(user);
				return noteEnvironment.getProperty("UPDATE_NOTE");
			}
		}
		throw new NoteException(noteEnvironment.getProperty("UNAUTHORIZED_USER_EXCEPTION"));
	}

	
	/**
	 *Method: To Delete a Note
	 */
	@Override
	public String deleteNote(String noteid, String token) {
		
		String email = jwt.getEmailId(token);
		User user = userrepository.findByEmail(email);
		
		if(email.equals(user.getEmail())) {
			Note note = noterepository.findById(noteid).get();
			noterepository.deleteById(noteid);
			
			user.getNotelist().remove(note);
			userrepository.save(user);
			return noteEnvironment.getProperty("DELETE_NOTE");
		}
		throw new NoteException(noteEnvironment.getProperty("UNAUTHORIZED_USER_EXCEPTION"));
	}

	
	/**
	 *Method: Search a Note using Id
	 */
	@Override
	public Optional<Note> findNoteById(String noteid, String token) {
		
		String email = jwt.getEmailId(token);
		User user = userrepository.findByEmail(email);
		
		if(email.equals(user.getEmail())) {
			Optional<Note> note =  noterepository.findById(noteid);
			return note;
		}
		throw new NoteException(noteEnvironment.getProperty("UNAUTHORIZED_USER_EXCEPTION"));
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
	public boolean isPin(String noteid, String token) {
		
		String email = jwt.getEmailId(token);
		List<Note> listofNote= noterepository.findByEmailId(email);
		Note note =listofNote.stream().filter(i->i.getId().equals(noteid)).findAny().orElse(null);
		User user = userrepository.findByEmail(email);
		if(email.equals(user.getEmail()))
		{
			if(note.getId().equals(noteid)) 
			{
				note.setPin(!(note.isPin()));
				noterepository.save(note);
				
				Note note1 = noterepository.findById(noteid).get();
				user.getNotelist().removeIf(data -> data.getId().equals(note1.getId()));
				user.getNotelist().add(note);
				userrepository.save(user);
				return note.isPin();
			}
			throw new NoteException(noteEnvironment.getProperty("NOTE_ID_NOT_FOUND"));
		}
		throw new NoteException(noteEnvironment.getProperty("UNAUTHORIZED_USER_EXCEPTION"));
	}

	
	/**
	 *Method: To Trash/Recover Note for User
	 */
	@Override
	public boolean isTrash(String noteid, String token) {
		
		String email = jwt.getEmailId(token);
		List<Note> listofNote= noterepository.findByEmailId(email);
		Note note =listofNote.stream().filter(i->i.getId().equals(noteid)).findAny().orElse(null);
		User user = userrepository.findByEmail(email);
		if(email.equals(user.getEmail()))
		{
			if(note.getId().equals(noteid)) {
				note.setTrash(!(note.isTrash()));
				noterepository.save(note);
				
				Note note1 = noterepository.findById(noteid).get();
				user.getNotelist().removeIf(data -> data.getId().equals(note1.getId()));
				user.getNotelist().add(note);
				userrepository.save(user);
				return note.isTrash();
			}
			throw new NoteException(noteEnvironment.getProperty("NOTE_ID_NOT_FOUND"));
		}
		throw new NoteException(noteEnvironment.getProperty("UNAUTHORIZED_USER_EXCEPTION"));
	}

	
	/**
	 *Method: To Archieve/Unarchieve Note for User
	 */
	@Override
	public boolean isArchieve(String noteid, String token) {
		
		String email = jwt.getEmailId(token);
		List<Note> listofNote= noterepository.findByEmailId(email);
		Note note =listofNote.stream().filter(i->i.getId().equals(noteid)).findAny().orElse(null);
		User user = userrepository.findByEmail(email);
		if(email.equals(user.getEmail()))
		{
			if(note.getId().equals(noteid)) {
				note.setArchieve(!(note.isArchieve()));
				noterepository.save(note);
				
				Note note1 = noterepository.findById(noteid).get();
				System.out.println("Delete Note"+note1);
				
				user.getNotelist().removeIf(data -> data.getId().equals(note1.getId()));
				System.out.println("After Deletion -> "+user.getNotelist());
				user.getNotelist().add(note);
				userrepository.save(user);
				return note.isArchieve();
			}
			throw new NoteException(noteEnvironment.getProperty("NOTE_ID_NOT_FOUND"));
		}
		throw new NoteException(noteEnvironment.getProperty("UNAUTHORIZED_USER_EXCEPTION"));
	}


	/**
	 *Method: To Sort Notes By Title in Ascending Order
	 */
	@Override
	public List<?> sortNoteByTitleAsc() {
		
		List<Note> list=showNotes();
        list= list.stream().sorted((list1,list2)->list1.getTitle().compareToIgnoreCase(list2.getTitle())).collect(Collectors.toList());
        return list;
	}

	
	/**
	 *Method: To Sort Notes By Title in Descending Order
	 */
	@Override
	public List<?> sortNoteByTitleDesc() {
		
		List<Note> list=showNotes();
        list= list.stream().sorted((list1,list2)->list2.getTitle().compareToIgnoreCase(list1.getTitle())).collect(Collectors.toList());
		return list;
	}

	
	/**
	 *Method: To Sort Notes By Date in Ascending Order
	 */
	@Override
	public List<?> sortNoteByDateAsc() {
		
		 List<Note> list=showNotes();
         list= list.stream().sorted((list1,list2)->list1.getCreateDate().compareToIgnoreCase(list2.getCreateDate())).collect(Collectors.toList());
         return list;
	}


	/**
	 *Method: To Sort Notes By Title in Descending Order
	 */
	@Override
	public List<?> sortNoteByDateDesc() {
		
		List<Note> list=showNotes();
        list= list.stream().sorted((list1,list2)->list2.getCreateDate().compareToIgnoreCase(list1.getCreateDate())).collect(Collectors.toList());
		return list;
	}

	
	/**
	 *Method: To Add Collaborators using Token of User
	 */
	@Override
	public String addCollaboratorDemo(String noteid, String token) {
		
		String email = jwt.getEmailId(token);
		User user = userrepository.findByEmail(email);
		if(email.equals(user.getEmail())) {
			
			Note note = noterepository.findById(noteid).get();
			if(note.getId().equals(noteid)) {
				
				boolean checker = note.getCollaboratorList().contains(email);
				if(checker) {
					return noteEnvironment.getProperty("COLLABORATOR_EXISTS");
				}
				
				if(email.equals(note.getEmailId())) {
					return noteEnvironment.getProperty("COLLABORATOR_CANNOT_ADD");
				}
				
				note.getCollaboratorList().add(email);
				noterepository.save(note);
				
				user.getNotelist().removeIf(data -> data.getId().equals(note.getId()));
				user.getNotelist().add(note);
				userrepository.save(user);
				return noteEnvironment.getProperty("CREATE_COLLABORATOR");
			}
			return noteEnvironment.getProperty("NOTE_ID_NOT_FOUND");
		}
		throw new NoteException(noteEnvironment.getProperty("UNAUTHORIZED_USER_EXCEPTION"));
	}

	
	/**
	 *Method: To Add Collaborators using emailId
	 */
	@Override
	public String addCollaborator(String noteid, String token, CollaboratorDTO collaboratordto) {
		
		String email = jwt.getEmailId(token);
		User user = userrepository.findByEmail(email);
		if(email.equals(user.getEmail())) {
			
			Note note = noterepository.findById(noteid).get();
			if(note.getId().equals(noteid)) {
				
//				List<User> listuser = userservice.showUsers();
//				List<String> users = new ArrayList<>();
//				for (int i = 0; i <listuser.size() ; i++) {
//					users.add(listuser.get(i).getEmail());
//				}
//				if(!users.contains(collaboratorEmailId)) {
//					throw new NoteException(NoteMessageReference.UNAUTHORIZED_COLLABORATOR_EXCEPTION);
//				}
				
				boolean isValid = note.getCollaboratorList().contains(collaboratordto.getCollaboratorEmail());
				if(isValid) {
					return noteEnvironment.getProperty("COLLABORATOR_EXISTS");
				}
				
				if(collaboratordto.getCollaboratorEmail().equals(note.getEmailId())) {
					return noteEnvironment.getProperty("COLLABORATOR_CANNOT_ADD");
				}
				
				note.getCollaboratorList().add(collaboratordto.getCollaboratorEmail());
				noterepository.save(note);
				
				user.getNotelist().removeIf(data -> data.getId().equals(note.getId()));
				user.getNotelist().add(note);
				userrepository.save(user);
				return noteEnvironment.getProperty("CREATE_COLLABORATOR");
			}
			return noteEnvironment.getProperty("NOTE_ID_NOT_FOUND");
		}
		throw new NoteException(noteEnvironment.getProperty("UNAUTHORIZED_USER_EXCEPTION"));
	}
	
	
	/**
	 *Method: To Remove Collaborators
	 */
	@Override
	public String removeCollaborator(String noteid, String token, String collaboratorEmailId) {
		String email = jwt.getEmailId(token);
		User user = userrepository.findByEmail(email);
		if(email.equals(user.getEmail())) {
			
			Note note = noterepository.findById(noteid).get();
			if(note.getId().equals(noteid)) {
				
				if(!note.getCollaboratorList().contains(collaboratorEmailId)) {
					return noteEnvironment.getProperty("COLLABORATOR_NOT_EXISTS");
				}
				
				note.getCollaboratorList().remove(collaboratorEmailId);
				noterepository.save(note);
				
				user.getNotelist().removeIf(data -> data.getId().equals(note.getId()));
				user.getNotelist().add(note);
				userrepository.save(user);
				return noteEnvironment.getProperty("REMOVE_COLLABORATOR");
			}
			return noteEnvironment.getProperty("NOTE_ID_NOT_FOUND");
		}
		throw new NoteException(noteEnvironment.getProperty("UNAUTHORIZED_USER_EXCEPTION"));
	}


	/**
	 *Method: To Add Reminder
	 */
	@Override
	public String addReminder(String token, String noteid, int year, int month, int day, int hour, int minute, int second) {
		
		String email = jwt.getEmailId(token);
		
		User user = userrepository.findByEmail(email);
		if(email.equals(user.getEmail())) {
			
			Note note = noterepository.findById(noteid).get();
			if(note.getId().equals(noteid)) {
				
				LocalDateTime now = LocalDateTime.now();
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern(day+"-"+month+"-"+year+"   "+hour+":"+minute+":"+second);
				String date = now.format(formatter);
				
				if(note.getReminder().equals(date)) {
					throw new NoteException(noteEnvironment.getProperty("REMINDER_EXISTS_EXCEPTION"));
				}
				
				if(note.getReminder() == null) {
					
					note.setReminder(date);
					noterepository.save(note);
					
					user.getNotelist().removeIf(data -> data.getId().equals(note.getId()));
					user.getNotelist().add(note);
					userrepository.save(user);
					return noteEnvironment.getProperty("ADD_REMINDER");
				}
				
			}
			return noteEnvironment.getProperty("NOTE_ID_NOT_FOUND");
		}
		throw new NoteException(noteEnvironment.getProperty("UNAUTHORIZED_USER_EXCEPTION"));
		
	}

	
	/**
	 *Method: To Edit Reminder
	 */
	@Override
	public String editReminder(String token, String noteid, int year, int month, int day, int hour, int minute, int second) {
		
		String email = jwt.getEmailId(token);
		User user = userrepository.findByEmail(email);
		if(email.equals(user.getEmail())) {
			
			Note note = noterepository.findById(noteid).get();
			if(note.getId().equals(noteid)) {
				
				LocalDateTime now = LocalDateTime.now();
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern(day+"-"+month+"-"+year+"   "+hour+":"+minute+":"+second);
				String date = now.format(formatter);
				String existingDate = note.getReminder();
				
				if(existingDate == null) {
					throw new NoteException(noteEnvironment.getProperty("REMINDER_NOT_FOUND_EXCEPTION"));
				}
				
				if(note.getReminder().equals(date)) {
					throw new NoteException(noteEnvironment.getProperty("REMINDER_EXISTS_EXCEPTION"));
				}
				
				note.setReminder(date);
				noterepository.save(note);
				
				user.getNotelist().removeIf(data -> data.getId().equals(note.getId()));
				user.getNotelist().add(note);
				userrepository.save(user);
				return noteEnvironment.getProperty("EDIT_REMINDER");
			}
			return noteEnvironment.getProperty("NOTE_ID_NOT_FOUND");
		}
		throw new NoteException(noteEnvironment.getProperty("UNAUTHORIZED_USER_EXCEPTION"));
	}
	
	
	/**
	 *Method: To Remove Reminder
	 */
	@Override
	public String removeReminder(String token, String noteid) {
		
		String email = jwt.getEmailId(token);
		User user = userrepository.findByEmail(email);
		if(email.equals(user.getEmail())) {
			
			Note note = noterepository.findById(noteid).get();
			if(note.getId().equals(noteid)) {
			
				if(note.getReminder() == null) {
					throw new NoteException(noteEnvironment.getProperty("REMINDER_REMOVE_EXCEPTION"));
				}
				
				note.setReminder(null);
				noterepository.save(note);
				
				user.getNotelist().removeIf(data -> data.getId().equals(note.getId()));
				user.getNotelist().add(note);
				userrepository.save(user);
				return noteEnvironment.getProperty("REMOVE_REMINDER");
			}
			return noteEnvironment.getProperty("NOTE_ID_NOT_FOUND");
		}
		throw new NoteException(noteEnvironment.getProperty("UNAUTHORIZED_USER_EXCEPTION"));
	}

}
