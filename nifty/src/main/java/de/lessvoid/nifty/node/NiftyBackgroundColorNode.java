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
package de.lessvoid.nifty.node;

import de.lessvoid.nifty.types.NiftyColor;
import de.lessvoid.nifty.spi.node.NiftyNode;

/**
 * The API for the NiftyBackgroundColorNode node.
 * Created by void on 09.08.15.
 */
public class NiftyBackgroundColorNode implements NiftyNode {
  private NiftyColor backgroundColor;

  public static NiftyBackgroundColorNode backgroundColorNode(final NiftyColor backgroundColor) {
    return new NiftyBackgroundColorNode(backgroundColor);
  }

  public NiftyBackgroundColorNode(final NiftyColor backgroundColor) {
    this.backgroundColor = backgroundColor;
  }

  public void setBackgroundColor(final NiftyColor backgroundColor) {
    this.backgroundColor = backgroundColor;
  }

  public NiftyColor getBackgroundColor() {
    return backgroundColor;
  }

  public String toString() {
    return "(" + this.getClass().getSimpleName() + ") backgroundColor [" + backgroundColor + "]";
  }
}