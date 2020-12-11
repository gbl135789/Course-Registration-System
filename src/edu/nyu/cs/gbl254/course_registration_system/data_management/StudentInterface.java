package edu.nyu.cs.gbl254.course_registration_system.data_management;

import java.util.ArrayList;

import edu.nyu.cs.gbl254.course_registration_system.gui_pages.Page;

// an interface containing methods that must be implemented by the Student class, comments on each method can be found in Student
public interface StudentInterface {
	
	public abstract void viewOpenCourses(Page prev);
	
	public abstract void viewMyCourses(Page prev);
	
	public abstract void enroll(Course course);
	
	public abstract void withdraw(Course course);
	
	public abstract void setUsernameAndPassword(String username, String password);
	
	public abstract void removeFromDeletedCourse(Course course);
	
	public abstract ArrayList<Course> getCourses();
	
	public abstract ArrayList<Course> getCoursesNotEnrolledIn();
}
