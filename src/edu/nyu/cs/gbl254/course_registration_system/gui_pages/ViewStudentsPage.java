package edu.nyu.cs.gbl254.course_registration_system.gui_pages;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import edu.nyu.cs.gbl254.course_registration_system.custom_components.PositionedComponent;
import edu.nyu.cs.gbl254.course_registration_system.data_management.Admin;
import edu.nyu.cs.gbl254.course_registration_system.data_management.Course;
import edu.nyu.cs.gbl254.course_registration_system.data_management.CourseRegistrationSystem;
import edu.nyu.cs.gbl254.course_registration_system.data_management.Student;

@SuppressWarnings("serial")
// represents the window admins see when viewing student indo
public class ViewStudentsPage extends Page {
	
	// the current admin
	private Admin currentAdmin = (Admin) CourseRegistrationSystem.getCurrentUser();
	
	// the students whose info is shown
	private ArrayList<Student> shownStudents;
	
	// whether this window is showing all students, or a specific subset of students, true if all students, false if not
	private boolean isAllStudents;
	
	// window name
	private String title;
	
	// header names for student info table
	private static final String[] colTitles = {"First Name", "Last Name", "Username"};
	
	// student info table
	private JTable studentTable;
	
	// scroll pane that the student table is in
	private PositionedComponent scrollPane;
	
	// button that allows the admin to delete a student
	private PositionedComponent deleteButton = new PositionedComponent(new JButton("Delete"), 2, 2, 1, 1, GridBagConstraints.WEST);
	
	// button that allows the admin to go back to the previous window
	private PositionedComponent backButton = new PositionedComponent(new JButton("Back"), 2, 2, 1, 1, GridBagConstraints.EAST);
	
	// only when viewing all students:
	// button that allows the admin to view a student's enrolled courses
	private PositionedComponent viewCoursesButton;
	
	// allows the admin to enroll a student in a course
	private PositionedComponent enrollInCourseButton;
	
	// allows the admin to unenroll a student from a course
	private PositionedComponent unenrollFromCourseButton;
	
	// only when viewing students from a specific course:
	// button that allows the admin to remove a student from a specific course
	private PositionedComponent removeFromCourseButton;
	
	// the course which the admin is viewing the students from
	private Course course;
	
