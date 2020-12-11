package edu.nyu.cs.gbl254.course_registration_system.gui_pages;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import edu.nyu.cs.gbl254.course_registration_system.custom_components.PositionedComponent;
import edu.nyu.cs.gbl254.course_registration_system.data_management.Admin;
import edu.nyu.cs.gbl254.course_registration_system.data_management.Course;
import edu.nyu.cs.gbl254.course_registration_system.data_management.CourseRegistrationSystem;
import edu.nyu.cs.gbl254.course_registration_system.data_management.Student;
import edu.nyu.cs.gbl254.course_registration_system.data_management.User;

@SuppressWarnings("serial")
// represents the window that displays course information
public class CoursesPage extends Page {
	
	// list of courses that are shown when clicking 'view all courses'
	private ArrayList<Course> allCourses;
	
	// list of courses that are currently shown 
	private ArrayList<Course> shownCourses;
	
	// current user
	private User currentUser = CourseRegistrationSystem.getCurrentUser();
	
	// whether the current user is an admin, true if yes, false if not
	private boolean isAdmin;
	
	// whether the admin is enrolling or unenrolling a student, true if yes, false if not
	private boolean isEnrollOrUnenroll;
	
	// name of the window
	private String title;
	
	// course data table headers
	private static final String[] colTitles = {"Name", "ID", "Capacity", "Enrolled", "Instructor", "Section", "Location"};
	
	// data table of courses
	private JTable courseTable;
	
	// scroll pane that the course table is inside
	private PositionedComponent pane;
	
	// button that allows users to view all courses
	private PositionedComponent viewAllCoursesButton = new PositionedComponent(new JButton("View all courses"), 0, 0, 2, 1, GridBagConstraints.WEST);
	
	// button that allows users to go back to the previous window
	private PositionedComponent backButton = new PositionedComponent(new JButton("Back"), 6, 0, 1, 1, GridBagConstraints.EAST);
	
	// admin exclusive features:
	// list of courses sorted by number of enrolled students
	private ArrayList<Course> sorted;
	
	// button that allows the admin to view courses that are full
	private PositionedComponent viewFullCoursesButton;
	
	// button that allows the admin to view courses sorted by number of enrolled students
	private PositionedComponent sortByNumberEnrolledButton;
	
	// button that allows the admin to edit a course
	private PositionedComponent editButton;
	
	// button that allows the admin to delete a course
	private PositionedComponent deleteButton;
	
	// button that allows the admin to view the list of students in a course
	private PositionedComponent viewStudentsButton;
	
	// button that allows the admin to search a course by ID
	private PositionedComponent searchByIdButton;
	
	// button that allows the admin to write the currently displayed course data to a text file
	private PositionedComponent writeToFileButton;
	
	// student exclusive features:
	// button that allows the student to view courses that are not full
	private PositionedComponent viewOpenCoursesButton;
	
	// button that allows the student to enroll in a course
	private PositionedComponent enrollButton;
	
	// allows instantiation of a courses page with a list of all courses, a list of shown courses, a boolean is admin, a reference to the previous window, and a window name
	public CoursesPage(ArrayList<Course> allCourses, ArrayList<Course> shownCourses, boolean isAdmin, Page prev, String title, boolean isEnrollOrUnenroll) {
		super(title, 1000, 750);
		QuickLinksBar.addPageToDispose(this);
		this.setCurr(this);
		this.setPrev(prev);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.allCourses = allCourses;
		this.shownCourses = shownCourses;
		this.isAdmin = isAdmin;
		this.isEnrollOrUnenroll = isEnrollOrUnenroll;
		this.title = title;
		this.courseTable = new JTable(this.getCourseData(shownCourses), colTitles);
		this.courseTable.setDefaultEditor(Object.class, null);
		this.courseTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		this.setColumnWidths(courseTable, 700);
		this.pane = new PositionedComponent(new JScrollPane(courseTable), 0, 1, 7, 1, GridBagConstraints.CENTER, new Dimension(700, 400));
		this.setScrollSpeed((JScrollPane) this.pane.getComponent(), 5, 5);
		this.addFeatures();
		if(this.isAdmin) this.addAdminFeatures();
		else this.addStudentFeatures();
		this.addPanel();
		this.pack();
		this.setLocation();
		this.setVisible(true);
		this.addWindowListener(new WindowAdapter() {
		    public void windowClosing(WindowEvent windowEvent) {
		        getPrev().setVisible(true);
		    }
		});
	}
	
