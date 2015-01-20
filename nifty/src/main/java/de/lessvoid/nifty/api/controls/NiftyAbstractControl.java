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
import de.lessvoid.nifty.api.NiftyStateManager;
import de.lessvoid.nifty.internal.accessor.NiftyNodeAccessor;

/**
 * This abstract class already implements a couple of NiftyControl method. Use this class as the base class for
 * custom controls.
 *
 * @author void
 */
public abstract class NiftyAbstractControl implements NiftyControl {
  protected NiftyNode niftyNode;
  protected NiftyStateManager stateManager;

  @Override
  public void init(final NiftyNode niftyNode, final NiftyStateManager stateManager) {
    this.niftyNode = niftyNode;
    this.stateManager = stateManager;
    NiftyNodeAccessor.getDefault().getInternalNiftyNode(niftyNode).setControl(this);
  }

  @Override
  public NiftyNode getNode() {
    return niftyNode;
  }

  @Override
  public void enable() {
    // TODO
  }

  @Override
  public void disable() {
    // TODO
  }

  @Override
  public void setEnabled(final boolean enabled) {
    // TODO
  }

  @Override
  public boolean isEnabled() {
    // TODO
    return false;
  }

  @Override
  public void setFocus() {
    // TODO
  }

  @Override
  public void setFocusable(boolean focusable) {
    // TODO
  }

  @Override
  public boolean hasFocus() {
    // TODO
    return false;
  }

  @Override
  public String toString() {
    return this.getClass().getSimpleName() + niftyNode.toString();
  }
}
