package de.lessvoid.nifty.renderer.lwjgl.render;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.logging.Logger;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import de.lessvoid.nifty.renderer.lwjgl.render.io.ImageLoader;
import de.lessvoid.nifty.spi.render.RenderImage;
import de.lessvoid.nifty.tools.resourceloader.ResourceLoader;

public class LwjglRenderImage implements RenderImage {
  private Logger log = Logger.getLogger(LwjglRenderImage.class.getName());

  private int width;
  private int height;
  private int textureWidth;
  private int textureHeight;
  private int textureId;

  public LwjglRenderImage(final String name, final boolean filterParam) {
    try {
      log.fine("loading image: " + name);
      ImageLoader imageLoader = new ImageLoader(name, ResourceLoader.getResourceAsStream(name));
      ByteBuffer imageData = imageLoader.getImageData();
      imageData.rewind();
      width = imageLoader.getWidth();
      height = imageLoader.getHeight();
      textureWidth = imageLoader.getTextureWidth();
      textureHeight = imageLoader.getTextureHeight();
      createTexture(imageData, textureWidth, textureHeight, 0, GL11.GL_RGBA);
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

  private void createTexture(final ByteBuffer textureBuffer, final int width, final int height, final int filter, final int srcPixelFormat) throws Exception {
    textureId = createTextureID(); 
    int minFilter = GL11.GL_NEAREST;
    int magFilter = GL11.GL_NEAREST;
    bind();

    int componentCount = 1;
     
    IntBuffer temp = BufferUtils.createIntBuffer(16);
    GL11.glGetInteger(GL11.GL_MAX_TEXTURE_SIZE, temp);
    checkGLError();

    int max = temp.get(0);
    if ((width > max) || (height > max)) {
      throw new Exception("Attempt to allocate a texture to big for the current hardware");
    }

    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, minFilter); 
    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, magFilter); 
    checkGLError();

    if (minFilter == GL11.GL_LINEAR_MIPMAP_NEAREST) {
      GLU.gluBuild2DMipmaps(
          GL11.GL_TEXTURE_2D,
          componentCount, 
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
    }
  }
}
