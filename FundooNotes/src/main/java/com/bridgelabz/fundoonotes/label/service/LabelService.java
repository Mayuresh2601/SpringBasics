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
import com.bridgelabz.fundoonotes.user.utility.Jms;
import com.bridgelabz.fundoonotes.user.utility.Jwt;

@Service
public class LabelService implements LabelServiceI{
	
	@Autowired
	LabelRepositoryI repository;
	
	@Autowired
	Jwt jwt;
	
	@Autowired
	Jms jms;
	
	@Autowired
	ModelMapper mapper;

	@Override
	public String createLabel(String token, LabelDTO labeldto) {
		String email = jwt.getToken(token);
		
		if(email != null) {
			Label label = mapper.map(labeldto, Label.class);
			label.setLabelTitle(labeldto.getLabelTitle());
			
			LocalDateTime now = LocalDateTime.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
			String date = now.format(formatter);
			
			label.setCreateDate(date);
			repository.save(label);
		}
		return LabelMessageReference.CREATE_LABEL;
	}

	@Override
	public String updateLabel(String id, LabelDTO labeldto) {
		Label label = repository.findById(id).get();
		
		if(label != null) {
			label.setLabelTitle(labeldto.getLabelTitle());
			
			LocalDateTime now = LocalDateTime.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
			String date = now.format(formatter);
			
			label.setEditDate(date);
			repository.save(label);
			return LabelMessageReference.UPDATE_LABEL;
		}
		return LabelMessageReference.ID_NOT_FOUND;
	}

	@Override
	public String deleteLabel(String id) {
		
		repository.deleteById(id);
		return LabelMessageReference.DELETE_LABEL;
	}

	@Override
	public List<Label> showLabels() {
		
		List<Label> list = repository.findAll();
		return list;
	}

	@Override
	public Label findLabelById(String id) {
		Label label = repository.findById(id).get();
		return label;
	}



}
