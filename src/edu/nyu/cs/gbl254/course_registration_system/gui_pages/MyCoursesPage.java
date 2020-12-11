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
import edu.nyu.cs.gbl254.course_registration_system.data_management.Course;
import edu.nyu.cs.gbl254.course_registration_system.data_management.CourseRegistrationSystem;
import edu.nyu.cs.gbl254.course_registration_system.data_management.Student;

@SuppressWarnings("serial")
// represents the windows students see when viewing their own courses
public class MyCoursesPage extends Page {
	
	// the current student
	private Student currentStudent = (Student) CourseRegistrationSystem.getCurrentUser();
	
	// the student's courses
	private ArrayList<Course> myCourses = currentStudent.getCourses();
	
	// header names for the course table
	private static final String[] colTitles = {"Name", "ID", "Capacity", "Enrolled", "Instructor", "Section", "Location"};
	
	// the table with course information
	private JTable courseTable = new JTable(getCourseData(myCourses), colTitles);
	
	// the scroll pane that the course table is in
	private PositionedComponent scrollPane = new PositionedComponent(new JScrollPane(courseTable), 0, 0, 6, 1, GridBagConstraints.CENTER, new Dimension(700, 400));
	
	// button that allows the student to withdraw from a course
	private PositionedComponent withdrawButton = new PositionedComponent(new JButton("Withdraw"), 0, 1, 1, 1, GridBagConstraints.WEST);
	
	// button that allows the student to return to the previous window
	private PositionedComponent backButton = new PositionedComponent(new JButton("Back"), 1, 1, 1, 1, GridBagConstraints.WEST);
	
	// all components in the window
	private PositionedComponent[] components = {scrollPane, withdrawButton, backButton};
	
	// allows instantiation of student courses window with a reference to the previous window
	public MyCoursesPage(Page prev) {
		super("My Courses", 1000, 750);
		this.setPrev(prev);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.courseTable.setDefaultEditor(Object.class, null);
		this.courseTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		this.setColumnWidths(this.courseTable, 700);
		this.setScrollSpeed((JScrollPane) this.scrollPane.getComponent(), 5, 5);
		this.addComponents(components);
		this.addPanel();
		this.pack();
		this.setLocation();
		this.setVisible(true);
		((JButton) this.withdrawButton.getComponent()).addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Course toWithdraw = searchCourse(myCourses, courseTable);
					if(toWithdraw == null) {
						displayErrorMessage("Course not found.");
					}
					else {
						currentStudent.withdraw(toWithdraw);
						displayMessage("You have withdrawn from this course.");
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
		((JButton) this.backButton.getComponent()).addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				prev.setVisible(true);
				dispose();
			}
		});
		this.addWindowListener(new WindowAdapter() {
		    public void windowClosing(WindowEvent windowEvent) {
		        prev.setVisible(true);
		    }
		});
	}
	
	// refreshes the window
	private void refresh() {
		new MyCoursesPage(getPrev());
		this.dispose();
	}
}
