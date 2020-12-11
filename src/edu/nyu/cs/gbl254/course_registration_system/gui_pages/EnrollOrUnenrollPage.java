package edu.nyu.cs.gbl254.course_registration_system.gui_pages;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JOptionPane;

import edu.nyu.cs.gbl254.course_registration_system.custom_components.PositionedComponent;
import edu.nyu.cs.gbl254.course_registration_system.data_management.Admin;
import edu.nyu.cs.gbl254.course_registration_system.data_management.Course;
import edu.nyu.cs.gbl254.course_registration_system.data_management.CourseRegistrationSystem;
import edu.nyu.cs.gbl254.course_registration_system.data_management.Student;

@SuppressWarnings("serial")
// represents the window admins see when choosing a course to enroll a student in
public class EnrollOrUnenrollPage extends CoursesPage {
	
	// the current admin
	private Admin currentAdmin = (Admin) CourseRegistrationSystem.getCurrentUser();
	
	// the student to enroll or unenroll in a course
	private Student student;
	
	// the courses to choose from, specifically, the courses which the student has not yet enrolled in or the ones which the student has enrolled in
	private ArrayList<Course> courses;
	
	// allows instantiation of an enrollment page with a reference to a student, and a reference to the previous window
	public EnrollOrUnenrollPage(ArrayList<Course> courses, Student student, Page prev, boolean isEnroll) {
		super(courses, courses, true, prev, "Choose A Course", isEnroll);
		this.student = student;
		this.courses = courses;
	}
	
	@Override
	// adds the back button, overrides parent class method
	public void addFeatures() {
		((JButton) this.getBackButton().getComponent()).addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getPrev().setVisible(true);
				dispose();
			}
		});
	}
	
	@Override
	// adds the enrollment or unenrollment button, overrides parent class method
	public void addAdminFeatures() {
		if(this.getEnrollOrUnenroll()) this.addEnrollFeatures(); 
		else this.addUnenrollFeatures();
	}
	
	// adds the features necessary to support enrollment of a student
	private void addEnrollFeatures() {
		PositionedComponent enrollButton = new PositionedComponent(new JButton("Enroll"), 0, 0, 1, 1, GridBagConstraints.WEST);
		PositionedComponent searchByIdButton = new PositionedComponent(new JButton("Search by ID"), 5, 0, 1, 1, GridBagConstraints.WEST);
		((JButton) enrollButton.getComponent()).addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Course courseToEnroll = searchCourse(courses, getTable());
					if(courseToEnroll == null) {
						displayErrorMessage("Please select a course.");
					}
					else if(student.getCourses().contains(courseToEnroll)) {
						displayErrorMessage("Student is already enrolled in this course.");
					}
					
					else if(courseToEnroll.isFull()) {
						displayErrorMessage("Course is full.");
					}
					else {
						currentAdmin.enrollStudentInCourse(student, courseToEnroll);
						displayMessage("Student enrolled in course.");
						refresh();
					}
				}
				catch(ArrayIndexOutOfBoundsException a) {
					displayErrorMessage("Please select a course.");
				}
				catch(NumberFormatException b) {
					displayErrorMessage("Please select a course.");
				}
			}
		});
		PositionedComponent[] components = {this.getScrollPane(), this.getBackButton(), searchByIdButton, enrollButton};
		this.addComponents(components);
		((JButton) searchByIdButton.getComponent()).addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				searchCoursesById();
			}
		});
	}
	
	// adds the features necessary to support unenrollment of a student
	private void addUnenrollFeatures() {
		PositionedComponent unenrollButton = new PositionedComponent(new JButton("Unenroll"), 0, 0, 1, 1, GridBagConstraints.WEST);
		PositionedComponent searchByIdButton = new PositionedComponent(new JButton("Search by ID"), 5, 0, 1, 1, GridBagConstraints.WEST);
		((JButton) unenrollButton.getComponent()).addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Course courseToUnenroll = searchCourse(courses, getTable());
					if(courseToUnenroll == null) {
						displayErrorMessage("Please select a course.");
					}
					else if(!courseToUnenroll.getStudents().contains(student)) {
						displayErrorMessage("Student not in course");
					}
					else {
						currentAdmin.removeStudentFromCourse(student, courseToUnenroll);
						displayMessage("Student unenrolled from course.");
						refresh();
					}
				}
				catch(ArrayIndexOutOfBoundsException a) {
					displayErrorMessage("Please select a course.");
				}
				catch(NumberFormatException b) {
					displayErrorMessage("Please select a course.");
				}
			}
		});
		PositionedComponent[] components = {this.getScrollPane(), this.getBackButton(), searchByIdButton, unenrollButton};
		this.addComponents(components);
		((JButton) searchByIdButton.getComponent()).addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				searchCoursesById();
			}
		});
	}
	
	@Override
	// searches courses by ID
	public void searchCoursesById() {
		String courseIdToSearch = JOptionPane.showInputDialog(null, "Enter course ID:", "Search By Course ID", JOptionPane.OK_CANCEL_OPTION);
		ArrayList<Course> coursesWithSearchedId = this.currentAdmin.searchCourseById(this.courses, courseIdToSearch);
		new EnrollOrUnenrollPage(coursesWithSearchedId, this.student, this.getPrev(), this.getEnrollOrUnenroll());
		this.dispose();
	}
		
	@Override
	// refreshes the page, overrides parent class method
	public void refresh() {
		if(this.getEnrollOrUnenroll()) new EnrollOrUnenrollPage(this.student.getCoursesNotEnrolledIn(), this.student, this.getPrev(), this.getEnrollOrUnenroll());
		else new EnrollOrUnenrollPage(this.student.getCourses(), this.student, this.getPrev(), this.getEnrollOrUnenroll());
		QuickLinksBar.removePageToDispose(this);
		this.dispose();
	}
}
