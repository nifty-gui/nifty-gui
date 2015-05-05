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
package de.lessvoid.nifty.api.input;

public class NiftyPointerEvent {
  public final static int BUTTON_COUNT = 5;

  private int x;
  private int y;
  private int z; // mouse wheel
  private int button;
  private boolean buttonDown;

  public int getX() {
    return x;
  }

  public void setX(final int x) {
    this.x = x;
  }

  public int getY() {
    return y;
  }

  public void setY(final int y) {
    this.y = y;
  }

  public int getZ() {
    return z;
  }

  public void setZ(final int z) {
    this.z = z;
  }

  public int getButton() {
    return button;
  }

  /**
   * First button is 0, second is 1 and so on.
   * @param button the button state for this event
   */
  public void setButton(final int button) {
    this.button = button;
  }

  public boolean isButtonDown() {
    return buttonDown;
  }

  /**
   * If the button is down (true) or not (false).
   * @param buttonDown the buttonDown flag
   */
  public void setButtonDown(final boolean buttonDown) {
    this.buttonDown = buttonDown;
  }

  public String toString() {
    StringBuilder out = new StringBuilder();
    out.append("x ").append("[").append(x).append("] ");
    out.append("y ").append("[").append(y).append("] ");
    out.append("z ").append("[").append(z).append("] ");
    out.append("button ").append("[").append(button).append("] ");
    out.append("buttonDown ").append("[").append(buttonDown).append("]");
    return out.toString();
  }
}
