package com.bridgelabz.fundoonotes.label.service;

import java.util.List;
import com.bridgelabz.fundoonotes.label.dto.LabelDTO;
import com.bridgelabz.fundoonotes.label.model.Label;

public interface LabelServiceI {
	
	public String createLabel(String token, LabelDTO labeldto);
	
	public String updateLabel(String noteid, String labelid, String token, LabelDTO labeldto);
	
	public String deleteLabel(String noteid, String labelid, String token);
	
	public List<Label> showLabels();
	
	public Label findLabelById(String labelid, String token);
	
	public String addLabelToNote(String noteId, String labelId, String token);

}
