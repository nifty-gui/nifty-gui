package de.lessvoid.nifty.renderer.jogl.render;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Nonnull;
import javax.media.nativewindow.util.Dimension;
import javax.media.nativewindow.util.DimensionImmutable;
import javax.media.nativewindow.util.PixelFormat;
import javax.media.nativewindow.util.PixelRectangle;

import com.jogamp.common.nio.Buffers;
import com.jogamp.newt.Display.PointerIcon;
import com.jogamp.newt.Window;

import de.lessvoid.nifty.render.io.ImageLoader;
import de.lessvoid.nifty.render.io.ImageLoaderFactory;
import de.lessvoid.nifty.spi.render.MouseCursor;
import de.lessvoid.nifty.tools.resourceloader.NiftyResourceLoader;

public class JoglMouseCursor implements MouseCursor {
	@Nonnull
	private static final Logger log = Logger.getLogger(JoglMouseCursor.class.getName());
	@Nonnull
	private final PointerIcon joglCursor;

	private Window newtWindow;

	public JoglMouseCursor(
			@Nonnull final String filename,
			final int hotspotX,
			final int hotspotY,
			@Nonnull final Window newtWindow,
			@Nonnull final NiftyResourceLoader resourceLoader) throws IOException {
		this.newtWindow = newtWindow;
		ImageLoader imageLoader = ImageLoaderFactory.createImageLoader(filename);
		InputStream imageStream = resourceLoader.getResourceAsStream(filename);
		if (imageStream == null) {
			throw new IOException("Cannot find / load mouse cursor image file: [" + filename + "].");
		}
		try {
			BufferedImage image = imageLoader.loadAsBufferedImage(imageStream);
			final DimensionImmutable size = new Dimension(image.getWidth(), image.getHeight());

			// grab pixel data from BufferedImage
			int[] pixels = new int[image.getWidth() * image.getHeight()];
			image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());
			flipArray(pixels); // flip image data to display correctly in OpenGL
			final IntBuffer pixelIntBuff = Buffers.newDirectIntBuffer(pixels);
			final ByteBuffer pixelBuff = Buffers.copyIntBufferAsByteBuffer(pixelIntBuff);

			// find compatible PixelFormat
			PixelFormat pixFormat = null;
			for (final PixelFormat pf : PixelFormat.values()) {
				if (pf.componentCount == image.getColorModel().getNumComponents()
						&& pf.bytesPerPixel() == image.getColorModel().getPixelSize() / 8) { // divide by 8 for bits -> bytes
					pixFormat = pf;
					break;
				}
			}

			final PixelRectangle.GenericPixelRect rec = new PixelRectangle.GenericPixelRect(pixFormat, size, 0, true,
					pixelBuff);
			joglCursor = newtWindow.getScreen().getDisplay().createPointerIcon(rec, hotspotX, hotspotY);
		} catch (Exception e) {
			throw(new RuntimeException(e));
		} finally {
			try {
				imageStream.close();
			} catch (IOException e) {
				log.log(Level.INFO, "An error occurred while closing the InputStream used to load mouse cursor image: " +
						"[" + filename + "].", e);
			}
		}
	}

	@Override
	public void enable() {
		newtWindow.setPointerIcon(joglCursor);
	}

	@Override
	public void disable() {
		newtWindow.setPointerIcon(null); // reset to default pointer icon
	}

	@Override
	public void dispose() {
		joglCursor.destroy();
	}

	public void setCurrentWindow(final Window newtWindow) {
		if (newtWindow == null)
			return;
		this.newtWindow = newtWindow;
	}

	// reverses the order of the passed array so that head -> tail becomes tail -> head
	private static void flipArray(int[] array) {
		if (array == null) {
			throw (new NullPointerException("passed array is of null value"));
		}
		int[] copy = Arrays.copyOf(array, array.length);
		int inv = 0;
		for (int i = array.length - 1; i >= 0; i--) {
			array[i] = copy[inv];
			inv++;
		}
	}
}
