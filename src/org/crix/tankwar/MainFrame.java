package org.crix.tankwar;

import java.awt.*;
import java.awt.event.*;

@SuppressWarnings("serial")
public class MainFrame extends Frame {
	private GameField field;
	
	public MainFrame() {
		PropertyManager.loadProperties();
		initializeComponents();
	}
	
	private void initializeComponents() {
		this.setSize(PropertyManager.Frame.width, PropertyManager.Frame.height);
		this.setResizable(false);
		this.setTitle("Tank Game By Lewis Cheng @2011/4/6");
		this.setBackground(Color.DARK_GRAY);
		this.setLayout(null);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				System.exit(0);
			}
		});
		
		field = new GameField(50);
		field.setLocation(PropertyManager.Field.x, PropertyManager.Field.y);
		field.setSize(PropertyManager.Field.width, PropertyManager.Field.height);
		this.add(field);
		
		
		this.setVisible(true);
		field.requestFocus();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new MainFrame();
	}

}
