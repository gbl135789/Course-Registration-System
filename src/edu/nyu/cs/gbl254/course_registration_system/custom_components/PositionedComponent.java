package edu.nyu.cs.gbl254.course_registration_system.custom_components;

import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JComponent;

import edu.nyu.cs.gbl254.course_registration_system.data_management.CourseRegistrationSystem;

// represents one component in a window panel with GridBagConstraints parameters
public class PositionedComponent {
	
	// the component
	private JComponent component;
	
	// grid x-position of the component
	private int gridx;
	
	// grid y-position of the component
	private int gridy;
	
	// how many cells wide the component is
	private int gridwidth;
	
	// how many cells tall the component is
	private int gridheight;
	
	// where in the cell the component is
	private int anchor;
	
	// allows instantiation of a positioned component given a component, grid x-position, grid y-position, grid width, grid height, and anchor
	public PositionedComponent(JComponent component, int gridx, int gridy, int gridwidth, int gridheight, int anchor) {
		this.component = component;
		this.gridx = gridx;
		this.gridy = gridy;
		this.gridwidth = gridwidth;
		this.gridheight = gridheight;
		this.anchor = anchor;
	}
	
	// allows instantiation of a positioned component given a component, grid x-position, grid y-position, grid width, grid height, anchor, and a size to set it to
	public PositionedComponent(JComponent component, int gridx, int gridy, int gridwidth, int gridheight, int anchor, Dimension prefSize) {
		this(component, gridx, gridy, gridwidth, gridheight, anchor);
		this.component.setPreferredSize(prefSize);
	}
	
	// allows instantiation of a positioned component given a component, grid x-position, grid y-position, grid width, grid height, anchor, style of font, and size of font
	public PositionedComponent(JComponent component, int gridx, int gridy, int gridwidth, int gridheight, int anchor, int fontStyle, int fontSize) {
		this(component, gridx, gridy, gridwidth, gridheight, anchor);
		this.component.setFont(new Font(CourseRegistrationSystem.defaultFont.getFontName(), fontStyle, fontSize));
	}
	
	// returns the component
	public JComponent getComponent() {
		return this.component;
	}
	
	// returns the grid x-position
	public int getGridx() {
		return this.gridx;
	}
	
	// returns the grid y-position
	public int getGridy() {
		return this.gridy;
	}
	
	// returns the grid width
	public int getGridwidth() {
		return this.gridwidth;
	}
	
	// returns the grid height
	public int getGridheight() {
		return this.gridheight;
	}
	
	// returns the anchor
	public int getAnchor() {
		return this.anchor;
	}
}
