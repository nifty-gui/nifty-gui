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
  private static GLWindow window;
  private static Mode mode = Mode.Old;

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
      mode = Mode.findMode(args[0]);
    }

    System.out.println("using mode " + mode.getName());

    window = GLWindow.create(new GLCapabilities(getProfile(mode)));
    window.setAutoSwapBufferMode(true);
    window.setSize(CANVAS_WIDTH, CANVAS_HEIGHT);
    window.addGLEventListener(new JOGLNiftyRunner(callback));

    final FPSAnimator animator = new FPSAnimator(window, FPS, false);

    window.setTitle(getCaption(mode));
    window.setVisible(true);

    animator.start();
  }

  private static GLProfile getProfile(final Mode mode) {
    if (Mode.Core.equals(mode)) {
      return GLProfile.get(GLProfile.GL3);
    }
    if (Mode.ES2.equals(mode)) {
      return GLProfile.get(GLProfile.GL2ES2);
    }
    return GLProfile.getDefault();
  }

  private static String getCaption(final Mode mode) {
    String add = "";
    if (Mode.Batch.equals(mode)) {
      add += " (BATCH RENDERER)";
    }
    if (Mode.Core.equals(mode)) {
      add += " (CORE PROFILE)";
    }
    if (Mode.ES2.equals(mode)) {
      add += " (ES2 PROFILE)";
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
    if (Mode.Batch.equals(mode)) {
      renderDevice = new BatchRenderDevice(new JoglBatchRenderBackend(), 2048, 2048);
    } else if (Mode.Core.equals(mode)) {
      renderDevice = new BatchRenderDevice(new JoglBatchRenderBackendCoreProfile(), 2048, 2048);
    } else if (Mode.ES2.equals(mode)) {
      renderDevice = new BatchRenderDevice(new JoglBatchRenderBackendCoreProfile(), 2048, 2048);
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
      System.exit(0);
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

  private static enum Mode {
    Old("old"),
    Batch("batch"),
    Core("core"),
    ES2("es2");

    private final String name;
    private Mode(final String name) {
      this.name = name;
    }

    public String getName() {
      return name;
    }

    public static Mode findMode(final String arg) {
      for (Mode mode : Mode.values()) {
        if (mode.matches(arg)) {
          return mode;
        }
      }
      return Mode.Old;
    }

    private boolean matches(final String check) {
      return name.equals(check);
    }
  }
}
