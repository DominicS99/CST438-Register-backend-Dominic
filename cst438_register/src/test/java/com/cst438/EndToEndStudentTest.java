package com.cst438;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.safari.SafariDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.cst438.domain.Course;
import com.cst438.domain.CourseRepository;
import com.cst438.domain.Enrollment;
import com.cst438.domain.EnrollmentRepository;
import com.cst438.domain.Student;
import com.cst438.domain.StudentRepository;

@SpringBootTest
public class EndToEndStudentTest {

	public static final String URL = "http://localhost:3000";

	public static final String TEST_USER_EMAIL = "test@csumb.edu";

	public static final int SLEEP_DURATION = 1000; // 1 second.
	
	public static final String TEST_USER_NAME = "Dominic";

	/*
	 * When running in @SpringBootTest environment, database repositories can be used
	 * with the actual database.
	 */
	
	@Autowired
	EnrollmentRepository enrollmentRepository;

	@Autowired
	CourseRepository courseRepository;
	
	@Autowired
	StudentRepository sRepo;

	/*
	 * As an Administrator, I can add a new student to the registration service
	 */
	
	@Test
	public void addStudentTest() throws Exception {

		/*
		 * Cleaning database at the beginning, to reduce bug possibilities
		 */
		
		Student s = null;
		do {
			s = sRepo.findByEmail(TEST_USER_EMAIL);
			if (s != null) sRepo.delete(s);
		} while (s != null);

		WebDriver driver = new SafariDriver();
		// Puts an Implicit wait for 10 seconds before throwing exception
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		try {
			driver.get(URL);
			Thread.sleep(SLEEP_DURATION);

			// Locate and click "Add Student" button
			driver.findElement(By.xpath("//button[@id='addAccountBtn']")).click();
			Thread.sleep(SLEEP_DURATION);

			// Locate the information fields and fill them in. Then click the "Add" button
			driver.findElement(By.xpath("//input[@name='accountName']")).sendKeys(TEST_USER_NAME);
			driver.findElement(By.xpath("//input[@name='accountEmail']")).sendKeys(TEST_USER_EMAIL);
			driver.findElement(By.xpath("//button[@id='Add']")).click();
			Thread.sleep(SLEEP_DURATION);

			/*
			* Verify that the student is now in the repository
			*/ 
		
			Student curr = sRepo.findByEmail(TEST_USER_EMAIL);
			assertTrue(curr != null, "Student added but not listed in repository.");

		} catch (Exception ex) {
			throw ex;
		} finally {
			// clean up database.
			Student curr = sRepo.findByEmail(TEST_USER_EMAIL);
			if (curr != null) sRepo.delete(curr);
			driver.quit();
		}

	}
}
