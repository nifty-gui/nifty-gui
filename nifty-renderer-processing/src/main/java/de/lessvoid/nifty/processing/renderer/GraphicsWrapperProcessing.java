package de.lessvoid.nifty.processing.renderer;

import java.awt.Graphics2D;
import processing.core.*;
import de.lessvoid.nifty.java2d.renderer.GraphicsWrapper;

/**
 * Wrap the Processing PGraphics object for use with the Nifty Java2D Renderer. * 
 * @author Xuanming
 */
public class GraphicsWrapperProcessing implements GraphicsWrapper {
	
	private final PGraphicsJava2D canvas;
	
	/**
	 * Initialize the GraphicsWrapper.
	 * @param app PApplet instance Processing is running in.
	 * @param width Width of canvas to draw Nifty on.
	 * @param height Height of canvas to draw Nifty on.
	 */
	public GraphicsWrapperProcessing(PApplet app, int width, int height) {
		canvas = new PGraphicsJava2D();
		canvas.setSize(width, height);
		canvas.beginDraw();
		canvas.endDraw();
	}
	
	/**
	 * Returns the PGraphics object for external use.
	 * @return PGraphics object used for rendering Nifty.
	 */
	public PGraphicsJava2D getCanvas() {
		return canvas;
	}
	
	@Override
	public Graphics2D getGraphics2d() {
		return canvas.g2;
	}

	@Override
	public int getHeight() {
		return canvas.height;
	}

	@Override
	public int getWidth() {
		return canvas.width;
	}
}
