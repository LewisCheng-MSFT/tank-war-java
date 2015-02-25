package org.crix.tankwar;

import java.awt.Image;
import java.awt.Toolkit;

public abstract class ResourceManager {
	private static ClassLoader classLoader = ResourceManager.class.getClassLoader();
	private static Toolkit toolkit = Toolkit.getDefaultToolkit();
	
	public static Image getImageFromFile(String filename) {
		return toolkit.getImage(classLoader.getResource(filename));
	}
}
