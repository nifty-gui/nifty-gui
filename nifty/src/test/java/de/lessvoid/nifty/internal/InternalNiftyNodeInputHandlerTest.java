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

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.lessvoid.nifty.api.NiftyNode;
import de.lessvoid.nifty.api.event.NiftyPointerClickedEvent;
import de.lessvoid.nifty.api.event.NiftyPointerDraggedEvent;
import de.lessvoid.nifty.api.event.NiftyPointerEnterNodeEvent;
import de.lessvoid.nifty.api.event.NiftyPointerExitNodeEvent;
import de.lessvoid.nifty.api.event.NiftyPointerHoverEvent;
import de.lessvoid.nifty.api.event.NiftyPointerPressedEvent;
import de.lessvoid.nifty.api.event.NiftyPointerReleasedEvent;
import de.lessvoid.nifty.api.input.NiftyPointerEvent;
import de.lessvoid.nifty.internal.math.Vec4;

public class InternalNiftyNodeInputHandlerTest {
  private InternalNiftyNodeInputHandler inputHandler;
  private InternalNiftyNode internalNiftyNode;
  private NiftyNode niftyNode;
  private InternalNiftyEventBus eventBus;

  @Before
  public void before() {
    internalNiftyNode = createMock(InternalNiftyNode.class);
    niftyNode = createNiceMock(NiftyNode.class);
    replay(niftyNode);
    eventBus = createMock(InternalNiftyEventBus.class);
    inputHandler = new InternalNiftyNodeInputHandler();
  }

  @After
  public void after() {
    verify(internalNiftyNode);
    verify(eventBus);
    verify(niftyNode);
  }

  @Test
  public void testNoEventBus() {
    replayEventBus();

    prepareInternalNiftyNodeStandard();
    prepareInternalNiftyNodeScreenToLocal(0, 0);
    internalNiftyNode.onHover(isA(NiftyPointerEvent.class));
    replayInternalNiftyNode();

    inputHandler.pointerEvent(null, internalNiftyNode, makePointerEvent(0, 0));
  }

  @Test
  public void testInside() {
    expectEventBusNiftyPointerEnterNodeEvent();
    expectEventBusNiftyPointerHoverEvent();
    replayEventBus();

    prepareInternalNiftyNode(50, 50);
    execPointerEvent(50, 50);
  }

  @Test
  public void testInsideBorderLeft() {
    expectEventBusNiftyPointerEnterNodeEvent();
    expectEventBusNiftyPointerHoverEvent();
    replayEventBus();

    prepareInternalNiftyNode(0, 50);
    execPointerEvent(0, 50);
  }

  @Test
  public void testInsideBorderRight() {
    expectEventBusNiftyPointerEnterNodeEvent();
    expectEventBusNiftyPointerHoverEvent();
    replayEventBus();

    prepareInternalNiftyNode(100, 50);
    execPointerEvent(100, 50);
  }

  @Test
  public void testInsideBorderTop() {
    expectEventBusNiftyPointerEnterNodeEvent();
    expectEventBusNiftyPointerHoverEvent();
    replayEventBus();

    prepareInternalNiftyNode(100, 0);
    execPointerEvent(100, 0);
  }

  @Test
  public void testInsideBorderBottom() {
    expectEventBusNiftyPointerEnterNodeEvent();
    expectEventBusNiftyPointerHoverEvent();
    replayEventBus();

    prepareInternalNiftyNode(100, 100);
    execPointerEvent(100, 100);
  }

  @Test
  public void testExitEvent() {
    expectEventBusNiftyPointerEnterNodeEvent();
    expectEventBusNiftyPointerHoverEvent();
    orepareEventBusNiftyPointerExitNodeEvent();
    replayEventBus();

    prepareInternalNiftyNodeStandard();
    prepareInternalNiftyNodeScreenToLocal(100, 100);
    prepareInternalNiftyNodeScreenToLocal(150, 150);
    internalNiftyNode.onExit();
    replayInternalNiftyNode();

    execPointerEvent(100, 100);
    execPointerEvent(150, 150);
  }

  @Test
  public void testOutside() {
    replayEventBus();

    expect(internalNiftyNode.getWidth()).andReturn(100);
    prepareInternalNiftyNodeScreenToLocal(150, 150);
    replayInternalNiftyNode();

    inputHandler.pointerEvent(eventBus, internalNiftyNode, makePointerEvent(150, 150));
  }

