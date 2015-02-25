package org.crix.tankwar;

import java.awt.*;

public class Wall {
	private int x;
	private int y;
	private int width;
	private int height;
	
	public Wall(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public void paint(Graphics g) {
		Color bak = g.getColor();
		g.setColor(Color.GRAY);
		g.fillRect(x, y, width, height);
		g.setColor(bak);
	}
	
	public Rectangle getRectangle() {
		return new Rectangle(x, y, width, height);
	}
}
