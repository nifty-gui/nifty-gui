package de.lessvoid.nifty.java2d.renderer;

import java.awt.Canvas;
import java.awt.Graphics2D;
import java.awt.image.VolatileImage;

/**
 * Default implementation of GraphicsWrapper using VolatileImage without BufferStrategy.
 * @author acoppes
 *
 */
public class GraphicsWrapperDefaultImpl implements GraphicsWrapper {

	private final Canvas canvas;

	private VolatileImage offscreenImage;

	public GraphicsWrapperDefaultImpl(Canvas canvas) {
		this.canvas = canvas;
		offscreenImage = (VolatileImage) canvas.createVolatileImage(
				getWidth(), getHeight());
	}

	@Override
	public Graphics2D beginFrame() {
		if (screenSizeHasChanged())
			offscreenImage = (VolatileImage) canvas.createVolatileImage(
					getWidth(), getHeight());
		return offscreenImage.createGraphics();
	}

	@Override
	public int getHeight() {
		return canvas.getSize().height;
	}

	@Override
	public int getWidth() {
		return canvas.getSize().width;
	}

	@Override
	public void endFrame() {
		canvas.getGraphics().drawImage(offscreenImage, 0, 0, null);
	}
	
	private boolean screenSizeHasChanged() {
		return (offscreenImage.getWidth() != getWidth() || offscreenImage
				.getHeight() != getHeight());
	}

}