package edu.nyu.cs.gbl254.course_registration_system.gui_pages;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import edu.nyu.cs.gbl254.course_registration_system.custom_components.PositionedComponent;
import edu.nyu.cs.gbl254.course_registration_system.data_management.Admin;
import edu.nyu.cs.gbl254.course_registration_system.data_management.CourseRegistrationSystem;
import edu.nyu.cs.gbl254.course_registration_system.data_management.Student;
import edu.nyu.cs.gbl254.course_registration_system.data_management.User;

@SuppressWarnings("serial")
// represents the window users see when entering login information
public class LoginPage extends Page {
	
	// label reminding users to login
	private PositionedComponent title = new PositionedComponent(new JLabel("Please login to continue."), 1, 0, 2, 1, GridBagConstraints.CENTER, Font.BOLD, 14);
	
	// label for username field
	private PositionedComponent usernameLabel = new PositionedComponent(new JLabel("Username:"), 0, 1, 1, 1, GridBagConstraints.EAST);
	
	// label for password field
	private PositionedComponent passwordLabel = new PositionedComponent(new JLabel("Password:"), 0, 2, 1, 1, GridBagConstraints.EAST);
	
	// username field
	private PositionedComponent usernameField = new PositionedComponent(new JTextField(15), 1, 1, 2, 1, GridBagConstraints.EAST);
	
	// password field
	private PositionedComponent passwordField = new PositionedComponent(new JPasswordField(15), 1, 2, 2, 1, GridBagConstraints.EAST);
	
	// button that logs users in
	private PositionedComponent loginButton = new PositionedComponent(new JButton("Login"), 2, 3, 1, 1, GridBagConstraints.EAST);
	
	// button that exits the program
	private PositionedComponent exitButton = new PositionedComponent(new JButton("Exit"), 1, 3, 1, 1, GridBagConstraints.EAST);
	
	// all components in the window
	private PositionedComponent[] components = {title, usernameLabel, passwordLabel, usernameField, passwordField, loginButton, exitButton};
	
	// constructor
	public LoginPage() {
		super("Course Registration System", 300, 200);
		this.setCurr(this);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.addComponents(components);
		this.addPanel();
		this.setLocation();
		this.setVisible(true);
		((JButton) this.loginButton.getComponent()).addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String username = ((JTextField) usernameField.getComponent()).getText();
				String password = new String(((JPasswordField) passwordField.getComponent()).getPassword());
				if(!isLoggedIn(username, password)) {
					displayErrorMessage("Your username or password is invalid");
				}
				else if(isLoggedIn(username, password) && CourseRegistrationSystem.getCurrentUser() instanceof Admin) {
					new AdminDashboard();
					dispose();
				}
				else if(isLoggedIn(username, password) && CourseRegistrationSystem.getCurrentUser() instanceof Student) {
					new StudentDashboard();
					dispose();
				}
			}
		});
		((JButton) this.exitButton.getComponent()).addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CourseRegistrationSystem.exit();
			}
		});
	}
	
	// checks if user login info is valid, returns true if it is, false if not
	private boolean isLoggedIn(String username, String password) {
		ArrayList<User> users = CourseRegistrationSystem.getUsers();
		for(User user:users) {
			String uName = user.getUsername();
			String pWord = user.getPassword();
			if(username.equals(uName) && password.equals(pWord)) {
				user.login();
				return true;
			}
		}
		return false;
	}
}
