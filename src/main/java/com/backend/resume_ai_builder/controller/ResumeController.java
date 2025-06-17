package com.backend.resume_ai_builder.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.resume_ai_builder.services.ResumeService;
 
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/resume")
@CrossOrigin("*")
@RequiredArgsConstructor
public class ResumeController {
 
	private final ResumeService service;
	@PostMapping("/generate")
	public ResponseEntity<Map<String, Object>> generateResume(@RequestBody String userDescription){
		
		try {
			Map<String, Object> response = service.generateResumeResponse(userDescription);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}catch(Exception e) {
			 Map<String, Object> errorBody = new HashMap<>();
		        errorBody.put("error", "Failed to generate resume");
		        errorBody.put("details", e.getMessage());

		        return new ResponseEntity<>(errorBody, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
}
