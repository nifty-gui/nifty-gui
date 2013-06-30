package de.lessvoid.nifty.examples.jogl;

import java.io.InputStream;
import java.util.logging.LogManager;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;
import com.jogamp.newt.opengl.GLWindow;

import com.jogamp.opengl.util.FPSAnimator;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.batch.BatchRenderDevice;
import de.lessvoid.nifty.examples.LoggerShortFormat;
import de.lessvoid.nifty.nulldevice.NullSoundDevice;
import de.lessvoid.nifty.renderer.jogl.input.JoglInputSystem;
import de.lessvoid.nifty.renderer.jogl.render.JoglRenderDevice;
import de.lessvoid.nifty.renderer.jogl.render.batch.JoglBatchRenderBackend;
import de.lessvoid.nifty.renderer.jogl.render.batch.JoglBatchRenderBackendCoreProfile;
import de.lessvoid.nifty.spi.render.RenderDevice;
import de.lessvoid.nifty.tools.TimeProvider;

/**
 * Takes care of JOGL initialization.
 * @author void
 */
public class JOGLNiftyRunner implements GLEventListener {
  private static final int FPS = 60;
  private static final int CANVAS_WIDTH = 1024;
  private static final int CANVAS_HEIGHT = 768;
  private static long time = System.currentTimeMillis();
  private static long frames = 0;
  private static boolean useBatchedRenderer = false;
  private static boolean useBatchedCoreRenderer = false;
  private static GLWindow window;

  private RenderDevice renderDevice;
  private JoglInputSystem inputSystem;
  private Nifty nifty;
  private Callback callback;

  public static void run(final String[] args, final Callback callback) throws Exception {
    InputStream input = null;
    try {
      input = LoggerShortFormat.class.getClassLoader().getResourceAsStream("logging.properties");
      LogManager.getLogManager().readConfiguration(input);
    } finally {
      if (input != null) {
        input.close();
      }
    }

    if (args.length == 1) {
      useBatchedRenderer = "batch".equals(args[0]);
      useBatchedCoreRenderer = "core".equals(args[0]);
      if (useBatchedCoreRenderer) {
        useBatchedRenderer = true;
      }
    }

    window = GLWindow.create(new GLCapabilities(getProfile(useBatchedCoreRenderer)));
    window.setAutoSwapBufferMode(true);
    window.setSize(CANVAS_WIDTH, CANVAS_HEIGHT);
    window.addGLEventListener(new JOGLNiftyRunner(callback));

    final FPSAnimator animator = new FPSAnimator(window, FPS, false);

    window.setTitle(getCaption(useBatchedRenderer, useBatchedCoreRenderer));
    window.setVisible(true);

    animator.start();
  }

  private static GLProfile getProfile(final boolean useCore) {
    if (useCore) {
      return GLProfile.get(GLProfile.GL3);
    }
    return GLProfile.getDefault();
  }

  private static String getCaption(final boolean batched, final boolean useCore) {
    String add = "";
    if (batched) {
      add += " (BATCH RENDERER)";
    }
    if (useCore) {
      add += " (CORE PROFILE)";
    }
    return "NEWT Window Test - Nifty" + add;
  }

  public JOGLNiftyRunner(final Callback callback) {
    this.callback = callback;
  }

  public void display(GLAutoDrawable drawable) {
    long startTime = System.nanoTime();
    update(drawable);
    render(drawable, startTime);
  }

  public void init(GLAutoDrawable drawable) {
    if (useBatchedRenderer) {
      if (useBatchedCoreRenderer) {
        renderDevice = new BatchRenderDevice(new JoglBatchRenderBackendCoreProfile(), 2048, 2048);
      } else {
        renderDevice = new BatchRenderDevice(new JoglBatchRenderBackend(), 2048, 2048);
      }
    } else {
      renderDevice = new JoglRenderDevice();
    }
    inputSystem = new JoglInputSystem();
    window.addMouseListener(inputSystem);
    window.addKeyListener(inputSystem);

    nifty = new Nifty(renderDevice, new NullSoundDevice(), inputSystem, new TimeProvider());
    callback.init(nifty, window);
  }

  public void dispose(GLAutoDrawable drawable) {
  }

  public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
    GL gl = drawable.getGL();
    gl.glViewport(0, 0, width, height);
    gl.glEnable(GL.GL_BLEND);

    if (gl.isGL2()) {
      GL2 gl2 = gl.getGL2();
      gl2.glMatrixMode(GL2.GL_PROJECTION);
      gl2.glLoadIdentity();
      gl2.glOrtho(0, width, height, 0, -9999, 9999);

      gl2.glMatrixMode(GL2.GL_MODELVIEW);
      gl2.glLoadIdentity();
    }
  }

  private void update(GLAutoDrawable drawable) {
    nifty.update();
    nifty.render(true);
  }

  private void render(GLAutoDrawable drawable, final long startTime) {
    long frameTime = System.nanoTime() - startTime;
    frames++;

    long diff = System.currentTimeMillis() - time;
    if (diff >= 1000) {
      time += diff;
      System.out.println("fps : " + frames + " (" + frameTime / 1000000f + " ms)");
      frames = 0;
    }
  }

  public interface Callback {
    void init(Nifty nifty, GLWindow window);
  }
}
