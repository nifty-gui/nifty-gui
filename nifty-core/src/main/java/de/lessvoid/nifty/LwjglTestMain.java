package de.lessvoid.nifty;

import static org.lwjgl.opengl.ARBInstancedArrays.glVertexAttribDivisorARB;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;

import java.nio.FloatBuffer;
import java.util.Random;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import de.lessvoid.coregl.CheckGL;
import de.lessvoid.coregl.Render;
import de.lessvoid.coregl.Shader;
import de.lessvoid.coregl.VA;
import de.lessvoid.coregl.VBO;

public class LwjglTestMain {
  static float left = 0;
  static float right = 1024;
  static float top = 0;
  static float bottom = 768;
  static float zNear = -9999;
  static float zFar = 9999;

  public static void main(final String[] args) throws Exception {
    final LwjglRunner runner = new LwjglRunner();
    runner.initialize("Nifty 2.0 Demo", 1024, 768);

    Matrix4f projection = new Matrix4f();
    projection.m00 = 2 / (right-left);
    projection.m30 = - (right+left) / (right-left);
    projection.m11 = 2 / (top-bottom);
    projection.m31 = - (top+bottom) / (top-bottom);
    projection.m22 = -2 / (zFar-zNear);
    projection.m32 = - (zFar+zNear) / (zFar-zNear);
    projection.m33 = 1;
    System.out.println(projection);

    final Shader shader = new Shader(
        "vVertex",
        "vTexCoords",
        "instanceColor",
        "instanceTransform1",
        "instanceTransform2",
        "instanceTransform3");
    shader.compile("nifty.vs", "nifty.fs");
    shader.activate();
    shader.setUniform("mProjection", projection);
    shader.setUniform("tex", 0);

    final VA va = new VA();
    va.bind();

    final VBO quad = VBO.createStatic(new float[] {
        0.f,      0.f,      0.f, 0.f,
        0.f + 50, 0.f,      1.f, 0.f,
        0.f,      0.f + 50, 0.f, 1.f,
        0.f + 50, 0.f + 50, 1.f, 1.f,
    });

    glVertexAttribPointer(0, 2, GL_FLOAT, false, 4 * 4, 0);
    glEnableVertexAttribArray(0);
    CheckGL.checkGLError("glVertexAttribPointer (" + 0 + ")");

    glVertexAttribPointer(1, 2, GL_FLOAT, false, 4 * 4, 2 * 4);
    glEnableVertexAttribArray(1);
    CheckGL.checkGLError("glVertexAttribPointer (" + 1 + ")");

    Random random = new Random();

    int INSTANCE_COUNT = 2000;

    float[] indexColorData = new float[INSTANCE_COUNT*4];
    for (int i=0; i<INSTANCE_COUNT; i++) {
      indexColorData[i*4 + 0] = random.nextFloat();
      indexColorData[i*4 + 1] = random.nextFloat();
      indexColorData[i*4 + 2] = random.nextFloat();
      indexColorData[i*4 + 3] = 1.0f;
    }
    final VBO indexColor = VBO.createDynamic(indexColorData);

    glVertexAttribPointer(2, 4, GL_FLOAT, false, 0, 0);
    glVertexAttribDivisorARB(2, 1);
    glEnableVertexAttribArray(2);
    CheckGL.checkGLError("glVertexAttribPointer (" + 2 + ")");

    float[] transformData = new float[INSTANCE_COUNT*4*3];
    final VBO transform = VBO.createDynamic(transformData);

    glVertexAttribPointer(3, 4, GL_FLOAT, false, 12 * 4, 0);
    glVertexAttribDivisorARB(3, 1);
    glEnableVertexAttribArray(3);
    CheckGL.checkGLError("glEnableVertexAttribArray (" + 5 + ")");

    glVertexAttribPointer(4, 4, GL_FLOAT, false, 12 * 4, 4 * 4);
    glVertexAttribDivisorARB(4, 1);
    glEnableVertexAttribArray(4);
    CheckGL.checkGLError("glEnableVertexAttribArray (" + 6 + ")");

    glVertexAttribPointer(5, 4, GL_FLOAT, false, 12 * 4, 8 * 4);
    glVertexAttribDivisorARB(5, 1);
    glEnableVertexAttribArray(5);
    CheckGL.checkGLError("glEnableVertexAttribArray (" + 7 + ")");

    runner.renderLoop(new RenderAction(INSTANCE_COUNT, shader, transform));
    runner.destroy();
  }

  private static class RenderAction implements LwjglRunner.RenderLoopCallback {
    private long start;
    private Matrix4f local = new Matrix4f();
    private Shader shader;
    private VBO vbo;
    private FloatBuffer floatBuffer = FloatBuffer.allocate(16);
    private float[] angle;
    private float[] angleInc;
    private float[] scale;
    private int instanceCount;

    public RenderAction(final int instanceCount, final Shader shader, final VBO vbo) {
      this.angle = new float[instanceCount];
      this.angleInc = new float[instanceCount];
      this.scale = new float[instanceCount];
      this.instanceCount = instanceCount;
      this.shader = shader;
      this.vbo = vbo;
      this.start = System.currentTimeMillis();
      for (int i=0; i<instanceCount; i++) {
        angleInc[i] = (float) ((Math.random() - 0.5) * 0.01f);
        scale[i] = (float) Math.random() * 5.f + 1.f;
      }

    }

    @Override
    public boolean process() {
      GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

      float f = System.currentTimeMillis() - start;
      shader.setUniform("t", f);
      Render.renderInstances(instanceCount);

      for (int i=0; i<instanceCount; i++) {

        /*
        local.setIdentity();
        Matrix4f.mul(local, new Matrix4f().translate(new Vector2f(25f, 25f)), local);
        Matrix4f.mul(local, new Matrix4f().rotate(angle[i], new Vector3f(0.f, 1.f, 0.0f)), local);
        Matrix4f.mul(local, new Matrix4f().rotate(angle[i], new Vector3f(1.f, 0.f, 0.0f)), local);
        Matrix4f.mul(local, new Matrix4f().rotate(angle[i], new Vector3f(0.f, 0.f, 1.0f)), local);
        Matrix4f.mul(local, new Matrix4f().scale(new Vector3f(scale[i], scale[i], scale[i])), local);
        Matrix4f.mul(local, new Matrix4f().translate(new Vector2f(-25f, -25f)), local);
        */
        floatBuffer.rewind();
        local.store(floatBuffer);
        vbo.getBuffer().put(floatBuffer.array(), 0, 12);

        angle[i] = angle[i] + angleInc[i];
      }
      vbo.update();

      return false;
    }
  }
}
