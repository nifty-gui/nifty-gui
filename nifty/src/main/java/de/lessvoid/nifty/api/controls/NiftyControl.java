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
package de.lessvoid.nifty.api.controls;

import de.lessvoid.nifty.api.NiftyNode;

/**
 * Interface for all Nifty Controls.
 *
 * @author void
 */
public interface NiftyControl {

  /**
   * Initialize this NiftyControl with the given NiftyNode.
   * @param node NiftyNode
   */
  void init(NiftyNode node);

  /**
   * Get the NiftyNode for this Nifty control.
   *
   * @return the NiftyNode
   */
  NiftyNode getNode();

  /**
   * Enable the control.
   */
  void enable();

  /**
   * Disable the control.
   */
  void disable();

  /**
   * Set the enabled state from the given boolean.
   *
   * @param enabled the new enabled state
   */
  void setEnabled(boolean enabled);

  /**
   * Get the current enabled state of the control.
   *
   * @return {@code true} in case the element is enabled
   */
  boolean isEnabled();

  /**
   * Set the focus to this control.
   */
  void setFocus();

  /**
   * Change if this control is focusable (if it can get the focus or not). Usually controls are set up
   * with focusable="true" but you can change this here if necessary.
   *
   * @param focusable true when this element can get the focus and false when not
   */
  void setFocusable(boolean focusable);

  /**
   * Returns true if this control has the focus.
   *
   * @return true, when the control has the focus and false if not
   */
  boolean hasFocus();
}
