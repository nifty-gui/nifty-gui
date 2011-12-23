package de.lessvoid.nifty.render;

import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.lessvoid.nifty.spi.input.InputSystem;
import de.lessvoid.nifty.spi.time.impl.AccurateTimeProvider;

public class NiftyMouseImplSetPositionTest {
  private NiftyMouseImpl niftyMouse;
  private InputSystem inputSystemMock;

  @Before
  public void before() {
    inputSystemMock = createMock(InputSystem.class);
    niftyMouse = new NiftyMouseImpl(null, inputSystemMock, new AccurateTimeProvider());
  }

  @After
  public void verifyMock() {
    verify(inputSystemMock);
  }

  @Test
  public void testSetPosition() {
    inputSystemMock.setMousePosition(100, 200);
    replay(inputSystemMock);

    niftyMouse.setMousePosition(100, 200);
  }
}
