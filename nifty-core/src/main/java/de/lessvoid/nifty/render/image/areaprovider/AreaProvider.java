package de.lessvoid.nifty.render.image.areaprovider;

import de.lessvoid.nifty.Parameterizable;
import de.lessvoid.nifty.Size;
import de.lessvoid.nifty.layout.Box;
import de.lessvoid.nifty.render.NiftyImage;
import de.lessvoid.nifty.spi.render.RenderImage;

public interface AreaProvider extends Parameterizable {
	Box getSourceArea(RenderImage renderImage);

	Size getNativeSize(NiftyImage image);
}
