package edu.nyu.cs.gbl254.course_registration_system.data_management;

import java.io.IOException;
import java.util.ArrayList;

import edu.nyu.cs.gbl254.course_registration_system.gui_pages.Page;

// an interface containing methods that the Admin class must implement, comments on each method can be found in Admin
public interface AdminInterface {
	
	public abstract void createCourse(String name, String id, int capacity, String instructor, int sectionNumber, String location);
	
	public abstract void deleteCourse(Course c, ArrayList<Course> allCourses, ArrayList<Course> shownCourses);
	
	public abstract void editCourse(Course c, int capacity, String instructor, int sectionNumber, String location);
	
	public abstract void registerStudent(Student student);
	
	public abstract void deleteStudent(ArrayList<Student> shownStudents, Student student);
	
	public abstract void viewAllCourses(ArrayList<Course> allCourses, ArrayList<Course> shownCourses, Page prev, String title, boolean isEnrollOrUnenroll);
	
	public abstract void viewFullCourses(ArrayList<Course> allCourses, ArrayList<Course> shownCourses, Page prev, String title);
	
	public abstract void viewStudents(Page prev);
	
	public abstract void viewStudentsInCourse(Course course, Page prev);
	
	public abstract void viewStudentCourses(Student student, Page prev);
	
	public abstract void enrollStudentInCourse(Student student, Course course);
	
	public abstract void removeStudentFromCourse(Student student, Course course);
	
	public abstract void viewCoursesByNumStudents(ArrayList<Course> allCourses, ArrayList<Course> shownCourses, Page prev, String title);
	
	public abstract ArrayList<Course> searchCourseById(ArrayList<Course> shownCourses, String courseIdToSearch);
	
	public abstract void coursesToFile(ArrayList<Course> courses, String[] colTitles, String dataName, String fileName) throws IOException;
}
