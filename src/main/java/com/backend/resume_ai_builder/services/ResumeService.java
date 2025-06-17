package com.backend.resume_ai_builder.services;

import java.io.IOException;
import java.util.Map;

public interface ResumeService {

	 Map<String, Object> generateResumeResponse(String userResumeDescription) throws IOException;
//	 String generateResumeResponse(String userResumeDescription) throws IOException;

}
