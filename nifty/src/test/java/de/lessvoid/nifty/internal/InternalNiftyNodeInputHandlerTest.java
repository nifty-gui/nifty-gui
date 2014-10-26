package de.lessvoid.nifty.internal;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.lessvoid.nifty.api.input.NiftyPointerEvent;
import de.lessvoid.nifty.internal.math.Vec4;

public class InternalNiftyNodeInputHandlerTest {
  private InternalNiftyNodeInputHandler inputHandler;
  private InternalNiftyNode niftyNode;

  @Before
  public void before() {
    niftyNode = createMock(InternalNiftyNode.class);
    inputHandler = new InternalNiftyNodeInputHandler(niftyNode);
  }

  @After
  public void after() {
    verify(niftyNode);
  }

  @Test
  public void testInside() {
    prepareMock(50, 50);
    execTest(50, 50);
  }

  @Test
  public void testInsideBorderLeft() {
    prepareMock(0, 50);
    execTest(0, 50);
  }

  @Test
  public void testInsideBorderRight() {
    prepareMock(100, 50);
    execTest(100, 50);
  }

  @Test
  public void testInsideBorderTop() {
    prepareMock(100, 0);
    execTest(100, 0);
  }

  @Test
  public void testInsideBorderBottom() {
    prepareMock(100, 100);
    execTest(100, 100);
  }

  @Test
  public void testOutside() {
    expect(niftyNode.getWidth()).andReturn(100);
    expect(niftyNode.screenToLocal(150, 150)).andReturn(new Vec4(150.f, 150.f, 0.f, 1.f));
    replay(niftyNode);

    inputHandler.pointerEvent(makePointerEvent(150, 150));
  }

  private void prepareMock(final int x, final int y) {
    expect(niftyNode.getWidth()).andReturn(100);
    expect(niftyNode.getHeight()).andReturn(100);
    expect(niftyNode.screenToLocal(x, y)).andReturn(new Vec4(x, y, 0.f, 1.f));
    expect(niftyNode.getId()).andReturn(12);
    replay(niftyNode);
  }

  private NiftyPointerEvent makePointerEvent(final int x, final int y) {
    NiftyPointerEvent event = new NiftyPointerEvent();
    event.setX(x);
    event.setY(y);
    return event;
  }

  private void execTest(final int x, final int y) {
    inputHandler.pointerEvent(makePointerEvent(x, y));
  }
}
