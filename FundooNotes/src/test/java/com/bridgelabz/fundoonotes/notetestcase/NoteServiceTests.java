package com.bridgelabz.fundoonotes.notetestcase;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.bridgelabz.fundoonotes.note.dto.CollaboratorDTO;
import com.bridgelabz.fundoonotes.note.dto.NoteDTO;
import com.bridgelabz.fundoonotes.note.model.Note;
import com.bridgelabz.fundoonotes.note.repository.NoteRepositoryI;
import com.bridgelabz.fundoonotes.note.service.NoteService;
import com.bridgelabz.fundoonotes.user.model.User;
import com.bridgelabz.fundoonotes.user.repository.UserRepositoryI;
import com.bridgelabz.fundoonotes.user.response.Response;
import com.bridgelabz.fundoonotes.user.utility.Jwt;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@TestPropertySource("classpath:message.properties")
public class NoteServiceTests {
	
	@InjectMocks
	NoteService noteService;
	
	@Mock
	NoteRepositoryI noteRepo;
	
	@Mock
	UserRepositoryI userRepo;
	
	@Mock
	User user;
	
	@Mock
	Jwt jwt;
	
	@Mock
	ModelMapper mapper;
	
	@Mock
	Environment noteEnv;
	
	LocalDateTime now = LocalDateTime.now();
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
	
	Note note = new Note();
	//private Note note = new Note("5dea5e3b836c8f441d888497","DBz","Dragon Ball Z","mssonar26@gmail.com",null,null,false,false,false,null,null,null);
	private String noteId = "5dea5e42836c8f441d888498";
	private String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJlbWFpbElkIjoibXNzb25hcjI2QGdtYWlsLmNvbSJ9.PnmJiMaZVOJ03T5WgZU8k0VNEK-Osgj-mCtWe2whkUQ";
	private String emailId = "mssonar26@gmail.com";
	private Optional<Note> databaseNote = Optional.of(note);
	NoteDTO noteDTO = new NoteDTO();
	List<User> userlist= new ArrayList<>();
	
	/**
	 * Method: Test Case for Create Note
	 */
	@Test
	public void testCreateNote() {

		when(jwt.getEmailId(token)).thenReturn(emailId);
		when(mapper.map(noteDTO, Note.class)).thenReturn(note);
		when(noteRepo.save(note)).thenReturn(note);
	
		Response response = noteService.createNote(token, noteDTO);
		assertEquals(200, response.getStatus());
	}
	
	
	/**
	 * Method: Test Case for Update Note
	 */
	@Test
	public void testUpdateNote() {
		
		NoteDTO noteDTO = new NoteDTO();
		noteDTO.setTitle("Secrets Can't Share");
		noteDTO.setDescription("My Secrets are my secrets none of your secrets");
		
		note.setId(noteId);
		note.setEmailId(emailId);
		
		// Mock Object Defined
		when(jwt.getEmailId(token)).thenReturn(emailId);
		when(mapper.map(noteDTO, Note.class)).thenReturn(note);
		when(noteRepo.save(note)).thenReturn(note);		
		
		Response response = noteService.updateNote(noteId, token, noteDTO);
		assertEquals(200, response.getStatus());
	}
	
	
	/**
	* Method: Test Case for Delete Note 
	*/
	@Test
	public void testDeleteNote() {
		
		note.setId(noteId);
		note.setEmailId(emailId);
		
		when(jwt.getEmailId(token)).thenReturn(emailId);
		when(noteRepo.findByIdAndEmailId(noteId, emailId)).thenReturn(databaseNote);
		
		Response response= noteService.deleteNote(noteId, token);
		assertEquals(200, response.getStatus());
	}

	  /**
	    * Test case to get all note
	    */
	@Test
	public void testfindNoteById() {

		when(jwt.getEmailId(token)).thenReturn(emailId);
		when(noteRepo.findById(noteId)).thenReturn(databaseNote);

		Response response = noteService.findNoteById(noteId, token);
		assertEquals(200, response.getStatus());

	}
	
