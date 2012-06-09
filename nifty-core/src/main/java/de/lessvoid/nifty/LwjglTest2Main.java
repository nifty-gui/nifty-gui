package de.lessvoid.nifty;

import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;

import org.lwjgl.opengl.GL11;

import de.lessvoid.coregl.MatrixFactory;
import de.lessvoid.coregl.Render;
import de.lessvoid.coregl.Shader;
import de.lessvoid.coregl.Texture;
import de.lessvoid.coregl.VA;
import de.lessvoid.coregl.VBO;
import de.lessvoid.imageloader.ImageData;
import de.lessvoid.imageloader.ImageLoader;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.render.RenderBuffer;
import de.lessvoid.resourceloader.ResourceLoader;

public class LwjglTest2Main {
  public static void main(final String[] args) throws Exception {
    final LwjglRunner runner = new LwjglRunner();
    runner.initialize("Nifty 2.0 Demo", 1024, 768);

    ResourceLoader resourceLoader = new ResourceLoader();

    ImageLoader loader = new ImageLoader();
    ImageData imageData = loader.load("nifty-logo-150x150.png", resourceLoader.getResourceAsStream("nifty-logo-150x150.png"));
    Texture texture = new Texture(true, imageData.getWidth(), imageData.getHeight(), imageData.getTexWidth(), imageData.getTexHeight(), imageData.getDepth(), imageData.getData());

    glActiveTexture(GL_TEXTURE0);
    texture.bind();

    final Shader shader = new Shader(
        "vVertex",
        "vTexCoords",
        "instanceColor",
        "instanceTransform1",
        "instanceTransform2",
        "instanceTransform3",
        "instanceTransform4");
    shader.compile("nifty.vs", "nifty.fs");
    shader.activate();
    shader.setUniform("mProjection", MatrixFactory.createProjection(1024, 768));
    shader.setUniform("tex", 0);

    final VA va = new VA();
    va.bind();

    final VBO quad = VBO.createStatic(new float[] {
        0.f,       0.f,       0.f, 0.f,
        0.f + 1.f, 0.f,       1.f, 0.f,
        0.f,       0.f + 1.f, 0.f, 1.f,
        0.f + 1.f, 0.f + 1.f, 1.f, 1.f,
    });

    va.enableVertexAttributef(0, 2, 4, 0);
    va.enableVertexAttributef(1, 2, 4, 2);

    RenderBuffer renderBuffer = new RenderBuffer(2000, 4 + 16);

    va.enableVertexAttributeDivisorf(2, 4, 20, 0, 1); // color
    va.enableVertexAttributeDivisorf(3, 4, 20, 4, 1); // transform
    va.enableVertexAttributeDivisorf(4, 4, 20, 8, 1);
    va.enableVertexAttributeDivisorf(5, 4, 20, 12, 1);
    va.enableVertexAttributeDivisorf(6, 4, 20, 16, 1);

    Element root = new Element();
    for (int i=0; i<100; i++) {
      root.addElement(new Element());
    }
    root.update();

    runner.renderLoop(new RenderAction(shader, root, renderBuffer));
    runner.destroy();
  }

  private static class RenderAction implements LwjglRunner.RenderLoopCallback {
    private Shader shader;
    private Element root;
    private RenderBuffer renderBuffer;
    private boolean first = true;
    private int count;

    public RenderAction(final Shader shader, final Element root, final RenderBuffer renderBuffer) {
      this.shader = shader;
      this.root = root;
      this.renderBuffer = renderBuffer;
      this.count = 0;
    }

    @Override
    public boolean process() {
      GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

      root.update();
      renderBuffer.begin();
      root.render(renderBuffer);
      count = renderBuffer.commit();

      Render.renderInstances(count);

      return false;
    }
  }
}
