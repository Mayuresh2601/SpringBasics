package com.bridgelabz.fundoonotes.notes.service;

import java.util.Optional;

import com.bridgelabz.fundoonotes.notes.dto.NoteDTO;
import com.bridgelabz.fundoonotes.notes.model.Note;

public interface NoteServiceI {
	
	public String createNote(NoteDTO notedto);
	
	public String updateNote(String token, NoteDTO notedto);
	
	public String deleteNote(String token);
	
	public Optional<Note> findNoteByToken(String id);
	
	public boolean isPin(String token);
	
	public boolean isTrash(String token);
	
	public boolean isArchieve(String token);

}
