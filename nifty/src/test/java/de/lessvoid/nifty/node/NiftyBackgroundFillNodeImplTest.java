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

import de.lessvoid.nifty.NiftyState;
import de.lessvoid.nifty.types.NiftyColor;
import de.lessvoid.nifty.types.NiftyLinearGradient;
import de.lessvoid.niftyinternal.accessor.NiftyStateAccessor;
import org.junit.Test;

import static de.lessvoid.nifty.NiftyState.NiftyStandardState.NiftyStateBackgroundFill;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Created by void on 28.02.16.
 */
public class NiftyBackgroundFillNodeImplTest {

  @Test
  public void testConstructorBackgroundColor() {
    NiftyBackgroundFillNodeImpl impl = new NiftyBackgroundFillNodeImpl(NiftyColor.aqua());
    assertEquals(NiftyColor.aqua(), impl.getBackgroundColor());
    assertNull(impl.getBackgroundGradient());
  }

  @Test
  public void testChangeBackgroundColor() {
    NiftyBackgroundFillNodeImpl impl = new NiftyBackgroundFillNodeImpl(NiftyColor.aqua());
    impl.setBackgroundColor(NiftyColor.fuchsia());
    assertEquals(NiftyColor.fuchsia(), impl.getBackgroundColor());
  }

  @Test
  public void testBackgroundColorUpdate() {
    NiftyBackgroundFillNodeImpl impl = new NiftyBackgroundFillNodeImpl(NiftyColor.aqua());
    NiftyState state = NiftyStateAccessor.getDefault().newNiftyState();
    impl.update(state);
    assertEquals(NiftyColor.aqua(), state.getState(NiftyStateBackgroundFill, null));
  }

  @Test
  public void testConstructorBackgroundGradient() {
    NiftyBackgroundFillNodeImpl impl = new NiftyBackgroundFillNodeImpl(NiftyLinearGradient.createFromAngleInDeg(0));
    assertNull(impl.getBackgroundColor());
    assertEquals(NiftyLinearGradient.createFromAngleInDeg(0), impl.getBackgroundGradient());
  }

  @Test
  public void testChangeBackgroundGradient() {
    NiftyBackgroundFillNodeImpl impl = new NiftyBackgroundFillNodeImpl(NiftyLinearGradient.createFromAngleInDeg(0));
    impl.setBackgroundGradient(NiftyLinearGradient.createFromAngleInDeg(90));
    assertEquals(NiftyLinearGradient.createFromAngleInDeg(90), impl.getBackgroundGradient());
  }

  @Test
  public void testBackgroundGradientUpdate() {
    NiftyBackgroundFillNodeImpl impl = new NiftyBackgroundFillNodeImpl(NiftyLinearGradient.createFromAngleInDeg(0));
    NiftyState state = NiftyStateAccessor.getDefault().newNiftyState();
    impl.update(state);
    assertEquals(NiftyLinearGradient.createFromAngleInDeg(0), state.getState(NiftyStateBackgroundFill, null));
  }

  @Test
  public void testBackgroundGradientUpdateWithColorSetAsWell() {
    NiftyBackgroundFillNodeImpl impl = new NiftyBackgroundFillNodeImpl(NiftyLinearGradient.createFromAngleInDeg(0));
    impl.setBackgroundColor(NiftyColor.fuchsia());
    NiftyState state = NiftyStateAccessor.getDefault().newNiftyState();
    impl.update(state);
    assertEquals(NiftyLinearGradient.createFromAngleInDeg(0), state.getState(NiftyStateBackgroundFill, null));
  }

  @Test
  public void testGetNiftyNode() {
    NiftyBackgroundFillNodeImpl impl = new NiftyBackgroundFillNodeImpl(NiftyLinearGradient.createFromAngleInDeg(0));
    assertEquals(impl, impl.getNiftyNode().getImpl());
  }
}
