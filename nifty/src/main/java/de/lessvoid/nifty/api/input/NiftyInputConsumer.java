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
package de.lessvoid.nifty.api.input;



/**
 * NiftyInputConsumer consumes InputEvents from the InputSystem.
 * @author void
 */
public interface NiftyInputConsumer {

  /**
   * Send the given mouse event to Nifty for processing.
   * @param mouseX the mouse x position when the event occurred
   * @param mouseY the mouse y position when the event occurred
   * @param mouseWheel the mouse wheel event
   * @param button the button that has been pressed with -1 = no button, 0 = first button, 1 = second button and so on
   * @param buttonDown the button was pressed down (true) or has been released (false)
   * @return true this event has been handled by nifty and false when not (in the later case this event should be processed by the caller)
   */
  boolean processPointerEvent(NiftyPointerEvent ... pointerEvents);

  /**
   * Process the given keyboard event.
   * @param keyEvent the keyboard event to process
   * @return true, when the keyboard event has been processed and false, if not
   */
  boolean processKeyboardEvent(NiftyKeyboardEvent keyEvent);
}
