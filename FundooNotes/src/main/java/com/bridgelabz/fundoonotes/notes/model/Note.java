package com.bridgelabz.fundoonotes.notes.model;

import javax.validation.constraints.NotBlank;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document
@Data
public class Note {
	
	@Id
	private String id;
	
	@NotBlank(message= "Title is Mandatory")
	private String title;
	
	@NotBlank(message = "Description is Mandatory")
	private String description;
	
	@NotBlank(message = "Email ID is Mandatory")
	private String emailId;
	
	@NotBlank(message = "Created Note Date is Mandatory")
	private String createDate;
	
	@NotBlank(message = "Edited Note Date is Mandatory")
	private String editDate;
	
	private boolean isPin;
	
	private boolean isTrash;
	
	private boolean isArchieve;

}
