package edu.nyu.cs.gbl254.course_registration_system.gui_pages;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;

import edu.nyu.cs.gbl254.course_registration_system.custom_components.PositionedComponent;
import edu.nyu.cs.gbl254.course_registration_system.data_management.CourseRegistrationSystem;

@SuppressWarnings("serial")
// represents a quick links window to allow easy navigation for admins
public class QuickLinksBar extends Page {
	
	// the dashboard to link to
	private AdminDashboard dashboard;
	
	// pages to dispose when going back to the dashboard
	private static ArrayList<Page> toDispose = new ArrayList<Page>();
	
	// button that allows the admin to return to the dashboard in one click
	private PositionedComponent returnToDashboardButton = new PositionedComponent(new JButton("Return to dashboard"), 0, 0, 1, 1, GridBagConstraints.CENTER);
	
	// button that exits the system
	private PositionedComponent exitButton = new PositionedComponent(new JButton("Exit"), 0, 1, 1, 1, GridBagConstraints.CENTER);
	
	// all components in the window
	private PositionedComponent[] components = {returnToDashboardButton, exitButton};
	
	// allows instantiation of a quick links window with a reference to an admin dashboard
	public QuickLinksBar(AdminDashboard dashboard) {
		super("Quick Links", 200, 100, (int) (CourseRegistrationSystem.screenSize.getWidth()/7), (int) (CourseRegistrationSystem.screenSize.getHeight()/7)-2);
		this.dashboard = dashboard;
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.addComponents(components);
		this.addPanel();
		this.setLocation();
		this.setVisible(true);
		((JButton) this.returnToDashboardButton.getComponent()).addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				returnToDashboard();
			}
		});
		((JButton) this.exitButton.getComponent()).addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CourseRegistrationSystem.exit();
			}
		});
	}
	
	// returns to the dashboard
	private void returnToDashboard() {
		for(Page page:toDispose) {
			page.dispose();
		}
		dashboard.setVisible(true);
	}
	
	// adds a page to the list of pages to dispose
	public static void addPageToDispose(Page page) {
		QuickLinksBar.toDispose.add(page);
	}
	
	// removes a page from the list of pages to dispose
	public static void removePageToDispose(Page page) {
		QuickLinksBar.toDispose.remove(page);
	}
}
