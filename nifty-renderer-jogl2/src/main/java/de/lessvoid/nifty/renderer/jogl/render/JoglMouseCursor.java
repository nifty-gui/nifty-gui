package de.lessvoid.nifty.renderer.jogl.render;

import de.lessvoid.nifty.render.io.ImageLoader;
import de.lessvoid.nifty.render.io.ImageLoaderFactory;
import de.lessvoid.nifty.spi.render.MouseCursor;
import de.lessvoid.nifty.tools.resourceloader.NiftyResourceLoader;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nonnull;

public class JoglMouseCursor implements MouseCursor {
  @Nonnull
  private static final Logger log = Logger.getLogger(JoglMouseCursor.class.getName());
  @Nonnull
  private final Cursor cursor;

  public JoglMouseCursor(
          @Nonnull final String filename,
          final int hotspotX,
          final int hotspotY,
          @Nonnull final NiftyResourceLoader resourceLoader) throws IOException {
    ImageLoader imageLoader = ImageLoaderFactory.createImageLoader(filename);
    InputStream imageStream = resourceLoader.getResourceAsStream(filename);
    if (imageStream == null) {
      throw new IOException("Cannot find / load mouse cursor image file: [" + filename + "].");
    }
    try {
      BufferedImage image = imageLoader.loadMouseCursorImageAsBufferedImage(imageStream);
      cursor = Toolkit.getDefaultToolkit().createCustomCursor(image, new Point(hotspotX, hotspotY), filename);
    } catch (Exception e) {
      throw new IOException(e);
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
    // FIXME: Changing the mouse cursor is only supported on a java.awt.Frame, not a com.jogamp.newt.opengl.GLWindow,
    // which, unfortunately, is what is currently being used for Nifty JOGL rendering. Is there any way to change this?
    // You can however, use GLWindow#setPointerVisible(false) to hide the system cursor, and could then write a nasty
    // hack that renders an image that moves with the mouse position, rather than directly changing the mouse cursor
    // image. Might be better than nothing. Otherwise, try using a java.awt.Frame for rendering...
  }

  @Override
  public void disable() {
    // FIXME: Changing the mouse cursor is only supported on a java.awt.Frame, not a com.jogamp.newt.opengl.GLWindow,
    // which, unfortunately, is what is currently being used for Nifty JOGL rendering. Is there any way to change this?
    // You can however, use GLWindow#setPointerVisible(false) to hide the system cursor, and could then write a nasty
    // hack that renders an image that moves with the mouse position, rather than directly changing the mouse cursor
    // image. Might be better than nothing. Otherwise, try using a java.awt.Frame for rendering...
  }

  @Override
  public void dispose() {
  }
}
