package com.bridgelabz.fundoonotes.label.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.bridgelabz.fundoonotes.label.dto.LabelDTO;
import com.bridgelabz.fundoonotes.label.model.Label;
import com.bridgelabz.fundoonotes.label.repository.LabelRepositoryI;
import com.bridgelabz.fundoonotes.note.model.Note;
import com.bridgelabz.fundoonotes.note.repository.NoteRepositoryI;
import com.bridgelabz.fundoonotes.note.service.NoteService;
import com.bridgelabz.fundoonotes.user.exception.LoginException;
import com.bridgelabz.fundoonotes.user.exception.NoteException;
import com.bridgelabz.fundoonotes.user.model.User;
import com.bridgelabz.fundoonotes.user.repository.UserRepositoryI;
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
	public String createLabel(String token, LabelDTO labeldto) {
		
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
			return labelEnvironment.getProperty("CREATE_LABEL");
		}
		throw new LoginException(labelEnvironment.getProperty("UNAUTHORIZED_USER"));

	}

	
	/**
	 *Method: To Update Label
	 */
	@Override
	public String updateLabel(String noteid, String labelid, String token, LabelDTO labeldto) {
		
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
					return labelEnvironment.getProperty("UPDATE_LABEL");
				}
				return labelEnvironment.getProperty("LABEL_ID_NOT_FOUND");
			}
			return labelEnvironment.getProperty("NOTE_ID_NOT_FOUND");
		}
		throw new LoginException(labelEnvironment.getProperty("UNAUTHORIZED_USER"));
	}

	
	/**
	 *Method: To Delete Label
	 */
	@Override
	public String deleteLabel(String noteid, String labelid, String token) {
		
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
					return labelEnvironment.getProperty("DELETE_LABEL");
				}
				return labelEnvironment.getProperty("LABEL_ID_NOT_FOUND");
			}
			return labelEnvironment.getProperty("LABEL_ID_NOT_FOUND");
		}
		throw new LoginException(labelEnvironment.getProperty("UNAUTHORIZED_USER"));
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
	public Label findLabelById(String labelid, String token) {
		
		String email = jwt.getEmailId(token);
		Label label = labelrepository.findById(labelid).get();
		
		if(email.equals(label.getEmail())) {
			return label;
		}
		throw new LoginException(labelEnvironment.getProperty("UNAUTHORIZED_USER"));
	}
	
	
	 /**
     * Adding label into notes and notes into labellist
     */
    @Override
    public String addLabelToNote(String noteid, String labelid, String token) {
    	
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
	                return (labelEnvironment.getProperty("LABEL_ADDED_TO_NOTE"));
            	}
            	return labelEnvironment.getProperty("LABEL_ID_NOT_FOUND");
            }
            return noteEnvironment.getProperty("NOTE_ID_NOT_FOUND");
        }
        throw new NoteException(labelEnvironment.getProperty("UNAUTHORIZED_USER"));

    }

}
