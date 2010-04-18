/**
 * 
 */
package de.lessvoid.nifty.java2d.renderer;

import java.awt.Image;

import de.lessvoid.nifty.spi.render.RenderImage;

public class RenderImageJava2dImpl implements RenderImage {

	final Image image;

	Image getImage() {
		return image;
	}

	public RenderImageJava2dImpl(Image image) {
		this.image = image;
	}

	public int getHeight() {
		return image.getHeight(null);
	}

	public int getWidth() {
		return image.getWidth(null);
	}

}