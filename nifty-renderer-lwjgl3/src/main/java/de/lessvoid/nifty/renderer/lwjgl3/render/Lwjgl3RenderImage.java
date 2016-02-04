package de.lessvoid.nifty.renderer.lwjgl3.render;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Nonnull;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GLUtil;

import de.lessvoid.nifty.render.io.ImageLoader;
import de.lessvoid.nifty.render.io.ImageLoaderFactory;
import de.lessvoid.nifty.spi.render.RenderImage;
import de.lessvoid.nifty.tools.resourceloader.NiftyResourceLoader;

public class Lwjgl3RenderImage implements RenderImage {
  @Nonnull
  private static final Logger log = Logger.getLogger(Lwjgl3RenderImage.class.getName());
  private int width;
  private int height;
  private int textureWidth;
  private int textureHeight;
  private int textureId;

  @Nonnull
  public Lwjgl3RenderImage (
          @Nonnull String filename,
          final boolean filterParam,
          @Nonnull final NiftyResourceLoader resourceLoader) {
    log.fine("Loading image: " + filename);
    ImageLoader loader = ImageLoaderFactory.createImageLoader(filename);
    InputStream imageStream = null;
    try {
      imageStream = resourceLoader.getResourceAsStream(filename);
      if (imageStream != null) {
        ByteBuffer image = loader.loadAsByteBufferRGBA(imageStream);
        image.rewind();
        width = loader.getImageWidth();
        height = loader.getImageHeight();
        textureWidth = loader.getTextureWidth();
        textureHeight = loader.getTextureHeight();
        createTexture(image, textureWidth, textureHeight, filterParam, loader.getImageBitDepth() == 32 ? GL11.GL_RGBA :
                GL11.GL_RGB);
      }
    } catch (Exception e) {
      log.log(Level.WARNING, "Could not load image from file: [" + filename + "]", e);
    } finally {
      if (imageStream != null) {
        try {
          imageStream.close();
        } catch (IOException e) {
          log.log(Level.INFO, "An error occurred while closing the InputStream used to load image: " + "[" + filename +
                  "].", e);
        }
      }
    }
  }

  @Override
  public int getWidth() {
    return width;
  }

  @Override
  public int getHeight() {
    return height;
  }

  public int getTextureWidth() {
    return textureWidth;
  }

  public int getTextureHeight() {
    return textureHeight;
  }

  @Override
  public void dispose() {
    GL11.glDeleteTextures(textureId);
    checkGLError();
  }

  private void createTexture(
      final ByteBuffer textureBuffer,
      final int width,
      final int height,
      final boolean filter,
      final int srcPixelFormat) throws Exception {
    textureId = createTextureID();
    int minFilter = GL11.GL_NEAREST;
    int magFilter = GL11.GL_NEAREST;
    if (filter) {
      minFilter = GL11.GL_LINEAR_MIPMAP_LINEAR;
      magFilter = GL11.GL_NEAREST;
    }
    bind();

    IntBuffer temp = BufferUtils.createIntBuffer(16);
    GL11.glGetIntegerv(GL11.GL_MAX_TEXTURE_SIZE, temp);
    checkGLError();

    int max = temp.get(0);
    if ((width > max) || (height > max)) {
      throw new Exception("Attempt to allocate a texture to big for the current hardware");
    }
    if (width < 0) {
      log.warning("Attempt to allocate a texture with negative width");
      return;
    }
    if (height < 0) {
      log.warning("Attempt to allocate a texture with negative height");
      return;
    }

    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, minFilter);
    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, magFilter);
    checkGLError();

    GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, 4, width, height, 0, srcPixelFormat, GL11.GL_UNSIGNED_BYTE, textureBuffer);

    if (minFilter == GL11.GL_LINEAR_MIPMAP_LINEAR) {
      // FIXME: not compatible with GL versions < 3.0!!
      GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
    }

    checkGLError();
  }

  private int createTextureID() {
    IntBuffer tmp = createIntBuffer(1);
    GL11.glGenTextures(tmp);
    checkGLError();
    return tmp.get(0);
  }

  @Nonnull
  private IntBuffer createIntBuffer(final int size) {
    ByteBuffer temp = ByteBuffer.allocateDirect(4 * size);
    temp.order(ByteOrder.nativeOrder());
    return temp.asIntBuffer();
  }

  public void bind() {
    GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);
    checkGLError();
  }

  private void checkGLError() {
    int error = GL11.glGetError();
    if (error != GL11.GL_NO_ERROR) {
      String glerrmsg = GLUtil.getErrorString(error);
      log.warning("OpenGL Error: (" + error + ") " + glerrmsg);
      try {
        throw new Exception();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
}
