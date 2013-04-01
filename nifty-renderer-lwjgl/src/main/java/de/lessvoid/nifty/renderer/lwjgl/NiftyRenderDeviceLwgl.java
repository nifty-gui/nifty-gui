package de.lessvoid.nifty.renderer.lwjgl;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;

import java.nio.FloatBuffer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;

import de.lessvoid.coregl.CoreFBO;
import de.lessvoid.coregl.CoreMatrixFactory;
import de.lessvoid.coregl.CoreRender;
import de.lessvoid.coregl.CoreShader;
import de.lessvoid.coregl.CoreTexture2D;
import de.lessvoid.coregl.CoreTexture2D.ColorFormat;
import de.lessvoid.coregl.CoreTexture2D.ResizeFilter;
import de.lessvoid.coregl.CoreVAO;
import de.lessvoid.coregl.CoreVBO;
import de.lessvoid.nifty.spi.NiftyCanvas;
import de.lessvoid.nifty.spi.NiftyRenderDevice;
import de.lessvoid.textureatlas.TextureAtlasGenerator;

public class NiftyRenderDeviceLwgl implements NiftyRenderDevice {
  private final CoreShader shader;
  private final CoreVAO vao;
  private final CoreVBO vbo;
  private final CoreFBO fbo;
private boolean hack = true;
  private final Matrix4f mvp;
  private int quadCount;
  private CoreTexture2D textureArray;

  public NiftyRenderDeviceLwgl() {
    mvp = CoreMatrixFactory.createOrtho(0, getWidth(), getHeight(), 0);

    shader = CoreShader.newShaderWithVertexAttributes("aVertex", "aUVL");
    shader.vertexShader("de/lessvoid/nifty/renderer/lwjgl/texture.vs");
    shader.fragmentShader("de/lessvoid/nifty/renderer/lwjgl/texture.fs");
    shader.link();
    shader.activate();
    shader.setUniformi("uTexture", 0);

    this.fbo = new CoreFBO();
    this.fbo.bindFramebuffer();

    vao = new CoreVAO();
    vao.bind();

    int xCount = getWidth() / 256;
    int yCount = getHeight() / 256;

    quadCount = yCount*xCount;
    vbo = CoreVBO.createStream(new float[5*6*quadCount]);
    vbo.bind();
    FloatBuffer buffer = vbo.getBuffer();
    for (int y=0; y<yCount; y++) {
      for (int x=0; x<xCount; x++) {
        addQuad(buffer, x*256, y*256, y*xCount + x);
      }
    }
    buffer.flip();
    vbo.send();

    vao.enableVertexAttributef(0, 2, 5, 0);
    vao.enableVertexAttributef(1, 3, 5, 2);

    vao.unbind();
  }

  private void addQuad(final FloatBuffer buffer, final int x, final int y, final int texIdx) {
    // first
    buffer.put(x);
    buffer.put(y);
    buffer.put(0.0f);
    buffer.put(0.0f);
    buffer.put(texIdx);

    buffer.put(x);
    buffer.put(y + 250);
    buffer.put(0.0f);
    buffer.put(1.0f);
    buffer.put(texIdx);

    buffer.put(x + 250);
    buffer.put(y);
    buffer.put(1.0f);
    buffer.put(0.0f);
    buffer.put(texIdx);

    // second
    buffer.put(x);
    buffer.put(y + 250);
    buffer.put(0.0f);
    buffer.put(1.0f);
    buffer.put(texIdx);

    buffer.put(x + 250);
    buffer.put(y);
    buffer.put(1.0f);
    buffer.put(0.0f);
    buffer.put(texIdx);

    buffer.put(x + 250);
    buffer.put(y + 250);
    buffer.put(1.0f);
    buffer.put(1.0f);
    buffer.put(texIdx);
  }

  @Override
  public int getWidth() {
   return 1024;
  }

  @Override
  public int getHeight() {
    return 768;
  }

  @Override
  public void createRenderTargets(final int width, final int height, final int num) {
    textureArray = CoreTexture2D.createEmptyTextureArray(ColorFormat.RGBA, GL11.GL_UNSIGNED_BYTE, width, height, num, ResizeFilter.Linear);

    fbo.bindFramebuffer(256, 256);
    for (int i=0; i<num; i++) {
      fbo.attachTexture(textureArray.getTextureId(), 0, i);
      glClearColor((float)Math.random(), (float)Math.random(), (float)Math.random(), 1.f);
      glClear(GL_COLOR_BUFFER_BIT);

    }
    fbo.disableAndResetViewport();
  }

  @Override
  public void render() {
    shader.activate();
    shader.setUniformMatrix4f("uMvp", mvp);

    vao.bind();
    textureArray.bind();
    CoreRender.renderTriangles(6*quadCount);
    vao.unbind();
  }
}
