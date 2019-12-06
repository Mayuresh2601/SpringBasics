package com.bridgelabz.fundoonotes.note.service;

import java.util.List;
import java.util.Optional;

import com.bridgelabz.fundoonotes.note.dto.CollaboratorDTO;
import com.bridgelabz.fundoonotes.note.dto.NoteDTO;
import com.bridgelabz.fundoonotes.note.model.Note;
import com.bridgelabz.fundoonotes.user.response.Response;

public interface NoteServiceI {
	
	public Response createNote(String token, NoteDTO notedto);
	
	public Response updateNote(String noteid, String token, NoteDTO notedto);
	
	public Response deleteNote(String noteid, String token);
	
	public Optional<Note> findNoteById(String noteid, String token);
	
	public List<Note> showNotes();
	
	public boolean isPin(String noteid, String token);
	
	public boolean isTrash(String noteid, String token);
	
	public boolean isArchieve(String noteid, String token);
	
	public List<?> sortNoteByTitleAsc();
	
	public List<?> sortNoteByTitleDesc();
	
	public List<?> sortNoteByDateAsc();
	
	public List<?> sortNoteByDateDesc();
	
	public Response addCollaboratorDemo(String noteid, String token);
	
	public Response addCollaborator(String noteid, String token, CollaboratorDTO collaboratorEmailId);

	public Response removeCollaborator(String noteid, String token, String collaboratorEmailId);
	
	public Response addReminder(String token,String noteid, int year, int month, int day, int hour, int minute, int second);
	
	public Response removeReminder(String token, String noteid);
	
	public Response editReminder(String token,String noteid, int year, int month, int day, int hour, int minute, int second);
}
