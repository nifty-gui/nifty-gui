package de.lessvoid.nifty.render.image.renderstrategy;

import de.lessvoid.nifty.Parameterizable;
import de.lessvoid.nifty.layout.Box;
import de.lessvoid.nifty.spi.render.RenderDevice;
import de.lessvoid.nifty.spi.render.RenderImage;
import de.lessvoid.nifty.tools.Color;

public interface RenderStrategy extends Parameterizable {
	void render(final RenderDevice renderDevice, final RenderImage image, Box sourceArea, final int x,
			final int y, final int width, final int height, final Color color, final float scale);
}
