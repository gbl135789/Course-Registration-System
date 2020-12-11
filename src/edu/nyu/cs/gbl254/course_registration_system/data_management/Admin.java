package edu.nyu.cs.gbl254.course_registration_system.data_management;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;

import edu.nyu.cs.gbl254.course_registration_system.gui_pages.CoursesPage;
import edu.nyu.cs.gbl254.course_registration_system.gui_pages.Page;
import edu.nyu.cs.gbl254.course_registration_system.gui_pages.ViewStudentsPage;

// represents a single admin in the system
public class Admin extends User implements AdminInterface {
	
	// for serialization
	private static final long serialVersionUID = 3191777020671394726L;
	
	// allows the instantiation of an admin with a username, password, first name, and last name
	public Admin(String username, String password, String firstName, String lastName) {
		super(username, password, firstName, lastName);
	}
	
	// allows admins to create a new course
	public void createCourse(String name, String id, int capacity, String instructor, int sectionNumber, String location) {
		CourseRegistrationSystem.addCourse(new Course(name, id, capacity, instructor, sectionNumber, location));
	}
	
	// allows admins to delete an existing course
	public void deleteCourse(Course c, ArrayList<Course> allCourses, ArrayList<Course> shownCourses) {
		allCourses.remove(c);
		shownCourses.remove(c);
		CourseRegistrationSystem.removeCourse(c);
	}
	
	// allows admins to edit an existing course
	public void editCourse(Course c, int capacity, String instructor, int sectionNumber, String location) {
		c.edit(capacity, instructor, sectionNumber, location);
	}
	
	// allows admins to register a student
	public void registerStudent(Student student) {
		CourseRegistrationSystem.addStudent(student);
	}
	
	// allows admins to delete an existing student
	public void deleteStudent(ArrayList<Student> shownStudents, Student student) {
		if(shownStudents != null) shownStudents.remove(student);
		CourseRegistrationSystem.removeStudent(student);
	}
	
	// allows admins to view all courses from a specific subset of courses (example: viewing all of a specific student's courses)
	public void viewAllCourses(ArrayList<Course> allCourses, ArrayList<Course> shownCourses, Page prev, String title, boolean isEnrollOrUnenroll) {
		new CoursesPage(allCourses, shownCourses, true, prev, title, isEnrollOrUnenroll);
	}
	
	// allows admins to view full courses from a specific subset of courses
	public void viewFullCourses(ArrayList<Course> allCourses, ArrayList<Course> shownCourses, Page prev, String title) {
		new CoursesPage(allCourses, shownCourses, true, prev, title, false);
	}
	
	// allows admins to view the information of all students
	public void viewStudents(Page prev) {
		new ViewStudentsPage(CourseRegistrationSystem.getStudents(), true, prev, "Students", null);
	}
	
	// allows admins to view the information of all students in a specific course
	public void viewStudentsInCourse(Course course, Page prev) {
		new ViewStudentsPage(course.getStudents(), false, prev, course.getName(), course);
	}
	
	// allows admins to view the courses of a specific student
	public void viewStudentCourses(Student student, Page prev) {
		new CoursesPage(student.getCourses(), student.getCourses(), true, prev, student.getFirstName() + "'s Courses", false);
	}
	
	// allows admins to enroll a student in a course
	public void enrollStudentInCourse(Student student, Course course) {
		student.enroll(course);
	}
	
	// allows admins to remove a student from a course
	public void removeStudentFromCourse(Student student, Course course) {
		student.withdraw(course);
	}
	
	// allows admins to view all courses from a specific subset of courses, sorted by number of students enrolled
	public void viewCoursesByNumStudents(ArrayList<Course> allCourses, ArrayList<Course> shownCourses, Page prev, String title) {
		Collections.sort(shownCourses);
		new CoursesPage(allCourses, shownCourses, true, prev, title, false);
	}
	
