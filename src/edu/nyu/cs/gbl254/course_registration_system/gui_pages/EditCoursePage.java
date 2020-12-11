package edu.nyu.cs.gbl254.course_registration_system.gui_pages;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;

import edu.nyu.cs.gbl254.course_registration_system.custom_components.PositionedComponent;
import edu.nyu.cs.gbl254.course_registration_system.data_management.Admin;
import edu.nyu.cs.gbl254.course_registration_system.data_management.Course;
import edu.nyu.cs.gbl254.course_registration_system.data_management.CourseRegistrationSystem;

@SuppressWarnings("serial")
// represents the window shown when creating or editing a course
public class EditCoursePage extends Page {
	
	// the course to edit
	private Course toEdit;
	
	// whether the admin is creating a course, true if yes, false if not
	private boolean isCreate;
	
	// label for course name field
	private PositionedComponent nameLabel = new PositionedComponent(new JLabel("Name:"), 0, 0, 1, 1, GridBagConstraints.EAST);
	
	// label for course ID field
	private PositionedComponent IDLabel = new PositionedComponent(new JLabel("ID:"), 0, 1, 1, 1, GridBagConstraints.EAST);
	
	// label for course capacity field
	private PositionedComponent capacityLabel = new PositionedComponent(new JLabel("Capacity:"), 0, 2, 1, 1, GridBagConstraints.EAST);
	
	// label for course instructor field
	private PositionedComponent instructorLabel = new PositionedComponent(new JLabel("Instructor:"), 0, 3, 1, 1, GridBagConstraints.EAST);
	
	// label for course section number field
	private PositionedComponent sectionNumberLabel = new PositionedComponent(new JLabel("Section Number:"), 0, 4, 1, 1, GridBagConstraints.EAST);
	
	// label for course location field
	private PositionedComponent locationLabel = new PositionedComponent(new JLabel("Location:"), 0, 5, 1, 1, GridBagConstraints.EAST);
	
	// course capacity field
	private PositionedComponent capacityField;
	
	// course instructor field
	private PositionedComponent instructorField;
	
	// course section number field
	private PositionedComponent sectionNumberField;
	
	// course location field
	private PositionedComponent locationField;
	
	// button that finishes the create or edit process
	private PositionedComponent finishButton = new PositionedComponent(new JButton("Finish"), 2, 6, 1, 1, GridBagConstraints.WEST);
	
	// button that cancels creation or editing
	private PositionedComponent cancelButton = new PositionedComponent(new JButton("Cancel"), 1, 6, 1, 1, GridBagConstraints.WEST);
	
	// for course creation:
	// course name field
	private PositionedComponent nameField = new PositionedComponent(new JTextField(15), 1, 0, 2, 1, GridBagConstraints.WEST);
	
	// course ID field
	private PositionedComponent IDField = new PositionedComponent(new JTextField(15), 1, 1, 2, 1, GridBagConstraints.WEST);
	
	// for course editing:
	// course name label
	private PositionedComponent courseName;
	
	// course ID label
	private PositionedComponent courseId;
	
