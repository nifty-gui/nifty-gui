/*
 * Copyright (c) 2015, Nifty GUI Community 
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

import javax.annotation.Nonnull;

import org.lwjgl.input.Keyboard;

import de.lessvoid.nifty.api.input.NiftyKeyboardEvent;


/**
 * NiftyKeyboardInputEventFactory.
 *
 * @author void
 */
public class NiftyKeyboardInputEventFactory {
  private boolean shiftDown = false;
  private boolean controlDown = false;

  /**
   * Create a NiftyKeyboardEvent.
   *
   * @param key       key code
   * @param character key character
   * @param keyDown   is key down?
   * @return NiftyKeyboardEvent
   */
  @Nonnull
  public NiftyKeyboardEvent createEvent(final int key, final char character, final boolean keyDown) {
    if (isShiftDown(key, keyDown)) {
      shiftDown = true;
    } else if (isShiftUp(key, keyDown)) {
      shiftDown = false;
    } else if (isControlDown(key, keyDown)) {
      controlDown = true;
    } else if (isControlUp(key, keyDown)) {
      controlDown = false;
    }
    // because Nifty uses the same keyboard encoding like lwjgl does, we can directly forward
    // the keyboard event to Nifty without the need for conversion
    return new NiftyKeyboardEvent(key, character, keyDown, shiftDown, controlDown);
  }

  /**
   * checks if the shift key is given.
   *
   * @param key key
   * @return true when shift has been pressed and false otherwise
   */
  private boolean isShiftKey(final int key) {
    return key == Keyboard.KEY_LSHIFT || key == Keyboard.KEY_RSHIFT;
  }

  /**
   * check if shift is down.
   *
   * @param key     key to check
   * @param keyDown keyDown
   * @return true when left or right shift has been pressed
   */
  private boolean isShiftDown(final int key, final boolean keyDown) {
    return keyDown && isShiftKey(key);
  }

  /**
   * check if shift is up.
   *
   * @param key     key
   * @param keyDown keyDown
   * @return true when left or right shift has been released
   */
  private boolean isShiftUp(final int key, final boolean keyDown) {
    return !keyDown && isShiftKey(key);
  }

  /**
   * check if the given key is the controlKey.
   *
   * @param key key
   * @return true left or right control key pressed and false otherwise
   */
  private boolean isControlKey(final int key) {
    return key == Keyboard.KEY_RCONTROL || key == Keyboard.KEY_LCONTROL || key == Keyboard.KEY_LMETA || key ==
        Keyboard.KEY_RMETA;
  }

  /**
   * check if control key is down.
   *
   * @param key     key
   * @param keyDown keyDown
   * @return controlDown
   */
  private boolean isControlDown(final int key, final boolean keyDown) {
    return keyDown && isControlKey(key);
  }

  /**
   * check if control key is up.
   *
   * @param key     key
   * @param keyDown keyDown
   * @return controlDown
   */
  private boolean isControlUp(final int key, final boolean keyDown) {
    return !keyDown && isControlKey(key);
  }
}
