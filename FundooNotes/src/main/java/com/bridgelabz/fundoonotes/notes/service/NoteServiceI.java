package com.bridgelabz.fundoonotes.notes.service;

import java.util.Optional;

import com.bridgelabz.fundoonotes.notes.dto.NoteDTO;
import com.bridgelabz.fundoonotes.notes.model.Note;

public interface NoteServiceI {
	
	public String createNote(String token, NoteDTO notedto);
	
	public String updateNote(String id, NoteDTO notedto);
	
	public String deleteNote(String id);
	
	public Optional<Note> findNoteById(String id);
	
	public boolean isPin(String id, String token);
	
	public boolean isTrash(String id, String token);
	
	public boolean isArchieve(String id, String token);

}
