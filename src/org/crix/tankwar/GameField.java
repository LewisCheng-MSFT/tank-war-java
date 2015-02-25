package org.crix.tankwar;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

@SuppressWarnings("serial")
public class GameField extends Panel {
	private Image offscreen;
	private long repaintInterval;
	
	private Random random;
	
	private LinkedList<Tank> tanks;
	private LinkedList<Missile> missiles;
	private LinkedList<Blast> blasts;
	private LinkedList<Wall> walls;
	
	private Tank player = new Tank(this, 0, 0);
	
	public GameField(long repaintInterval) {
		random = new Random();
		
		// Create walls.
		walls = new LinkedList<Wall>();
		Wall w1 = new Wall(80, 150, 20, 300);
		Wall w2 = new Wall(250, 80, 300, 20);
		walls.add(w1);
		walls.add(w2);
		
		// Create tanks.
		tanks = new LinkedList<Tank>();
		addNewTanks(true);
		
		// Create missiles.
		missiles = new LinkedList<Missile>();
		
		// Create blasts.
		blasts = new LinkedList<Blast>();
		
		addKeyListener(new Controller());
		this.repaintInterval = repaintInterval;
		new Thread(new RepaintThreshold()).start();
	}
	
	public void addNewTanks(boolean init) {
		int tankCount = init ? PropertyManager.Field.initTanks : PropertyManager.Field.reproduceTanks;
		for (int i = 0; i < tankCount; ++i) {
			Tank tank = new Tank(this, 100 + i * 50, 200);
			tank.setMoving(true);
			tanks.add(tank);
		}
	}
	
	public Tank getPlayerTank() {
		return player;
	}
	
	public LinkedList<Tank> getTanks() {
		return tanks;
	}
	
	public LinkedList<Wall> getWalls() {
		return walls;
	}
	
	@Override
	public void paint(Graphics g) {
		// Paint player tank.
		if (player.isLive())
			player.moveAndPaint(g);
		
		// Paint walls.
		Iterator<Wall> itrWall = walls.iterator();
		while (itrWall.hasNext()) {
			Wall wall = itrWall.next();
			wall.paint(g);
		}
		
		// Paint tanks.
		Iterator<Tank> itrTank = tanks.iterator();
		while (itrTank.hasNext()) {
			Tank tank = itrTank.next();
			if (tank.isLive()) {
				// Change direction under the probability of 1/10.
				if (random.nextInt() % 10 == 0) {
					// Select a randomized direction.
					Direction[] directions = Direction.values();
					tank.setDirection(directions[random.nextInt(directions.length)]);
				}
				// Fire a missile under the probability of 1/30.
				if (random.nextInt() % 30 == 0) {
					// Select a randomized direction.
					Direction[] directions = Direction.values();
					missiles.add(tank.fireMissile((directions[random.nextInt(directions.length)])));
				}
				tank.moveAndPaint(g);
			}
			else
			{
				itrTank.remove();
			}
		}
		
		// Paint missiles.
		Iterator<Missile> itrMissile = missiles.iterator();
		while (itrMissile.hasNext()) {
			Missile missile = itrMissile.next();
			missile.paint(g);
			if (missile.hitObjects()) {
				blasts.add(missile.produceBlast());
				itrMissile.remove();
			}
		}
		if (tanks.size() < 1)
			addNewTanks(false);
		
		// Paint blasts.
		Iterator<Blast> itrBlast = blasts.iterator();
		while (itrBlast.hasNext()) {
			Blast blast = itrBlast.next();
			blast.paint(g);
			if (blast.blastOver())
				itrBlast.remove();
		}
	}

	@Override
	public void update(Graphics g) {
		if (offscreen == null)
			offscreen = createImage(PropertyManager.Field.width, PropertyManager.Field.height);
		Graphics os = offscreen.getGraphics();
		
		// Clear background image.
		Color bak = os.getColor();
		os.setColor(Color.BLACK);
		os.fillRect(0, 0, PropertyManager.Field.width, PropertyManager.Field.height);
		os.setColor(bak);
		
		// Paint objects to background image.
		paint(os);
		
		// Paint background image.
		g.drawImage(offscreen, 0, 0, null);
	}
	
	private class RepaintThreshold implements Runnable {
		@Override
		public void run() {
			while (true) {
				repaint();
				try {
					Thread.sleep(repaintInterval);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public class Controller extends KeyAdapter {
		private boolean upPressed;
		private boolean downPressed;
		private boolean leftPressed;
		private boolean rightPressed;
		
		private void setMyTankDirection() {
			if (!player.isLive())
				return;
			
			if (!upPressed && !downPressed && !leftPressed && !rightPressed) {
				player.setMoving(false);
				return;
			}
			
			if (upPressed && !downPressed && !leftPressed && !rightPressed)
				player.setDirection(Direction.UP);
			else if (!upPressed && downPressed && !leftPressed && !rightPressed)
				player.setDirection(Direction.DOWN);
			else if (!upPressed && !downPressed && leftPressed && !rightPressed)
				player.setDirection(Direction.LEFT);
			else if (!upPressed && !downPressed && !leftPressed && rightPressed)
				player.setDirection(Direction.RIGHT);
			else if (upPressed && !downPressed && leftPressed && !rightPressed)
				player.setDirection(Direction.LEFT_UP);
			else if (!upPressed && downPressed && leftPressed && !rightPressed)
				player.setDirection(Direction.LEFT_DOWN);
			else if (upPressed && !downPressed && !leftPressed && rightPressed)
				player.setDirection(Direction.RIGHT_UP);
			else if (!upPressed && downPressed && !leftPressed && rightPressed)
				player.setDirection(Direction.RIGHT_DOWN);
			player.setMoving(true);
		}
		
		@Override
		public void keyPressed(KeyEvent e) {
			int key = e.getKeyCode();
			switch (key) {
			case KeyEvent.VK_SPACE:
				if (player.isLive())
					missiles.add(player.fireMissile());
				break;
			case KeyEvent.VK_A:
				if (player.isLive())
					missiles.addAll(player.fireMissiles());
				break;
			case KeyEvent.VK_R:
				player.setLive(true);
				break;
			case KeyEvent.VK_UP:
				upPressed = true;
				break;
			case KeyEvent.VK_DOWN:
				downPressed = true;
				break;
			case KeyEvent.VK_LEFT:
				leftPressed = true;
				break;
			case KeyEvent.VK_RIGHT:
				rightPressed = true;
				break;
			}
			setMyTankDirection();
		}

		@Override
		public void keyReleased(KeyEvent e) {
			int key = e.getKeyCode();
			switch (key) {
			case KeyEvent.VK_UP:
				upPressed = false;
				break;
			case KeyEvent.VK_DOWN:
				downPressed = false;
				break;
			case KeyEvent.VK_LEFT:
				leftPressed = false;
				break;
			case KeyEvent.VK_RIGHT:
				rightPressed = false;
				break;
			}
			setMyTankDirection();
		}
	}
}
