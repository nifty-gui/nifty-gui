package de.lessvoid.nifty.processing.renderer;

import de.lessvoid.nifty.spi.render.MouseCursor;
import processing.core.*;

/**
 * Implementation of Nifty mouse cursor functions for Processing.
 * @author Xuanming
 */
public class MouseCursorProcessing implements MouseCursor {
	
	private final PApplet app;
	private final PImage img;
	private final int x;
	private final int y;
	
	/**
	 * Instantiate the MouseCursorProcessing object.
	 * @param app PApplet instance that Processing is currently running in.
	 * @param img PImage object to be used as mouse cursor.
	 * @param x Cursor hotspot x-coordinate.
	 * @param y Cursor hotspot y-coordinate.
	 */
	public MouseCursorProcessing(PApplet app, String filename, int x, int y) {
		this.app = app;
		this.img = app.loadImage(filename);
		this.x = x;
		this.y = y;			
	}

	@Override
	public void enable() {
		app.cursor(img, x, y);
	}

	@Override
	public void disable() {
		app.cursor(PConstants.ARROW);
	}

	@Override
	public void dispose() { // Do nothing.
	}
}
