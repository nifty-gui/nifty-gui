package de.lessvoid.nifty.render;

import de.lessvoid.nifty.Size;
import de.lessvoid.nifty.render.image.ImageMode;
import de.lessvoid.nifty.render.image.ImageModeFactory;
import de.lessvoid.nifty.render.image.ImageModeHelper;
import de.lessvoid.nifty.spi.render.RenderDevice;
import de.lessvoid.nifty.spi.render.RenderImage;
import de.lessvoid.nifty.tools.Color;

/**
 * RenderImageMode supports the following modes.
 *
 * - "normal" (render the given image to the given image size)
 * - "subImage:" (render the given subImage of the image to the given size)
 * - "resize:" (special mode that allows scaling of bitmaps with a predefined schema)
 * - "repeat:" (special mode that repeating of texture)
 *
 * @author void
 * @deprecated use {@link ImageModeFactory} class instead to create {@link ImageMode} instances.
 */
@Deprecated
public class NiftyImageMode implements ImageMode {

	private ImageMode m_imageMode;

	private NiftyImageMode(ImageMode imageMode) {
		m_imageMode = imageMode;
	}

	/**
	 * Render image.
	 * @param renderImage RenderImage
	 * @param x x
	 * @param y y
	 * @param width width
	 * @param height height
	 * @param color color
	 * @param scale scale
	 */
	public void render(final RenderDevice renderDevice, final RenderImage renderImage, final int x, final int y,
			final int width, final int height, final Color color, final float scale) {
		m_imageMode.render(renderDevice, renderImage, x, y, width, height, color, scale);
	}

	@Override
	public void setParameters(String parameters) {
		m_imageMode.setParameters(parameters);
	}

	@Override
	public Size getImageNativeSize(NiftyImage image) {
		return m_imageMode.getImageNativeSize(image);
	}

	/**
	 * create a RenderImageMode from the given String.
	 * @param imageMode imageMode String
	 * @return a RenderImageMode
	 */
	public static NiftyImageMode valueOf(final String imageMode) {
		String areaProviderProperty = ImageModeHelper.getAreaProviderProperty(imageMode);
		String renderStrategyProperty = ImageModeHelper.getRenderStrategyProperty(imageMode);

		return new NiftyImageMode(ImageModeFactory.getSharedInstance().createImageMode(areaProviderProperty,
				renderStrategyProperty));
	}

	/**
	 * normal rendering.
	 * @return RenderImageMode for NORMAL mode
	 */
	public static NiftyImageMode normal() {
		return new NiftyImageMode(valueOf("normal"));
	}

	/**
	 * scale a sub image.
	 * @param x x
	 * @param y y
	 * @param w w
	 * @param h h
	 * @return RenderImageMode for SUBIMAGE mode
	 */
	public static NiftyImageMode subImage(final int x, final int y, final int w, final int h) {
		return new NiftyImageMode(valueOf("subImage:" + x + "px," + y + "px," + w + "px," + h + "px"));
	}
}
