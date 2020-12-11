package edu.nyu.cs.gbl254.course_registration_system.data_management;

import java.util.ArrayList;

import edu.nyu.cs.gbl254.course_registration_system.gui_pages.CoursesPage;
import edu.nyu.cs.gbl254.course_registration_system.gui_pages.MyCoursesPage;
import edu.nyu.cs.gbl254.course_registration_system.gui_pages.Page;

// represents a single student in the system
public class Student extends User implements StudentInterface {
	
	// for serialization
	private static final long serialVersionUID = -7013467881342812461L;
	
	// whether the student logged in for the first time
	private boolean isFirstTime = true;
	
	// the student's courses
	ArrayList<Course> enrolledCourses = new ArrayList<Course>();
	
	// allows instantiation of a student with a username, password, first name, and last name
	public Student(String username, String password, String firstName, String lastName) {
		super(username, password, firstName, lastName);
	}
	
	@Override
	// allows the student to view all courses he/she is not enrolled in, overrides parent class method
	public void viewAllCourses(Page prev) {
		new CoursesPage(this.getCoursesNotEnrolledIn(), this.getCoursesNotEnrolledIn(), false, prev, "Courses", false);
	}
	
	// allows the student to view courses that are not full
	public void viewOpenCourses(Page prev) {
		new CoursesPage(this.getCoursesNotEnrolledIn(), CourseRegistrationSystem.getOpenCourses(this.getCoursesNotEnrolledIn()), false, prev, "Courses", false);
	}
	
	// allows the student to view enrolled courses
	public void viewMyCourses(Page prev) {
		new MyCoursesPage(prev);
	}
	
	// enrolls the student in a course
	public void enroll(Course course) {
		this.enrolledCourses.add(course);
		course.addStudent(this);
		CourseRegistrationSystem.serialize();
	}
	
	// withdraws the student from a course
	public void withdraw(Course course) {
		this.enrolledCourses.remove(course);
		course.removeStudent(this);
		CourseRegistrationSystem.serialize();
	}
	
	// sets the student's username and password when logging in for the first time
	public void setUsernameAndPassword(String username, String password) {
		this.setUsername(username);
		this.setPassword(password);
		this.setIsFirstTime(false);
		CourseRegistrationSystem.serialize();
	}
	
	// removes the student from a course that is being deleted
	public void removeFromDeletedCourse(Course course) {
		this.enrolledCourses.remove(course);
	}
	
	// returns the student's courses
	public ArrayList<Course> getCourses() {
		return this.enrolledCourses;
	}
	
	// returns whether the student logged in the first time
	public boolean getIsFirstTime() {
		return this.isFirstTime;
	}
	
	// sets whether the student logged in the first time
	public void setIsFirstTime(boolean isFirstTime) {
		this.isFirstTime = isFirstTime;
	}
	
	// returns the courses the student is not enrolled in
	public ArrayList<Course> getCoursesNotEnrolledIn() {
		ArrayList<Course> notEnrolled = new ArrayList<Course>();
		for(Course course:CourseRegistrationSystem.getCourses()) {
			if(!this.enrolledCourses.contains(course)) notEnrolled.add(course);
		}
		return notEnrolled;
	}
}
