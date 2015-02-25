package org.crix.tankwar;

import java.awt.*;
import java.util.*;

public class Tank {
	private int x;
	private int y;
	private Direction direction = Direction.DOWN;
	private boolean live = true;
	private GameField field;
	
	private boolean moving;
	
	private static Image[] images;
	
	private static Image getDirectionImage(Direction direction) {
		return images[direction.ordinal()];
	}
	
	static {
		images = new Image[] {
			ResourceManager.getImageFromFile("image/tankD.gif"),
			ResourceManager.getImageFromFile("image/tankL.gif"),
			ResourceManager.getImageFromFile("image/tankLD.gif"),
			ResourceManager.getImageFromFile("image/tankLU.gif"),
			ResourceManager.getImageFromFile("image/tankR.gif"),
			ResourceManager.getImageFromFile("image/tankRD.gif"),
			ResourceManager.getImageFromFile("image/tankRU.gif"),
			ResourceManager.getImageFromFile("image/tankU.gif")
		};
	}
	
	public Tank(GameField field, int x, int  y) {
		this.field = field;
		this.x = x;
		this.y = y;
	}
	
	public boolean isLive() {
		return live;
	}
	
	public void setLive(boolean live) {
		this.live = live;
	}
	
	public void setMoving(boolean moving) {
		this.moving = moving;
	}
	
	public void setDirection(Direction direction) {
		this.direction = direction;
	}
	
	public void setLocation(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public Missile fireMissile(Direction direction) {
		Missile missile = new Missile(
				field,
				this,
				direction,
				x + PropertyManager.Tank.width / 2 + PropertyManager.Missile.width / 2,
				y + PropertyManager.Tank.height / 2 + PropertyManager.Missile.height / 2);
		return missile;
	}
	
	public Missile fireMissile() {
		return fireMissile(direction);
	}
	
	public LinkedList<Missile> fireMissiles() {
		LinkedList<Missile> missiles = new LinkedList<Missile>();
		Direction[] directions = Direction.values();
		for (int i = 0; i < directions.length; ++i) {
			missiles.add(fireMissile(directions[i]));
		}
		return missiles;
	}
	
	private void fixPositionForBounds() {
		int rightMostX = PropertyManager.Field.width - PropertyManager.Tank.width;
		int bottomMostY = PropertyManager.Field.height - PropertyManager.Tank.height;
		if (x < 0)
			x = 0;
		else if (x > rightMostX)
			x = rightMostX;
		if (y < 0)
			y = 0;
		else if (y > bottomMostY)
			y = bottomMostY;
	}
	
	private void fixPositionForCollision(int oldX, int oldY) {
		Rectangle mine = getRectangle();
		
		// Detect collision from walls.
		LinkedList<Wall> walls = field.getWalls();
		if (walls != null) {
			Iterator<Wall> itrWall = walls.iterator();
			while (itrWall.hasNext()) {
				Wall wall = itrWall.next();
				if (mine.intersects(wall.getRectangle())) {
					setLocation(oldX, oldY);
					return;
				}
			}
		}
		
		// Detect collision from tanks.
		Tank player = field.getPlayerTank();
		if (this != player && mine.intersects(player.getRectangle())) {
			setLocation(oldX, oldY);
			return;
		}
		LinkedList<Tank> tanks = field.getTanks();
		if (tanks != null) {
			Iterator<Tank> itrTank = field.getTanks().iterator();
			while (itrTank.hasNext()) {
				Tank tank = itrTank.next();
				if (this == tank)
					continue;
				if (mine.intersects(tank.getRectangle())) {
					setLocation(oldX, oldY);
					return;
				}
			}
		}
	}
	
	public Rectangle getRectangle() {
		return new Rectangle(x, y, PropertyManager.Tank.width, PropertyManager.Tank.height);
	}
	
	public void moveAndPaint(Graphics g) {
		if (moving) {
			int oldX = x;
			int oldY = y;
			switch (direction) {
			case DOWN:
				y += PropertyManager.Tank.speed;
				break;
			case LEFT:
				x -= PropertyManager.Tank.speed;
				break;
			case LEFT_DOWN:
				x -= PropertyManager.Tank.diagSpeed;
				y += PropertyManager.Tank.diagSpeed;
				break;
			case LEFT_UP:
				x -= PropertyManager.Tank.diagSpeed;
				y -= PropertyManager.Tank.diagSpeed;
				break;
			case RIGHT:
				x += PropertyManager.Tank.speed;
				break;
			case RIGHT_DOWN:
				x += PropertyManager.Tank.diagSpeed;
				y += PropertyManager.Tank.diagSpeed;
				break;
			case RIGHT_UP:
				x += PropertyManager.Tank.diagSpeed;
				y -= PropertyManager.Tank.diagSpeed;
				break;
			case UP:
				y -= PropertyManager.Tank.speed;
				break;
			}
			fixPositionForBounds();
			fixPositionForCollision(oldX, oldY);
		}
		
		g.drawImage(getDirectionImage(direction), x, y, null);
	}
}
