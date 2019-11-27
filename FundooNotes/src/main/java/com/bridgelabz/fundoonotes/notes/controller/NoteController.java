/******************************************************************************
*  
*  Purpose: To Implement Fundoo Notes App
*  @class Note Controller
*  @author  Mayuresh Sunil Sonar
*
******************************************************************************/
package com.bridgelabz.fundoonotes.notes.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.fundoonotes.notes.dto.NoteDTO;
import com.bridgelabz.fundoonotes.notes.model.Note;
import com.bridgelabz.fundoonotes.notes.service.NoteService;
import com.bridgelabz.fundoonotes.response.Response;

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
		return new Response(100, "Create Note", result);
	}

	
	/**Method: To Update a Note
	 * @param id
	 * @param notedto
	 * @return Update Note Implementation Logic
	 */
	@PutMapping("/updatenote")
	public Response updateNote(@RequestHeader String id,@RequestBody NoteDTO notedto) {
		
		String result = noteService.updateNote(id, notedto);
		return new Response(105, "Update Note", result);
	}

	
	/**Method: To Delete a Note
	 * @param id
	 * @return Delete Note Implementation Logic
	 */
	@DeleteMapping("/deletenote")
	public Response deleteNote(@RequestHeader String id) {
		
		String result =  noteService.deleteNote(id);
		return new Response(110, "Delete Note", result);
	}

	
	/**Method: To Find Note by Token
	 * @param id
	 * @return Find Note By Id Implementation Logic 
	 */
	@GetMapping("/notes")
	public Response findNoteById(@RequestHeader String id) {
		
		Optional<Note> note = noteService.findNoteById(id);
		return new Response(115, "Note By Id", note);
	}

	
	/**Method: To Pin/Unpin a Note
	 * @param id
	 * @param token
	 * @return Pin/Unpin a Note Implementation Logic
	 */
	@PutMapping("/notes/pin")
	public Response isPin(@RequestHeader String id, @RequestHeader String token) {
		
		boolean result = noteService.isPin(id, token);
		return new Response(120, "Pin/Unpin Note", result);
	}

	
	/**Method: To Trash/Recover a Note
	 * @param id
	 * @param token
	 * @return Trash/Recover a Note Implementation Logic
	 */
	@PutMapping("/notes/trash")
	public Response isTrash(@RequestHeader String id, @RequestHeader String token) {
		
		boolean result = noteService.isTrash(id, token);
		return new Response(125, "Trash/Recover Note", result);
	}

	
	/**Method: To Archieve/Unarchieve a Note 
	 * @param id
	 * @param token
	 * @return Archieve/Unarchieve a Note Implementation Logic
	 */
	@PutMapping("/notes/archieve")
	public Response isArchieve(@RequestHeader String id, @RequestHeader String token) {
		
		boolean result = noteService.isArchieve(id, token);
		return new Response(130, "Archieve/Unarchieve Note", result);
	}

}
