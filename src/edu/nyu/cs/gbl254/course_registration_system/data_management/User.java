package edu.nyu.cs.gbl254.course_registration_system.data_management;

import java.io.Serializable;

import edu.nyu.cs.gbl254.course_registration_system.gui_pages.CoursesPage;
import edu.nyu.cs.gbl254.course_registration_system.gui_pages.Page;

// represents a single user in the system
public abstract class User implements Serializable {
	
	// for serialization
	private static final long serialVersionUID = -7649768850551271654L;
	
	// the user's username
	private String username;
	
	// the user's password
	private String password;
	
	// the user's first name
	private String firstName;
	
	// the user's last name
	private String lastName;
	
	// allows instantiation of a user with a username, password, first name, last name, and account status
	public User(String username, String password, String firstName, String lastName) {
		this.username = username;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
	}
	
	// logs the user into the system
	public void login() {
		CourseRegistrationSystem.setCurrentUser(this);
	}
	
	// allows the user to view all courses
	public void viewAllCourses(Page prev) {
		new CoursesPage(CourseRegistrationSystem.getCourses(), CourseRegistrationSystem.getCourses(), true, prev, "Courses", false);
	}
	
	// returns the user's username
	public String getUsername() {
		return this.username;
	}

	// sets the user's username
	public void setUsername(String username) {
		this.username = username;
	}
	
	// returns the user's password
	public String getPassword() {
		return this.password;
	}
	
	// sets the user's password
	public void setPassword(String password) {
		this.password = password;
	}
	
	// returns the user's first name
	public String getFirstName() {
		return this.firstName;
	}
	
	// returns the user's last name
	public String getLastName() {
		return this.lastName;
	}
}
