package de.lessvoid.nifty.render.image.renderstrategy;

import de.lessvoid.nifty.layout.Box;
import de.lessvoid.nifty.spi.render.RenderDevice;
import de.lessvoid.nifty.spi.render.RenderImage;
import de.lessvoid.nifty.tools.Color;

public class ResizeStrategy implements RenderStrategy {

	@Override
	public void setParameters(String parameters) {
		if (parameters != null) {
			throw new IllegalArgumentException("Trying to parse [" + this.getClass().getName()
					+ "] : expected no parameters, found [" + parameters + "].");
		}
	}

	@Override
	public void render(RenderDevice renderDevice, RenderImage image, Box sourceArea, int x, int y, int width,
			int height, Color color, float scale) {
		final int centerX = x + width / 2;
		final int centerY = y + height / 2;

		renderDevice.renderImage(image, x, y, width, height, sourceArea.getX(), sourceArea.getY(),
				sourceArea.getWidth(), sourceArea.getHeight(), color, scale, centerX, centerY);
	}
}
