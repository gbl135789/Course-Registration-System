package edu.nyu.cs.gbl254.course_registration_system.gui_pages;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import edu.nyu.cs.gbl254.course_registration_system.custom_components.PositionedComponent;
import edu.nyu.cs.gbl254.course_registration_system.data_management.Admin;
import edu.nyu.cs.gbl254.course_registration_system.data_management.CourseRegistrationSystem;
import edu.nyu.cs.gbl254.course_registration_system.data_management.Student;

@SuppressWarnings("serial")
// represents the window admins see when registering a student
public class RegisterPage extends Page {
	
	// label for first name field
	private PositionedComponent fNameLabel = new PositionedComponent(new JLabel("First Name:"), 0, 0, 1, 1, GridBagConstraints.EAST);
	
	// first name field
	private PositionedComponent fNameField = new PositionedComponent(new JTextField(15), 1, 0, 2, 1, GridBagConstraints.EAST);
	
	// last name field
	private PositionedComponent lNameField = new PositionedComponent(new JTextField(15), 1, 1, 2, 1, GridBagConstraints.EAST);
	
	// label for last name field
	private PositionedComponent lNameLabel = new PositionedComponent(new JLabel("Last Name:"), 0, 1, 1, 1, GridBagConstraints.EAST);
	
	// label for username field
	private PositionedComponent uNameLabel = new PositionedComponent(new JLabel("Username:"), 0, 2, 1, 1, GridBagConstraints.EAST);
	
	// username field
	private PositionedComponent uNameField = new PositionedComponent(new JTextField(15), 1, 2, 2, 1, GridBagConstraints.EAST);
	
	// password field
	private PositionedComponent pWordField = new PositionedComponent(new JPasswordField(15), 1, 3, 2, 1, GridBagConstraints.EAST);
	
	// label for password field
	private PositionedComponent pWordLabel = new PositionedComponent(new JLabel("Password:"), 0, 3, 1, 1, GridBagConstraints.EAST);
	
	// button that allows the student to cancel registration
	private PositionedComponent cancelButton = new PositionedComponent(new JButton("Cancel"), 1, 4, 1, 1, GridBagConstraints.EAST);
	
	// button that allows the student to finish registering
	private PositionedComponent finishButton = new PositionedComponent(new JButton("Finish"), 2, 4, 1, 1, GridBagConstraints.EAST);
	
	// all components in the window
	private PositionedComponent[] components = {fNameLabel, fNameField, lNameField, lNameLabel, uNameLabel, uNameField, pWordField, pWordLabel, cancelButton, finishButton};
	
	// allows instantiation of a registration window with a reference to the previous window
	public RegisterPage(Page prev) {
		super("Student Registration", 400, 200);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.addComponents(components);
		this.addPanel();
		this.pack();
		this.setLocation();
		this.setVisible(true);
		((JButton) this.finishButton.getComponent()).addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String firstName = ((JTextField) fNameField.getComponent()).getText().trim().replaceAll("\\s+", " ");
				String lastName = ((JTextField) lNameField.getComponent()).getText().trim().replaceAll("\\s+", " ");
				String username = ((JTextField) uNameField.getComponent()).getText();
				String password = new String(((JPasswordField) pWordField.getComponent()).getPassword());
				if(firstName.length() < 1) {
					displayErrorMessage("Please enter a first name.");
				}
				else if(lastName.length() < 1) {
					displayErrorMessage("Please enter a last name.");
				}
				else if(!isUsernameAndPasswordValid(username, password)) {
					displayErrorMessage("Username and password must be at least 5 characters and contain no spaces.");
				}
				else if(isDuplicate(username)) {
					displayErrorMessage("This username is already taken.");
				}
				else {
					((Admin) CourseRegistrationSystem.getCurrentUser()).registerStudent(new Student(username, password, firstName, lastName));
					displayMessage("New student registered.");
					prev.setVisible(true);
					dispose();
				}
			}
		});
		((JButton) this.cancelButton.getComponent()).addActionListener(new ActionListener() {
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
}
