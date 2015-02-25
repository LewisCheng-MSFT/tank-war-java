package org.crix.tankwar;

import java.io.*;
import java.util.*;

public abstract class PropertyManager {
	private static Properties properties = new Properties();
	
	public static class Frame {
		public static int width;
		public static int height;
	}
	
	public static class Field {
		public static int x;
		public static int y;
		public static int width;
		public static int height;
		public static int initTanks;
		public static int reproduceTanks;
	}
	
	public static class Tank {
		public static int speed;
		public static int diagSpeed;
		public static int width;
		public static int height;
	}
	
	public static class Missile {
		public static int width;
		public static int height;
		public static int speed;
		public static int diagSpeed;
	}
	
	public static class Blast {
		public static int speed;
		public static int count;
	}
	
	public static void loadProperties() {
		ClassLoader classLoader = PropertyManager.class.getClassLoader();
		InputStream resource = classLoader.getResourceAsStream("config/game.properties");
		try {
			properties.load(resource);
		} catch (IOException e) {
			e.printStackTrace();
		}

		Frame.width = getPropertyInt("frame.width");
		Frame.height = getPropertyInt("frame.height");
		
		Field.x = getPropertyInt("field.x");
		Field.y = getPropertyInt("field.y");
		Field.width = getPropertyInt("field.width");
		Field.height = getPropertyInt("field.height");
		Field.initTanks = getPropertyInt("field.inittanks");
		Field.reproduceTanks = getPropertyInt("field.reprtanks");
		
		Tank.speed = getPropertyInt("tank.speed");
		Tank.diagSpeed = (int)(Tank.speed / Math.sqrt(2));
		Tank.width = getPropertyInt("tank.width");
		Tank.height = getPropertyInt("tank.height");
		
		Missile.width = getPropertyInt("missile.width");
		Missile.height = getPropertyInt("missile.height");
		Missile.speed = getPropertyInt("missile.speed");
		Missile.diagSpeed = (int)(Missile.speed / Math.sqrt(2));
		
		Blast.speed = getPropertyInt("blast.speed");
		Blast.count = getPropertyInt("blast.count");
	}
	
	private static int getPropertyInt(String key) {
		return Integer.parseInt(properties.getProperty(key));
	}
}
