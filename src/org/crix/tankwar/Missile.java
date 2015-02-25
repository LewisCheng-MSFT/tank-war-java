package org.crix.tankwar;

import java.awt.*;
import java.util.*;

public class Missile {
	private Direction direction;
	private GameField field;
	private int x;
	private int y;
	private Tank launcher;
	
	private static Image[] images;
	
	private static Image getDirectionImage(Direction direction) {
		return images[direction.ordinal()];
	}
	
	static {
		images = new Image[] {
			ResourceManager.getImageFromFile("image/missileU.gif"),
			ResourceManager.getImageFromFile("image/missileL.gif"),
			ResourceManager.getImageFromFile("image/missileLD.gif"),
			ResourceManager.getImageFromFile("image/missileLU.gif"),
			ResourceManager.getImageFromFile("image/missileR.gif"),
			ResourceManager.getImageFromFile("image/missileRD.gif"),
			ResourceManager.getImageFromFile("image/missileRU.gif"),
			ResourceManager.getImageFromFile("image/missileD.gif")
		};
	}
	
	public Missile(GameField field, Tank launcher, Direction direction, int x, int y) {
		this.field = field;
		this.launcher = launcher;
		this.direction = direction;
		this.x = x;
		this.y = y;
	}

	public void setLocation(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public void setDirection(Direction direction) {
		this.direction = direction;
	}
	
	public boolean hitObjects() {
		// If hit bounds.
		if (x < 0 || x > PropertyManager.Field.width || y < 0 || y > PropertyManager.Field.height)
			return true;
		
		Rectangle mine = getRectangle();
		
		// If hit walls.
		LinkedList<Wall> walls = field.getWalls();
		if (walls != null) {
			Iterator<Wall> itrWall = walls.iterator();
			while (itrWall.hasNext()) {
				Wall wall = itrWall.next();
				if (mine.intersects(wall.getRectangle()))
					return true;
			}
		}
		
		// If hit tanks.
		Tank player = field.getPlayerTank();
		if (player == launcher) {
			LinkedList<Tank> tanks = field.getTanks();
			if (tanks != null) {
				Iterator<Tank> itrTank = field.getTanks().iterator();
				while (itrTank.hasNext()) {
					Tank tank = itrTank.next();
					if (mine.intersects(tank.getRectangle())) {
						tank.setLive(false);
						return true;
					}
				}
			}
		} else {
			if (mine.intersects(player.getRectangle())) {
				player.setLive(false);
				return true;
			}
		}
		
		return false;
	}
	
	public Rectangle getRectangle() {
		return new Rectangle(x, y, PropertyManager.Missile.width, PropertyManager.Missile.height);
	}
	
	public void paint(Graphics g) {
		switch (direction) {
		case DOWN:
			y += PropertyManager.Missile.speed;
			break;
		case LEFT:
			x -= PropertyManager.Missile.speed;
			break;
		case LEFT_DOWN:
			x -= PropertyManager.Missile.diagSpeed;
			y += PropertyManager.Missile.diagSpeed;
			break;
		case LEFT_UP:
			x -= PropertyManager.Missile.diagSpeed;
			y -= PropertyManager.Missile.diagSpeed;
			break;
		case RIGHT:
			x += PropertyManager.Missile.speed;
			break;
		case RIGHT_DOWN:
			x += PropertyManager.Missile.diagSpeed;
			y += PropertyManager.Missile.diagSpeed;
			break;
		case RIGHT_UP:
			x += PropertyManager.Missile.diagSpeed;
			y -= PropertyManager.Missile.diagSpeed;
			break;
		case UP:
			y -= PropertyManager.Missile.speed;
			break;
		}
		
		g.drawImage(getDirectionImage(direction), x, y, null);
	}
	
	public Blast produceBlast() {
		Blast blast = new Blast();
		blast.setLocation(x, y);
		return blast;
	}
}
