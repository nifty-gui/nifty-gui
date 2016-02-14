package de.lessvoid.nifty.examples.jogl;

import java.io.InputStream;
import java.util.logging.LogManager;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.util.FPSAnimator;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.examples.LoggerShortFormat;
import de.lessvoid.nifty.nulldevice.NullSoundDevice;
import de.lessvoid.nifty.render.batch.BatchRenderDevice;
import de.lessvoid.nifty.renderer.jogl.input.JoglInputSystem;
import de.lessvoid.nifty.renderer.jogl.render.JoglBatchRenderBackendCoreProfileFactory;
import de.lessvoid.nifty.renderer.jogl.render.JoglBatchRenderBackendFactory;
import de.lessvoid.nifty.renderer.jogl.render.JoglRenderDevice;
import de.lessvoid.nifty.sound.paulssoundsystem.PaulsSoundsystemSoundDevice;
import de.lessvoid.nifty.spi.render.RenderDevice;
import de.lessvoid.nifty.spi.sound.SoundDevice;
import de.lessvoid.nifty.spi.time.TimeProvider;
import de.lessvoid.nifty.spi.time.impl.AccurateTimeProvider;
import paulscode.sound.SoundSystemException;
import paulscode.sound.libraries.LibraryJavaSound;

/**
 * Takes care of JOGL initialization.
 *
 * @author void
 */
public class JOGLNiftyRunner implements GLEventListener {
  private static final int FPS = 60;
  private static final int CANVAS_WIDTH = 1024;
  private static final int CANVAS_HEIGHT = 768;
  private static TimeProvider timeProvider = new AccurateTimeProvider();
  private static long time = timeProvider.getMsTime();
  private static long frames = 0;
  private static GLWindow window;
  @Nonnull
  private static Mode mode = Mode.Batch;

  @Nullable
  private Nifty nifty;
  private Callback callback;

  public static void run(@Nonnull final String[] args, final Callback callback) throws Exception {
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

    GLCapabilities glCapabilities = new GLCapabilities(getProfile(mode));
    window = GLWindow.create(glCapabilities);
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

  @Nonnull
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

  @Override
  public void display(GLAutoDrawable drawable) {
    long startTime = System.nanoTime();
    update(drawable);
    render(drawable, startTime);
  }

  @Override
  public void init(GLAutoDrawable drawable) {
    RenderDevice renderDevice;
    if (Mode.Batch.equals(mode)) {
      renderDevice = new BatchRenderDevice(JoglBatchRenderBackendFactory.create(window));
    } else if (Mode.Core.equals(mode)) {
      renderDevice = new BatchRenderDevice(JoglBatchRenderBackendCoreProfileFactory.create(window));
    } else if (Mode.ES2.equals(mode)) {
      renderDevice = new BatchRenderDevice(JoglBatchRenderBackendCoreProfileFactory.create(window));
    } else {
      renderDevice = new JoglRenderDevice();
    }
    JoglInputSystem inputSystem = new JoglInputSystem(window);
    window.addMouseListener(inputSystem);
    window.addKeyListener(inputSystem);

    SoundDevice soundDevice;
    try {
      soundDevice = new PaulsSoundsystemSoundDevice(LibraryJavaSound.class);
    } catch (SoundSystemException e) {
      soundDevice = new NullSoundDevice();
    }

    nifty = new Nifty(renderDevice, soundDevice, inputSystem, timeProvider);
    callback.init(nifty, window);
  }

  @Override
  public void dispose(GLAutoDrawable drawable) {
    System.exit(0);
  }

  @Override
  public void reshape(@Nonnull GLAutoDrawable drawable, int x, int y, int width, int height) {
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
    long frameTime = timeProvider.getMsTime() - startTime;
    frames++;

    long diff = timeProvider.getMsTime() - time;
    if (diff >= 1000) {
      time += diff;
      System.out.println("fps : " + frames + " (" + frameTime + " ms)");
      frames = 0;
    }
  }

  public interface Callback {
    void init(@Nonnull Nifty nifty, @Nonnull GLWindow window);
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

    @Nonnull
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
