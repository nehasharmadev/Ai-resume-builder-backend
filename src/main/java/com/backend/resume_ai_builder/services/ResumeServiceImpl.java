package com.backend.resume_ai_builder.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ResumeServiceImpl  implements ResumeService{

	private final WebClient  webClient;
    @Value("${gemini.api.url}")
	private String geminiApiUrl;
    @Value("${gemini.api.key}")
	private String geminiApiKey;
    
    public  ResumeServiceImpl(WebClient.Builder webClient) {
    	this.webClient = webClient.build();
    }
	@Override
	public Map<String, Object> generateResumeResponse(String userResumeDescription) throws IOException {
		// TODO Auto-generated method stub
		 
		//create prompt 
		String promptString = loadPromptFromFile("resume_prompt.text");
		String promptContent = putValuesToTemplate(promptString, Map.of(
				                "userDescription", userResumeDescription));
		
		// generate request
				 Map<String,Object> requestBody = Map.of(
						 "contents", new Object[] {
							Map.of("parts", new Object[] {
								Map.of("text", promptContent)
							})
						 }
						 );
		
				 // Make a request
				 
				 String response = webClient.post()
						           .uri(geminiApiUrl + geminiApiKey)
						           .header("Content-Type","application/json")
						           .bodyValue(requestBody)
						           .retrieve()
						           .bodyToMono(String.class)
						           .block();
//				 String response = loadPromptFromFile("gemini_response");
				 //return response
				 String rawText = extractResponseContext(response);
				 String cleanJson = rawText.replaceAll("```json", "")
                         .replaceAll("```", "")
                         .trim();
				 Map<String, Object> jsonMap = new ObjectMapper().readValue(cleanJson, Map.class);

				 return jsonMap;
				 
	}
	private String extractResponseContext(String response) {
		// TODO Auto-generated method stub
		try {
			ObjectMapper mapper = new ObjectMapper();
			JsonNode rootNode = mapper.readTree(response);
			return rootNode.path("candidates")
					       .get(0)
					       .path("content")
					       .path("parts")
					       .get(0)
					       .path("text")
					       .asText();
		}catch(Exception e) {
			return "Error processing request: " + e.getMessage();
		}
	}
	String loadPromptFromFile(String filename) throws IOException {
	        Path path = new ClassPathResource(filename).getFile().toPath();
	        return Files.readString(path);
	    }
	 
	 String putValuesToTemplate( String template, Map<String,String> values) {
		 for(Map.Entry<String, String> entry : values.entrySet()) {
			 template = template.replace("{{" + entry.getKey() + "}}", entry.getValue());
		 } 
		 return template;
	 }
	
	

}
