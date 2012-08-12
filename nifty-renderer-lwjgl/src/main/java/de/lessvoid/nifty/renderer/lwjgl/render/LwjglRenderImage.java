package de.lessvoid.nifty.renderer.lwjgl.render;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.logging.Logger;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import de.lessvoid.nifty.renderer.lwjgl.render.io.ImageData;
import de.lessvoid.nifty.renderer.lwjgl.render.io.ImageIOImageData;
import de.lessvoid.nifty.renderer.lwjgl.render.io.TGAImageData;
import de.lessvoid.nifty.spi.render.RenderImage;
import de.lessvoid.nifty.tools.resourceloader.NiftyResourceLoader;

public class LwjglRenderImage implements RenderImage {
  private static Logger log = Logger.getLogger(LwjglRenderImage.class.getName());

  private int width;
  private int height;
  private int textureWidth;
  private int textureHeight;
  private int textureId;

  public LwjglRenderImage(final String name, final boolean filterParam, final NiftyResourceLoader resourceLoader) {
    try {
      log.fine("loading image: " + name);
      ImageData imageLoader;
      if (name.endsWith(".tga")) {
        imageLoader = new TGAImageData();
      } else {
        imageLoader = new ImageIOImageData();
      }
      ByteBuffer imageData = imageLoader.loadImage(resourceLoader.getResourceAsStream(name));
      imageData.rewind();
      width = imageLoader.getWidth();
      height = imageLoader.getHeight();
      textureWidth = imageLoader.getTexWidth();
      textureHeight = imageLoader.getTexHeight();
      createTexture(imageData, textureWidth, textureHeight, filterParam, imageLoader.getDepth() == 32 ? GL11.GL_RGBA : GL11.GL_RGB);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public int getWidth() {
    return width;
  }

  public int getHeight() {
    return height;
  }

  public int getTextureWidth() {
    return textureWidth;
  }

  public int getTextureHeight() {
    return textureHeight;
  }

  public void dispose() {
    GL11.glDeleteTextures(textureId);
    checkGLError();
  }  

  private void createTexture(final ByteBuffer textureBuffer, final int width, final int height, final boolean filter, final int srcPixelFormat) throws Exception {
    textureId = createTextureID();
    int minFilter = GL11.GL_NEAREST;
    int magFilter = GL11.GL_NEAREST;
    if (filter) {
      minFilter = GL11.GL_LINEAR_MIPMAP_LINEAR;
      magFilter = GL11.GL_NEAREST;
    }
    bind();

    IntBuffer temp = BufferUtils.createIntBuffer(16);
    GL11.glGetInteger(GL11.GL_MAX_TEXTURE_SIZE, temp);
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

    if (minFilter == GL11.GL_LINEAR_MIPMAP_NEAREST ||
        minFilter == GL11.GL_LINEAR_MIPMAP_LINEAR) {
      GLU.gluBuild2DMipmaps(
          GL11.GL_TEXTURE_2D,
          4, 
          width,
          height,
          srcPixelFormat, 
          GL11.GL_UNSIGNED_BYTE,
          textureBuffer);
    } else {
      GL11.glTexImage2D(
          GL11.GL_TEXTURE_2D, 
          0,
          4, 
          width, 
          height, 
          0, 
          srcPixelFormat, 
          GL11.GL_UNSIGNED_BYTE, 
          textureBuffer);
    }
    checkGLError();
  }
  
  private int createTextureID() { 
     IntBuffer tmp = createIntBuffer(1); 
     GL11.glGenTextures(tmp);
     checkGLError();
     return tmp.get(0);
  }

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
    int error= GL11.glGetError();
    if (error != GL11.GL_NO_ERROR) {
      String glerrmsg = GLU.gluErrorString(error);
      log.warning("OpenGL Error: (" + error + ") " + glerrmsg);
      try {
        throw new Exception();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
}
