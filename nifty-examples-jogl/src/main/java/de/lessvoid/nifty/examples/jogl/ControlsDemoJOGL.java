package de.lessvoid.nifty.examples.jogl;

import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.InputStream;
import java.util.logging.LogManager;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLCanvas;

import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

import com.jogamp.opengl.util.FPSAnimator;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.examples.LoggerShortFormat;
import de.lessvoid.nifty.examples.defaultcontrols.ControlsDemo;
import de.lessvoid.nifty.examples.resolution.ResolutionControlLWJGL;
import de.lessvoid.nifty.nulldevice.NullSoundDevice;
import de.lessvoid.nifty.renderer.jogl.input.JoglInputSystem;
import de.lessvoid.nifty.renderer.jogl.render.JoglRenderDevice;
import de.lessvoid.nifty.tools.TimeProvider;

public class ControlsDemoJOGL implements GLEventListener {
  private static final int FPS = 60;
  private static final int CANVAS_WIDTH = 1024;
  private static final int CANVAS_HEIGHT = 768;
  long time = System.currentTimeMillis();
  long frames = 0;
  private static JoglInputSystem inputSystem;
  private static JoglRenderDevice renderDevice;
  private static Nifty nifty;
  private static GLCanvas canvas;

  private static void intialize() throws Exception {
    InputStream input = null;
    try {
      input = LoggerShortFormat.class.getClassLoader().getResourceAsStream("logging.properties");
      LogManager.getLogManager().readConfiguration(input);
    } finally {
      if (input != null) {
        input.close();
      }
    }
  }

  public static void main(String[] args) throws Exception {
    intialize();

    canvas = new GLCanvas(new GLCapabilities(GLProfile.getDefault()));
    canvas.setAutoSwapBufferMode(true);
    canvas.setSize(CANVAS_WIDTH, CANVAS_HEIGHT);
    canvas.addGLEventListener(new ControlsDemoJOGL());

    final FPSAnimator animator = new FPSAnimator(canvas, FPS, false);

    final Frame frame = new Frame("AWT Window Test - Nifty");
    frame.setSize(CANVAS_WIDTH, CANVAS_HEIGHT);
    frame.add(canvas);
    frame.setVisible(true);
    frame.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        animator.stop();
        frame.remove(canvas);
        System.exit(0);
      }
    });

    animator.start();
  }

  public void display(GLAutoDrawable drawable) {
    update(drawable);
    render(drawable);
  }

  public void init(GLAutoDrawable drawable) {
    renderDevice = new JoglRenderDevice();
    inputSystem = new JoglInputSystem();
    canvas.addMouseListener(inputSystem);
    canvas.addMouseMotionListener(inputSystem);
    canvas.addKeyListener(inputSystem);

    nifty = new Nifty(renderDevice, new NullSoundDevice(), inputSystem, new TimeProvider());

    ControlsDemo<DisplayMode> demo = new ControlsDemo(new ResolutionControlLWJGL());
    demo.prepareStart(nifty);
    nifty.gotoScreen("start");
  }

  public void dispose(GLAutoDrawable drawable) {
  }

  public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
    GL2 gl = drawable.getGL().getGL2();

    gl.glViewport(0, 0, width, height);

    gl.glMatrixMode(GL2.GL_PROJECTION);
    gl.glLoadIdentity();
    gl.glOrtho(0, width, height, 0, -9999, 9999);

    gl.glMatrixMode(GL11.GL_MODELVIEW);
    gl.glLoadIdentity();
  }

  private void update(GLAutoDrawable drawable) {
    boolean di = nifty.update();
    nifty.render(true);
  }

  private void render(GLAutoDrawable drawable) {
    frames++;

    long diff = System.currentTimeMillis() - time;
    if (diff >= 1000) {
      time += diff;
      System.out.println("fps : " + frames);
      frames = 0;
    }
  }
}
