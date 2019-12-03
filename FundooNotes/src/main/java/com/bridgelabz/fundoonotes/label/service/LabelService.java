package com.bridgelabz.fundoonotes.label.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bridgelabz.fundoonotes.label.dto.LabelDTO;
import com.bridgelabz.fundoonotes.label.model.Label;
import com.bridgelabz.fundoonotes.label.repository.LabelRepositoryI;
import com.bridgelabz.fundoonotes.note.model.Note;
import com.bridgelabz.fundoonotes.note.repository.NoteRepositoryI;
import com.bridgelabz.fundoonotes.user.exception.LoginException;
import com.bridgelabz.fundoonotes.user.model.User;
import com.bridgelabz.fundoonotes.user.repository.UserRepositoryI;
import com.bridgelabz.fundoonotes.user.utility.Jwt;

@Service
public class LabelService implements LabelServiceI{
	
	@Autowired
	LabelRepositoryI labelrepository;
	
	@Autowired
	NoteRepositoryI noterepository;
	
	@Autowired
	UserRepositoryI userrepository;
	
	@Autowired
	Jwt jwt;
	
	@Autowired
	ModelMapper mapper;

	
	/**
	 *Method: To Create Label
	 */
	@Override
	public String createLabel(String id, String token, LabelDTO labeldto) {
		
		String email = jwt.getToken(token);
		if(email != null) {
			Label label = mapper.map(labeldto, Label.class);
			label.setLabelTitle(labeldto.getLabelTitle());
			label.setEmail(email);
			
			LocalDateTime now = LocalDateTime.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
			String date = now.format(formatter);
			
			label.setCreateDate(date);
			labelrepository.save(label);
			
			Note note = noterepository.findById(id).get();
			note.getLabellist().add(label);
			noterepository.save(note);
		
			label.getNotelist().add(note);
			labelrepository.save(label);
			
//			Note note1 = noterepository.findById(id).get();
			User user = userrepository.findByEmail(email);
//			user.getNotelist().removeIf(data -> data.getId().equals(note1.getId()));
			//user.getNotelist().stream().filter(i -> i.getId().equals(label.getId()));
			userrepository.save(user);
			return LabelMessageReference.CREATE_LABEL;
		}
		throw new LoginException(LabelMessageReference.UNAUTHORIZED_USER);
	}

	
	/**
	 *Method: To Update Label
	 */
	@Override
	public String updateLabel(String id, String token, LabelDTO labeldto) {
		
		String email = jwt.getToken(token);
		if(email != null) {
			Label label = labelrepository.findById(id).get();
			
			if(label != null) {
				label.setLabelTitle(labeldto.getLabelTitle());
				
				LocalDateTime now = LocalDateTime.now();
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
				String date = now.format(formatter);
				
				label.setEditDate(date);
				labelrepository.save(label);
				
//				Note note1 = noterepository.findById(id).get();
//				User user = userrepository.findByEmail(email);
//				user.getNotelist().removeIf(data -> data.getId().equals(note1.getId()));
//				user.getNotelist().add(label);
//				userrepository.save(user);
				return LabelMessageReference.UPDATE_LABEL;
			}
			return LabelMessageReference.ID_NOT_FOUND;
		}
		throw new LoginException(LabelMessageReference.UNAUTHORIZED_USER);
	}

	
	/**
	 *Method: To Delete Label
	 */
	@Override
	public String deleteLabel(String id) {
		
		labelrepository.deleteById(id);
		return LabelMessageReference.DELETE_LABEL;
	}

	
	/**
	 *Method: To Show All Labels
	 */
	@Override
	public List<Label> showLabels() {
		
		List<Label> list = labelrepository.findAll();
		return list;
	}

	
	/**
	 *Method: To Find Label By Id
	 */
	@Override
	public Label findLabelById(String id) {
		
		Label label = labelrepository.findById(id).get();
		return label;
	}

}
