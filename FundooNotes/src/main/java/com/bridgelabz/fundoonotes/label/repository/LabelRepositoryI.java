package com.bridgelabz.fundoonotes.label.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.bridgelabz.fundoonotes.label.model.Label;

@Repository
public interface LabelRepositoryI extends MongoRepository<Label, String>{
	
//	Label findLabelByEmailId(String id);

}
