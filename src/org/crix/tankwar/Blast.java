package org.crix.tankwar;

import java.awt.*;

public class Blast {
	private int blastCount;
	
	private int x;
	private int y;
	
	private static Image[] images;
	
	static {
		images = new Image[PropertyManager.Blast.count];
		for (int i = 0; i < PropertyManager.Blast.count; ++i) {
			String filename = "image/" + String.valueOf(i) + ".gif";
			images[i] = ResourceManager.getImageFromFile(filename);
		}
	}
	
	public void paint(Graphics g) {
		if (!blastOver()) {
			fixPositionForBounds();
			g.drawImage(images[blastCount / PropertyManager.Blast.speed], x, y, null);
			++blastCount;
		}
	}
	
	public boolean blastOver() {
		return !(blastCount / PropertyManager.Blast.speed < PropertyManager.Blast.count);
	}
	
	private void fixPositionForBounds() {
		if (x < 0)
			x = 0;
		else if (x > PropertyManager.Field.width)
			x = PropertyManager.Field.width;
		
		if (y < 0)
			y = 0;
		else if (y > PropertyManager.Field.height)
			y = PropertyManager.Field.height;
	}
	
	public void setLocation(int x, int y) {
		this.x = x;
		this.y = y;
	}
}
