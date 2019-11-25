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

@RestController
public class NoteController {
	
	@Autowired
	NoteService noteService;
	
	@PostMapping("/notecreate")
	public String createNote(@RequestBody NoteDTO notedto) {
		
		return noteService.createNote(notedto);
	}

	@PutMapping("/noteupdate")
	public String updateNote(@RequestHeader String token,@RequestBody NoteDTO notedto) {
		
		return noteService.updateNote(token, notedto);
	}

	@DeleteMapping("/notedelete")
	public String deleteNote(@RequestHeader String token) {
		
		return noteService.deleteNote(token);
	}

	@GetMapping("/notes")
	public Optional<Note> findNoteByToken(@RequestHeader String token) {
		
		return noteService.findNoteByToken(token);
	}

	@PutMapping("/notes/pin")
	public boolean isPin(@RequestHeader String token) {
		
		return noteService.isPin(token);
	}

	@PutMapping("/notes/trash")
	public boolean isTrash(@RequestHeader String token) {
		
		return noteService.isTrash(token);
	}

	@PutMapping("/notes/archieve")
	public boolean isArchieve(@RequestHeader String token) {
		
		return noteService.isArchieve(token);
	}

}
