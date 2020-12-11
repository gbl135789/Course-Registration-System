package edu.nyu.cs.gbl254.course_registration_system.gui_pages;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;

import edu.nyu.cs.gbl254.course_registration_system.custom_components.PositionedComponent;
import edu.nyu.cs.gbl254.course_registration_system.data_management.Admin;
import edu.nyu.cs.gbl254.course_registration_system.data_management.CourseRegistrationSystem;

@SuppressWarnings("serial")

// represents a dashboard window with admin features
public class AdminDashboard extends Page {
	
	// the admin currently using the system
	private Admin currentAdmin = (Admin) CourseRegistrationSystem.getCurrentUser();
	
	// the first name of the current admin
	private String firstName = CourseRegistrationSystem.getCurrentUser().getFirstName();
	
	// a welcome label
	private PositionedComponent welcomeLabel = new PositionedComponent(new JLabel("Welcome, " + firstName), 0, 0, 1, 1, GridBagConstraints.CENTER, Font.PLAIN, 20);
	
	// button to view all courses
	private PositionedComponent viewCoursesButton = new PositionedComponent(new JButton("View courses"), 0, 1, 1, 1, GridBagConstraints.CENTER);
	
	// button to create a new course
	private PositionedComponent createCourseButton = new PositionedComponent(new JButton("Create a course"), 0, 2, 1, 1, GridBagConstraints.CENTER);
	
	// button to view all students
	private PositionedComponent viewStudentsButton = new PositionedComponent(new JButton("View students"), 0, 3, 1, 1, GridBagConstraints.CENTER);
	
	// button to register a student
	private PositionedComponent registerButton = new PositionedComponent(new JButton("Register a student"), 0, 4, 1, 1, GridBagConstraints.CENTER);
	
	// button to exit the system
	private PositionedComponent exitButton = new PositionedComponent(new JButton("Exit"), 0, 5, 1, 1, GridBagConstraints.CENTER);
	
	// all components on the dashboard
	private PositionedComponent[] components = {welcomeLabel, viewCoursesButton, createCourseButton, viewStudentsButton, registerButton, exitButton};
	
	// constructor
	public AdminDashboard() {
		super("Dashboard", 250, 300);
		this.setCurr(this);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.addComponents(components);
		this.addPanel();
		this.setLocation();
		this.setVisible(true);
		new QuickLinksBar(this);
		((JButton) this.viewCoursesButton.getComponent()).addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				currentAdmin.viewAllCourses(getCurr());
				hideDashboard();
			}
		});
		((JButton) this.createCourseButton.getComponent()).addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new EditCoursePage(getCurr(), true, "Create A Course", null);
				hideDashboard();
			}
		});
		((JButton) this.viewStudentsButton.getComponent()).addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				currentAdmin.viewStudents(getCurr());
				hideDashboard();
			}
		});
		((JButton) this.registerButton.getComponent()).addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new RegisterPage(getCurr());
				hideDashboard();
			}
		});
		((JButton) this.exitButton.getComponent()).addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CourseRegistrationSystem.exit();
			}
		});
	}
	
	// hides the dashboard
	public void hideDashboard() {
		getCurr().setVisible(false);
	}
}
