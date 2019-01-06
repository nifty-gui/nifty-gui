package de.lessvoid.nifty.elements;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.FocusHandler;
import de.lessvoid.nifty.elements.events.ElementDisableEvent;
import de.lessvoid.nifty.elements.events.ElementEnableEvent;
import de.lessvoid.nifty.elements.render.ElementRenderer;
import de.lessvoid.nifty.loaderv2.types.ElementType;
import de.lessvoid.nifty.spi.time.TimeProvider;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.createMockBuilder;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ElementEnableDisableTest {
  private Nifty niftyMock;
  private Element e1;
  private Element e2;
  private FocusHandler focusHandler = new FocusHandler();

  @Before
  public void before() throws Exception {
    niftyMock = createMock(Nifty.class);
    expect(niftyMock.getAlternateKey()).andReturn(null).times(2);
  }

  @After
  public void after() {
    verify(niftyMock);
    verify(e1);
    verify(e2);
  }

  @Test
  public void testSimpleDisable() throws Exception {
    niftyMock.publishEvent(eq("e1"), isA(ElementDisableEvent.class));
    replay(niftyMock);
    setupMocks();

    e2.disableFocus();
    replay(e1);
    replay(e2);

    e2.disable();

    assertTrue(e1.isEnabled());
    assertFalse(e2.isEnabled());
  }

  @Test
  public void testDisableTwice() throws Exception {
    niftyMock.publishEvent(eq("e1"), isA(ElementDisableEvent.class));
    niftyMock.publishEvent(eq("e1"), isA(ElementEnableEvent.class));
    replay(niftyMock);
    setupMocks();

    e2.disableFocus();
    replay(e1);
    replay(e2);

    e2.disable();
    e2.disable();

    assertTrue(e1.isEnabled());
    assertFalse(e2.isEnabled());

    e2.enable();
    assertTrue(e2.isEnabled());
  }

  @Test
  public void testDisableTwiceAndThenEnable() throws Exception {
    niftyMock.publishEvent(eq("e1"), isA(ElementDisableEvent.class));
    niftyMock.publishEvent(eq("e1"), isA(ElementEnableEvent.class));
    replay(niftyMock);
    setupMocks();

    e2.disableFocus();
    replay(e1);
    replay(e2);

    e2.disable();
    e2.disable();
    e2.enable();

    assertTrue(e1.isEnabled());
    assertTrue(e2.isEnabled());
  }

  @Test
  public void testSimpleEnable() throws Exception {
    replay(niftyMock);
    setupMocks();

    replay(e1);
    replay(e2);

    e2.enable();

    assertTrue(e1.isEnabled());
    assertTrue(e2.isEnabled());
  }

  @Test
  public void testEnableTwice() throws Exception {
    replay(niftyMock);
    setupMocks();

    replay(e1);
    replay(e2);

    e2.enable();
    e2.enable();

    assertTrue(e1.isEnabled());
    assertTrue(e2.isEnabled());
  }

  @Test
  public void testEnableTwiceAndThenDisable() throws Exception {
    niftyMock.publishEvent(eq("e1"), isA(ElementDisableEvent.class));
    replay(niftyMock);
    setupMocks();

    e2.disableFocus();
    replay(e1);
    replay(e2);

    e2.enable();
    e2.enable();
    e2.disable();

    assertTrue(e1.isEnabled());
    assertFalse(e2.isEnabled());
  }

  @Test
  public void testParentDisable() throws Exception {
    niftyMock.publishEvent(eq("e1"), isA(ElementDisableEvent.class));
    niftyMock.publishEvent(eq("e1"), isA(ElementDisableEvent.class));
    replay(niftyMock);
    setupMocks();

    e1.disableFocus();
    e2.disableFocus();
    replay(e1);
    replay(e2);

    e1.disable();

    assertFalse(e1.isEnabled());
    assertFalse(e2.isEnabled());
  }

  @Test
  public void testParentDisableWithDisabledChild() throws Exception {
    niftyMock.publishEvent(eq("e1"), isA(ElementDisableEvent.class));
    niftyMock.publishEvent(eq("e1"), isA(ElementDisableEvent.class));
    replay(niftyMock);
    setupMocks();

    e1.disableFocus();
    e2.disableFocus();
    replay(e1);
    replay(e2);

    e2.disable();
    e1.disable();

    assertFalse(e1.isEnabled());
    assertFalse(e2.isEnabled());
  }

  @Test
  public void testParentDisableWithDisabledChildAndParentEnable() throws Exception {
    niftyMock.publishEvent(eq("e1"), isA(ElementDisableEvent.class));
    niftyMock.publishEvent(eq("e1"), isA(ElementDisableEvent.class));
    niftyMock.publishEvent(eq("e1"), isA(ElementEnableEvent.class));
    niftyMock.publishEvent(eq("e1"), isA(ElementEnableEvent.class));
    replay(niftyMock);
    setupMocks();

    e1.disableFocus();
    e2.disableFocus();
    replay(e1);
    replay(e2);

    e2.disable();
    e1.disable();
    e1.enable();

    assertTrue(e1.isEnabled());
    assertFalse(e2.isEnabled());

    e2.enable();
    assertTrue(e2.isEnabled());
  }

  private void setupMocks() throws NoSuchMethodException {
    e1 = createMockBuilder(Element.class).withConstructor(
        Nifty.class, ElementType.class, String.class, Element.class,
        FocusHandler.class, boolean.class, TimeProvider.class, ElementRenderer[].class).withArgs(
        niftyMock, null, "e1", null, focusHandler, false, null, null).createMock();

    e2 = createMockBuilder(Element.class)
        .withConstructor(Nifty.class, ElementType.class, String.class, Element.class, FocusHandler.class, boolean.class, TimeProvider.class, ElementRenderer[].class)
        .withArgs(niftyMock, null, "e1", null, focusHandler, false, null, null)
        .addMockedMethod(Element.class.getMethod("disableFocus"))
        .createMock();

    e1.addChild(e2);
  }
}
