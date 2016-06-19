package de.lessvoid.nifty.examples.lwjgl;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.examples.LoggerShortFormat;
import de.lessvoid.nifty.examples.NiftyExample;
import de.lessvoid.nifty.render.batch.BatchRenderDevice;
import de.lessvoid.nifty.render.batch.CheckGL;
import de.lessvoid.nifty.render.batch.spi.GL;
import de.lessvoid.nifty.renderer.lwjgl3.input.Lwjgl3InputSystem;
import de.lessvoid.nifty.sound.openal.OpenALSoundDevice;
import de.lessvoid.nifty.spi.time.impl.AccurateTimeProvider;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.opengl.*;

import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glViewport;

/**
 * TODO: Not yet implemented
 * 
 * Loads & runs any {@link de.lessvoid.nifty.examples.NiftyExample} using Nifty and LWJGL3.
 *
 * @author void
 * @author Aaron Mahan &lt;aaron@forerunnergames.com&gt;
 * @author Brian Groenke
 */
public class NiftyExampleLoaderLWJGL3 {
  private static final Logger log = Logger.getLogger(NiftyExampleLoaderLWJGL3.class.getName());
  private static final boolean USE_CORE_PROFILE = true;
  private static final int WIDTH = 1024;
  private static final int HEIGHT = 768;
  private static Lwjgl3InputSystem inputSystem;
  private static Nifty nifty;

  public interface RenderLoopCallback {
    void process(@Nonnull Nifty nifty);
  }

  /**
   * Easily run any {@link de.lessvoid.nifty.examples.NiftyExample}. Just instantiate your example and pass it to this
   * method. This method will use LWJGL to run your example, automatically creating & initializing an LWJGL application
   * for you before running the example.
   *
   * @param example The {@link de.lessvoid.nifty.examples.NiftyExample} to run.
   */
  public static void run (@Nonnull final NiftyExample example) {
    run (example, null);
  }

