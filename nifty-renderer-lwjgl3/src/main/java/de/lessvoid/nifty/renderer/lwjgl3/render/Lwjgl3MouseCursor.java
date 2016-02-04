package de.lessvoid.nifty.renderer.lwjgl3.render;

import static org.lwjgl.glfw.GLFW.*;

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

import org.lwjgl.glfw.GLFWImage;

public class Lwjgl3MouseCursor implements MouseCursor {
  @Nonnull
  private static final Logger log = Logger.getLogger(Lwjgl3MouseCursor.class.getName());
  private final long glfwWindow;
  private final long cursor;

  public Lwjgl3MouseCursor(final long glfwWindow, @Nonnull final String cursorImageFilename, final int hotspotX,
      final int hotspotY, @Nonnull final NiftyResourceLoader resourceLoader) throws IOException {
    this.glfwWindow = glfwWindow;
    ImageLoader imageLoader = ImageLoaderFactory.createImageLoader(cursorImageFilename);
    InputStream imageStream = resourceLoader.getResourceAsStream(cursorImageFilename);
    if (imageStream == null) {
      throw new IOException("Cannot find / load mouse cursor image file: [" + cursorImageFilename + "].");
    }
    try {
      ByteBuffer imageData = imageLoader.loadAsByteBufferARGB(imageStream, true);
      imageData.rewind();
      GLFWImage image = new GLFWImage(imageData);
      cursor = glfwCreateCursor(image, hotspotX, hotspotY);
    } finally {
      try {
        imageStream.close();
      } catch (IOException e) {
        log.log(Level.INFO, "An error occurred while closing the InputStream used to load mouse cursor image: " + "["
            + cursorImageFilename + "].", e);
      }
    }
  }

  @Override
  public void dispose() {
    glfwDestroyCursor(cursor);
  }

  @Override
  public void enable() {
    glfwSetCursor(glfwWindow, cursor);
  }

  @Override
  public void disable() {
    glfwSetCursor(glfwWindow, GLFW_CURSOR_NORMAL);
  }
}
