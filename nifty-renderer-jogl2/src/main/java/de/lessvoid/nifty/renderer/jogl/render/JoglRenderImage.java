package de.lessvoid.nifty.renderer.jogl.render;

import com.jogamp.common.nio.Buffers;

import de.lessvoid.nifty.render.io.ImageLoader;
import de.lessvoid.nifty.render.io.ImageLoaderFactory;
import de.lessvoid.nifty.spi.render.RenderImage;
import de.lessvoid.nifty.tools.resourceloader.NiftyResourceLoader;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nonnull;
import javax.media.opengl.GL;
import javax.media.opengl.GLContext;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.gl2.GLUgl2;

/**
 * @author Julien Gouesse
 */
public class JoglRenderImage implements RenderImage {
  @Nonnull
  private final Logger log = Logger.getLogger(JoglRenderImage.class.getName());
  @Nonnull
  private final GLU glu;
  private int width;
  private int height;
  private int textureWidth;
  private int textureHeight;
  private int textureId;

  @Nonnull
  public JoglRenderImage (
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
        createTexture(image, textureWidth, textureHeight, filterParam ? GL.GL_LINEAR : GL.GL_NEAREST,
                loader.getImageBitDepth() == 32 ? GL.GL_RGBA : GL.GL_RGB);
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
    glu = new GLUgl2();//FIXME rather call GLU.createGLU(GLContext.getCurrentGL()); but ensure there is a current OpenGL context on this thread
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
  }

  private void createTexture(
      final ByteBuffer textureBuffer, final int width, final int height,
      final int filter, final int srcPixelFormat) throws Exception {
    final GL gl = GLContext.getCurrentGL();
    textureId = createTextureID();
    bind();

    int componentCount = 1;

    IntBuffer temp = Buffers.newDirectIntBuffer(16);
    gl.glGetIntegerv(GL.GL_MAX_TEXTURE_SIZE, temp);
    checkGLError();

    int max = temp.get(0);
    if ((width > max) || (height > max)) {
      throw new Exception("Attempt to allocate a texture to big for the current hardware");
    }

    gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, filter);
    gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, filter);
    checkGLError();

    if (filter == GL.GL_LINEAR_MIPMAP_NEAREST || filter == GL.GL_LINEAR_MIPMAP_LINEAR) {
      glu.gluBuild2DMipmaps(GL.GL_TEXTURE_2D, componentCount, width, height, srcPixelFormat,
          GL.GL_UNSIGNED_BYTE, textureBuffer);
    } else {
      gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, 4, width, height, 0, srcPixelFormat,
          GL.GL_UNSIGNED_BYTE, textureBuffer);
    }
    checkGLError();
  }

  private int createTextureID() {
    IntBuffer tmp = createIntBuffer(1);
    final GL gl = GLContext.getCurrentGL();
    gl.glGenTextures(1, tmp);
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
    final GL gl = GLContext.getCurrentGL();
    gl.glBindTexture(GL.GL_TEXTURE_2D, textureId);
    checkGLError();
  }

  private void checkGLError() {
    final GL gl = GLContext.getCurrentGL();
    int error = gl.glGetError();
    if (error != GL.GL_NO_ERROR) {
      String glerrmsg = glu.gluErrorString(error);
      log.warning("OpenGL Error: (" + error + ") " + glerrmsg);
    }
  }
}