  /**
   * Easily run any {@link de.lessvoid.nifty.examples.NiftyExample}. Just instantiate your example and pass it along
   * with your optional custom callback to this method. This method will use LWJGL to run your example, automatically
   * creating & initializing an LWJGL application for you before running the example.
   *
   * @param example The {@link de.lessvoid.nifty.examples.NiftyExample} to run.
   * @param callback Provides access to the render loop by calling your implementation of the process method once at
   *                 the beginning of each frame. You can put any custom code here that needs to be executed every frame.
   *                 It also provides you access to the Nifty instance.
   */
  public static void run (@Nonnull final NiftyExample example, @Nullable RenderLoopCallback callback) {
//    if (!initSubSystems(example.getTitle(), USE_CORE_PROFILE)) {
//      System.exit(1);
//    }
//
//    try {
//      example.prepareStart(nifty);
//      if (example.getMainXML() != null) {
//        nifty.fromXml(example.getMainXML(), example.getStartScreen());
//      } else {
//        nifty.gotoScreen(example.getStartScreen());
//      }
//    } catch (Exception e) {
//      log.log(Level.SEVERE, "Unable to run Nifty example!", e);
//    }
//
//    boolean done = false;
//
//    while (!done) {
//      render(nifty, callback);
//      done = true;
//    }
//
//    shutDown();
  }

//  private static boolean initSubSystems(final String windowTitle, final boolean useCoreProfile) {
//    try {
//      LoggerShortFormat.intialize();
//    } catch (Exception e) {
//      throw new RuntimeException(e);
//    }
//
//    return initGraphics(windowTitle, useCoreProfile) && initInput() && initNifty (inputSystem);
//  }
//
//  private static boolean initGraphics(final String title, final boolean enableCoreProfile) {
//    int width;
//    int height;
//    try {
//      DisplayMode currentMode = Display.getDisplayMode();
//      log.fine("currentmode: " + currentMode.getWidth() + ", " + currentMode.getHeight() + ", " +
//          "" + currentMode.getBitsPerPixel() + ", " + currentMode.getFrequency());
//
//      width = currentMode.getWidth();
//      height = currentMode.getHeight();
//
//      // Get available display modes.
//      DisplayMode[] modes = Display.getAvailableDisplayModes();
//      log.fine("Found " + modes.length + " display modes");
//
//      List<DisplayMode> matching = new ArrayList<DisplayMode>();
//      for (int i = 0; i < modes.length; i++) {
//        DisplayMode mode = modes[i];
//        if (mode.getWidth() == WIDTH && mode.getHeight() == HEIGHT && mode.getBitsPerPixel() == 32) {
//          log.fine(mode.getWidth() + ", " + mode.getHeight() + ", " + mode.getBitsPerPixel() + ", " +
//              "" + mode.getFrequency());
//          matching.add(mode);
//        }
//      }
//
//      DisplayMode[] matchingModes = matching.toArray(new DisplayMode[matching.size()]);
//
//      // Find display mode with matching frequency.
//      boolean found = false;
//      for (final DisplayMode matchingMode : matchingModes) {
//        if (matchingMode.getFrequency() == currentMode.getFrequency()) {
//          log.fine("using mode: " + matchingMode.getWidth() + ", "
//                  + matchingMode.getHeight() + ", "
//                  + matchingMode.getBitsPerPixel() + ", "
//                  + matchingMode.getFrequency());
//          Display.setDisplayMode(matchingMode);
//          found = true;
//          break;
//        }
//      }
//
//      if (!found) {
//        Arrays.sort(matchingModes, new Comparator<DisplayMode>() {
//          @Override
//          public int compare(@Nonnull final DisplayMode o1, @Nonnull final DisplayMode o2) {
//            if (o1.getFrequency() > o2.getFrequency()) {
//              return 1;
//            } else if (o1.getFrequency() < o2.getFrequency()) {
//              return -1;
//            } else {
//              return 0;
//            }
//          }
//        });
//
//        for (int i = 0; i < matchingModes.length; i++) {
//          try {
//            log.fine("using fallback mode: " + matchingModes[i].getWidth() + ", "
//                + matchingModes[i].getHeight() + ", "
//                + matchingModes[i].getBitsPerPixel() + ", "
//                + matchingModes[i].getFrequency());
//            Display.setDisplayMode(matchingModes[i]);
//            break;
//          } catch (LWJGLException ignored) {
//          }
//        }
//      }
//
//      int x = (width - Display.getDisplayMode().getWidth()) / 2;
//      int y = (height - Display.getDisplayMode().getHeight()) / 2;
//      Display.setLocation(x, y);
//
//      // Create the window.
//      try {
//        Display.setFullscreen(false);
//        if (enableCoreProfile) {
//          Display.create(new PixelFormat(), new ContextAttribs(3, 2).withProfileCore(true));
//        } else {
//          Display.create();
//        }
//        Display.setVSyncEnabled(false);
//        Display.setTitle(title);
//      } catch (Exception e) {
//        e.printStackTrace();
//        log.warning("Unable to create window!, exiting...");
//        System.exit(-1);
//      }
//
//      log.fine(
//          "Width: " + Display.getDisplayMode().getWidth()
//              + ", Height: " + Display.getDisplayMode().getHeight()
//              + ", Bits per pixel: " + Display.getDisplayMode().getBitsPerPixel()
//              + ", Frequency: " + Display.getDisplayMode().getFrequency()
//              + ", Title: " + Display.getTitle());
//
//      log.fine("platform: " + LWJGLUtil.getPlatformName());
//      log.fine("opengl version: " + GL11.glGetString(GL11.GL_VERSION));
//      log.fine("opengl vendor: " + GL11.glGetString(GL11.GL_VENDOR));
//      log.fine("opengl renderer: " + GL11.glGetString(GL11.GL_RENDERER));
//
//      GL gl = new LwjglGL();
//
//      if (enableCoreProfile) {
//        String extensions = GL30.glGetStringi(GL11.GL_EXTENSIONS, 0);
//        if (extensions != null) {
//          String[] ext = extensions.split(" ");
//          for (int i = 0; i < ext.length; i++) {
//            log.finer("opengl extensions: " + ext[i]);
//          }
//        }
//        CheckGL.checkGLError(gl, "extension check failed");
//
//        glViewport(0, 0, Display.getDisplayMode().getWidth(), Display.getDisplayMode().getHeight());
//        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
//        glClear(GL11.GL_COLOR_BUFFER_BIT);
//        glEnable(GL11.GL_BLEND);
//        glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
//      } else {
//        String extensions = GL11.glGetString(GL11.GL_EXTENSIONS);
//        if (extensions != null) {
//          String[] ext = extensions.split(" ");
//          for (int i = 0; i < ext.length; i++) {
//            log.finer("opengl extensions: " + ext[i]);
//          }
//        }
//        CheckGL.checkGLError(gl, "extension check failed");
//
//        IntBuffer viewportBuffer = BufferUtils.createIntBuffer(4 * 4);
//        GL11.glGetInteger(GL11.GL_VIEWPORT, viewportBuffer);
//        int viewportWidth = viewportBuffer.get(2);
//        int viewportHeight = viewportBuffer.get(3);
//
//        GL11.glMatrixMode(GL11.GL_PROJECTION);
//        GL11.glLoadIdentity();
//        GL11.glOrtho(0, viewportWidth, viewportHeight, 0, -9999, 9999);
//
//        GL11.glMatrixMode(GL11.GL_MODELVIEW);
//        GL11.glLoadIdentity();
//
//        GL11.glDisable(GL11.GL_DEPTH_TEST);
//        glEnable(GL11.GL_BLEND);
//        GL11.glDisable(GL11.GL_CULL_FACE);
//
//        glEnable(GL11.GL_ALPHA_TEST);
//        GL11.glAlphaFunc(GL11.GL_NOTEQUAL, 0);
//
//        GL11.glDisable(GL11.GL_LIGHTING);
//        GL11.glDisable(GL11.GL_TEXTURE_2D);
//
//        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
//        glClear(GL11.GL_COLOR_BUFFER_BIT);
//        glEnable(GL11.GL_TEXTURE_2D);
//      }
//
//      return true;
//    } catch (LWJGLException e) {
//      e.printStackTrace();
//      return false;
//    }
//  }
//
//  private static boolean initInput() {
//    try {
//      inputSystem = new LwjglInputSystem();
//      inputSystem.startup();
//      return true;
//    } catch (Exception e) {
//      e.printStackTrace();
//      log.warning("Unable to create keyboard!, exiting...");
//      return false;
//    }
//  }
//
//  private static boolean initNifty (final LwjglInputSystem inputSystem) {
//    try {
//      log.info ("\n\nRunning in OpenGL " + (USE_CORE_PROFILE ? "Core Profile Mode (3.2+)" : "Compatibility Mode (1.2)") + ".\n\n");
//      nifty = new Nifty(
//              new BatchRenderDevice(USE_CORE_PROFILE ? LwjglBatchRenderBackendCoreProfileFactory.create() : LwjglBatchRenderBackendFactory.create()),
//              new OpenALSoundDevice(),
//              inputSystem,
//              new AccurateTimeProvider());
//      return true;
//    } catch (Exception e) {
//      e.printStackTrace();
//      log.warning("Unable to create Nifty!, exiting...");
//      return false;
//    }
//  }
//
//  private static void render(@Nonnull final Nifty nifty, @Nullable final RenderLoopCallback callback) {
//    boolean done = false;
//    while (!Display.isCloseRequested() && !done) {
//      if (callback != null) {
//        callback.process(nifty);
//      }
//
//      // Show rendering.
//      Display.update();
//
//      if (nifty.update()) {
//        done = true;
//      }
//
//      nifty.render(true);
//
//      // Check for OpenGL errors at least once per frame.
//      int error = GL11.glGetError();
//      if (error != GL11.GL_NO_ERROR) {
//        String glerrmsg = GLU.gluErrorString(error);
//        log.warning("OpenGL Error: (" + error + ") " + glerrmsg);
//      }
//    }
//  }
//
//  private static void shutDown() {
//    inputSystem.shutdown();
//    Display.destroy();
//    System.exit(0);
//  }
}
