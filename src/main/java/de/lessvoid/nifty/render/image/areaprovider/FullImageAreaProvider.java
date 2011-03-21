package de.lessvoid.nifty.render.image.areaprovider;

import de.lessvoid.nifty.Size;
import de.lessvoid.nifty.layout.Box;
import de.lessvoid.nifty.render.NiftyImage;
import de.lessvoid.nifty.spi.render.RenderImage;

public class FullImageAreaProvider implements AreaProvider {

	@Override
	public void setParameters(String parameters) {
		if (parameters != null) {
			throw new IllegalArgumentException("Trying to parse [" + this.getClass().getName()
					+ "] : expected no parameters, found [" + parameters + "].");
		}
	}

	@Override
	public Box getSourceArea(RenderImage renderImage) {
		return new Box(0, 0, renderImage.getWidth(), renderImage.getHeight());
	}

	@Override
	public Size getNativeSize(NiftyImage image) {
		return new Size(image.getWidth(), image.getHeight());
	}
}
