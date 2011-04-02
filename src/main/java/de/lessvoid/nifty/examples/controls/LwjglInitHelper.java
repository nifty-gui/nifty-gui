package de.lessvoid.nifty.examples.controls;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.renderer.lwjgl.input.LwjglInputSystem;

/**
 * Helper class shared by all the examples to initialize lwjgl and stuff.
 * @author void
 */
public class LwjglInitHelper {

  private static final int WIDTH = 1024;
  private static final int HEIGHT = 768;

  /** logger. */
  private static Logger log = Logger.getLogger(LwjglInitHelper.class.getName());

  private static LwjglInputSystem inputSystem;

  public static LwjglInputSystem getInputSystem() {
    return inputSystem;
  }

  /**
   * RenderLoopCallback.
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
   * @param title title pf window
   * @return true on success and false otherwise
   */
  public static boolean initSubSystems(final String title) {
    LoggerShortFormat.intialize();
    if (!LwjglInitHelper.initGraphics(title)) {
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
   * @param title title of window
   * @return true on success and false otherwise
   */
  private static boolean initGraphics(final String title) {
    int width = 1920;
    int height = 1200;
    try {
      DisplayMode currentMode = Display.getDisplayMode();
      log.fine("currentmode: " + currentMode.getWidth() + ", " + currentMode.getHeight() + ", " + currentMode.getBitsPerPixel() + ", " + currentMode.getFrequency());

      width = currentMode.getWidth();
      height = currentMode.getHeight();

      //  get available modes, and print out
      DisplayMode[] modes = Display.getAvailableDisplayModes();
      log.fine("Found " + modes.length + " display modes");

      List < DisplayMode > matching = new ArrayList < DisplayMode >();
      for (int i = 0; i < modes.length; i++) {
        DisplayMode mode = modes[i];
        if (mode.getWidth() == WIDTH && mode.getHeight() == HEIGHT && mode.getBitsPerPixel() == 32 ) {
          log.fine(mode.getWidth() + ", " + mode.getHeight() + ", " + mode.getBitsPerPixel() + ", " + mode.getFrequency());
          matching.add(mode);
        }
      }

      DisplayMode[] matchingModes = matching.toArray(new DisplayMode[0]);

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
        Arrays.sort(matchingModes, new Comparator < DisplayMode >() {
          public int compare(final DisplayMode o1, final DisplayMode o2) {
            if (o1.getFrequency() > o2.getFrequency()) {
              return 1;
            } else if (o1.getFrequency() < o2.getFrequency()) {
              return -1;
            } else {
              return 0;
            }
          }
        });

        for (DisplayMode mode : matchingModes) {
          log.fine("using fallback mode: " + mode.getWidth() + ", "
              + mode.getHeight() + ", "
              + mode.getBitsPerPixel() + ", "
              + mode.getFrequency());
          Display.setDisplayMode(mode);
          break;
        }
      }

      int x = (width - Display.getDisplayMode().getWidth()) / 2;
      int y = (height - Display.getDisplayMode().getHeight()) / 2;
      Display.setLocation(x, y);

      // Create the actual window
      try {
        Display.setFullscreen(false);
        Display.create();
        Display.setVSyncEnabled(false);
        Display.setTitle(title);
      } catch (Exception e) {
        e.printStackTrace();
        log.warning("Unable to create window!, exiting...");
        System.exit(-1);
      }

      log.info(
          "Width: " + Display.getDisplayMode().getWidth()
          + ", Height: " + Display.getDisplayMode().getHeight()
          + ", Bits per pixel: " + Display.getDisplayMode().getBitsPerPixel()
          + ", Frequency: " + Display.getDisplayMode().getFrequency()
          + ", Title: " + Display.getTitle());

      // just output some infos about the system we're on
      log.info("plattform: " + LWJGLUtil.getPlatformName());
      log.info("opengl version: " + GL11.glGetString(GL11.GL_VERSION));
      log.info("opengl vendor: " + GL11.glGetString(GL11.GL_VENDOR));
      log.info("opengl renderer: " + GL11.glGetString(GL11.GL_RENDERER));
      String extensions = GL11.glGetString(GL11.GL_EXTENSIONS);
      if (extensions != null) {
        String[] ext = extensions.split(" ");
        for (int i = 0; i < ext.length; i++) {
          log.fine("opengl extensions: " + ext[i]);
        }
      }

      IntBuffer viewportBuffer = BufferUtils.createIntBuffer(4 * 4);
      GL11.glGetInteger(GL11.GL_VIEWPORT, viewportBuffer);
      int viewportWidth = viewportBuffer.get(2);
      int viewportHeight = viewportBuffer.get(3);

//      GL11.glViewport(0, 0, Display.getDisplayMode().getWidth(), Display.getDisplayMode().getHeight());
      GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0, viewportWidth, viewportHeight, 0, -9999, 9999);

      GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();

        // Prepare Rendermode
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_CULL_FACE);

        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glAlphaFunc(GL11.GL_NOTEQUAL, 0);

        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_TEXTURE_2D);

        GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
        GL11.glEnable(GL11.GL_TEXTURE_2D);

      return true;
    } catch (LWJGLException e) {
      e.printStackTrace();
      return false;
    }
  }

  /**
   * Init input system.
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
   * @param nifty nifty instance
   * @param editor 
   * @param callback callback
   */
  public static void renderLoop(
      final Nifty nifty,
      final RenderLoopCallback callback) {
    boolean done = false;
    Display.setVSyncEnabled(true);
    while (!Display.isCloseRequested() && !done) {
      if (callback != null) {
        callback.process();
      }

      // show render
      Display.update();

      // render nifty
      if (nifty.update()) {
        done = true;
      }

      nifty.render(false);

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
