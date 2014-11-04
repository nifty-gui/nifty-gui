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
import de.lessvoid.nifty.api.event.NiftyMouseEnterNodeEvent;
import de.lessvoid.nifty.api.event.NiftyMouseExitNodeEvent;
import de.lessvoid.nifty.api.event.NiftyMouseHoverEvent;
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
    replayInternalNiftyNode();
    inputHandler.pointerEvent(null, internalNiftyNode, makePointerEvent(0, 0));
  }

  @Test
  public void testInside() {
    expectEventBusNiftyMouseEnterNodeEvent();
    expectEventBusNiftyMouseHoverEvent();
    replayEventBus();

    prepareInternalNiftyNode(50, 50);
    execTest(50, 50);
  }

  @Test
  public void testInsideBorderLeft() {
    expectEventBusNiftyMouseEnterNodeEvent();
    expectEventBusNiftyMouseHoverEvent();
    replayEventBus();

    prepareInternalNiftyNode(0, 50);
    execTest(0, 50);
  }

  @Test
  public void testInsideBorderRight() {
    expectEventBusNiftyMouseEnterNodeEvent();
    expectEventBusNiftyMouseHoverEvent();
    replayEventBus();

    prepareInternalNiftyNode(100, 50);
    execTest(100, 50);
  }

  @Test
  public void testInsideBorderTop() {
    expectEventBusNiftyMouseEnterNodeEvent();
    expectEventBusNiftyMouseHoverEvent();
    replayEventBus();

    prepareInternalNiftyNode(100, 0);
    execTest(100, 0);
  }

  @Test
  public void testInsideBorderBottom() {
    expectEventBusNiftyMouseEnterNodeEvent();
    expectEventBusNiftyMouseHoverEvent();
    replayEventBus();

    prepareInternalNiftyNode(100, 100);
    execTest(100, 100);
  }

  @Test
  public void testMouseExitEvent() {
    expectEventBusNiftyMouseEnterNodeEvent();
    expectEventBusNiftyMouseHoverEvent();
    orepareEventBusNiftyMouseExitNodeEvent();
    replayEventBus();

    prepareInternalNiftyNodeStandard();
    prepareInternalNiftyNodeScreenToLocal(100, 100);
    prepareInternalNiftyNodeScreenToLocal(150, 150);
    replayInternalNiftyNode();

    execTest(100, 100);
    execTest(150, 150);
  }

  @Test
  public void testOutside() {
    replayEventBus();

    expect(internalNiftyNode.getWidth()).andReturn(100);
    prepareInternalNiftyNodeScreenToLocal(150, 150);
    replayInternalNiftyNode();

    inputHandler.pointerEvent(eventBus, internalNiftyNode, makePointerEvent(150, 150));
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
    expect(internalNiftyNode.getId()).andReturn(12).anyTimes();
  }

  private void prepareInternalNiftyNodeScreenToLocal(final int x, final int y) {
    expect(internalNiftyNode.screenToLocal(x, y)).andReturn(new Vec4(x, y, 0.f, 1.f));
  }

  private void expectEventBusNiftyMouseHoverEvent() {
    eventBus.publish(isA(NiftyMouseHoverEvent.class));
  }

  private void expectEventBusNiftyMouseEnterNodeEvent() {
    eventBus.publish(isA(NiftyMouseEnterNodeEvent.class));
  }

  private void orepareEventBusNiftyMouseExitNodeEvent() {
    eventBus.publish(isA(NiftyMouseExitNodeEvent.class));
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

  private void execTest(final int x, final int y) {
    inputHandler.pointerEvent(eventBus, internalNiftyNode, makePointerEvent(x, y));
  }
}
