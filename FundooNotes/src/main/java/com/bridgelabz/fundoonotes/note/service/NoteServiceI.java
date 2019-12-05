package com.bridgelabz.fundoonotes.note.service;

import java.util.List;
import java.util.Optional;

import com.bridgelabz.fundoonotes.note.dto.CollaboratorDTO;
import com.bridgelabz.fundoonotes.note.dto.NoteDTO;
import com.bridgelabz.fundoonotes.note.model.Note;

public interface NoteServiceI {
	
	public String createNote(String token, NoteDTO notedto);
	
	public String updateNote(String noteid, String token, NoteDTO notedto);
	
	public String deleteNote(String noteid, String token);
	
	public Optional<Note> findNoteById(String noteid, String token);
	
	public List<Note> showNotes();
	
	public boolean isPin(String noteid, String token);
	
	public boolean isTrash(String noteid, String token);
	
	public boolean isArchieve(String noteid, String token);
	
	public List<?> sortNoteByTitleAsc();
	
	public List<?> sortNoteByTitleDesc();
	
	public List<?> sortNoteByDateAsc();
	
	public List<?> sortNoteByDateDesc();
	
	public String addCollaboratorDemo(String noteid, String token);
	
	public String addCollaborator(String noteid, String token, CollaboratorDTO collaboratorEmailId);

	public String removeCollaborator(String noteid, String token, String collaboratorEmailId);
	
	public String addReminder(String token,String noteid, int year, int month, int day, int hour, int minute, int second);
	
	public String removeReminder(String token, String noteid);
	
	public String editReminder(String token,String noteid, int year, int month, int day, int hour, int minute, int second);
}