	// allows admins to search a course by ID
	public ArrayList<Course> searchCourseById(ArrayList<Course> shownCourses, String courseIdToSearch) {
		ArrayList<Course> coursesWithSearchedId = new ArrayList<Course>();
		for(Course course:shownCourses) {
			if(course.getId().toLowerCase().equals(courseIdToSearch.toLowerCase().trim().replaceAll("\\s+", ""))) {
				coursesWithSearchedId.add(course);
			}
		}
		return coursesWithSearchedId;
	}
	
	// allows admins to write the currently displayed courses to a text file
	public void coursesToFile(ArrayList<Course> courses, String[] colTitles, String dataName, String fileName) throws IOException {
		try {
			File file = new File("course_data/" + fileName + ".txt");
			FileWriter writer;
			if(file.exists()) {
				
				// if a file of the same name exists, append to existing file
				writer = new FileWriter(file, true);
			}
			else {
				
				// if the file doesn't exist, create a new file
				writer = new FileWriter("course_data/" + fileName + ".txt");
			}
			int[] formatValues = getFormatValues(courses, colTitles);
			String dateTime = LocalDateTime.now().toString();
			writer.write("=======================================================================================================================" + "\r\n");
			writer.write(dataName + " (" + dateTime + ")\r\n\r\n");
			writeData(writer, courses, colTitles, formatValues);
		}
		catch(IOException ioe) {
			throw ioe;
		}
	}
	
	// helper method for file writing
	private void writeData(FileWriter writer, ArrayList<Course> courses, String[] colTitles, int[] formatValues) {
		String val1 = "%-"+Integer.toString(formatValues[0]+1)+"s ";
		String val2 = "%-"+Integer.toString(formatValues[1]+1)+"s ";
		String val3 = "%-"+Integer.toString(formatValues[2]+1)+"s ";
		String val4 = "%-"+Integer.toString(formatValues[3]+1)+"s ";
		String val5 = "%-"+Integer.toString(formatValues[4]+1)+"s ";
		String val6 = "%-"+Integer.toString(formatValues[5]+1)+"s ";
		String val7 = "%-"+Integer.toString(formatValues[6]+1)+"s \r\n";
		try {
		writer.write(String.format(val1+val2+val3+val4+val5+val6+val7, colTitles[0]+":", colTitles[1]+":", colTitles[2]+":", colTitles[3]+":", colTitles[4]+":", colTitles[5]+":", colTitles[6]+":"));
		writer.write("\r\n");
		for(Course course:courses) {
			writer.write(String.format(val1+val2+val3+val4+val5+val6+val7, course.getName(), course.getId(), Integer.toString(course.getMaxStudents()), 
					Integer.toString(course.getRegisteredStudents()), course.getInstructor(), Integer.toString(course.getSectionNumber()), course.getLocation()));
		}
		writer.write("=======================================================================================================================" + "\r\n\r\n");
		writer.close();
		}
		catch(IOException ioe) {
			System.out.println("There was an error writing to the file.");
		}
	}
	
	// helper method for getting the proper formatting values for file writing
	private int[] getFormatValues(ArrayList<Course> courses, String[] colTitles) {
		int[] formatValues = {colTitles[0].length(), colTitles[1].length(), colTitles[2].length(), colTitles[3].length(), colTitles[4].length(), colTitles[5].length(), colTitles[6].length()};
		for(Course course:courses) {
			if(course.getName().length() > formatValues[0]) formatValues[0] = course.getName().length();
			if(course.getId().length() > formatValues[1]) formatValues[1] = course.getId().length();
			if(String.valueOf(course.getMaxStudents()).length() > formatValues[2]) formatValues[2] = String.valueOf(course.getMaxStudents()).length();
			if(String.valueOf(course.getRegisteredStudents()).length() > formatValues[3]) formatValues[3] = String.valueOf(course.getRegisteredStudents()).length();
			if(course.getInstructor().length() > formatValues[4]) formatValues[4] = course.getInstructor().length();
			if(String.valueOf(course.getSectionNumber()).length() > formatValues[5]) formatValues[5] = String.valueOf(course.getSectionNumber()).length();
			if(course.getLocation().length() > formatValues[6]) formatValues[6] = course.getLocation().length();
		}
		return formatValues;
	}
}
