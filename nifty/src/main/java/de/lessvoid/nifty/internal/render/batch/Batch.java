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
package de.lessvoid.nifty.internal.render.batch;

import de.lessvoid.nifty.spi.NiftyRenderDevice;

/**
 * A Batch stores pre-transformed vertices that share the same rendering state and that can be rendered.
 */
public interface Batch<T> {

  /**
   * Render this Batch using the given RenderDevice.
   * @param renderDevice the RenderDevice to render the batch to
   */
  void render(NiftyRenderDevice renderDevice);

  /**
   * Given the parameters param this Batch should check if the state of the params is the same as the state stored in
   * this Batch. If the state is the same the method is supposed to return true. In that case the Batch is reused. If
   * the state is different the method should return false and a new Batch will be created.
   *
   * Note: The method can return true if other, batch internal properties require the use of a new Batch as well. Like
   * a full Batch.
   *
   * @param param the Parameters to check the Batch for
   * @return true when the parameters are different from the ones stored inside the Batch or other criteria will require
   * a new Batch.
   */
  boolean requiresNewBatch(T param);
}
