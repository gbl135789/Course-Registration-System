package edu.nyu.cs.gbl254.course_registration_system.gui_pages;

import java.awt.FontMetrics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import edu.nyu.cs.gbl254.course_registration_system.custom_components.PositionedComponent;
import edu.nyu.cs.gbl254.course_registration_system.data_management.Course;
import edu.nyu.cs.gbl254.course_registration_system.data_management.CourseRegistrationSystem;
import edu.nyu.cs.gbl254.course_registration_system.data_management.Student;
import edu.nyu.cs.gbl254.course_registration_system.data_management.User;

@SuppressWarnings("serial")
// represents a single window on the GUI
public abstract class Page extends JFrame {
	
	// the window's panel, with GridBagLayout for component positioning
	private JPanel panel = new JPanel(new GridBagLayout());
	
	// the window that is currently open
	private Page curr;
	
	// the previous window
	private Page prev;
	
	// the x-coordinate of the window on the screen
	private int locationX;
	
	// the y-coordinate of the window on the screen
	private int locationY;
	
	// allows instantiation of a window given a window name, window width, window height, window x-coordinate, and window y-coordinate
	public Page(String name, int width, int height, int locationX, int locationY) {
		this(name, width, height);
		this.locationX = locationX;
		this.locationY = locationY;
	}
	
	// allows instantiation of a window given a window name, window width, and window height
	public Page(String name, int width, int height) {
		super(name);
		this.setSize(width, height);
		this.locationX = (int) (CourseRegistrationSystem.screenSize.getWidth()/7); 
		this.locationY = (int) (CourseRegistrationSystem.screenSize.getHeight()/4);
	}
	
	// adds the components to the panel
	public void addComponents(PositionedComponent[] components) {
		GridBagConstraints gbc = new GridBagConstraints();
		for(PositionedComponent pc:components) {
			gbc.gridx = pc.getGridx();
			gbc.gridy = pc.getGridy();
			gbc.gridwidth = pc.getGridwidth();
			gbc.gridheight = pc.getGridheight();
			gbc.anchor = pc.getAnchor();
			this.panel.add(pc.getComponent(), gbc);
		}
	}
	
	// adds the panel to the window
	public void addPanel() {
		this.add(this.panel);
	}
	
	// sets the location of the window on the screen
	public void setLocation() {
		this.setLocation(this.locationX, this.locationY);
	}
	
	// returns the current window
	public Page getCurr() {
		return this.curr;
	}
	
	// sets the current window
	public void setCurr(Page curr) {
		this.curr = curr;
	}
	
	// returns the previous window
	public Page getPrev() {
		return this.prev;
	}
	
	// sets the previous window
	public void setPrev(Page prev) {
		this.prev = prev;
	}
	
	// displays a message
	public void displayMessage(String message) {
		JOptionPane.showMessageDialog(null, message, "Success", JOptionPane.INFORMATION_MESSAGE);
	}
	
	// displays an error message
	public void displayErrorMessage(String message) {
		JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
	}
	
	// returns course data in a 2D array
	public String[][] getCourseData(ArrayList<Course> courses) {
		if(courses.size() > 0) {
			String[][] data = new String[courses.size()][7];
			for(int i=0; i<courses.size(); i++) {
				data[i][0] = courses.get(i).getName();
				data[i][1] = courses.get(i).getId();
				data[i][2] = Integer.toString(courses.get(i).getMaxStudents());
				data[i][3] = Integer.toString(courses.get(i).getRegisteredStudents());
				data[i][4] = courses.get(i).getInstructor();
				data[i][5] = Integer.toString(courses.get(i).getSectionNumber());
				data[i][6] = courses.get(i).getLocation();
			}
			return data;
		}
		String[][] empty = {{"", "", "", "No Courses", "", "", ""}};
		return empty;
	}
	
	// returns student data in a 2D array
	public String[][] getStudentData(ArrayList<Student> students) {
		if(students.size() > 0) {
			String[][] data = new String[students.size()][3];
			for(int i=0; i<students.size(); i++) {
				data[i][0] = students.get(i).getFirstName();
				data[i][1] = students.get(i).getLastName();
				data[i][2] = students.get(i).getUsername();
			}
			return data;
		}
		String[][] empty = {{"", "No students", ""}};
		return empty;
	}
	
	// returns a reference to the course selected in the table
	public Course searchCourse(ArrayList<Course> courses, JTable table) {
		int col1 = 0;
		int col2 = 5;
		int row = table.getSelectedRow();
		String courseName = String.valueOf(table.getValueAt(row, col1));
		int sectionNumber = Integer.valueOf(String.valueOf(table.getValueAt(row, col2)));
		Course course = null;
		for(Course c:courses) {
			if(courseName.equals(c.getName()) && sectionNumber==c.getSectionNumber()) {
				course = c;
			}
		}
		return course;
	}
	
	// returns a reference to the student selected in the table
	public Student searchStudent(ArrayList<Student> students, JTable table) {
		int row = table.getSelectedRow();
		String username = String.valueOf(table.getValueAt(row, 2));
		Student stu = null;
		for(Student student:students) {
			if(student.getUsername().equals(username)) stu = student;
		}
		return stu;
	}
	
	// set widths of table columns to fit all data
	public void setColumnWidths(JTable table, int paneWidth) {
		FontMetrics metrics = table.getFontMetrics(table.getFont());
		int totalWidth = 0;
		for(int i=0; i<table.getColumnCount(); i++) {
			int colWidth = metrics.stringWidth(table.getColumnName(i)) + 10;
			for(int j=0; j<table.getRowCount(); j++) {
				int width = metrics.stringWidth(String.valueOf(table.getValueAt(j, i))) + 10;
				if(width > colWidth) {
					colWidth = width;
				}
			}
			totalWidth += colWidth;
			table.getColumnModel().getColumn(i).setPreferredWidth(colWidth);
		}
		if(totalWidth < paneWidth) {
			table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		}
	}
	
	// sets the scroll speed of a scroll pane
	public void setScrollSpeed(JScrollPane pane, int verticalSpeed, int horizontalSpeed) {
		pane.getVerticalScrollBar().setUnitIncrement(verticalSpeed);
		pane.getHorizontalScrollBar().setUnitIncrement(horizontalSpeed);
	}
	
	// checks if the username and password fields are valid, returns true if they are at least 5 characters and have no spaces, false otherwise
	public boolean isUsernameAndPasswordValid(String username, String password) {
		if((username.length()<5||password.length()<5) || (username.contains(" ")||password.contains(" "))) return false;
		return true;
	}
		
	// checks if the username is already in use, returns true if it is, false if not
	public boolean isDuplicate(String username) {
		ArrayList<User> users = CourseRegistrationSystem.getUsers();
		for(User user:users) {
			if(user.getUsername().equals(username)) return true;
		}
		return false;
	}
}
