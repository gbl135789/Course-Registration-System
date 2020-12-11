package edu.nyu.cs.gbl254.course_registration_system.gui_pages;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import edu.nyu.cs.gbl254.course_registration_system.custom_components.PositionedComponent;
import edu.nyu.cs.gbl254.course_registration_system.data_management.CourseRegistrationSystem;
import edu.nyu.cs.gbl254.course_registration_system.data_management.Student;

@SuppressWarnings("serial")
// represents a dashboard window with student features
public class StudentDashboard extends Page{
	
	// the current student
	Student currentStudent = (Student) CourseRegistrationSystem.getCurrentUser();
	
	// the student's first name
	private String firstName = CourseRegistrationSystem.getCurrentUser().getFirstName();
	
	// welcome label
	private PositionedComponent welcomeLabel = new PositionedComponent(new JLabel("Welcome, " + firstName), 0, 0, 1, 1, GridBagConstraints.CENTER, Font.PLAIN, 20);
	
	// button that allows the student to view all courses
	private PositionedComponent viewCoursesButton = new PositionedComponent(new JButton("Browse courses"), 0, 1, 1, 1, GridBagConstraints.CENTER);
	
	// button that allows the student to view enrolled courses
	private PositionedComponent myCoursesButton = new PositionedComponent(new JButton("My courses"), 0, 2, 1, 1, GridBagConstraints.CENTER);
	
	// button that exits the system
	private PositionedComponent exitButton = new PositionedComponent(new JButton("Exit"), 0, 3, 1, 1, GridBagConstraints.CENTER);
	
	// all components in the window
	private PositionedComponent[] components = {welcomeLabel, viewCoursesButton, myCoursesButton, exitButton};
	
	// constructor
	public StudentDashboard() {
		super("Dashboard", 250, 300);
		if(currentStudent.getIsFirstTime()) this.setUsernameAndPassword();
		this.setCurr(this);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.addComponents(components);
		this.addPanel();
		this.setLocation();
		this.setVisible(true);
		((JButton) this.viewCoursesButton.getComponent()).addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				currentStudent.viewAllCourses(getCurr());
				getCurr().setVisible(false);
			}
		});
		((JButton) this.myCoursesButton.getComponent()).addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				currentStudent.viewMyCourses(getCurr());
				getCurr().setVisible(false);
			}
		});
		((JButton) this.exitButton.getComponent()).addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CourseRegistrationSystem.exit();
			}
		});
	}
	
	// prompts the student to set a new username and password when logging in for the first time
	private void setUsernameAndPassword() {
		JTextField username = new JTextField(15);
		JPasswordField password = new JPasswordField(15);
		JPasswordField confirmPassword = new JPasswordField(15);
		Object[] fields = {"Username:", username, "Password:", password, "Confirm Password:", confirmPassword};
		boolean isSet = false;
		while(!isSet) {
			int option = JOptionPane.showConfirmDialog(null, fields, "Set Username and Password", JOptionPane.OK_CANCEL_OPTION);
			String uName = username.getText();
			String pWord = new String(password.getPassword());
			String cPWord = new String(confirmPassword.getPassword());
			if(option==JOptionPane.OK_OPTION) {
				if(!this.isUsernameAndPasswordValid(uName, pWord)) {
					this.displayErrorMessage("Username and password must be at least 5 characters and contain no spaces.");
				}
				else if(this.isDuplicate(uName)) {
					this.displayErrorMessage("This username is already taken");
				}
				else if(!pWord.equals(cPWord)) {
					this.displayErrorMessage("Password and confirmed password do not match.");
				}
				else {
					currentStudent.setUsernameAndPassword(uName, pWord);
					isSet = true;
				}
			}
			else {
				CourseRegistrationSystem.exit();
			}
		}
	}
}