	// allows instantiation of a view students window with a list of students, boolean is all students, a reference to the previous window, a window name, and a reference to a course
	public ViewStudentsPage(ArrayList<Student> students, boolean isAllStudents, Page prev, String title, Course course) {
		super(title, 400, 300);
		QuickLinksBar.addPageToDispose(this);
		this.setCurr(this);
		this.setPrev(prev);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.shownStudents = students;
		this.isAllStudents = isAllStudents;
		this.title = title;
		this.course = course;
		this.studentTable = new JTable(getStudentData(students), colTitles);
		this.studentTable.setDefaultEditor(Object.class, null);
		this.studentTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		this.setColumnWidths(studentTable, 375);
		this.scrollPane = new PositionedComponent(new JScrollPane(studentTable), 0, 1, 3, 1, GridBagConstraints.CENTER, new Dimension(375, 250));
		this.setScrollSpeed((JScrollPane) this.scrollPane.getComponent(), 5, 5);
		if(isAllStudents) this.addAllStudentsFeatures();
		else this.addCourseStudentsFeatures();
		this.addPanel();
		this.pack();
		this.setLocation();
		this.setVisible(true);
		((JButton) this.deleteButton.getComponent()).addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Student toDelete = searchStudent(shownStudents, studentTable);
					if(toDelete != null) {
						currentAdmin.deleteStudent(shownStudents, toDelete);
						displayMessage("Student deleted.");
						refresh();
					}
					else {
						displayErrorMessage("Please select a student.");
					}
				}
				catch(ArrayIndexOutOfBoundsException a) {
					displayErrorMessage("Please select a student.");
				}
			}
		});
		((JButton) this.backButton.getComponent()).addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				close();
				dispose();
			}
		});
		this.addWindowListener(new WindowAdapter() {
		    public void windowClosing(WindowEvent windowEvent) {
		    	close();
		    }
		});
	}
	
	// adds features for the all students page
	private void addAllStudentsFeatures() {
		viewCoursesButton = new PositionedComponent(new JButton("View student's courses"), 0, 2, 1, 1, GridBagConstraints.WEST);
		enrollInCourseButton = new PositionedComponent(new JButton("Enroll in course"), 0, 0, 2, 1, GridBagConstraints.WEST);
		unenrollFromCourseButton = new PositionedComponent(new JButton("Unenroll from course"), 1, 0, 2, 1, GridBagConstraints.WEST);
		PositionedComponent[] components = {scrollPane, deleteButton, backButton, viewCoursesButton, enrollInCourseButton, unenrollFromCourseButton};
		this.addViewCoursesListener();
		this.addEnrollListener();
		this.addUnenrollListener();
		this.addComponents(components);
	}
	
	// adds features for a course's students page
	private void addCourseStudentsFeatures() {
		removeFromCourseButton = new PositionedComponent(new JButton("Remove from course"), 0, 2, 1, 1, GridBagConstraints.WEST);
		PositionedComponent[] components = {scrollPane, deleteButton, backButton, removeFromCourseButton};
		this.addRemoveListener();
		this.addComponents(components);
	}
	
	// allows the view courses button to show the selected student's courses
	private void addViewCoursesListener() {
		((JButton) this.viewCoursesButton.getComponent()).addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Student student = searchStudent(shownStudents, studentTable);
					if(student != null) {
						currentAdmin.viewStudentCourses(student, getCurr());
						setVisible(false);
					}
					else {
						displayErrorMessage("Please select a student.");
					}
				}
				catch(ArrayIndexOutOfBoundsException a) {
					displayErrorMessage("Please select a student.");
				}
			}
		});
	}
	
	// allows the enroll in course button to open a list of courses to choose from
	private void addEnrollListener() {
		((JButton) this.enrollInCourseButton.getComponent()).addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Student toEnroll = searchStudent(shownStudents, studentTable);
					if(toEnroll != null) {
						new EnrollOrUnenrollPage(toEnroll.getCoursesNotEnrolledIn(), toEnroll, getCurr(), true);
						setVisible(false);
					}
					else{
						displayErrorMessage("Please select a student.");
					}
				}
				catch(ArrayIndexOutOfBoundsException a) {
					displayErrorMessage("Please select a student.");
				}
			}
		});
	}
	
	// allows the unenroll from course button to open a list of courses to choose from
	private void addUnenrollListener() {
		((JButton) this.unenrollFromCourseButton.getComponent()).addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Student toUnenroll = searchStudent(shownStudents, studentTable);
					if(toUnenroll != null) {
						new EnrollOrUnenrollPage(toUnenroll.getCourses(), toUnenroll, getCurr(), false);
						setVisible(false);
					}
					else{
						displayErrorMessage("Please select a student.");
					}
				}
				catch(ArrayIndexOutOfBoundsException a) {
					displayErrorMessage("Please select a student.");
				}
			}
		});
	}
	
	// allows the remove button to remove a selected student from a course
	private  void addRemoveListener() {
		((JButton) this.removeFromCourseButton.getComponent()).addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Student student = searchStudent(shownStudents, studentTable);
					if(student != null) {
						currentAdmin.removeStudentFromCourse(student, course);
						displayMessage("Student removed from course.");
						refresh();
					}
					else {
						displayErrorMessage("Please select a student.");
					}
				}
				catch(ArrayIndexOutOfBoundsException a) {
					displayErrorMessage("Please select a student.");
				}
			}
		});
	}
	
	// closes the window
	private void close() {
		if(getPrev() instanceof CoursesPage) {
			((CoursesPage) getPrev()).refresh();
		}
		else {
			getPrev().setVisible(true);
			QuickLinksBar.removePageToDispose(this);
		}
	}
	
	// refreshes the window
	private void refresh() {
		new ViewStudentsPage(this.shownStudents, this.isAllStudents, getPrev(), this.title, this.course);
		QuickLinksBar.removePageToDispose(this);
		this.dispose();
	}
}
