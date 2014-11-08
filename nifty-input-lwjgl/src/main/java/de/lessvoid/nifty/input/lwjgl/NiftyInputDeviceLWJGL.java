/*
 * Copyright (c) 2014, Jens Hohmuth 
 * All rights reserved. 
 * 
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are 
 * met: 
 * 
 *  * Redistributions of source code must retain the above copyright 
 *    notice, this list of conditions and the following disclaimer. 
 *  * Redistributions in binary form must reproduce the above copyright 
 *    notice, this list of conditions and the following disclaimer in the 
 *    documentation and/or other materials provided with the distribution. 
 * 
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR AND CONTRIBUTORS ``AS IS'' AND 
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR 
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHOR OR CONTRIBUTORS BE 
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR 
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF 
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS 
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN 
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF 
 * THE POSSIBILITY OF SUCH DAMAGE.
 */
package de.lessvoid.nifty.input.lwjgl;

/**
 *
 * @author Joseph
 */
import java.nio.IntBuffer;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Logger;

import javax.annotation.Nonnull;

import org.lwjgl.BufferUtils;
import org.lwjgl.input.Cursor;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import de.lessvoid.nifty.api.NiftyResourceLoader;
import de.lessvoid.nifty.api.input.NiftyInputConsumer;
import de.lessvoid.nifty.api.input.NiftyKeyboardEvent;
import de.lessvoid.nifty.api.input.NiftyPointerEvent;
import de.lessvoid.nifty.spi.NiftyInputDevice;

public class NiftyInputDeviceLWJGL implements NiftyInputDevice {
  private final Logger log = Logger.getLogger(NiftyInputDeviceLWJGL.class.getName());
  @Nonnull
  private final NiftyKeyboardInputEventFactory keyboardEventCreator = new NiftyKeyboardInputEventFactory();
  private final IntBuffer viewportBuffer = BufferUtils.createIntBuffer(4 * 4);
  @Nonnull
  private final ConcurrentLinkedQueue<MouseInputEvent> mouseEventsOut = new ConcurrentLinkedQueue<MouseInputEvent>();
  private final ConcurrentLinkedQueue<NiftyKeyboardEvent> keyboardEventsOut = new ConcurrentLinkedQueue<NiftyKeyboardEvent>();

  private boolean niftyHasKeyboardFocus = true;
  private boolean niftyTakesKeyboardFocusOnClick = false;

  public NiftyInputDeviceLWJGL() throws Exception {
    Mouse.create();
    Keyboard.create();
    Keyboard.enableRepeatEvents(true);
    logMouseCapabilities();
  }

  public void shutdown() {
    Mouse.destroy();
    Keyboard.destroy();
  }

  // NiftyInputDevice Implementation

  @Override
  public void setResourceLoader(@Nonnull final NiftyResourceLoader resourceLoader) {
  }

  @Override
  public void forwardEvents(@Nonnull final NiftyInputConsumer inputEventConsumer) {
    mouseEventsOut.clear();
    keyboardEventsOut.clear();

    processMouseEvents(inputEventConsumer);
    processKeyboardEvents(inputEventConsumer);
  }

