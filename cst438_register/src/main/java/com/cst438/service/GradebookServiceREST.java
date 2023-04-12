package com.cst438.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

import com.cst438.domain.EnrollmentDTO;


public class GradebookServiceREST extends GradebookService {

	private RestTemplate restTemplate = new RestTemplate();

	@Value("${gradebook.url}")
	String gradebook_url;
	
	public GradebookServiceREST() {
		System.out.println("REST grade book service");
	}

	@Override
	public void enrollStudent(String sEmail, String sName, int courseId) {
		
		//TODO  complete this method in homework 4
		EnrollmentDTO enroll = new EnrollmentDTO();
		enroll.course_id = courseId;
		enroll.studentEmail = sEmail;
		enroll.studentName = sName;
		
		EnrollmentDTO res = restTemplate.postForObject(gradebook_url + "/enrollment", enroll, EnrollmentDTO.class);
		System.out.println("End of enrollStudent response: " + res);
	}

}
