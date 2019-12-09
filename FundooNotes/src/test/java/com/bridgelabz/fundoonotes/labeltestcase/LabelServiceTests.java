package com.bridgelabz.fundoonotes.labeltestcase;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

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

import com.bridgelabz.fundoonotes.label.dto.LabelDTO;
import com.bridgelabz.fundoonotes.label.model.Label;
import com.bridgelabz.fundoonotes.label.repository.LabelRepositoryI;
import com.bridgelabz.fundoonotes.label.service.LabelService;
import com.bridgelabz.fundoonotes.note.model.Note;
import com.bridgelabz.fundoonotes.note.repository.NoteRepositoryI;
import com.bridgelabz.fundoonotes.user.model.User;
import com.bridgelabz.fundoonotes.user.repository.UserRepositoryI;
import com.bridgelabz.fundoonotes.user.response.Response;
import com.bridgelabz.fundoonotes.user.utility.Jwt;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@TestPropertySource("classpath:message.properties")
public class LabelServiceTests {

	
	@InjectMocks
	private LabelService labelService;
	
	@Mock
	private UserRepositoryI userRepo;
	
	@Mock
	private NoteRepositoryI noteRepo;
	
	@Mock
	private LabelRepositoryI labelRepo;
	
	@Mock
	private LabelDTO labeldto;
	
	@Mock
	private Jwt jwt;
	
	@Mock
	private ModelMapper mapper;
	
	@Mock
	private Environment labelEnv;
	
	Label label = new Label();
	Note note = new Note();
	User user = new User();
	String noteId = "5dea5dee836c8f441d888494";
	String labelId = "5dea5dee836c8f441d888494";
	String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJlbWFpbElkIjoibXNzb25hcjI2QGdtYWlsLmNvbSJ9.PnmJiMaZVOJ03T5WgZU8k0VNEK-Osgj-mCtWe2whkUQ";
	String email = "mssonar26@gmail.com";
	String password = "123456";
	String confirmPassword = "123456";
	boolean status = true;
	Optional<Label> optionalLabel = Optional.of(label);
	Optional<Note> optionalNote = Optional.of(note);
	List<Label> userlist = new ArrayList<>();
	
	/**
	 * Method: Test Case to Create Label
	 */
	@Test
	public void testCreateLabel() {
		
		when(jwt.getEmailId(token)).thenReturn(email);
		when(userRepo.findByEmail(email)).thenReturn(user);
		when(mapper.map(labeldto, Label.class)).thenReturn(label);
		user.setEmail(email);
		
		Response response = labelService.createLabel(token, labeldto);
		assertEquals(200, response.getStatus());
	}
	
	
	/**
	 * Method: Test Case to Update Label
	 */
	@Test
	public void testUpdateLabel() {
		note.setId(noteId);
		label.setId(labelId);
		when(jwt.getEmailId(token)).thenReturn(email);
		when(labelRepo.findById(labelId)).thenReturn(optionalLabel);
		when(noteRepo.findById(noteId)).thenReturn(optionalNote);
		when(mapper.map(labeldto, Label.class)).thenReturn(label);

		assertTrue(note.getId().equals(noteId));
		assertTrue(label.getId().equals(labelId));
		Response response = labelService.updateLabel(noteId, labelId, token, labeldto);
		assertEquals(200, response.getStatus());
		
	}
	
	
	/**
	 * Method: Test Case to Delete Label
	 */
	public void testDeleteLabel() {
		
	}
	
	
	/**
	 * Method: Test Case to Show All Labels
	 */
	public void showLabels() {
		
	}
	
	
	/**
	 * Method: Test Case to Find Label By Id
	 */
	public void testFindLabelById() {
		
	}
	
	
	/**
	 * Method: Test Case to Add Label to Note
	 */
	public void testAddLabelToNote() {
		
	}
}
