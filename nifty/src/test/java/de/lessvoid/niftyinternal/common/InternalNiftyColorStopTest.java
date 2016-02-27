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
package de.lessvoid.niftyinternal.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import de.lessvoid.nifty.types.NiftyColor;
import org.junit.Test;

public class InternalNiftyColorStopTest {

  @Test
  public void testCreate() {
    InternalNiftyColorStop stop = new InternalNiftyColorStop(1.0, NiftyColor.blue());
    assertEquals(1.0, stop.getStop(), 1.f / 255.f);
    assertEquals("#0000ffff {0.0, 0.0, 1.0, 1.0}", stop.getColor().toString());
  }

  @Test
  public void testLower() {
    InternalNiftyColorStop stopA = new InternalNiftyColorStop(0.4, NiftyColor.blue());
    InternalNiftyColorStop stopB = new InternalNiftyColorStop(1.0, NiftyColor.blue());
    assertTrue(stopA.compareTo(stopB) < 0);
  }

  @Test
  public void testHigher() {
    InternalNiftyColorStop stopA = new InternalNiftyColorStop(1.0, NiftyColor.blue());
    InternalNiftyColorStop stopB = new InternalNiftyColorStop(0.4, NiftyColor.blue());
    assertTrue(stopA.compareTo(stopB) > 0);
  }

  @Test
  public void testEquals() {
    InternalNiftyColorStop stopA = new InternalNiftyColorStop(1.0, NiftyColor.blue());
    InternalNiftyColorStop stopB = new InternalNiftyColorStop(1.0, NiftyColor.blue());
    assertTrue(stopA.compareTo(stopB) == 0);
  }

  @Test
  public void testEqualsLower() {
    InternalNiftyColorStop stopA = new InternalNiftyColorStop(1.0, NiftyColor.black());
    InternalNiftyColorStop stopB = new InternalNiftyColorStop(1.0, NiftyColor.blue());
    assertTrue(stopA.compareTo(stopB) == 0);
  }

  @Test
  public void testEqualsHigher() {
    InternalNiftyColorStop stopA = new InternalNiftyColorStop(1.0, NiftyColor.blue());
    InternalNiftyColorStop stopB = new InternalNiftyColorStop(1.0, NiftyColor.black());
    assertTrue(stopA.compareTo(stopB) == 0);
  }
}
