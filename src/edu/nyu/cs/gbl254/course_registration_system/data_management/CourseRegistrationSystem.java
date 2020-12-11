package edu.nyu.cs.gbl254.course_registration_system.data_management;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import edu.nyu.cs.gbl254.course_registration_system.gui_pages.LoginPage;

// represents the course registration system
public class CourseRegistrationSystem {
	
	// size of monitor
	public static final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	
	// default font to use
	public static final Font defaultFont = new JLabel().getFont();
	
	// all courses in the system
	private static ArrayList<Course> courses = new ArrayList<Course>();
	
	// all users in the system
	private static ArrayList<User> users = new ArrayList<User>();
	
	// keep track of the current user, will be able to reference it from any class
	private static User currentUser;
	
	// try to read the serialized data, read the CSV if unable, and add the admin account
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					
					// try to read the serialized file
					CourseRegistrationSystem.readSerialized();
				}
				catch(IOException | ClassNotFoundException e) {
					
					// read the CSV
					CourseRegistrationSystem.readCSV();
					CourseRegistrationSystem.addAdmin("Admin", "Admin001", "Admin", "Test");
				}
				
				// open the GUI
				new LoginPage();
			}
		});
	}
	
	// reads the CSV file
	public static void readCSV() {
		try (Scanner sc = new Scanner(new File("MyUniversityCourses.csv"))) {
			
			// skip header row
			sc.nextLine();
			while(sc.hasNextLine()) {
				String line = sc.nextLine();
				String[] courseData = line.split(",");
				CourseRegistrationSystem.addCourse(new Course(courseData));
			}
		}
		catch(FileNotFoundException fnfe) {
			System.out.println("File not found.");
		}
	}
	
	// adds an admin to the system
	public static void addAdmin(String username, String password, String firstName, String lastName) {
		CourseRegistrationSystem.users.add(new Admin(username, password, firstName, lastName));
		CourseRegistrationSystem.serialize();
	}
	
	@SuppressWarnings("unchecked")
	// reads the serialized data
	public static void readSerialized() throws IOException, ClassNotFoundException {
		try (FileInputStream fis = new FileInputStream("serialized.ser");
		ObjectInputStream ois = new ObjectInputStream(fis);) {

			CourseRegistrationSystem.courses = (ArrayList<Course>) ois.readObject();
			CourseRegistrationSystem.users = (ArrayList<User>) ois.readObject();
		}
		catch(IOException ioe) {
			throw ioe;
		}
		catch(ClassNotFoundException cnfe) {
			throw cnfe;
		}
	}
	
	// serializes the data
	public static void serialize() {
		try (FileOutputStream fos = new FileOutputStream("serialized.ser");
		ObjectOutputStream oos = new ObjectOutputStream(fos);) {

			oos.writeObject(CourseRegistrationSystem.courses);
			oos.writeObject(CourseRegistrationSystem.users);
		}
		catch(IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	// returns all courses in the system
	public static ArrayList<Course> getCourses() {
		return CourseRegistrationSystem.courses;
	}
	
	// returns all users in the system
	public static ArrayList<User> getUsers() {
		return CourseRegistrationSystem.users;
	}
	
	// returns the current user
	public static User getCurrentUser() {
		return CourseRegistrationSystem.currentUser;
	}
	
	// sets the current user
	public static void setCurrentUser(User user) {
		CourseRegistrationSystem.currentUser = user;
	}
	
	// adds a course to the system
	public static void addCourse(Course c) {
		CourseRegistrationSystem.courses.add(c);
		CourseRegistrationSystem.serialize();
	}
	
	// removes a course from the system
	public static void removeCourse(Course c) {
		for(Student s:c.getStudents()) {
			s.removeFromDeletedCourse(c);
		}
		CourseRegistrationSystem.courses.remove(c);
		CourseRegistrationSystem.serialize();
	}
	
	// adds a student to the system
	public static void addStudent(Student student) {
		CourseRegistrationSystem.users.add(student);
		CourseRegistrationSystem.serialize();
	}
	
	// removes a student from the system
	public static void removeStudent(Student student) {
		for(Course c:student.getCourses()) {
			c.removeStudent(student);
		}
		CourseRegistrationSystem.users.remove(student);
		CourseRegistrationSystem.serialize();
	}
	
	// returns all students in the system
	public static ArrayList<Student> getStudents() {
		ArrayList<Student> students = new ArrayList<Student>();
		for(User user:CourseRegistrationSystem.users) {
			if(user instanceof Student) students.add((Student) user);
		}
		return students;
	}
	
	// returns all courses that are full
	public static ArrayList<Course> getFullCourses() {
		ArrayList<Course> fullCourses = new ArrayList<Course>();
		for(Course course:CourseRegistrationSystem.courses) {
			if(course.isFull()) fullCourses.add(course);
		}
		return fullCourses;
	}
	
	// returns all courses that are full in a specific subset of courses
	public static ArrayList<Course> getFullCourses(ArrayList<Course> courses) {
		ArrayList<Course> fullCourses = new ArrayList<Course>();
		for(Course course:courses) {
			if(course.isFull()) fullCourses.add(course);
		}
		return fullCourses;
	}
	
	// returns all courses that are open
	public static ArrayList<Course> getOpenCourses() {
		ArrayList<Course> openCourses = new ArrayList<Course>();
		for(Course course:CourseRegistrationSystem.courses) {
			if(!course.isFull()) openCourses.add(course);
		}
		return openCourses;
	}
	
	// returns all open courses in a specific subset of courses
	public static ArrayList<Course> getOpenCourses(ArrayList<Course> courses) {
		ArrayList<Course> openCourses = new ArrayList<Course>();
		for(Course course:courses) {
			if(!course.isFull()) openCourses.add(course);
		}
		return openCourses;
	}
	
	// exits the system
	public static void exit() {
		System.exit(0);
	}
}
