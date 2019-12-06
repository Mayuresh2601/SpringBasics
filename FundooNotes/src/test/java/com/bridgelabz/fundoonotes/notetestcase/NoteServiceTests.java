package com.bridgelabz.fundoonotes.notetestcase;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
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
@PropertySource("classpath:message.properties")
public class NoteServiceTests {
	
	@InjectMocks
	private NoteService noteService;
	
	@Mock
	private NoteRepositoryI noteRepo;
	
	@Mock
	private UserRepositoryI userRepo;
	
	@Mock
	private User user;
	
	@Mock
	private Jwt jwt;
	
	@Mock
	private ModelMapper mapper;
	
	@Mock
	private Environment noteEnvironment;

	private Note note = new Note();
	private String noteId = "5dba69b03f43761e31622cbe";
	private String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJlbWFpbElkIjoibXNzb25hcjI2QGdtYWlsLmNvbSJ9.PnmJiMaZVOJ03T5WgZU8k0VNEK-Osgj-mCtWe2whkUQ";
	private String emailId = "mssonar26@gmail.com";
	private Optional<Note> databaseNote;
	
	
	/**
	 * Method: Test Case for Create Note
	 */
	@Test
	public void testCreateNote() {
		
		NoteDTO noteDTO = new NoteDTO();
		noteDTO.setTitle("Title");
		noteDTO.setDescription("Description");
		note.setEmailId(emailId);
		System.out.println("Creating Note in JUnitTest: " + noteDTO.getTitle() +"   "+ noteDTO.getDescription());

		// Mock Object Defined
		when(jwt.getEmailId(token)).thenReturn(emailId);
		when(mapper.map(noteDTO, Note.class)).thenReturn(note);
		when(noteRepo.save(note)).thenReturn(note);

		Response response = noteService.createNote(token, noteDTO);
		assertEquals(noteEnvironment.getProperty("CREATE_NOTE"), response);
	}
	
	
	/**
	 * Method: Test Case for Update Note
	 */
	@Test
	public void testUpdateNote() {
		
		NoteDTO noteDTO = new NoteDTO();
		noteDTO.setTitle("Secrets Can't Share");
		String message = "Note Successfully Updated";
		when(mapper.map(noteDTO, Note.class)).thenReturn(note);
		when(noteEnvironment.getProperty("UPDATE_NOTE")).thenReturn(message);
		Response response = noteService.updateNote(noteId, token, noteDTO);
		System.out.println(response);
		assertEquals(noteEnvironment.getProperty("UPDATE_NOTE"), "Note Successfully Updated");
	}
	
	
	/**
	* Method: Test Case for Delete Note 
	*/
	@Test
	public void testDeleteNote() {
		
		when(noteRepo.findById(noteId)).thenReturn(databaseNote);
		Response note= noteService.deleteNote(noteId, token);
		noteRepo.deleteById(noteId);
		assertEquals(note, databaseNote);
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
		
		note.setPin(true);
		when(noteRepo.findByIdAndEmailId(noteId, emailId)).thenReturn(note);
		
		if (note.isPin()) {
			note.setPin(true);
		}
		else {
			note.setPin(false);
		}
		assertEquals(true, note.isPin());
	}

	
	/**
	* Method: Test Case to Trash Note for User 
	*/
	@Test
	public void testIsTrash() {
		
		note.setTrash(true);
		when(noteRepo.findByIdAndEmailId(noteId, emailId)).thenReturn(note);
	
		if (note.isTrash()) {
			note.setTrash(true);
		}
		else {
			note.setTrash(false);
		}
		assertEquals(true, note.isTrash());
	}

	
	/**
	* Method: Test Case to Archieve Note for User 
	*/
	@Test
	public void testIsArchieve() {
		
		note.setArchieve(true);
		when(noteRepo.findByIdAndEmailId(noteId, emailId)).thenReturn(note);
		
		if (note.isArchieve()) {
			note.setArchieve(true);
		}
		else {
			note.setArchieve(false);
		}
		assertEquals(true, note.isArchieve());
	}
	
	
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
	
	
	/**
	 * Method: Test Case to Add Collaborator
	 */
	@Test
	public void testAddCollaborator() {
		
		CollaboratorDTO collaboratordto = null;
		Response response = noteService.addCollaborator(noteId, token, collaboratordto);
		assertEquals(response, noteEnvironment.getProperty("CREATE_COLLABORATOR"));
	}

	
	/**
	 * Method: Test Case to Remove Collaborator
	 */
	@Test
	public void testRemoveCollaborator() {
		
		String collaboratorEmailId = "demo.mayuresh@gmail.com";
		Response response = noteService.removeCollaborator(noteId, token, collaboratorEmailId);
		assertEquals(response, noteEnvironment.getProperty("REMOVE_COLLABORATOR"));
	}
	
	
	/**
	 * Method: Test Case to Add Reminder
	 */
	@Test
	public void testAddReminder() {
		
		int year=2020,month=12,day=23,hour=12,minute=20,second=45;
		Response response = noteService.addReminder(token, noteId, year, month, day, hour, minute, second);
		assertEquals(response, noteEnvironment.getProperty("ADD_REMINDER"));
	}
	
	
	/**
	 * Method: Test Case to Edit Reminder
	 */
	@Test
	public void testEditReminder() {
		
		int year=2021,month=6,day=12,hour=18,minute=17,second=57;
		Response response = noteService.editReminder(token, noteId, year, month, day, hour, minute, second);
		assertEquals(response, noteEnvironment.getProperty("EDIT_REMINDER"));
	}
	
	
	/**
	 * Method: Test Case to Remove Reminder 
	 */
	@Test
	public void testRemoveReminder() {
		
		String collaboratorEmailId = "demo.mayuresh@gmail.com";
		Response response = noteService.removeCollaborator(noteId, token, collaboratorEmailId);
		assertEquals(response, noteEnvironment.getProperty("EDIT_REMINDER"));
	}
	
}
