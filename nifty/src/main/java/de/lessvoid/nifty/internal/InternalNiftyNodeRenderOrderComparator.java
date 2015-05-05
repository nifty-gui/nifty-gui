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
package de.lessvoid.nifty.internal;

import java.util.Comparator;

import javax.annotation.Nonnull;

import de.lessvoid.nifty.api.NiftyNode;
import de.lessvoid.nifty.internal.accessor.NiftyNodeAccessor;

/**
 * This uses the renderOrder attribute of the NiftyNodes to compare them. If the renderOrder
 * attribute is not set (is 0) then the index of the element in the elements list is used
 * as the renderOrder value. This is done to keep the original sort order of the elements for
 * input processing.
 */
public class InternalNiftyNodeRenderOrderComparator implements Comparator<NiftyNode> {

  @Override
  public int compare(@Nonnull final NiftyNode o1, @Nonnull final NiftyNode o2) {
    if (o1 == o2) {
      return 0;
    }
    int o1RenderOrder = getRenderOrder(o1);
    int o2RenderOrder = getRenderOrder(o2);

    if (o1RenderOrder < o2RenderOrder) {
      return -1;
    } else if (o1RenderOrder > o2RenderOrder) {
      return 1;
    }

    // this means the renderOrder values are equal. since this is a set
    // we can't return 0 because this would mean (for the set) that the
    // elements are equal and one of the values will be removed. so here
    // we simply compare the String representation of the elements so that
    // we keep a fixed sort order.
    String o1Id = o1.toString();
    String o2Id = o2.toString();
    return o1Id.compareTo(o2Id);
  }

  private int getRenderOrder(@Nonnull final NiftyNode niftyNode) {
    if (niftyNode.getRenderOrder() != 0) {
      return niftyNode.getRenderOrder();
    }
    return NiftyNodeAccessor.getDefault().getInternalNiftyNode(niftyNode).getInputOrderIndex();
  }
}
