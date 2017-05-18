package edu.ifce.sistops;

import java.awt.image.BufferedImage;
import java.io.InputStream;

import javax.imageio.ImageIO;

public enum Loader {
	INSTANCE;
	public BufferedImage assetImg(String name) throws Exception {
		try (InputStream in = Loader.class.getResourceAsStream(name)) {
			return ImageIO.read(in);
		}
	}
}
