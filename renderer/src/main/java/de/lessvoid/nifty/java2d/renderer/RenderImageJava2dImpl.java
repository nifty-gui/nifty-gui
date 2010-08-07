package de.lessvoid.nifty.java2d.renderer;

import java.awt.image.BufferedImage;

import de.lessvoid.nifty.spi.render.RenderImage;

public class RenderImageJava2dImpl implements RenderImage {

	final BufferedImage image;

	BufferedImage getImage() {
		return image;
	}

	public RenderImageJava2dImpl(BufferedImage image) {
		this.image = image;
	}

	public int getHeight() {
		return image.getHeight(null);
	}

	public int getWidth() {
		return image.getWidth(null);
	}

	public void dispose() {
	}
}