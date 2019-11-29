package com.bridgelabz.fundoonotes.label.service;

import java.util.List;
import com.bridgelabz.fundoonotes.label.dto.LabelDTO;
import com.bridgelabz.fundoonotes.label.model.Label;

public interface LabelServiceI {
	
	public String createLabel(String id, String token, LabelDTO labeldto);
	
	public String updateLabel(String id, LabelDTO labeldto);
	
	public String deleteLabel(String id);
	
	public List<Label> showLabels();
	
	public Label findLabelById(String id);

}
