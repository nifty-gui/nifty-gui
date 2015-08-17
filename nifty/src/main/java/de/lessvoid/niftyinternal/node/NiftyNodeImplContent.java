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
package de.lessvoid.niftyinternal.node;

import de.lessvoid.nifty.NiftyState;
import de.lessvoid.nifty.node.NiftyContentNode;
import de.lessvoid.nifty.spi.NiftyNodeImpl;
import de.lessvoid.nifty.spi.NiftyNodeStateImpl;
import de.lessvoid.nifty.types.NiftyColor;

import static de.lessvoid.nifty.NiftyState.NiftyStandardState.NiftyStateBackgroundColor;

/**
 * Created by void on 09.08.15.
 */
public class NiftyNodeImplContent implements NiftyNodeStateImpl, NiftyNodeImpl<NiftyContentNode> {
  private NiftyContentNode niftyNode;

  @Override
  public void initialize(final NiftyContentNode niftyNode) {
    this.niftyNode = niftyNode;
  }

  @Override
  public NiftyContentNode getNiftyNode() {
    return niftyNode;
  }

  @Override
  public void update(final NiftyState niftyState) {
    NiftyColor color = niftyState.getState(NiftyStateBackgroundColor);
    // TODO
  }
}
