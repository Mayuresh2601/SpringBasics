package com.bridgelabz.fundoonotes.notes.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bridgelabz.fundoonotes.notes.dto.NoteDTO;
import com.bridgelabz.fundoonotes.notes.model.Note;
import com.bridgelabz.fundoonotes.notes.repository.NoteRepositoryI;
import com.bridgelabz.fundoonotes.utility.Jms;
import com.bridgelabz.fundoonotes.utility.Jwt;

@Service
public class NoteService implements NoteServiceI{
	
	@Autowired
	Jwt jwt;
	
	@Autowired
	Jms jms;
	
	@Autowired
	NoteRepositoryI repository;
	
	@Override
	public String createNote(NoteDTO notedto) {
		ModelMapper mapper = new ModelMapper();
		Note note = mapper.map(notedto, Note.class);
		note.setTitle(notedto.getTitle());
		note.setDescription(notedto.getDescription());
		String token = jwt.createToken(note.getEmailId());
		
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
		String date = now.format(formatter);
		
		note.setCreateDate(date);
		jms.sendMail(note.getEmailId(), token);
		repository.save(note);
		return "Note Successfully Created";
	}

	@Override
	public String updateNote(String token, NoteDTO notedto) {
		
		String email = jwt.getToken(token);
		
		if(email != null) {
			Note update = repository.findByEmailId(email);
			update.setTitle(notedto.getTitle());
			update.setDescription(notedto.getDescription());
			
			LocalDateTime now = LocalDateTime.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
			
			String date = now.format(formatter);
			update.setEditDate(date);
			repository.save(update);
			return "Note Successfully Updated";
		}
		return "Id Not Found";
	}

	@Override
	public String deleteNote(String token) {
		String email = jwt.getToken(token);
		if(email != null) {
			
			Note delete = repository.findByEmailId(email);
			repository.delete(delete);
			return "Note Successfully Deleted";
		}
		else {
			return "Id Not Found";
		}
	}

	@Override
	public Optional<Note> findNoteByToken(String id) {
		
		return repository.findById(id);
		
	}

	@Override
	public boolean isPin(String token) {
		
		String email = jwt.getToken(token);
		if (email != null) {

			Note note = repository.findByEmailId(email);
			if (note != null) {
				note.setPin(!(note.isPin()));
				repository.save(note);
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean isTrash(String token) {
		String email = jwt.getToken(token);
		if (email != null) {

			Note note = repository.findByEmailId(email);
			if (note != null) {
				note.setTrash(!(note.isTrash()));
				repository.save(note);
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean isArchieve(String token) {
		
		String email = jwt.getToken(token);
		if (email != null) {

			Note note = repository.findByEmailId(email);
			if (note != null) {
				note.setArchieve(!(note.isArchieve()));
				repository.save(note);
				return true;
			}
		}
		return false;
	}

}
