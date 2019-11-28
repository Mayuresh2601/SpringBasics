/******************************************************************************
*  
*  Purpose: To Implement Fundoo Notes App
*  @class Note Controller
*  @author  Mayuresh Sunil Sonar
*
******************************************************************************/
package com.bridgelabz.fundoonotes.note.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.fundoonotes.note.dto.NoteDTO;
import com.bridgelabz.fundoonotes.note.model.Note;
import com.bridgelabz.fundoonotes.note.service.NoteService;
import com.bridgelabz.fundoonotes.user.response.Response;

@RestController
public class NoteController {
	
	@Autowired
	NoteService noteService;
	
	
	/**Method: To create a Note
	 * @param token
	 * @param notedto
	 * @return Create Note Implementation Logic
	 */
	@PostMapping("/createnote")
	public Response createNote(@RequestHeader String token, @RequestBody NoteDTO notedto) {
		
		String result = noteService.createNote(token, notedto);
		return new Response(200, "Creating Note", result);
	}

	
	/**Method: To Update a Note
	 * @param id
	 * @param notedto
	 * @return Update Note Implementation Logic
	 */
	@PutMapping("/updatenote")
	public Response updateNote(@RequestHeader String id,@RequestBody NoteDTO notedto) {
		
		String result = noteService.updateNote(id, notedto);
		return new Response(200, "Updating Note", result);
	}

	
	/**Method: To Delete a Note
	 * @param id
	 * @return Delete Note Implementation Logic
	 */
	@DeleteMapping("/deletenote")
	public Response deleteNote(@RequestHeader String id) {
		
		String result =  noteService.deleteNote(id);
		return new Response(200, "Deleting Note", result);
	}

	
	/**Method: To Find Note by Token
	 * @param id
	 * @return Find Note By Id Implementation Logic 
	 */
	@GetMapping("/notes")
	public Response findNoteById(@RequestHeader String id) {
		
		Note note = noteService.findNoteById(id).get();
		return new Response(200, "Finding Note By Id", note);
	}
	
	
	/**Method: To Display All Notes
	 * @return Display All Notes Implementation Logic
	 */
	@GetMapping("/shownotes")
	public Response showNotes() {
		
		List<Note> note = noteService.showNotes();
		return new Response(200, "Showing All Notes", note);
	}

	
	/**Method: To Pin/Unpin a Note
	 * @param id
	 * @param token
	 * @return Pin/Unpin a Note Implementation Logic
	 */
	@PutMapping("/notes/pin")
	public Response isPin(@RequestHeader String id, @RequestHeader String token) {
		
		boolean result = noteService.isPin(id, token);
		return new Response(200, "Pin/Unpin Note", result);
	}

	
	/**Method: To Trash/Recover a Note
	 * @param id
	 * @param token
	 * @return Trash/Recover a Note Implementation Logic
	 */
	@PutMapping("/notes/trash")
	public Response isTrash(@RequestHeader String id, @RequestHeader String token) {
		
		boolean result = noteService.isTrash(id, token);
		return new Response(200, "Trash/Recover Note", result);
	}

	
	/**Method: To Archieve/Unarchieve a Note 
	 * @param id
	 * @param token
	 * @return Archieve/Unarchieve a Note Implementation Logic
	 */
	@PutMapping("/notes/archieve")
	public Response isArchieve(@RequestHeader String id, @RequestHeader String token) {
		
		boolean result = noteService.isArchieve(id, token);
		return new Response(200, "Archieve/Unarchieve Note", result);
	}
	
	
	/**Method: To Sort Notes By Title
	 * @return Sorted Notes By Title Implementation Logic
	 */
	@GetMapping("/sortbytitle")
	public Response sortNoteByTitle() {
		
		List<?> list = noteService.sortNoteByTitle();
		return new Response(200, "Sorting Notes By Title", list);
	}
	
	
	/**Method: To Sort Notes By Note Creation Date
	 * @return Sorted Notes By Note Creation Date Implementation Logic
	 */
	@GetMapping("/sortbydate")
	public Response sortNoteByDate() {
		
		List<?> list = noteService.sortNoteByDate();
		return new Response(200, "Sorting Notes By Date", list);
	}
	
	
	/**Method: To Add Collaborators using Users Authenticated Tokens
	 * @param id
	 * @param token
	 * @return Adding Collaborator using token Implementation Logic
	 */
	@PostMapping("/addcollaboratordemo")
	public Response addCollaboratorDemo(@RequestHeader String id,@RequestHeader String token) {
		
		String result = noteService.addCollaboratorDemo(id, token);
		return new Response(200, "Adding Collaborator", result);
	}
	
	
	/**Method: To Add Collaborators using emailId
	 * @param id
	 * @param collaboratorEmail
	 * @return Adding Collaborator using emailId Implementation Logic
	 */
	@PostMapping("/addcollaborator")
	public Response addCollaborator(@RequestHeader String id,@RequestHeader String collaboratorEmail) {
		
		String result = noteService.addCollaborator(id, collaboratorEmail);
		return new Response(200, "Adding Collaborator", result);
	}
	
	
	/**Method: To Remove Collaborators using emailId
	 * @param id
	 * @param collaboratorEmail
	 * @return Removing Collaborator using emailId Implementation Logic
	 */
	@DeleteMapping("removecollaborator")
	public Response removeCollaborator(@RequestHeader String id,@RequestHeader String collaboratorEmail) {
		
		String result = noteService.removeCollaborator(id, collaboratorEmail);
		return new Response(200, "Removing Collaborator", result);
	}

}
