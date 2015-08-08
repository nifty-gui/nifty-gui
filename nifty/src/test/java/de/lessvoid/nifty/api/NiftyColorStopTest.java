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
package de.lessvoid.nifty.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import de.lessvoid.nifty.api.types.NiftyColor;
import de.lessvoid.nifty.api.types.NiftyColorStop;
import org.junit.Test;

public class NiftyColorStopTest {

  @Test
  public void testCreate() {
    NiftyColorStop stop = new NiftyColorStop(1.0, NiftyColor.blue());
    assertEquals(1.0, stop.getStop());
    assertEquals("#0000ffff {0.0, 0.0, 1.0, 1.0}", stop.getColor().toString());
  }

  @Test
  public void testLower() {
    NiftyColorStop stopA = new NiftyColorStop(0.4, NiftyColor.blue());
    NiftyColorStop stopB = new NiftyColorStop(1.0, NiftyColor.blue());
    assertTrue(stopA.compareTo(stopB) < 0);
  }

  @Test
  public void testHigher() {
    NiftyColorStop stopA = new NiftyColorStop(1.0, NiftyColor.blue());
    NiftyColorStop stopB = new NiftyColorStop(0.4, NiftyColor.blue());
    assertTrue(stopA.compareTo(stopB) > 0);
  }

  @Test
  public void testEquals() {
    NiftyColorStop stopA = new NiftyColorStop(1.0, NiftyColor.blue());
    NiftyColorStop stopB = new NiftyColorStop(1.0, NiftyColor.blue());
    assertTrue(stopA.compareTo(stopB) == 0);
  }

  @Test
  public void testEqualsLower() {
    NiftyColorStop stopA = new NiftyColorStop(1.0, NiftyColor.black());
    NiftyColorStop stopB = new NiftyColorStop(1.0, NiftyColor.blue());
    assertTrue(stopA.compareTo(stopB) == 0);
  }

  @Test
  public void testEqualsHigher() {
    NiftyColorStop stopA = new NiftyColorStop(1.0, NiftyColor.blue());
    NiftyColorStop stopB = new NiftyColorStop(1.0, NiftyColor.black());
    assertTrue(stopA.compareTo(stopB) == 0);
  }
}
