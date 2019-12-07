package com.bridgelabz.fundoonotes.label.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.bridgelabz.fundoonotes.label.dto.LabelDTO;
import com.bridgelabz.fundoonotes.label.model.Label;
import com.bridgelabz.fundoonotes.label.repository.LabelRepositoryI;
import com.bridgelabz.fundoonotes.note.model.Note;
import com.bridgelabz.fundoonotes.note.repository.NoteRepositoryI;
import com.bridgelabz.fundoonotes.note.service.NoteService;
import com.bridgelabz.fundoonotes.user.exception.LoginException;
import com.bridgelabz.fundoonotes.user.model.User;
import com.bridgelabz.fundoonotes.user.repository.UserRepositoryI;
import com.bridgelabz.fundoonotes.user.response.Response;
import com.bridgelabz.fundoonotes.user.utility.Jwt;

@Service
public class LabelService implements LabelServiceI{
	
	@Autowired
	private LabelRepositoryI labelrepository;
	
	@Autowired
	private NoteRepositoryI noterepository;
	
	@Autowired
	private UserRepositoryI userrepository;
	
	@Autowired
	private NoteService noteService;
	
	@Autowired
	private Jwt jwt;
	
	@Autowired
	private ModelMapper mapper;

	@Autowired
	private Environment labelEnvironment;
	
	@Autowired
	private	Environment noteEnvironment;
	