  @Test
  public void testClicked() {
    expectEventBusNiftyPointerEnterNodeEvent();
    expectEventBusNiftyPointerHoverEvent();

    expectEventBusNiftyPointerHoverEvent();
    expectEventBusNiftyPointerPressedEvent();

    expectEventBusNiftyPointerHoverEvent();
    expectEventBusNiftyPointerClickedEvent();
    expectEventBusNiftyPointerReleasedEvent();
    replayEventBus();

    prepareInternalNiftyNode(50, 50);
    execPointerEvent(50, 50);
    execPointerDownEvent(50, 50);
    execPointerUpEvent(50, 50);
  }

  @Test
  public void testDragged() {
    expectEventBusNiftyPointerEnterNodeEvent();
    expectEventBusNiftyPointerHoverEvent();

    expectEventBusNiftyPointerHoverEvent();
    expectEventBusNiftyPointerPressedEvent();

    expectEventBusNiftyPointerHoverEvent();
    expectEventBusNiftyPointerDraggedEvent();
    replayEventBus();

    prepareInternalNiftyNodeStandard();
    prepareInternalNiftyNodeScreenToLocal(50, 50);
    prepareInternalNiftyNodeScreenToLocal(51, 51);
    replayInternalNiftyNode();

    execPointerEvent(50, 50);
    execPointerDownEvent(50, 50);
    execPointerDownEvent(51, 51);
  }

  private void prepareInternalNiftyNode(final int x, final int y) {
    prepareInternalNiftyNodeStandard();
    prepareInternalNiftyNodeScreenToLocal(x, y);
    replayInternalNiftyNode();
  }

  private void replayInternalNiftyNode() {
    replay(internalNiftyNode);
  }

  private void prepareInternalNiftyNodeStandard() {
    expect(internalNiftyNode.getNiftyNode()).andReturn(niftyNode).anyTimes();
    expect(internalNiftyNode.getWidth()).andReturn(100).anyTimes();
    expect(internalNiftyNode.getHeight()).andReturn(100).anyTimes();
    expect(internalNiftyNode.getId()).andReturn("12").anyTimes();
  }

  private void prepareInternalNiftyNodeScreenToLocal(final int x, final int y) {
    expect(internalNiftyNode.screenToLocal(x, y)).andReturn(new Vec4(x, y, 0.f, 1.f)).anyTimes();
  }

  private void expectEventBusNiftyPointerHoverEvent() {
    internalNiftyNode.onHover(isA(NiftyPointerEvent.class));
    eventBus.publish(isA(NiftyPointerHoverEvent.class));
  }

  private void expectEventBusNiftyPointerDraggedEvent() {
    eventBus.publish(isA(NiftyPointerDraggedEvent.class));
  }

  private void expectEventBusNiftyPointerClickedEvent() {
    eventBus.publish(isA(NiftyPointerClickedEvent.class));
  }

  private void expectEventBusNiftyPointerReleasedEvent() {
    eventBus.publish(isA(NiftyPointerReleasedEvent.class));
  }

  private void expectEventBusNiftyPointerEnterNodeEvent() {
    eventBus.publish(isA(NiftyPointerEnterNodeEvent.class));
  }

  private void orepareEventBusNiftyPointerExitNodeEvent() {
    eventBus.publish(isA(NiftyPointerExitNodeEvent.class));
  }

  private void expectEventBusNiftyPointerPressedEvent() {
    eventBus.publish(isA(NiftyPointerPressedEvent.class));
  }

  private void replayEventBus() {
    replay(eventBus);
  }

  private NiftyPointerEvent makePointerEvent(final int x, final int y) {
    NiftyPointerEvent event = new NiftyPointerEvent();
    event.setX(x);
    event.setY(y);
    return event;
  }

  private NiftyPointerEvent makePointerDownEvent(final int x, final int y) {
    NiftyPointerEvent event = new NiftyPointerEvent();
    event.setX(x);
    event.setY(y);
    event.setButton(0);
    event.setButtonDown(true);
    return event;
  }

  private NiftyPointerEvent makePointerUpEvent(final int x, final int y) {
    NiftyPointerEvent event = new NiftyPointerEvent();
    event.setX(x);
    event.setY(y);
    event.setButton(0);
    event.setButtonDown(false);
    return event;
  }

  private void execPointerEvent(final int x, final int y) {
    inputHandler.pointerEvent(eventBus, internalNiftyNode, makePointerEvent(x, y));
  }

  private void execPointerUpEvent(final int x, final int y) {
    inputHandler.pointerEvent(eventBus, internalNiftyNode, makePointerUpEvent(x, y));
  }

  private void execPointerDownEvent(final int x, final int y) {
    inputHandler.pointerEvent(eventBus, internalNiftyNode, makePointerDownEvent(x, y));
  }
}
