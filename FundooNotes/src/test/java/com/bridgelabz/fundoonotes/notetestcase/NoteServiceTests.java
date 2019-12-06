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
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.bridgelabz.fundoonotes.note.dto.NoteDTO;
import com.bridgelabz.fundoonotes.note.model.Note;
import com.bridgelabz.fundoonotes.note.repository.NoteRepositoryI;
import com.bridgelabz.fundoonotes.note.service.NoteService;
import com.bridgelabz.fundoonotes.user.model.User;
import com.bridgelabz.fundoonotes.user.repository.UserRepositoryI;
import com.bridgelabz.fundoonotes.user.utility.Jwt;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
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
	
	/**
	 * Method: Test Case for Create Note
	 */
	@Test
	public void testCreateNote() {
		
		NoteDTO noteDTO = new NoteDTO();
		String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJlbWFpbElkIjoibXNzb25hcjI2QGdtYWlsLmNvbSJ9.PnmJiMaZVOJ03T5WgZU8k0VNEK-Osgj-mCtWe2whkUQ";
		String email = "mssonar26@gmail.com";
		noteDTO.setTitle("Title");
		noteDTO.setDescription("Description");
		note.setEmailId(email);
		System.out.println("Creating Note in JUnitTest: " + noteDTO.getTitle() +"   "+ noteDTO.getDescription());

		// Mock Object Defined
		when(jwt.getEmailId(token)).thenReturn(email);
//		when(user.getNotelist().add(note));
		when(mapper.map(noteDTO, Note.class)).thenReturn(note);
		when(noteRepo.save(note)).thenReturn(note);
		//verify(user.getNotelist().add(note));
		//when(user.getNotelist().add(note)).;

		String response = noteService.createNote(token, noteDTO);
		System.out.println("Testing CreateNote Response\n" + response);
		System.out.println(noteEnvironment.getProperty("CREATE_NOTE"));
		assertEquals(noteEnvironment.getProperty("CREATE_NOTE"), response);
	}
	
	
	/**
	 * Method: Test Case for Update Note
	 */
	@Test
	public void testUpdateNote() {
		
		Optional<Note> already = Optional.of(note);
		note.setTitle("Secrets Can't Share");
		note.setTitle("101 Fitness Routine");
		when(noteRepo.save(note)).thenReturn(note);
		assertEquals(note.getTitle(), already.get().getTitle());
	}
	
	
	/**
	* Method: Test Case for Delete Note 
	*/
	@Test
	public void testDeleteNote() {
		
		String noteId = "5dba69b03f43761e31622cbe";
		String emailId = "mssonar26@gmail.com";
		Optional<Note> already = Optional.of(note);
		when(noteRepo.findByIdAndEmailId(noteId, emailId)).thenReturn(note);
		noteRepo.deleteById(noteId);
		assertEquals(note.getId(), already.get().getId());
	}

	
	/**
	* Method: Test Case to Show All Notes
	*/
	@Test
	public void testShowNotes() {
		
		List<Note> note1 = null;
		String emailId = "mssonar26@gmail.com";
		Optional<Note> already = Optional.of(note);
		when(noteRepo.findByEmailId(emailId)).thenReturn(note1);
		assertEquals(note.getEmailId(), already.get().getEmailId());
	}

	
	/**
	* Method: Test Case to Pin Note for User 
	*/
	@Test
	public void TestisPin() {
		
		String noteId = "5dba69b03f43761e31622cbe";
		String emailId = "shelkeva@gmail.com";
		note.setPin(false);
		when(noteRepo.findByIdAndEmailId(noteId, emailId)).thenReturn(note);
		if (note.isPin())
			note.setPin(false);
		else
			note.setPin(true);
		assertEquals(true, note.isPin());
	}

	
	/**
	* Method: Test Case to Trash Note for User 
	*/
	@Test
	public void testIsTrash() {
		
		String noteId = "5dba69b03f43761e31622cbe";
		String emailId = "shelkeva@gmail.com";
		note.setTrash(false);
		when(noteRepo.findByIdAndEmailId(noteId, emailId)).thenReturn(note);
		if (note.isTrash())
			note.setTrash(false);
		else
			note.setTrash(true);
		assertEquals(true, note.isTrash());
	}

	
	/**
	* Method: Test Case to Archieve Note for User 
	*/
	@Test
	public void testIsArchieve() {
		
		String noteId = "5dba69b03f43761e31622cbe";
		String emailId = "shelkeva@gmail.com";
		note.setArchieve(false);
		when(noteRepo.findByIdAndEmailId(noteId, emailId)).thenReturn(note);
		if (note.isArchieve())
			note.setArchieve(false);
		else
			note.setArchieve(true);
		assertEquals(true, note.isArchieve());
	}
}
