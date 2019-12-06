package com.bridgelabz.fundoonotes.label.service;

import java.util.List;
import com.bridgelabz.fundoonotes.label.dto.LabelDTO;
import com.bridgelabz.fundoonotes.label.model.Label;
import com.bridgelabz.fundoonotes.user.response.Response;

public interface LabelServiceI {
	
	public Response createLabel(String token, LabelDTO labeldto);
	
	public Response updateLabel(String noteid, String labelid, String token, LabelDTO labeldto);
	
	public Response deleteLabel(String noteid, String labelid, String token);
	
	public List<Label> showLabels();
	
	public Label findLabelById(String labelid, String token);
	
	public Response addLabelToNote(String noteId, String labelId, String token);

}
