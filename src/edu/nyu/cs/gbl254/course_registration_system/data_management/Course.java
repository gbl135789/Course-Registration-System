package edu.nyu.cs.gbl254.course_registration_system.data_management;

import java.io.Serializable;
import java.util.ArrayList;

// represents a single course in the system
public class Course implements Comparable<Course>, Serializable {
	
	// for serialization
	private static final long serialVersionUID = 8074228300422315003L;
	
	// course name
	private String name;
	
	// course ID
	private String id;
	
	// course capacity
	private int maxStudents;
	
	// number of students enrolled in course
	private int registeredStudents;
	
	// all students enrolled in course
	private ArrayList<Student> students;
	
	// course instructor
	private String instructor;
	
	// course section number
	private int sectionNumber;
	
	// course location
	private String location;
	
	// allows the instantiation of a course with a string array of course data, used when reading from the CSV
	public Course(String[] courseData) {
		this(courseData[0], courseData[1], Integer.parseInt(courseData[2]), Integer.parseInt(courseData[3]), new ArrayList<Student>(), courseData[5], Integer.parseInt(courseData[6]), courseData[7]);
	}
	
	// allows the instantiation of a course with a name, id, capacity, instructor, section number, and location, used when an admin creates a new course
	public Course(String name, String id, int maxStudents, String instructor, int sectionNumber, String location) {
		this(name, id, maxStudents, 0, new ArrayList<Student>(), instructor, sectionNumber, location);
	}
	
	// constructor that sets each field of the course, called by the 2 other overloaded constructors
	public Course(String name, String id, int maxStudents, int registeredStudents, ArrayList<Student> students, String instructor, int sectionNumber, String location) {
		this.name = name;
		this.id = id;
		this.maxStudents = maxStudents;
		this.registeredStudents = registeredStudents;
		this.students = students;
		this.instructor = instructor;
		this.sectionNumber = sectionNumber;
		this.location = location;
	}
	
	// compares this course to another course in terms of enrolled students number
	public int compareTo(Course other) {
		
		// return positive int if other course has more students
		return other.getRegisteredStudents() - this.getRegisteredStudents();
	}
	
	// adds a student to the course
	public void addStudent(Student student) {
		if(this.registeredStudents < this.maxStudents) {
			this.students.add(student);
			this.registeredStudents++;
		}
		else {
			System.out.println("Course is full!");
		}
	}
	
	// removes a student from the course
	public void removeStudent(Student student) {
		try {
			this.students.remove(student);
			this.registeredStudents--;
		}
		catch(NullPointerException npe) {
			System.out.println("Student not in course");
		}
	}
	
	// edits the capacity, instructor, section number, and location fields
	public void edit(int capacity, String instructor, int sectionNumber, String location) {
		this.maxStudents = capacity;
		this.instructor = instructor;
		this.sectionNumber = sectionNumber;
		this.location = location;
		CourseRegistrationSystem.serialize();
	}
	
	// returns the course name
	public String getName() {
		return this.name;
	}

	// returns the course id
	public String getId() {
		return this.id;
	}
	
	// returns the course capacity
	public int getMaxStudents() {
		return this.maxStudents;
	}
	
	// returns the number of enrolled students
	public int getRegisteredStudents() {
		return this.registeredStudents;
	}
	
	// returns the list of students
	public ArrayList<Student> getStudents() {
		return this.students;
	}
	
	// returns the course instructor
	public String getInstructor() {
		return this.instructor;
	}
	
	// returns the course section number
	public int getSectionNumber() {
		return this.sectionNumber;
	}
	
	// returns the course location
	public String getLocation() {
		return this.location;
	}
	
	// returns whether the course is full or not
	public boolean isFull() {
		return this.registeredStudents >= this.maxStudents;
	}
}
