package com.bridgelabz.fundoonotes.label.model;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotEmpty;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.bridgelabz.fundoonotes.note.model.Note;

import lombok.Data;

@Document
@Data
public class Label {
	
	@Id
	private String id;
	
	@NotEmpty(message = "Label Title is Mandatory")
	private String labelTitle;
	
	private String email;
	
	private String createDate;
	
	private String editDate;
	
	@DBRef
	private List<Note> notelist = new ArrayList<>();

	@Override
	public String toString() {
		return "Label [id=" + id + ", labelTitle=" + labelTitle + ", email=" + email + ", createDate=" + createDate
				+ ", editDate=" + editDate + ", notelist=" + notelist + "]";
	}

}