	// adds features common to students and admins
	public void addFeatures() {
		((JButton) this.viewAllCoursesButton.getComponent()).addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				viewAllCourses();
			}
		});
		((JButton) this.backButton.getComponent()).addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getPrev().setVisible(true);
				dispose();
			}
		});
	}
	
	// adds admin exclusive features
	public void addAdminFeatures() {
		viewFullCoursesButton = new PositionedComponent(new JButton("View full courses"), 2, 0, 2, 1, GridBagConstraints.WEST);
		sortByNumberEnrolledButton = new PositionedComponent(new JButton("Sort by number enrolled"), 4, 0, 1, 1, GridBagConstraints.WEST);
		editButton = new PositionedComponent(new JButton("Edit"), 0, 2, 1, 1, GridBagConstraints.WEST);
		deleteButton = new PositionedComponent(new JButton("Delete"), 1, 2, 1, 1, GridBagConstraints.WEST);
		viewStudentsButton = new PositionedComponent(new JButton("View enrolled students"), 2, 2, 3, 1, GridBagConstraints.WEST);
		writeToFileButton = new PositionedComponent(new JButton("Write to file"), 5, 2, 1, 1, GridBagConstraints.WEST);
		searchByIdButton = new PositionedComponent(new JButton("Search by ID"), 5, 0, 1, 1, GridBagConstraints.WEST);
		PositionedComponent[] components = {pane, viewAllCoursesButton, backButton, viewFullCoursesButton, sortByNumberEnrolledButton, editButton, deleteButton, viewStudentsButton, writeToFileButton, searchByIdButton};
		this.addComponents(components);
		((JButton) this.viewFullCoursesButton.getComponent()).addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				viewFullCourses();
			}
		});
		((JButton) this.sortByNumberEnrolledButton.getComponent()).addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				viewSortedCourses();
			}
		});
		((JButton) this.searchByIdButton.getComponent()).addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				searchCoursesById();
			}
		});
		((JButton) this.editButton.getComponent()).addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Course toEdit = searchCourse(shownCourses, courseTable);
					if(toEdit != null) {
						openEditPanel(toEdit);
					}
					else {
						displayErrorMessage("Please select a course.");
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
		((JButton) this.deleteButton.getComponent()).addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Course toDelete = searchCourse(shownCourses, courseTable);
					if(toDelete != null) deleteCourse(toDelete, allCourses, shownCourses);
					else displayErrorMessage("Please select a course.");
				}
				catch(ArrayIndexOutOfBoundsException a) {
					displayErrorMessage("Please select a course.");
				}
				catch(NumberFormatException b) {
					displayErrorMessage("Please select a course.");
				}
			}
		});
		((JButton) this.viewStudentsButton.getComponent()).addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Course course = searchCourse(shownCourses, courseTable);
					if(course != null) {
						viewStudentsInCourse(course);
					}
					else {
						displayErrorMessage("Please select a course.");
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
		((JButton) this.writeToFileButton.getComponent()).addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if(shownCourses.size() == 0) {
						displayErrorMessage("There are no courses to write to a file.");
					}
					else {
						JTextField name = new JTextField(15);
						JTextField file = new JTextField(15);
						Object[] fields = {"Data name:", name, "File name:", file};
						int option = JOptionPane.showConfirmDialog(null, fields, "Write To File", JOptionPane.OK_CANCEL_OPTION);
						if(option == JOptionPane.OK_OPTION) {
							String nameStr = String.valueOf(name.getText()).trim();
							String fileNameStr = String.valueOf(file.getText()).trim();
							if(nameStr.length() == 0) {
								displayErrorMessage("Please enter a name for your data.");
							}
							else if(fileNameStr.length() == 0) {
								displayErrorMessage("Please enter a file name.");
							}
							else if(!isFileNameValid(fileNameStr)){
								displayErrorMessage("Make sure there are no spaces or special characters other than underscores in the file name.");
							}
							else {
								((Admin) currentUser).coursesToFile(shownCourses, colTitles, nameStr, fileNameStr);
								displayMessage("Course data has been written to course_data/" + fileNameStr + ".txt");
							}
						}
					}
				}
				catch(IOException a) {
					displayErrorMessage("There was an error writing to the file.");
				}
			}
		});
	}
	
	// adds student exclusive features
	public void addStudentFeatures() {
		viewOpenCoursesButton = new PositionedComponent(new JButton("View open courses"), 2, 0, 1, 1, GridBagConstraints.WEST);
		enrollButton = new PositionedComponent(new JButton("Enroll"), 0, 2, 1, 1, GridBagConstraints.WEST);
		PositionedComponent[] components = {pane, viewAllCoursesButton, backButton, viewOpenCoursesButton, enrollButton};
		this.addComponents(components);
		((JButton) viewOpenCoursesButton.getComponent()).addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				viewOpenCourses();
			}
		});
		((JButton) enrollButton.getComponent()).addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Course toEnroll = searchCourse(shownCourses, courseTable);
					Student student = (Student) currentUser;
					if(toEnroll == null) {
						displayErrorMessage("Course not found.");
					}
					else if(toEnroll.isFull()) {
						displayErrorMessage("Course is full.");
					}
					else if(student.getCourses().contains(toEnroll)) {
						displayErrorMessage("You are already enrolled in this course.");
					}
					else {
						student.enroll(toEnroll);
						displayMessage("You have enrolled in this course");
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
	}
	
	// checks if a file name is valid, returns true if it does not contain spaces or special characters, false if not
	private boolean isFileNameValid(String fileName) {
		Pattern noSpecialChars = Pattern.compile("[^A-Za-z0-9_]");
		Matcher matcher = noSpecialChars.matcher(fileName);
		if(matcher.find()) return false;
		return true;
	}
	
	// views all courses
	private void viewAllCourses() {
		if(this.currentUser instanceof Admin) {
			Admin currentAdmin = (Admin) currentUser;
			if(this.title.equals("Courses")) currentAdmin.viewAllCourses(getPrev());
			else currentAdmin.viewAllCourses(this.allCourses, this.allCourses, this.getPrev(), this.title, this.isEnrollOrUnenroll);
			QuickLinksBar.removePageToDispose(this);
			this.dispose();
		}
		else {
			((Student) currentUser).viewAllCourses(getPrev());
			this.dispose();
		}
	}
	
	// views full courses
	private void viewFullCourses() {
		this.shownCourses = CourseRegistrationSystem.getFullCourses(this.allCourses);
		((Admin) currentUser).viewFullCourses(this.allCourses, this.shownCourses, this.getPrev(), this.title);
		QuickLinksBar.removePageToDispose(this);
		this.dispose();
	}
	
	// searches courses by ID
	public void searchCoursesById() {
		String courseIdToSearch = JOptionPane.showInputDialog(null, "Enter course ID:", "Search By Course ID", JOptionPane.OK_CANCEL_OPTION);
		if(courseIdToSearch != null) {
			ArrayList<Course> coursesWithSearchedId = ((Admin) CourseRegistrationSystem.getCurrentUser()).searchCourseById(this.shownCourses, courseIdToSearch);
			new CoursesPage(this.shownCourses, coursesWithSearchedId, true, this.getPrev(), this.title, this.isEnrollOrUnenroll);
			this.dispose();
		}
	}
	
	// views open courses
	private void viewOpenCourses() {
		((Student) currentUser).viewOpenCourses(getPrev());
		this.dispose();
	}
	
	// views sorted courses
	private void viewSortedCourses() {
		this.sorted = new ArrayList<Course>(this.shownCourses);
		((Admin) currentUser).viewCoursesByNumStudents(this.allCourses, this.sorted, getPrev(), this.title);
		QuickLinksBar.removePageToDispose(this);
		this.dispose();
	}
	
	// views list of students in a course
	private void viewStudentsInCourse(Course course) {
		((Admin) currentUser).viewStudentsInCourse(course, getCurr());
		getCurr().setVisible(false);
	}
	
	// starts editing process for a course
	private void openEditPanel(Course toEdit) {
		new EditCoursePage(getCurr(), false, "Edit Course", toEdit);
		getCurr().setVisible(false);
	}
	
	// deletes a course
	private void deleteCourse(Course toDelete, ArrayList<Course> allCourses, ArrayList<Course> shownCourses) {
		((Admin) this.currentUser).deleteCourse(toDelete, allCourses, shownCourses);
		displayMessage("Course deleted.");
		this.refresh();
	}
	
	// refreshes the page
	public void refresh() {
		if(currentUser instanceof Student) ((Student) currentUser).viewAllCourses(getPrev());
		else new CoursesPage(this.allCourses, this.shownCourses, this.isAdmin, this.getPrev(), this.title, this.isEnrollOrUnenroll);
		QuickLinksBar.removePageToDispose(this);
		this.dispose();
	}
	
	// returns the back button
	public PositionedComponent getBackButton() {
		return this.backButton;
	}
	
	// returns the scroll pane
	public PositionedComponent getScrollPane() {
		return this.pane;
	}
	
	public PositionedComponent getSearchByIdButton() {
		return this.searchByIdButton;
	}
	
	// returns the course table
	public JTable getTable() {
		return this.courseTable;
	}
	
	// return whether the admin is enrolling or unenrolling a student
	public boolean getEnrollOrUnenroll() {
		return this.isEnrollOrUnenroll;
	}
}
