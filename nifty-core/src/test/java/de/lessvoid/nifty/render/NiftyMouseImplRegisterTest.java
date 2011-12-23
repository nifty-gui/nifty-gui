package de.lessvoid.nifty.render;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.lessvoid.nifty.spi.render.MouseCursor;
import de.lessvoid.nifty.spi.render.RenderDevice;
import de.lessvoid.nifty.spi.time.impl.AccurateTimeProvider;

public class NiftyMouseImplRegisterTest {
  private NiftyMouseImpl niftyMouse;
  private RenderDevice renderDeviceMock;
  private MouseCursor mouseCursor;

  @Before
  public void before() {
    renderDeviceMock = createMock(RenderDevice.class);
    niftyMouse = new NiftyMouseImpl(renderDeviceMock, null, new AccurateTimeProvider());
    mouseCursor = createMock(MouseCursor.class);
  }

  @After
  public void verifyMock() {
    verify(renderDeviceMock);
    verify(mouseCursor);
  }

  @Test
  public void testRegisterMouseCursor() throws IOException {
    replay(mouseCursor);

    expect(renderDeviceMock.createMouseCursor("test", 10, 20)).andReturn(mouseCursor);
    renderDeviceMock.enableMouseCursor(mouseCursor);
    replay(renderDeviceMock);

    niftyMouse.registerMouseCursor("id", "test", 10, 20);
    niftyMouse.enableMouseCursor("id");
  }

  @Test
  public void testUnregister() throws IOException {
    mouseCursor.dispose();
    replay(mouseCursor);

    expect(renderDeviceMock.createMouseCursor("test", 10, 20)).andReturn(mouseCursor);
    replay(renderDeviceMock);

    niftyMouse.registerMouseCursor("id", "test", 10, 20);
    niftyMouse.unregisterAll();
  }
}
