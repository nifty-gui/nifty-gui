package de.lessvoid.nifty.render;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.lessvoid.nifty.spi.render.MouseCursor;
import de.lessvoid.nifty.spi.render.RenderDevice;

public class NiftyMouseImplTest {
  private NiftyMouseImpl niftyMouse;
  private RenderDevice renderDeviceMock;
  private MouseCursor mouseCursor = new TestMouseCursor();

  @Before
  public void before() {
    renderDeviceMock = createMock(RenderDevice.class);
    niftyMouse = new NiftyMouseImpl(renderDeviceMock, null);
  }

  @After
  public void verifyMock() {
    verify(renderDeviceMock);
  }

  @Test
  public void testRegisterMouseCursor() throws IOException {
    expect(renderDeviceMock.createMouseCursor("test", 10, 20)).andReturn(mouseCursor);
    replay(renderDeviceMock);

    assertEquals(mouseCursor, niftyMouse.registerMouseCursor("test", 10, 20));
  }

  @Test(expected = IOException.class)
  public void testRegisterMouseCursorIOException() throws IOException {
    expect(renderDeviceMock.createMouseCursor("test", 10, 20)).andThrow(new IOException("load error"));
    replay(renderDeviceMock);

    niftyMouse.registerMouseCursor("test", 10, 20);
  }

  @Test
  public void testResetMouseCursor() {
    renderDeviceMock.disableMouseCursor();
    replay(renderDeviceMock);

    niftyMouse.resetMouseCursor();
  }

  @Test
  public void testEnableMouseCursor() throws IOException {
    expect(renderDeviceMock.createMouseCursor("test", 10, 20)).andReturn(mouseCursor);
    renderDeviceMock.enableMouseCursor(mouseCursor);
    replay(renderDeviceMock);

    niftyMouse.enableMouseCursor(niftyMouse.registerMouseCursor("test", 10, 20));
  }

  public class TestMouseCursor implements MouseCursor {
    @Override
    public void dispose() {
    }
  }
}
