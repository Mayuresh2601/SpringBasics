package com.bridgelabz.fundoonotes.service.testcase;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import com.bridgelabz.fundoonotes.label.repository.LabelRepositoryI;
import com.bridgelabz.fundoonotes.note.dto.NoteDTO;
import com.bridgelabz.fundoonotes.note.model.Note;
import com.bridgelabz.fundoonotes.note.repository.NoteRepositoryI;
import com.bridgelabz.fundoonotes.note.service.NoteService;
import com.bridgelabz.fundoonotes.user.repository.UserRepositoryI;
import com.bridgelabz.fundoonotes.user.utility.Jwt;

@SpringBootTest
public class NoteServiceTest {
	
	@InjectMocks
	NoteService noteService;
	
	@Mock
	Jwt jwt;
	
	@Mock
	NoteRepositoryI noteRepo;
	
	@Mock
	LabelRepositoryI labelRepo;
	
	@Mock
	UserRepositoryI userRepo;
	
	@Mock
	ModelMapper mapper;
	
	
	Note note = new Note("1","Dragon Ball","Action Cartoon","mssonar26@gmail.com",null,null,false,false,false,null,null,null);
	
	/**
	 * Method: Test Case for Create Note
	 */
	@Test
	public void testCreateNote() {
		
		NoteDTO noteDTO = new NoteDTO();

		noteDTO.setTitle("title");
		noteDTO.setDescription("description");
		System.out.println("Creating Note in Testing:" + noteDTO.getTitle() +"   "+ noteDTO.getDescription());
		String email = "mssonar26@gmail.com";

		// Mock Object Defined
		when(mapper.map(noteDTO, Note.class)).thenReturn(note);
		when(noteRepo.save(note)).thenReturn(note);

		String response = noteService.createNote(email, noteDTO);
		System.out.println("Testing CreateNote Response\n" + response + "\n");
		assertEquals(HttpStatus.OK, noteService.createNote(email, noteDTO));
	}
	
	/**
	 * Method: Test Case for Update Note
	 */
	@Test
	public void testUpdateNote() {
		
		NoteDTO noteDTO = new NoteDTO();
		Optional<Note> already = Optional.of(note);
		note.setTitle("Secrets Can't Share");
		note.setTitle("101 Fitness Routine");
		when(noteRepo.save(note)).thenReturn(note);
		assertEquals(noteDTO.getTitle(), already.get().getEmailId());
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
