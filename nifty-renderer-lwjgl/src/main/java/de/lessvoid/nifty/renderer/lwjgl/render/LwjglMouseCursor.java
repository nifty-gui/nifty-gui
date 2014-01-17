package de.lessvoid.nifty.renderer.lwjgl.render;

import de.lessvoid.nifty.render.io.ImageLoader;
import de.lessvoid.nifty.render.io.ImageLoaderFactory;
import de.lessvoid.nifty.spi.render.MouseCursor;
import de.lessvoid.nifty.tools.resourceloader.NiftyResourceLoader;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nonnull;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Cursor;
import org.lwjgl.input.Mouse;

public class LwjglMouseCursor implements MouseCursor {
  @Nonnull
  private static final Logger log = Logger.getLogger(LwjglMouseCursor.class.getName());
  @Nonnull
  private final Cursor cursor;

  public LwjglMouseCursor(
          @Nonnull final String cursorImageFilename,
          final int hotspotX,
          final int hotspotY,
          @Nonnull final NiftyResourceLoader resourceLoader) throws IOException {
    ImageLoader imageLoader = ImageLoaderFactory.createImageLoader(cursorImageFilename);
    InputStream imageStream = resourceLoader.getResourceAsStream(cursorImageFilename);
    if (imageStream == null) {
      throw new IOException("Cannot find / load mouse cursor image file: [" + cursorImageFilename + "].");
    }
    try {
      ByteBuffer imageData = imageLoader.loadAsByteBufferARGB(imageStream, true);
      imageData.rewind();
      int width = imageLoader.getImageWidth();
      int height = imageLoader.getImageHeight();
      cursor = new Cursor(width, height, hotspotX, height - hotspotY - 1, 1, imageData.asIntBuffer(), null);
    } catch (LWJGLException e) {
      throw new IOException(e);
    } finally {
      try {
        imageStream.close();
      } catch (IOException e) {
        log.log(Level.INFO, "An error occurred while closing the InputStream used to load mouse cursor image: " +
                "[" + cursorImageFilename + "].", e);
      }
    }
  }

  @Override
  public void dispose() {
    cursor.destroy();
  }

  @Override
  public void enable() {
    try {
      Mouse.setNativeCursor(cursor);
    } catch (LWJGLException e) {
      log.warning(e.getMessage());
    }
  }

  @Override
  public void disable() {
    try {
      Mouse.setNativeCursor(null);
    } catch (LWJGLException e) {
      log.warning(e.getMessage());
    }
  }
}
