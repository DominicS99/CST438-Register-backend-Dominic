package com.cst438.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cst438.domain.Student;
import com.cst438.domain.StudentRepository;

@RestController
public class StudentController {
	@Autowired
	StudentRepository studentRepo;

	@PostMapping("/student/createNewStudent")
	public void createNewStudent(String email, String name) throws Exception {
		Student student = studentRepo.findByEmail(email).orElse(null);
		if (student != null) {
			throw new Exception("Error: Student email is already in use " + email);
		}

		student.setName(name);
		student.setEmail(email);
		studentRepo.save(student);
	}
	
	@PostMapping("/student/enableHold")
	public void changeHold(String email) throws Exception {
		Student student = studentRepo.findByEmail(email).orElse(null);
		if (student == null) {
			throw new Exception("Error: Student needs to register this email: " + email);
		}
		
		if (student.getStatusCode() == 0) {
			student.setStatus("Enable Hold");
			student.setStatusCode(1);
		} else {
			student.setStatus("Disable Hold");
			student.setStatusCode(0);
		}
	}
}
