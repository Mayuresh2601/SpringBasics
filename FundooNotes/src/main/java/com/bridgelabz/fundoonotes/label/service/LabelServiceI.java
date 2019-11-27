package com.bridgelabz.fundoonotes.label.service;

import java.util.List;
import com.bridgelabz.fundoonotes.label.dto.LabelDTO;
import com.bridgelabz.fundoonotes.label.model.Label;

public interface LabelServiceI {
	
	String createLabel(String token, LabelDTO labeldto);
	
	String updateLabel(String id, LabelDTO labeldto);
	
	String deleteLabel(String id);
	
	List<Label> showLabels();
	
	Label findLabelById(String id);

}
