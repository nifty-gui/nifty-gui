package de.lessvoid.nifty.renderer.jogl.render;

import com.jogamp.common.nio.Buffers;
import de.lessvoid.nifty.renderer.jogl.render.io.ImageData;
import de.lessvoid.nifty.renderer.jogl.render.io.ImageIOImageData;
import de.lessvoid.nifty.renderer.jogl.render.io.TGAImageData;
import de.lessvoid.nifty.spi.render.RenderImage;
import de.lessvoid.nifty.tools.resourceloader.NiftyResourceLoader;

import javax.annotation.Nonnull;
import javax.media.opengl.GL;
import javax.media.opengl.GLContext;
import javax.media.opengl.glu.GLU;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.logging.Logger;

/**
 * @author Julien Gouesse
 */
public class JoglRenderImage implements RenderImage {

  private final Logger log = Logger.getLogger(JoglRenderImage.class.getName());

  private int width;

  private int height;

  private int textureWidth;

  private int textureHeight;

  private int textureId;

  private GLU glu;

  public JoglRenderImage(
      @Nonnull final String name,
      final boolean filterParam,
      @Nonnull final NiftyResourceLoader resourceLoader) {
    InputStream in = null;
    try {
      log.fine("loading image: " + name);
      glu = new GLU();
      ImageData imageLoader;
      if (name.endsWith(".tga")) {
        imageLoader = new TGAImageData();
      } else {
        imageLoader = new ImageIOImageData();
      }
      in = resourceLoader.getResourceAsStream(name);
      if (in == null) {
        return;
      }
      ByteBuffer imageData = imageLoader.loadImage(in);
      imageData.rewind();
      width = imageLoader.getWidth();
      height = imageLoader.getHeight();
      textureWidth = imageLoader.getTexWidth();
      textureHeight = imageLoader.getTexHeight();
      createTexture(imageData, textureWidth, textureHeight, filterParam ? GL.GL_LINEAR : GL.GL_NEAREST,
          imageLoader.getDepth() == 32 ? GL.GL_RGBA : GL.GL_RGB);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (in != null) {
        try {
          in.close();
        } catch (IOException ignored) {
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
    // do nothing
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