	// allows instantiation of a course creation/editing window with a reference to the previous window, boolean is creating, a window name, and the course to be edited
	public EditCoursePage(Page prev, boolean isCreate, String title, Course toEdit) {
		super(title, 400, 350);
		QuickLinksBar.addPageToDispose(this);
		this.setPrev(prev);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.toEdit = toEdit;
		this.isCreate = isCreate;
		if(isCreate) this.addCreateFeatures();
		else this.addEditFeatures();
		this.addPanel();
		this.pack();
		this.setLocation();
		this.setVisible(true);
		((JButton) this.finishButton.getComponent()).addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String capacity = ((JTextField) capacityField.getComponent()).getText().trim();
				String instructor = ((JTextField) instructorField.getComponent()).getText().trim().replaceAll("\\s+", " ");
				String sectionNumber = ((JTextField) sectionNumberField.getComponent()).getText().trim();
				String location = ((JTextField) locationField.getComponent()).getText().trim().replaceAll("\\s+", " ");
				if(isCreate) {
					String name = ((JTextField) nameField.getComponent()).getText().trim().replaceAll("\\s+", " ");
					String id = ((JTextField) IDField.getComponent()).getText().trim().replaceAll("\\s+", "");
					finishEdit(name, id, capacity, instructor, sectionNumber, location);
				}
				else {
					String name = ((JLabel) courseName.getComponent()).getText();
					String id = ((JLabel) courseId.getComponent()).getText();
					finishEdit(name, id, capacity, instructor, sectionNumber, location);
				}
			}
		});
		((JButton) this.cancelButton.getComponent()).addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				close();
			}
		});
		this.addWindowListener(new WindowAdapter() {
		    public void windowClosing(WindowEvent windowEvent) {
		        close();
		    }
		});
	}
	
	// adds features only used when creating a course
	private void addCreateFeatures() {
		capacityField = new PositionedComponent(new JTextField(15), 1, 2, 2, 1, GridBagConstraints.WEST);
		instructorField = new PositionedComponent(new JTextField(15), 1, 3, 2, 1, GridBagConstraints.WEST);
		sectionNumberField = new PositionedComponent(new JTextField(15), 1, 4, 2, 1, GridBagConstraints.WEST);
		locationField = new PositionedComponent(new JTextField(15), 1, 5, 2, 1, GridBagConstraints.WEST);
		PositionedComponent[] components = {
			nameLabel, IDLabel, capacityLabel, instructorLabel, sectionNumberLabel, locationLabel,
			nameField, IDField, capacityField, instructorField, sectionNumberField, locationField, cancelButton, finishButton
		};
		this.addComponents(components);
	}
	
	// adds features only used when editing a course
	private void addEditFeatures() {
		courseName = new PositionedComponent(new JLabel(" " + toEdit.getName()), 1, 0, 2, 1, GridBagConstraints.WEST);
		courseId = new PositionedComponent(new JLabel(" " + toEdit.getId()), 1, 1, 2, 1, GridBagConstraints.WEST);
		capacityField = new PositionedComponent(new JTextField(Integer.toString(toEdit.getMaxStudents()), 15), 1, 2, 2, 1, GridBagConstraints.WEST);
		instructorField = new PositionedComponent(new JTextField(toEdit.getInstructor(), 15), 1, 3, 2, 1, GridBagConstraints.WEST);
		sectionNumberField = new PositionedComponent(new JTextField(Integer.toString(toEdit.getSectionNumber()), 15), 1, 4, 2, 1, GridBagConstraints.WEST);
		locationField = new PositionedComponent(new JTextField(toEdit.getLocation(), 15), 1, 5, 2, 1, GridBagConstraints.WEST);
		PositionedComponent[] components = {
			nameLabel, IDLabel, capacityLabel, instructorLabel, sectionNumberLabel, locationLabel,
			courseName, courseId, capacityField, instructorField, sectionNumberField, locationField, cancelButton, finishButton
		};
		this.addComponents(components);
	}
	
	// completes the creation/editing process
	private void finishEdit(String name, String id, String capacity, String instructor, String sectionNumber, String location) {
		Admin currentAdmin = (Admin) CourseRegistrationSystem.getCurrentUser();
		if(!isEntriesValid(name, id, capacity, instructor, sectionNumber, location)) {
			displayErrorMessage("One or more entries are invalid. Please try again.");
		}
		else if(!isCreate && (Integer.parseInt(capacity)<toEdit.getRegisteredStudents())) {
			displayErrorMessage("Enrolled students cannot exceed capacity.");
		}
		else if(isDuplicate(name, sectionNumber)) {
			displayErrorMessage("There is already a course with this name and section number.");
		}
		else {
			if(!isCreate) {
				currentAdmin.editCourse(toEdit, Integer.parseInt(capacity), instructor, Integer.parseInt(sectionNumber), location);
				displayMessage("Course successfully edited.");
				close();
				
			}
			else{
				currentAdmin.createCourse(name, id, Integer.parseInt(capacity), instructor, Integer.parseInt(sectionNumber), location);
				displayMessage("Course successfully created.");
				close();
			}
		}
	}
	
	// checks if course field entries are valid, returns false if they are empty, if capacity/section number fields are not integers or are negative, true if otherwise
	public static boolean isEntriesValid(String name, String id, String capacity, String instructor, String sectionNumber, String location) {
		if(name.length()<1 || id.length()<1 || capacity.length()<1 || instructor.length()<1 || sectionNumber.length()<1 || location.length()<1) {
			return false;
		}
		try {
			int cap = Integer.parseInt(capacity);
			int sec = Integer.parseInt(sectionNumber);
			if(cap<0 || sec<0) return false;
		}
		catch(NumberFormatException nfe) {
			return false;
		}
		return true;
	}
	
	// checks if the course being created/edited already exists, true if it does, false if it does not
	private boolean isDuplicate(String name, String sectionNumber) {
		ArrayList<Course> courses = CourseRegistrationSystem.getCourses();
		for(Course course:courses) {
			if(course!=toEdit && (course.getName().toLowerCase().replaceAll("\\s+", "").equals(name.toLowerCase().replaceAll("\\s+", ""))
					&&course.getSectionNumber()==Integer.parseInt(sectionNumber))) {
				return true;
			}
		}
		return false;
	}
	
	// closes the window
	private void close() {
		if(!isCreate) {
			((CoursesPage) getPrev()).refresh();
			QuickLinksBar.removePageToDispose(this);
			this.dispose();
		}
		else {
			getPrev().setVisible(true);
			QuickLinksBar.removePageToDispose(this);
			this.dispose();
		}
	}
}