	/**
	* Method: Test Case to Show All Notes
	*/
	@Test
	public void testShowNotes() {
		
		List<Note> notelist = noteService.showNotes();
		assertEquals(notelist, noteRepo.findAll());
	}

	
	/**
	* Method: Test Case to Pin Note for User 
	*/
	@Test
	public void TestisPin() {
		
		note.setId(noteId);
		note.setEmailId(emailId);
		note.setPin(true);
		when(noteRepo.findByIdAndEmailId(noteId, emailId)).thenReturn(note);
		when(userRepo.findByEmail(emailId)).thenReturn(user);
		Response response = noteService.isPin(noteId, token);
		if (note.isPin()) {
			note.setPin(true);
		}
		else {
			note.setPin(false);
		}
		assertEquals(200, response.getStatus());
	}

	
//	/**
//	* Method: Test Case to Trash Note for User 
//	*/
//	@Test
//	public void testIsTrash() {
//		
//		note.setTrash(true);
//		when(noteRepo.findByIdAndEmailId(noteId, emailId)).thenReturn(note);
//	
//		Response response = noteService.isTrash(noteId, token);
//		if (note.isTrash()) {
//			note.setTrash(true);
//		}
//		else {
//			note.setTrash(false);
//		}
//		assertEquals(200, response.getStatus());
//	}
//
//	
//	/**
//	* Method: Test Case to Archieve Note for User 
//	*/
//	@Test
//	public void testIsArchieve() {
//		
//		note.setArchieve(true);
//		when(noteRepo.findByIdAndEmailId(noteId, emailId)).thenReturn(note);
//		
//		Response response = noteService.isArchieve(noteId, token);
//		if (note.isArchieve()) {
//			note.setArchieve(true);
//		}
//		else {
//			note.setArchieve(false);
//		}
//		assertEquals(200, response.getStatus());
//	}
	
	
	/**
	 * Method: Test Case to sort Note By Title in Ascending Order
	 */
	@Test
	public void testSortNoteByTitleAsc() {
		
		List<?> notelist = noteService.sortNoteByTitleAsc();
		assertEquals(notelist, noteRepo.findAll());
	}
	
	/**
	 * Method: Test Case to sort Note By Title in Descending Order
	 */
	@Test
	public void testSortNoteByTitleDesc() {
		
		List<?> notelist = noteService.sortNoteByTitleDesc();
		assertEquals(notelist, noteRepo.findAll());
	}
	
	
	/**
	 * Method: Test Case to sort Note By Date in Ascending Order
	 */
	@Test
	public void testSortNoteByDateAsc() {
		
		List<?> notelist = noteService.sortNoteByDateAsc();
		assertEquals(notelist, noteRepo.findAll());
	}
	
	
	/**
	 * Method: Test Case to sort Note By Date in Descending Order
	 */
	@Test
	public void testSortNoteByDateDesc() {
		
		List<?> notelist = noteService.sortNoteByDateAsc();
		assertEquals(notelist, noteRepo.findAll());
	}
	
//	
//	/**
//	 * Method: Test Case to Add Collaborator
//	 */
//	@Test
//	public void testAddCollaborator() {
//		
//		CollaboratorDTO collaboratordto = null;
//		Response response = noteService.addCollaborator(noteId, token, collaboratordto);
//		assertEquals(200, response.getStatus());
//	}
//
//	
//	/**
//	 * Method: Test Case to Remove Collaborator
//	 */
//	@Test
//	public void testRemoveCollaborator() {
//		
//		String collaboratorEmailId = "demo.mayuresh@gmail.com";
//		Response response = noteService.removeCollaborator(noteId, token, collaboratorEmailId);
//		assertEquals(200, response.getStatus());
//	}
//	
//	
//	/**
//	 * Method: Test Case to Add Reminder
//	 */
//	@Test
//	public void testAddReminder() {
//		
//		int year=2020,month=12,day=23,hour=12,minute=20,second=45;
//		Response response = noteService.addReminder(token, noteId, year, month, day, hour, minute, second);
//		assertEquals(200, response.getStatus());
//	}
//	
//	
//	/**
//	 * Method: Test Case to Edit Reminder
//	 */
//	@Test
//	public void testEditReminder() {
//		
//		int year=2021,month=6,day=12,hour=18,minute=17,second=57;
//		Response response = noteService.editReminder(token, noteId, year, month, day, hour, minute, second);
//		assertEquals(200, response.getStatus());
//	}
//	
//	
//	/**
//	 * Method: Test Case to Remove Reminder 
//	 */
//	@Test
//	public void testRemoveReminder() {
//		
//		String collaboratorEmailId = "demo.mayuresh@gmail.com";
//		Response response = noteService.removeCollaborator(noteId, token, collaboratorEmailId);
//		assertEquals(200,response.getStatus());
//	}

	
}