  @Override
  public void setMousePosition(final int x, final int y) {
    int viewportHeight = getViewportHeight();
    Mouse.setCursorPosition(x, viewportHeight - y);
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
  public NiftyKeyboardEvent nextKeyboardEvent() {
    return keyboardEventsOut.poll();
  }

  public boolean isNiftyHasKeyboardFocus() {
    return niftyHasKeyboardFocus;
  }

  public void setNiftyHasKeyboardFocus(final boolean niftyHasKeyboardFocus) {
    this.niftyHasKeyboardFocus = niftyHasKeyboardFocus;
  }

  public boolean isNiftyTakesKeyboardFocusOnClick() {
    return niftyTakesKeyboardFocusOnClick;
  }

  public void setNiftyTakesKeyboardFocusOnClick(final boolean niftyTakesKeyboardFocusOnClick) {
    this.niftyTakesKeyboardFocusOnClick = niftyTakesKeyboardFocusOnClick;
  }

  // Internals

  private void processMouseEvents(@Nonnull final NiftyInputConsumer inputEventConsumer) {
    // this is usually called by Display.update() but since we call Display.update() in Nifty 2.0 only when the screen
    // has actually changed we need to manually call poll() here as well.
    Mouse.poll();

    int viewportHeight = getViewportHeight();
    while (Mouse.next()) {
      int mouseX = Mouse.getEventX();
      int mouseY = viewportHeight - Mouse.getEventY();
      int mouseWheel = Mouse.getEventDWheel() / 120; // not sure about that 120 here. works on my system and makes
      // this return 1 if the wheel is moved the minimal amount.
      int button = Mouse.getEventButton();
      boolean buttonDown = Mouse.getEventButtonState();

      // now send the event to nifty
      NiftyPointerEvent event = new NiftyPointerEvent();
      event.setX(mouseX);
      event.setY(mouseY);
      event.setZ(mouseWheel);
      event.setButton(button);
      event.setButtonDown(buttonDown);

      boolean mouseEventProcessedByNifty = inputEventConsumer.processPointerEvent(event);
      if (!mouseEventProcessedByNifty) {
        log.fine("Nifty did not processed this mouse event. You can handle it.");

        // nifty did not process this event, it did not hit any element
        mouseEventsOut.offer(new MouseInputEvent(mouseX, mouseY, mouseWheel, button, buttonDown));
        if (niftyTakesKeyboardFocusOnClick) {
          log.fine("Nifty gave up the keyboard focus");
          niftyHasKeyboardFocus = false; // give up focus if clicked outside nifty
        }
      } else {
        log.fine("Nifty has processed this mouse event");

        // nifty did handle that event. it hit an element and was processed by some GUI element
        if (niftyTakesKeyboardFocusOnClick) { // take focus if nifty element is clicked
          log.fine("Nifty takes the keyboard focus back");
          niftyHasKeyboardFocus = true;
        }
      }
    }
  }

  private void processKeyboardEvents(@Nonnull final NiftyInputConsumer inputEventConsumer) {
    // this is usually called by Display.update() but since we call Display.update() in Nifty 2.0 only when the screen
    // has actually changed we need to manually call poll() here as well.
    Keyboard.poll();

    while (Keyboard.next()) {
      NiftyKeyboardEvent event = keyboardEventCreator.createEvent(Keyboard.getEventKey(),
          Keyboard.getEventCharacter(), Keyboard.getEventKeyState());
      // due to or short-circuiting on true, the event will get forward to keyboardEventsOut if keyboardEventsOut=true
      if (!niftyHasKeyboardFocus || !inputEventConsumer.processKeyboardEvent(event)) {
        keyboardEventsOut.offer(event);
      }
    }
  }

  private int getViewportHeight() {
    GL11.glGetInteger(GL11.GL_VIEWPORT, viewportBuffer);
    return viewportBuffer.get(3);
  }

  private void logMouseCapabilities() {
    int caps = Cursor.getCapabilities();
    StringBuffer out = new StringBuffer();
    if ((caps & Cursor.CURSOR_ONE_BIT_TRANSPARENCY) != 0) {
      add(out, "CURSOR_ONE_BIT_TRANSPARENCY");
    }
    if ((caps & Cursor.CURSOR_8_BIT_ALPHA) != 0) {
      add(out, "CURSOR_8_BIT_ALPHA");
    }
    if ((caps & Cursor.CURSOR_ANIMATION) != 0) {
      add(out, "CURSOR_ANIMATION");
    }
    log.fine("native cursor support (" + caps + ") -> [" + out.toString() + "]");
    log.fine("native cursor min size: " + Cursor.getMinCursorSize());
    log.fine("native cursor max size: " + Cursor.getMaxCursorSize());
  }

  private static void add(@Nonnull StringBuffer out, String text) {
    if (out.length() > 0) {
      out.append(", ");
    }
    out.append(text);
  }

  public static class MouseInputEvent {
    public float mouseX;
    public float mouseY;
    public float pmouseX;
    public float pmouseY;
    public int button;
    public int scroll;
    public boolean buttonDown;

    MouseInputEvent(float mx, float my, int scroll, int button, boolean buttonDown) {
      this.mouseX = mx;
      this.mouseY = my;
      this.button = button;
      this.scroll = scroll;
      this.buttonDown = buttonDown;
    }

    @Nonnull
    @Override
    public String toString() {
      return this.button + "=" + this.buttonDown + " at " + this.mouseX + "," + this.mouseY + " scroll:" + this.scroll;
    }
  }
}
