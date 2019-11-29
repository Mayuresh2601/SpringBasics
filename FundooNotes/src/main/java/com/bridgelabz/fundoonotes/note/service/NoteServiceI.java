package com.bridgelabz.fundoonotes.note.service;

import java.util.List;
import java.util.Optional;

import com.bridgelabz.fundoonotes.note.dto.NoteDTO;
import com.bridgelabz.fundoonotes.note.model.Note;

public interface NoteServiceI {
	
	public String createNote(String token, NoteDTO notedto);
	
	public String updateNote(String id, NoteDTO notedto);
	
	public String deleteNote(String id);
	
	public Optional<Note> findNoteById(String id);
	
	public List<Note> showNotes();
	
	public boolean isPin(String id, String token);
	
	public boolean isTrash(String id, String token);
	
	public boolean isArchieve(String id, String token);
	
	public List<?> sortNoteByTitle();
	
	public List<?> sortNoteByDate();
	
	public String addCollaboratorDemo(String id, String token);
	
	public String addCollaborator(String id, String collaboratorEmailId);

	public String removeCollaborator(String id, String collaboratorEmailId);
	
	public String addReminder(String token,String id, int year, int month, int day, int hour, int minute, int second);
	
	public String removeReminder(String token, String id);
	
	public String editReminder(String token,String id, int year, int month, int day, int hour, int minute, int second);
}
