package de.lessvoid.nifty.renderer.lwjgl3.input;

import static org.lwjgl.glfw.GLFW.*;

import java.nio.DoubleBuffer;
import java.nio.IntBuffer;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Logger;

import javax.annotation.Nonnull;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWScrollCallback;

import de.lessvoid.nifty.NiftyInputConsumer;
import de.lessvoid.nifty.input.keyboard.KeyboardInputEvent;
import de.lessvoid.nifty.spi.input.InputSystem;
import de.lessvoid.nifty.tools.resourceloader.NiftyResourceLoader;

/**
 * Port of <code>de.lessvoid.nifty.renderer.lwjgl.LwjglInputSystem</code>
 * to LWJGL3/GLFW.
 * 
 * @author Brian Groenke
 */
public class Lwjgl3InputSystem implements InputSystem {
  
  private final Logger log = Logger.getLogger(Lwjgl3InputSystem.class.getName());
  private final long glfwWindow;
  private final DoubleBuffer cursorX = BufferUtils.createDoubleBuffer(1);
  private final DoubleBuffer cursorY = BufferUtils.createDoubleBuffer(1);
  @Nonnull
  private final ConcurrentLinkedQueue<MouseInputEvent> mouseEventsOut = new ConcurrentLinkedQueue<MouseInputEvent>();
  private final ConcurrentLinkedQueue<KeyboardInputEvent> keyboardEventsOut = new
      ConcurrentLinkedQueue<KeyboardInputEvent>();
  
  public final GLFWKeyCallback keyCallback = new GLFWKeyCallback() {
    @Override
    public void invoke(long window, int key, int scancode, int action, int mods) {
      keyboardEventsOut.offer(createKeyEvent(key, scancode, action, mods));
    }
  };
  
  public final GLFWCursorPosCallback cursorPosCallback = new GLFWCursorPosCallback() {
    @Override
    public void invoke(long window, double xpos, double ypos) {
      mouseEventsOut.offer(createMouseEvent((float)xpos, (float)ypos));
    }
  };
  
  public final GLFWMouseButtonCallback mouseButtonCallback = new GLFWMouseButtonCallback() {
    @Override
    public void invoke(long window, int button, int action, int mods) {
      mouseEventsOut.offer(createMouseEvent(button, action == GLFW_PRESS));
    }
  };
  
  public final GLFWScrollCallback scrollCallback = new GLFWScrollCallback(){
    @Override
    public void invoke(long window, double xoffset, double yoffset) {
      mouseEventsOut.offer(createMouseEvent((int) yoffset));
    }
  };
  
  private boolean initialized = false;
  
  public Lwjgl3InputSystem (final long glfwWindow) {
    this.glfwWindow = glfwWindow;
  }

  @Override
  public void setResourceLoader(@Nonnull final NiftyResourceLoader resourceLoader) {
  }

  public void startup() throws Exception {
    log.finer("Initializing LWJGL3 input system...");
    
    initialized = true;
  }

  public void shutdown() {
    log.finer("Shutting down LWJGL3 input system...");
    mouseEventsOut.clear();
    keyboardEventsOut.clear();
    
    initialized = false;
  }

  // InputSystem Implementation

  @Override
  public void forwardEvents(@Nonnull final NiftyInputConsumer inputEventConsumer) {
    if (!initialized) return;
    
    mouseEventsOut.clear();
    keyboardEventsOut.clear();

    glfwPollEvents();
    
    while (hasNextKeyboardEvent()) {
      inputEventConsumer.processKeyboardEvent(nextKeyboardEvent());
    }

    while (hasNextMouseEvent()) {
      final MouseInputEvent mouseEvent = nextMouseEvent();
      inputEventConsumer.processMouseEvent((int) mouseEvent.mouseX, (int) mouseEvent.mouseY, mouseEvent.scrollY,
          mouseEvent.button, mouseEvent.buttonDown);
    }
  }

  @Override
  public void setMousePosition(final int x, final int y) {
    final IntBuffer xpos = IntBuffer.allocate(1);
    final IntBuffer ypos = IntBuffer.allocate(1);
    glfwGetWindowPos(glfwWindow, xpos, ypos);
    glfwSetCursorPos(glfwWindow, x - xpos.get(0), y - ypos.get(0));
  }

  // Additional methods to access events in case they've got not handled by Nifty

  /**
   * This can be called the check if any mouse events have not been handled by Nifty.
   *
   * @return true when mouse events are available and false if not
   */
  public boolean hasNextMouseEvent() {
    return mouseEventsOut.peek() != null;
  }

  /**
   * Retrieve a unhandled mouse event from the internal queue.
   *
   * @return MouseInputEvent of the mouse event that was not handled by Nifty
   */
  public MouseInputEvent nextMouseEvent() {
    return mouseEventsOut.poll();
  }

  /**
   * This can be called the check if any keyboard events have not been handled by Nifty.
   *
   * @return true when keyboard events are available and false if not
   */
  public boolean hasNextKeyboardEvent() {
    return keyboardEventsOut.peek() != null;
  }

  /**
   * Retrieve a unhandled keyboard event from the internal queue.
   *
   * @return KeyboardInputEvent of the event that was not handled by Nifty
   */
  public KeyboardInputEvent nextKeyboardEvent() {
    return keyboardEventsOut.poll();
  }
  
  @Nonnull
  private KeyboardInputEvent createKeyEvent(final int key, final int scancode, final int action, final int mods) {
    final boolean keyDown = action == GLFW_PRESS || action == GLFW_REPEAT;
    final boolean shiftDown = (mods & GLFW_MOD_SHIFT) != 0;
    final boolean ctrlDown = (mods & GLFW_MOD_CONTROL) != 0;
    final int niftyKeyCode = GlfwToNiftyKeyCodeConverter.convertToNiftyKeyCode(key);
    final String keyName = glfwGetKeyName (key, scancode);
    final char keyChar = (keyName != null && keyName.length() == 1) ? keyName.charAt(0) : Character.MIN_VALUE;
    return new KeyboardInputEvent(niftyKeyCode, keyChar, keyDown, shiftDown, ctrlDown);
  }
  
  private MouseInputEvent createMouseEvent(final float xpos, final float ypos) {
    return new MouseInputEvent(xpos, ypos, 0, -1, false);
  }
  
  private MouseInputEvent createMouseEvent(final int button, final boolean buttonDown) {
    updateCursorPos();
    final float mx = (float) cursorX.get(0);
    final float my = (float) cursorY.get(0);
    return new MouseInputEvent(mx, my, 0, button, buttonDown);
  }
  
  private MouseInputEvent createMouseEvent(final int scrollY) {
    updateCursorPos();
    final float mx = (float) cursorX.get(0);
    final float my = (float) cursorY.get(0);
    return new MouseInputEvent(mx, my, scrollY, 0, false);
  }
  
  private void updateCursorPos() {
    glfwGetCursorPos(glfwWindow, cursorX, cursorY);
  }

  public static class MouseInputEvent {
    public float mouseX;
    public float mouseY;
    public int button;
    public int scrollY;
    public boolean buttonDown;

    MouseInputEvent(float mx, float my, int scrollY, int button, boolean buttonDown) {
      this.mouseX = mx;
      this.mouseY = my;
      this.button = button;
      this.scrollY = scrollY;
      this.buttonDown = buttonDown;
    }

    @Nonnull
    @Override
    public String toString() {
      return this.button + "=" + this.buttonDown + " at " + this.mouseX + "," + this.mouseY + " scroll:" + this.scrollY;
    }
  }
}
