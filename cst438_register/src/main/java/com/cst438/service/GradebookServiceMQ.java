package com.cst438.service;


import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import com.cst438.domain.CourseDTOG;
import com.cst438.domain.Enrollment;
import com.cst438.domain.EnrollmentDTO;
import com.cst438.domain.EnrollmentRepository;


public class GradebookServiceMQ extends GradebookService {
	
	@Autowired
	RabbitTemplate rabbitTemplate;
	
	@Autowired
	EnrollmentRepository enrollmentRepository;
	
	@Autowired
	Queue gradebookQueue;
	
	
	public GradebookServiceMQ() {
		System.out.println("MQ grade book service");
	}
	
	// send message to grade book service about new student enrollment in course
	@Override
	public void enrollStudent(String sEmail, String sName, int courseId) {
		 
		// create EnrollmentDTO and send to gradebookQueue
		EnrollmentDTO enrollObj = new EnrollmentDTO(sEmail, sName, courseId);
		rabbitTemplate.convertAndSend(gradebookQueue.getName(), enrollObj);
		System.out.println("Message send to gradbook service for student "+ sEmail +" " + courseId);  
		
	}
	
	@RabbitListener(queues = "registration-queue")
	public void receive(CourseDTOG courseDTOG) {
		System.out.println("Receive enrollment :" + courseDTOG);

		// for each student grade in courseDTOG,  find the student enrollment entity, update the grade and save back to enrollmentRepository.
		for (CourseDTOG.GradeDTO gradeObj : courseDTOG.grades) {
			Enrollment currEnroll = enrollmentRepository.findByEmailAndCourseId(gradeObj.student_email, courseDTOG.course_id);
			currEnroll.setCourseGrade(gradeObj.grade);
			enrollmentRepository.save(currEnroll);
		}
	}

}
