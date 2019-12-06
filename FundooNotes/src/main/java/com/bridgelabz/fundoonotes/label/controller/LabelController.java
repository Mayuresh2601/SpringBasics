package com.bridgelabz.fundoonotes.label.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.fundoonotes.label.dto.LabelDTO;
import com.bridgelabz.fundoonotes.label.service.LabelService;
import com.bridgelabz.fundoonotes.user.response.Response;

@RestController
public class LabelController {
	
	@Autowired
	private LabelService service;
	
	@Autowired
	private Environment labelEnvironment;
	
	
	/**Method: To Create Label in Database
	 * @param token
	 * @param labeldto
	 * @return Create Label implementation Logic
	 */
	@PostMapping("/createlabel")
	public Response createLabel(@RequestHeader String token, @Valid @RequestBody LabelDTO labeldto) {
		
		Response response = service.createLabel(token, labeldto);
		return response;
	}
	
	
	/**Method: To Update Label in Database
	 * @param id
	 * @param labeldto
	 * @return Update Label implementation Logic
	 */
	@PutMapping("/updatelabel")
	public Response updateLabel(@RequestHeader String noteid, @RequestHeader String labelid, @RequestHeader String token,@Valid @RequestBody LabelDTO labeldto) {
		
		Response response = service.updateLabel(noteid, labelid, token, labeldto);
		return response;
	}

	
	/**Method: To Delete Label from Database
	 * @param id
	 * @return Delete Label implementation Logic
	 */
	@DeleteMapping("/deletelabel")
	public Response deleteLabel(@RequestHeader String noteid, @RequestHeader String labelid, @RequestHeader String token) {
	
		Response response = service.deleteLabel(noteid, labelid, token);
		return response;
	}
	
	
	/**Method: To Find Label By Id from database
	 * @param id
	 * @return Find Label by Id implementation Logic
	 */
	@GetMapping("/labels")
	public Response findLabelById(@RequestHeader String labelid, @RequestHeader String token) {
		
		return new Response(200, labelEnvironment.getProperty("Find_Label"), service.findLabelById(labelid, token));
	}
	
	
	/**Method: To Show All Label present in database
	 * @return Display All Label Implementation Logic
	 */
	@GetMapping("/showlabels")
	public Response showLabels() {
		
		return new Response(200, labelEnvironment.getProperty("Show_Labels"), service.showLabels());
	}
	
	
	/**Method: To Add Label to Note
	 * @param noteid
	 * @param labelid
	 * @param token
	 * @return Add Label to Note Implementation Logic
	 */
	@PostMapping("/addlabeltonote")
	public Response addLabelToNote(@RequestHeader String noteid, @RequestHeader  String labelid, @RequestHeader String token) {
		
		Response response = service.addLabelToNote(noteid,labelid, token);
		return response;
	}
}