	/**
	 *Method: To Create Label
	 */
	@Override
	public Response createLabel(String token, LabelDTO labeldto) {
		
		String email = jwt.getEmailId(token);
		User user = userrepository.findByEmail(email);
		if (email.equals(user.getEmail())) {
			
			Label label = mapper.map(labeldto, Label.class);
			label.setLabelTitle(labeldto.getLabelTitle());
			label.setEmail(email);

			LocalDateTime now = LocalDateTime.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
			String date = now.format(formatter);

			label.setCreateDate(date);
			labelrepository.save(label);
			return new Response(200, labelEnvironment.getProperty("Create_Label"), labelEnvironment.getProperty("CREATE_LABEL"));
		}
		return new Response(404, labelEnvironment.getProperty("UNAUTHORIZED_USER"), HttpStatus.BAD_REQUEST);

	}

	
	/**
	 *Method: To Update Label
	 */
	@Override
	public Response updateLabel(String noteid, String labelid, String token, LabelDTO labeldto) {
		
		String email = jwt.getEmailId(token);
		Label label = labelrepository.findById(labelid).get();
		Note n = noterepository.findById(noteid).get();
		
		if(email.equals(label.getEmail())) {
			
			if(n.getId().equalsIgnoreCase(noteid)) {
			
				if(label.getId().equalsIgnoreCase(labelid)) {
					label.setLabelTitle(labeldto.getLabelTitle());
					
					LocalDateTime now = LocalDateTime.now();
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
					String date = now.format(formatter);
					
					label.setEditDate(date);
					labelrepository.save(label);
					
					List<Note> notelist = noteService.showNotes();
					Note note = notelist.stream().filter(data -> data.getEmailId().equals(email)).findAny().orElse(null);
					note.getLabellist().removeIf(data -> data.getId().equals(labelid));
					note.getLabellist().add(label);
					noterepository.save(note);
					
					User user = userrepository.findByEmail(email);
					user.getNotelist().removeIf(data -> data.getId().equals(note.getId()));
					user.getNotelist().add(note);
					userrepository.save(user);
					return new Response(200, labelEnvironment.getProperty("Update_Label"), labelEnvironment.getProperty("UPDATE_LABEL"));
				}
				return new Response(404, labelEnvironment.getProperty("LABEL_ID_NOT_FOUND"), HttpStatus.BAD_REQUEST);
			}
			return new Response(404, labelEnvironment.getProperty("NOTE_ID_NOT_FOUND"), HttpStatus.BAD_REQUEST);
		}
		return new Response(404, labelEnvironment.getProperty("UNAUTHORIZED_USER"), HttpStatus.BAD_REQUEST);
	}

	
	/**
	 *Method: To Delete Label
	 */
	@Override
	public Response deleteLabel(String noteid, String labelid, String token) {
		
		String email = jwt.getEmailId(token);
		Label label = labelrepository.findById(labelid).get();
		if(email.equals(label.getEmail())) {
			
			Note n = noterepository.findById(noteid).get();
			if(n.getId().equalsIgnoreCase(noteid)) {
			
				if(label.getId().equalsIgnoreCase(labelid)) {
					labelrepository.deleteById(labelid);
				
					List<Note> notelist = noteService.showNotes();
					Note note = notelist.stream().filter(data -> data.getEmailId().equals(email)).findAny().orElse(null);
					note.getLabellist().remove(label);
					noterepository.save(note);
					
					User user = userrepository.findByEmail(email);
					user.getNotelist().removeIf(data -> data.getId().equals(note.getId()));
					user.getNotelist().add(note);
					userrepository.save(user);
					return new Response(200, labelEnvironment.getProperty("Delete_Label"), labelEnvironment.getProperty("DELETE_LABEL"));
				}
				return new Response(404, labelEnvironment.getProperty("LABEL_ID_NOT_FOUND"), labelEnvironment.getProperty("LABEL_ID_NOT_FOUND"));
			}
			return new Response(404, noteEnvironment.getProperty("NOTE_ID_NOT_FOUND"), noteEnvironment.getProperty("NOTE_ID_NOT_FOUND"));
		}
		return new Response(404, labelEnvironment.getProperty("UNAUTHORIZED_USER"), labelEnvironment.getProperty("UNAUTHORIZED_USER"));
	}

	
	/**
	 *Method: To Show All Labels
	 */
	@Override
	public Response showLabels() {
		
		List<Label> list = labelrepository.findAll();
		return new Response(200, labelEnvironment.getProperty("Delete_Label"), list);
	}

	
	/**
	 *Method: To Find Label By Id
	 */
	@Override
	public Response findLabelById(String labelid, String token) {
		
		String email = jwt.getEmailId(token);
		Label label = labelrepository.findById(labelid).get();
		
		if(email.equals(label.getEmail())) {
			return new Response(200, labelEnvironment.getProperty("Find_Label"), label);
		}
		return new Response(404, labelEnvironment.getProperty("UNAUTHORIZED_USER"), HttpStatus.BAD_REQUEST);
	}
	
	
	 /**
     * Adding label into notes and notes into labellist
     */
    @Override
    public Response addLabelToNote(String noteid, String labelid, String token) {
    	
    	String emailId = jwt.getEmailId(token);
        User user = userrepository.findByEmail(emailId);
        if (emailId.equals(user.getEmail())) {
            List<Note> listNote = noterepository.findByEmailId(emailId);

            Note note = listNote.stream().filter(i -> i.getId().equals(noteid)).findAny().orElse(null);
            Label label = labelrepository.findById(labelid).get();
            if (note.getId().equals(noteid)) {

            	if(label.getId().equals(labelid)) {
            		label.getNotelist().add(note);
            		labelrepository.save(label);
            		
            		note.getLabellist().add(label);
	                noterepository.save(note);
	                
	    			user.getNotelist().removeIf(data -> data.getId().equals(note.getId()));
	    			user.getNotelist().add(note);
	    			userrepository.save(user);
	                return new Response(200, labelEnvironment.getProperty("Add_Label_To_Note"), labelEnvironment.getProperty("LABEL_ADDED_TO_NOTE"));
            	}
                return new Response(404, labelEnvironment.getProperty("LABEL_ID_NOT_FOUND"), HttpStatus.BAD_REQUEST);
            }
            return new Response(404, labelEnvironment.getProperty("NOTE_ID_NOT_FOUND"), HttpStatus.BAD_REQUEST);
        }
        return new Response(404, labelEnvironment.getProperty("UNAUTHORIZED_USER"), HttpStatus.BAD_REQUEST);

    }

}
