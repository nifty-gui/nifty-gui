package de.lessvoid.nifty.render;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.lessvoid.nifty.spi.render.MouseCursor;
import de.lessvoid.nifty.spi.render.RenderDevice;
import de.lessvoid.nifty.spi.time.impl.AccurateTimeProvider;

public class NiftyMouseImplTest {
  private NiftyMouseImpl niftyMouse;
  private RenderDevice renderDeviceMock;
  private MouseCursor mouseCursor = new TestMouseCursor();

  @Before
  public void before() {
    renderDeviceMock = createMock(RenderDevice.class);
    niftyMouse = new NiftyMouseImpl(renderDeviceMock, null, new AccurateTimeProvider());
  }

  @After
  public void verifyMock() {
    verify(renderDeviceMock);
  }

  @Test
  public void testGetCurrentMouseCursorDefaults() {
    replay(renderDeviceMock);
    assertNull(niftyMouse.getCurrentId());
  }

  @Test
  public void testRegisterMouseCursor() throws IOException {
    expect(renderDeviceMock.createMouseCursor("test", 10, 20)).andReturn(mouseCursor);
    replay(renderDeviceMock);

    niftyMouse.registerMouseCursor("id", "test", 10, 20);
  }

  @Test(expected = IOException.class)
  public void testRegisterMouseCursorIOException() throws IOException {
    expect(renderDeviceMock.createMouseCursor("test", 10, 20)).andThrow(new IOException("load error"));
    replay(renderDeviceMock);

    niftyMouse.registerMouseCursor("id", "test", 10, 20);
  }

  @Test
  public void testResetMouseCursor() {
    renderDeviceMock.disableMouseCursor();
    replay(renderDeviceMock);

    niftyMouse.resetMouseCursor();

    assertNull(niftyMouse.getCurrentId());
  }

  @Test
  public void testEnableMouseCursor() throws IOException {
    expect(renderDeviceMock.createMouseCursor("test", 10, 20)).andReturn(mouseCursor);
    renderDeviceMock.enableMouseCursor(mouseCursor);
    replay(renderDeviceMock);

    niftyMouse.registerMouseCursor("id", "test", 10, 20);
    niftyMouse.enableMouseCursor("id");

    assertEquals("id", niftyMouse.getCurrentId());
  }

  @Test
  public void testEnableMouseCursorTwice() throws IOException {
    expect(renderDeviceMock.createMouseCursor("test", 10, 20)).andReturn(mouseCursor);
    renderDeviceMock.enableMouseCursor(mouseCursor); // only called once
    replay(renderDeviceMock);

    niftyMouse.registerMouseCursor("id", "test", 10, 20);
    niftyMouse.enableMouseCursor("id");
    niftyMouse.enableMouseCursor("id");

    assertEquals("id", niftyMouse.getCurrentId());
  }

  @Test
  public void testEnableMouseCursorTwiceWithId() throws IOException {
    expect(renderDeviceMock.createMouseCursor("test", 10, 20)).andReturn(mouseCursor);
    renderDeviceMock.enableMouseCursor(mouseCursor); // only called once
    replay(renderDeviceMock);

    niftyMouse.registerMouseCursor("id", "test", 10, 20);
    niftyMouse.enableMouseCursor("id");
    niftyMouse.enableMouseCursor("id");

    assertEquals("id", niftyMouse.getCurrentId());
  }

  @Test
  public void testEnableMouseCursorAndReset() throws IOException {
    expect(renderDeviceMock.createMouseCursor("test", 10, 20)).andReturn(mouseCursor);
    renderDeviceMock.enableMouseCursor(mouseCursor);
    renderDeviceMock.disableMouseCursor();
    replay(renderDeviceMock);

    niftyMouse.registerMouseCursor("id", "test", 10, 20);
    niftyMouse.enableMouseCursor("id");
    niftyMouse.resetMouseCursor();

    assertNull(niftyMouse.getCurrentId());
  }

  @Test
  public void testEnableMouseCursorNullId() throws IOException {
    renderDeviceMock.disableMouseCursor();
    replay(renderDeviceMock);

    niftyMouse.enableMouseCursor((String) null);

    assertNull(niftyMouse.getCurrentId());
  }

  public class TestMouseCursor implements MouseCursor {
    @Override
    public void dispose() {
    }
  }
}
