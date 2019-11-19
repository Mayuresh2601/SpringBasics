package com.bridgelabz.springboot.starter;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class TopicService {
	
	List<Topic> topics = new ArrayList<>(Arrays.asList(
					new Topic(23,"Java","Java Spring and Api Calling"),
					new Topic(67,"Python","Basics of Python Programming"),
					new Topic(19,"Angularjs","Designing Interactive Angular UI")
					));
	
	public List<Topic> getAllDetails(){
		
		return topics;
	}
	
	public Topic getTopic(int id){
		
		return topics.stream().filter(t -> t.getId() == id).findFirst().get();
	}

	public void addTopic(Topic topic) {
		
		topics.add(topic);
	}
	
	public void putTopic(Topic topic, int id) {
		
		for (int i = 0; i < topics.size(); i++) {
			
			Topic t = topics.get(i);
			if(t.getId() == id) {
				topics.set(i, topic);
				return;
			}
		}
	}

	public void deleteTopic(int id) {
		
		topics.removeIf(t -> t.getId() == id);
	}


	 

}
