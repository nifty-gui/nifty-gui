package de.lessvoid.nifty.examples;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.batch.CheckGL;
import de.lessvoid.nifty.batch.spi.GL;
import de.lessvoid.nifty.renderer.lwjgl.input.LwjglInputSystem;
import de.lessvoid.nifty.renderer.lwjgl.render.LwjglGL;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.opengl.*;
import org.lwjgl.util.glu.GLU;

import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glViewport;

/**
 * Helper class shared by all the examples to initialize lwjgl and stuff.
 *
 * @author void
 */
public class LwjglInitHelper {

  private static final int WIDTH = 1024;
  private static final int HEIGHT = 768;

  /**
   * logger.
   */
  private static final Logger log = Logger.getLogger(LwjglInitHelper.class.getName());
  private static LwjglInputSystem inputSystem;

  public static LwjglInputSystem getInputSystem() {
    return inputSystem;
  }

  /**
   * RenderLoopCallback.
   *
   * @author void
   */
  public interface RenderLoopCallback {
    /**
     * process.
     */
    void process();
  }

  /**
   * Init SubSystems.
   *
   * @param title title pf window
   * @return true on success and false otherwise
   */
  public static boolean initSubSystems(final String title) {
    return initSubSystems(title, false);
  }

  public static boolean initSubSystems(final String title, final boolean useCoreProfile) {
    try {
      LoggerShortFormat.intialize();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

    if (!LwjglInitHelper.initGraphics(title, useCoreProfile)) {
      return false;
    }

    // init input system
    if (!LwjglInitHelper.initInput()) {
      return false;
    }

    return true;
  }

  /**
   * Init lwjgl graphics.
   *
   * @param title title of window
   * @return true on success and false otherwise
   */
  private static boolean initGraphics(final String title, final boolean enableCoreProfile) {
    int width;
    int height;
    try {
      DisplayMode currentMode = Display.getDisplayMode();
      log.fine("currentmode: " + currentMode.getWidth() + ", " + currentMode.getHeight() + ", " +
          "" + currentMode.getBitsPerPixel() + ", " + currentMode.getFrequency());

      width = currentMode.getWidth();
      height = currentMode.getHeight();

      //  get available modes, and print out
      DisplayMode[] modes = Display.getAvailableDisplayModes();
      log.fine("Found " + modes.length + " display modes");

      List<DisplayMode> matching = new ArrayList<DisplayMode>();
      for (int i = 0; i < modes.length; i++) {
        DisplayMode mode = modes[i];
        if (mode.getWidth() == WIDTH && mode.getHeight() == HEIGHT && mode.getBitsPerPixel() == 32) {
          log.fine(mode.getWidth() + ", " + mode.getHeight() + ", " + mode.getBitsPerPixel() + ", " +
              "" + mode.getFrequency());
          matching.add(mode);
        }
      }

      DisplayMode[] matchingModes = matching.toArray(new DisplayMode[matching.size()]);

      // find mode with matching freq
      boolean found = false;
      for (int i = 0; i < matchingModes.length; i++) {
        if (matchingModes[i].getFrequency() == currentMode.getFrequency()) {
          log.fine("using mode: " + matchingModes[i].getWidth() + ", "
              + matchingModes[i].getHeight() + ", "
              + matchingModes[i].getBitsPerPixel() + ", "
              + matchingModes[i].getFrequency());
          Display.setDisplayMode(matchingModes[i]);
          found = true;
          break;
        }
      }

      if (!found) {
        Arrays.sort(matchingModes, new Comparator<DisplayMode>() {
          @Override
          public int compare(@Nonnull final DisplayMode o1, @Nonnull final DisplayMode o2) {
            if (o1.getFrequency() > o2.getFrequency()) {
              return 1;
            } else if (o1.getFrequency() < o2.getFrequency()) {
              return -1;
            } else {
              return 0;
            }
          }
        });

        for (int i = 0; i < matchingModes.length; i++) {
          try {
            log.fine("using fallback mode: " + matchingModes[i].getWidth() + ", "
                + matchingModes[i].getHeight() + ", "
                + matchingModes[i].getBitsPerPixel() + ", "
                + matchingModes[i].getFrequency());
            Display.setDisplayMode(matchingModes[i]);
            break;
          } catch (LWJGLException ignored) {
          }
        }
      }

      int x = (width - Display.getDisplayMode().getWidth()) / 2;
      int y = (height - Display.getDisplayMode().getHeight()) / 2;
      Display.setLocation(x, y);

      // Create the actual window
      try {
        Display.setFullscreen(false);
        if (enableCoreProfile) {
          Display.create(new PixelFormat(), new ContextAttribs(3, 2).withProfileCore(true));
        } else {
          Display.create();
        }
        Display.setVSyncEnabled(false);
        Display.setTitle(title);
      } catch (Exception e) {
        e.printStackTrace();
        log.warning("Unable to create window!, exiting...");
        System.exit(-1);
      }

      log.fine(
          "Width: " + Display.getDisplayMode().getWidth()
              + ", Height: " + Display.getDisplayMode().getHeight()
              + ", Bits per pixel: " + Display.getDisplayMode().getBitsPerPixel()
              + ", Frequency: " + Display.getDisplayMode().getFrequency()
              + ", Title: " + Display.getTitle());

      // just output some infos about the system we're on
      log.fine("plattform: " + LWJGLUtil.getPlatformName());
      log.fine("opengl version: " + GL11.glGetString(GL11.GL_VERSION));
      log.fine("opengl vendor: " + GL11.glGetString(GL11.GL_VENDOR));
      log.fine("opengl renderer: " + GL11.glGetString(GL11.GL_RENDERER));

      GL gl = new LwjglGL();

      if (enableCoreProfile) {
        String extensions = GL30.glGetStringi(GL11.GL_EXTENSIONS, 0);
        if (extensions != null) {
          String[] ext = extensions.split(" ");
          for (int i = 0; i < ext.length; i++) {
            log.finer("opengl extensions: " + ext[i]);
          }
        }
        CheckGL.checkGLError(gl, "extension check failed");

        glViewport(0, 0, Display.getDisplayMode().getWidth(), Display.getDisplayMode().getHeight());
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        glClear(GL11.GL_COLOR_BUFFER_BIT);
        glEnable(GL11.GL_BLEND);
        glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
      } else {
        String extensions = GL11.glGetString(GL11.GL_EXTENSIONS);
        if (extensions != null) {
          String[] ext = extensions.split(" ");
          for (int i = 0; i < ext.length; i++) {
            log.finer("opengl extensions: " + ext[i]);
          }
        }
        CheckGL.checkGLError(gl, "extension check failed");

        IntBuffer viewportBuffer = BufferUtils.createIntBuffer(4 * 4);
        GL11.glGetInteger(GL11.GL_VIEWPORT, viewportBuffer);
        int viewportWidth = viewportBuffer.get(2);
        int viewportHeight = viewportBuffer.get(3);

        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0, viewportWidth, viewportHeight, 0, -9999, 9999);

        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();

        GL11.glDisable(GL11.GL_DEPTH_TEST);
        glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_CULL_FACE);

        glEnable(GL11.GL_ALPHA_TEST);
        GL11.glAlphaFunc(GL11.GL_NOTEQUAL, 0);

        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_TEXTURE_2D);

        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        glClear(GL11.GL_COLOR_BUFFER_BIT);
        glEnable(GL11.GL_TEXTURE_2D);
      }

      return true;
    } catch (LWJGLException e) {
      e.printStackTrace();
      return false;
    }
  }

  /**
   * Init input system.
   *
   * @return true on success and false otherwise
   */
  private static boolean initInput() {
    try {
      inputSystem = new LwjglInputSystem();
      inputSystem.startup();
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      log.warning("Unable to create keyboard!, exiting...");
      return false;
    }
  }

  /**
   * @param nifty    nifty instance
   * @param callback callback
   */
  public static void renderLoop(
      @Nonnull final Nifty nifty,
      @Nullable final RenderLoopCallback callback) {
    boolean done = false;
    while (!Display.isCloseRequested() && !done) {
      if (callback != null) {
        callback.process();
      }

      // show render
      Display.update();

      if (nifty.update()) {
        done = true;
      }

      nifty.render(true);

      // check gl error at least ones per frame
      int error = GL11.glGetError();
      if (error != GL11.GL_NO_ERROR) {
        String glerrmsg = GLU.gluErrorString(error);
        log.warning("OpenGL Error: (" + error + ") " + glerrmsg);
      }
    }
  }

  /**
   * destroy all and quit.
   */
  public static void destroy() {
    inputSystem.shutdown();
    Display.destroy();
    System.exit(0);
  }
}
