package de.lessvoid.nifty.internal;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.lessvoid.nifty.api.NiftyNode;
import de.lessvoid.nifty.api.event.NiftyMouseEnterNodeEvent;
import de.lessvoid.nifty.api.event.NiftyMouseHoverEvent;
import de.lessvoid.nifty.api.input.NiftyPointerEvent;
import de.lessvoid.nifty.internal.math.Vec4;

public class InternalNiftyNodeInputHandlerTest {
  private InternalNiftyNodeInputHandler inputHandler;
  private InternalNiftyNode internalNiftyNode;
  private InternalNiftyEventBus eventBus;
  private NiftyNode niftyNode;

  @Before
  public void before() {
    internalNiftyNode = createMock(InternalNiftyNode.class);
    eventBus = createMock(InternalNiftyEventBus.class);
    inputHandler = new InternalNiftyNodeInputHandler(internalNiftyNode);
    niftyNode = createMock(NiftyNode.class);
    replay(niftyNode);
  }

  @After
  public void after() {
    verify(internalNiftyNode);
    verify(eventBus);
    verify(niftyNode);
  }

  @Test
  public void testInside() {
    prepareEventBusMock();
    prepareMock(50, 50);
    execTest(50, 50);
  }

  @Test
  public void testInsideBorderLeft() {
    prepareEventBusMock();
    prepareMock(0, 50);
    execTest(0, 50);
  }

  @Test
  public void testInsideBorderRight() {
    prepareEventBusMock();
    prepareMock(100, 50);
    execTest(100, 50);
  }

  @Test
  public void testInsideBorderTop() {
    prepareEventBusMock();
    prepareMock(100, 0);
    execTest(100, 0);
  }

  @Test
  public void testInsideBorderBottom() {
    prepareEventBusMock();
    prepareMock(100, 100);
    execTest(100, 100);
  }

  @Test
  public void testOutside() {
    replay(eventBus);
    expect(internalNiftyNode.getWidth()).andReturn(100);
    expect(internalNiftyNode.screenToLocal(150, 150)).andReturn(new Vec4(150.f, 150.f, 0.f, 1.f));
    replay(internalNiftyNode);

    inputHandler.pointerEvent(eventBus, makePointerEvent(150, 150));
  }

  private void prepareEventBusMock() {
    eventBus.publish(isA(NiftyMouseEnterNodeEvent.class));
    eventBus.publish(isA(NiftyMouseHoverEvent.class));
    replay(eventBus);
  }

  private void prepareMock(final int x, final int y) {
    expect(internalNiftyNode.getNiftyNode()).andReturn(niftyNode).anyTimes();
    expect(internalNiftyNode.getWidth()).andReturn(100);
    expect(internalNiftyNode.getHeight()).andReturn(100);
    expect(internalNiftyNode.screenToLocal(x, y)).andReturn(new Vec4(x, y, 0.f, 1.f));
    expect(internalNiftyNode.getId()).andReturn(12).anyTimes();
    replay(internalNiftyNode);
  }

  private NiftyPointerEvent makePointerEvent(final int x, final int y) {
    NiftyPointerEvent event = new NiftyPointerEvent();
    event.setX(x);
    event.setY(y);
    return event;
  }

  private void execTest(final int x, final int y) {
    inputHandler.pointerEvent(eventBus, makePointerEvent(x, y));
  }
}
