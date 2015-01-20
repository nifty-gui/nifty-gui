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
package de.lessvoid.nifty.internal;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.lessvoid.nifty.api.NiftyNodeState;
import de.lessvoid.nifty.api.NiftyStateSetter;

public class StateManagerTest {
  private InternalStateManager stateManager;
  private TestTarget testTarget;
  private NiftyStateSetter<TestTarget, String> setter;

  private static class TestTarget {
    
  }

  @Before
  public void before() {
    testTarget = createMock(TestTarget.class);
    setter = createMock(NiftyStateSetter.class);
    stateManager = new InternalStateManager();
  }

  @After
  public void after() {
    verify(testTarget);
    verify(setter);
  }

  @Test
  public void testSetValue_NullStates() {
    replay(testTarget);

    setter.set(testTarget, "value", NiftyNodeState.Regular);
    replay(setter);

    stateManager.setValue(testTarget, "value", setter);
  }

  @Test
  public void testSetValue_EmptyStates() {
    replay(testTarget);

    setter.set(testTarget, "value", NiftyNodeState.Regular);
    replay(setter);

    stateManager.setValue(testTarget, "value", setter, new NiftyNodeState[0]);
  }

  @Test
  public void testSetValue_RegularStateOnly() {
    replay(testTarget);

    setter.set(testTarget, "value", NiftyNodeState.Regular);
    replay(setter);

    stateManager.setValue(testTarget, "value", setter, NiftyNodeState.Regular);
  }

  @Test
  public void testSetValue_HoverOnly() {
    replay(testTarget);
    replay(setter);

    stateManager.setValue(testTarget, "value", setter, NiftyNodeState.Hover);
  }

  @Test
  public void testActivateStates_Default() {
    replay(testTarget);
    replay(setter);

    stateManager.activateStates();
  }

  @Test
  public void testActivateStates_WithRegular() {
    replay(testTarget);

    setter.set(testTarget, "value", NiftyNodeState.Regular);
    setter.set(testTarget, "value", NiftyNodeState.Regular);
    replay(setter);

    stateManager.setValue(testTarget, "value", setter, NiftyNodeState.Regular);
    stateManager.activateStates();
  }

  @Test
  public void testActivateStates_WithHover() {
    replay(testTarget);
    replay(setter);

    stateManager.setValue(testTarget, "value", setter, NiftyNodeState.Hover);
    stateManager.activateStates();
  }

  @Test
  public void testActivateStates_WithHoverActive() {
    replay(testTarget);

    setter.set(testTarget, "value", NiftyNodeState.Hover);
    replay(setter);

    stateManager.setValue(testTarget, "value", setter, NiftyNodeState.Hover);
    stateManager.activateStates(NiftyNodeState.Hover);
  }

  @Test
  public void testToString_Default() {
    replay(testTarget);
    replay(setter);

    assertEquals(
        "current states: [Regular], available: {Regular=[], Hover=[]}",
        stateManager.toString());
  }

  @Test
  public void testToString_Hover() {
    replay(testTarget);

    setter.set(testTarget, "value", NiftyNodeState.Hover);
    replay(setter);

    stateManager.setValue(testTarget, "value", setter, NiftyNodeState.Hover);
    stateManager.activateStates(NiftyNodeState.Hover);

    assertEquals(
        "current states: [Regular, Hover], available: {Regular=[], Hover=[value]}",
        stateManager.toString());
  }
}
