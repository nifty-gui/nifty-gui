package de.lessvoid.nifty.processing.renderer;

import de.lessvoid.nifty.spi.render.RenderImage;
import processing.core.PApplet;
import processing.core.PImage;

/**
 * Implementation of Nifty RenderImage using Processing's PImage class.
 * @author Xuanming
 */

public class RenderImageProcessing implements RenderImage {
	PImage image;
	
	public RenderImageProcessing (String filename, PApplet app){
		this.image = app.loadImage(filename);
	}

	@Override
	public int getWidth() {
		return image.width;
	}

	@Override
	public int getHeight() {
		return image.height;
	}

	@Override
	public void dispose() {
		// no such function in processing.
	}
	
	public PImage resize(int width, int height, float factor) {
		PImage temp;
		temp = image.get();
		temp.resize(width, height);
		temp.resize(Math.round(temp.width * factor), Math.round(temp.height * factor));
		return temp;
	}
	
	public PImage crop(int x, int y, int width, int height) {
		PImage temp;
		temp = image.get(x, y, width, height);
		return temp;
	}
}
